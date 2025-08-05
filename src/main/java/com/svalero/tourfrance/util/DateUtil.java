package com.svalero.tourfrance.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static final String DATE_PATTERN = "dd/MM/yyyy";

    public static LocalDate format(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static long getDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        return Math.abs(startDate.until(endDate, ChronoUnit.DAYS));
    }
}
