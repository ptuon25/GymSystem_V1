package com.tuon.util.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String format() {

        return LocalDateTime.now().format(DATE_FORMATTER);
    }
}
