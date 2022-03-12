package com.ticketbook.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbook.order.dto.InvoiceRequest;
import com.ticketbook.order.service.OrderService;
import com.ticketbook.order.service.exception.FlightIsNotFinishedException;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.StringContains.containsString;
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

  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void requestInvoice_should_return_400_when_email_format_is_not_correct() throws Exception {
    InvoiceRequest request = InvoiceRequest.builder().email("test email").build();

    mockMvc.perform(post("/orders/yl68q1/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("The email is invalid.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_email_is_empty() throws Exception {
    InvoiceRequest request = InvoiceRequest.builder().email("").build();

    mockMvc.perform(post("/orders/yl68q1/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Email is required.")));
  }

  @Test
  public void requestInvoice_should_return_400_when_email_is_null() throws Exception {
    InvoiceRequest request = InvoiceRequest.builder().build();

    mockMvc.perform(post("/orders/yl68q1/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Email is required.")));
  }

  @Test
  public void requestInvoice_should_return_400_flight_is_not_finished() throws Exception {
    InvoiceRequest request = InvoiceRequest.builder().email("test@gmail.com").build();

    when(orderService.requestInvoice("AH597C")).thenThrow(new FlightIsNotFinishedException("6X5CAB"));

    mockMvc.perform(post("/orders/AH597C/tickets/af12f6/invoice")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Flight with id 6X5CAB is not finished.")));
  }
}
