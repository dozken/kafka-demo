package com.baeldung.kafka.embedded;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.stream.IntStream;

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
    void executeOrder_SHOULD_returnAfterKafkaResponse() throws Exception {
        String orderId = "order#1";

        producer.send("test-topic", orderId);

        String response = orderService.executeOrder(orderId);

        verify(consumer, times(1)).receive(any());
        verify(orderService, times(1)).executionReport(orderId);
        assertThat(response, equalTo(orderId));
    }

    @Test
    void executeOrder_SHOULD_returnAfterKafkaResponse2() throws Exception {
        String orderId = "order#1";


        String response = orderService.executeOrder(orderId);

        producer.send("test-topic", orderId);


        verify(consumer, times(1)).receive(any());
        verify(orderService, times(1)).executionReport(orderId);
        assertThat(response, equalTo(orderId));
    }

    @Test
    void executeOrder_SHOULD_returnAfterKafkaResponseWithStream() throws Exception {
        String orderId = "order#1";


        IntStream.range(0, 10).forEach(i -> {
            try {
                orderService.executeOrder("order#"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        IntStream.range(0, 10).forEach(i -> producer.send("test-topic", "order#" + i));



        verify(consumer, times(1)).receive(any());
        verify(orderService, times(1)).executionReport(orderId);
//        assertThat(response, equalTo(orderId));
    }
}