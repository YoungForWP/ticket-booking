package com.ticketbook.order.infrastructure.repository.entity;

import com.ticketbook.order.model.CancellationConfirmation;
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
@Table(name = "cancellation_confirmation")
public class CancellationConfirmationEntity {

  @Id
  private UUID id;

  private UUID cancellationRequestId;

  private String ticketId;

  private BigDecimal amount;

  private boolean confirmed;

  public CancellationConfirmation toModel() {
    return CancellationConfirmation.builder()
        .ticketId(this.getTicketId())
        .amount(this.getAmount())
        .confirmed(this.isConfirmed())
        .cancellationRequestId(this.getCancellationRequestId())
        .build();
  }
}
