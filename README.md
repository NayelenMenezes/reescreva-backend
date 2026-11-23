# Reescreva — API REST (Back-end)

API REST desenvolvida em **Java 25** e **Spring Boot 4.1.2** para o projeto **Reescreva**, voltado ao mapeamento de turmas de EJA/alfabetização e captação de indicações de jovens, adultos e idosos em Irecê-BA.

## Tecnologias Utilizadas
- **Java 25** & **Spring Boot 4.1.2**
- **Spring Web MVC** (API RESTful)
- **Spring Data JPA / Hibernate**
- **PostgreSQL** (hospedado no Supabase)
- **Bean Validation** & **Lombok**

## Arquitetura do Projeto
Organizado no padrão *package-by-feature* dividido em camadas (`controller`, `service`, `repository`, `entity`, `dto`):
- `usuario`: Gestão de administradores e coordenadores, autenticação e proteção do último administrador ativo.
- `turma`: Cadastro de pontos de ensino com coordenadas geográficas, controle de capacidade/vagas e filtros por turno e bairro.
- `indicacao`: Registro de interesse vinculado às turmas (`@ManyToOne`), validação de idade mínima (15 anos), bloqueio de duplicidade e atualização automática de vagas ocupadas/liberadas.
- `infrastructure`: Configuração de CORS, inicializador de dados (`DataInitializer`) e tratamento global de exceções (`GlobalExceptionHandler` para HTTP 400, 401 e 404).

## Como Executar
1. Configure as credenciais do banco PostgreSQL no `src/main/resources/application.properties` (ou via variáveis de ambiente `DB_URL`, `DB_USER` e `DB_PASSWORD`).
2. Execute a classe `ReescrevaApplication.java` (a aplicação iniciará na porta `8080` e criará o administrador padrão `admin@reescreva.com` / `admin123`).
3. Importe `postman/collections/Reescreva-API` no Postman e execute via **Collection Runner** para validar todos os endpoints.