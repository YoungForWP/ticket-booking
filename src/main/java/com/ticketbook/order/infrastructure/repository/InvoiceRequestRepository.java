package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.InvoiceRequest;

import java.util.UUID;

public interface InvoiceRequestRepository {

   UUID save(InvoiceRequest invoiceRequest);

}
