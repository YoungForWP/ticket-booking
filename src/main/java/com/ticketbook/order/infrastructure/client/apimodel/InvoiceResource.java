package com.ticketbook.order.infrastructure.client.apimodel;

import com.ticketbook.order.model.InvoiceRequest;
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
public class InvoiceResource {

  private UUID requestId;

  private String email;

  private BigDecimal amount;

  public static InvoiceResource from(InvoiceRequest invoiceRequest) {
    return InvoiceResource.builder()
        .requestId(invoiceRequest.getId())
        .email(invoiceRequest.getEmail())
        .amount(invoiceRequest.getAmount())
        .build();
  }
}
