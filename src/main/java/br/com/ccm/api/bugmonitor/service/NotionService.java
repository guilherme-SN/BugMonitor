package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.inputs.FilterDatabaseCommand;
import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Condition;
import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Filter;
import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Sort;
import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
import br.com.ccm.api.bugmonitor.mapper.NotionPagePropertiesExtractor;
import br.com.ccm.api.bugmonitor.util.BugStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotionService {
    private final WebClient notionWebClient;
    private final NotionPagePropertiesExtractor propertiesExtractor;

    @Value("${notion.database.id}")
    private String notionDatabaseId;
    private LocalDate bugSearchStartDate;

    public Flux<NotionResponse> getAllBugsFromStartDate() {
        List<Condition> conditions = new ArrayList<>();

        Map<String, Object> multiSelectFilter = Map.of("multi_select", Map.of("contains", "Bug"));
        conditions.add(new Condition("Tipo", multiSelectFilter));

        if (this.bugSearchStartDate != null) {
            Map<String, Object> startDateFilter = Map.of("date", Map.of("on_or_after", bugSearchStartDate.toString()));
            conditions.add(new Condition("Created time", startDateFilter));
        }

        FilterDatabaseCommand notionRequest = FilterDatabaseCommand.builder()
                .filter(new Filter(conditions))
                .build();

        return getPage(notionRequest, null)
                .expand(response -> {
                    if (Boolean.TRUE.equals(response.hasMore()) && response.nextCursor() != null) {
                        return getPage(notionRequest, response.nextCursor());
                    }

                    return Mono.empty();
                });
    }

    private Mono<NotionResponse> getPage(FilterDatabaseCommand notionRequest, String startCursor) {
        notionRequest.setStartCursor(startCursor);

        return notionWebClient.post()
                .uri("/databases/{databaseId}/query", notionDatabaseId)
                .body(Mono.just(notionRequest), FilterDatabaseCommand.class)
                .retrieve()
                .bodyToMono(NotionResponse.class);
    }

    public void updateBugSearchStartDate() {
        getAllOpenBugs()
                .flatMap(response -> Flux.fromIterable(response.notionPages()))
                .next()
                .map(propertiesExtractor::extractCreatedAt)
                .doOnNext(newStartSearchDate -> this.bugSearchStartDate = newStartSearchDate.toLocalDate())
                .doOnError(error -> log.error("Error processing Notion database pages: {}", error.getMessage()))
                .subscribe();
    }

    private Flux<NotionResponse> getAllOpenBugs() {
        Map<String, Object> multiSelectFilter = Map.of("multi_select", Map.of("contains", "Bug"));
        Map<String, Object> selectFilterDoesNotEqualDone = Map.of("select", Map.of("does_not_equal", BugStatus.STATUS_DONE));
        Map<String, Object> selectFilterDoesNotEqualFinalizada = Map.of("select", Map.of("does_not_equal", BugStatus.STATUS_FINALIZADA));

        FilterDatabaseCommand notionRequest = FilterDatabaseCommand.builder()
                .filter(new Filter(
                        List.of(
                                new Condition("Tipo", multiSelectFilter),
                                new Condition("Status da Task", selectFilterDoesNotEqualDone),
                                new Condition("Status da Task", selectFilterDoesNotEqualFinalizada)
                        )
                ))
                .sorts(List.of(new Sort("Created time", "ascending")))
                .build();

        return getPage(notionRequest, null)
                .expand(response -> {
                    if (Boolean.TRUE.equals(response.hasMore()) && response.nextCursor() != null) {
                        return getPage(notionRequest, response.nextCursor());
                    }

                    return Mono.empty();
                });
    }
}
