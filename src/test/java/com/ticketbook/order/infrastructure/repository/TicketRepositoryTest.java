package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import com.ticketbook.order.model.Ticket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class TicketRepositoryTest extends DbBase {

  @Autowired
  private TicketRepository ticketRepository;

  @Test
  @Transactional
  public void getTicketById_should_get_ticket_detail_by_id() {
    setupTicket();

    Ticket ticket = ticketRepository.getTicketById("AH597C");

    Ticket expectedTicket = Ticket.builder().id("AH597C").flightId("6X5CAB").build();
    assertEquals(ticket, expectedTicket);
  }
}
