package br.com.ccm.api.bugmonitor.command.notion.inputs.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Filter(
        String property,
        @JsonProperty("multi_select")
        MultiSelect multiSelect
) {
}
