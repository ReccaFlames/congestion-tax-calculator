package com.reccaflames.calculator.tax.fee;

import com.reccaflames.calculator.tax.TimeRange;

import java.time.LocalDateTime;
import java.util.Map;

public interface FeeCalculator {
    int getFee(LocalDateTime date, Map<TimeRange, Integer> timeRanges);
}
