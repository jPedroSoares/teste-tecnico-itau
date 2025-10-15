# Exemplos da API Anti-Fraud

Este documento contém exemplos de responses da API Anti-Fraud para diferentes cenários, baseados nas regras de negócio implementadas.

## Resposta Padrão da API

A API Anti-Fraud retorna sempre a mesma estrutura mockada:

```json
{
  "orderId": "e053467f-bd48-4fd2-9481-75bd4e88ee40",
  "customerId": "7c2a27ba-71ef-4dd8-a3cf-5e094316ffd8",
  "analyzedAt": "2024-05-10T12:00:00Z",
  "classification": "HIGH_RISK",
  "occurrences": [
    {
      "id": "e053467f-bd48-4fd2-9481-75bd4e88ee40",
      "productId": 78900069,
      "type": "FRAUD",
      "description": "Attempted Fraudulent transaction",
      "createdAt": "2024-05-10T12:00:00Z",
      "updatedAt": "2024-05-10T12:00:00Z"
    },
    {
      "id": "f053467f-bd48-4fd2-9481-75bd4e88ee41",
      "productId": 104445569,
      "type": "SUSPICION",
      "description": "Unusual activity flagged for review",
      "createdAt": "2024-04-09T14:35:30Z",
      "updatedAt": "2024-04-09T14:35:30Z"
    }
  ]
}
```

## Como o Sistema Interpreta a Resposta

O sistema mapeia a `classification` para tipos de cliente:

| Classification | Tipo de Cliente |
|---|---|
| `REGULAR` | Cliente Regular |
| `HIGH_RISK` | Cliente Alto Risco |
| `PREFERENTIAL` | Cliente Preferencial |
| `NO_INFO` | Cliente Sem Informação |

## Regras de Validação por Tipo de Cliente

### 🟢 Cliente Regular

**Limites para aprovação automática:**

- **Seguro Vida/Residencial:** até R$ 500.000,00
- **Seguro Auto:** até R$ 350.000,00  
- **Outros seguros:** até R$ 255.000,00

### 🔴 Cliente Alto Risco

**Limites para aprovação automática:**

- **Seguro Auto:** até R$ 250.000,00
- **Seguro Residencial:** até R$ 150.000,00
- **Outros seguros:** até R$ 125.000,00

### 🟡 Cliente Preferencial

**Limites para aprovação automática:**

- **Seguro Vida:** até R$ 800.000,00
- **Seguro Auto/Residencial:** até R$ 450.000,00
- **Outros seguros:** até R$ 375.000,00

### ⚪ Cliente Sem Informação

**Limites para aprovação automática:**

- **Seguro Vida/Residencial:** até R$ 200.000,00
- **Seguro Auto:** até R$ 75.000,00
- **Outros seguros:** até R$ 55.000,00

## Notas Importantes

1. **Mock Atual:** A API de fraudes sempre retorna `HIGH_RISK`. Para testar outros cenários, seria necessário modificar o mock ou implementar lógica condicional.
