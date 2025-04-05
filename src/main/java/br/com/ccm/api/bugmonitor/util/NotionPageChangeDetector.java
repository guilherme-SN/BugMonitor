package br.com.ccm.api.bugmonitor.util;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.model.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NotionPageChangeDetector {
    private final NotionPagePropertiesExtractor propertiesExtractor;

    public boolean isBugUpdated(NotionPage notionPage, Bug existingBug) {
        LocalDateTime lastEditedTime = propertiesExtractor.extractLastEditedAt(notionPage);

        return lastEditedTime.isEqual(existingBug.getLastEditedAt()) || lastEditedTime.isAfter(existingBug.getLastEditedAt());
    }

    public boolean isTaskStatusUpdated(Bug existingBug, Bug updatedBug) {
        return !Objects.equals(existingBug.getTaskStatus(), updatedBug.getTaskStatus());
    }

    public boolean isQaStatusUpdated(Bug existingBug, Bug updatedBug) {
        return !Objects.equals(existingBug.getQaStatus(), updatedBug.getQaStatus());
    }

    public boolean isBackendStatusUpdated(Bug existingBug, Bug updatedBug) {
        return !Objects.equals(existingBug.getBackendStatus(), updatedBug.getBackendStatus());
    }

    public boolean isFrontendStatusUpdated(Bug existingBug, Bug updatedBug) {
        return !Objects.equals(existingBug.getFrontendStatus(), updatedBug.getFrontendStatus());
    }

    public boolean isBugNowCompleted(Bug existingBug, Bug updatedBug) {
        return isStatusCompleted(updatedBug.getTaskStatus()) && !isStatusCompleted(existingBug.getTaskStatus());
    }

    public LocalDateTime resolveLastEditedTimestamp(Bug existingBug, Bug updatedBug) {
        if (isStatusCompleted(existingBug.getTaskStatus()) && isStatusCompleted(updatedBug.getTaskStatus())) {
            return existingBug.getLastEditedAt();
        }

        return updatedBug.getLastEditedAt();
    }

    private boolean isStatusCompleted(String status) {
        return Objects.equals(status, "Done") || Objects.equals(status, "Finalizada");
    }
}
