package com.ticketbook.order.infrastructure.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateTimeHelper {
  public static final String TIMEZONE_FOR_UTC = "UTC";

  public LocalDateTime getCurrentTime() {
    return LocalDateTime.now(ZoneId.of(TIMEZONE_FOR_UTC));
  }
}
