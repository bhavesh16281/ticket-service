package com.bhavesh16281.ticket_service.client;

public record TicketClassification(
    String intent,
    String sentiment,
    String priority,
    String supportTeam,
    String suggestion
){
    private static final String UNCLASSIFIED = "UNCLASSIFIED";
    private static final String DEFAULT_PRIORITY = "MEDIUM";
    private static final String DEFAULT_SUPPORT_TEAM = "GENERAL";
    private static final String DEFAULT_SUGGESTION = "NO_SUGGESTION";
    private static final String UNKNOWN = "UNKNOWN";

    public static TicketClassification unclassified() {
        return new TicketClassification(
            UNCLASSIFIED,
            UNKNOWN,
            DEFAULT_PRIORITY,
            DEFAULT_SUPPORT_TEAM,
            DEFAULT_SUGGESTION
        );
    }
}