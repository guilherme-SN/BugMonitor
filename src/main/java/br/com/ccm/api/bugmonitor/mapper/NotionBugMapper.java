package br.com.ccm.api.bugmonitor.mapper;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.enums.EBugNotificationStatus;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.util.BugStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotionBugMapper {
    private final NotionPagePropertiesExtractor propertiesExtractor;

    public Long extractBugId(NotionPage notionPage) {
        return propertiesExtractor.extractCcmId(notionPage);
    }

    public Bug createFromNotionPage(NotionPage notionPage) {
        Bug bug = propertiesExtractor.extractBugFromNotionPage(notionPage);

        if (isReadyToNotify(bug)) {
            bug.setNotificationStatus(EBugNotificationStatus.READY);
        } else {
            bug.setNotificationStatus(EBugNotificationStatus.NOT_READY);
        }

        if (BugStatus.isCompleted(bug.getTaskStatus())) bug.setCompletedAt(bug.getLastEditedAt());

        return bug;
    }

    public boolean isReadyToNotify(Bug bug) {
        return bug.getName() != null
                && !bug.getImpactedCustomers().isEmpty()
                && bug.getPriority() != null
                && bug.getTaskStatus() != null
                && bug.getCreatedBy() != null;
    }
}
