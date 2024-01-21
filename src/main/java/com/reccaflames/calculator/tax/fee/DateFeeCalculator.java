package com.reccaflames.calculator.tax.fee;

import com.reccaflames.calculator.tax.TimeRange;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DateFeeCalculator implements FeeCalculator {

    private static final List<LocalDate> holidays = Arrays.asList(
            LocalDate.of(2013, 1, 1),   // New Year's Day
            LocalDate.of(2013, 1, 6),   // Epiphany
            LocalDate.of(2013, 3, 29),  // Good Friday
            LocalDate.of(2013, 4, 1),   // Easter Monday
            LocalDate.of(2013, 5, 1),   // International Workers' Day
            LocalDate.of(2013, 5, 9),   // Ascension Day
            LocalDate.of(2013, 6, 6),   // National Day of Sweden
            LocalDate.of(2013, 6, 22),  // Midsummer Day
            LocalDate.of(2013, 11, 2),  // All Saints' Day
            LocalDate.of(2013, 12, 25), // Christmas Day
            LocalDate.of(2013, 12, 26) // Christmas Day
    );

    private static boolean isHoliday(LocalDateTime date) {
        return holidays.contains(date.toLocalDate());
    }

    private static boolean isDayBeforeHoliday(LocalDateTime date) {
        var nextDay = date.plusDays(1);
        return isHoliday(nextDay);
    }

    private static boolean isInJuly(LocalDateTime date) {
        return date.getMonth() == Month.JULY;
    }

    @Override
    public int getFee(LocalDateTime date, Map<TimeRange, Integer> timeRanges) {
        if (isTollFreeDate(date)) {
            return 0;
        }

        return getFeeRate(date.toLocalTime(), timeRanges);
    }

    private int getFeeRate(LocalTime time, Map<TimeRange, Integer> timeRanges) {
        if (time == null) {
            throw new IllegalArgumentException("Input time cannot be null");
        }

        return timeRanges.entrySet().stream()
                .filter(entry -> entry.getKey().contains(time))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(0);
    }


    private boolean isTollFreeDate(LocalDateTime date) {
        if (isWeekend(date)) {
            return true;
        }

        return isHoliday(date) || isDayBeforeHoliday(date) || isInJuly(date);
    }

    private boolean isWeekend(LocalDateTime date) {
        var dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
