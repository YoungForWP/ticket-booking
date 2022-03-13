package com.ticketbook.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmation {

  private String orderId;

  private BigDecimal amount;

  private boolean confirmed;

}
