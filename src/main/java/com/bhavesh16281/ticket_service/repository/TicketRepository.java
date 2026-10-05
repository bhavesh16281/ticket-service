package com.bhavesh16281.ticket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhavesh16281.ticket_service.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
}
