# TechManager Implements

Desafio de Desenvolvimento Back-End com Spring Boot

---

## 📘 Contexto

Este projeto foi desenvolvido como parte de um **desafio técnico backend**, no qual o objetivo é criar uma API RESTful em **Spring Boot** para gerenciamento de usuários. A proposta simula o backend de um sistema corporativo chamado **TechManage**, permitindo a realização de operações de CRUD (Create, Read, Update, Delete) com validação, persistência em banco de dados relacional e testes automatizados.

---

## 🎯 Objetivos Atendidos

- ✅ Criar, listar, buscar, atualizar e excluir usuários via API REST
- ✅ Conexão com banco de dados relacional (MySQL em produção / H2 para testes)
- ✅ Aplicação de validações em todos os dados recebidos
- ✅ Organização modular com uso da **arquitetura hexagonal**
- ✅ Testes unitários e de integração cobrindo controllers, serviços e validadores
- ✅ Documentação completa em `README.md` com exemplos de uso e execução local
- ✅ Projeto versionado e publicado no GitHub

---

## 🧱 Arquitetura: Hexagonal (Ports and Adapters)

### 🧩 O que é?

A arquitetura hexagonal, também conhecida como **Ports and Adapters**, tem como princípio central **separar as regras de negócio da infraestrutura**. Isso facilita testes, manutenções e futuras integrações.

### ✅ Por que é importante?

- Favorece o **princípio da inversão de dependências**
- Permite que os casos de uso (regras de negócio) não conheçam detalhes da camada externa (banco, frameworks, etc)
- Facilita a **testabilidade**, já que os adaptadores são injetáveis
- Estimula **código desacoplado**, limpo e de fácil evolução

### 📐 Como foi aplicada no projeto?

O projeto é dividido em camadas bem definidas:

```
gerenciar.usuario.desafio
├── adapter.input          # Entrada da aplicação (REST controllers, DTOs, Mappers)
├── domain.entity          # Regras de domínio (entidades, enums, DTOs)
├── port.input             # Portas de entrada para os casos de uso (interfaces)
├── usecase                # Implementação da lógica central da aplicação
├── infra.repository       # Adapters de saída (JPA repositories)
├── utils.validation       # Validações personalizadas
```

---

## ✅ Validações Personalizadas

Para garantir a integridade dos dados enviados à API, além do uso das anotações padrão do Jakarta Bean Validation (`@NotBlank`, `@Email`, `@Size`), foram implementadas **validações customizadas reutilizáveis**:

### 📞 `@ValidPhone`

Valida o número de telefone segundo as seguintes regras:
- Deve estar no formato internacional: **`+55 11 99999-9999`**
- Tamanho entre **16 e 20 caracteres**
- Campo **não pode ser nulo ou vazio**
- Usa uma expressão regular e checagem de comprimento

**Implementação técnica:**

- Criada anotação `@ValidPhone` com `@Constraint(validatedBy = PhoneValidator.class)`
- `PhoneValidator` implementa `ConstraintValidator<ValidPhone, String>` e contém a lógica de validação

---

### 🧾 `@ValidEnum`

Garante que o valor recebido em um campo `String` corresponda a um valor válido de um `enum` Java.


**Funcionamento interno:**

- Usa a API de `java.lang.annotation.*` para criar uma annotation customizada
- A classe `EnumValidator` implementa `ConstraintValidator<ValidEnum, String>`
- Compara o valor recebido com `enumClass.getEnumConstants()`, aplicando `toUpperCase()` para garantir flexibilidade

---

## 📦 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL (produção)
- H2 (testes)
- Jakarta Bean Validation (JSR-380)
- Lombok
- JUnit 5 + Mockito + mockito-inline

---

# 🚀 Como Executar e Testar a Aplicação Localmente


---

