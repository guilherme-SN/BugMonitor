package br.com.ccm.api.bugmonitor.event;

import br.com.ccm.api.bugmonitor.model.Bug;

public record BugCompletedEvent(
        Bug bug
) {
}
