package com.bhavesh16281.ticket_service.exception;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class ApiError {

    private Instant timestamp;
    private int status;
    private String message;
    private String path;
    
}