## 🛠️ Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- [Java 17+](https://www.oracle.com/br/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/)
- [MySQL](https://dev.mysql.com/downloads/mysql/)
- Uma IDE como [IntelliJ IDEA](https://www.jetbrains.com/idea/) (opcional)

---

## 📥 Clonando e Executando o Repositório

Siga os passos abaixo para baixar o projeto e executá-lo na sua máquina local:

```bash
# 1. Clone o repositório a partir do GitHub
git clone https://github.com/Akemi-Samantha/TechManage-Desafio.git

# 2. Acesse o diretório do projeto
cd TechManage-Desafio

# 3. Aguarde a indexação do Maven (caso esteja usando uma IDE)
```

---

### 🧑‍💻 Abrindo o projeto

Você pode abrir o projeto de duas formas:

- **Via terminal:** continue com os comandos abaixo.
- **Via IDE (ex: IntelliJ IDEA):** abra a pasta clonada como projeto Maven e aguarde a sincronização.

---

### ▶️ Executando o projeto

#### Pelo terminal:

```bash
# Compila e sobe a aplicação Spring Boot
./mvnw spring-boot:run
```

#### Pela IntelliJ:

1. Navegue até a classe `DesafioApplication.java`
2. Clique com o botão direito e selecione `Run 'DesafioApplication'`

A API estará disponível em:

```
http://localhost:8080/api/users
```

---

### 🧪 Rodando os testes

#### Pelo terminal:

```bash
./mvnw test
```

#### Pela IDE:

- Clique com o botão direito sobre qualquer classe de teste (ex: `UserControllerTest.java`)
- Selecione: **Run 'ClasseDeTeste'**

---

### 📡 Testando a API

Você pode usar ferramentas como:

- [Postman](https://www.postman.com/)
- [Insomnia](https://insomnia.rest/)
- Extensões de teste de API no próprio IntelliJ



---

## 🔧 Configurando o Banco de Dados

1. Inicie o MySQL e crie o banco com:

```sql
CREATE DATABASE desafio;
```

2. Altere o arquivo `src/main/resources/application.properties` se necessário:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/desafio
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

---

## ▶️ Executando o Projeto

### Via terminal:

```bash
./mvnw spring-boot:run
```

### Via IntelliJ:

1. Abra o projeto
2. Execute a classe `DesafioApplication.java`
3. A aplicação estará rodando em:  
   `http://localhost:8080/api/users`

---

# 📮 Endpoints da API

---

## 🔹 POST `/api/users`

### ➕ Cadastrar um novo usuário

**Request Body (JSON):**

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "phone": "+55 11 99999-9999",
  "birthDate": "1995-03-10",
  "userType": "ADMIN"
}
```

### 🔒 Validações:

| Campo       | Validação                         |
|-------------|-----------------------------------|
| name        | Obrigatório, não pode ser vazio   |
| email       | Obrigatório, formato válido       |
| phone       | Obrigatório, `@ValidPhone`        |
| birthDate   | Obrigatório, formato `yyyy-MM-dd` |
| userType    | Obrigatório, `@ValidEnum`         |

### ✅ Resposta:

```json
{
  "result": {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "phone": "+55 11 99999-9999",
    "birthDate": "1995-03-10",
    "userType": "ADMIN"
  },
  "message": "Usuário cadastrado com sucesso!"
}
```

---

## 🔹 GET `/api/users`

### 🔍 Lista todos os usuários

**Resposta:**

```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "phone": "+55 11 99999-9999",
    "birthDate": "1995-03-10",
    "userType": "ADMIN"
  }
]
```

---

## 🔹 GET `/api/users/{id}`

### 🔎 Busca um usuário por ID

**Resposta:**

- ✅ Sucesso: retorna o usuário
- ❌ Erro 404: se o ID não existir

---

## 🔹 GET `/api/users/filter?type=ADMIN`

### 🧾 Filtra usuários por tipo

| Tipo permitido | Descrição         |
|----------------|-------------------|
| ADMIN          | Administrador     |
| EDITOR         | Permissão média   |
| VIEWER         | Leitura apenas    |

---

## 🔹 PUT `/api/users/{id}`

### ✏️ Atualiza um usuário existente

Mesmas validações do `POST`. O ID deve existir.

**Resposta:**

```json
{
  "result": {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "phone": "+55 11 99999-9999",
    "birthDate": "1995-03-10",
    "userType": "EDITOR"
  },
  "message": "Usuário atualizado com sucesso!"
}
```

---

## 🔹 DELETE `/api/users/{id}`

### 🗑️ Remove um usuário

- ✅ Sucesso: retorna status `204 No Content`
- ❌ Erro 404: se o ID não existir

---

# 🧩 Explicação das Principais Classes

| Classe                        | Responsabilidade                                                                 |
|------------------------------|----------------------------------------------------------------------------------|
| `UserController`             | Recebe requisições HTTP, valida entradas e chama os casos de uso                |
| `UserRequest / UserResponse` | Representam os dados de entrada e saída da API                                  |
| `UserDTO`                    | Objeto de transporte entre camadas de domínio                                   |
| `UserUseCase`                | Implementa a lógica de negócio (create, update, delete...)                      |
| `IUserUseCase`               | Interface usada pela controller para abstração da lógica                        |
| `UserRepository`             | Interface JPA para persistência dos dados no banco                              |
| `UserMapper`                 | Converte entre entidades e DTOs/Requests/Responses                              |
| `@ValidEnum` / `@ValidPhone` | Validações customizadas com anotação e lógica encapsulada em classes validator |

---

# 🧪 Executando os Testes

### Rodar testes unitários e de integração:

```bash
./mvnw test
```

Cobertura:

## 🧪 Testes Automatizados

- ✅ Testes unitários com `assertEquals` para todos os endpoints das classes UserController e UserUseCase.
- ✅ Testes de integração para todos os endpoints das classes UserController e UserUseCase.
- ✅ Testes para validações `@ValidPhone` e `@ValidEnum`
- ✅ Mock de métodos estáticos com `mockito-inline`
- ✅ Testes de integração com banco H2 (`application-test.properties`)

---

## 💡 Possíveis melhorias futuras

- [ ] Documentação com Swagger/OpenAPI
- [ ] Autenticação com Spring Security + JWT
- [ ] Paginação nos endpoints de listagem
- [ ] Upload de imagens para perfil de usuário
- [ ] Versionamento da API (ex: `/api/v1`)

---


# ✅ Considerações Finais

Este projeto foi construído com o objetivo de aplicar boas práticas de design, código limpo, arquitetura desacoplada e testabilidade. A escolha por arquitetura hexagonal favoreceu a manutenibilidade e clareza da lógica central da aplicação.

---

## 🧑‍💻 Autor

Desenvolvido por **Akemi Samantha Nakayama** como parte de um desafio técnico backend.  
Entre em contato para sugestões, dúvidas ou colaborações.

---

