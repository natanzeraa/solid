# SRP (Single Responsibility Principle) no projeto

Este documento descreve como o **SRP (Princípio da Responsabilidade Única)** foi aplicado neste repositório usando um exemplo simples de **CRUD de usuários** com disparo de **evento** e envio de **e‑mail** desacoplado (Mailtrap).

![Arquitetura do exemplo SRP](architecture.png)

## O que é SRP (em 1 frase)

Uma classe/módulo deve ter **um único motivo para mudar** — ou seja, deve ser responsável por **uma única coisa** bem definida.

## Onde está o exemplo

O exemplo de SRP vive neste pacote:

- `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple`

Estrutura (resumo):

```text
SingleReponsibilityPrinciple/
  Controllers/   -> camada HTTP (endpoints)
  dtos/          -> objetos de entrada/saída
  events/        -> eventos de domínio/aplicação
  listeners/     -> reação a eventos (efeitos colaterais)
  Models/        -> entidades/modelos
  Repositories/  -> persistência (JPA)
  Services/      -> regras de aplicação + integrações
```

## Como o SRP foi aplicado aqui

O fluxo foi dividido em responsabilidades pequenas e independentes:

- HTTP e roteamento: `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/Controllers/UserController.java`
- Caso de uso (aplicação) de usuários: `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/Services/UserService.java`
- Persistência (JPA): `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/Repositories/UserRepository.java`
- Evento disparado após criação: `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/events/UserCreatedEvent.java`
- Listener responsável pelo “efeito colateral” (e‑mail): `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/listeners/UserMailListener.java`
- Serviço de e‑mail (integração Mailtrap): `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/Services/EmailService.java`
- Contrato para envio (inversão de dependência do listener): `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/Services/interfaces/IMail.java`

### Resultado prático

- O `UserService` **não sabe** como e‑mail é enviado (ele só publica um evento).
- O `UserMailListener` **não sabe** como o usuário é persistido (ele só reage ao evento).
- O `EmailService` **só** cuida da integração com o provedor (Mailtrap).

Assim, cada componente tem um motivo claro para mudar:

- Mudou API/contrato HTTP? → `UserController`.
- Mudaram regras de criação de usuário? → `UserService`.
- Mudou banco ou estratégia de persistência? → `UserRepository` / JPA.
- Mudou provedor/SDK de e‑mail? → `EmailService`.
- Mudou quando/enviar ou o “conteúdo” do e‑mail após criar usuário? → `UserMailListener`.

## Walkthrough do fluxo (POST /users)

1. `POST /users` recebe um `UserDTO`:
   - `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple/dtos/UserDTO.java`
2. `UserController` delega para `UserService#createUser`.
3. `UserService` mapeia DTO → entidade `Users`, salva via `UserRepository` e publica `UserCreatedEvent`.
4. `UserMailListener` escuta `UserCreatedEvent`, monta um `Email` e chama `IMail#send`.
5. `EmailService` envia via Mailtrap (token e sender vêm do `application.properties`):
   - `src/main/resources/application.properties`

## Como rodar (mínimo)

### Requisitos

- Java 17+
- Maven Wrapper (`./mvnw`)

### Variáveis de ambiente

Banco:

- `DB_URL`
- `DB_USER`
- `DB_PASS`

Mailtrap:

- `MAILTRAP_TOKEN`
- `MAILTRAP_SENDER`

### Subir a aplicação

```bash
./mvnw spring-boot:run
```

### Testar o exemplo SRP

- Criar usuário: `POST /users`
- Ver lista: `GET /users`

Ao criar um usuário, o listener deve disparar o envio do e‑mail via Mailtrap.
