package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.util.NotionPageChangeDetector;
import br.com.ccm.api.bugmonitor.util.NotionPagePropertiesExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BugService {
    private final NotionPagePropertiesExtractor propertiesExtractor;
    private final NotionPageChangeDetector changeDetector;
    private final DiscordNotificationService discordNotificationService;
    private final BugRepository bugRepository;

    public void processNotionDatabase(NotionResponse notionResponse) {
        notionResponse.notionPages().forEach(this::processNotionPage);
    }

    private void processNotionPage(NotionPage notionPage) {
        Optional<Bug> bugOptional = bugRepository.findById(notionPage.properties().pageId().uniqueId().number());

        if (bugOptional.isEmpty()) {
            saveNewBug(notionPage);
        } else {
            updateBugIfChanged(notionPage, bugOptional.get());
        }
    }

    private void saveNewBug(NotionPage notionPage) {
        Bug bug = propertiesExtractor.extractBugFromNotionPage(notionPage);
        bugRepository.save(bug);
        //discordNotificationService.notifyNewBug(bug);
    }

    private void updateBugIfChanged(NotionPage notionPage, Bug existingBug) {
        if (changeDetector.isBugUpdated(notionPage, existingBug)) {
            Bug updatedBug = propertiesExtractor.extractBugFromNotionPage(notionPage);
            updatedBug.setCcmId(existingBug.getCcmId());
            updatedBug.setLastEditedAt(changeDetector.resolveLastEditedTimestamp(existingBug, updatedBug));

            bugRepository.save(updatedBug);

            if (changeDetector.isBugNowCompleted(existingBug, updatedBug)) {
                // TODO: send discord notification with COMPLETED template in GENERAL
            } else if (changeDetector.isTaskStatusUpdated(existingBug, updatedBug)) {
                // TODO: send discord notification with STATUS_CHANGED template in GENERAL
            }

            if (changeDetector.isQaStatusUpdated(existingBug, updatedBug)) {
                // TODO: send discord notification with STATUS_CHANGED template in QA_CHANNEL
            }

            if (changeDetector.isBackendStatusUpdated(existingBug, updatedBug)) {
                // TODO: send discord notification with STATUS_CHANGED template in BACKEND_CHANNEL
            }

            if (changeDetector.isFrontendStatusUpdated(existingBug, updatedBug)) {
                // TODO: send discord notification with STATUS_CHANGED template in FRONTEND_CHANNEL
            }
        }
    }

    public void checkForDeletedPages(NotionResponse notionResponse) {
        Set<Long> bugsToBeDeleted = bugRepository.findAllIds();

        for (NotionPage notionPage : notionResponse.notionPages()) {
            bugsToBeDeleted.remove(propertiesExtractor.extractCcmId(notionPage));
        }

        deleteBugs(bugsToBeDeleted);
    }

    private void deleteBugs(Set<Long> bugsToBeDeleted) {
        for (Long bugId : bugsToBeDeleted) {
            bugRepository.deleteById(bugId);
        }
    }
}
