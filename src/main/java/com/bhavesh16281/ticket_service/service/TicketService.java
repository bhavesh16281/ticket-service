package com.bhavesh16281.ticket_service.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bhavesh16281.ticket_service.dto.TicketResponse;
import com.bhavesh16281.ticket_service.dto.CreateTicketRequest;
import com.bhavesh16281.ticket_service.entity.Ticket;
import com.bhavesh16281.ticket_service.exception.ResourceNotFoundException;
import com.bhavesh16281.ticket_service.entity.TicketStatus;
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
        ticket.setStatus(TicketStatus.OPEN);
        
        return TicketResponse.from(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public TicketResponse getById(Long id) {

        return TicketResponse.from(ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    public Page<TicketResponse> getAll(Pageable pageable) {

        return ticketRepository.findAll(pageable).map(TicketResponse::from);
    }

    @Transactional
    public TicketResponse updateStatus(Long id, TicketStatus ticketStatus) {

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        ticket.setStatus(ticketStatus);

        return TicketResponse.from(ticketRepository.save(ticket));
    }

    @Transactional
    public void delete(Long id) {

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        ticketRepository.delete(ticket);
    }

}
