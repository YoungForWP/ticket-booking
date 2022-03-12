package com.ticketbook.order.infrastructure.client;

import com.ticketbook.order.infrastructure.client.apimodel.FlightResponse;
import com.ticketbook.order.model.Flight;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlightClientImpl implements FlightClient {

  private final String flightUrl;

  private final RestClient restClient;

  FlightClientImpl(@Value("${flight_api.base_url}") String flightUrl, RestClient restClient) {
    this.flightUrl = flightUrl;
    this.restClient = restClient;
  }

  @Override
  public Flight getFlight(String flightId) {
    String url = String.format("%s/flights/%s", flightUrl, flightId);
    FlightResponse flightResponse = restClient.get(url, FlightResponse.class);
    return flightResponse.toModel();
  }

}
