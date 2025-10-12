package com.order.api.infrastructure.messaging;

import com.order.api.application.dto.InsurancePolicyEvent;
import org.springframework.kafka.core.KafkaTemplate;


public class KafkaProducer implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicName;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate, String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(InsurancePolicyEvent event){
        String key = event.policyId().toString();
        kafkaTemplate.send(topicName, key, event);
    }
}
