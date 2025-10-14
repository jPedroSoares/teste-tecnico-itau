package com.order.api.application.interactors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.domain.usecases.EventProcessor;

import java.time.LocalDateTime;

public record ProcessEventImp(ObjectMapper objectMapper, FindInsurancePolicy findInsurancePolicy,
                              HistoryEntryGateway historyEntryGateway) implements EventProcessor {
    public void processEvent(String eventJson) {
        try {
            InsurancePolicyEvent event = objectMapper.readValue(eventJson, InsurancePolicyEvent.class);
            InsurancePolicy insurancePolicy = findInsurancePolicy.find(event.policyId());
            if (insurancePolicy == null) {
                throw new IllegalArgumentException("Insurance policy not found for ID: " + event.policyId());
            }
            HistoryEntry historyEntry = new HistoryEntry(LocalDateTime.parse(event.timestamp()), event.status());
            historyEntryGateway.create(historyEntry, insurancePolicy);
        } catch (Exception e) {
            System.out.println("Evento com erro: " + e.getMessage());
        }
    }
}
