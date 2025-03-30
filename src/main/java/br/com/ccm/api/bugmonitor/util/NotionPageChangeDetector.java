package br.com.ccm.api.bugmonitor.util;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.enums.EResponsibleRole;
import br.com.ccm.api.bugmonitor.model.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotionPageChangeDetector {
    private final NotionPagePropertiesExtractor propertiesExtractor;

    public boolean isBugUpdated(NotionPage notionPage, Bug existingBug) {
        LocalDateTime lastEditedTime = propertiesExtractor.extractLastEditedAt(notionPage);

        return lastEditedTime.isAfter(existingBug.getLastEditedAt());
    }

    public boolean isTaskStatusUpdated(NotionPage notionPage, Bug existingBug) {
        return hasStatusChanged(propertiesExtractor.extractTaskStatus(notionPage), existingBug.getTaskStatus());
    }

    public boolean isQaStatusUpdated(NotionPage notionPage, Bug existingBug) {
        return hasStatusChanged(propertiesExtractor.extractImplementationStatusByRole(notionPage, EResponsibleRole.QA),
                                existingBug.getQaStatus());
    }

    public boolean isBackendStatusUpdated(NotionPage notionPage, Bug existingBug) {
        return hasStatusChanged(propertiesExtractor.extractImplementationStatusByRole(notionPage, EResponsibleRole.BACKEND),
                                existingBug.getBackendStatus());
    }

    public boolean isFrontendStatusUpdated(NotionPage notionPage, Bug existingBug) {
        return hasStatusChanged(propertiesExtractor.extractImplementationStatusByRole(notionPage, EResponsibleRole.FRONTEND),
                                existingBug.getFrontendStatus());
    }

    private boolean hasStatusChanged(String newStatus, String existingStatus) {
        return !newStatus.equalsIgnoreCase(existingStatus);
    }

    public boolean isBugNowCompleted(NotionPage notionPage, Bug existingBug) {
        String newTaskStatus = propertiesExtractor.extractTaskStatus(notionPage);
        String existingTaskStatus = existingBug.getTaskStatus();

        return isStatusCompleted(newTaskStatus) && !isStatusCompleted(existingTaskStatus);
    }

    public LocalDateTime resolveLastEditedTimestamp(NotionPage notionPage, Bug existingBug) {
        String newTaskStatus = propertiesExtractor.extractTaskStatus(notionPage);
        String existingTaskStatus = existingBug.getTaskStatus();

        if (isStatusCompleted(newTaskStatus) && isStatusCompleted(existingTaskStatus)) {
            return existingBug.getLastEditedAt();
        }

        return propertiesExtractor.extractLastEditedAt(notionPage);
    }

    private boolean isStatusCompleted(String status) {
        return status.equalsIgnoreCase("Done") || status.equalsIgnoreCase("Finalizada");
    }
}
