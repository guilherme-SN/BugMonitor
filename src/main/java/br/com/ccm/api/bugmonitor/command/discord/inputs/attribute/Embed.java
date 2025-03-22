package br.com.ccm.api.bugmonitor.command.discord.inputs.attribute;

import java.util.List;

public record Embed(
        String title,
        Integer color,
        List<Field> fields,
        Footer footer
) {
}
