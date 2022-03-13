package com.ticketbook.order.infrastructure.repository.entity;

import com.ticketbook.order.model.AlternationRequest;
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
@Table(name = "alternation_request")
public class AlternationRequestEntity {

  @Id
  private UUID id;

  private String ticketId;

  private String oldFlightId;

  private String newFlightId;

  private BigDecimal amount;

  public AlternationRequest toModel() {
    return AlternationRequest
        .builder()
        .ticketId(this.getTicketId())
        .oldFlightId(this.getOldFlightId())
        .newFlightId(this.getNewFlightId())
        .amount(this.getAmount())
        .build();
  }
}
