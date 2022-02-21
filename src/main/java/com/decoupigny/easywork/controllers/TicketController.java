package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.notification.Notification;
import com.decoupigny.easywork.models.ticket.Ticket;
import com.decoupigny.easywork.services.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@ApiOperation(value = "Visualize and manage tickets")
@Api(tags = "Ticket")
@RequestMapping("/api/ticket")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    TicketService ticketService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/getAll")
    public ResponseEntity<List<Ticket>> getAllTickets(@RequestParam(required = false) String title) {
        try {
            List<Ticket> tickets = new ArrayList<>();

            if (title == null)
                tickets.addAll(ticketService.findAll());
            else
                tickets.addAll(ticketService.findByTitleContaining(title));

            if (tickets.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable("id") String id) {
        Optional<Ticket> ticketData = ticketService.findById(id);

        return ticketData.map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/new")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket newTicket = ticketService.save(new Ticket(ticket.getOwner(), ticket.getOwnerName(), ticket.getTitle(), ticket.getStatus(), ticket.getReference(), ticket.getCreationDate(), ticket.getEndDate(),
                    ticket.getDescription(), ticket.getParticipants(), ticket.getCommentaries()));

            sendNotificationToParticipants(newTicket,"NewTicket");

            return new ResponseEntity<>(newTicket, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable("id") String id, @RequestBody Ticket ticket, @RequestHeader("type") String updateType) {
        Optional<Ticket> ticketData = ticketService.findById(id);

        if (ticketData.isPresent()) {
            Ticket updateTicket = ticketService.updateTicket(id, ticket);
            // Send Notification
            switch (updateType) {
                case "addNewParticipant" -> sendNotificationToParticipants(ticket, "NewTicket");
                case "TicketApproved" -> sendNotificationToOwner(ticket, "TicketApproved");
                case "TicketUpdate" -> sendNotificationToParticipants(ticket, "TicketUpdate");
                default -> {
                    logger.error("UpdateType for ticket " + updateTicket.getId() + " was " + updateType);
                }
            }

            return new ResponseEntity<>(ticketService.save(updateTicket), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("id") String id) {
        try {
            Optional<Ticket> ticket = ticketService.findById(id);

            ticketService.deleteById(id);
            assert ticket.orElse(null) != null;
            sendNotificationToParticipants(ticket.orElse(null),"NewTicket");

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAllTickets() {
        try {
            ticketService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tickets/filtered/{status}/{page}")
    public ResponseEntity<List<Ticket>> findByStatus(@PathVariable String status, @PathVariable int page) {
        try {
            List<Ticket> tickets;
            tickets = ticketService.findByStatus(status, PageRequest.of(page,9));
            if (tickets.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendNotificationToParticipants(Ticket ticket, String type){
        if(ticket.getParticipants() == null){
            return;
        }
        Arrays.stream(ticket.getParticipants()).forEach(participant -> {
            messagingTemplate.convertAndSendToUser(
                    participant.getUserId(),"/queue/messages",
                    new Notification(
                            ticket.getId(),
                            ticket.getOwner(),
                            ticket.getOwnerName(),
                            type,
                            new Date()));
        });
    }
    private void sendNotificationToOwner(Ticket ticket, String type){
        messagingTemplate.convertAndSendToUser(
                ticket.getOwner(),"/queue/messages",
                new Notification(
                        ticket.getId(),
                        ticket.getOwner(),
                        ticket.getOwnerName(),
                        type,
                        new Date()));
    }
}
