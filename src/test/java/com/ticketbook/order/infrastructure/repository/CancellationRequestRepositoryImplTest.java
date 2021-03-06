package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.helper.DateTimeHelper;
import com.ticketbook.order.infrastructure.repository.entity.CancellationRequestEntity;
import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import com.ticketbook.order.model.CancellationRequest;
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
public class CancellationRequestRepositoryImplTest extends DbBase {

  @Autowired
  private CancellationRequestRepositoryImpl requestRepository;

  @MockBean
  private DateTimeHelper helper;

  @Test
  @Transactional
  public void save_should_save_request_in_db() {
    CancellationRequest cancellationRequest = CancellationRequest
        .builder()
        .ticketId("ABCD")
        .amount(BigDecimal.valueOf(500))
        .build();

    LocalDateTime localTime = LocalDateTime.of(2020, 1, 1, 5, 55);
    when(helper.getCurrentTime()).thenReturn(localTime);

    UUID requestId = requestRepository.save(cancellationRequest);

    CancellationRequestEntity entity = entityManager.find(CancellationRequestEntity.class, requestId);

    CancellationRequestEntity expected = CancellationRequestEntity.builder()
        .id(requestId)
        .ticketId("ABCD")
        .amount(BigDecimal.valueOf(500))
        .createdAt(localTime)
        .expiryAt(localTime.plusDays(7))
        .build();

    assertEquals(entity, expected);
  }

  @Test
  @Transactional
  public void get_should_get_request_from_db() {
    String ticketId = "FTY78";
    setupCancellationRequest(ticketId);

    CancellationRequest request = requestRepository.get(ticketId);

    CancellationRequest expected = CancellationRequest
        .builder()
        .id(UUID.fromString("eefa0041-19ce-4fb6-a4e2-131e2e83c06d"))
        .ticketId(ticketId)
        .amount(BigDecimal.valueOf(600))
        .build();

    assertEquals(request, expected);
  }
}
