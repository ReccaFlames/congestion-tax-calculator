package com.reccaflames.calculator.service;

import com.reccaflames.calculator.api.CalculationRequest;
import com.reccaflames.calculator.api.CalculationResponse;
import com.reccaflames.calculator.repository.CityRepository;
import com.reccaflames.calculator.repository.TaxRateRepository;
import com.reccaflames.calculator.tax.CongestionTaxCalculator;
import com.reccaflames.calculator.tax.TaxCalculator;
import com.reccaflames.calculator.tax.fee.FeeCalculator;
import com.reccaflames.calculator.validators.ClientRequestValidator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.reccaflames.calculator.DateTimeUtils.parseDate;
import static com.reccaflames.calculator.DateTimeUtils.parseDates;
import static com.reccaflames.calculator.RepositoryValues.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CongestionTaxCalculatorServiceTest {

    private final FeeCalculator feeCalculator = mock(FeeCalculator.class);
    private final CityRepository cityRepository = mock(CityRepository.class);
    private final TaxRateRepository taxRateRepository = mock(TaxRateRepository.class);

    private final TaxCalculator calculator = new CongestionTaxCalculator(taxRateRepository, cityRepository, feeCalculator);
    private final ClientRequestValidator validator = new ClientRequestValidator();
    private final CongestionTaxCalculatorService service = new CongestionTaxCalculatorService(validator, calculator);

    @Test
    void shouldCallCongestionTaxCalculator() {
        //given
        var dates = parseDates("2013-02-14 14:59:00", "2013-02-14 15:05:00");
        var request = new CalculationRequest("Car", CITY_CODE, COUNTRY, dates);
        given(cityRepository.findCityByCodeAndCountry(CITY_CODE, COUNTRY)).willReturn(Optional.of(city));
        given(taxRateRepository.findAllByCityId(CITY_ID)).willReturn(taxRates);
        given(feeCalculator.getFee(eq(parseDate("2013-02-14 14:59:00")), anyMap())).willReturn(8);
        given(feeCalculator.getFee(eq(parseDate("2013-02-14 15:05:00")), anyMap())).willReturn(13);

        //when
        var result = service.calculate(request);

        //then
        var expected = new CalculationResponse(13);
        assertThat(result).isEqualTo(expected);
    }
}