package com.order.api.application.interactors;

import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.*;
import com.order.api.domain.exceptions.InvalidPolicyStateException;
import com.order.api.domain.exceptions.PolicyNotFoundException;
import com.order.api.domain.ports.EventPublisher;
import com.order.api.domain.states.InsurancePolicyState;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelInsurancePolicyInteractorTest {

    @Mock
    private InsurancePolicyGateway insurancePolicyGateway;
    
    @Mock
    private EventPublisher eventPublisher;
    
    @Mock
    private HistoryEntryGateway historyEntryGateway;
    
    @Mock
    private InsurancePolicyState mockState;

    private CancelInsurancePolicyInteractor interactor;
    private UUID policyId;
    private UUID customerId;
    private InsurancePolicy insurancePolicy;

    @BeforeEach
    void setUp() {
        interactor = new CancelInsurancePolicyInteractor(
                insurancePolicyGateway, 
                eventPublisher, 
                historyEntryGateway
        );
        
        policyId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        insurancePolicy = createValidInsurancePolicy(policyId, customerId, productId);
        insurancePolicy.setStatus(mockState);
    }

    @Test
    @DisplayName("Should cancel insurance policy successfully")
    void shouldCancelInsurancePolicySuccessfully() {
        when(insurancePolicyGateway.findById(policyId)).thenReturn(insurancePolicy);

        InsurancePolicy result = interactor.cancelPolicy(policyId);

        assertNotNull(result);
        assertEquals(policyId, result.getId());
        assertEquals(customerId, result.getCustomerId());
        
        verify(insurancePolicyGateway).findById(policyId);
        verify(mockState).cancel(insurancePolicy);
        verify(eventPublisher).publish(any(InsurancePolicyEvent.class));
        verify(historyEntryGateway).create(any(HistoryEntry.class), eq(insurancePolicy));
    }

    @Test
    @DisplayName("Should publish correct event when canceling policy")
    void shouldPublishCorrectEventWhenCancelingPolicy() {
        when(insurancePolicyGateway.findById(policyId)).thenReturn(insurancePolicy);
        when(mockState.getStatusName()).thenReturn(InsurancePolicyStatus.CANCELED);
        ArgumentCaptor<InsurancePolicyEvent> eventCaptor = ArgumentCaptor.forClass(InsurancePolicyEvent.class);

        interactor.cancelPolicy(policyId);

        verify(eventPublisher).publish(eventCaptor.capture());
        
        InsurancePolicyEvent publishedEvent = eventCaptor.getValue();
        assertNotNull(publishedEvent);
        assertNotNull(publishedEvent.eventId());
        assertEquals(EventType.ORDER_CANCELED, publishedEvent.eventType());
        assertEquals(policyId, publishedEvent.policyId());
        assertEquals(customerId, publishedEvent.customerId());
        assertEquals(InsurancePolicyStatus.CANCELED, publishedEvent.status());
        assertNotNull(publishedEvent.timestamp());
    }

    @Test
    @DisplayName("Should create history entry when canceling policy")
    void shouldCreateHistoryEntryWhenCancelingPolicy() {
        when(insurancePolicyGateway.findById(policyId)).thenReturn(insurancePolicy);
        when(mockState.getStatusName()).thenReturn(InsurancePolicyStatus.CANCELED);
        ArgumentCaptor<HistoryEntry> historyCaptor = ArgumentCaptor.forClass(HistoryEntry.class);

        interactor.cancelPolicy(policyId);

        verify(historyEntryGateway).create(historyCaptor.capture(), eq(insurancePolicy));
        HistoryEntry createdHistoryEntry = historyCaptor.getValue();
        assertNotNull(createdHistoryEntry);
        assertNotNull(createdHistoryEntry.timestamp());
        assertEquals(InsurancePolicyStatus.CANCELED, createdHistoryEntry.status());
    }

    @Test
    @DisplayName("Should return null when policy not found")
    void shouldThrowExceptionWhenPolicyNotFound() {
        UUID notFoundPolicyId = UUID.randomUUID();
        when(insurancePolicyGateway.findById(notFoundPolicyId)).thenReturn(null);

        PolicyNotFoundException exception = assertThrows(PolicyNotFoundException.class, () -> {
            interactor.cancelPolicy(notFoundPolicyId);
        });
        assertEquals("Insurance policy not found with ID: " + notFoundPolicyId, exception.getMessage());
        verify(insurancePolicyGateway).findById(notFoundPolicyId);
        verifyNoInteractions(eventPublisher);
        verifyNoInteractions(historyEntryGateway);
    }

    @Test
    @DisplayName("Should handle exception during event publishing")
    void shouldHandleExceptionDuringEventPublishing() {
        when(insurancePolicyGateway.findById(policyId)).thenReturn(insurancePolicy);
        doThrow(new RuntimeException("Event publishing failed")).when(eventPublisher).publish(any());

        assertThrows(RuntimeException.class, () -> {
            interactor.cancelPolicy(policyId);
        });
        
        verify(insurancePolicyGateway).findById(policyId);
        verify(mockState).cancel(insurancePolicy);
        verify(eventPublisher).publish(any(InsurancePolicyEvent.class));
        verifyNoInteractions(historyEntryGateway);
    }

    @Test
    @DisplayName("Should handle exception during history entry creation")
    void shouldHandleExceptionDuringHistoryEntryCreation() {
        when(insurancePolicyGateway.findById(policyId)).thenReturn(insurancePolicy);
        doThrow(new RuntimeException("History creation failed")).when(historyEntryGateway)
                .create(any(HistoryEntry.class), any(InsurancePolicy.class));

        assertThrows(RuntimeException.class, () -> {
            interactor.cancelPolicy(policyId);
        });
        
        verify(insurancePolicyGateway).findById(policyId);
        verify(mockState).cancel(insurancePolicy);
        verify(eventPublisher).publish(any(InsurancePolicyEvent.class));
        verify(historyEntryGateway).create(any(HistoryEntry.class), eq(insurancePolicy));
    }

    @Test
    @DisplayName("Should verify all interactions occur in correct order")
    void shouldVerifyAllInteractionsOccurInCorrectOrder() {
        when(insurancePolicyGateway.findById(policyId)).thenReturn(insurancePolicy);
        interactor.cancelPolicy(policyId);

        var inOrder = inOrder(insurancePolicyGateway, mockState, eventPublisher, historyEntryGateway);
        inOrder.verify(insurancePolicyGateway).findById(policyId);
        inOrder.verify(mockState).cancel(insurancePolicy);
        inOrder.verify(eventPublisher).publish(any(InsurancePolicyEvent.class));
        inOrder.verify(historyEntryGateway).create(any(HistoryEntry.class), eq(insurancePolicy));
    }

    private InsurancePolicy createValidInsurancePolicy(UUID id, UUID customerId, UUID productId) {
        return new InsurancePolicy(
                id,
                customerId,
                productId,
                PolicyCategory.LIFE,
                Map.of("DEATH", new BigDecimal("100000")),
                List.of("24h Medical Assistance"),
                new BigDecimal("150.00"),
                null,
                null,
                new BigDecimal("100000"),
                PaymentMethod.CREDIT_CARD,
                SalesChannel.WEB_SITE
        );
    }
}