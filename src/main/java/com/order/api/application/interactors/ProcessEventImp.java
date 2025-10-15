package com.order.api.application.interactors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.domain.usecases.EventProcessor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public record ProcessEventImp(ObjectMapper objectMapper, FindInsurancePolicy findInsurancePolicy,
                              HistoryEntryGateway historyEntryGateway) implements EventProcessor {
    public void processEvent(String eventJson) {
        try {
            log.debug("Processing Kafka event: {}", eventJson);
            InsurancePolicyEvent event = objectMapper.readValue(eventJson, InsurancePolicyEvent.class);
            log.info("Processing event for policy: {}, customer: {}, status: {}",
                    event.policyId(), event.customerId(), event.status());
            InsurancePolicy insurancePolicy = findInsurancePolicy.find(event.policyId());
            if (insurancePolicy == null) {
                log.error("Policy not found for event processing: policyId={}, eventId={}",
                        event.policyId(), event.eventId());
                throw new IllegalArgumentException("Insurance policy not found for ID: " + event.policyId());
            }
            HistoryEntry historyEntry = new HistoryEntry(LocalDateTime.parse(event.timestamp()), event.status());
            historyEntryGateway.create(historyEntry, insurancePolicy);
            log.info("Event processed successfully: policyId={}, newStatus={}",
                    event.policyId(), event.status());
        } catch (Exception e) {
            log.error("Failed to process Kafka event: eventJson={}, error={}",
                    eventJson, e.getMessage(), e);
            throw new RuntimeException("Error processing event: " + e.getMessage(), e);
        }
    }
}
