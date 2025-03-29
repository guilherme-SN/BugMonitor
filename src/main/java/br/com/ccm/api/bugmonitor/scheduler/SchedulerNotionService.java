package br.com.ccm.api.bugmonitor.scheduler;

import br.com.ccm.api.bugmonitor.service.BugService;
import br.com.ccm.api.bugmonitor.service.NotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerNotionService {
    private final NotionService notionService;
    private final BugService bugService;

    public void retrieveNotionDatabase() {
        notionService.getDatabase().subscribe(bugService::processNotionDatabase);
    }
}
