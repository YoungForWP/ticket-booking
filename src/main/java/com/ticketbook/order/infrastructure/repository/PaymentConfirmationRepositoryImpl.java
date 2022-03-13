package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.infrastructure.repository.entity.PaymentConfirmationEntity;
import com.ticketbook.order.model.PaymentConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PaymentConfirmationRepositoryImpl implements PaymentConfirmationRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public PaymentConfirmation getPaymentConfirmation(String orderId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PaymentConfirmationEntity> query =
        builder.createQuery(PaymentConfirmationEntity.class);

    Root<PaymentConfirmationEntity> root = query.from(PaymentConfirmationEntity.class);
    query.where(builder.equal(root.get("orderId"), orderId));

    List<PaymentConfirmationEntity> resultList =
        entityManager.createQuery(query).getResultList();

    return !resultList.isEmpty() ? resultList.get(0).toModel() : null;
  }
}
