package com.ticketbook.order.infrastructure.repository.helper;

import com.ticketbook.order.infrastructure.repository.entity.CancellationConfirmationEntity;
import com.ticketbook.order.infrastructure.repository.entity.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
public abstract class DbBase {

  @Autowired
  protected EntityManager entityManager;

  protected void setupTicket() {
    TicketEntity ticket = TicketEntity.builder().id("AH597C").flightId("6X5CAB").build();
    entityManager.persist(ticket);
  }

  protected void setupCancellationConfirmation(String ticketId) {
    CancellationConfirmationEntity confirmation = CancellationConfirmationEntity
        .builder()
        .ticketId(ticketId)
        .id(UUID.fromString("eefa0041-19ce-4fb6-a4e2-131e2e83c06d"))
        .cancellationRequestId(UUID.fromString("5e25c7ed-fdfc-448d-b987-6fe3e76c2a70"))
        .amount(BigDecimal.valueOf(600))
        .confirmed(true)
        .build();
    entityManager.persist(confirmation);
  }

}
