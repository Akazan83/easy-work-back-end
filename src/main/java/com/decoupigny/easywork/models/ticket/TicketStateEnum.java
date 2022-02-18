package com.decoupigny.easywork.models.ticket;

import lombok.Getter;

@Getter
public enum TicketStateEnum {
    WAITING("En attente"),
    REFUSED("Refusé"),
    APPROVED("Validé");

    private final String status;

    TicketStateEnum(String status) {
        this.status = status;
    }
}
