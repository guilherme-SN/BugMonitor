package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.inputs.Filter;
import br.com.ccm.api.bugmonitor.command.notion.inputs.RetrieveDatabaseCommand;
import br.com.ccm.api.bugmonitor.command.notion.inputs.Select;
import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
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

    public Mono<NotionResponse> getDatabase() {
        RetrieveDatabaseCommand notionRequest = new RetrieveDatabaseCommand(new Filter("Tipo", new Select("Bug")));
        return webClient.post()
                .uri("/databases/{databaseId}/query", notionDatabaseId)
                .body(Mono.just(notionRequest), RetrieveDatabaseCommand.class)
                .retrieve()
                .bodyToMono(NotionResponse.class);
    }
}
