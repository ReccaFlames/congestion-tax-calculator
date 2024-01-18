package com.reccaflames.calculator.service;

import com.reccaflames.calculator.api.CalculationRequest;
import com.reccaflames.calculator.api.CalculationResponse;
import org.springframework.stereotype.Service;

@Service
public class CongestionTaxCalculatorService {

    private CongestionTaxCalculator calculator;

    public CalculationResponse calculate(CalculationRequest request) {
        return new CalculationResponse(0);
    }
}
