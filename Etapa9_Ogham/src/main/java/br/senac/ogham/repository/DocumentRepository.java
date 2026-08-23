package br.senac.ogham.repository;

import br.senac.ogham.model.Document;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    List<Document> listarTodos();
    List<Document> pesquisar(String termo);
    Optional<Document> buscarPorId(int id);
    Document inserir(Document d);
}
