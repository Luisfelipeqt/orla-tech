# Orla Tech API

API RESTful para gerenciamento de **Projetos** e **Funcionários**, permitindo criar e listar registros. Cada projeto pode ter vários funcionários e cada funcionário pode participar de vários projetos (relação N:N).

---

## Tecnologias

- **Java:** 21
- **Spring Boot:** 3.5.5
- **Banco de Dados:** PostgreSQL via Docker
- **Testes:** Unitários e integrados
- **Documentação de API:** Padrão RESTful

---

## Endpoints

### Funcionários
- Base URL: `/v1/funcionarios`
- Operações disponíveis:
    - **POST** `/v1/funcionarios` → Criar funcionário
    - **GET** `/v1/funcionarios` → Listar funcionários

### Projetos
- Base URL: `/v1/projetos`
- Operações disponíveis:
    - **POST** `/v1/projetos` → Criar projeto
    - **GET** `/v1/projetos` → Listar projetos com seus funcionários

---

## Entidades

### Projeto
- `nome` (String, obrigatório)
- `dataCriacao` (LocalDate, obrigatório)

### Funcionário
- `nome` (String, obrigatório)
- `cpf` (String, obrigatório)
- `email` (String, obrigatório)
- `salario` (BigDecimal, obrigatório)

---

## Relação
- **Projetos ↔ Funcionários** → N:N

---

## Postman

Arquivo de collection disponível em:  
`./app/src/main/resources/postman/orla-tech.postman_collection.json`

---

## Execução via Docker

O projeto inclui um `docker-compose.yml` que provisiona:

- **PostgreSQL** (`postgres_db`)
- **PgAdmin** (`pgadmin_web`)
- **Aplicação Spring Boot** (`orla_tech_app`)

### Passos

1. Build e subida dos containers:

   ```bash
   docker-compose up -d --build
   ```
   
2. A aplicação estará acessível em:
   ```bash
   http://localhost:8081
   Exemplo: http://localhost:8081/v1/funcionarios ou http://localhost:8081/v1/projetos
   ```

Observações

Segue a normalização do banco de dados.

A aplicação é documentada e segue padrões RESTful.

OBS sobre Testcontainers: além de nunca ter usado a ferramenta antes, só tive a segunda-feira para desenvolver todo o projeto. Ou seja, tudo foi feito em 1 dia, e o tempo de aprendizado foi muito curto para fazer os testes com a qualidade que eu gostaria. Então peço que relevem isso.