package br.com.ccm.api.bugmonitor.command.notion.outputs.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Id(
        @JsonProperty("unique_id")
        UniqueId uniqueId
) {
}
