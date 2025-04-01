package br.com.ccm.api.bugmonitor.scheduler;

import br.com.ccm.api.bugmonitor.service.BugService;
import br.com.ccm.api.bugmonitor.service.NotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerNotionService {
    private final NotionService notionService;
    private final BugService bugService;

    @Scheduled(cron = "*/5 * * * * *")
    public void retrieveNotionDatabase() {
        notionService.getDatabase().subscribe(bugService::processNotionDatabase);
    }
}
