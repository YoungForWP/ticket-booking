package com.ticketbook.order.infrastructure.client.apimodel;

import lombok.Data;
import org.mockserver.model.HttpStatusCode;

@Data
public class RefundResponse {

  private HttpStatusCode statusCode;

  private String detail;
}
