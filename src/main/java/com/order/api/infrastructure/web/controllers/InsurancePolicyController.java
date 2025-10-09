package com.order.api.infrastructure.web.controllers;

import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyResponse;
import com.order.api.infrastructure.web.mapper.InsurancePolicyDTOMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyController {
    private final CreateInsurancePolicy createInsurancePolicy;
    private final InsurancePolicyDTOMapper insurancePolicyDTOMapper;

    public InsurancePolicyController(CreateInsurancePolicy createInsurancePolicy, InsurancePolicyDTOMapper insurancePolicyDTOMapper) {
        this.createInsurancePolicy = createInsurancePolicy;
        this.insurancePolicyDTOMapper = insurancePolicyDTOMapper;
    }

    @PostMapping
    CreateInsurancePolicyResponse create(@RequestBody CreateInsurancePolicyRequest request){
        var insurancePolicy = insurancePolicyDTOMapper.toInsurancePolicy(request);
        var createdInsurancePolicy = createInsurancePolicy.create(insurancePolicy);
        return insurancePolicyDTOMapper.toResponse(createdInsurancePolicy);
    }
}
