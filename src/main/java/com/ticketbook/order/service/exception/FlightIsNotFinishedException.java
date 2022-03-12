package com.ticketbook.order.service.exception;

import javax.validation.constraints.NotNull;

public class FlightIsNotFinishedException extends RuntimeException{

  public FlightIsNotFinishedException(@NotNull String flightId) {
    super(String.format("Flight with id %s is not finished." , flightId));
  }

}
