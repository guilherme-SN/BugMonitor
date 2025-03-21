package br.com.ccm.api.bugmonitor.scheduler;

import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.service.BugService;
import br.com.ccm.api.bugmonitor.service.NotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchedulerNotionService {
    private final BugRepository bugRepository;
    private final NotionService notionService;
    private final BugService bugService;

    public void retrieveNotionDatabase() {
        notionService.getDatabase()
                .subscribe(this::processNotionDatabase);
    }

    private void processNotionDatabase(NotionResponse notionResponse) {
        for (NotionPage notionPage : notionResponse.results()) {
            processNotionPage(notionPage);
        }
    }

    private void processNotionPage(NotionPage notionPage) {
        Optional<Bug> bugOptional = bugRepository.findById(notionPage.properties().id().uniqueId().number());

        if (bugOptional.isEmpty()) {
            bugService.saveFromNotionPage(notionPage);
        } else {
            checkForUpdatesOnPage(notionPage, bugOptional.get());
        }
    }

    private void checkForUpdatesOnPage(NotionPage notionPage, Bug savedBug) {
    }
}
