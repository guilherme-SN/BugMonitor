package br.com.ccm.api.bugmonitor.command.notion.outputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NotionResponse(
        List<Result> results
) {
}
