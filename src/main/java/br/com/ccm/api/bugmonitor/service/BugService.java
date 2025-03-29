package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.util.NotionPageExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BugService {
    private final NotionPageExtractor notionPageExtractor;
    private final DiscordNotificationService discordNotificationService;
    private final BugRepository bugRepository;

    public void saveFromNotionPage(NotionPage notionPage) {
        Bug bug = notionPageExtractor.extractBugFromNotionPage(notionPage);
        discordService.sendNewBugNotification(bug);
        bugRepository.save(bug);
        discordNotificationService.notifyNewBug(bug);
    }

    private void updateBugIfNeeded(NotionPage notionPage, Bug savedBug) {
    }
}
