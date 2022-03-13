package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.helper.DbBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class AlternationConfirmationRepositoryImplTest extends DbBase {

  @Autowired
  private AlternationConfirmationRepositoryImpl repository;

  @Test
  public void getAlternationConfirmation_should_return_null_when_not_existed_in_db() {
    assertNull(repository.getAlternationConfirmation("ABCD"));
  }
}
