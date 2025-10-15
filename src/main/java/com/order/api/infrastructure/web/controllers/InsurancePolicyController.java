package com.order.api.infrastructure.web.controllers;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CancelInsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import com.order.api.infrastructure.web.dto.InsurancePolicyResponse;
import com.order.api.infrastructure.web.mapper.InsurancePolicyDTOMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
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
    ResponseEntity<InsurancePolicyResponse> create(@Valid @RequestBody CreateInsurancePolicyRequest request) {
        log.info("Creating insurance policy: customerId={}, category={}, insuredAmount={}",
                request.customerId(), request.category(), request.insuredAmount());
        
        var insurancePolicy = insurancePolicyDTOMapper.toInsurancePolicy(request);
        var createdInsurancePolicy = createInsurancePolicy.create(insurancePolicy);
        
        log.info("Insurance policy created successfully: policyId={}, customerId={}, status={}",
                createdInsurancePolicy.getId(), createdInsurancePolicy.getCustomerId(),
                createdInsurancePolicy.getStatus().getStatusName());

        return ResponseEntity.ok(insurancePolicyDTOMapper.toResponse(createdInsurancePolicy));
    }

    @GetMapping("/policy/{policyId}")
    InsurancePolicyResponse getByPolicyId(@PathVariable UUID policyId) {
        log.debug("Fetching policy by ID: {}", policyId);
        
        InsurancePolicy response = findInsurancePolicy.find(policyId);
        return insurancePolicyDTOMapper.toResponse(response);
    }

    @GetMapping("/customer/{customerId}")
    List<InsurancePolicyResponse> getByCustomerId(@PathVariable UUID customerId) {
        log.debug("Fetching insurance policies by customerId: {}", customerId);
        
        List<InsurancePolicy> response = findInsurancePolicy.findByCustomerId(customerId);
        return insurancePolicyDTOMapper.toResponse(response);
    }

    @PatchMapping("/{policyId}/cancel")
    ResponseEntity<InsurancePolicyResponse> cancel(@PathVariable UUID policyId) {
        log.info("Canceling insurance policy with id: {}", policyId);
        
        InsurancePolicy canceledPolicy = cancelInsurancePolicy.cancelPolicy(policyId);
        return ResponseEntity.ok(insurancePolicyDTOMapper.toReadResponse(canceledPolicy));
    }
}
