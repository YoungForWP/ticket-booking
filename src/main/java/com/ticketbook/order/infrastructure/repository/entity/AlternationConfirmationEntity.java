package com.ticketbook.order.infrastructure.repository.entity;

import com.ticketbook.order.model.AlternationConfirmation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alternation_confirmation")
public class AlternationConfirmationEntity {

  @Id
  private UUID id;

  private UUID requestId;

  private String ticketId;

  private String oldFlightId;

  private String newFlightId;

  private BigDecimal amount;

  private boolean confirmed;

  public AlternationConfirmation toModel() {
    return AlternationConfirmation
        .builder()
        .ticketId(this.getTicketId())
        .alternationRequestId(this.getRequestId())
        .oldFlightId(this.getOldFlightId())
        .newFlightId(this.getNewFlightId())
        .amount(this.getAmount())
        .confirmed(this.isConfirmed())
        .build();
  }
}
