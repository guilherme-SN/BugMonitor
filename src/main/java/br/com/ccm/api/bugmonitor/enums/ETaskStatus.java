package br.com.ccm.api.bugmonitor.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ETaskStatus {
    PROD_PRIORITIZATION("Priorização Roadmap"),
    BENCHMARKING_IDEATION("Benchmarking e Ideação"),
    PRODUCT("Produto"),
    QA_PRE_ANALYSIS("QA - Pré Análise"),
    DEV_READY_FOR_DEVELOPMENT("DEV - Pronto para desenvolvimento"),
    DEV_PRESENTATION("Apresentação Dev"),
    PENDING_DEV_CORRECTION("Correção pendente DEV"),
    IN_DEVELOPMENT("Em Desenvolvimento"),
    DEV_PAUSED("Pausado Dev"),
    BLOCKED_IN_QA("Bloqueado em QA"),
    QA_HML_VERIFICATION("QA - Verificação - HML"),
    APPROVED_HOMOLOG("Aprovado Homolog"),
    DEV_PRE_PROD_DEPLOY("DEV - Deploy Pré-prod"),
    QA_PRE_PROD_EVALUATION("QA - Avaliação Pré-Prod"),
    PRODUCT_PRE_PROD_EVALUATION("Produto - Avaliação Pré-Prod"),
    DEV_DEPLOY("DEV - Deploy"),
    POST_DEPLOY_VALIDATION("Validação Pós-Deploy"),
    ROADMAP_QUEUE("Fila Roadmap"),
    DONE("Done"),
    PENDING_PRODUCT_UX("Pendente de Produto/UX"),
    PAUSED("Pausado"),
    FINISHED("Finalizada"),
    STAKEHOLDERS_APPROVAL("Aprovação stakeholders"),
    DEVOPS("Devops"),
    PRODUCT_HML_VERIFICATION("Produto - Verificação - HML"),
    AUTOMATION("Automatização"),
    INTERNAL("Interno"),
    UNKNOWN("Unknown");

    private final String originalName;

    public static ETaskStatus fromString(String originalName) {
        return Arrays.stream(values())
                .filter(taskStatus -> taskStatus.originalName.equalsIgnoreCase(originalName))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
