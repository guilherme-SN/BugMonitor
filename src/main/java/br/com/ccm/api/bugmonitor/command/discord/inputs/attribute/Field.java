package br.com.ccm.api.bugmonitor.command.discord.inputs.attribute;

public record Field(
        String name,
        String value,
        Boolean inline
) {
}
