package com.ticketbook.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.client.PaymentClient;
import com.ticketbook.order.infrastructure.client.SqsClientImpl;
import com.ticketbook.order.infrastructure.client.exception.PaymentServiceNotAvailableException;
import com.ticketbook.order.infrastructure.repository.AlternationConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.AlternationRequestRepository;
import com.ticketbook.order.infrastructure.repository.CancellationConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.CancellationRequestRepository;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.PaymentConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
import com.ticketbook.order.model.AlternationRequest;
import com.ticketbook.order.model.CancellationConfirmation;
import com.ticketbook.order.model.CancellationRequest;
import com.ticketbook.order.model.Flight;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.model.PaymentConfirmation;
import com.ticketbook.order.model.Ticket;
import com.ticketbook.order.service.exception.FlightIsFinishedException;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import com.ticketbook.order.service.exception.OrderNotPaidException;
import com.ticketbook.order.service.exception.TicketIsAlreadyCancelledException;
import com.ticketbook.order.service.exception.TicketIsInAlterationProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
  private CancellationConfirmationRepository cancellationConfirmationRepository;

  @Mock
  private PaymentConfirmationRepository paymentConfirmationRepository;

  @Mock
  private AlternationRequestRepository alternationRequestRepository;

  @Mock
  private AlternationConfirmationRepository alternationConfirmationRepository;

  @Mock
  private CancellationRequestRepository cancellationRequestRepository;

  @Mock
  private FlightClientImpl flightClient;

  @Mock
  private SqsClientImpl sqsClient;

  @Mock
  private PaymentClient paymentClient;

  @Test
  public void requestInvoice_should_throw_exception_when_flight_is_not_finished() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";

    InvoiceRequest invoiceRequest = InvoiceRequest.builder()
        .ticketId(ticketId)
        .email("test@gmail.com")
        .build();

    mockTicket(ticketId, flightId);
    mockFlight(flightId, false);

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

    mockTicket(ticketId, flightId);
    mockFlight(flightId, true);

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

    mockTicket(ticketId, flightId);
    mockFlight(flightId, true);

    Throwable exception = assertThrows(
        FlightIsFinishedException.class, () -> orderService.requestCancellation(cancellationRequest)
    );

    assertEquals(exception.getMessage(), "Flight with id 6X5CAB is finished.");
  }

  @Test
  public void requestCancellation_should_throw_exception_when_ticket_has_already_cancellation_confirmed() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";

    CancellationRequest cancellationRequest = CancellationRequest.builder()
        .ticketId(ticketId)
        .amount(BigDecimal.valueOf(600))
        .build();

    mockTicket(ticketId, flightId);
    mockFlight(flightId, false);
    mockCancellationConfirmation(ticketId, true);

    Throwable exception = assertThrows(
        TicketIsAlreadyCancelledException.class,
        () -> orderService.requestCancellation(cancellationRequest)
    );

    assertEquals(exception.getMessage(), "Ticket with id AH597C is already cancelled.");

  }

  @Test
  public void requestCancellation_should_throw_exception_when_order_is_not_paid() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";
    String orderId = "AC78F6";

    CancellationRequest cancellationRequest = CancellationRequest.builder()
        .ticketId(ticketId)
        .orderId(orderId)
        .amount(BigDecimal.valueOf(600))
        .build();

    mockTicket(ticketId, flightId);
    mockFlight(flightId, false);
    mockCancellationConfirmation(ticketId, false);
    mockPaymentConfirmation(orderId, false);

    Throwable exception = assertThrows(
        OrderNotPaidException.class,
        () -> orderService.requestCancellation(cancellationRequest)
    );

    assertEquals(exception.getMessage(), "Order with id AC78F6 is not paid.");
  }

  @Test
  public void requestCancellation_should_throw_exception_when_ticket_is_in_alternation_processing() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";
    String orderId = "AC78F6";

    CancellationRequest cancellationRequest = CancellationRequest.builder()
        .ticketId(ticketId)
        .orderId(orderId)
        .amount(BigDecimal.valueOf(600))
        .build();

    mockTicket(ticketId, flightId);
    mockFlight(flightId, false);
    mockCancellationConfirmation(ticketId, false);
    mockPaymentConfirmation(orderId, true);

    when(alternationConfirmationRepository.getAlternationConfirmation(ticketId)).thenReturn(null);
    when(alternationRequestRepository.getAlternationRequest(ticketId)).thenReturn(mock(AlternationRequest.class));

    Throwable exception = assertThrows(
        TicketIsInAlterationProcessingException.class,
        () -> orderService.requestCancellation(cancellationRequest)
    );

    assertEquals(exception.getMessage(), "Ticket with id AH597C is in alternation processing.");
  }

  @Test
  public void requestCancellation_should_throw_exception_when_payment_service_is_not_available() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";
    String orderId = "AC78F6";

    CancellationRequest cancellationRequest = CancellationRequest.builder()
        .ticketId(ticketId)
        .orderId(orderId)
        .amount(BigDecimal.valueOf(600))
        .build();

    mockTicket(ticketId, flightId);
    mockFlight(flightId, false);
    mockCancellationConfirmation(ticketId, false);
    mockPaymentConfirmation(orderId, true);
    doThrow(HttpServerErrorException.ServiceUnavailable.class).when(paymentClient).refund(any());

    Throwable exception = assertThrows(
        PaymentServiceNotAvailableException.class,
        () -> orderService.requestCancellation(cancellationRequest)
    );

    assertEquals(exception.getMessage(), "Payment service is not available now.");
  }

  @Test
  public void requestCancellation_should_succeed_when_all_good() {
    String ticketId = "AH597C";
    String flightId = "6X5CAB";
    String orderId = "AC78F6";

    CancellationRequest cancellationRequest = CancellationRequest.builder()
        .ticketId(ticketId)
        .orderId(orderId)
        .amount(BigDecimal.valueOf(600))
        .build();

    mockTicket(ticketId, flightId);
    mockFlight(flightId, false);
    mockCancellationConfirmation(ticketId, false);
    mockPaymentConfirmation(orderId, true);
    UUID requestId = UUID.randomUUID();
    when(cancellationRequestRepository.save(any())).thenReturn(requestId);

    UUID result = orderService.requestCancellation(cancellationRequest);

    assertEquals(requestId, result);
  }

  private void mockPaymentConfirmation(String orderId, boolean confirmed) {
    PaymentConfirmation paymentConfirmation = PaymentConfirmation.builder().confirmed(confirmed).build();
    when(paymentConfirmationRepository.getPaymentConfirmation(orderId)).thenReturn(paymentConfirmation);
  }

  private void mockCancellationConfirmation(String ticketId, boolean confirmed) {
    CancellationConfirmation cancellationConfirmation = CancellationConfirmation.builder().confirmed(confirmed).build();
    when(cancellationConfirmationRepository.getCancellationConfirmation(ticketId))
        .thenReturn(cancellationConfirmation);
  }

  private void mockFlight(String flightId, boolean finished) {
    Flight mockedFlight = Flight.builder().id(flightId).finished(finished).build();
    when(flightClient.getFlight(flightId)).thenReturn(mockedFlight);
  }

  private void mockTicket(String ticketId, String flightId) {
    Ticket mockedTicket = Ticket.builder().id(ticketId).flightId(flightId).build();
    when(ticketRepository.getTicket(ticketId)).thenReturn(mockedTicket);
  }
}
