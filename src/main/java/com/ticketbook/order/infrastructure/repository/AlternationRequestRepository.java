package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.AlternationRequest;

public interface AlternationRequestRepository {

  AlternationRequest getAlternationRequest(String ticketId);

}
