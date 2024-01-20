package com.reccaflames.calculator.tax.fee;

import java.time.LocalDateTime;

public interface FeeCalculator {
    int getFee(LocalDateTime date);
}
