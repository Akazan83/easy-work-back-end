package com.decoupigny.easywork.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;
    private String owner;
    private String ownerName;
    private String title;
    private String status;
    private String reference;
    private String creationDate;
    private String endDate;
    private String description;
    private Participant[] participants;
    private Commentary[] commentaries;
}
