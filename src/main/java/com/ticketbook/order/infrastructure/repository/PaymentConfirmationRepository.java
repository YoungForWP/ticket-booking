package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.PaymentConfirmation;

public interface PaymentConfirmationRepository {

  PaymentConfirmation getPaymentConfirmation(String orderId);

}
