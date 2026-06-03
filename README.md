# GymSystem V1

## Sobre o Projeto

GymSystem V1 é um sistema backend para gerenciamento de academias desenvolvido em Java.

O projeto foi criado com o objetivo de consolidar conhecimentos em desenvolvimento back-end, banco de dados e arquitetura em camadas, simulando um sistema corporativo real.

## Objetivos de Aprendizado

- Programação Orientada a Objetos (POO)
- Java
- JDBC
- MySQL
- Arquitetura em Camadas
- DAO Pattern
- Service Layer
- Tratamento de Exceções
- Autenticação
- Segurança com BCrypt
- Auditoria de Operações

---

## Tecnologias Utilizadas

- Java 25
- Maven
- MySQL
- JDBC
- BCrypt
- IntelliJ IDEA

---

## Estrutura do Projeto

```text
src/main/java/com/tuon

├── app
├── db
│   ├── connection
│   ├── DAO
│   └── DAOImpl
├── entities
├── enums
├── exceptions
├── logs
│   ├── audit
│   └── reports
├── services
└── util
```

### Pacotes

#### app

Responsável pela inicialização da aplicação.

#### db

Contém toda a camada de persistência:

- Conexão com banco
- Interfaces DAO
- Implementações DAO

#### entities

Entidades do domínio do sistema.

Exemplos:

- GymUser
- Employee
- Exercise
- Workout
- WorkoutExercise

#### enums

Enums utilizados pelas entidades.

#### services

Camada de regras de negócio.

#### logs

Sistema de auditoria e geração de relatórios.

#### exceptions

Exceções customizadas da aplicação.

#### util

Classes utilitárias.

---

## Funcionalidades

### Usuários

- Cadastrar aluno
- Atualizar aluno
- Excluir aluno
- Consultar aluno

### Funcionários

- Cadastrar funcionário
- Atualizar funcionário
- Excluir funcionário
- Consultar funcionário
- Controle de cargos
- Controle salarial

### Exercícios

- Cadastrar exercício
- Atualizar exercício
- Excluir exercício
- Consultar exercício

### Treinos

- Criar treino
- Atualizar treino
- Excluir treino
- Associar exercícios ao treino
- Controle de status
- Cálculo de volume total

### Autenticação

- Login com usuário e senha
- Hash de senha utilizando BCrypt
- Alteração de senha

### Auditoria

Registro de operações importantes do sistema:

- Login
- Cadastro
- Alteração
- Exclusão
- Matrículas
- Criação de treinos

### Relatórios

Exportação de relatórios em CSV:

- Todos os logs
- Por funcionário
- Por ação
- Por entidade
- Por período

---

## Modelo de Banco de Dados

### Tabelas

- employee
- gym_user
- exercises
- workouts
- workout_exercises
- audit_log

---

## Como Executar

### 1. Clonar o Projeto

```bash
git clone https://github.com/seu-usuario/GymSystem.git
```

### 2. Entrar na Pasta

```bash
cd GymSystem
```

### 3. Configurar Banco de Dados

Criar o banco MySQL e executar os scripts de criação das tabelas.

### 4. Compilar

```bash
mvn clean compile
```

### 5. Executar

```bash
mvn exec:java -Dexec.mainClass="com.tuon.app.Main"
```

---

## Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Main
 ↓
Services
 ↓
DAO
 ↓
MySQL
```

### Fluxo

1. Usuário executa uma ação
2. Service valida as regras de negócio
3. DAO acessa o banco de dados
4. Resultado retorna para a aplicação
5. Operação é registrada no Audit Log

---

## Próximas Melhorias (V2)

- API REST com Spring Boot
- JWT Authentication
- JPA/Hibernate
- Swagger/OpenAPI
- Testes Automatizados com JUnit
- Dashboard Administrativo
- Estatísticas e Indicadores
- Front-End Web

---

## Autor

### Paulo Otávio Oliveira Tuon

Projeto desenvolvido para estudos e evolução profissional nas áreas de:

- Desenvolvimento Back-End
- Java
- Banco de Dados
- Arquitetura de Software
- Sistemas Corporativos