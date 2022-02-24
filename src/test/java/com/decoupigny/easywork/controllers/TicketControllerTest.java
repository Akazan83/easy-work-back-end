package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.ticket.Participant;
import com.decoupigny.easywork.models.ticket.Ticket;
import com.decoupigny.easywork.repository.TicketRepository;
import com.decoupigny.easywork.services.TicketService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService ticketService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach()
    public void setup()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mongoTemplate.dropCollection(Ticket.class);
    }

    @Test
    void createTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Titre de test");

        doReturn(ticket).when(repository).save(any());
        Ticket returnedTicket = ticketService.save(ticket);

        Assertions.assertNotNull(returnedTicket, "The saved ticket should not be null");
        Assertions.assertEquals("Titre de test" , returnedTicket.getTitle());
    }

    @Test
    void getAllTickets() {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        doReturn(Arrays.asList(ticket1, ticket2)).when(repository).findAll();

        List<Ticket> tickets = ticketService.findAll();

        Assertions.assertEquals(2, tickets.size(), "findAll should return 2 tickets");
    }

    @Test
    void getTicketById() {
        Ticket ticket = new Ticket();
        ticket.setId("001");
        doReturn(Optional.of(ticket)).when(repository).findById("001");

        Optional<Ticket> returnedWidget = ticketService.findById("001");

        Assertions.assertTrue(returnedWidget.isPresent(), "ticket was not found");
        Assertions.assertSame(returnedWidget.get(), ticket, "The ticket returned was not the same as the mock");
    }

    @Test
    void updateTicket() throws Exception {
        saveOneTicket();

        Participant[] p = new Participant[1];
        p[0] = new Participant();

        String jsonString = new JSONObject()
                .put("id", "001")
                .put("tested", false)
                .put("participant", p)
                .put("name", "Toyota")
                .toString();

        mockMvc.perform(put("/api/ticket/update/001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                        .header("type","test"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTicket() throws Exception {
        saveOneTicket();

        mockMvc.perform(delete("/api/ticket/delete/001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private void saveOneTicket(){
        Ticket ticket1 = new Ticket();
        ticket1.setId("001");
        ticket1.setTitle("truc");
        ticket1.setDescription("Test");
        mongoTemplate.save(ticket1);
    }

}