package com.reccaflames.calculator.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidCalculationRequestException extends RuntimeException {
    private final Map<String, String> errors;

    public InvalidCalculationRequestException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
