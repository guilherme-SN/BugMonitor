package br.com.ccm.api.bugmonitor.util;

import br.com.ccm.api.bugmonitor.command.discord.inputs.SendMessageCommand;
import br.com.ccm.api.bugmonitor.command.discord.inputs.attribute.*;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.model.Customer;

import java.util.List;
import java.util.stream.Collectors;

public class DiscordMessageFactory {
    public static SendMessageCommand createBugCreatedMessage(Bug bug) {
        return new SendMessageCommand(
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
    }

    public static SendMessageCommand createBugCompletedMessage(Bug bug) {
        return new SendMessageCommand(
                List.of(
                        new Embed(
                                "\u2705 [BUG RESOLVIDO] \u2705",
                                65280,
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
    }
}
