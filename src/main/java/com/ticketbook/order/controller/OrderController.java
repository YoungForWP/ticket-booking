package com.ticketbook.order.controller;

import com.ticketbook.order.dto.InvoiceRequest;
import com.ticketbook.order.service.OrderService;
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

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/{id}/tickets/{tid}/invoice")
  public String requestInvoice(
      @PathVariable("id") String orderId,
      @PathVariable("tid") String ticketId,
      @RequestBody @Valid InvoiceRequest invoiceRequest
  ) {
    orderService.requestInvoice(orderId);
    return null;
  }
}
