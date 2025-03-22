package br.com.ccm.api.bugmonitor.command.discord.inputs.attribute;

public record Component(
        Integer type,
        Integer style,
        String label,
        String url
) {
}
