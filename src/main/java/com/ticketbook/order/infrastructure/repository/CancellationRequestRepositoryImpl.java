package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.entity.CancellationRequestEntity;
import com.ticketbook.order.model.CancellationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.UUID;

@Repository
public class CancellationRequestRepositoryImpl implements CancellationRequestRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public UUID save(CancellationRequest request) {
    CancellationRequestEntity entity = CancellationRequestEntity.fromModel(request);
    entityManager.persist(entity);
    return entity.getId();
  }
}
