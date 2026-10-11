package com.bhavesh16281.ticket_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import com.bhavesh16281.ticket_service.dto.TicketResponse;
import com.bhavesh16281.ticket_service.dto.CreateTicketRequest;
import com.bhavesh16281.ticket_service.service.TicketService;
import com.bhavesh16281.ticket_service.dto.UpdateTicketStatusRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }       

    @PostMapping("/create")
    public ResponseEntity<TicketResponse> create(@RequestBody @Valid CreateTicketRequest createTicketRequest) {

        return ResponseEntity.ok(ticketService.create(createTicketRequest));
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(ticketService.getById(id));
    }

    @GetMapping("/getAll")
    public Page<TicketResponse> getAll(Pageable pageable) {

        return ticketService.getAll(pageable);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<TicketResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateTicketStatusRequest updateTicketStatusRequest) {

        return ResponseEntity.ok(ticketService.updateStatus(id, updateTicketStatusRequest.status()));
    }

    @DeleteMapping("/deleteById/{id}") 
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
