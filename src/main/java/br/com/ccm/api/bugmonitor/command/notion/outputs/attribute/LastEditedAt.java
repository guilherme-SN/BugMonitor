package br.com.ccm.api.bugmonitor.command.notion.outputs.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LastEditedAt(
        @JsonProperty("last_edited_time")
        LocalDateTime lastEditedAt
) {
}
