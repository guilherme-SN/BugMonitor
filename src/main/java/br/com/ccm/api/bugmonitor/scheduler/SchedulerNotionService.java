package br.com.ccm.api.bugmonitor.scheduler;

import br.com.ccm.api.bugmonitor.service.BugService;
import br.com.ccm.api.bugmonitor.service.NotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerNotionService {
    private final NotionService notionService;
    private final BugService bugService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void retrieveNotionDatabase() {
        notionService.getAllBugsFromStartDate()
                .doOnNext(bugService::processNotionDatabase)
                .doOnError(error -> log.error("Error processing Notion database pages: {}", error.getMessage()))
                .onErrorResume(error -> Mono.empty())
                .subscribe();
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    public void updateBugSearchStartDate() {
        notionService.updateBugSearchStartDate();
    }
}
