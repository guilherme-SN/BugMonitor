package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.discord.inputs.SendMessageCommand;
import br.com.ccm.api.bugmonitor.command.discord.inputs.attribute.*;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.model.Customer;
import br.com.ccm.api.bugmonitor.model.User;
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
    @Value("${discord.webhook.id}")
    private String webhookId;

    @Value("${discord.webhook.token}")
    private String webhookToken;

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
                                        new Field("Prioridade", String.valueOf(bug.getPriority()), true),
                                        new Field("Status", bug.getTaskStatus(), true),
                                        new Field("Responsáveis - Backend",
                                                bug.getBackendResponsibles().stream()
                                                        .map(User::getName)
                                                        .collect(Collectors.joining(", ")),
                                                false),
                                        new Field("Responsáveis - Frontend",
                                                bug.getFrontendResponsibles().stream()
                                                        .map(User::getName)
                                                        .collect(Collectors.joining(", ")),
                                                false),
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
