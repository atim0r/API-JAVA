# TaskFlow API

API REST para gerenciamento de tarefas, desenvolvida com Java 25 e Spring Boot 3.

## Recursos

- CRUD de tarefas
- Status: `TODO`, `IN_PROGRESS` e `DONE`
- Prioridades: `LOW`, `MEDIUM`, `HIGH` e `CRITICAL`
- Validação de entrada e respostas de erro consistentes
- Persistência com H2
- Documentação OpenAPI/Swagger
- Health checks com Spring Actuator
- Testes de integração
- Containerização com Docker

## Executar localmente

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

- Swagger UI: `http://localhost:8080/docs`
- Health: `http://localhost:8080/actuator/health`

## Executar com Docker

```bash
docker compose up --build
```

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/tasks` | Lista todas as tarefas |
| `GET` | `/api/v1/tasks/{id}` | Busca uma tarefa |
| `POST` | `/api/v1/tasks` | Cria uma tarefa |
| `PUT` | `/api/v1/tasks/{id}` | Atualiza uma tarefa |
| `DELETE` | `/api/v1/tasks/{id}` | Remove uma tarefa |

### Exemplo

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Publicar API","description":"Preparar deploy","priority":"HIGH"}'
```

```json
{
  "title": "Publicar API",
  "description": "Preparar deploy",
  "status": "TODO",
  "priority": "HIGH"
}
```

## Testes

```bash
mvn verify
```
