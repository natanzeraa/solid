# Aplicando princípios SOLID na prática com Java

Este projeto foi criado para estudar e demonstrar, na prática, os princípios **SOLID** usando **Java** e **Spring Boot**.  
Além dos exemplos de código relacionados a SOLID, o projeto já está configurado para se conectar a um **Autonomous Database** na **Oracle Cloud (OCI)** utilizando **Wallet** e migrações de banco com **Flyway**.

## Tecnologias principais

- Java 17  
- Spring Boot 3 / Spring Web MVC  
- Oracle JDBC (`ojdbc11`)  
- Flyway (migrações de banco)  
- Autonomous Database (OCI)

## Conexão com Autonomous Database (OCI) usando Wallet

Já deixamos preparada a infraestrutura para conectar o projeto a um Autonomous Database na OCI:

- Dependência do driver Oracle adicionada em `pom.xml` (`com.oracle.database.jdbc:ojdbc11-production`).
- Configuração do datasource apontando para Oracle em `src/main/resources/application.properties`.
- Migrações de banco versionadas em `src/main/resources/db/migration`.
- Endpoint simples para testar a conexão com o banco: `GET /test-db` no `TestController`.

### 1. Baixar e configurar o Wallet

1. Crie (ou use) um **Autonomous Database** na OCI.  
2. No console da OCI, baixe o **Client Credentials (Wallet)** do seu banco.  
3. Extraia o `.zip` do Wallet em um diretório local, por exemplo:

   ```bash
   ~/wallets/meu-autonomous-db
   ```

4. Opcionalmente, defina a variável de ambiente `TNS_ADMIN` apontando para esse diretório (o driver Oracle utiliza esse caminho para localizar os arquivos de configuração do Wallet):

   ```bash
   export TNS_ADMIN=~/wallets/meu-autonomous-db
   ```

### 2. Variáveis de ambiente usadas pelo Spring Boot

O arquivo `application.properties` está configurado para ler as credenciais e URL de conexão a partir de variáveis de ambiente:

- `DB_URL`  
- `DB_USER`  
- `DB_PASS`

Um exemplo de `DB_URL` para Autonomous Database (ajuste com os dados reais do seu ambiente) é:

```bash
export DB_URL="jdbc:oracle:thin:@meu_db_high?TNS_ADMIN=$HOME/wallets/meu-autonomous-db"
export DB_USER="USUARIO_DO_BANCO"
export DB_PASS="SENHA_DO_BANCO"
```

> Observação: a string de conexão exata é gerada pela própria OCI (na tela de detalhes do Autonomous Database). Use sempre a URL e alias recomendados pela OCI.

### 3. Migrações com Flyway

As migrações de banco são gerenciadas pelo **Flyway**, que é inicializado automaticamente quando a aplicação sobe:

- Migrations em: `src/main/resources/db/migration`
- Exemplos já existentes:
  - `V1__create_table_user.sql`
  - `V2__create_users_table_and_trg.sql`

Ao iniciar a aplicação com o banco acessível e as variáveis de ambiente configuradas, o Flyway vai aplicar essas migrações automaticamente na instância do Autonomous Database.

### 4. Testando a conexão com o banco

Com o projeto rodando (por exemplo com `mvn spring-boot:run` ou via IDE), você pode testar rapidamente se a aplicação está conseguindo se conectar ao banco acessando:

- `GET /test-db`

O `TestController` abre uma conexão via `DataSource` configurado pelo Spring Boot e retorna uma mensagem simples indicando se a conexão foi bem-sucedida.

## Próximos passos em SOLID

Além da infraestrutura de banco pronta, o repositório contém exemplos relacionados ao princípio de **Responsabilidade Única (Single Responsibility Principle)** dentro do pacote:

- `src/main/java/com/natanfelipe/solid/solid/SingleReponsibilityPrinciple`
Ao longo da evolução do projeto, outros princípios SOLID serão implementados e documentados aqui, sempre mantendo a mesma base de conexão com o Autonomous Database na OCI para sustentar os exemplos mais próximos de cenários reais.

## API REST: recurso de usuários

O principal exemplo de negócio do projeto é um CRUD simples de **Usuários**, exposto no path `/users`:

- `GET /users` – retorna a lista de usuários.  
- `GET /users/{id}` – retorna os detalhes de um usuário.  
- `POST /users` – cria um novo usuário.  
- `PUT /users/{id}` – atualiza um usuário existente.  
- `DELETE /users/{id}` – remove um usuário.

O payload utilizado em `POST /users` e `PUT /users/{id}` segue a estrutura do `UserDTO`:

```json
{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "email": "ada@example.com",
  "phone": "+55 11 99999-0000",
  "pix": "chave-pix-ada",
  "crc": "123456"
}
```

Internamente, o endpoint delega toda a lógica de negócio para o `UserService`, que:

- mapeia o DTO para a entidade JPA `Users`;  
- persiste a entidade via `UserRepository`;  
- publica um `UserCreatedEvent` após a criação bem‑sucedida.

## Responsabilidade Única na prática: eventos e e‑mails

O princípio de **Responsabilidade Única** é aplicado no fluxo de criação de usuário dividindo as responsabilidades em componentes pequenos e focados:

- `UserController` – expõe os endpoints HTTP e delega para a camada de serviço.  
- `UserService` – contém a lógica de aplicação para gerenciar usuários e publica um `UserCreatedEvent` quando um novo usuário é criado.  
- `UserCreatedEvent` – objeto de valor que carrega a instância criada de `Users`.  
- `UserMailListener` – escuta o `UserCreatedEvent` e é responsável por montar o modelo `Email` e chamar o serviço de e‑mail.  
- `EmailService` (via interface `IMail`) – encapsula a integração com o **Mailtrap** e realiza o envio do e‑mail.

Com esse desenho:

- a persistência de usuários **não** conhece detalhes da infraestrutura de e‑mail;  
- o envio de e‑mail **não** conhece detalhes de persistência ou HTTP;  
- cada classe tem um motivo claro para mudar (respeitando SRP).

## Configuração do Mailtrap (sandbox de e‑mail)

Para enviar e‑mails quando um novo usuário é criado, o projeto utiliza o **Mailtrap** como sandbox de e‑mail.  
O `EmailService` lê a configuração do arquivo `application.properties`:

- `mailtrap.token=${MAILTRAP_TOKEN}`  
- `mailtrap.sender=${MAILTRAP_SENDER}`

Antes de subir a aplicação, configure as variáveis de ambiente:

```bash
export MAILTRAP_TOKEN="seu-mailtrap-api-token"
export MAILTRAP_SENDER="from@example.com"
```

Esses valores são usados para construir o cliente Java do Mailtrap e enviar um e‑mail com um assunto personalizado como:

> `Hello, user@example.com from Java SRP (Single Responsibility Principle)!`

Esse fluxo, disparado na criação do usuário e desacoplado via eventos, complementa a configuração de banco e demonstra como os princípios SOLID podem ser aplicados em um cenário mais próximo do mundo real.
