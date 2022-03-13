package com.ticketbook.order.service.exception;

import javax.validation.constraints.NotNull;

public class TicketIsInAlterationProcessingException extends RuntimeException {
  public TicketIsInAlterationProcessingException(@NotNull String ticketId) {
    super(String.format("Ticket with id %s is in alternation processing." , ticketId));
  }
}
