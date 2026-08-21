package ogham.repository;

import ogham.model.Document;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistência para {@link Document}.
 *
 * Princípio da Inversão de Dependência (DIP): as camadas de regra de negócio
 * (service) e de interface (ui) dependem apenas desta abstração, nunca de uma
 * implementação concreta (JDBC, memória, etc.). Isso é o que torna o código
 * reaproveitável no futuro projeto web: basta criar uma nova implementação
 * (ex.: DocumentRepositoryJpa, DocumentRepositoryRest) sem tocar em
 * DocumentService nem nas regras de negócio.
 *
 * Princípio da Segregação de Interfaces (ISP): a interface expõe somente as
 * operações que os consumidores realmente precisam (CRUD + busca), sem
 * métodos de infraestrutura (conexão, transação, etc.).
 */
public interface DocumentRepository {

    List<Document> listarTodos();

    List<Document> pesquisar(String termo);

    Optional<Document> buscarPorId(int id);

    Document inserir(Document documento);

    boolean excluir(int id);
}
