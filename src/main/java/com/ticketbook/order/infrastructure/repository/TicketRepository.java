package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.Ticket;

public interface TicketRepository {

  Ticket getTicket(String id);

}
