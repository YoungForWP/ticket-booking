package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.CancellationRequest;

import java.util.UUID;

public interface CancellationRequestRepository {

  UUID save(CancellationRequest request);

}
