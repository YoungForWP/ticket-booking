package com.ticketbook.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.client.SqsClient;
import com.ticketbook.order.infrastructure.repository.CancellationConfirmationRepository;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
import com.ticketbook.order.model.CancellationConfirmation;
import com.ticketbook.order.model.CancellationRequest;
import com.ticketbook.order.model.Flight;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.model.Ticket;
import com.ticketbook.order.service.exception.FlightIsFinishedException;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import com.ticketbook.order.service.exception.TicketIsAlreadyCancelledException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {

  private final TicketRepository ticketRepository;

  private final InvoiceRequestRepository invoiceRequestRepository;

  private final CancellationConfirmationRepository cancellationConfirmationRepository;

  private final FlightClientImpl flightClient;

  private final SqsClient sqsClient;

  public OrderService(
      TicketRepository ticketRepository,
      InvoiceRequestRepository invoiceRequestRepository,
      CancellationConfirmationRepository cancellationConfirmationRepository,
      FlightClientImpl flightClient,
      SqsClient sqsClient) {
    this.ticketRepository = ticketRepository;
    this.invoiceRequestRepository = invoiceRequestRepository;
    this.cancellationConfirmationRepository = cancellationConfirmationRepository;
    this.flightClient = flightClient;
    this.sqsClient = sqsClient;
  }

  public UUID requestInvoice(InvoiceRequest request) throws JsonProcessingException {
    Ticket ticket = ticketRepository.getTicketById(request.getTicketId());
    checkFlightIsFinished(ticket.getFlightId());

    InvoiceRequest invoiceRequest = request.toBuilder().amount(ticket.getAmount()).build();
    UUID invoiceRequestId = invoiceRequestRepository.save(invoiceRequest);

    sqsClient.sendMessage(invoiceRequest.toBuilder().id(invoiceRequestId).build());

    return invoiceRequestId;
  }

  public UUID requestCancellation(CancellationRequest request) {
    Ticket ticket = ticketRepository.getTicketById(request.getTicketId());
    checkFlightIsNotFinished(ticket.getFlightId());
    checkNoCancellationConfirmation(request, ticket);
    return null;
  }

  private void checkNoCancellationConfirmation(CancellationRequest request, Ticket ticket) {
    CancellationConfirmation cancellationConfirmation = cancellationConfirmationRepository.getCancellationConfirmation(ticket.getId());
    if (Objects.nonNull(cancellationConfirmation) && cancellationConfirmation.isConfirmed()) {
      throw new TicketIsAlreadyCancelledException(request.getTicketId());
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
