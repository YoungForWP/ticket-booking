package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.CancellationConfirmation;

public interface CancellationConfirmationRepository {

  CancellationConfirmation getCancellationConfirmation(String ticketId);

}
