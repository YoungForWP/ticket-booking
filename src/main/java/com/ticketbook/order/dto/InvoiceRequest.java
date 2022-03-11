package com.ticketbook.order.dto;

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
public class InvoiceRequest {

  @NotNull(message = "Email is required.")
  @NotEmpty(message = "Email is required.")
  @Pattern(
      regexp = "^(.+)@(\\S+)$",
      message = "The email is invalid."
  )
  private String email;
}
