package com.order.api.application.interactors;

import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.domain.enums.EventType;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.exceptions.PolicyNotFoundException;
import com.order.api.domain.usecases.CancelInsurancePolicy;
import com.order.api.domain.ports.EventPublisher;

import java.time.LocalDateTime;
import java.util.UUID;

public record CancelInsurancePolicyInteractor (InsurancePolicyGateway insurancePolicyGateway, EventPublisher eventPublisher,
                                              HistoryEntryGateway historyEntryGateway) implements CancelInsurancePolicy {
    public InsurancePolicy cancelPolicy(UUID policyId) {
        InsurancePolicy insurancePolicy = insurancePolicyGateway.findById(policyId);
        if (insurancePolicy == null) {
            throw new PolicyNotFoundException(policyId);
        }
        insurancePolicy.cancel();
        publishEvent(insurancePolicy);
        return insurancePolicy;
    }

    private void publishEvent(InsurancePolicy insurancePolicy) {
        InsurancePolicyEvent event = new InsurancePolicyEvent(
                UUID.randomUUID(),
                EventType.ORDER_CANCELED,
                insurancePolicy.getId(),
                insurancePolicy.getCustomerId(),
                insurancePolicy.getStatus().getStatusName(),
                LocalDateTime.now().toString()
        );
        eventPublisher.publish(event);
        HistoryEntry historyEntry = new HistoryEntry(LocalDateTime.now(), insurancePolicy.getStatus().getStatusName());
        historyEntryGateway.create(historyEntry, insurancePolicy);
    }
}
