package com.ticketbook.order.infrastructure.repository.helper;

import com.ticketbook.order.infrastructure.entity.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

@ActiveProfiles("test")
@SpringBootTest
public abstract class DbBase {

  @Autowired
  protected EntityManager entityManager;

  protected void setupTicket() {
    TicketEntity ticket = TicketEntity.builder().id("AH597C").flightId("6X5CAB").build();
    entityManager.persist(ticket);
  }

}
