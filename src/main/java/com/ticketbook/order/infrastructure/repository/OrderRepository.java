package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {

  Order getOrderById(String id);

}
