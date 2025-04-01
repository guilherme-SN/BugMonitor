package br.com.ccm.api.bugmonitor.command.notion.inputs.attribute;

public record Filter(
        String property,
        Select select
) {
}
