package com.reccaflames.calculator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DateTimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<LocalDateTime> parseDates(String... s) {
        return Arrays.stream(s)
                .map(DateTimeUtils::parseDate)
                .toList();
    }

    public static LocalDateTime parseDate(String s) {
        return LocalDateTime.parse(s, formatter);
    }
}
