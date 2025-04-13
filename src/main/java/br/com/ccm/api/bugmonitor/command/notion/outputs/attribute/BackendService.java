package br.com.ccm.api.bugmonitor.command.notion.outputs.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BackendService(
        @JsonProperty("rich_text")
        List<RichText> richText
) {
}
