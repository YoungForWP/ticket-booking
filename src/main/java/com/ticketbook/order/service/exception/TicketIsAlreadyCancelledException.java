package com.ticketbook.order.service.exception;

import javax.validation.constraints.NotNull;

public class TicketIsAlreadyCancelledException extends RuntimeException {

  public TicketIsAlreadyCancelledException(@NotNull String ticketId) {
    super(String.format("Ticket with id %s is already cancelled." , ticketId));
  }
}
