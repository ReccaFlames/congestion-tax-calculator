package com.reccaflames.calculator.controller;

import com.reccaflames.calculator.api.CalculationRequest;
import com.reccaflames.calculator.api.CalculationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.reccaflames.calculator.DateTimeUtils.parseDates;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CongestionTaxCalculatorControllerTest {
    @LocalServerPort
    int localServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCallCalculationsEndpoint() {
        //given
        var dates = parseDates("2013-02-14 14:59:00", "2013-02-14 15:05:00");
        var request = new CalculationRequest("Car", "GOT", "SWE", dates);

        //when
        ResponseEntity<CalculationResponse> result = restTemplate.postForEntity("http://localhost:" + localServerPort + "/api/calculations", request, CalculationResponse.class);

        //then
        var actual = result.getBody();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(new CalculationResponse(13));
    }
}
