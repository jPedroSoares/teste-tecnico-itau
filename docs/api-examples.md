# Exemplos de Payloads da API

Este documento contém exemplos de payloads para testar a API de apólices, baseados no contrato `CreateInsurancePolicyRequest`.

## Estrutura do Contrato

```java
public record CreateInsurancePolicyRequest(
    @NotNull UUID customerId,
    @NotNull UUID productId,
    @NotNull PolicyCategory category,
    @NotNull SalesChannel salesChannel,
    @NotNull PaymentMethod paymentMethod,
    @NotNull @DecimalMin("0.01") BigDecimal totalMonthlyPremiumAmount,
    @NotNull @DecimalMin("0.01") BigDecimal insuredAmount,
    @NotEmpty Map<String, BigDecimal> coverages,
    @NotEmpty List<String> assistances
)
```

## Enums Disponíveis

### PolicyCategory

- `LIFE`
- `AUTO`
- `PROPERTY`
- `BUSINESS`

### SalesChannel

- `MOBILE`
- `WHATSAPP`
- `WEB_SITE`

### PaymentMethod

- `CREDIT_CARD`
- `ACCOUNT_DEBIT`
- `BOLETO`
- `PIX`

## Cenários para Teste de Validação

### Valor Muito Alto (deve ser rejeitado)

```json
{
  "customerId": "111e2222-e89b-12d3-a456-426614174888",
  "productId": "333e4444-e89b-12d3-a456-426614174999",
  "category": "LIFE",
  "salesChannel": "WEB_SITE",
  "paymentMethod": "PIX",
  "totalMonthlyPremiumAmount": 2500.00,
  "insuredAmount": 2000000.00,
  "coverages": {
    "MORTE_NATURAL": 2000000.00
  },
  "assistances": [
    "FUNERAL"
  ]
}
```

### Valor Baixo (deve ser aprovado)

```json
{
  "customerId": "555e6666-e89b-12d3-a456-426614174000",
  "productId": "777e8888-e89b-12d3-a456-426614174111",
  "category": "PROPERTY",
  "salesChannel": "WHATSAPP",
  "paymentMethod": "PIX",
  "totalMonthlyPremiumAmount": 35.90,
  "insuredAmount": 50000.00,
  "coverages": {
    "INCENDIO": 50000.00
  },
  "assistances": [
    "CHAVEIRO"
  ]
}
```
