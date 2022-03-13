package com.ticketbook.order.controller;

import com.ticketbook.order.dto.ErrorDetail;
import com.ticketbook.order.service.exception.FlightIsFinishedException;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import com.ticketbook.order.service.exception.OrderNotPaidException;
import com.ticketbook.order.service.exception.TicketIsAlreadyCancelledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.ticketbook.order.dto.ErrorDetail.buildExceptionResponse;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(value = {MethodArgumentNotValidException.class, FlightIsNotFinishedException.class,
      FlightIsFinishedException.class, TicketIsAlreadyCancelledException.class, OrderNotPaidException.class})
  public ResponseEntity<ErrorDetail> handleInvalidInputException(
      Exception exception
  ) {
    return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception);
  }
}
