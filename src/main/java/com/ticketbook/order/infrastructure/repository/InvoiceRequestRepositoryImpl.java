package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.entity.InvoiceRequestEntity;
import com.ticketbook.order.model.InvoiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


@Repository
public class InvoiceRequestRepositoryImpl implements InvoiceRequestRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public void save(InvoiceRequest invoiceRequest) {
    InvoiceRequestEntity entity = InvoiceRequestEntity.fromModel(invoiceRequest);
    entityManager.persist(entity);
  }
}
