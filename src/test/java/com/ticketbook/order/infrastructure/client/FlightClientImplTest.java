package com.ticketbook.order.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.model.Flight;
import com.ticketbook.order.infrastructure.repository.helper.MockServerBase;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class FlightClientImplTest extends MockServerBase {

  private final String flightUrl = "http://localhost:1080";

  private final RestClient restClient = new RestClient(new RestTemplate());

  private final FlightClientImpl flightClient = new FlightClientImpl(flightUrl, restClient);

  @Test
  public void getFlight_should_return_flight_info_when_get_flight_with_flight_not_finished() throws JsonProcessingException {
    mockFlightServer("6X5CAB", false);

    Flight flight = flightClient.getFlight("6X5CAB");

    Flight expectedFlight = Flight.builder().id("6X5CAB").finished(false).build();
    assertEquals(flight, expectedFlight);
  }

  @Test
  public void getFlight_should_return_flight_info_when_get_flight_with_flight_is_finished() throws JsonProcessingException {
    mockFlightServer("9A5F7B", true);

    Flight flight = flightClient.getFlight("9A5F7B");

    Flight expectedFlight = Flight.builder().id("9A5F7B").finished(true).build();
    assertEquals(flight, expectedFlight);
  }
}
