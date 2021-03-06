package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.entity.TicketEntity;
import com.ticketbook.order.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

  @Autowired
  private EntityManager entityManager;


  @Override
  public Ticket getTicket(String id) {
    TicketEntity ticketEntity = entityManager.find(TicketEntity.class, id);
    return ticketEntity.toModel();
  }
}
