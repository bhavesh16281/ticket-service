package com.bhavesh16281.ticket_service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.time.Duration;

@Component 
public class ClassifierClient {

    private final WebClient webClient;
    private final Duration timeout;
    private static final Logger log = LoggerFactory.getLogger(ClassifierClient.class);

    public ClassifierClient(WebClient webClient, @Value("${app.classifier-service.timeout:5000}") long timeoutMillis) {
        this.webClient = webClient;
        this.timeout = Duration.ofMillis(timeoutMillis); // Example timeout value, can be configured as needed
    }

    @CircuitBreaker(name = "classifierService")
    @Retry(name = "classifierService", fallbackMethod = "classifyFallback")
    public TicketClassification classify(String subject, String description) {

        return webClient.post()
                .uri("api/v1/classify")
                .bodyValue(new ClassifyRequest(subject, description))
                .retrieve()
                .bodyToMono(TicketClassification.class)
                .block();
    }

    @SuppressWarnings("unused")
    private TicketClassification classifyFallback(String subject, String description, Throwable throwable) {
        log.error("Classifier service failed, returning unclassified ticket", throwable);
        return TicketClassification.unclassified();
    }
}
