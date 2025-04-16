package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.inputs.FilterDatabaseCommand;
import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Filter;
import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.MultiSelect;
import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotionService {
    @Value("${notion.database.id}")
    private String notionDatabaseId;

    private final WebClient notionWebClient;

    public Flux<NotionResponse> getAllPages() {
        FilterDatabaseCommand notionRequest = FilterDatabaseCommand.builder()
                .filter(new Filter("Tipo", new MultiSelect("Bug")))
                .build();

        return getPage(notionRequest, null)
                .expand(response -> {
                    if (Boolean.TRUE.equals(response.hasMore()) && response.nextCursor() != null) {
                        return getPage(notionRequest, response.nextCursor());
                    }

                    return Mono.empty();
                });
    }

    public Mono<NotionResponse> getPage(FilterDatabaseCommand notionRequest, String startCursor) {
        notionRequest.setStartCursor(startCursor);

        return notionWebClient.post()
                .uri("/databases/{databaseId}/query", notionDatabaseId)
                .body(Mono.just(notionRequest), FilterDatabaseCommand.class)
                .retrieve()
                .bodyToMono(NotionResponse.class);
    }
}
