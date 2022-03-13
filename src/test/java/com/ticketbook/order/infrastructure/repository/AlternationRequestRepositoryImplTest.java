package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import com.ticketbook.order.model.AlternationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class AlternationRequestRepositoryImplTest extends DbBase {

  @Autowired
  private AlternationRequestRepository alternationRequestRepository;

  @Test
  @Transactional
  public void get_should_get_alternation_request_info() {
    String ticketId = "ABCD";
    setupAlternationRequest(ticketId);

    AlternationRequest actual = alternationRequestRepository.getAlternationRequest(ticketId);

    AlternationRequest expected = AlternationRequest
        .builder()
        .ticketId(ticketId)
        .amount(BigDecimal.valueOf(600))
        .oldFlightId("ABCD")
        .newFlightId("DGFT")
        .build();

    assertEquals(expected, actual);
  }
}
