package com.order.api.application.interactors;

import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.application.dto.PolicyValidationRequest;
import com.order.api.application.dto.PolicyValidationResponse;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.application.gateways.PolicyValidationGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.CustomerType;
import com.order.api.domain.enums.EventType;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import com.order.api.domain.ports.EventPublisher;
import com.order.api.domain.states.Pending;
import com.order.api.domain.usecases.ValidateInsurancePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyInteractorTest {

    @Mock
    private InsurancePolicyGateway insurancePolicyGateway;

    @Mock
    private PolicyValidationGateway policyValidationGateway;

    @Mock
    private ValidateInsurancePolicy validateInsurancePolicy;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private HistoryEntryGateway historyEntryGateway;

    @Mock
    private InsurancePolicy mockCreatedPolicy;

    private InsurancePolicyInteractor insurancePolicyInteractor;

    private InsurancePolicy inputPolicy;
    private PolicyValidationResponse validationResponse;
    private UUID customerId;
    private UUID policyId;

    @BeforeEach
    void setUp() {
        insurancePolicyInteractor = new InsurancePolicyInteractor(
                insurancePolicyGateway,
                policyValidationGateway,
                validateInsurancePolicy,
                eventPublisher,
                historyEntryGateway
        );

        policyId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        inputPolicy = buildInsurancePolicy();
        
        lenient().when(mockCreatedPolicy.getId()).thenReturn(policyId);
        lenient().when(mockCreatedPolicy.getCustomerId()).thenReturn(customerId);
        lenient().when(mockCreatedPolicy.getStatus()).thenReturn(new Pending());
        
        validationResponse = new PolicyValidationResponse(
                policyId,
                customerId,
                "2023-10-13T10:15:30",
                CustomerType.REGULAR,
                List.of()
        );
    }

    private InsurancePolicy buildInsurancePolicy() {
        return new InsurancePolicy(
                null,
                customerId,
                UUID.randomUUID(),
                PolicyCategory.PROPERTY,
                Map.of("any_coverage", BigDecimal.valueOf(5000)),
                List.of("any_assistance"),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(50000.0),
                PaymentMethod.PIX,
                SalesChannel.MOBILE
        );
    }

    private void setupSuccessfulCreation() throws Exception {
        lenient().when(insurancePolicyGateway.createInsurancePolicy(inputPolicy)).thenReturn(mockCreatedPolicy);
        lenient().when(policyValidationGateway.validate(any(PolicyValidationRequest.class))).thenReturn(validationResponse);
        lenient().when(historyEntryGateway.create(any(HistoryEntry.class), eq(mockCreatedPolicy))).thenReturn(new HistoryEntry(null, null));
    }

    private void setupValidationApproved() {
        when(validateInsurancePolicy.validate(inputPolicy, CustomerType.REGULAR)).thenReturn(true);
    }

    private void setupValidationRejected() {
        when(validateInsurancePolicy.validate(inputPolicy, CustomerType.REGULAR)).thenReturn(false);
    }

    @Test
    @DisplayName("Should create and validate insurance policy successfully")
    void shouldCreateAndValidateInsurancePolicySuccessfully() throws Exception {
        setupSuccessfulCreation();
        setupValidationApproved();

        InsurancePolicy result = insurancePolicyInteractor.create(inputPolicy);

        assertNotNull(result);
        assertEquals(policyId, result.getId());
        assertEquals(customerId, result.getCustomerId());

        verify(insurancePolicyGateway).createInsurancePolicy(inputPolicy);
        verify(policyValidationGateway).validate(any(PolicyValidationRequest.class));
        verify(validateInsurancePolicy).validate(inputPolicy, CustomerType.REGULAR);

        verify(mockCreatedPolicy).validate();

        verify(eventPublisher, times(2)).publish(any(InsurancePolicyEvent.class));
        
        verify(historyEntryGateway, times(2)).create(any(HistoryEntry.class), eq(mockCreatedPolicy));
    }

    @Test
    @DisplayName("Should create insurance policy but reject validation")
    void shouldCreateInsurancePolicyButRejectValidation() throws Exception {
        setupSuccessfulCreation();
        setupValidationRejected();

        InsurancePolicy result = insurancePolicyInteractor.create(inputPolicy);

        assertNotNull(result);
        assertEquals(policyId, result.getId());

        verify(insurancePolicyGateway).createInsurancePolicy(inputPolicy);
        verify(policyValidationGateway).validate(any(PolicyValidationRequest.class));
        verify(validateInsurancePolicy).validate(inputPolicy, CustomerType.REGULAR);

        verify(mockCreatedPolicy).reject();

        verify(eventPublisher, times(2)).publish(any(InsurancePolicyEvent.class));
        
        verify(historyEntryGateway, times(2)).create(any(HistoryEntry.class), eq(mockCreatedPolicy));
    }

    @Test
    @DisplayName("Should publish correct events for approved validation")
    void shouldPublishCorrectEventsForApprovedValidation() throws Exception {
        setupSuccessfulCreation();
        setupValidationApproved();

        insurancePolicyInteractor.create(inputPolicy);

        ArgumentCaptor<InsurancePolicyEvent> eventCaptor = ArgumentCaptor.forClass(InsurancePolicyEvent.class);
        verify(eventPublisher, times(2)).publish(eventCaptor.capture());

        List<InsurancePolicyEvent> publishedEvents = eventCaptor.getAllValues();
        
        InsurancePolicyEvent createdEvent = publishedEvents.get(0);
        assertEquals(EventType.ORDER_CREATED, createdEvent.eventType());
        assertEquals(policyId, createdEvent.policyId());
        assertEquals(customerId, createdEvent.customerId());

        InsurancePolicyEvent validatedEvent = publishedEvents.get(1);
        assertEquals(EventType.ORDER_VALIDATED, validatedEvent.eventType());
        assertEquals(policyId, validatedEvent.policyId());
        assertEquals(customerId, validatedEvent.customerId());
    }

    @Test
    @DisplayName("Should publish correct events for rejected validation")
    void shouldPublishCorrectEventsForRejectedValidation() throws Exception {
        setupSuccessfulCreation();
        setupValidationRejected();

        insurancePolicyInteractor.create(inputPolicy);

        ArgumentCaptor<InsurancePolicyEvent> eventCaptor = ArgumentCaptor.forClass(InsurancePolicyEvent.class);
        verify(eventPublisher, times(2)).publish(eventCaptor.capture());

        List<InsurancePolicyEvent> publishedEvents = eventCaptor.getAllValues();
        
        assertEquals(EventType.ORDER_CREATED, publishedEvents.get(0).eventType());
        assertEquals(EventType.ORDER_REJECTED, publishedEvents.get(1).eventType());
    }

    @Test
    @DisplayName("Should throw exception when insurance policy gateway fails")
    void shouldThrowExceptionWhenInsurancePolicyGatewayFails() {
        RuntimeException gatewayException = new RuntimeException("Gateway error");
        when(insurancePolicyGateway.createInsurancePolicy(inputPolicy)).thenThrow(gatewayException);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            insurancePolicyInteractor.create(inputPolicy);
        });

        assertEquals("Gateway error", thrownException.getMessage());
        verify(insurancePolicyGateway).createInsurancePolicy(inputPolicy);
        
        verifyNoInteractions(policyValidationGateway, validateInsurancePolicy, eventPublisher, historyEntryGateway);
    }

    @Test
    @DisplayName("Should throw exception when policy validation gateway fails")
    void shouldThrowExceptionWhenPolicyValidationGatewayFails() throws Exception {
        RuntimeException validationGatewayException = new RuntimeException("Validation gateway error");
        when(insurancePolicyGateway.createInsurancePolicy(inputPolicy)).thenReturn(mockCreatedPolicy);
        when(policyValidationGateway.validate(any(PolicyValidationRequest.class))).thenThrow(validationGatewayException);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            insurancePolicyInteractor.create(inputPolicy);
        });

        assertEquals("Validation gateway error", thrownException.getMessage());
        verify(insurancePolicyGateway).createInsurancePolicy(inputPolicy);
        verify(eventPublisher).publish(any(InsurancePolicyEvent.class)); // ORDER_CREATED event
        verify(policyValidationGateway).validate(any(PolicyValidationRequest.class));
        
        verifyNoInteractions(validateInsurancePolicy);
    }

    @Test
    @DisplayName("Should create history entries correctly")
    void shouldCreateHistoryEntriesCorrectly() throws Exception {
        setupSuccessfulCreation();
        setupValidationApproved();

        insurancePolicyInteractor.create(inputPolicy);

        ArgumentCaptor<HistoryEntry> historyCaptor = ArgumentCaptor.forClass(HistoryEntry.class);
        verify(historyEntryGateway, times(2)).create(historyCaptor.capture(), eq(mockCreatedPolicy));

        List<HistoryEntry> createdHistories = historyCaptor.getAllValues();
        assertEquals(2, createdHistories.size());

        createdHistories.forEach(history -> {
            assertNotNull(history.timestamp());
            assertNotNull(history.status());
        });
    }

    @Test
    @DisplayName("Should call validate method on created policy when validation passes")
    void shouldCallValidateMethodOnCreatedPolicyWhenValidationPasses() throws Exception {
        setupSuccessfulCreation();
        setupValidationApproved();

        insurancePolicyInteractor.create(inputPolicy);

        verify(mockCreatedPolicy).validate();
        verify(mockCreatedPolicy, never()).reject();
    }

    @Test
    @DisplayName("Should call reject method on created policy when validation fails")
    void shouldCallRejectMethodOnCreatedPolicyWhenValidationFails() throws Exception {
        setupSuccessfulCreation();
        setupValidationRejected();

        insurancePolicyInteractor.create(inputPolicy);

        verify(mockCreatedPolicy).reject();
        verify(mockCreatedPolicy, never()).validate();
    }
}