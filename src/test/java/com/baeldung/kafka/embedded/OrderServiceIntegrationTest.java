package com.baeldung.kafka.embedded;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class OrderServiceIntegrationTest {

    @Autowired
    public KafkaTemplate<String, String> template;

    @SpyBean
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Autowired
    @SpyBean
    private OrderService orderService;

    @Test
    void executeOrder_SHOULD_returnAfterGettingMessageFromConsumer() throws Exception {
        String message = "Sending with our own simple KafkaProducer";
        producer.send("test-topic", message);

        long response = orderService.executeOrder();

        verify(consumer, times(1)).receive(any());
        verify(orderService, times(1)).executionReport(message);
        assertThat(response, equalTo(0L));
    }
}