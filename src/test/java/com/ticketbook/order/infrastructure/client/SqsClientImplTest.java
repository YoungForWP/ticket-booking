package com.ticketbook.order.infrastructure.client;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbook.order.infrastructure.client.apimodel.InvoiceResource;
import com.ticketbook.order.infrastructure.repository.helper.SqsBase;
import com.ticketbook.order.model.InvoiceRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class SqsClientImplTest extends SqsBase {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private SqsClientImpl sqsClient;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    sqsClient = new SqsClientImpl(queueUrl, amazonSQSAsync, objectMapper);
  }

  @Test
  public void sendMessage_should_send_invoice_request_to_sqs_successfully() throws JsonProcessingException {
    UUID requestId = UUID.randomUUID();
    InvoiceRequest invoiceRequest = InvoiceRequest.builder()
        .id(requestId)
        .email("test@gmail.com")
        .amount(BigDecimal.valueOf(1000))
        .build();

    sqsClient.sendMessage(invoiceRequest);

    ReceiveMessageResult messageResult = amazonSQSAsync.receiveMessage(queueUrl);
    List<Message> receivedMessages = messageResult.getMessages();
    assertThat(receivedMessages.size()).isEqualTo(1);
    InvoiceResource receivedRequest = objectMapper.readValue(receivedMessages.get(0).getBody(), InvoiceResource.class);

    InvoiceResource invoiceResource = InvoiceResource.builder()
        .requestId(requestId)
        .email("test@gmail.com")
        .amount(BigDecimal.valueOf(1000))
        .build();

    assertEquals(receivedRequest, invoiceResource);
  }
}
