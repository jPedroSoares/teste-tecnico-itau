<img width="100%" alt="Image" src="./docs/assets/banner-itau.jpg" />

# Desafio Software Engineer Itaú - Microsserviço de Apólices de Seguro

> Este projeto é um microsserviço desenvolvido como parte de um desafio técnico para uma posição de **Software Engineer** no Itaú.  O objetivo é gerenciar o ciclo de vida de solicitações de apólices de seguro, implementando uma arquitetura orientada a eventos (EDA).

[![JAVA](https://img.shields.io/badge/Java-v17-orange)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![MAVEN](https://img.shields.io/badge/Maven-3.9.5-orange)](https://maven.apache.org/download.cgi)
[![Docker](https://img.shields.io/badge/Docker-24.0^-blue)](https://docs.docker.com/desktop/setup/install/windows-install)

<h3 align="center">
	🚧  Projeto em construção...  🚧
</h3>

## 📜 Tabela de Conteúdos

* [Funcionalidades Principais](#funcionalidades-principais)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Como Executar a Solução](#como-executar-a-solucao)
* [Testando a Aplicação](#testando-a-aplicacao)
* [Decisões e Arquitetura da Solução](#decisoes-e-arquitetura-da-solucao)
* [Endpoints da API](#endpoints-da-api)
* [Premissas e Decisões de Design](#premissas-e-decisoes-de-design)
* [Estratégia de Testes](#estrategia-de-testes)
* [Melhorias Futuras](#melhorias-futuras)
* [Autor](#autor)

<h2 id="funcionalidades-principais">✨ Funcionalidades Principais </h2>

* **Recepção de Solicitações:** API REST para receber e persistir novas solicitações de apólice.
* **Gerenciamento do Ciclo de Vida:** Controle de estados da solicitação (`Recebido`, `Validado`, `Pendente`, `Aprovado`, `Rejeitado`, `Cancelado`).
* **Integração com API de Fraudes:** Consumo de uma API (mockada) para análise e classificação de risco do cliente.
* **Aplicação de Regras de Negócio:** Validações baseadas no tipo de cliente (`Regular`, `Alto Risco`, `Preferencial`, `Sem Informação`) e no capital segurado.
* **Publicação de Eventos:** Publicação dos resultados de cada alteração de estado para notificar outros sistemas.
* **Consulta de Solicitações:** API REST para consultar solicitações por ID da solicitação ou ID do cliente.
* **Cancelamento de Solicitação:** API REST para cancelar uma apólice, exceto quando já aprovada.

<h2 id="tecnologias-utilizadas">💻 Tecnologias Utilizadas</h2>

| Tecnologia | Finalidade |
| :--- | :--- |
| **Java 17** | Linguagem de programação principal |
| **Spring Boot** | Framework para construção da aplicação e da API REST |
| **Maven** | Gerenciador de dependências e build do projeto |
| **Docker & Docker Compose**| Orquestração da infraestrutura e execução da aplicação |
| **Kafka** | Broker de Mensageria |
| **PostgreSQL** | Banco de Dados |
| **Mockserver** | Servidor de Mock  |
| **JUnit 5 & Mockito** | Testes de unidade e integração |

<h2 id="como-executar-a-solucao">▶️ Como Executar a Solução </h2>

### Pré-requisitos
- Docker e Docker Compose instalados
- Portas 8080, 8082, 1080 e 5432 livres na máquina

### Rodando o projeto

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/jPedroSoares/teste-tecnico-itau.git
    cd teste-tecnico-itau
    ```

2.  **Suba os contêineres:**
    ```bash
    docker-compose up -d --build
    ```

3.  **Aguarde uns 30-60 segundos** para tudo subir corretamente.

4.  **Teste se funcionou:**
    ```bash
    curl http://localhost:8080/health
    ```
    Se retornar `{"status":"UP"}`, está tudo certo!

### Se algo der errado:
- **Porta ocupada?** Mate o processo com `lsof -ti:8080 | xargs kill -9`
- **Containers não sobem?** Rode `docker-compose down -v` e tente novamente
- **Banco não conecta?** Aguarde mais um pouco, o PostgreSQL demora para inicializar

Após tudo funcionar, o ambiente estará disponível:

* **API do Microsserviço:** `http://localhost:8080`
* **Servidor de Mocks:** - `http://localhost:1080`
* **UI do Servidor de Mocks:** - `http://localhost:1080/mockserver/dashboard`
* **UI do Broker:** - `http://localhost:8082`

## � Testando a Aplicação

### Fluxo completo de teste

#### 1. Criar uma nova apólice

```bash
curl -X POST http://localhost:8080/api/insurance-policies \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "123e4567-e89b-12d3-a456-426614174000",
    "productId": "456e7890-e89b-12d3-a456-426614174111",
    "category": "VIDA",
    "salesChannel": "ONLINE",
    "paymentMethod": "PIX",
    "totalMonthlyPremiumAmount": 89.90,
    "insuredAmount": 150000.00,
    "coverages": {
      "MORTE_NATURAL": 150000.00,
      "INVALIDEZ_PERMANENTE": 75000.00
    },
    "assistances": ["FUNERAL", "CESTA_BASICA"]
  }'
```

**Resposta esperada:** A apólice será criada com status `RECEIVED` e depois automaticamente mudará para `VALIDATED` após a consulta à API de fraudes.

#### 2. Para aprovar ou rejeitar a apólice

O sistema aguarda um evento no tópico `payment-topic` do Kafka. Você pode simular isso de duas formas:

**Opção A - Usando Kafka CLI:**
```bash
# Aprovar a apólice
docker exec -it kafka kafka-console-producer --topic payment-topic --bootstrap-server localhost:9092
# Cole este JSON (substitua os IDs pelos reais):
{
  "eventId": "293e805b-1a96-4170-95ae-94b6e1161346",
  "eventType": "ORDER_APPROVED",
  "policyId": "SEU_POLICY_ID_AQUI",
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "status": "APPROVED",
  "timestamp": "2025-10-14T15:28:57.307129"
}

# Ou rejeitar a apólice
{
  "eventId": "293e805b-1a96-4170-95ae-94b6e1161346",
  "eventType": "ORDER_REJECTED",
  "policyId": "SEU_POLICY_ID_AQUI",
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "status": "REJECTED",
  "timestamp": "2025-10-14T15:28:57.307129"
}
```

**Opção B - Usando UI do Kafka (mais fácil):**
1. Acesse `http://localhost:8082`
2. Vá em "Topics" → "payment-topic"
3. Clique em "Produce Message"
4. Cole um dos JSONs acima

#### 3. Verificar o resultado

```bash
# Consultar a apólice para ver o novo status
curl http://localhost:8080/api/insurance-policies/{id}
```

O status deve estar `APPROVED` ou `REJECTED` conforme o evento enviado.

### Teste rápido (só criação)

Se quiser apenas testar a criação sem o fluxo completo:

```bash
# Criar apólice
curl -X POST http://localhost:8080/api/insurance-policies \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "123e4567-e89b-12d3-a456-426614174000",
    "productId": "456e7890-e89b-12d3-a456-426614174111",
    "category": "VIDA",
    "salesChannel": "ONLINE",
    "paymentMethod": "PIX",
    "totalMonthlyPremiumAmount": 89.90,
    "insuredAmount": 150000.00,
    "coverages": {
      "MORTE_NATURAL": 150000.00
    },
    "assistances": ["FUNERAL"]
  }'

# Consultar a apólice criada
curl http://localhost:8080/api/insurance-policies/{id}

# Cancelar se necessário (só funciona se não estiver aprovada)
curl -X PATCH http://localhost:8080/api/insurance-policies/{id}/cancel
```

### Usando Swagger ou Postman

* **Swagger:** `http://localhost:8080/swagger-ui/index.html` - interface gráfica para testar a API
* **Collection do Postman:** Importe o arquivo `docs/insurance_policy_anagement_api_postman_collection.json` no seu Postman

### Documentação Adicional

Para exemplos mais detalhados e cenários específicos, consulte:

* **[Exemplos de Payloads](docs/api-examples.md)** - Payloads prontos para diferentes tipos de seguro
* **[Exemplos Anti-Fraud](docs/anti-fraud-examples.md)** - Regras de validação e cenários de teste baseados na classificação de risco

## �🤔 Decisões e Arquitetura da Solução

### Por que Kafka ao invés de RabbitMQ?

Escolhi Kafka principalmente por três razões: 

1. **Experiência prévia** - já havia trabalhado com ele antes e sabia que conseguiria implementar rapidamente
2. **Volume e performance** - para um sistema de apólices que pode ter muito volume, o Kafka aguenta melhor a pancada com sua arquitetura de partições  
3. **Retentativas nativas** - a documentação mencionava a necessidade de retentativas, e o Kafka tem isso nativamente com configuração simples

### Clean Architecture

Era um dos pré-requisitos do desafio e eu já tinha experiência com ela, então foi natural usar. Os principais benefícios que obtive:

* **Testabilidade:** Consegui escrever testes unitários isolados
* **Flexibilidade:** Posso trocar banco ou broker sem afetar as regras de negócio  
* **Organização:** Separação clara entre Domain, Application e Infrastructure

Considerei usar Arquitetura Hexagonal também, mas Clean se ajustou melhor ao que estava sendo avaliado.

### State Pattern para o ciclo de vida

Inicialmente pensei em usar um enum com switch/case, mas optei pelo State Pattern porque:

* Cada estado tem suas próprias regras de transição
* É mais fácil adicionar novos estados no futuro
* Evita aqueles switches gigantes que são um pesadelo para manter

### Strategy Pattern para tipos de cliente

Cada tipo de cliente (Regular, Alto Risco, etc.) tem regras diferentes de validação. O Strategy permite adicionar novos tipos sem mexer no código existente - princípio Open/Closed na prática.

### Tratamento de Erros Robusto

Implementei um sistema de exceções customizadas com `@ControllerAdvice` porque:

* **Produção-ready:** APIs precisam de tratamento consistente de erros
* **Códigos HTTP corretos:** 404 para not found, 409 para conflitos de estado, etc.
* **Segurança:** Não vaza stack traces ou detalhes internos para clientes
* **Debugging:** Logs estruturados internamente mantêm rastreabilidade

Essa foi uma decisão consciente de priorizar robustez sobre conveniências como Swagger, que conflitava com o tratamento customizado.

### PostgreSQL + Docker

Poderia ter usado H2 em memória, mas quis simular um ambiente mais realista. O Docker facilita a vida de quem for rodar o projeto - não precisa instalar nada na máquina.

<h2 id="diagramas-e-arquitetura-visual">🎨 Diagramas e Arquitetura Visual</h2>

Diagrama da Arquitetura do Microsserviço

<img width="50%" alt="Diagrama da Arquitetura do Microsserviço" src="./docs/assets/ecossistema.png" />

Diagrama de Entidade-Relacionamento (ER)

<img width="50%" alt="Diagrama de Entidade-Relacionamento (ER)" src="./docs/assets/der.png" />

<h2 id="endpoints-da-api">📡 Endpoints da API</h2>
Abaixo estão os principais endpoints expostos pela API REST:

| Verbo | Rota | Descrição |
| :--- | :--- | :--- |
| **POST** | /api/insurance-policies | Cria uma nova solicitação de apólice. |
| **GET** | /api/insurance-policies/{id} | Consulta uma solicitação por ID da apólice. |
| **GET** | /api/insurance-policies/customer/{customerId} | Consulta solicitações por ID do cliente. |
| **PATCH** | /api/insurance-policies/{id}/cancel | Cancela uma solicitação de apólice, se aplicável. |
| **GET** | /health | Verifica o status de saúde da aplicação. |

## 💭 Premissas e Decisões de Design

Durante o desenvolvimento, precisei fazer algumas suposições onde o enunciado não era 100% claro:

### Volume de transações
Assumi um volume médio de transações (centenas por minuto, não milhares por segundo). Por isso escolhi PostgreSQL ao invés de uma solução NoSQL mais complexa.

### API de Fraude
Como era uma API mockada, assumi que ela sempre responde em até 2 segundos. Na vida real, implementaria timeout e retry com backoff exponencial.

### Consistência de dados
Optei por consistência eventual - quando publico um evento no Kafka, pode haver um pequeno delay até outros sistemas processarem. Para apólices de seguro, isso me pareceu aceitável.

### Estados da apólice

Interpretei que uma apólice aprovada não pode mais ser cancelada (seria estranho cancelar algo já aprovado). Se estiver errado, é só ajustar no código.

Também resolvi um conflito na documentação: estava descrito que só poderia ir de "Recebido" para "Validado", mas depois dizia que em "Validado" poderia ir para "Rejeitado". Tomei a liberdade de permitir transição direta de "Recebido" para "Rejeitado" também - faz mais sentido para casos onde a validação falha logo de cara.

### Segurança

Não implementei autenticação/autorização por simplicidade, mas numa aplicação real seria obrigatório JWT + OAuth2.

## 🧪 Estratégia de Testes

### Comandos para Executar Testes

```bash
# Todos os testes
./mvnw test

# Só testes unitários (rápido)
./mvnw test -Dtest="*Test"

# Testes específicos
./mvnw test -Dtest="InsurancePolicyInteractorTest"
```

### Decisões sobre Testes

#### Testes Unitários - Prioridade Alta
Cobri todas as regras de negócio principais porque são o coração do sistema:

* **Estados da apólice:** Cada transição testada isoladamente
* **Strategies de validação:** Todos os tipos de cliente e limites
* **Interactors:** Orquestração dos casos de uso
* **Processamento de eventos:** Kafka consumer logic

**Por que focei aqui?** As regras de negócio são complexas e críticas - um bug na validação pode aprovar/rejeitar incorretamente uma apólice.

#### Testes de Integração - Implementação Parcial
Implementei alguns testes de integração, mas focaria mais em:

* **Testes end-to-end:** Da API até o banco e Kafka
* **Contract testing:** Garantir compatibilidade com API de fraude
* **Performance:** Validar tempo de resposta sob carga

**Trade-off consciente:** Priorizei qualidade dos unitários vs quantidade de integração, considerando tempo do desafio.

### Por que Removemos o Swagger?

Como o tratamento robusto de erros é **crítico para produção** e o Swagger é apenas uma conveniência de desenvolvimento, optei por manter a qualidade da API em detrimento da documentação automática.

**Alternativas:** A documentação completa está disponível via:

* Collection do Postman (arquivo `docs/`)
* Exemplos práticos neste README
* Documentação detalhada nos arquivos `docs/api-examples.md` e `docs/anti-fraud-examples.md`

## �🚀 Melhorias Futuras

Algumas funcionalidades que implementaria nas próximas iterações:

* **Health checks** mais robustos (verificar conectividade com Kafka, banco, API externa)
* **Rate limiting** para evitar spam de requests
* **Cache Redis** para consultas frequentes
* **Paginação** nas APIs de listagem


<h2 id="autor">👨‍💻 Autor </h2>

<a href="https://github.com/jPedroSoares">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/39804819?v=4" width="100px;" alt=""/>
 <br />
 <sub><b>João Soares</b></sub></a>

👋🏽 Entre em contato!

[![Linkedin Badge](https://img.shields.io/badge/-João%20Pedro-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/jpedrosoares1/)](https://www.linkedin.com/in/jpedrosoares1/)
[![Gmail Badge](https://img.shields.io/badge/-joao.soares204@hotmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:joao.soares204@hotmail.com)](mailto:joao.soares204@hotmail.com)
