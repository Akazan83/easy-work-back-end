package com.decoupigny.easywork.repository;

import com.decoupigny.easywork.models.ticket.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByTitleContaining(String title);
    List<Ticket> findByStatus(String status, Pageable pageable);
}
