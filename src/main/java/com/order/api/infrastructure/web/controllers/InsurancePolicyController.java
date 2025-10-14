package com.order.api.infrastructure.web.controllers;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CancelInsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import com.order.api.infrastructure.web.dto.InsurancePolicyResponse;
import com.order.api.infrastructure.web.mapper.InsurancePolicyDTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyController {
    private final CreateInsurancePolicy createInsurancePolicy;
    private final InsurancePolicyDTOMapper insurancePolicyDTOMapper;
    private final FindInsurancePolicy findInsurancePolicy;
    private final CancelInsurancePolicy cancelInsurancePolicy;

    public InsurancePolicyController(
            CreateInsurancePolicy createInsurancePolicy,
            InsurancePolicyDTOMapper insurancePolicyDTOMapper,
            FindInsurancePolicy findInsurancePolicy,
            CancelInsurancePolicy cancelInsurancePolicy) {
        this.createInsurancePolicy = createInsurancePolicy;
        this.insurancePolicyDTOMapper = insurancePolicyDTOMapper;
        this.findInsurancePolicy = findInsurancePolicy;
        this.cancelInsurancePolicy = cancelInsurancePolicy;
    }

    @PostMapping
    InsurancePolicyResponse create(@RequestBody CreateInsurancePolicyRequest request){
        var insurancePolicy = insurancePolicyDTOMapper.toInsurancePolicy(request);
        var createdInsurancePolicy = createInsurancePolicy.create(insurancePolicy);
        return insurancePolicyDTOMapper.toResponse(createdInsurancePolicy);
    }

    @GetMapping("/policy/{policyId}")
    InsurancePolicyResponse getByPolicyId(
            @PathVariable UUID policyId) {
        InsurancePolicy response = findInsurancePolicy.find(policyId);
        if (response == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Policy not found with id: " + policyId
            );
        }
        return insurancePolicyDTOMapper.toResponse(response);
    }

    @GetMapping("/customer/{customerId}")
    List<InsurancePolicyResponse> getByCustomerId(
            @PathVariable UUID customerId) {
        List<InsurancePolicy> response = findInsurancePolicy.findByCustomerId(customerId);
        if (response.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Policies not found for CustomerId: " + customerId
            );
        }
        return insurancePolicyDTOMapper.toResponse(response);
    }

    @PatchMapping("/{policyId}/cancel")
    ResponseEntity<InsurancePolicyResponse> cancel(@PathVariable UUID policyId) {
        InsurancePolicy canceledPolicy = cancelInsurancePolicy.cancelPolicy(policyId);
        if (canceledPolicy == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Policy not found with id: " + policyId
            );
        }
        return ResponseEntity.ok(insurancePolicyDTOMapper.toReadResponse(canceledPolicy));
    }
}
