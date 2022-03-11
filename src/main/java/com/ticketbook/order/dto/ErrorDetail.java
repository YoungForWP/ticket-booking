package com.ticketbook.order.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDetail {
  private final String errorCode;
  private final String message;

  public static ResponseEntity<ErrorDetail> buildExceptionResponse(
      HttpStatus httpStatus,
      Exception e
  ) {
    ErrorDetail errorDetail = ErrorDetail
        .builder()
        .errorCode(String.valueOf(httpStatus.value()))
        .message(e.getMessage())
        .build();
    return new ResponseEntity<>(errorDetail, httpStatus);
  }
}
