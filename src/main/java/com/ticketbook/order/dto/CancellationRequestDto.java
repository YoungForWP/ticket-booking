package com.ticketbook.order.dto;

import com.ticketbook.order.model.CancellationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationRequestDto {

  private BigDecimal amount;

  public static CancellationRequest toModel(CancellationRequestDto requestDto,
                                            String orderId,
                                            String ticketId) {
    return CancellationRequest.builder()
        .amount(requestDto.getAmount())
        .orderId(orderId)
        .ticketId(ticketId)
        .build();
  }
}
