package com.order.api.infrastructure.gateways;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;
import com.order.api.infrastructure.persistence.InsurancePolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyRepositoryGatewayTest {

    @Mock
    private InsurancePolicyMapper insurancePolicyMapper;

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @InjectMocks
    private InsurancePolicyRepositoryGateway insurancePolicyRepositoryGateway;

    private InsurancePolicy buildInsurancePolicy() {
        return new InsurancePolicy(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                PolicyCategory.PROPERTY,
                Map.of("any_coverage", BigDecimal.valueOf(5000)),
                List.of("any_assistance"),
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(50000),
                PaymentMethod.PIX,
                SalesChannel.MOBILE
        );
    }

    private InsurancePolicyEntity buildInsurancePolicyEntity() {
        return new InsurancePolicyEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                PolicyCategory.PROPERTY,
                InsurancePolicyStatus.RECEIVED,
                SalesChannel.MOBILE,
                PaymentMethod.PIX,
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(50000.0),
                Map.of("any_coverage", BigDecimal.valueOf(5000.0)),
                List.of("any_assistance")
        );
    }

    @Test
    @DisplayName("Should map and save an insurance policy successfully")
    void createInsurancePolicyCase1() {
        InsurancePolicy domainObj = buildInsurancePolicy();
        InsurancePolicyEntity entity = buildInsurancePolicyEntity();
        InsurancePolicyEntity savedEntity = buildInsurancePolicyEntity();
        InsurancePolicy domainObjResult = buildInsurancePolicy();

        when(insurancePolicyMapper.toEntity(domainObj)).thenReturn(entity);
        when(insurancePolicyRepository.save(entity)).thenReturn(savedEntity);
        when(insurancePolicyMapper.toDomain(savedEntity)).thenReturn(domainObjResult);

        InsurancePolicy result = insurancePolicyRepositoryGateway.createInsurancePolicy(domainObj);

        assertNotNull(result);
        assertEquals(domainObjResult, result);

        verify(insurancePolicyMapper).toEntity(domainObj);
        verify(insurancePolicyRepository).save(entity);
        verify(insurancePolicyMapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should throw if insurancePolicyMapper.toEntity throws")
    void createInsurancePolicyCase2() {
        InsurancePolicy insurancePolicy = buildInsurancePolicy();

        when(insurancePolicyMapper.toEntity(insurancePolicy)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> insurancePolicyRepositoryGateway.createInsurancePolicy(insurancePolicy));
    }

    @Test
    @DisplayName("Should throw if insurancePolicyMapper.toDomain throws")
    void createInsurancePolicyCase3() {
        InsurancePolicy insurancePolicy = buildInsurancePolicy();
        InsurancePolicyEntity insurancePolicyEntity = buildInsurancePolicyEntity();

        when(insurancePolicyMapper.toDomain(insurancePolicyEntity)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> insurancePolicyRepositoryGateway.createInsurancePolicy(insurancePolicy));
    }
}