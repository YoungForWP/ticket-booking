package com.ticketbook.order.infrastructure.repository.entity;

import com.ticketbook.order.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class TicketEntity {

  @Id
  private String id;

  private BigDecimal amount;

  private BigDecimal actuallyPaid;

  private String flightId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private OrderEntity order;

  public Ticket toModel() {
    return Ticket.builder()
        .id(this.getId())
        .actuallyPaid(this.getActuallyPaid())
        .amount(this.getAmount())
        .flightId(this.getFlightId())
        .build();
  }
}
