package com.bhavesh16281.ticket_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component 
public class ClassifierClient {

    private final WebClient webClient;

    public ClassifierClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public TicketClassification classify(String subject, String description) {

        return webClient.post()
                .uri("api/v1/classify")
                .bodyValue(new ClassifyRequest(subject, description))
                .retrieve()
                .bodyToMono(TicketClassification.class)
                .block();
    }
}
