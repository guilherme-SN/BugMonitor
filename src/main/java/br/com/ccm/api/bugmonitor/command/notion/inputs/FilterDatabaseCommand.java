package br.com.ccm.api.bugmonitor.command.notion.inputs;

import br.com.ccm.api.bugmonitor.command.notion.inputs.attribute.Filter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterDatabaseCommand {
    private final Filter filter;

    @JsonProperty("start_cursor")
    private String startCursor;
}
