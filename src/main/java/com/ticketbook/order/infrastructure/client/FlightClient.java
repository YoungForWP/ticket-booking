package com.ticketbook.order.infrastructure.client;

import com.ticketbook.order.infrastructure.model.Flight;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlightClient {

  private final String flightUrl;

  private final RestClient restClient;

  FlightClient(@Value("${flight_api.base_url}") String flightUrl, RestClient restClient) {
    this.flightUrl = flightUrl;
    this.restClient = restClient;
  }

  public Flight getFlight(String flightId) {
    String url = String.format("%s/flights/%s", flightUrl, flightId);
    return restClient.get(url, Flight.class);
  }

}
