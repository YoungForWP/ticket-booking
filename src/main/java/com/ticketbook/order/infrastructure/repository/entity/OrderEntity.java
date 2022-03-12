package com.ticketbook.order.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

  @Id
  private String id;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "order",
      cascade = CascadeType.ALL
  )
  private List<TicketEntity> tickets;

}

