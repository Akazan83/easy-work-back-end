package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.messenger.Notification;
import com.decoupigny.easywork.models.ticket.Ticket;
import com.decoupigny.easywork.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class TicketController {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTickets(@RequestParam(required = false) String title) {
        try {
            List<Ticket> tickets = new ArrayList<>();

            if (title == null)
                tickets.addAll(ticketRepository.findAll());
            else
                tickets.addAll(ticketRepository.findByTitleContaining(title));

            if (tickets.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable("id") String id) {
        Optional<Ticket> ticketData = ticketRepository.findById(id);

        return ticketData.map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/ticket")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket _ticket = ticketRepository.save(new Ticket(ticket.getOwner(), ticket.getOwnerName(), ticket.getTitle(), ticket.getStatus(), ticket.getReference(), ticket.getCreationDate(), ticket.getEndDate(),
                    ticket.getDescription(), ticket.getParticipants(), ticket.getCommentaries()));

            sendNotificationToParticipants(_ticket,"NewTicket");

            return new ResponseEntity<>(_ticket, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tickets/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable("id") String id, @RequestBody Ticket ticket, @RequestHeader("type") String updateType) {
        Optional<Ticket> ticketData = ticketRepository.findById(id);

        if (ticketData.isPresent()) {
            Ticket _ticket = ticketData.get();
            _ticket.setTitle(ticket.getTitle());
            _ticket.setDescription(ticket.getDescription());
            _ticket.setCreationDate(ticket.getCreationDate());
            _ticket.setOwner(ticket.getOwner());
            _ticket.setStatus(checkStatus(ticket));
            _ticket.setReference(ticket.getReference());
            _ticket.setEndDate(ticket.getEndDate());
            _ticket.setParticipants(ticket.getParticipants());
            _ticket.setCommentaries(ticket.getCommentaries());

            // Send Notification
            switch (updateType) {
                case "addNewParticipant" -> {
                    sendNotificationToParticipants(ticket, "NewTicket");
                }
                case "TicketUpdate" -> {
                    sendNotificationToOwner(ticket, "TicketUpdate");
                }
                default -> {
                }
            }

            return new ResponseEntity<>(ticketRepository.save(_ticket), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("id") String id) {
        try {
            ticketRepository.deleteById(id);

            Optional<Ticket> ticket = ticketRepository.findById(id);
            assert ticket.orElse(null) != null;
            sendNotificationToParticipants(ticket.orElse(null),"NewTicket");

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tickets")
    public ResponseEntity<HttpStatus> deleteAllTickets() {
        try {
            ticketRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tickets/filtered/{status}/{page}")
    public ResponseEntity<List<Ticket>> findByStatus(@PathVariable String status, @PathVariable int page) {
        try {
            List<Ticket> tickets;
            tickets = ticketRepository.findByStatus(status, PageRequest.of(page,9));
            if (tickets.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String checkStatus(Ticket ticket){
        int participantNumber = ticket.getParticipants().length;
        if(participantNumber == 0) return "En attente";

        final int approvedNumber = (int) Arrays.stream(ticket.getParticipants()).filter(participant -> participant.getStatus().equals("Valid??") ).count();
        final int refusedNumber = (int) Arrays.stream(ticket.getParticipants()).filter(participant -> participant.getStatus().equals("Refus??") ).count();

        if(participantNumber == approvedNumber) return "Valid??";
        if(participantNumber == refusedNumber) return "Refus??";

        return "En attente";
    }

    private void sendNotificationToParticipants(Ticket ticket, String type){
        Arrays.stream(ticket.getParticipants()).forEach(participant -> {
            System.out.println(participant.getUserId());
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
