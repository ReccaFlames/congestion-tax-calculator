package com.reccaflames.calculator.tax;

import com.reccaflames.calculator.tax.fee.FeeCalculator;
import com.reccaflames.calculator.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@AllArgsConstructor
public class CongestionTaxCalculator implements TaxCalculator {

    private static final Set<String> tollFreeVehicles = Set.of("Emergency", "Bus", "Diplomat", "Motorcycle", "Military", "Foreign");

    private FeeCalculator dateFeeCalculator;

    public int getTax(Vehicle vehicle, List<LocalDateTime> dates) {
        if (isTollFreeVehicle(vehicle)) {
            return 0;
        }
        LocalDateTime intervalStart = dates.get(0);
        int totalFee = 0;

        for (LocalDateTime date : dates) {
            int nextFee = dateFeeCalculator.getFee(date);
            int tempFee = dateFeeCalculator.getFee(intervalStart);

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
}
