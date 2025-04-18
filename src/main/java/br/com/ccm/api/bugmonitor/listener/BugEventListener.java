package br.com.ccm.api.bugmonitor.listener;

import br.com.ccm.api.bugmonitor.event.BugCompletedEvent;
import br.com.ccm.api.bugmonitor.event.BugStatusChangedEvent;
import br.com.ccm.api.bugmonitor.service.DiscordNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BugEventListener {
    private final DiscordNotificationService discordNotificationService;

    @EventListener
    public void handleBugCompletedEvent(BugCompletedEvent event) {
        discordNotificationService.sendBugCompletedNotification(event.bug());
    }

    @EventListener
    public void handleBugStatusChangedEvent(BugStatusChangedEvent event) {
    }
}
