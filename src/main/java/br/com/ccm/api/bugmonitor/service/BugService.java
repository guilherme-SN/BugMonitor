package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.enums.EBugNotificationStatus;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.util.NotionPageChangeDetector;
import br.com.ccm.api.bugmonitor.util.NotionPagePropertiesExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        bug.setCompletedAt(changeDetector.isStatusCompleted(bug.getTaskStatus()) ? bug.getLastEditedAt() : null);
        bug.setNotificationStatus(EBugNotificationStatus.NOT_READY);
        bugRepository.save(bug);
    }

    private void updateBugIfChanged(NotionPage notionPage, Bug existingBug) {
        if (changeDetector.isBugUpdated(notionPage, existingBug)) {
            Bug updatedBug = propertiesExtractor.extractBugFromNotionPage(notionPage);
            updatedBug.setCcmId(existingBug.getCcmId());
            updatedBug.setCompletedAt(getCompletedAt(existingBug, updatedBug));
            updatedBug.setNotificationStatus(getNewNotificationStatus(existingBug.getNotificationStatus(), updatedBug));

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

    private LocalDateTime getCompletedAt(Bug existingBug, Bug updatedBug) {
        if (changeDetector.isBugNowCompleted(existingBug, updatedBug)) return updatedBug.getLastEditedAt();

        if (existingBug.getCompletedAt() != null) return existingBug.getCompletedAt();

        return null;
    }

    private EBugNotificationStatus getNewNotificationStatus(EBugNotificationStatus oldStatus, Bug bug) {
        if (oldStatus == EBugNotificationStatus.SENT) return EBugNotificationStatus.SENT;

        if (isReadyToNotify(bug)) return EBugNotificationStatus.READY;

        return EBugNotificationStatus.NOT_READY;
    }

    private boolean isReadyToNotify(Bug bug) {
        return bug.getName() != null
                && !bug.getImpactedCustomers().isEmpty()
                && bug.getPriority() != null
                && bug.getTaskStatus() != null
                && bug.getCreatedBy() != null;
    }

    public Set<Bug> getBugsReadyToBeNotified() {
        return bugRepository.findAllBugsReadyToBeNotified();
    }

    public void updateNotificationStatus(Bug bug, EBugNotificationStatus notificationStatus) {
        bug.setNotificationStatus(notificationStatus);
        bugRepository.save(bug);
    }
}
