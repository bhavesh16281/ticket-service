package com.bhavesh16281.ticket_service.client;

public record TicketClassification(
    String intent,
    String sentiment,
    String priority,
    String supportTeam,
    String suggestion
){}