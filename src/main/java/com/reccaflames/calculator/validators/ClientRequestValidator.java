package com.reccaflames.calculator.validators;

import com.reccaflames.calculator.api.CalculationRequest;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
public class ClientRequestValidator {

    public Map<String, String> validate(CalculationRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.cityCode() == null || request.cityCode().length() != 3) {
            errors.put("cityCode", "City Code needs to be build from 3 letters");
        }
        if (request.countryCode() == null || request.countryCode().length() != 3) {
            errors.put("country", "Country Code needs to be build from 3 letters");
        }
        if (request.vehicleType() == null || request.vehicleType().isBlank()) {
            errors.put("vehicleType", "Vehicel Type needs to be defined");
        }
        return errors;
    }
}
