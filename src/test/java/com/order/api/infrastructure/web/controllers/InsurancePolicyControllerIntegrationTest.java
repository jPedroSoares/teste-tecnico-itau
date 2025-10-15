package com.order.api.infrastructure.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InsurancePolicyControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    CreateInsurancePolicyRequest buildRequest() {
        return new CreateInsurancePolicyRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                PolicyCategory.AUTO,
                SalesChannel.WEB_SITE,
                PaymentMethod.CREDIT_CARD,
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(10000.0),
                null,
                null
        );
    }

    @Test
    @DisplayName("GET /api/insurance-policies/policy/{policyId} - Not Found")
    void getInsurancePolicyById() throws Exception {
        UUID policyId = UUID.randomUUID();
        mockMvc.perform(get("/api/insurance-policies/policy/" + policyId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/insurance-policies/customer/{customerId} - Not Found")
    void getInsurancePoliciesByCustomerId() throws Exception {
        UUID customerId = UUID.randomUUID();
        mockMvc.perform(get("/api/insurance-policies/customer/" + customerId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/insurance-policies - Create Insurance VALIDATED Policy")
    void createInsurancePolicy() throws Exception {
        CreateInsurancePolicyRequest request = buildRequest();
        mockMvc.perform(post("/api/insurance-policies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("POST /api/insurance-policies - Create Insurance REJECTED Policy")
    void createInsurancePolicyRejected() throws Exception {
        CreateInsurancePolicyRequest request = new CreateInsurancePolicyRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                PolicyCategory.AUTO,
                SalesChannel.WEB_SITE,
                PaymentMethod.CREDIT_CARD,
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(10000000.0),
                null,
                null
        );
        mockMvc.perform(post("/api/insurance-policies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").exists())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    @DisplayName("PATCH /api/insurance-policies/{policyId}/cancel - Cancel Insurance Policy")
    void cancelInsurancePolicy() throws Exception {
        CreateInsurancePolicyRequest request = buildRequest();
        String responseJson = mockMvc.perform(post("/api/insurance-policies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var createdPolicy = objectMapper.readTree(responseJson);
        UUID policyId = UUID.fromString(createdPolicy.get("id").asText());

        mockMvc.perform(patch("/api/insurance-policies/" + policyId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(policyId.toString()))
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @Test
    @DisplayName("PATCH /api/insurance-policies/{policyId}/cancel - Cancel Non-Existing Insurance Policy")
    void cancelNonExistingInsurancePolicy() throws Exception {
        UUID policyId = UUID.randomUUID();
        mockMvc.perform(patch("/api/insurance-policies/" + policyId + "/cancel"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/insurance-policies/policy/{policyId} - Get Insurance Policy by ID")
    void getInsurancePolicyByIdFound() throws Exception {
        CreateInsurancePolicyRequest request = buildRequest();
        String responseJson = mockMvc.perform(post("/api/insurance-policies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var createdPolicy = objectMapper.readTree(responseJson);
        UUID policyId = UUID.fromString(createdPolicy.get("id").asText());

        mockMvc.perform(get("/api/insurance-policies/policy/" + policyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(policyId.toString()));
    }

    @Test
    @DisplayName("GET /api/insurance-policies/customer/{customerId} - Get Insurance Policies by Customer ID")
    void getInsurancePoliciesByCustomerIdFound() throws Exception {
        CreateInsurancePolicyRequest request = buildRequest();
        String responseJson = mockMvc.perform(post("/api/insurance-policies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var createdPolicy = objectMapper.readTree(responseJson);
        UUID customerId = UUID.fromString(createdPolicy.get("customerId").asText());

        mockMvc.perform(get("/api/insurance-policies/customer/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(customerId.toString()));
    }
}