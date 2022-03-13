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
public class AlternationRequest {

  private String ticketId;

  private String oldFlightId;

  private String newFlightId;

  private BigDecimal amount;
}
