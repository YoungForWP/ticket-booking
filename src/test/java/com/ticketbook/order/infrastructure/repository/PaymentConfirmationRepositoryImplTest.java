package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import com.ticketbook.order.model.PaymentConfirmation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class PaymentConfirmationRepositoryImplTest extends DbBase {

  @Autowired
  private PaymentConfirmationRepositoryImpl repository;

  @Transactional
  @Test
  public void getPaymentConfirmation_should_get_Payment_confirmation_by_ticket_id() {
    String orderId = "ABCD";
    setupPaymentConfirmation(orderId);

    PaymentConfirmation paymentConfirmation = repository.getPaymentConfirmation(orderId);

    PaymentConfirmation expectedConfirmation = PaymentConfirmation
        .builder()
        .orderId(orderId)
        .amount(BigDecimal.valueOf(600))
        .confirmed(false)
        .build();

    assertEquals(expectedConfirmation, paymentConfirmation);
  }
}
