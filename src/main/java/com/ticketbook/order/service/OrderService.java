package com.ticketbook.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.client.SqsClient;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
import com.ticketbook.order.model.Flight;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.model.Ticket;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

  private final TicketRepository ticketRepository;

  private final InvoiceRequestRepository invoiceRequestRepository;

  private final FlightClientImpl flightClient;

  private final SqsClient sqsClient;

  public OrderService(
      TicketRepository ticketRepository,
      InvoiceRequestRepository invoiceRequestRepository,
      FlightClientImpl flightClient,
      SqsClient sqsClient) {
    this.ticketRepository = ticketRepository;
    this.invoiceRequestRepository = invoiceRequestRepository;
    this.flightClient = flightClient;
    this.sqsClient = sqsClient;
  }

  public UUID requestInvoice(InvoiceRequest request) throws JsonProcessingException {
    Ticket ticket = ticketRepository.getTicketById(request.getTicketId());
    checkFlightIsFinished(ticket);

    InvoiceRequest invoiceRequest = request.toBuilder().amount(ticket.getAmount()).build();
    UUID invoiceRequestId = invoiceRequestRepository.save(invoiceRequest);

    sqsClient.sendMessage(invoiceRequest.toBuilder().id(invoiceRequestId).build());

    return invoiceRequestId;
  }

  private void checkFlightIsFinished(Ticket ticket) {
    Flight flight = flightClient.getFlight(ticket.getFlightId());

    if (!flight.isFinished()) {
      throw new FlightIsNotFinishedException(flight.getId());
    }
  }

}
