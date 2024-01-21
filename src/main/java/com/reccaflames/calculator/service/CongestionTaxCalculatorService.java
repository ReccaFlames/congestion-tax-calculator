package com.reccaflames.calculator.service;

import com.reccaflames.calculator.api.CalculationRequest;
import com.reccaflames.calculator.api.CalculationResponse;
import com.reccaflames.calculator.exceptions.InvalidCalculationRequestException;
import com.reccaflames.calculator.model.Vehicle;
import com.reccaflames.calculator.tax.TaxCalculator;
import com.reccaflames.calculator.validators.ClientRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class CongestionTaxCalculatorService {

    private ClientRequestValidator validator;
    private TaxCalculator calculator;

    public CalculationResponse calculate(CalculationRequest request) {
        Map<String, String> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            throw new InvalidCalculationRequestException("Invalid calculation request", errors);
        }
        var tax = calculator.getTax(Vehicle.of(request.vehicleType()), request.dates(), request.cityCode(), request.countryCode());
        return new CalculationResponse(tax);
    }
}
