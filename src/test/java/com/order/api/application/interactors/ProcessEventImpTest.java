package com.order.api.application.interactors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.api.application.dto.InsurancePolicyEvent;
import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.EventType;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import com.order.api.domain.exceptions.PolicyNotFoundException;
import com.order.api.domain.usecases.FindInsurancePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessEventImpTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private FindInsurancePolicy findInsurancePolicy;

    @Mock
    private HistoryEntryGateway historyEntryGateway;

    private ProcessEventImp processEventImp;
    
    private String validEventJson;
    private InsurancePolicyEvent mockEvent;
    private InsurancePolicy mockPolicy;
    private HistoryEntry mockHistoryEntry;

    @BeforeEach
    void setUp() {
        processEventImp = new ProcessEventImp(objectMapper, findInsurancePolicy, historyEntryGateway);
        
        validEventJson = createValidEventJson();
        mockEvent = createMockEvent();
        mockPolicy = createMockInsurancePolicy();
        mockHistoryEntry = new HistoryEntry(LocalDateTime.now(), InsurancePolicyStatus.VALIDATED);
    }

    private InsurancePolicyEvent createMockEvent() {
        return new InsurancePolicyEvent(
                UUID.randomUUID(),
                EventType.ORDER_VALIDATED,
                UUID.randomUUID(),
                UUID.randomUUID(),
                InsurancePolicyStatus.VALIDATED,
                LocalDateTime.now().toString()
        );
    }

    private InsurancePolicy createMockInsurancePolicy() {
        return new InsurancePolicy(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                PolicyCategory.AUTO,
                Map.of("coverage1", BigDecimal.valueOf(1000)),
                List.of("assistance1"),
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(50000.0),
                PaymentMethod.PIX,
                SalesChannel.MOBILE
        );
    }

    private String createValidEventJson() {
        return """
                {
                    "eventId": "123e4567-e89b-12d3-a456-426614174000",
                    "eventType": "ORDER_VALIDATED",
                    "policyId": "123e4567-e89b-12d3-a456-426614174001",
                    "customerId": "123e4567-e89b-12d3-a456-426614174002",
                    "status": "VALIDATED",
                    "timestamp": "2023-10-13T10:15:30"
                }
                """;
    }



    private void setupSuccessfulMocks(String eventJson, InsurancePolicyEvent event, InsurancePolicy policy, HistoryEntry historyEntry) throws Exception {
        when(objectMapper.readValue(eventJson, InsurancePolicyEvent.class)).thenReturn(event);
        when(findInsurancePolicy.find(event.policyId())).thenReturn(policy);
        when(historyEntryGateway.create(any(HistoryEntry.class), eq(policy))).thenReturn(historyEntry);
    }

    @Test
    @DisplayName("Should process event successfully when all operations succeed")
    void shouldProcessEventSuccessfullyWhenAllOperationsSucceed() throws Exception {
        setupSuccessfulMocks(validEventJson, mockEvent, mockPolicy, mockHistoryEntry);

        processEventImp.processEvent(validEventJson);

        verify(objectMapper).readValue(validEventJson, InsurancePolicyEvent.class);
        verify(findInsurancePolicy).find(mockEvent.policyId());
        
        ArgumentCaptor<HistoryEntry> historyCaptor = ArgumentCaptor.forClass(HistoryEntry.class);
        verify(historyEntryGateway).create(historyCaptor.capture(), eq(mockPolicy));
        
        HistoryEntry capturedHistory = historyCaptor.getValue();
        assertEquals(mockEvent.status(), capturedHistory.status());
        assertNotNull(capturedHistory.timestamp());
    }

    @Test
    @DisplayName("Should handle JSON parsing error and throw RuntimeException")
    void shouldHandleJsonParsingErrorAndThrowRuntimeException() throws Exception {
        String invalidJson = "{ invalid json }";
        JsonProcessingException jsonException = new JsonProcessingException("Invalid JSON") {};
        
        when(objectMapper.readValue(invalidJson, InsurancePolicyEvent.class)).thenThrow(jsonException);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            processEventImp.processEvent(invalidJson);
        });

        assertEquals("Error processing event: Invalid JSON", thrownException.getMessage());
        assertEquals(jsonException, thrownException.getCause());
        
        verify(objectMapper).readValue(invalidJson, InsurancePolicyEvent.class);
        verifyNoInteractions(findInsurancePolicy, historyEntryGateway);
    }

    @Test
    @DisplayName("Should handle policy not found and throw RuntimeException")
    void shouldHandlePolicyNotFoundAndThrowRuntimeException() throws Exception {
        PolicyNotFoundException policyNotFoundException = new PolicyNotFoundException(mockEvent.policyId());

        when(objectMapper.readValue(validEventJson, InsurancePolicyEvent.class)).thenReturn(mockEvent);
        when(findInsurancePolicy.find(mockEvent.policyId())).thenThrow(policyNotFoundException);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            processEventImp.processEvent(validEventJson);
        });

        assertTrue(thrownException.getMessage().startsWith("Error processing event:"));
        assertEquals(policyNotFoundException, thrownException.getCause());
        
        verify(objectMapper).readValue(validEventJson, InsurancePolicyEvent.class);
        verify(findInsurancePolicy).find(mockEvent.policyId());
        verifyNoInteractions(historyEntryGateway);
    }

    @Test
    @DisplayName("Should handle history entry creation error and throw RuntimeException")
    void shouldHandleHistoryEntryCreationErrorAndThrowRuntimeException() throws Exception {
        RuntimeException historyCreationException = new RuntimeException("History creation failed");

        when(objectMapper.readValue(validEventJson, InsurancePolicyEvent.class)).thenReturn(mockEvent);
        when(findInsurancePolicy.find(mockEvent.policyId())).thenReturn(mockPolicy);
        when(historyEntryGateway.create(any(HistoryEntry.class), eq(mockPolicy))).thenThrow(historyCreationException);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            processEventImp.processEvent(validEventJson);
        });

        assertEquals("Error processing event: History creation failed", thrownException.getMessage());
        assertEquals(historyCreationException, thrownException.getCause());

        verify(objectMapper).readValue(validEventJson, InsurancePolicyEvent.class);
        verify(findInsurancePolicy).find(mockEvent.policyId());
        verify(historyEntryGateway).create(any(HistoryEntry.class), eq(mockPolicy));
    }

    @Test
    @DisplayName("Should verify correct method calls sequence")
    void shouldVerifyCorrectMethodCallsSequence() throws Exception {
        setupSuccessfulMocks(validEventJson, mockEvent, mockPolicy, mockHistoryEntry);

        processEventImp.processEvent(validEventJson);

        var inOrder = inOrder(objectMapper, findInsurancePolicy, historyEntryGateway);
        inOrder.verify(objectMapper).readValue(validEventJson, InsurancePolicyEvent.class);
        inOrder.verify(findInsurancePolicy).find(mockEvent.policyId());
        inOrder.verify(historyEntryGateway).create(any(HistoryEntry.class), eq(mockPolicy));
    }
}