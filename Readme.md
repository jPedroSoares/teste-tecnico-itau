<img width="100%" alt="Image" src="./dev-env/assets/banner-itau.jpg" />

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
* [Diagramas e Arquitetura Visual](#diagramas-e-arquitetura-visual)
* [Endpoints da API](#endpoints-da-api)
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

A maneira mais simples e recomendada de executar a solução é utilizando o Docker Compose, que irá provisionar toda a infraestrutura necessária (aplicação, banco de dados, broker e mock server).

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/jPedroSoares/teste-tecnico-itau.git
    cd teste-tecnico-itau
    ```

2.  **Suba os contêineres:**

    ```bash
    docker-compose up -d --build
    ```

Aguarde alguns instantes para que todos os serviços sejam iniciados. Após a conclusão, o ambiente estará disponível nos seguintes endereços:

* **API do Microsserviço:** `http://localhost:8080`
* **Servidor de Mocks:** - `http://localhost:1080`
* **UI do Servidor de Mocks:** - `http://localhost:1080/mockserver/dashboard`
* **Swagger:** - `http://localhost:8080/swagger-ui/index.html`
* **UI do Broker:** - `http://localhost:8082`

<h2 id="decisoes-e-arquitetura-da-solucao">🤔 Decisões e Arquitetura da Solução</h2>

### 1. Arquitetura Orientada a Eventos (EDA)

Optei pelo Kafka como broker de mensageria devido a experiência prévia com a ferramenta e sua capacidade de lidar com alta concorrência e escalabilidade.

### 2. Clean Architecture e SOLID

Para atender aos critérios de avaliação, a estrutura do projeto segue os princípios da Clean Architecture, separando o código em camadas bem definidas:

* **Domain:** Contém as entidades de negócio e a lógica de domínio pura, sem dependências de frameworks.

* **Application:** Orquestra os fluxos de negócio (use cases) e serve como mediador entre a camada de domínio e a de infraestrutura.

* **Infrastructure:** Detalhes de implementação, como controllers da API, configuração do banco de dados, clientes de mensageria e integrações externas.

Essa separação garante testabilidade, extensibilidade e a troca de componentes de infraestrutura (como o banco de dados) com impacto mínimo no núcleo do negócio.

### 3. Padrões de Projeto (Design Patterns)

* **State Pattern:** Para gerenciar o ciclo de vida da solicitação, o padrão State foi aplicado. Cada estado (Recebido, Validado, etc.) é representado por uma classe específica que encapsula as regras de transição e o comportamento permitido. Isso evita o uso de condicionais (if/else, switch) complexas no código de serviço, tornando-o mais limpo e aderente ao princípio Open/Closed do SOLID.

* **Strategy Pattern:** As regras de validação para diferentes tipos de clientes (Regular, Alto Risco, etc.) foram implementadas com o padrão Strategy. Cada tipo de cliente possui sua própria "estratégia" de validação, permitindo adicionar novas regras ou tipos de cliente no futuro sem alterar o código existente.

* **Factory Pattern:** Para a criação de instâncias de estados e estratégias, o padrão Factory foi utilizado. Isso centraliza a lógica de criação e facilita a manutenção do código.

<h2 id="diagramas-e-arquitetura-visual">🎨 Diagramas e Arquitetura Visual</h2>

Diagrama da Arquitetura do Microsserviço

Diagrama de Sequência do Ciclo de Vida da Apólice

Diagrama de Entidade-Relacionamento (ER)

<h2 id="endpoints-da-api">📡 Endpoints da API</h2>
Abaixo estão os principais endpoints expostos pela API REST:

| Verbo | Rota | Descrição |
| :--- | :--- | :--- |
| **POST** | /api/insurance-policies | Cria uma nova solicitação de apólice. |
| **GET** | /api/insurance-policies/{id} | Consulta uma solicitação por ID da apólice. |
| **GET** | /api/insurance-policies/customer/{customerId} | Consulta solicitações por ID do cliente. |
| **PATCH** | /api/insurance-policies/{id}/cancel | Cancela uma solicitação de apólice, se aplicável. |
| **GET** | /health | Verifica o status de saúde da aplicação. |

<h2 id="autor">👨‍💻 Autor </h2>

<a href="https://github.com/jPedroSoares">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/39804819?v=4" width="100px;" alt=""/>
 <br />
 <sub><b>João Soares</b></sub></a>

👋🏽 Entre em contato!

[![Linkedin Badge](https://img.shields.io/badge/-João%20Pedro-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/jpedrosoares1/)](https://www.linkedin.com/in/jpedrosoares1/)
[![Gmail Badge](https://img.shields.io/badge/-joao.soares204@hotmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:joao.soares204@hotmail.com)](mailto:joao.soares204@hotmail.com)
