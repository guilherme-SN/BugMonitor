package br.com.ccm.api.bugmonitor.util;

import java.util.Objects;

public class BugStatus {
    public static final String STATUS_DONE = "Done";
    public static final String STATUS_FINALIZADA = "Finalizada";

    public static boolean isCompleted(String status) {
        return Objects.equals(status, STATUS_DONE) || Objects.equals(status, STATUS_FINALIZADA);
    }
}
