package com.ticketbook.order.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidService {

  public UUID generateRandom() {
    return UUID.randomUUID();
  }

}
