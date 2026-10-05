package com.bhavesh16281.ticket_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(

    @NotBlank(message = "Subject cannot be blank")
    @Size(min = 5, max = 200, message = "Subject must be between 5 and 200 characters")
    String subject,

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 4000, message = "Description must be between 10 and 4000 characters")
    String description,

    @NotBlank(message = "Requester email cannot be blank")
    @Email(message = "Requester email must be a valid email address")
    String requesterEmail
) {
    
}
