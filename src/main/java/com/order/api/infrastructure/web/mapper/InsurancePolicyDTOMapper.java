package com.order.api.infrastructure.web.mapper;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import com.order.api.infrastructure.web.dto.InsurancePolicyResponse;

import java.util.List;

public class InsurancePolicyDTOMapper {
    public InsurancePolicyResponse toResponse (InsurancePolicy insurancePolicy) {
        return new InsurancePolicyResponse(
                insurancePolicy.getId(),
                insurancePolicy.getCustomerId(),
                insurancePolicy.getProductId(),
                insurancePolicy.getCategory(),
                insurancePolicy.getSalesChannel(),
                insurancePolicy.getPaymentMethod(),
                insurancePolicy.getStatus().getStatusName(),
                insurancePolicy.getCreatedAt(),
                insurancePolicy.getFinishedAt(),
                insurancePolicy.getTotalMonthlyPremiumAmount(),
                insurancePolicy.getInsuredAmount(),
                insurancePolicy.getCoverages(),
                insurancePolicy.getAssistances(),
                insurancePolicy.getHistory()
        );
    }

     public InsurancePolicy toInsurancePolicy (CreateInsurancePolicyRequest request){
        return new InsurancePolicy(
                null,
                request.customerId(),
                request.productId(),
                request.category(),
                request.coverages(),
                request.assistances(),
                request.totalMonthlyPremiumAmount(),
                null,
                null,
                request.insuredAmount(),
                request.paymentMethod(),
                request.salesChannel()
        );
     }

    public List<InsurancePolicyResponse> toResponse(List<InsurancePolicy> response) {
        if (response.isEmpty()) {
            return null;
        }
        return response.stream().map(this::toResponse).toList();
    }

    public InsurancePolicyResponse toReadResponse(InsurancePolicy entity) {
        return new InsurancePolicyResponse(
                entity.getId(),
                entity.getCustomerId(),
                entity.getProductId(),
                entity.getCategory(),
                entity.getSalesChannel(),
                entity.getPaymentMethod(),
                entity.getStatus().getStatusName(),
                entity.getCreatedAt(),
                entity.getFinishedAt(),
                entity.getTotalMonthlyPremiumAmount(),
                entity.getInsuredAmount(),
                entity.getCoverages(),
                entity.getAssistances(),
                entity.getHistory()
        );
    }
}
