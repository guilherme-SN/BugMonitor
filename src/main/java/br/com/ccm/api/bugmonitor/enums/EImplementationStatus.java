package br.com.ccm.api.bugmonitor.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EImplementationStatus {
    DONE("Pronto"),
    TODO("A fazer"),
    IN_PROGRESS("Fazendo"),
    BLOCKED("Bloqueado"),
    NOT_APPLICABLE("Não se aplica"),
    PAUSED("Pausado"),
    PENDING("Pendente"),
    BUG_FIX("Correção de bugs"),
    UNKNOWN("Unknown");

    private final String originalName;

    @JsonCreator
    public static EImplementationStatus fromString(String originalName) {
        return Arrays.stream(values())
                .filter(implementationStatus -> implementationStatus.originalName.equals(originalName))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
