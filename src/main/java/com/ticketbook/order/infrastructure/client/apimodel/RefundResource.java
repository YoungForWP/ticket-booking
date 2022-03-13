package com.ticketbook.order.infrastructure.client.apimodel;

import com.ticketbook.order.model.CancellationRequest;
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
public class RefundResource {

  private UUID requestId;

  private BigDecimal amount;

  public static RefundResource fromModel(CancellationRequest request) {
    return RefundResource.builder()
        .requestId(request.getId())
        .amount(request.getAmount())
        .build();
  }

}
