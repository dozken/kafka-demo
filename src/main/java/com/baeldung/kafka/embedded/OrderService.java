package com.baeldung.kafka.embedded;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class OrderService {

    final CountDownLatch countDownLatch = new CountDownLatch(2);

    public long executeOrder() throws InterruptedException {
        log.info("executeOrder, execute in market");
        log.info("executeOrder, update order");

        log.info("executeOrder, countDownLatch before=[{}]", countDownLatch);
        countDownLatch.countDown();
        log.info("executeOrder, countDownLatch after=[{}]", countDownLatch);

        countDownLatch.await();
        log.info("executeOrder, all operations done");
        return countDownLatch.getCount();
    }

    public void executionReport(String consumerMessage) {
        log.info("executionReport, consumerMessage=[{}]", consumerMessage);

        log.info("executionReport, countDownLatch before=[{}]", countDownLatch);
        countDownLatch.countDown();
        log.info("executionReport, countDownLatch after=[{}]", countDownLatch);
    }

}
