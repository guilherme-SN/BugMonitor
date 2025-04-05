package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.inputs.FilterDatabaseCommand;
import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Filter;
import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Select;
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

    private final WebClient notionWebClient;

    public Mono<NotionResponse> getDatabase() {
        FilterDatabaseCommand notionRequest = new FilterDatabaseCommand(new Filter("Tipo", new Select("Bug")));
        return notionWebClient.post()
                .uri("/databases/{databaseId}/query", notionDatabaseId)
                .body(Mono.just(notionRequest), FilterDatabaseCommand.class)
                .retrieve()
                .bodyToMono(NotionResponse.class);
    }
}
