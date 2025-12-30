# Coupon API

API REST para gerenciamento de cupons.

## Requisitos
- Java 21+
- Maven 

## Rodar localmente

```bash
./mvnw spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Rodar com Docker Compose

```bash
./mvnw -DskipTests package
docker compose up --build
```

## Contratos

### Criar cupom

```bash
curl -i -X POST http://localhost:8080/coupon \
  -H 'Content-Type: application/json' \
  -d '{
    "code": "ABC-123",
    "description": "string",
    "expirationDate": "2025-11-04T17:14:45.180Z",
    "published": false,
    "discountValue": 0.8
  }'
```

### Buscar cupom

```bash
curl -i http://localhost:8080/coupon/{id}
```

### Deletar cupom (soft delete)

```bash
curl -i -X DELETE http://localhost:8080/coupon/{id}
```

## Decisões
- Sanitização do código: remove tudo que não for `[A-Za-z0-9]` e transforma em maiúsculas. Após sanitização deve ter exatamente 6 caracteres.
