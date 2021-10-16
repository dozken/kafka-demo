package com.baeldung.kafka.embedded;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;

@Getter
@Setter
@RequiredArgsConstructor
public class KafkaConsumerLatch {
    private Exception exception;
    private final CountDownLatch latch;
}
