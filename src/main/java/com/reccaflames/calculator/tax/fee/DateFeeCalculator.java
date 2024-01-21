package com.reccaflames.calculator.tax.fee;

import com.reccaflames.calculator.entity.TaxRate;
import com.reccaflames.calculator.repository.CityRepository;
import com.reccaflames.calculator.repository.TaxRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private static final Map<TimeRange, Integer> TIME_RANGES;

    static {
        Map<TimeRange, Integer> tempMap = new TreeMap<>();
        tempMap.put(new TimeRange(LocalTime.of(6, 0), LocalTime.of(6, 29)), 8);
        tempMap.put(new TimeRange(LocalTime.of(6, 30), LocalTime.of(6, 59)), 13);
        tempMap.put(new TimeRange(LocalTime.of(7, 0), LocalTime.of(7, 59)), 18);
        tempMap.put(new TimeRange(LocalTime.of(8, 0), LocalTime.of(8, 29)), 13);
        tempMap.put(new TimeRange(LocalTime.of(8, 30), LocalTime.of(14, 59)), 8);
        tempMap.put(new TimeRange(LocalTime.of(15, 0), LocalTime.of(15, 29)), 13);
        tempMap.put(new TimeRange(LocalTime.of(15, 30), LocalTime.of(16, 59)), 18);
        tempMap.put(new TimeRange(LocalTime.of(17, 0), LocalTime.of(17, 59)), 13);
        tempMap.put(new TimeRange(LocalTime.of(18, 0), LocalTime.of(18, 29)), 8);
        TIME_RANGES = Collections.unmodifiableMap(tempMap);
    }

    private final TaxRateRepository taxRateRepository;
    private final CityRepository cityRepository;

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
    public int getFee(LocalDateTime date) {
        if (isTollFreeDate(date)) {
            return 0;
        }

        return getFeeRate(date.toLocalTime());
    }

    private int getFeeRate(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Input time cannot be null");
        }

        return getTimeRanges().entrySet().stream()
                .filter(entry -> entry.getKey().contains(time))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(0);
    }

    private Map<TimeRange, Integer> getTimeRanges() {
        return cityRepository.findCityByCodeAndCountry("GOT", "SWE")
                .map(city -> taxRateRepository.findAllByCityId(city.getId())
                        .stream()
                        .collect(Collectors.toMap(
                                tr -> new TimeRange(tr.getStartTime(), tr.getEndTime()),
                                TaxRate::getRate,
                                (existing, replacement) -> existing, // Merge function (keep existing in case of duplicates)
                                TreeMap::new // Use TreeMap for sorting
                        ))
                )
                .orElseGet(TreeMap::new);
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
