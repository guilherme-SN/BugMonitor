package br.com.ccm.api.bugmonitor.enums;

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

    public static EReporter fromString(String originalName) {
        return Arrays.stream(values())
                .filter(reporter -> reporter.originalName.equalsIgnoreCase(originalName))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
