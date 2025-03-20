package br.com.ccm.api.bugmonitor.command.notion.outputs;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NotionResponse(
        List<NotionPage> results
) {
}
