# GymSystem V1

## Sobre o projeto

GymSystem V1 é um sistema backend para gerenciamento de academia desenvolvido em Java.

O projeto utiliza JDBC puro com arquitetura baseada em DAO, Services e entidades orientadas a objetos.

O objetivo do projeto é consolidar conhecimentos em:

- Java
- Programação Orientada a Objetos
- JDBC
- MySQL
- Arquitetura em camadas
- DAO Pattern
- Services
- Autenticação
- Segurança com BCrypt

## Tecnologias utilizadas

- Java 21
- Maven
- MySQL
- JDBC
- BCrypt
- IntelliJ IDEA

## Estrutura do projeto

src/main/java/com/tuon

- app -> inicialização do sistema
- db -> conexão, DAOs e persistência
- entities -> entidades do domínio
- enums -> enums do sistema
- services -> regras de negócio

## Funcionalidades

- Cadastro de usuários da academia
- Cadastro de funcionários
- Cadastro de exercícios
- Criação de treinos
- Associação de exercícios aos treinos
- Sistema de autenticação
- Hash de senhas com BCrypt

## Como executar

Entre na pasta:

cd GymSystem_V1

Compile:

mvn clean compile

Execute:

mvn exec:java -Dexec.mainClass="com.tuon.app.Main"

---

# PRÓXIMOS PASSOS

```md id="qr14h8"
## Próximos passos

- Implementar API REST com Spring Boot
- Implementar autenticação JWT
- Adicionar JPA/Hibernate
- Criar frontend
- Adicionar testes automatizados
```

---

# AUTOR

```md id="ob8e2g"
## Autor

Paulo Otávio Oliveira Tuon
```



## Próximos passos

## Autor
