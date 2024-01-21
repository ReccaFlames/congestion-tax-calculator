package com.reccaflames.calculator;

import com.reccaflames.calculator.entity.City;
import com.reccaflames.calculator.entity.TaxRate;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class RepositoryValues {
    public static final UUID CITY_ID = UUID.fromString("69085902-6fba-433f-8442-74505b29c29c");
    public static final String CITY_CODE = "GOT";
    public static final String COUNTRY = "SWE";

    public static final City city = new City(CITY_ID, CITY_CODE, COUNTRY, "Goteborg");

    public static final List<TaxRate> taxRates = List.of(
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(6, 0), LocalTime.of(6, 29), 8),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(6, 30), LocalTime.of(6, 59), 13),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(7, 0), LocalTime.of(7, 59), 18),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(8, 0), LocalTime.of(8, 29), 13),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(8, 30), LocalTime.of(14, 59), 8),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(15, 0), LocalTime.of(15, 29), 13),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(15, 30), LocalTime.of(16, 59), 18),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(17, 0), LocalTime.of(17, 59), 13),
            new TaxRate(UUID.randomUUID(), CITY_ID, LocalTime.of(18, 0), LocalTime.of(18, 29), 8)
    );
}
