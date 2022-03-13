package com.ticketbook.order.dto;

import com.ticketbook.order.model.InvoiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDto {

  @NotNull(message = "Email is required.")
  @NotEmpty(message = "Email is required.")
  @Pattern(
      regexp = "^(.+)@(\\S+)$",
      message = "The email is invalid."
  )
  private String email;

  public static InvoiceRequest toModel(InvoiceRequestDto invoiceRequestDto,
                                       String orderId,
                                       String ticketId) {
    return InvoiceRequest.builder()
        .email(invoiceRequestDto.getEmail())
        .orderId(orderId)
        .ticketId(ticketId)
        .build();
  }
}
