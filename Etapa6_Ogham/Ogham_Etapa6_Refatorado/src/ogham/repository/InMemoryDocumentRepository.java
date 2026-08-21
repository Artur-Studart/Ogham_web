package ogham.repository;

import ogham.model.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementação em memória de {@link DocumentRepository}.
 *
 * Não faz parte do sistema em produção: existe para (1) permitir testes
 * automatizados sem depender de um banco MySQL real (ver {@code app.SelfTest})
 * e (2) servir de referência de como o futuro projeto web pode plugar outra
 * fonte de dados (ex.: uma API REST) sem alterar DocumentService nem as telas.
 * Essa troca só é possível porque o restante do sistema depende da interface
 * DocumentRepository, e não de DocumentRepositoryJdbc (Inversão de Dependência).
 */
public class InMemoryDocumentRepository implements DocumentRepository {

    private final List<Document> dados = new ArrayList<>();
    private final AtomicInteger sequencia = new AtomicInteger(1);

    @Override
    public List<Document> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public List<Document> pesquisar(String termo) {
        String alvo = termo == null ? "" : termo.toLowerCase();
        List<Document> resultado = new ArrayList<>();
        for (Document d : dados) {
            boolean bate = contem(d.getTitulo(), alvo) || contem(d.getAutor(), alvo) || contem(d.getTags(), alvo);
            if (bate) {
                resultado.add(d);
            }
        }
        return resultado;
    }

    @Override
    public Optional<Document> buscarPorId(int id) {
        return dados.stream().filter(d -> d.getId() == id).findFirst();
    }

    @Override
    public Document inserir(Document d) {
        d.setId(sequencia.getAndIncrement());
        dados.add(d);
        return d;
    }

    @Override
    public boolean excluir(int id) {
        return dados.removeIf(d -> d.getId() == id);
    }

    private boolean contem(String campo, String alvo) {
        return campo != null && campo.toLowerCase().contains(alvo);
    }
}
