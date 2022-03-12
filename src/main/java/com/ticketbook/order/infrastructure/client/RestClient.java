package com.ticketbook.order.infrastructure.client;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClient {

  private final RestTemplate restTemplate;

  @Autowired
  public RestClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public <T> T get(String endpoint, Class<T> clazz) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(ImmutableList.of(MediaType.APPLICATION_JSON));
    return restTemplate
        .exchange(endpoint, HttpMethod.GET, new HttpEntity<String>(headers), clazz).getBody();
  }
}
