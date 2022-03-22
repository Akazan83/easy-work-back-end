package com.decoupigny.easywork.models.ticket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void getTitle() {
        Ticket ticket = new Ticket();
        ticket.setTitle("test");
        assertEquals(ticket.getTitle(),"test");
    }

    @Test
    void setTitle() {
    }
}