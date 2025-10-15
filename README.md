# Walmart Angolano
Um sistema de gerenciamento de de estoques de um walmart, desenvolvido com foco em programação web.

## Sobre o Projeto
Desenvolvido com as tecnologias **Java  21**, **Maven**,**Spring Boot**, **Security**, e **PostgreSQL** via **Docker**

## Funcionalidades

O software possui as seguintes funcionalidades:

- Criação, edição, exclusão e pegar de dados:
- Usuario: email, nome e senha
- Produto: nome, descrição, preço e fornecedor
- Cupom: codigo, tipo e valor
- Pedidos: cliente, data, status, itens e cupons

Existem 3 tipos de usuarios, sendo eles:
- Admin: responsavel pelo gerenciamento do sistema
- Cliente: responsavel pelas compras dos produtos do walmart
- Fornecedor: responsavel pelo fornecimento dos produtos

## Como Utilizar o sistema

### Pré-requisitos

- [Java 21](https://jdk.java.net/21/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [Git](https://git-scm.com/)

## Passos
- **1. Clonar Repositório**:

```bash
git clone https://github.com/raian2209/ENTREGA-PROG-WEB
```

- **2. Setar as variaveis de ambiente**:

Antes de executar o projeto, é necessário definir as variáveis de ambiente usadas pelo contêiner do **PostgreSQL**.
Essas variáveis controlam o nome do banco, o usuário e a senha de acesso ao banco de dados.

As variáveis utilizadas são:
```bash
POSTGRES_USER       admin
POSTGRES_PASSWORD   suaSenhaSuperSecreta
POSTGRES_DB         gerenciamento_db
```

- **3. Com as variaveis definidas bastar subir a aplicação**:

```bash
docker-compose up -d
```
