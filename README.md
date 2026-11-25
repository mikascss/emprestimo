# Empréstimo de equipamentos

API REST para cadastrar equipamentos e colaboradores, registrar empréstimos e devoluções e consultar atrasos. Desenvolvida em Java 25, Spring Boot 4.1.2, Spring MVC, Spring Data JPA, Bean Validation e Lombok, com banco H2 e documentação Swagger/OpenAPI via springdoc 3.1.0.

## Como executar

É necessário ter o **JDK 25** instalado e `JAVA_HOME` apontando para ele. O Maven Wrapper está incluído; a primeira execução precisa de acesso à internet para baixar o Maven e as dependências.

Na raiz do projeto, no Windows/PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

Se já tiver Maven instalado, use `mvn spring-boot:run`. A aplicação inicia em **http://localhost:8081**. Não é necessário instalar um banco externo.

## Swagger / OpenAPI

Com a aplicação em execução:

- [Swagger UI](http://localhost:8081/swagger-ui.html): documentação interativa, organizada por equipamentos, colaboradores e empréstimos. Use **Try it out** para enviar requisições.
- [OpenAPI JSON](http://localhost:8081/v3/api-docs).
- [OpenAPI YAML](http://localhost:8081/v3/api-docs.yaml).

A especificação é gerada a partir dos controllers e DTOs, com descrições de operações, filtros, exemplos de campos e respostas HTTP. A API não possui autenticação implementada.

## Endpoints

| Método | Caminho | Função | Sucesso |
| --- | --- | --- | --- |
| GET | `/equipamentos` | Listar; filtros opcionais `tipo` e `disponivel` | 200 |
| GET | `/equipamentos/{id}` | Buscar equipamento | 200 |
| POST | `/equipamentos` | Cadastrar equipamento | 201 |
| PUT | `/equipamentos/{id}` | Atualizar nome e tipo | 200 |
| DELETE | `/equipamentos/{id}` | Solicitar exclusão (ver limitação abaixo) | 204 |
| GET | `/colaboradores` | Listar colaboradores | 200 |
| GET | `/colaboradores/{id}` | Buscar colaborador | 200 |
| POST | `/colaboradores` | Cadastrar colaborador | 201 |
| GET | `/colaboradores/{id}/emprestimos` | Consultar histórico do colaborador | 200 |
| GET | `/emprestimo` | Listar; `atrasados=true` retorna apenas atrasados | 200 |
| POST | `/emprestimo` | Registrar empréstimo | 201 |
| PUT | `/emprestimo/{id}/devolucao` | Registrar devolução | 200 |

Os filtros de equipamentos podem ser combinados, por exemplo: `/equipamentos?tipo=NOTEBOOK&disponivel=true`. Em empréstimos, `atrasados=false` ou a ausência do parâmetro retorna todos os registros. O histórico de um colaborador sem registros, inclusive um ID inexistente, retorna uma lista vazia.

## Exemplo de uso

Envie os corpos abaixo com `Content-Type: application/json`, pelo Swagger UI ou pelo cliente HTTP de sua preferência.

1. Cadastre um colaborador com `POST /colaboradores`:

```json
{
  "nome": "Maria Silva",
  "email": "maria@example.com",
  "cargo": "Analista"
}
```

2. Cadastre um equipamento com `POST /equipamentos`:

```json
{
  "patrimonio": "PAT-001",
  "nome": "Notebook Dell",
  "tipo": "NOTEBOOK"
}
```

3. Use os IDs retornados em `dados.id` para registrar um empréstimo com `POST /emprestimo`:

```json
{
  "equipamentoId": 1,
  "colaboradorId": 1
}
```

4. Para devolver, envie `PUT /emprestimo/{id}/devolucao`, substituindo `{id}` pelo ID do empréstimo. Essa requisição não precisa de corpo.

Para atualizar um equipamento, `PUT /equipamentos/{id}` recebe somente `nome` e `tipo`, ambos obrigatórios.

## Regras de negócio

- Patrimônio do equipamento e e-mail do colaborador devem ser únicos.
- Equipamentos novos ficam disponíveis. O empréstimo os torna indisponíveis, e a devolução os libera novamente.
- Um colaborador com empréstimo atrasado não pode realizar um novo empréstimo.
- A data prevista de devolução é calculada a partir da data atual e do tipo do equipamento.
- Um empréstimo está atrasado quando ainda não foi devolvido e a data prevista é anterior à data atual.
- Uma segunda devolução do mesmo empréstimo gera conflito.
- Equipamentos com qualquer histórico de empréstimos têm a exclusão bloqueada.

| Tipo | Prazo em dias |
| --- | --- |
| `NOTEBOOK` | 30 |
| `MONITOR` | 14 |
| `MOUSE` | 7 |
| `TECLADO` | 7 |
| `HEADSET` | 7 |
| `OUTRO` | 14 |

## Respostas

As respostas de sucesso com conteúdo usam o formato:

```json
{
  "status": 200,
  "mensagem": "Operação realizada com sucesso!",
  "dados": []
}
```

`dados` contém um objeto ou uma lista, conforme a operação. Respostas HTTP 204 não têm corpo. Erros tratados pela aplicação usam:

```json
{
  "message": "Equipamento indisponível."
}
```

Validações de campos e parâmetros inválidos retornam **400**, recursos não encontrados retornam **404**, e conflitos de negócio retornam **409**.

## Banco e configuração

As configurações ficam em `src/main/resources/application.properties`. Por padrão, o H2 persiste os dados em `./dados/emprestimos`, relativo ao diretório em que a aplicação é executada.

O [console H2](http://localhost:8081/h2-console) usa os seguintes valores de desenvolvimento:

| Campo | Valor padrão |
| --- | --- |
| Driver | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:file:./dados/emprestimos` |
| Usuário | `admin` |
| Senha | `admin123` |

É possível sobrescrever propriedades com variáveis de ambiente, como `SERVER_PORT`, `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`. O Hibernate usa `ddl-auto=update` para atualizar o esquema ao iniciar.

## Testes e build

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
java -jar target/emprestimo-0.0.1-SNAPSHOT.jar
```

No Linux/macOS, substitua `.\mvnw.cmd` por `./mvnw`. Para executar os testes sem utilizar o banco local persistente:

```powershell
.\mvnw.cmd test "-Dspring.datasource.url=jdbc:h2:mem:emprestimo-tests;DB_CLOSE_DELAY=-1" "-Dspring.jpa.hibernate.ddl-auto=create-drop"
```

## Docker

O Dockerfile compila a aplicação com Maven e Java 25. Execute:

```bash
docker build -t emprestimo .
docker run --rm -p 8081:8081 -v emprestimo-dados:/app/dados emprestimo
```

O volume preserva o banco entre execuções. Embora o Dockerfile declare `EXPOSE 8080`, a aplicação está configurada para escutar em **8081**, por isso o mapeamento acima usa essa porta. O build Docker pula os testes; execute-os separadamente.

## Estrutura

```text
src/main/java/io/github/mikascss/emprestimo/
├── config/        # Configuração OpenAPI
├── controller/    # Endpoints REST e documentação das operações
├── dto/           # Dados de entrada
├── exception/     # Exceções e tratamento de erros
├── model/         # Entidades, tipos e envelope de resposta
├── repository/    # Acesso ao banco via Spring Data JPA
└── service/       # Regras de negócio
src/main/resources/application.properties
src/test/java/io/github/mikascss/emprestimo/
```

## Demonstração

**Vídeo do projeto:** [link da apresentação](https://drive.google.com/file/d/1EpfhD76tITzhX9T-yxD46R9LMjqYrVSP/view?usp=sharing)
**link de deploy:** [link da aplicação](https://emprestimo-lllu.onrender.com)
