package com.decoupigny.easywork.models.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
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

    public Ticket(String owner,String ownerName, String title, String status, String reference, String creationDate, String endDate, String description, Participant[] participants, Commentary[] commentaries) {
        this.title = title;
        this.owner = owner;
        this.ownerName = ownerName;
        this.status = status;
        this.reference = reference;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.description = description;
        this.participants = participants;
        this.commentaries = commentaries;
    }

    public Ticket(String id, String owner,String ownerName, String title, String status, String reference, String creationDate, String endDate, String description, Participant[] participants, Commentary[] commentaries) {
        this.id = id;
        this.title = title;
        this.owner = owner;
        this.ownerName = ownerName;
        this.status = status;
        this.reference = reference;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.description = description;
        this.participants = participants;
        this.commentaries = commentaries;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", title=" + title + ", desc=" + description + "]";
    }

}
