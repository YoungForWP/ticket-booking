package com.ticketbook.order.service;

import com.ticketbook.order.infrastructure.entity.Order;
import com.ticketbook.order.infrastructure.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public void requestInvoice(String orderId) {
    Order order = orderRepository.getOrderById(orderId);
  }
}
