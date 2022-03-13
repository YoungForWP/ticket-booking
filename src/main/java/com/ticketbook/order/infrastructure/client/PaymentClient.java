package com.ticketbook.order.infrastructure.client;

import com.ticketbook.order.model.CancellationRequest;

public interface PaymentClient {

  void refund(CancellationRequest cancellationRequest);

}
