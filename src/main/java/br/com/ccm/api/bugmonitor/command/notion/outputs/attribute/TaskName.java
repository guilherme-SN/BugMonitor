package br.com.ccm.api.bugmonitor.command.notion.outputs.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskName(
        List<Title> title
) {
}
