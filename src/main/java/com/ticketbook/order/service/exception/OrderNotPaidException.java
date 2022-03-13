package com.ticketbook.order.service.exception;

import javax.validation.constraints.NotNull;

public class OrderNotPaidException extends RuntimeException {

  public OrderNotPaidException(@NotNull String orderId) {
    super(String.format("Order with id %s is not paid." , orderId));
  }

}
