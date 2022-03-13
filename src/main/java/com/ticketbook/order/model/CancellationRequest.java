package com.ticketbook.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationRequest {

  private String orderId;

  private String ticketId;

  private BigDecimal amount;

}
