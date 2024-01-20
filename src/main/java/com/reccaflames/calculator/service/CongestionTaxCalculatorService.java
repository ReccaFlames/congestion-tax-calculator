package com.reccaflames.calculator.service;

import com.reccaflames.calculator.api.CalculationRequest;
import com.reccaflames.calculator.api.CalculationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CongestionTaxCalculatorService {

    private CongestionTaxCalculator calculator;

    public CalculationResponse calculate(CalculationRequest request) {
        var tax = calculator.getTax(Vehicle.of(request.vehicleType()), request.dates());
        return new CalculationResponse(tax);
    }
}
