package com.ticketbook.order.infrastructure.client;

import com.ticketbook.order.infrastructure.client.apimodel.RefundResource;
import com.ticketbook.order.infrastructure.client.apimodel.RefundResponse;
import com.ticketbook.order.model.CancellationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentClientImpl implements PaymentClient {

  private final String paymentUrl;

  private final RestClient restClient;

  public PaymentClientImpl(@Value("${payment_api.base_url}") String paymentUrl,
                           RestClient restClient) {
    this.paymentUrl = paymentUrl;
    this.restClient = restClient;
  }

  @Override
  public void refund(CancellationRequest cancellationRequest) {
    RefundResource refundResource = RefundResource.fromModel(cancellationRequest);
    String url = String.format("%s/payment/refund", paymentUrl);
    restClient.post(url, refundResource, RefundResponse.class);
  }
}
