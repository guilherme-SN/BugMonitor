package br.com.ccm.api.bugmonitor.enums;

public enum EBugStatusSource {
    TASK("Status da Task"),
    PRODUCT("Status Produto"),
    QA("Status QA"),
    BACKEND("Status Backend"),
    FRONTEND("Status Frontend");

    public final String sourceName;

    EBugStatusSource(String sourceName) {
        this.sourceName = sourceName;
    }
}
