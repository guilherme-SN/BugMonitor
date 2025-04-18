package br.com.ccm.api.bugmonitor.command.notion.inputs.attribute;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Builder
@RequiredArgsConstructor
public class Condition {
    @JsonProperty("property")
    private final String property;

    private final Map<String, Object> filterType;

    @JsonAnyGetter
    public Map<String, Object> getFilterType() {
        return filterType;
    }
}
