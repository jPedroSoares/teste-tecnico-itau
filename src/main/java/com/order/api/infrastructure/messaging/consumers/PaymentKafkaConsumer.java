package com.order.api.infrastructure.messaging.consumers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
public class PaymentKafkaConsumer {
    @KafkaListener(topics = "payment-topic")
    public void consume(
            @Payload String message) {
        log.info("Received message: '{}'", message);
    }
}
