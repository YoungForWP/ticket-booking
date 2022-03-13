package com.ticketbook.order.service.exception;

public class PaymentServiceNotAvailableException extends RuntimeException {

  public PaymentServiceNotAvailableException() {
    super("Payment service is not available now.");
  }

}
