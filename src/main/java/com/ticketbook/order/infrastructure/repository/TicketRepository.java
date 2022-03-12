package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, String> {

  TicketEntity getTicketById(String id);

}
