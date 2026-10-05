package com.bhavesh16281.ticket_service.dto;
import java.time.Instant;

import com.bhavesh16281.ticket_service.entity.Ticket;
import com.bhavesh16281.ticket_service.entity.TicketStatus;

public record TicketResponse(
    Long id,
    String subject,
    String description,
    String requesterEmail,
    TicketStatus status,
    String priority,
    String sentiment,
    String intent,
    String suggestedTeam,
    Instant createdAt,
    Instant updatedAt
) {
    
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
            ticket.getId(),
            ticket.getSubject(),
            ticket.getDescription(),
            ticket.getRequesterEmail(),
            ticket.getStatus(),
            ticket.getPriority(),
            ticket.getSentiment(),
            ticket.getIntent(),
            ticket.getSuggestedTeam(),
            ticket.getCreatedAt(),
            ticket.getUpdatedAt()
        );
    }
}
