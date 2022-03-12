package com.ticketbook.order.service;

import com.ticketbook.order.infrastructure.client.FlightClient;
import com.ticketbook.order.infrastructure.entity.Order;
import com.ticketbook.order.infrastructure.model.Flight;
import com.ticketbook.order.infrastructure.repository.OrderRepository;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private FlightClient flightClient;

  @Test
  public void requestInvoice_should_throw_exception_when_flight_is_not_finished() {
    String orderID = "AH597C";
    String flightId = "6X5CAB";

    Throwable exception = assertThrows(
        FlightIsNotFinishedException.class, () -> {
          Order mockedOrder = Order.builder().id(orderID).flightId(flightId).build();
          when(orderRepository.getOrderById(orderID)).thenReturn(mockedOrder);
          Flight mockedFlight = Flight.builder().id(flightId).finished(false).build();
          when(flightClient.getFlight(flightId)).thenReturn(mockedFlight);

          orderService.requestInvoice(orderID);
        }
    );

    assertEquals(exception.getMessage(), "Flight with id 6X5CAB is not finished.");
  }
}
