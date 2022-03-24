package com.decoupigny.easywork.services;

import com.decoupigny.easywork.models.ticket.Ticket;
import com.decoupigny.easywork.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findAll(){
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(String id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> findByStatus(String status, Pageable pageable){
        return ticketRepository.findByStatus(status, pageable);
    }

    public void deleteById(String id) {
        ticketRepository.deleteById(id);
    }

    public void deleteAll() {
        ticketRepository.deleteAll();
    }

    public Ticket updateTicket(String id, Ticket ticket) {
        Optional<Ticket> ticketData = ticketRepository.findById(id);

        if (ticketData.isPresent()) {
            Ticket updatedTicket = ticketData.get();
            updatedTicket.setTitle(ticket.getTitle());
            updatedTicket.setDescription(ticket.getDescription());
            updatedTicket.setCreationDate(ticket.getCreationDate());
            updatedTicket.setOwner(ticket.getOwner());
            updatedTicket.setStatus(checkStatus(ticket));
            updatedTicket.setReference(ticket.getReference());
            updatedTicket.setEndDate(ticket.getEndDate());
            updatedTicket.setParticipants(ticket.getParticipants());
            updatedTicket.setCommentaries(ticket.getCommentaries());
        }

        return ticket;
    }

    private String checkStatus(Ticket ticket){
        final String WAITING = "En attente";
        final String VALID = "Validé";
        final String REFUSE = "Refusé";

        if(ticket.getParticipants() == null ){
            return WAITING;
        }

        int participantNumber = ticket.getParticipants().length;
        if(participantNumber == 0) return WAITING;

        final int approvedNumber = (int) Arrays.stream(ticket.getParticipants()).filter(participant -> participant.getStatus().equals(VALID) ).count();
        final int refusedNumber = (int) Arrays.stream(ticket.getParticipants()).filter(participant -> participant.getStatus().equals(REFUSE) ).count();

        if(participantNumber == approvedNumber) return VALID;
        if(participantNumber == refusedNumber) return REFUSE;

        return WAITING;
    }


}
