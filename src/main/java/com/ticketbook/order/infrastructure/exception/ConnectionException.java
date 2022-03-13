package com.ticketbook.order.infrastructure.exception;

public class ConnectionException extends RuntimeException {

  public ConnectionException() {
    super("Failed to connect to SQS.");
  }

}
