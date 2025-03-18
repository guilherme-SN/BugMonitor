package br.com.ccm.api.bugmonitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotionService {
    @Value("${notion.database.id}")
    private String notionDatabaseId;

    private final WebClient webClient;

    public Mono<String> getDatabase() {
        return webClient.post()
                .uri("/databases/{databaseId}/query", notionDatabaseId)
                .retrieve()
                .bodyToMono(String.class);
    }
}
