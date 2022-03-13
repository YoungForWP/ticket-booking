package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.entity.CancellationConfirmationEntity;
import com.ticketbook.order.model.CancellationConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CancellationConfirmationRepositoryImpl implements CancellationConfirmationRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public CancellationConfirmation getCancellationConfirmation(String ticketId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CancellationConfirmationEntity> query =
        builder.createQuery(CancellationConfirmationEntity.class);

    Root<CancellationConfirmationEntity> root = query.from(CancellationConfirmationEntity.class);
        query.where(builder.equal(root.get("ticketId"), ticketId));

    List<CancellationConfirmationEntity> resultList =
        entityManager.createQuery(query).getResultList();
    return !resultList.isEmpty() ? resultList.get(0).toModel() : null;
  }

}
