package com.order.api.application.interactors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.domain.usecases.EventProcessor;

import java.time.LocalDateTime;

public class ProcessEventImp implements EventProcessor {
    private final ObjectMapper objectMapper;
    private final FindInsurancePolicy findInsurancePolicy;
    private final HistoryEntryGateway historyEntryGateway;

    public ProcessEventImp(
            ObjectMapper objectMapper,
            FindInsurancePolicy findInsurancePolicy,
            HistoryEntryGateway historyEntryGateway
    ) {
        this.objectMapper = objectMapper;
        this.findInsurancePolicy = findInsurancePolicy;
        this.historyEntryGateway = historyEntryGateway;
    }
    public void processEvent(String eventJson) {
        try {
        InsurancePolicyEvent event = objectMapper.readValue(eventJson, InsurancePolicyEvent.class);
        InsurancePolicy insurancePolicy = findInsurancePolicy.find(event.policyId());
        HistoryEntry historyEntry = new HistoryEntry(LocalDateTime.parse(event.timestamp()), event.status());
        historyEntryGateway.create(historyEntry, insurancePolicy);
        } catch (Exception e) {
            System.out.println("Evento com erro: " + e.getMessage());
        }
    }
}
