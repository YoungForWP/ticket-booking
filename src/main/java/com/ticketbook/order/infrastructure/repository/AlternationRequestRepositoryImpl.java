package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.entity.AlternationRequestEntity;
import com.ticketbook.order.model.AlternationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AlternationRequestRepositoryImpl implements AlternationRequestRepository{

  @Autowired
  private EntityManager entityManager;

  @Override
  public AlternationRequest getAlternationRequest(String ticketId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AlternationRequestEntity> query =
        builder.createQuery(AlternationRequestEntity.class);

    Root<AlternationRequestEntity> root = query.from(AlternationRequestEntity.class);
    query.where(builder.equal(root.get("ticketId"), ticketId));

    List<AlternationRequestEntity> resultList =
        entityManager.createQuery(query).getResultList();
    return !resultList.isEmpty() ? resultList.get(0).toModel() : null;
  }
}
