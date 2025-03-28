package br.com.ccm.api.bugmonitor.command.notion.outputs.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NotionProperties(
    @JsonProperty("ID")
    Id pageId,

    @JsonProperty("Prior..")
    Priority priority,

    @JsonProperty("Cliente")
    Customer customers,

    @JsonProperty("Reportado por")
    ReportedBy reportedBy,

    @JsonProperty("Épico")
    Epic epics,

    @JsonProperty("Nome da tarefa")
    TaskName taskName,

    @JsonProperty("Status da Task")
    TaskStatus taskStatus,

    @JsonProperty("Status Front")
    TaskStatus frontendStatus,

    @JsonProperty("Status Back")
    TaskStatus backendStatus,

    @JsonProperty("Status QA")
    TaskStatus qaStatus,

    @JsonProperty("Responsavel Front")
    TaskResponsible frontendResponsible,

    @JsonProperty("Responsável Back")
    TaskResponsible backendResponsible,

    @JsonProperty("Created by")
    CreatedBy createdBy,

    @JsonProperty("Created time")
    CreatedTime createdTime,

    @JsonProperty("Última edição")
    LastEditedAt lastEditedAt
) {
}
