package com.reccaflames.calculator.controller;

import com.reccaflames.calculator.api.CalculationRequest;
import com.reccaflames.calculator.api.CalculationResponse;
import com.reccaflames.calculator.service.CongestionTaxCalculatorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "api/calculations")
@AllArgsConstructor
public class CongestionTaxCalculatorController {
    private final CongestionTaxCalculatorService service;

    @GetMapping
    public String hello() {
        log.atInfo().log("Say hello to log4j");
        return "Hello world";
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CalculationResponse calculate(@RequestBody CalculationRequest request) {
        log.atInfo().log("Calculate congestion tax for vehicle {} and dates {}", request.vehicleType(), request.dates());
        return service.calculate(request);
    }
}
