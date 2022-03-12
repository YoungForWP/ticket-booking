package com.ticketbook.order.infrastructure.client;

import com.ticketbook.order.model.Flight;

public interface FlightClient {

  Flight getFlight(String flightId);

}
