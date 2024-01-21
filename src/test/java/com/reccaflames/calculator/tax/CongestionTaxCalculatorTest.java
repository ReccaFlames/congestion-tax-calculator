package com.reccaflames.calculator.tax;

import com.reccaflames.calculator.model.Vehicle;
import com.reccaflames.calculator.repository.CityRepository;
import com.reccaflames.calculator.repository.TaxRateRepository;
import com.reccaflames.calculator.tax.fee.DateFeeCalculator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.reccaflames.calculator.DateTimeUtils.parseDates;
import static com.reccaflames.calculator.RepositoryValues.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CongestionTaxCalculatorTest {

    private static final List<LocalDateTime> datesSpecialVehicles = parseDates(
            "2013-02-14 14:59:00",
            "2013-02-14 15:05:00"
    );
    private final CityRepository cityRepository = mock(CityRepository.class);
    private final TaxRateRepository taxRateRepository = mock(TaxRateRepository.class);
    private final DateFeeCalculator feeCalculator = new DateFeeCalculator(taxRateRepository, cityRepository);
    private final CongestionTaxCalculator calculator = new CongestionTaxCalculator(feeCalculator);

    private static Stream<Arguments> getVehiclesAndDates() {
        return Stream.of(
                // Happy cases
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-14 15:00:00",
                        "2013-02-14 16:59:00"
                ), 31),
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-14 15:00:00",
                        "2013-02-21 15:00:00"
                ), 26)
                ,
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-12 07:00:00",
                        "2013-02-13 07:00:00",
                        "2013-02-14 07:00:00"
                ), 54),
                // Weekends
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-16 15:00:00",
                        "2013-02-17 16:59:00"
                ), 0),
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-15 15:00:00",
                        "2013-02-16 16:59:00"
                ), 13),
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-17 15:00:00",
                        "2013-02-18 16:59:00"
                ), 18),
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-15 15:00:00",
                        "2013-02-16 16:59:00",
                        "2013-02-17 15:00:00",
                        "2013-02-18 16:59:00"
                ), 31),
                // 60 minute case
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-14 14:59:00",
                        "2013-02-14 15:05:00"
                ), 13),
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-14 06:29:00",
                        "2013-02-14 06:30:00",
                        "2013-02-15 07:00:00"
                ), 31),
                // 60 per day and vehicle
                Arguments.of(Vehicle.of("Car"), parseDates(
                        "2013-02-14 04:59:00",
                        "2013-02-14 06:59:00",
                        "2013-02-14 07:59:00",
                        "2013-02-14 08:59:00",
                        "2013-02-14 09:05:00",
                        "2013-02-14 10:59:00",
                        "2013-02-14 11:05:00",
                        "2013-02-14 12:59:00",
                        "2013-02-14 13:05:00",
                        "2013-02-14 14:59:00",
                        "2013-02-14 15:05:00"
                ), 60)
        );
    }

    private static Stream<Arguments> getSpecialVehicles() {
        return Stream.of(
                Arguments.of(Vehicle.of("Emergency")),
                Arguments.of(Vehicle.of("Bus")),
                Arguments.of(Vehicle.of("Diplomat")),
                Arguments.of(Vehicle.of("Motorcycle")),
                Arguments.of(Vehicle.of("Military")),
                Arguments.of(Vehicle.of("Foreign"))
        );
    }

    @ParameterizedTest
    @MethodSource("getVehiclesAndDates")
    void shouldGetTax(Vehicle vehicle, List<LocalDateTime> dates, int expected) {
        //given
        given(cityRepository.findCityByCodeAndCountry(CITY_CODE, COUNTRY)).willReturn(Optional.of(city));
        given(taxRateRepository.findAllByCityId(CITY_ID)).willReturn(taxRates);

        //when
        int result = calculator.getTax(vehicle, dates);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getSpecialVehicles")
    void shouldReturnZeroForSpecialVehicles(Vehicle vehicle) {
        //given
        //when
        var result = calculator.getTax(vehicle, datesSpecialVehicles);
        //then
        assertThat(result).isZero();
    }
}