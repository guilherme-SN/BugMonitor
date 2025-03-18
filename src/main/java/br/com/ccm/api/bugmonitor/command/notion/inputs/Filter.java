package br.com.ccm.api.bugmonitor.command.notion.inputs;

public record Filter(
        String property,
        Select select
) {
}
