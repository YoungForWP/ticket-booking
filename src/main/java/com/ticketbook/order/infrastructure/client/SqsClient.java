package com.ticketbook.order.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.model.InvoiceRequest;

public interface SqsClient {

  void sendMessage(InvoiceRequest invoiceRequest) throws JsonProcessingException;

}
