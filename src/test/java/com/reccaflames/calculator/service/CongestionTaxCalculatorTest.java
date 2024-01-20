package com.reccaflames.calculator.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CongestionTaxCalculatorTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static List<LocalDateTime> datesSpecialVehicles = List.of(
            parseDate("2013-02-14 14:59:00"),
            parseDate("2013-02-14 15:05:00")
    );
    private final DateFeeCalculator feeCalculator = new DateFeeCalculator();
    private final CongestionTaxCalculator calculator = new CongestionTaxCalculator(feeCalculator);

    private static LocalDateTime parseDate(String s) {
        return LocalDateTime.parse(s, formatter);
    }

    public static Stream<Arguments> getVehiclesAndDates() {
        return Stream.of(
                // Happy cases
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-14 15:00:00"),
                        parseDate("2013-02-14 16:59:00")
                ), 31),
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-14 15:00:00"),
                        parseDate("2013-02-21 15:00:00")
                ), 26)
                ,
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-12 07:00:00"),
                        parseDate("2013-02-13 07:00:00"),
                        parseDate("2013-02-14 07:00:00")
                ), 54),
                // Weekends
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-16 15:00:00"),
                        parseDate("2013-02-17 16:59:00")
                ), 0),
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-15 15:00:00"),
                        parseDate("2013-02-16 16:59:00")
                ), 13),
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-17 15:00:00"),
                        parseDate("2013-02-18 16:59:00")
                ), 18),
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-15 15:00:00"),
                        parseDate("2013-02-16 16:59:00"),
                        parseDate("2013-02-17 15:00:00"),
                        parseDate("2013-02-18 16:59:00")
                ), 31),
                // 60 minute case
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-14 14:59:00"),
                        parseDate("2013-02-14 15:05:00")
                ), 13),
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-14 06:29:00"),
                        parseDate("2013-02-14 06:30:00"),
                        parseDate("2013-02-15 07:00:00")
                ), 31),
                // 60 per day and vehicle
                Arguments.of(Vehicle.of("Car"), List.of(
                        parseDate("2013-02-14 04:59:00"),
                        parseDate("2013-02-14 06:59:00"),
                        parseDate("2013-02-14 07:59:00"),
                        parseDate("2013-02-14 08:59:00"),
                        parseDate("2013-02-14 09:05:00"),
                        parseDate("2013-02-14 10:59:00"),
                        parseDate("2013-02-14 11:05:00"),
                        parseDate("2013-02-14 12:59:00"),
                        parseDate("2013-02-14 13:05:00"),
                        parseDate("2013-02-14 14:59:00"),
                        parseDate("2013-02-14 15:05:00")
                ), 60)
        );
    }

    public static Stream<Arguments> getSpecialVehicles() {
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