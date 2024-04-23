package com.github.dschreid.groups.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationUtil {

    public static long parseDuration(String strDuration) throws DateTimeParseException {
        strDuration = strDuration.replaceAll("\\s+", "").replaceFirst("(\\d+d)", "P$1T");

        strDuration =
                strDuration.charAt(0) != 'P'
                        ? "PT" + strDuration.replace("min", "m")
                        : strDuration.replace("min", "m");

        Duration duration = Duration.parse(strDuration);
        return duration.toMillis();
    }

    public static String formatDuration(long millis, String forever) {
        if (millis == 0) {
            return forever;
        }

        Duration duration = Duration.ofMillis(millis);
        long days = duration.toDays();
        long hours = duration.toHours() - (duration.toDays() * 24);
        long minutes = duration.toMinutes() - (duration.toHours() * 60);
        long seconds = duration.getSeconds() - (duration.toMinutes() * 60);
        StringBuilder stringBuilder = new StringBuilder();

        append(stringBuilder, days, "d");
        append(stringBuilder, hours, "h");
        append(stringBuilder, minutes, "m");
        append(stringBuilder, seconds, "s");
        return stringBuilder.toString().endsWith(" ")
                ? stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1).toString()
                : stringBuilder.toString();
    }

    private static void append(StringBuilder builder, long time, String prefix) {
        if (time != 0) {
            builder.append(time + prefix + " ");
        }
    }
}
