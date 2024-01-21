package com.reccaflames.calculator.tax;

import com.reccaflames.calculator.entity.City;
import com.reccaflames.calculator.entity.TaxRate;
import com.reccaflames.calculator.exceptions.MissingCityException;
import com.reccaflames.calculator.model.Vehicle;
import com.reccaflames.calculator.repository.CityRepository;
import com.reccaflames.calculator.repository.TaxRateRepository;
import com.reccaflames.calculator.tax.fee.FeeCalculator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class CongestionTaxCalculator implements TaxCalculator {

    private static final Set<String> tollFreeVehicles = Set.of("Emergency", "Bus", "Diplomat", "Motorcycle", "Military", "Foreign");

    private final TaxRateRepository taxRateRepository;
    private final CityRepository cityRepository;
    private FeeCalculator dateFeeCalculator;

    public int getTax(Vehicle vehicle, List<LocalDateTime> dates, String cityCode, String countryCode) {
        if (isTollFreeVehicle(vehicle)) {
            return 0;
        }
        LocalDateTime intervalStart = dates.get(0);
        int totalFee = 0;

        Map<TimeRange, Integer> timeRanges = getTimeRanges(cityCode, countryCode);
        for (LocalDateTime date : dates) {
            int nextFee = dateFeeCalculator.getFee(date, timeRanges);
            int tempFee = dateFeeCalculator.getFee(intervalStart, timeRanges);

            var duration = Duration.between(intervalStart, date);
            long diffInMillies = duration.toMillis();

            long minutes = diffInMillies / 1000 / 60;

            if (minutes <= 60) {
                if (totalFee > 0) totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
            }
        }

        if (totalFee > 60) totalFee = 60;
        return totalFee;
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        String vehicleType = vehicle.getVehicleType();
        return tollFreeVehicles.contains(vehicleType);
    }

    private Map<TimeRange, Integer> getTimeRanges(String cityCode, String countryCode) {
        var cityId = getCityId(cityCode, countryCode);
        return taxRateRepository.findAllByCityId(cityId)
                .stream()
                .collect(Collectors.toMap(
                        tr -> new TimeRange(tr.getStartTime(), tr.getEndTime()),
                        TaxRate::getRate,
                        (existing, replacement) -> existing, // Merge function (keep existing in case of duplicates)
                        TreeMap::new // Use TreeMap for sorting
                ));
    }

    private UUID getCityId(String cityCode, String countryCode) {
        return cityRepository.findCityByCodeAndCountry(cityCode, countryCode)
                .map(City::getId)
                .orElseThrow(() -> new MissingCityException("There is no city with a code " + cityCode + "for a country" + countryCode));
    }
}
