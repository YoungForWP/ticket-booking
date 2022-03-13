package com.ticketbook.order.infrastructure.client;

import com.ticketbook.order.infrastructure.repository.helper.MockServerBase;
import com.ticketbook.order.model.CancellationRequest;
import org.junit.Test;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertThrows;

public class PaymentClientImplTest extends MockServerBase {

  private final String paymentUrl = "http://localhost:1080";

  private final RestClient restClient = new RestClient(new RestTemplate());

  private final PaymentClientImpl paymentClient = new PaymentClientImpl(paymentUrl, restClient);

  @Test
  public void refund_should_throw_exception_when_payment_service_is_not_available() {
    mockPaymentServerUnableAvailable();

    CancellationRequest request = CancellationRequest.builder().build();

    assertThrows(
        HttpServerErrorException.ServiceUnavailable.class,
        () -> paymentClient.refund(request)
    );
  }
}
