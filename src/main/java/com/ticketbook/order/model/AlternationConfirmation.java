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
public class AlternationConfirmation {

  private UUID  alternationRequestId;

  private String ticketId;

  private String oldFlightId;

  private String newFlightId;

  private BigDecimal amount;

  private boolean confirmed;

}
