
<img src="/src/main/resources/images/preview.gif" width="800" alt="Project Logo">

# 🍃 SpringNAS (Mini NAS em Spring Boot)

Backend em **Spring Boot** para um “mini NAS” doméstico: faz **upload de arquivos grandes em partes (chunks)**, salva o **arquivo no disco** e guarda **metadados no banco via JPA**.
Também oferece **listagem paginada** e **download por streaming** (sem alocar o arquivo inteiro na RAM).

## Principais features

- Upload em 3 etapas:
  - `POST /upload/init` → cria uma “sessão” e retorna um `uploadId` gerado aleatoriamente
  - `POST /upload/chunk` → envia um pedaço do arquivo (multipart)
  - `POST /upload/finish` → junta os `.partN` e persiste metadados no banco
- Download de arquivo por **streaming**:
  - `GET /files/{id}/content` → `StreamingResponseBody` com `InputStream.transferTo(...)`
- Listagem de arquivos com **paginação**:
  - `GET /files` → retorna `Page<FileDTO>`
- Organização em camadas (Controller / Service / Repository) e DTO para listagem

## Stack / Tecnologias

- Java + Spring Boot (Spring Web / Spring Data JPA)
- Persistência: JPA (configure o banco no `application.properties`)
- Armazenamento de arquivos: **filesystem local** (pasta `Files/uploads` no diretório do projeto)

## Como funciona (arquitetura)

### Metadados no banco, arquivo no disco
O banco guarda somente informações como `id`, `nome`, `tamanho`, `data`, `path` e `temporary path`.
O conteúdo do arquivo fica no disco.

### Pastas usadas no disco

A aplicação cria/usa (a partir do `user.dir` do projeto):

- `Files/uploads/<nomeSemExtensao>/<arquivo>` → arquivo final
- `Files/tempUploads/<uploadId>/<uploadId>.partN` → chunks temporários

> Obs.: O merge usa um buffer (atualmente) de `50MB` (ver `UuidEntity.fileChunkSize`).

## Como rodar localmente

1. Configure o banco (JPA) no `application.properties` (exemplos abaixo).
2. Suba a aplicação pelo seu build (Maven ou Gradle).

### Maven (se o projeto usar Maven)
```bash
./mvnw spring-boot:run
# ou
mvn spring-boot:run
````

A aplicação expõe páginas simples:

* `GET /` → redireciona/abre a tela de upload
* `GET /upload` → tela de upload
* `GET /download` → tela de download

## Configuração de banco (exemplos)

> Ajuste conforme seu banco. O projeto usa `JpaRepository` (`FileResource`).

### Exemplo (H2 em arquivo)

```properties
spring.datasource.url=jdbc:h2:file:./data/springnas
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

### Exemplo (MySQL)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springnas
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## Endpoints da API

### 1) Iniciar upload

Cria a sessão e retorna um `uploadId`.

**Request**

* `POST /upload/init`
* `Content-Type: application/json`

Body:

```json
{
  "fileName": "video.mp4",
  "fileSize": 123456789
}
```

**Response**

```json
{ "uploadId": "Ab3kP9xQ1" }
```

### 2) Enviar chunk

Envia um chunk do arquivo (multipart).

* `POST /upload/chunk`
* `multipart/form-data`

Campos:

* `uploadId` (string)
* `chunkNumber` (número; recomendado começar em 1)
* `file` (o blob do chunk)

### 3) Finalizar upload

Junta os `.partN`, grava o arquivo final e salva metadados no banco.

**Request**

* `POST /upload/finish`
* `Content-Type: application/json`

Body:

```json
{ "uploadId": "Ab3kP9xQ1" }
```

**Response**

* `200 OK`

### 4) Listar arquivos (paginado)

* `GET /files`

Exemplo:

* `/files?page=0&size=10&sort=id,desc`

Response: `Page<FileDTO>` (campos: `id`, `name`, `size`, `date`)

### 5) Baixar conteúdo do arquivo (streaming)

* `GET /files/{id}/content`

Retorna:

* `Content-Type: application/octet-stream`
* `Content-Length: <tamanho>`
* `Content-Disposition: inline; filename="..."`

## Limitações atuais (MVP)

* Sessão de upload é **em memória** (se reiniciar a aplicação, perde o estado do `uploadId`)
* Não há **resume/retry inteligente** nem checksum (integridade) dos chunks
* Não há suporte a **HTTP Range** no download (sem resume parcial)
* Tratamento de erros HTTP (404/400) pode ser melhorado
