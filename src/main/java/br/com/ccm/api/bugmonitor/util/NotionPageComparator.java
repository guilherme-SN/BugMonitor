package br.com.ccm.api.bugmonitor.util;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.model.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotionPageComparator {
    private final NotionPagePropertiesExtractor notionPagePropertiesExtractor;

    public boolean isNotionPageUpdated(NotionPage notionPageRetrieved, Bug existingBug) {
        LocalDateTime lastEditedTime = notionPagePropertiesExtractor.extractLastEditedAt(notionPageRetrieved);

        return (lastEditedTime.isAfter(existingBug.getLastEditedAt()));
    }
}
