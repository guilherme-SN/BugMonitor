package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.outputs.NotionResponse;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.enums.EBugNotificationStatus;
import br.com.ccm.api.bugmonitor.mapper.NotionBugMapper;
import br.com.ccm.api.bugmonitor.mapper.NotionPagePropertiesExtractor;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.util.NotionPageChangeDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BugService {
    private final NotionBugMapper bugMapper;
    private final NotionPagePropertiesExtractor propertiesExtractor;
    private final NotionPageChangeDetector changeDetector;
    private final BugRepository bugRepository;

    public void processNotionDatabase(NotionResponse notionResponse) {
        notionResponse.notionPages().forEach(this::processNotionPage);
    }

    private void processNotionPage(NotionPage notionPage) {
        Long bugId = bugMapper.extractBugId(notionPage);
        Optional<Bug> bugOptional = bugRepository.findById(bugId);

        if (bugOptional.isEmpty()) {
            createNewBug(notionPage);
        } else {
            updateBugIfChanged(notionPage, bugOptional.get());
        }
    }

    private void createNewBug(NotionPage notionPage) {
        Bug newBug = bugMapper.createFromNotionPage(notionPage);
        bugRepository.save(newBug);
    }

    private void updateBugIfChanged(NotionPage notionPage, Bug existingBug) {
        if (changeDetector.isBugUpdated(notionPage, existingBug)) {
            Bug updatedBug = propertiesExtractor.extractBugFromNotionPage(notionPage);

            updatedBug.setCcmId(existingBug.getCcmId());
            updatedBug.setCompletedAt(determineCompletedTime(existingBug, updatedBug));
            updatedBug.setNotificationStatus(determineNotificationStatus(existingBug.getNotificationStatus(), updatedBug));

            bugRepository.save(updatedBug);
        }
    }

    private LocalDateTime determineCompletedTime(Bug existingBug, Bug updatedBug) {
        if (changeDetector.isBugNowCompleted(existingBug, updatedBug)) return updatedBug.getLastEditedAt();

        if (existingBug.getCompletedAt() != null) return existingBug.getCompletedAt();

        return null;
    }

    private EBugNotificationStatus determineNotificationStatus(EBugNotificationStatus oldStatus, Bug bug) {
        if (oldStatus == EBugNotificationStatus.SENT) return EBugNotificationStatus.SENT;

        if (bugMapper.isReadyToNotify(bug)) return EBugNotificationStatus.READY;

        return EBugNotificationStatus.NOT_READY;
    }

    public Set<Bug> getBugsReadyToBeNotified() {
        return bugRepository.findAllBugsReadyToBeNotified();
    }

    public void updateNotificationStatus(Bug bug, EBugNotificationStatus notificationStatus) {
        bug.setNotificationStatus(notificationStatus);
        bugRepository.save(bug);
    }
}
