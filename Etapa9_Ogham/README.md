# Ogham Web — Etapa 9

Implementação final do Projeto Integrador Senac: Spring Boot + Spring JDBC + MySQL, integração com o front-end da Etapa 8, autenticação administrativa, upload/download e testes.

## Requisitos
- Java 17+
- Maven 3.9+
- MySQL 8+
- NetBeans pode abrir o projeto como projeto Maven existente.

## Banco
1. Abra o MySQL Workbench.
2. Execute `database/schema.sql`.
3. Configure `OGHAM_DB_URL`, `OGHAM_DB_USER` e `OGHAM_DB_PASSWORD` ou ajuste `src/main/resources/application.properties`.

O script cria `oghamdb`, as tabelas `documentos` e `administradores` e três registros de demonstração.

## Execução
```bash
mvn clean test
mvn spring-boot:run
```
Acesse `http://localhost:8080/`.

Login inicial: `admin` / `ogham123`.

## Funcionalidades da Etapa 9
- API REST para listar, pesquisar e consultar documentos.
- Persistência real em MySQL via Spring JDBC.
- Login administrativo com sessão HTTP e credencial armazenada como SHA-256.
- Upload de PDF/JPEG com validação de extensão e tipo.
- Download do arquivo armazenado.
- Reaproveitamento das regras de validação e classificação de período do projeto anterior.
- Front-end responsivo integrado por `fetch()`.
- Testes JUnit 5 das regras de serviço.

## Testes manuais da Etapa 7 atendidos
1. Login correto/incorreto.
2. Upload PDF/JPEG e rejeição de arquivo incompatível.
3. Listagem e busca por título/autor/tag.
4. Detalhe e download.
5. Validação de título/tipo/arquivo.
6. Responsividade em viewport móvel.

## Observação de segurança acadêmica
A senha inicial é apenas para demonstração da atividade. Em produção, deve-se usar hash adaptativo (BCrypt/Argon2), HTTPS, controle de autorização mais completo e gestão de segredos fora do código.
