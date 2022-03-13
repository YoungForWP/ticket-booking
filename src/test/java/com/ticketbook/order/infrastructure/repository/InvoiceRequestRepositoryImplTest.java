package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.helper.DateTimeHelper;
import com.ticketbook.order.infrastructure.repository.entity.InvoiceRequestEntity;
import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import com.ticketbook.order.model.InvoiceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class InvoiceRequestRepositoryImplTest extends DbBase {

  @Autowired
  private InvoiceRequestRepositoryImpl invoiceRequestRepository;

  @MockBean
  private DateTimeHelper helper;

  @Test
  @Transactional
  public void save_should_save_request_invoice_into_db() {

    InvoiceRequest invoiceRequest = InvoiceRequest.builder()
        .ticketId("ABCD")
        .amount(BigDecimal.valueOf(1900))
        .email("test@gmail.com")
        .build();

    LocalDateTime localTime = LocalDateTime.of(2020, 1, 1, 5, 55);
    when(helper.getCurrentTime()).thenReturn(localTime);

    UUID invoiceId = invoiceRequestRepository.save(invoiceRequest);

    InvoiceRequestEntity entity = entityManager.find(InvoiceRequestEntity.class, invoiceId);
    InvoiceRequestEntity expectedEntity = InvoiceRequestEntity.builder()
        .id(invoiceId)
        .ticketId("ABCD")
        .createdAt(localTime)
        .expiryAt(localTime.plusDays(2))
        .amount(BigDecimal.valueOf(1900))
        .email("test@gmail.com")
        .build();

    assertEquals(entity, expectedEntity);
  }
}
