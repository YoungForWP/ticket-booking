package com.ticketbook.order.service;

import com.ticketbook.order.infrastructure.client.FlightClient;
import com.ticketbook.order.infrastructure.entity.Order;
import com.ticketbook.order.infrastructure.model.Flight;
import com.ticketbook.order.infrastructure.repository.OrderRepository;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  private final FlightClient flightClient;

  public OrderService(OrderRepository orderRepository, FlightClient flightClient) {
    this.orderRepository = orderRepository;
    this.flightClient = flightClient;
  }

  public String requestInvoice(String orderId) {
    Order order = orderRepository.getOrderById(orderId);
    Flight flight = flightClient.getFlight(order.getFlightId());
    if (!flight.isFinished()) {
      throw new FlightIsNotFinishedException(flight.getId());
    }
    return null;
  }
}
