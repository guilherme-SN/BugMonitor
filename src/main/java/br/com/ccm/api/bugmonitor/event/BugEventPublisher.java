package br.com.ccm.api.bugmonitor.event;

import br.com.ccm.api.bugmonitor.enums.EBugStatusSource;
import br.com.ccm.api.bugmonitor.model.Bug;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BugEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishBugCreated(Bug bug) {
        eventPublisher.publishEvent(new BugCreatedEvent(bug));
    }

    public void publishBugCompleted(Bug bug) {
        eventPublisher.publishEvent(new BugCompletedEvent(bug));
    }

    public void publishTaskStatusChanged(Bug oldBug, Bug newBug) {
        eventPublisher.publishEvent(new BugStatusChangedEvent(oldBug, newBug, EBugStatusSource.TASK));
    }

    public void publishProductStatusChanged(Bug oldBug, Bug newBug) {
        eventPublisher.publishEvent(new BugStatusChangedEvent(oldBug, newBug, EBugStatusSource.PRODUCT));
    }

    public void publishQaStatusChanged(Bug oldBug, Bug newBug) {
        eventPublisher.publishEvent(new BugStatusChangedEvent(oldBug, newBug, EBugStatusSource.QA));
    }

    public void publishBackendStatusChanged(Bug oldBug, Bug newBug) {
        eventPublisher.publishEvent(new BugStatusChangedEvent(oldBug, newBug, EBugStatusSource.BACKEND));
    }

    public void publishFrontendStatusChanged(Bug oldBug, Bug newBug) {
        eventPublisher.publishEvent(new BugStatusChangedEvent(oldBug, newBug, EBugStatusSource.FRONTEND));
    }
}
