package com.ticketbook.order.service.exception;

import javax.validation.constraints.NotNull;

public class TicketIsInCancellationProcessingException extends RuntimeException {
  public TicketIsInCancellationProcessingException(@NotNull String ticketId) {
    super(String.format("Ticket with id %s is in cancellation processing." , ticketId));
  }
}
