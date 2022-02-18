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

    public List<Ticket> findByTitleContaining(String title){
        return ticketRepository.findByTitleContaining(title);
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
        }

        return ticket;
    }

    private String checkStatus(Ticket ticket){
        if(ticket.getParticipants() == null ){
            return "En attente";
        }

        int participantNumber = ticket.getParticipants().length;
        if(participantNumber == 0) return "En attente";

        final int approvedNumber = (int) Arrays.stream(ticket.getParticipants()).filter(participant -> participant.getStatus().equals("Validé") ).count();
        final int refusedNumber = (int) Arrays.stream(ticket.getParticipants()).filter(participant -> participant.getStatus().equals("Refusé") ).count();

        if(participantNumber == approvedNumber) return "Validé";
        if(participantNumber == refusedNumber) return "Refusé";

        return "En attente";
    }


}
