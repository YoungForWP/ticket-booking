package com.ticketbook.order.service;

import com.ticketbook.order.infrastructure.entity.Order;
import com.ticketbook.order.infrastructure.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;

  @Test
  public void requestInvoice_should_throw_exception_when_flight_is_not_finished() {
    String orderID = "AH597C";
    Order mockedOrder = Order.builder().id(orderID).flightId("6X5CAB").build();
    when(orderRepository.getOrderById(orderID)).thenReturn(mockedOrder);

   orderService.requestInvoice(orderID);
  }
}
