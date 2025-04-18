package br.com.ccm.api.bugmonitor.command.notion.inputs.attribute;

import java.util.List;

public record Filter(
        List<Condition> and
) {
}
