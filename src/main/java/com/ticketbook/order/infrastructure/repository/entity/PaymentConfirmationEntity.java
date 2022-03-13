package com.ticketbook.order.infrastructure.repository.entity;

import com.ticketbook.order.model.PaymentConfirmation;
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
@Table(name = "payment_confirmation")
public class PaymentConfirmationEntity {

  @Id
  private UUID id;

  private String orderId;

  private BigDecimal amount;

  private boolean confirmed;

  public PaymentConfirmation toModel() {
    return PaymentConfirmation
        .builder()
        .orderId(this.getOrderId())
        .amount(this.getAmount())
        .confirmed(this.isConfirmed())
        .build();
  }
}
