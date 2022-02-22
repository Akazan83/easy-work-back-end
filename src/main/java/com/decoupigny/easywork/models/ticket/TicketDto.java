package com.decoupigny.easywork.models.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class TicketDto {
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

    public Ticket toEntity() {
        Ticket entity = new Ticket();

        entity.setId(this.id);
        entity.setTitle(this.title);
        entity.setOwner(this.owner);
        entity.setStatus(this.status);
        entity.setReference(this.reference);
        entity.setCreationDate(this.creationDate);
        entity.setEndDate(this.endDate);
        entity.setDescription(this.description);
        entity.setParticipants(this.participants);
        entity.setCommentaries(this.commentaries);

        return entity;
    }
}
