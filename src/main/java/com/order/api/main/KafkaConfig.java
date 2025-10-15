package com.order.api.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.application.interactors.ProcessEventImp;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.domain.usecases.EventProcessor;
import com.order.api.infrastructure.messaging.consumers.PaymentKafkaConsumer;
import com.order.api.infrastructure.messaging.KafkaProducer;
import com.order.api.infrastructure.messaging.consumers.SubscriptionKafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    KafkaProducer kafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaProducer(kafkaTemplate, "orders-topic");
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public PaymentKafkaConsumer paymentKafkaConsumer(EventProcessor eventProcessor) {
        return new PaymentKafkaConsumer(eventProcessor);
    }

    @Bean
    public SubscriptionKafkaConsumer subscriptionKafkaConsumer(EventProcessor eventProcessor) {
        return new SubscriptionKafkaConsumer(eventProcessor);
    }

    @Bean
    public EventProcessor eventProcessor(FindInsurancePolicy findInsurancePolicy,
                                         HistoryEntryGateway historyEntryGateway, InsurancePolicyGateway insurancePolicyGateway) {
        ObjectMapper objectMapper = new ObjectMapper();
        return new ProcessEventImp(objectMapper, findInsurancePolicy, historyEntryGateway, insurancePolicyGateway);
    }
}
