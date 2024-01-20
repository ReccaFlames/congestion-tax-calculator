package com.reccaflames.calculator.service;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.reccaflames.calculator.DateTimeUtils.parseDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DateFeeCalculatorTest {

    private final DateFeeCalculator calculator = new DateFeeCalculator();

    public Stream<Arguments> getDatesData() {
        return Stream.of(
                Arguments.of(parseDate("2013-02-14 05:59:00"), 0),
                Arguments.of(parseDate("2013-02-14 06:00:00"), 8),
                Arguments.of(parseDate("2013-02-14 06:29:00"), 8),
                Arguments.of(parseDate("2013-02-14 06:30:00"), 13),
                Arguments.of(parseDate("2013-02-14 06:59:00"), 13),
                Arguments.of(parseDate("2013-02-14 07:00:00"), 18),
                Arguments.of(parseDate("2013-02-14 07:59:00"), 18),
                Arguments.of(parseDate("2013-02-14 08:00:00"), 13),
                Arguments.of(parseDate("2013-02-14 08:29:00"), 13),
                Arguments.of(parseDate("2013-02-14 08:30:00"), 8),
                Arguments.of(parseDate("2013-02-14 14:59:00"), 8),
                Arguments.of(parseDate("2013-02-14 07:59:00"), 18),
                Arguments.of(parseDate("2013-02-14 08:00:00"), 13),
                Arguments.of(parseDate("2013-02-14 15:00:00"), 13),
                Arguments.of(parseDate("2013-02-14 16:59:00"), 18),
                Arguments.of(parseDate("2013-02-14 18:29:00"), 8),
                Arguments.of(parseDate("2013-02-14 18:30:00"), 0),
                Arguments.of(parseDate("2013-12-25 18:30:00"), 0), // Holiday
                Arguments.of(parseDate("2013-12-25 16:59:00"), 0), // Holiday
                Arguments.of(parseDate("2013-12-24 18:30:00"), 0), // Day before Holiday
                Arguments.of(parseDate("2013-12-24 16:59:00"), 0), // Day before Holiday
                Arguments.of(parseDate("2013-05-11 18:30:00"), 0), // Saturday
                Arguments.of(parseDate("2013-05-11 16:59:00"), 0), // Saturday
                Arguments.of(parseDate("2013-05-12 18:30:00"), 0), // Sunday
                Arguments.of(parseDate("2013-05-12 16:59:00"), 0) // Sunday
        );
    }

    @ParameterizedTest
    @MethodSource("getDatesData")
    void shouldGetToolFee(LocalDateTime date, int expected) {
        //given
        //when
        var result = calculator.calculate(date);

        //then
        assertThat(result).isEqualTo(expected);
    }
}