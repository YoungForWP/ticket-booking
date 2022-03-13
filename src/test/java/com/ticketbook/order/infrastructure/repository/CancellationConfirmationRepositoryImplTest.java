package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import com.ticketbook.order.model.CancellationConfirmation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CancellationConfirmationRepositoryImplTest extends DbBase {

  @Autowired
  private CancellationConfirmationRepositoryImpl repository;

  @Test
  @Transactional
  public void getCancellationConfirmation_should_get_cancellation_confirmation_by_ticket_id() {
    String ticketId = "ABCD";
    setupCancellationConfirmation(ticketId);

    CancellationConfirmation cancellationConfirmation =
        repository.getCancellationConfirmation(ticketId);

    CancellationConfirmation expectedConfirmation = CancellationConfirmation
        .builder()
        .ticketId(ticketId)
        .cancellationRequestId(UUID.fromString("5e25c7ed-fdfc-448d-b987-6fe3e76c2a70"))
        .amount(BigDecimal.valueOf(600))
        .confirmed(true)
        .build();

    assertEquals(expectedConfirmation, cancellationConfirmation);
  }
}
