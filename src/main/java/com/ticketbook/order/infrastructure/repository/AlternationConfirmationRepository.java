package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.AlternationConfirmation;

public interface AlternationConfirmationRepository {

  AlternationConfirmation getAlternationConfirmation(String ticketId);

}
