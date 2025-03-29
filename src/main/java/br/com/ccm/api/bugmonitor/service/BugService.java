package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.util.NotionPageExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BugService {
    private final NotionPageExtractor notionPageExtractor;
    private final DiscordNotificationService discordNotificationService;
    private final BugRepository bugRepository;

    public void processNotionDatabase(NotionResponse notionResponse) {
        notionResponse.notionPages().forEach(this::processNotionPage);
    }

    private void processNotionPage(NotionPage notionPage) {
        Optional<Bug> bugOptional = bugRepository.findById(notionPage.properties().pageId().uniqueId().number());

        if (bugOptional.isEmpty()) {
            createBugFromNotionPage(notionPage);
        } else {
            updateBugIfNeeded(notionPage, bugOptional.get());
        }
    }

    public void createBugFromNotionPage(NotionPage notionPage) {
        Bug bug = notionPageExtractor.extractBugFromNotionPage(notionPage);
        bugRepository.save(bug);
        discordNotificationService.notifyNewBug(bug);
    }

    private void updateBugIfNeeded(NotionPage notionPage, Bug savedBug) {
    }
}
