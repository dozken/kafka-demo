package com.baeldung.kafka.embedded;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;

@Service
@Slf4j
public class OrderService {

    final Map<String, KafkaConsumerLatch> latchMap = new ConcurrentHashMap<>();

    @SneakyThrows
    public String executeOrder(String orderId){
        latchMap.put(orderId, new KafkaConsumerLatch( new CountDownLatch(1)));
        log.info("executeOrder, execute in market");
        log.info("executeOrder, update order");

        KafkaConsumerLatch kafkaConsumerLatch = latchMap.get(orderId);

        if(kafkaConsumerLatch.getException() != null){
            throw kafkaConsumerLatch.getException();
        }

        kafkaConsumerLatch.getLatch().await();

        latchMap.remove(orderId);
        log.info("executeOrder, all operations done");
        return orderId;
    }

    public void executionReport(String orderId) {
        KafkaConsumerLatch kafkaConsumerLatch = latchMap.get(orderId);

        try {
            log.info("executionReport, orderId=[{}]", orderId);

            kafkaConsumerLatch.setException(new RejectedExecutionException("Order Rejected by market"));
        } finally {
            CountDownLatch countDownLatch = kafkaConsumerLatch.getLatch();
            log.info("executionReport, countDownLatch before=[{}]", countDownLatch);
            countDownLatch.countDown();
            log.info("executionReport, countDownLatch after=[{}]", countDownLatch);

        }

    }

}


