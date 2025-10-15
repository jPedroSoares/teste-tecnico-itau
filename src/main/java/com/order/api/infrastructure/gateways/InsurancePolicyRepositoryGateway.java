package com.order.api.infrastructure.gateways;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;
import com.order.api.infrastructure.persistence.InsurancePolicyRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record InsurancePolicyRepositoryGateway(InsurancePolicyMapper insurancePolicyMapper,
                                               InsurancePolicyRepository insurancePolicyRepository) implements InsurancePolicyGateway {

    public InsurancePolicy createInsurancePolicy(InsurancePolicy insurancePolicyDomainObj) {
        InsurancePolicyEntity insurancePolicyEntity = insurancePolicyMapper.toEntity(insurancePolicyDomainObj);
        InsurancePolicyEntity savedEntity = insurancePolicyRepository.save(insurancePolicyEntity);
        return insurancePolicyMapper.toDomain(savedEntity);
    }

    public InsurancePolicy findById(UUID id) {
        Optional<InsurancePolicyEntity> entity = insurancePolicyRepository.findById(id);
        return insurancePolicyMapper.toDomain(entity);
    }

    public List<InsurancePolicy> findByCustomerId(UUID customerId) {
        List<InsurancePolicyEntity> entities = insurancePolicyRepository.findByCustomerId(customerId);
        return insurancePolicyMapper.toDomain(entities);
    }

    public void cancelInsurancePolicy(UUID id) {
        insurancePolicyRepository.updateStatusById(id, InsurancePolicyStatus.CANCELED);
    }

    public void updateStatus(UUID id, InsurancePolicyStatus status) {
        insurancePolicyRepository.updateStatusById(id, status);
    }

    public void finishPolicy(InsurancePolicy insurancePolicy) {
        insurancePolicyRepository.finish(insurancePolicy.getId(), insurancePolicy.getFinishedAt());
    }
}
