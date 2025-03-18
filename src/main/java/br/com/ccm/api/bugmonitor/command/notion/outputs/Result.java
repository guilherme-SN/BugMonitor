package br.com.ccm.api.bugmonitor.command.notion.outputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Result(
        Properties properties
) {
}
