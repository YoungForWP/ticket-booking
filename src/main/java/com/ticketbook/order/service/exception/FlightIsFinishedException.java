package com.ticketbook.order.service.exception;

import javax.validation.constraints.NotNull;

public class FlightIsFinishedException extends RuntimeException {

  public FlightIsFinishedException(@NotNull String flightId) {
    super(String.format("Flight with id %s is finished." , flightId));
  }

}
