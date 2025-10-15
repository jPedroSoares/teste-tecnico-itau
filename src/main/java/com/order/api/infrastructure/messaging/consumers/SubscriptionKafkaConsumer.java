package com.order.api.infrastructure.messaging.consumers;

import com.order.api.domain.usecases.EventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
public record SubscriptionKafkaConsumer (EventProcessor eventProcessor) {
    @KafkaListener(topics = "insurance-subscriptions-topic")
    public void consume(
            @Payload String message) {
        log.info("Received message: '{}'", message);
        eventProcessor.processEvent(message);
    }
}
