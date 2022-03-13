package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.helper.DateTimeHelper;
import com.ticketbook.order.infrastructure.repository.entity.CancellationConfirmationEntity;
import com.ticketbook.order.infrastructure.repository.entity.CancellationRequestEntity;
import com.ticketbook.order.model.CancellationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class CancellationRequestRepositoryImpl implements CancellationRequestRepository {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private DateTimeHelper dateTimeHelper;

  @Override
  public UUID save(CancellationRequest request) {
    LocalDateTime currentTime = dateTimeHelper.getCurrentTime();
    CancellationRequestEntity entity = CancellationRequestEntity.fromModel(request, currentTime);
    entityManager.persist(entity);
    return entity.getId();
  }

  @Override
  public CancellationRequest get(String ticketId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CancellationRequestEntity> query =
        builder.createQuery(CancellationRequestEntity.class);

    Root<CancellationConfirmationEntity> root = query.from(CancellationConfirmationEntity.class);
    query.where(builder.equal(root.get("ticketId"), ticketId));

    List<CancellationRequestEntity> resultList =
        entityManager.createQuery(query).getResultList();
    return !resultList.isEmpty() ? resultList.get(0).toModel() : null;
  }
}
