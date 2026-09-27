package com.bhavesh16281.ticket_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TicketController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Ticket service is running";
    }
}
