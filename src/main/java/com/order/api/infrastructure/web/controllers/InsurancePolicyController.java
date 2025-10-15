package com.order.api.infrastructure.web.controllers;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CancelInsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import com.order.api.infrastructure.web.dto.InsurancePolicyResponse;
import com.order.api.infrastructure.web.mapper.InsurancePolicyDTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    InsurancePolicyResponse create(@RequestBody CreateInsurancePolicyRequest request) {
        log.info("Creating insurance policy: customerId={}, category={}, insuredAmount={}",
                request.customerId(), request.category(), request.insuredAmount());
        try {
            var insurancePolicy = insurancePolicyDTOMapper.toInsurancePolicy(request);
            var createdInsurancePolicy = createInsurancePolicy.create(insurancePolicy);
            log.info("Insurance policy created successfully: policyId={}, customerId={}, status={}",
                    createdInsurancePolicy.getId(), createdInsurancePolicy.getCustomerId(),
                    createdInsurancePolicy.getStatus().getStatusName());
            return insurancePolicyDTOMapper.toResponse(createdInsurancePolicy);
        } catch (Exception e) {
            log.error("Failed to create insurance policy: customerId={}, error={}",
                    request.customerId(), e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create insurance policy: " + e.getMessage()
            );
        }
    }

    @GetMapping("/policy/{policyId}")
    InsurancePolicyResponse getByPolicyId(
            @PathVariable UUID policyId) {
        log.debug("Fetching policy by ID: {}", policyId);
        try {
            InsurancePolicy response = findInsurancePolicy.find(policyId);
            if (response == null) {
                log.error("Policy not found with id: {}",
                        policyId);
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Policy not found with id: " + policyId
                );
            }
            return insurancePolicyDTOMapper.toResponse(response);
        } catch (Exception e) {
            log.error("Error fetching policy by ID: {}, error={}",
                    policyId, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/customer/{customerId}")
    List<InsurancePolicyResponse> getByCustomerId(
            @PathVariable UUID customerId) {
        log.debug("Fetching insurance policies by customerId: {}", customerId);
        try {
            List<InsurancePolicy> response = findInsurancePolicy.findByCustomerId(customerId);
            if (response.isEmpty()) {
                log.error("No policies found for CustomerId: {}", customerId);
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Policies not found for CustomerId: " + customerId
                );
            }
            return insurancePolicyDTOMapper.toResponse(response);
        } catch (Exception e) {
            log.error("Error fetching policies by CustomerId: {}, error={}",
                    customerId, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{policyId}/cancel")
    ResponseEntity<InsurancePolicyResponse> cancel(@PathVariable UUID policyId) {
        log.info("Canceling insurance policy with id: {}", policyId);
        try {
            InsurancePolicy canceledPolicy = cancelInsurancePolicy.cancelPolicy(policyId);
            if (canceledPolicy == null) {
                log.error("Policy not found with id: {}", policyId);
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Policy not found with id: " + policyId
                );
            }
            return ResponseEntity.ok(insurancePolicyDTOMapper.toReadResponse(canceledPolicy));
        } catch (Exception e) {
            log.error("Error canceling policy with id: {}, error={}",
                    policyId, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
