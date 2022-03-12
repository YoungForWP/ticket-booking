package com.ticketbook.order.infrastructure.repository.helper;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@ActiveProfiles("test")
@SpringBootTest
public abstract class SqsBase {

  protected AmazonSQSAsync amazonSQSAsync;

  protected String queueUrl;

  private static final DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");

  @ClassRule
  public static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
      .withServices(SQS);

  @Before
  public void setUp() {
    amazonSQSAsync = AmazonSQSAsyncClientBuilder
        .standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(
                localstack.getEndpointConfiguration(SQS).getServiceEndpoint(),
                localstack.getRegion())
        )
        .withCredentials(
            localstack.getDefaultCredentialsProvider()
        )
        .build();

    CreateQueueResult queue = amazonSQSAsync.createQueue(new CreateQueueRequest("testSqs"));
    queueUrl = queue.getQueueUrl();
  }
}
