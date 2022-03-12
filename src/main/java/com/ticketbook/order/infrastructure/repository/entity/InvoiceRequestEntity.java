package com.ticketbook.order.infrastructure.repository.entity;

import com.ticketbook.order.model.InvoiceRequest;
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
@Table(name = "invoice_request")
public class InvoiceRequestEntity {

  @Id
  private UUID id;

  private String ticketId;

  private String email;

  private BigDecimal amount;

  public static InvoiceRequestEntity fromModel(InvoiceRequest request) {
    return InvoiceRequestEntity.builder()
        .id(UUID.randomUUID())
        .ticketId(request.getTicketId())
        .email(request.getEmail())
        .amount(request.getAmount())
        .build();
  }
}
