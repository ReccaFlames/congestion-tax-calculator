package com.reccaflames.calculator.service;

import com.reccaflames.calculator.api.CalculationRequest;
import com.reccaflames.calculator.api.CalculationResponse;
import org.junit.jupiter.api.Test;

import static com.reccaflames.calculator.DateTimeUtils.parseDate;
import static com.reccaflames.calculator.DateTimeUtils.parseDates;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CongestionTaxCalculatorServiceTest {

    private final DateFeeCalculator feeCalculator = mock(DateFeeCalculator.class);

    private final CongestionTaxCalculator calculator = new CongestionTaxCalculator(feeCalculator);
    private final CongestionTaxCalculatorService service = new CongestionTaxCalculatorService(calculator);

    @Test
    void shouldCallCongestionTaxCalculator() {
        //given
        var dates = parseDates("2013-02-14 14:59:00", "2013-02-14 15:05:00");
        CalculationRequest request = new CalculationRequest("Car", dates);
        given(feeCalculator.calculate(parseDate("2013-02-14 14:59:00"))).willReturn(8);
        given(feeCalculator.calculate(parseDate("2013-02-14 15:05:00"))).willReturn(13);

        //when
        var result = service.calculate(request);

        //then
        var expected = new CalculationResponse(13);
        assertThat(result).isEqualTo(expected);
    }
}