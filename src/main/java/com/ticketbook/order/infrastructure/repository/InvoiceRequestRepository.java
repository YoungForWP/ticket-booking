package com.ticketbook.order.infrastructure.repository;

import com.ticketbook.order.model.InvoiceRequest;

public interface InvoiceRequestRepository {

   void save(InvoiceRequest invoiceRequest);

}
