package br.com.ccm.api.bugmonitor.scheduler;

import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
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

    @Scheduled(fixedDelay = 5000)
    public void retrieveNotionDatabase() {
        NotionResponse response = notionService.getDatabase().block();

        if (response != null) bugService.processNotionDatabase(response);
    }

    @Scheduled(cron = "0 0 */6 * * 1-5")
    public void checkForDeletedPages() {
        notionService.getDatabase().subscribe(bugService::checkForDeletedPages);
    }
}
