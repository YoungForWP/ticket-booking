package com.ticketbook.order.service.exception;

public class ConnectToSqsFailedException extends RuntimeException {
  public ConnectToSqsFailedException() {
    super("Failed to connect to SQS.");
  }
}
