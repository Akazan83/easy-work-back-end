package com.decoupigny.easywork.models.ticket;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Ticket() {

    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Participant[] getParticipants() {
        return participants;
    }

    public void setParticipants(Participant[] participants) {
        this.participants = participants;
    }

    public Commentary[] getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(Commentary[] commentaries) {
        this.commentaries = commentaries;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", title=" + title + ", desc=" + description + "]";
    }

}
