package com.ticketbook.order.controller;

import com.ticketbook.order.dto.InvoiceRequestDTO;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.service.OrderService;
import com.ticketbook.order.service.UuidService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

  private final OrderService orderService;

  private final UuidService uuidService;

  public OrderController(OrderService orderService, UuidService uuidService) {
    this.orderService = orderService;
    this.uuidService = uuidService;
  }

  @PostMapping("/{id}/tickets/{tid}/invoice")
  public String requestInvoice(
      @PathVariable("id") String orderId,
      @PathVariable("tid") String ticketId,
      @RequestBody @Valid InvoiceRequestDTO invoiceRequestDTO
  ) {
    InvoiceRequest request = InvoiceRequest
        .builder()
        .id(uuidService.generateRandom())
        .orderId(orderId)
        .ticketId(ticketId)
        .email(invoiceRequestDTO.getEmail())
        .build();

    orderService.requestInvoice(request);
    return null;
  }
}
