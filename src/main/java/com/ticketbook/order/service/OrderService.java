package com.ticketbook.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.client.PaymentClient;
import com.ticketbook.order.infrastructure.client.SqsClient;
import com.ticketbook.order.infrastructure.client.exception.PaymentServiceNotAvailableException;
import com.ticketbook.order.infrastructure.repository.AlternationConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.AlternationRequestRepository;
import com.ticketbook.order.infrastructure.repository.CancellationConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.CancellationRequestRepository;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.PaymentConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
import com.ticketbook.order.model.AlternationConfirmation;
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
import com.ticketbook.order.service.exception.TicketIsInCancellationProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {

  private final TicketRepository ticketRepository;

  private final InvoiceRequestRepository invoiceRequestRepository;

  private final CancellationConfirmationRepository cancellationConfirmationRepository;

  private final PaymentConfirmationRepository paymentConfirmationRepository;

  private final AlternationConfirmationRepository alternationConfirmationRepository;

  private final AlternationRequestRepository alternationRequestRepository;

  private final CancellationRequestRepository cancellationRequestRepository;

  private final FlightClientImpl flightClient;

  private final SqsClient sqsClient;

  private final PaymentClient paymentClient;

  public OrderService(
      TicketRepository ticketRepository,
      InvoiceRequestRepository invoiceRequestRepository,
      CancellationConfirmationRepository cancellationConfirmationRepository,
      PaymentConfirmationRepository paymentConfirmationRepository,
      AlternationConfirmationRepository alternationConfirmationRepository,
      AlternationRequestRepository alternationRequestRepository,
      CancellationRequestRepository cancellationRequestRepository,
      FlightClientImpl flightClient,
      SqsClient sqsClient,
      PaymentClient paymentClient) {
    this.ticketRepository = ticketRepository;
    this.invoiceRequestRepository = invoiceRequestRepository;
    this.cancellationConfirmationRepository = cancellationConfirmationRepository;
    this.paymentConfirmationRepository = paymentConfirmationRepository;
    this.alternationConfirmationRepository = alternationConfirmationRepository;
    this.alternationRequestRepository = alternationRequestRepository;
    this.cancellationRequestRepository = cancellationRequestRepository;
    this.flightClient = flightClient;
    this.sqsClient = sqsClient;
    this.paymentClient = paymentClient;
  }

  public UUID requestInvoice(InvoiceRequest request) throws JsonProcessingException {
    Ticket ticket = ticketRepository.getTicket(request.getTicketId());
    checkCanRequestInvoice(ticket, request.getOrderId());

    InvoiceRequest invoiceRequest = request.toBuilder().amount(ticket.getActuallyPaid()).build();
    UUID invoiceRequestId = invoiceRequestRepository.save(invoiceRequest);

    sqsClient.sendMessage(invoiceRequest.toBuilder().id(invoiceRequestId).build());

    return invoiceRequestId;
  }

  public UUID requestCancellation(CancellationRequest request) {
    checkCanRequestCancellation(request);
    UUID requestId = cancellationRequestRepository.save(request);

    try {
      paymentClient.refund(request.toBuilder().id(requestId).build());
    } catch (HttpServerErrorException error) {
      throw new PaymentServiceNotAvailableException();
    }

    return requestId;
  }

  private void checkCanRequestInvoice(Ticket ticket, String orderId) {
    checkFlightIsFinished(ticket.getFlightId());
    checkTicketIsCancelledOrInCancelledProgress(ticket.getId());
    checkOrderIsPaid(orderId);
  }

  private void checkCanRequestCancellation(CancellationRequest request) {
    Ticket ticket = ticketRepository.getTicket(request.getTicketId());
    checkFlightIsNotFinished(ticket.getFlightId());
    checkNoCancellationConfirmation(ticket.getId());
    checkOrderIsPaid(request.getOrderId());
    checkTicketIsInAlteration(ticket.getId());
  }


  private void checkTicketIsCancelledOrInCancelledProgress(String ticketId) {
    CancellationConfirmation confirmation = cancellationConfirmationRepository.getCancellationConfirmation(ticketId);
    if (Objects.nonNull(confirmation) && confirmation.isConfirmed()) {
      throw new TicketIsAlreadyCancelledException(ticketId);
    }

    CancellationRequest request = cancellationRequestRepository.get(ticketId);
    if (Objects.nonNull(request)) {
      throw new TicketIsInCancellationProcessingException(ticketId);
    }
  }

  private void checkTicketIsInAlteration(String ticketId) {
    AlternationConfirmation confirmation = alternationConfirmationRepository.getAlternationConfirmation(ticketId);
    if (Objects.nonNull(confirmation) && confirmation.isConfirmed()) {
      return;
    }
    AlternationRequest request = alternationRequestRepository.getAlternationRequest(ticketId);
    if (Objects.nonNull(request)) {
      throw new TicketIsInAlterationProcessingException(ticketId);
    }

  }

  private void checkOrderIsPaid(String orderId) {
    PaymentConfirmation paymentConfirmation = paymentConfirmationRepository.getPaymentConfirmation(orderId);
    if (Objects.isNull(paymentConfirmation) || !paymentConfirmation.isConfirmed()) {
      throw new OrderNotPaidException(orderId);
    }
  }

  private void checkNoCancellationConfirmation(String ticketId) {
    CancellationConfirmation cancellationConfirmation = cancellationConfirmationRepository.getCancellationConfirmation(ticketId);
    if (Objects.nonNull(cancellationConfirmation) && cancellationConfirmation.isConfirmed()) {
      throw new TicketIsAlreadyCancelledException(ticketId);
    }
  }

  private void checkFlightIsNotFinished(String flightId) {
    Flight flight = flightClient.getFlight(flightId);

    if (flight.isFinished()) {
      throw new FlightIsFinishedException(flight.getId());
    }
  }

  private void checkFlightIsFinished(String flightId) {
    Flight flight = flightClient.getFlight(flightId);

    if (!flight.isFinished()) {
      throw new FlightIsNotFinishedException(flight.getId());
    }
  }
}
