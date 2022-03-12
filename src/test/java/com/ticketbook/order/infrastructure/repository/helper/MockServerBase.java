package com.ticketbook.order.infrastructure.repository.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbook.order.infrastructure.model.Flight;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

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
                Flight.builder().id(flightId).finished(finished).build())
            )
    );
  }

  @AfterClass
    public static void tearDown() {
      mockServer.stop();
    }
}
