package com.decoupigny.easywork.repository;

import com.decoupigny.easywork.models.ticket.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByTitleContaining(String title);
    List<Ticket> findByStatus(boolean status);
}
