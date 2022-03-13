package com.ticketbook.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbook.order.dto.CancellationRequestDto;
import com.ticketbook.order.dto.CancellationResponseDto;
import com.ticketbook.order.dto.InvoiceRequestDto;
import com.ticketbook.order.dto.InvoiceResponseDto;
import com.ticketbook.order.service.exception.ConnectToSqsFailedException;
import com.ticketbook.order.service.exception.PaymentServiceNotAvailableException;
import com.ticketbook.order.model.InvoiceRequest;
import com.ticketbook.order.service.OrderService;
import com.ticketbook.order.service.exception.FlightIsFinishedException;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
import com.ticketbook.order.service.exception.OrderNotPaidException;
import com.ticketbook.order.service.exception.TicketIsAlreadyCancelledException;
import com.ticketbook.order.service.exception.TicketIsInAlterationProcessingException;
import com.ticketbook.order.service.exception.TicketIsInCancellationProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void requestInvoice_should_return_400_when_email_format_is_not_correct() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test email").build();

    mockMvc.perform(post("/orders/yl68q1/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("The email is invalid.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_email_is_empty() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("").build();

    mockMvc.perform(post("/orders/yl68q1/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Email is required.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_email_is_null() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().build();

    mockMvc.perform(post("/orders/yl68q1/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Email is required.")));
  }

  @Test
  public void requestInvoice_should_return_400_flight_is_not_finished() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test@gmail.com").build();

    InvoiceRequest invoiceRequest = InvoiceRequest.builder()
        .orderId("AH597C")
        .ticketId("af12f6")
        .email("test@gmail.com")
        .build();

    when(orderService.requestInvoice(invoiceRequest)).thenThrow(new FlightIsNotFinishedException("6X5CAB"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Flight with id 6X5CAB is not finished.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_ticket_is_cancelled() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test@gmail.com").build();

    when(orderService.requestInvoice(any())).thenThrow(new TicketIsAlreadyCancelledException("6X5CAB"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Ticket with id 6X5CAB is already cancelled.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_ticket_is_in_cancellation_processing() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test@gmail.com").build();

    when(orderService.requestInvoice(any())).thenThrow(new TicketIsInCancellationProcessingException("6X5CAB"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Ticket with id 6X5CAB is in cancellation processing.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_order_is_not_paid() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test@gmail.com").build();

    when(orderService.requestInvoice(any())).thenThrow(new OrderNotPaidException("AH597C"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Order with id AH597C is not paid.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_ticket_is_in_alternation_processing() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test@gmail.com").build();

    when(orderService.requestInvoice(any())).thenThrow(new TicketIsInAlterationProcessingException("af12f6"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Ticket with id af12f6 is in alternation processing.")));
  }

  @Test
  public void requestInvoice_should_return_500_when_connect_to_sqs_failed() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test@gmail.com").build();

    when(orderService.requestInvoice(any())).thenThrow(new ConnectToSqsFailedException());

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(content().string(containsString("Failed to connect to SQS.")));
  }

  @Test
  public void requestInvoice_should_return_201_when_all_good() throws Exception {
    InvoiceRequestDto request = InvoiceRequestDto.builder().email("test@gmail.com").build();

    UUID invoiceId = UUID.randomUUID();
    when(orderService.requestInvoice(any())).thenReturn(invoiceId);

    MvcResult mvcResult = mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

    String response = mvcResult.getResponse().getContentAsString();
    InvoiceResponseDto expected = InvoiceResponseDto.builder().invoiceRequestId(invoiceId).build();
    assertEquals(response, objectMapper.writeValueAsString(expected));
  }

  @Test
  public void requestCancellation_should_return_400_when_ticket_is_already_cancelled() throws Exception {
    CancellationRequestDto request = CancellationRequestDto.builder().amount(BigDecimal.valueOf(600)).build();

    when(orderService.requestCancellation(any())).thenThrow(new TicketIsAlreadyCancelledException("af12f6"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/cancellation")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Ticket with id af12f6 is already cancelled.")));
  }

  @Test
  public void requestCancellation_should_return_400_when_order_is_not_paid() throws Exception {
    CancellationRequestDto request = CancellationRequestDto.builder().amount(BigDecimal.valueOf(600)).build();

    when(orderService.requestCancellation(any())).thenThrow(new OrderNotPaidException("AH597C"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/cancellation")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Order with id AH597C is not paid.")));
  }

  @Test
  public void requestCancellation_should_return_400_when_ticket_is_in_alternation_processing() throws Exception {
    CancellationRequestDto request = CancellationRequestDto.builder().amount(BigDecimal.valueOf(600)).build();

    when(orderService.requestCancellation(any())).thenThrow(new TicketIsInAlterationProcessingException("af12f6"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/cancellation")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Ticket with id af12f6 is in alternation processing.")));
  }

  @Test
  public void requestCancellation_should_return_500_when_payment_service_not_available() throws Exception {
    CancellationRequestDto request = CancellationRequestDto.builder().amount(BigDecimal.valueOf(600)).build();
    when(orderService.requestCancellation(any())).thenThrow(new PaymentServiceNotAvailableException());

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/cancellation")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(content().string(containsString("Payment service is not available now.")));
  }

  @Test
  public void requestCancellation_should_return_201_when_all_good() throws Exception {
    CancellationRequestDto request = CancellationRequestDto.builder().amount(BigDecimal.valueOf(600)).build();
    UUID requestId = UUID.randomUUID();
    when(orderService.requestCancellation(any())).thenReturn(requestId);

    MvcResult mvcResult = mockMvc.perform(post("/orders/AH597C/tickets/af12f6/cancellation")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

    String response = mvcResult.getResponse().getContentAsString();
    CancellationResponseDto expected = CancellationResponseDto.builder().cancellationRequestId(requestId).build();
    assertEquals(response, objectMapper.writeValueAsString(expected));
  }
}
