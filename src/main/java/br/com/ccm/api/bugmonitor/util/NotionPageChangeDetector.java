package br.com.ccm.api.bugmonitor.util;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.model.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NotionPageChangeDetector {
    private final NotionPagePropertiesExtractor propertiesExtractor;

    public boolean isBugUpdated(NotionPage notionPage, Bug existingBug) {
        LocalDateTime lastEditedTime = propertiesExtractor.extractLastEditedAt(notionPage);
        LocalDateTime oneMinuteAgo = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("America/Sao_Paulo")).minusSeconds(60);

        boolean editedOneMinuteAgo = lastEditedTime.isAfter(oneMinuteAgo);
        boolean editedAfterExisting = lastEditedTime.isAfter(existingBug.getLastEditedAt());

        return editedOneMinuteAgo || editedAfterExisting;
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

    public boolean isStatusCompleted(String status) {
        return Objects.equals(status, "Done") || Objects.equals(status, "Finalizada");
    }
}
