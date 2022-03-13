package com.ticketbook.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.client.SqsClientImpl;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
import com.ticketbook.order.model.CancellationRequest;
import com.ticketbook.order.model.Flight;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.model.Ticket;
import com.ticketbook.order.service.exception.FlightIsFinishedException;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private InvoiceRequestRepository invoiceRequestRepository;

  @Mock
  private FlightClientImpl flightClient;

  @Mock
  private SqsClientImpl sqsClient;

  @Test
  public void requestInvoice_should_throw_exception_when_flight_is_not_finished() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";

    InvoiceRequest invoiceRequest = InvoiceRequest.builder()
        .ticketId(ticketId)
        .email("test@gmail.com")
        .build();

    Ticket mockedTicket = Ticket.builder().id(ticketId).flightId(flightId).build();
    when(ticketRepository.getTicketById(ticketId)).thenReturn(mockedTicket);
    Flight mockedFlight = Flight.builder().id(flightId).finished(false).build();
    when(flightClient.getFlight(flightId)).thenReturn(mockedFlight);

    Throwable exception = assertThrows(
        FlightIsNotFinishedException.class, () -> orderService.requestInvoice(invoiceRequest)
    );

    assertEquals(exception.getMessage(), "Flight with id 6X5CAB is not finished.");
  }

  @Test
  public void requestInvoice_should_success_when_flight_is_finished() throws JsonProcessingException {
    String flightId = "9A5F7B";
    String ticketId = "af12f6";

    InvoiceRequest invoiceRequest = InvoiceRequest.builder()
        .ticketId(ticketId)
        .email("test@gmail.com")
        .build();

    Ticket mockedTicket = Ticket.builder().id(ticketId).flightId(flightId).build();
    when(ticketRepository.getTicketById(ticketId)).thenReturn(mockedTicket);

    Flight mockedFlight = Flight.builder().id(flightId).finished(true).build();
    when(flightClient.getFlight(flightId)).thenReturn(mockedFlight);

    UUID requestId = UUID.randomUUID();
    when(invoiceRequestRepository.save(invoiceRequest)).thenReturn(requestId);

    UUID actual = orderService.requestInvoice(invoiceRequest);

    assertEquals(requestId, actual);
  }

  @Test
  public void requestCancellation_should_throw_exception_when_flight_is_finished() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";

    CancellationRequest cancellationRequest = CancellationRequest.builder()
        .ticketId(ticketId)
        .amount(BigDecimal.valueOf(600))
        .build();

    Ticket mockedTicket = Ticket.builder().id(ticketId).flightId(flightId).build();
    when(ticketRepository.getTicketById(ticketId)).thenReturn(mockedTicket);
    Flight mockedFlight = Flight.builder().id(flightId).finished(true).build();
    when(flightClient.getFlight(flightId)).thenReturn(mockedFlight);

    Throwable exception = assertThrows(
        FlightIsFinishedException.class, () -> orderService.requestCancellation(cancellationRequest)
    );

    assertEquals(exception.getMessage(), "Flight with id 6X5CAB is finished.");
  }
}
