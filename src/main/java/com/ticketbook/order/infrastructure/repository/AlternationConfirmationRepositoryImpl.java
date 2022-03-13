package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.entity.AlternationConfirmationEntity;
import com.ticketbook.order.infrastructure.repository.entity.CancellationConfirmationEntity;
import com.ticketbook.order.model.AlternationConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AlternationConfirmationRepositoryImpl implements AlternationConfirmationRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public AlternationConfirmation getAlternationConfirmation(String ticketId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AlternationConfirmationEntity> query =
        builder.createQuery(AlternationConfirmationEntity.class);

    Root<AlternationConfirmationEntity> root = query.from(AlternationConfirmationEntity.class);
    query.where(builder.equal(root.get("ticketId"), ticketId));

    List<AlternationConfirmationEntity> resultList =
        entityManager.createQuery(query).getResultList();
    return !resultList.isEmpty() ? resultList.get(0).toModel() : null;
  }
}
