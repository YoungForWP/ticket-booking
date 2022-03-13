package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.helper.DateTimeHelper;
import com.ticketbook.order.infrastructure.repository.entity.InvoiceRequestEntity;
import com.ticketbook.order.model.InvoiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class InvoiceRequestRepositoryImpl implements InvoiceRequestRepository {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private DateTimeHelper dateTimeHelper;

  @Override
  public UUID save(InvoiceRequest invoiceRequest) {
    LocalDateTime currentTime = dateTimeHelper.getCurrentTime();
    InvoiceRequestEntity entity = InvoiceRequestEntity.fromModel(invoiceRequest, currentTime);
    entityManager.persist(entity);
    return entity.getId();
  }
}
