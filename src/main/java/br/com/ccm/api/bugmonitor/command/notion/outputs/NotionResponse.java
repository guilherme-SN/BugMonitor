package br.com.ccm.api.bugmonitor.command.notion.outputs;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NotionResponse(
        @JsonProperty("results")
        List<NotionPage> notionPages
) {
}
