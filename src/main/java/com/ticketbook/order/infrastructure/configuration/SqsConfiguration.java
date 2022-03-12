package com.ticketbook.order.infrastructure.configuration;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SqsConfiguration {

  @Value("${aws.region}")
  private String region;

  @Bean
  @Profile("!local")
  public AmazonSQSAsync sqsClient() {
    return AmazonSQSAsyncClientBuilder
        .standard()
        .withRegion(region)
        .withCredentials(InstanceProfileCredentialsProvider.getInstance())
        .build();
  }

  @Bean
  @Profile("local")
  public AmazonSQSAsync localSqsClient() {
    return AmazonSQSAsyncClientBuilder
        .standard()
        .withRegion(region)
        .withCredentials(new EnvironmentVariableCredentialsProvider())
        .build();
  }

}
