package com.reccaflames.calculator.controller;

import com.reccaflames.calculator.service.CongestionTaxCalculator;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/calculations")
@AllArgsConstructor
public class CongestionTaxCalculatorController {
    private final CongestionTaxCalculator congestionTaxCalculator;

    @GetMapping
    public String hello() {
        return "Hello world";
    }
}
