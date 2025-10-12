package com.order.api.infrastructure.gateways;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;
import com.order.api.infrastructure.persistence.InsurancePolicyRepository;

import java.util.Optional;
import java.util.UUID;

public class InsurancePolicyRepositoryGateway implements InsurancePolicyGateway {
    private final InsurancePolicyMapper insurancePolicyMapper;
    private final InsurancePolicyRepository insurancePolicyRepository;

    public InsurancePolicyRepositoryGateway(InsurancePolicyMapper insurancePolicyMapper, InsurancePolicyRepository insurancePolicyRepository) {
        this.insurancePolicyMapper = insurancePolicyMapper;
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public InsurancePolicy createInsurancePolicy(InsurancePolicy insurancePolicyDomainObj) {
        InsurancePolicyEntity insurancePolicyEntity = insurancePolicyMapper.toEntity(insurancePolicyDomainObj);
        InsurancePolicyEntity savedEntity = insurancePolicyRepository.save(insurancePolicyEntity);
        return insurancePolicyMapper.toDomain(savedEntity);
    }

    public InsurancePolicy findById(UUID id) {
        Optional<InsurancePolicyEntity> entity = insurancePolicyRepository.findById(id);
        return insurancePolicyMapper.toDomain(entity);
    }
}
