package com.ticketbook.order.infrastructure.client.apimodel;

import com.ticketbook.order.model.Flight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {

  private String id;

  private boolean finished;

  public Flight toModel() {
    return Flight.builder()
        .id(this.getId())
        .finished(this.isFinished())
        .build();
  }

}
