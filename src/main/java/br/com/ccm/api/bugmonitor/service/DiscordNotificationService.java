package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.discord.inputs.SendMessageCommand;
import br.com.ccm.api.bugmonitor.command.discord.inputs.attribute.*;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

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

    public void notifyNewBug(Bug bug) {
        SendMessageCommand discordRequest = new SendMessageCommand(
                "Notificador de Bug - From Notion",
                List.of(
                        new Embed(
                                "\uD83D\uDEA8 [NOVO BUG] \uD83D\uDEA8",
                                16711680,
                                List.of(
                                        new Field("Nome", bug.getName(), false),
                                        new Field("Clientes Impactados",
                                                bug.getImpactedCustomers().stream()
                                                        .map(Customer::getName)
                                                        .collect(Collectors.joining(", ")),
                                                false),
                                        new Field("Prioridade", bug.getPriority(), true),
                                        new Field("Status", bug.getTaskStatus(), true),
                                        new Field("Criado por", bug.getCreatedBy().getName(), false)
                                ),
                                new Footer("Monitoramento Automático")
                        )
                ),
                List.of(
                        new MessageComponent(
                                1,
                                List.of(
                                        new Component(2, 5, "Link do card", bug.getUrl())
                                )
                        )
                )
        );

        discordWebClient.post()
                .uri("/webhooks/" + webhookId + "/" + webhookToken)
                .body(Mono.just(discordRequest), SendMessageCommand.class)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
