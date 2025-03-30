package br.com.ccm.api.bugmonitor.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimezoneConverter {
    public static LocalDateTime convertToSaoPauloTime(LocalDateTime utcTime) {
        return utcTime.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                .toLocalDateTime();
    }
}
