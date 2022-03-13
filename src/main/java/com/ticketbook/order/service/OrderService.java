package com.ticketbook.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.client.SqsClient;
import com.ticketbook.order.infrastructure.repository.CancellationConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.PaymentConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
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
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {

  private final TicketRepository ticketRepository;

  private final InvoiceRequestRepository invoiceRequestRepository;

  private final CancellationConfirmationRepository cancellationConfirmationRepository;

  private final PaymentConfirmationRepository paymentConfirmationRepository;

  private final FlightClientImpl flightClient;

  private final SqsClient sqsClient;

  public OrderService(
      TicketRepository ticketRepository,
      InvoiceRequestRepository invoiceRequestRepository,
      CancellationConfirmationRepository cancellationConfirmationRepository,
      PaymentConfirmationRepository paymentConfirmationRepository,
      FlightClientImpl flightClient,
      SqsClient sqsClient) {
    this.ticketRepository = ticketRepository;
    this.invoiceRequestRepository = invoiceRequestRepository;
    this.cancellationConfirmationRepository = cancellationConfirmationRepository;
    this.paymentConfirmationRepository = paymentConfirmationRepository;
    this.flightClient = flightClient;
    this.sqsClient = sqsClient;
  }

  public UUID requestInvoice(InvoiceRequest request) throws JsonProcessingException {
    Ticket ticket = ticketRepository.getTicket(request.getTicketId());
    checkFlightIsFinished(ticket.getFlightId());

    InvoiceRequest invoiceRequest = request.toBuilder().amount(ticket.getActuallyPaid()).build();
    UUID invoiceRequestId = invoiceRequestRepository.save(invoiceRequest);

    sqsClient.sendMessage(invoiceRequest.toBuilder().id(invoiceRequestId).build());

    return invoiceRequestId;
  }

  public UUID requestCancellation(CancellationRequest request) {
    Ticket ticket = ticketRepository.getTicket(request.getTicketId());
    checkFlightIsNotFinished(ticket.getFlightId());
    checkNoCancellationConfirmation(ticket.getId());
    checkOrderIsPaid(request.getOrderId());
    return null;
  }

  private void checkOrderIsPaid(String orderId) {
    PaymentConfirmation paymentConfirmation = paymentConfirmationRepository.getPaymentConfirmation(orderId);
    if (Objects.nonNull(paymentConfirmation) && !paymentConfirmation.isConfirmed()) {
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
