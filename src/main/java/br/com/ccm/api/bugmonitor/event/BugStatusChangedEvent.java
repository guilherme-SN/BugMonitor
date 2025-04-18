package br.com.ccm.api.bugmonitor.event;

import br.com.ccm.api.bugmonitor.enums.EBugStatusSource;
import br.com.ccm.api.bugmonitor.model.Bug;

public record BugStatusChangedEvent(
        Bug oldBug,
        Bug newBug,
        EBugStatusSource source
) {
}
