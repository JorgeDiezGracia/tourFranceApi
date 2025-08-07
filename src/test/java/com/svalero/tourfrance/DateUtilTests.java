package com.svalero.tourfrance;


import com.svalero.tourfrance.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilTests {

    @Test
    public void testFormat() {
        String dateString = "05/08/2025";
        LocalDate actualLocalDate = DateUtil.format(dateString);
        assertEquals(2025, actualLocalDate.getYear());
        assertEquals(8, actualLocalDate.getMonthValue());
        assertEquals(5, actualLocalDate.getDayOfMonth());
    }

    @Test
    public void testGetDaysBetweenDates() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 1);

        long actualDays = DateUtil.getDaysBetweenDates(startDate, endDate);
        assertEquals(59, actualDays);

        actualDays = DateUtil.getDaysBetweenDates(endDate, startDate);
        assertEquals(59, actualDays);

        actualDays = DateUtil.getDaysBetweenDates(startDate, startDate);
        assertEquals(0, actualDays);
    }
}
