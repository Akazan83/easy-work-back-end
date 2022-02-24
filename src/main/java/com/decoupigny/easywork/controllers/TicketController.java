package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.notification.Notification;
import com.decoupigny.easywork.models.ticket.Ticket;
import com.decoupigny.easywork.models.dto.TicketDto;
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

@CrossOrigin(origins = "http://localhost:4200")
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
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable("id") String id) {
        Optional<Ticket> ticketData = ticketService.findById(id);

        return ticketData.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketDto ticket) {
        try {
            Ticket newTicket = ticketService.save(ticket.toEntity());

            sendNotificationToParticipants(newTicket,"NewTicket");

            return  ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable("id") String id, @RequestBody TicketDto ticket, @RequestHeader("type") String updateType) {
        Optional<Ticket> ticketData = ticketService.findById(id);

        if (ticketData.isPresent()) {
            Ticket updateTicket = ticketService.updateTicket(id, ticket.toEntity());
            // Send Notification
            switch (updateType) {
                case "addNewParticipant" -> sendNotificationToParticipants(updateTicket, "NewTicket");
                case "TicketApproved" -> sendNotificationToOwner(updateTicket, "TicketApproved");
                case "TicketUpdate" -> sendNotificationToParticipants(updateTicket, "TicketUpdate");
                default -> {
                    logger.error("UpdateType for ticket " + updateTicket.getId() + " was " + updateType);
                }
            }

            return ResponseEntity.ok(ticketService.save(updateTicket));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("id") String id) {
        try {
            Optional<Ticket> ticket = ticketService.findById(id);

            ticketService.deleteById(id);
            assert ticket.orElse(null) != null;
            sendNotificationToParticipants(ticket.orElse(null),"NewTicket");

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAllTickets() {
        try {
            ticketService.deleteAll();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tickets/filtered/{status}/{page}")
    public ResponseEntity<List<Ticket>> findByStatus(@PathVariable String status, @PathVariable int page) {
        try {
            List<Ticket> tickets;
            tickets = ticketService.findByStatus(status, PageRequest.of(page,9));
            if (tickets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
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
