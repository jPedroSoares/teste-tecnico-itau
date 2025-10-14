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
import com.order.api.domain.usecases.FindInsurancePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    private PrintStream originalSystemOut;

    @BeforeEach
    void setUp() {
        processEventImp = new ProcessEventImp(objectMapper, findInsurancePolicy, historyEntryGateway);
        
        validEventJson = createValidEventJson();
        mockEvent = createMockEvent();
        mockPolicy = createMockInsurancePolicy();
        mockHistoryEntry = new HistoryEntry(LocalDateTime.now(), InsurancePolicyStatus.VALIDATED);
        
        originalSystemOut = System.out;
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

    private ByteArrayOutputStream captureSystemOut() {
        ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputCapture));
        return outputCapture;
    }

    private void restoreSystemOut() {
        System.setOut(originalSystemOut);
    }

    private String getSystemOutContent(ByteArrayOutputStream outputCapture) {
        return outputCapture.toString();
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
    @DisplayName("Should handle JSON parsing error gracefully")
    void shouldHandleJsonParsingErrorGracefully() throws Exception {
        String invalidJson = "{ invalid json }";
        JsonProcessingException jsonException = new JsonProcessingException("Invalid JSON") {};
        
        when(objectMapper.readValue(invalidJson, InsurancePolicyEvent.class)).thenThrow(jsonException);
        ByteArrayOutputStream outputCapture = captureSystemOut();

        assertDoesNotThrow(() -> processEventImp.processEvent(invalidJson));

        verify(objectMapper).readValue(invalidJson, InsurancePolicyEvent.class);
        verifyNoInteractions(findInsurancePolicy, historyEntryGateway);
        
        String output = getSystemOutContent(outputCapture);
        assertTrue(output.contains("Evento com erro:"));
        assertTrue(output.contains("Invalid JSON"));

        restoreSystemOut();
    }

    @Test
    @DisplayName("Should handle policy not found error gracefully")
    void shouldHandlePolicyNotFoundErrorGracefully() throws Exception {

        when(objectMapper.readValue(validEventJson, InsurancePolicyEvent.class)).thenReturn(mockEvent);
        when(findInsurancePolicy.find(mockEvent.policyId())).thenReturn(null);
        ByteArrayOutputStream outputCapture = captureSystemOut();

        assertDoesNotThrow(() -> processEventImp.processEvent(validEventJson));

        verify(objectMapper).readValue(validEventJson, InsurancePolicyEvent.class);
        verify(findInsurancePolicy).find(mockEvent.policyId());
        verifyNoInteractions(historyEntryGateway);
        
        String output = getSystemOutContent(outputCapture);
        assertTrue(output.contains("Insurance policy not found for ID:"));
        restoreSystemOut();
    }

    @Test
    @DisplayName("Should handle history entry creation error gracefully")
    void shouldHandleHistoryEntryCreationErrorGracefully() throws Exception {
        RuntimeException historyCreationException = new RuntimeException("History creation failed");

        when(objectMapper.readValue(validEventJson, InsurancePolicyEvent.class)).thenReturn(mockEvent);
        when(findInsurancePolicy.find(mockEvent.policyId())).thenReturn(mockPolicy);
        when(historyEntryGateway.create(any(HistoryEntry.class), eq(mockPolicy))).thenThrow(historyCreationException);
        ByteArrayOutputStream outputCapture = captureSystemOut();

        assertDoesNotThrow(() -> processEventImp.processEvent(validEventJson));

        verify(objectMapper).readValue(validEventJson, InsurancePolicyEvent.class);
        verify(findInsurancePolicy).find(mockEvent.policyId());
        verify(historyEntryGateway).create(any(HistoryEntry.class), eq(mockPolicy));
        
        String output = getSystemOutContent(outputCapture);
        assertTrue(output.contains("Evento com erro:"));
        assertTrue(output.contains("History creation failed"));

        restoreSystemOut();
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