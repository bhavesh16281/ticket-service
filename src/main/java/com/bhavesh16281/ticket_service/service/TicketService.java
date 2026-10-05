package com.bhavesh16281.ticket_service.service;

import org.springframework.stereotype.Service;

import com.bhavesh16281.ticket_service.dto.TicketResponse;
import com.bhavesh16281.ticket_service.dto.CreateTicketRequest;
import com.bhavesh16281.ticket_service.entity.Ticket;
import com.bhavesh16281.ticket_service.repository.TicketRepository;

import org.springframework.transaction.annotation.Transactional;

@Service 
public class TicketService {

    private final TicketRepository ticketRepository;
    
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    
    @Transactional 
    public TicketResponse create(CreateTicketRequest createTicketRequest) {
        
        Ticket ticket = new Ticket();

        ticket.setSubject(createTicketRequest.subject());
        ticket.setDescription(createTicketRequest.description());
        ticket.setRequesterEmail(createTicketRequest.requesterEmail());
        
        return TicketResponse.from(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public TicketResponse getById(Long id) {
        return TicketResponse.from(ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found" + id)));
    }
}
