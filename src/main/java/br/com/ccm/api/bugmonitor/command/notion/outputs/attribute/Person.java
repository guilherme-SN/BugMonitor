package br.com.ccm.api.bugmonitor.command.notion.outputs.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Person(
        String email
) {
}
