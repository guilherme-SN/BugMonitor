package br.com.ccm.api.bugmonitor.command.discord.inputs.attribute;

import java.util.List;

public record MessageComponent(
        Integer type,
        List<Component> components
) {
}
