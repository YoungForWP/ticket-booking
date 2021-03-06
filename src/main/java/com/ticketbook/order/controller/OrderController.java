package com.ticketbook.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ticketbook.order.dto.CancellationRequestDto;
import com.ticketbook.order.dto.CancellationResponseDto;
import com.ticketbook.order.dto.InvoiceRequestDto;
import com.ticketbook.order.dto.InvoiceResponseDto;
import com.ticketbook.order.model.CancellationRequest;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/{id}/tickets/{tid}/invoice")
  public InvoiceResponseDto requestInvoice(
      @PathVariable("id") String orderId,
      @PathVariable("tid") String ticketId,
      @RequestBody @Valid InvoiceRequestDto invoiceRequestDTO
  ) throws JsonProcessingException {
    InvoiceRequest request = InvoiceRequestDto.toModel(invoiceRequestDTO, orderId, ticketId);
    UUID requestId = orderService.requestInvoice(request);
    return InvoiceResponseDto.builder().invoiceRequestId(requestId).build();
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/{id}/tickets/{tid}/cancellation")
  public CancellationResponseDto requestCancellation(
      @PathVariable("id") String orderId,
      @PathVariable("tid") String ticketId,
      @RequestBody CancellationRequestDto cancellationRequestDto
  )  {
    CancellationRequest cancellationRequest = CancellationRequestDto.toModel(cancellationRequestDto, orderId, ticketId);
    UUID requestId = orderService.requestCancellation(cancellationRequest);
    return CancellationResponseDto.builder().cancellationRequestId(requestId).build();
  }
}
