package com.ticketbook.order.service;

import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
import com.ticketbook.order.model.Flight;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.model.Ticket;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
  public void requestInvoice_should_success_when_flight_is_finished() {
    String flightId = "9A5F7B";
    String ticketId = "af12f6";

    InvoiceRequest invoiceRequest = InvoiceRequest.builder()
        .id(UUID.randomUUID())
        .ticketId(ticketId)
        .email("test@gmail.com")
        .build();

    Ticket mockedTicket = Ticket.builder().id(ticketId).flightId(flightId).build();
    when(ticketRepository.getTicketById(ticketId)).thenReturn(mockedTicket);

    Flight mockedFlight = Flight.builder().id(flightId).finished(true).build();
    when(flightClient.getFlight(flightId)).thenReturn(mockedFlight);

    Mockito.doNothing().when(invoiceRequestRepository).save(invoiceRequest);

    orderService.requestInvoice(invoiceRequest);
  }
}
