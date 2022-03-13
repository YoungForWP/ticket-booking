package com.ticketbook.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationConfirmation {

  private String ticketId;

  private UUID cancellationRequestId;

  private BigDecimal amount;

  private boolean confirmed;
}
