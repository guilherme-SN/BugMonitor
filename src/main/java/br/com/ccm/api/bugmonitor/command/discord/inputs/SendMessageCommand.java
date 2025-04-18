package br.com.ccm.api.bugmonitor.command.discord.inputs;

import br.com.ccm.api.bugmonitor.command.discord.inputs.attribute.Embed;
import br.com.ccm.api.bugmonitor.command.discord.inputs.attribute.MessageComponent;

import java.util.List;

public record SendMessageCommand(
        List<Embed> embeds,
        List<MessageComponent> components
) {
}
