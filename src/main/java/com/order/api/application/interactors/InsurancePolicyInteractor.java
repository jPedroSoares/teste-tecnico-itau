package com.order.api.application.interactors;

import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.application.dto.PolicyValidationRequest;
import com.order.api.application.dto.PolicyValidationResponse;
import com.order.api.domain.enums.EventType;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.application.gateways.PolicyValidationGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.domain.usecases.ValidateInsurancePolicy;
import com.order.api.domain.ports.EventPublisher;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public record InsurancePolicyInteractor(
        InsurancePolicyGateway insurancePolicyGateway,
        PolicyValidationGateway policyValidationGateway,
        ValidateInsurancePolicy validateInsurancePolicy,
        EventPublisher eventPublisher,
        HistoryEntryGateway historyEntryGateway
) implements CreateInsurancePolicy {

    public InsurancePolicy create(InsurancePolicy insurancePolicy) {
        log.info("Creating insurance policy for customer: {}", insurancePolicy.getCustomerId());
        try {
            InsurancePolicy createdInsurancePolicy = insurancePolicyGateway.createInsurancePolicy(insurancePolicy);
            createdInsurancePolicy.setHistory(insurancePolicy.getHistory());
            publishEvent(createdInsurancePolicy, EventType.ORDER_CREATED);
            PolicyValidationRequest policyValidationRequest = new PolicyValidationRequest(createdInsurancePolicy.getId(), createdInsurancePolicy.getCustomerId());
            PolicyValidationResponse policyValidationResponse = policyValidationGateway.validate(policyValidationRequest);
            boolean isValid = validateInsurancePolicy.validate(insurancePolicy, policyValidationResponse.classification());
            if (isValid) {
                createdInsurancePolicy.validate();
                publishEvent(createdInsurancePolicy, EventType.ORDER_VALIDATED);
                log.info("Insurance policy validated for customer: {}", createdInsurancePolicy.getCustomerId());
            } else {
                createdInsurancePolicy.reject();
                publishEvent(createdInsurancePolicy, EventType.ORDER_REJECTED);
                log.info("Insurance policy rejected for customer: {}", createdInsurancePolicy.getCustomerId());
            }
            log.info("Insurance policy created successfully with ID: {}", createdInsurancePolicy.getId());
            return createdInsurancePolicy;
        } catch (Exception e) {
            log.error("Failed to create insurance policy for customer: {}",
                    insurancePolicy.getCustomerId(), e);
            throw e;
        }

    }

    private void publishEvent(InsurancePolicy insurancePolicy, EventType eventType) {
        InsurancePolicyEvent event = new InsurancePolicyEvent(
                UUID.randomUUID(),
                eventType,
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
