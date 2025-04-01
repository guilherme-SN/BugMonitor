package br.com.ccm.api.bugmonitor.command.notion.inputs;

import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Filter;

public record FilterDatabaseCommand(
        Filter filter
) {
}
