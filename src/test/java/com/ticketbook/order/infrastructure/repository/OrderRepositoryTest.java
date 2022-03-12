package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.entity.Order;
import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class OrderRepositoryTest extends DbBase {

  @Autowired
  private OrderRepository orderRepository;

  @Test
  @Transactional
  public void getOrderById_should_get_order_detail_by_id() {
    setupOrder();

    Order order = orderRepository.getOrderById("AH597C");

    Order expectedOrder = Order.builder().id("AH597C").flightId("6X5CAB").build();
    assertEquals(order, expectedOrder);
  }
}
