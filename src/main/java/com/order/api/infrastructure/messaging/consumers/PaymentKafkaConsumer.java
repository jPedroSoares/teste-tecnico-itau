package com.order.api.infrastructure.messaging.consumers;

import com.order.api.domain.usecases.EventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
public class PaymentKafkaConsumer {
    private final EventProcessor eventProcessor;
    public PaymentKafkaConsumer(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @KafkaListener(topics = "payment-topic")
    public void consume(
            @Payload String message) {
        eventProcessor.processEvent(message);
        log.info("Received message: '{}'", message);
    }
}
