package com.reccaflames.calculator.tax;

import com.reccaflames.calculator.model.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

public interface TaxCalculator {
    int getTax(Vehicle vehicle, List<LocalDateTime> dates, String cityCode, String countryCode);
}
