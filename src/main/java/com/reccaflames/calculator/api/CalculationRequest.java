package com.reccaflames.calculator.api;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record CalculationRequest(
        String vehicleType,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") List<LocalDateTime> dates
) {
}
