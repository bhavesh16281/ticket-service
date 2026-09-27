package com.bhavesh16281.ticket_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.Instant;

import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity 
@Table(name = "tickets")
@Data
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(nullable = false, length = 5000)
    private String description;

    @Column(nullable = false)
    private String requesterEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 50)
    private TicketStatus status = TicketStatus.OPEN;

    //AI derived fields

    @Column(length = 50)
    private String priority;

    @Column(length = 50)
    private String sentiment;

    @Column(length = 50)
    private String intent;

    @Column(length = 50)
    private String suggestedTeam;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist 
    protected void onCreate() {

        Instant now = Instant.now();

        this.createdAt = now;
        this.updatedAt = now;

        if(this.status == null) {
            this.status = TicketStatus.OPEN;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
