package com.bhavesh16281.ticket_service.exception;
import java.time.Instant;

import lombok.Data;

@Data
public class ApiError {

    private Instant timestamp;
    private int status;
    private String message;
    private String path;

    public ApiError(Instant timestamp, int status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

}
