package com.ticketbook.order.infrastructure.client;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbook.order.infrastructure.client.apimodel.InvoiceResource;
import com.ticketbook.order.model.InvoiceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SqsClientImpl implements SqsClient {

  private final String sqsUrl;

  private final AmazonSQSAsync amazonSQSAsync;

  private final ObjectMapper objectMapper;

  public SqsClientImpl(@Value("${sqs.invoice.url}") String sqsUrl,
                       AmazonSQSAsync amazonSQSAsync,
                       ObjectMapper objectMapper) {
    this.sqsUrl = sqsUrl;
    this.amazonSQSAsync = amazonSQSAsync;
    this.objectMapper = objectMapper;
  }

  @Override
  public void sendMessage(InvoiceRequest invoiceRequest) throws JsonProcessingException {
    InvoiceResource invoiceResource = InvoiceResource.from(invoiceRequest);
    amazonSQSAsync.sendMessage(sqsUrl, objectMapper.writeValueAsString(invoiceResource));
  }

}
