package com.ticketbook.order.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Ticket {

  private String id;

  private BigDecimal amount;

  private BigDecimal actuallyPaid;

  private String flightId;

}
