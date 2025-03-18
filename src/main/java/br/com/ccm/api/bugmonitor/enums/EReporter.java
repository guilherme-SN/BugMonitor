package br.com.ccm.api.bugmonitor.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EReporter {
    INTERNAL("Interno"),
    CUSTOMER("Cliente"),
    UNKNOWN("Unknown");

    private final String originalName;

    @JsonCreator
    public static EReporter fromString(String originalName) {
        return Arrays.stream(values())
                .filter(reporter -> reporter.originalName.equals(originalName))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
