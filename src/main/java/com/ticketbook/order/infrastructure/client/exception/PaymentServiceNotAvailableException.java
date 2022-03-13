package com.ticketbook.order.infrastructure.client.exception;

public class PaymentServiceNotAvailableException extends RuntimeException {

  public PaymentServiceNotAvailableException() {
    super("Payment service is not available now.");
  }

}
