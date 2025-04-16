package br.com.ccm.api.bugmonitor.scheduler;

import br.com.ccm.api.bugmonitor.enums.EBugNotificationStatus;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.service.BugService;
import br.com.ccm.api.bugmonitor.service.DiscordNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class SchedulerDiscordNotificationService {
    private final BugService bugService;
    private final DiscordNotificationService discordNotificationService;

    //@Scheduled(cron = "* */1 * * * *")
    public void sendBugsReadyToBeNotified() {
        Set<Bug> bugsToBeNotified = bugService.getBugsReadyToBeNotified();

        for (Bug bug : bugsToBeNotified) {
            discordNotificationService.notifyNewBug(bug);
            bugService.updateNotificationStatus(bug, EBugNotificationStatus.SENT);
        }
    }
}
