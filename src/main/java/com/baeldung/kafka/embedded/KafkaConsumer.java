package com.baeldung.kafka.embedded;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrderService service;

    @KafkaListener(topics = "test-topic")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        log.info("received payload='{}'", consumerRecord.toString());
        try {
            service.executionReport(consumerRecord.value().toString());
        } catch (Exception e) {

            log.error("MyError", e);
        }
    }

}
