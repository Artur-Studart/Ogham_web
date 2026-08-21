# Ogham – Etapa 6 (Núcleo refatorado)

Este projeto é a refatoração do sistema desktop Ogham (Etapa 4), preparando o
código para reaproveitamento no futuro sistema web (Etapas 7–9). O pacote
`ogham` foi reorganizado em camadas, aplicando princípios SOLID.

## Estrutura de pacotes

```
src/ogham/
 ├── model/       → Document (dado puro, sem regra de negócio)
 ├── repository/  → DocumentRepository (interface) + implementações (JDBC, memória)
 ├── service/     → DocumentService (regras de negócio) + DocumentValidator
 ├── storage/     → FileStorageService (interface) + LocalFileStorageService
 ├── db/          → DBConfig + DBConnection (conexão configurável)
 ├── ui/          → MainFrame e InsertDialog (Swing, só chamam DocumentService)
 └── app/         → AppMain (composition root) e SelfTest (testes via main())
```

A ideia central: **`service` e `model` não sabem que existe Swing, MySQL ou
JDBC** — dependem apenas das interfaces `DocumentRepository` e
`FileStorageService`. É essa separação que permite, na próxima etapa, criar um
controlador web (Servlet/Spring/etc.) que reaproveita `DocumentService`,
`DocumentValidator` e `Document` sem alterar uma linha sequer neles.

## Como executar no NetBeans

1. Crie um novo projeto **Java com Ant → Java Application** no NetBeans, sem
   classe principal criada automaticamente.
2. Copie a pasta `src/ogham` para dentro de `Source Packages` do novo projeto.
3. Copie `resources/db.properties` para `Source Packages` também (mesma pasta
   raiz de `ogham`), ou adicione a pasta `resources` como uma *Source Package*
   extra em Propriedades do Projeto → Source. Isso garante que o arquivo vá
   para o classpath e seja encontrado por `DBConfig`.
4. Importe `script.sql` no MySQL Workbench (cria o banco `oghamdb`).
5. Ajuste `resources/db.properties` se seu usuário/senha do MySQL forem
   diferentes (não precisa mexer no código Java).
6. Adicione o driver **MySQL Connector/J** nas Libraries do projeto.
7. Defina `ogham.app.AppMain` como classe principal do projeto e execute.

## Como rodar os testes (`main()`)

Defina `ogham.app.SelfTest` como classe principal (ou rode via linha de
comando) e execute. Ela não depende do MySQL — usa um repositório em memória
— então funciona mesmo sem o banco configurado:

```
java -cp build ogham.app.SelfTest
```

Saída esperada: uma lista de `[OK]` para cada regra de negócio testada e um
resumo final `0 falharam`.

## Relação com o projeto original (Etapa 4)

| Etapa 4 (desktop) | Etapa 6 (refatorado) | O que mudou |
|---|---|---|
| `model.Document` | `ogham.model.Document` | Só mudou de pacote |
| `dao.DocumentDAO` | `ogham.repository.DocumentRepository` + `DocumentRepositoryJdbc` | Virou uma interface + implementação; erros não são mais engolidos |
| `util.FileUtil` | `ogham.storage.FileStorageService` + `LocalFileStorageService` | Virou uma interface + implementação |
| `db.DBConnection` | `ogham.db.DBConnection` + `DBConfig` | Credenciais saíram do código-fonte |
| Validação dentro de `ui.InsertDialog` | `ogham.service.DocumentValidator` | Regra de negócio extraída da tela |
| — | `ogham.service.DocumentService` | Nova camada, orquestra tudo (não existia antes) |
| `ui.MainFrame`, `ui.InsertDialog` | `ogham.ui.MainFrame`, `ogham.ui.InsertDialog` | Agora só chamam `DocumentService` |

Veja o relatório `RELATORIO_ETAPA6.docx` para a justificativa detalhada de
cada princípio SOLID e cada refatoração aplicada.
