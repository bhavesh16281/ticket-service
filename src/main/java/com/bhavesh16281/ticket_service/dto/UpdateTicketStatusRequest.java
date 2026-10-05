package com.bhavesh16281.ticket_service.dto;

import com.bhavesh16281.ticket_service.entity.TicketStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusRequest(

    @NotNull(message = "Status must not be null")
    TicketStatus status
) {
    
}
