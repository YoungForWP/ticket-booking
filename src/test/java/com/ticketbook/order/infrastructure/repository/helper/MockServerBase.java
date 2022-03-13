package com.ticketbook.order.infrastructure.repository.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbook.order.infrastructure.client.apimodel.FlightResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;

public abstract class MockServerBase {

  private static ClientAndServer mockServer;

  private static ObjectMapper objectMapper;

  @BeforeClass
  public static void setUp() {
    objectMapper = new ObjectMapper();
    mockServer = startClientAndServer(1080);
  }

  protected void mockFlightServer(String flightId, boolean finished) throws JsonProcessingException {
    mockServer.when(
        request()
            .withMethod("GET")
            .withPath(String.format("/flights/%s", flightId))
    ).respond(
        response()
            .withHeaders(new Header(CONTENT_TYPE, APPLICATION_JSON.toString()))
            .withBody(objectMapper.writeValueAsString(
                FlightResponse.builder().id(flightId).finished(finished).build())
            )
    );
  }

  protected void mockPaymentServerUnableAvailable() {
    mockServer.when(
        request()
            .withMethod("POST")
            .withPath("/payment/refund")
    ).respond(
        response()
            .withHeaders(new Header(CONTENT_TYPE, APPLICATION_JSON.toString()))
            .withStatusCode(HttpStatusCode.SERVICE_UNAVAILABLE_503.code())
    );
  }

  @AfterClass
    public static void tearDown() {
      mockServer.stop();
    }
}
