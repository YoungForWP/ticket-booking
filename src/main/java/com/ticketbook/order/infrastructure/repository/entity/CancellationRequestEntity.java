package com.ticketbook.order.infrastructure.repository.entity;

import com.ticketbook.order.model.CancellationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cancellation_request")
public class CancellationRequestEntity {

  @Id
  private UUID id;

  private String ticketId;

  private BigDecimal amount;

  private LocalDateTime createdAt;

  private LocalDateTime expiryAt;

  public static CancellationRequestEntity fromModel(CancellationRequest request, LocalDateTime current) {
    return CancellationRequestEntity
        .builder()
        .id(UUID.randomUUID())
        .amount(request.getAmount())
        .ticketId(request.getTicketId())
        .createdAt(current)
        .expiryAt(current.plusDays(7))
        .build();
  }

  public CancellationRequest toModel() {
    return CancellationRequest
        .builder()
        .id(UUID.randomUUID())
        .amount(this.getAmount())
        .ticketId(this.getTicketId())
        .build();
  }
}
