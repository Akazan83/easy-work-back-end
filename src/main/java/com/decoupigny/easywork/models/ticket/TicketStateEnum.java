package com.decoupigny.easywork.models.ticket;

public enum TicketStateEnum {
    WAITING("En attente"),
    REFUSED("Refusé"),
    APPROVED("Validé");

    private String status;

    TicketStateEnum(String status) {
        this.status = status;
    }

}
