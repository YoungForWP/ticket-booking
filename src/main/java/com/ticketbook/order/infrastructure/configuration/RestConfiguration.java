package com.ticketbook.order.infrastructure.configuration;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

    @Bean
    CloseableHttpClient getHttpClient() {
        RequestConfig config = RequestConfig
            .custom()
            .setConnectTimeout(30000)
            .setSocketTimeout(60000)
            .build();
        return HttpClientBuilder
            .create()
            .setDefaultRequestConfig(config)
            .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(getHttpClient());
        return clientHttpRequestFactory;
    }

    @Bean
    public RestTemplate getRestTemplate(
        RestTemplateBuilder restTemplateBuilder
    ) {
        return restTemplateBuilder
            .requestFactory(this::clientHttpRequestFactory)
            .build();
    }
}
