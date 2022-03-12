package com.ticketbook.order.service;

import com.ticketbook.order.infrastructure.client.FlightClientImpl;
import com.ticketbook.order.infrastructure.repository.InvoiceRequestRepository;
import com.ticketbook.order.infrastructure.repository.TicketRepository;
import com.ticketbook.order.model.Flight;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.model.Ticket;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final TicketRepository ticketRepository;

  private final InvoiceRequestRepository invoiceRequestRepository;

  private final FlightClientImpl flightClient;


  public OrderService(
      TicketRepository ticketRepository,
      InvoiceRequestRepository invoiceRequestRepository,
      FlightClientImpl flightClient) {
    this.ticketRepository = ticketRepository;
    this.invoiceRequestRepository = invoiceRequestRepository;
    this.flightClient = flightClient;
  }

  public String requestInvoice(InvoiceRequest request) {
    Ticket ticket = ticketRepository.getTicketById(request.getTicketId());
    Flight flight = flightClient.getFlight(ticket.getFlightId());

    if (!flight.isFinished()) {
      throw new FlightIsNotFinishedException(flight.getId());
    }

    InvoiceRequest invoiceRequest = request.toBuilder().amount(ticket.getAmount()).build();
    invoiceRequestRepository.save(invoiceRequest);

    return null;
  }

}
