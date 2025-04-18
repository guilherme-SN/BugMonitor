package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.discord.inputs.SendMessageCommand;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.util.DiscordMessageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DiscordNotificationService {
    @Value("${discord.channel.general.id}")
    private String generalChannelId;

    @Value("${discord.channel.product.id}")
    private String productChannelId;

    @Value("${discord.channel.qa.id}")
    private String qaChannelId;

    @Value("${discord.channel.backend.id}")
    private String backendChannelId;

    @Value("${discord.channel.frontend.id}")
    private String frontendChannelId;

    private final WebClient discordWebClient;

    public void sendBugCreatedNotification(Bug bug) {
        SendMessageCommand discordRequest = DiscordMessageFactory.createBugCreatedMessage(bug);
        sendNotification(discordRequest, this.generalChannelId);
    }

    public void sendBugCompletedNotification(Bug bug) {
        SendMessageCommand discordRequest = DiscordMessageFactory.createBugCompletedMessage(bug);
        sendNotification(discordRequest, this.generalChannelId);
    }

    public void sendNotification(SendMessageCommand discordRequest, String channelId) {
        discordWebClient.post()
                .uri("/channels/" + channelId + "/messages")
                .body(Mono.just(discordRequest), SendMessageCommand.class)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
