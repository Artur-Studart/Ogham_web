package br.senac.ogham.service;

import br.senac.ogham.model.Document;
import br.senac.ogham.repository.DocumentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private final DocumentRepository repository;
    public DocumentService(DocumentRepository repository) { this.repository = repository; }
    public List<Document> listarTodos() { return repository.listarTodos(); }
    public List<Document> pesquisar(String termo) { return termo == null || termo.isBlank() ? repository.listarTodos() : repository.pesquisar(termo); }
    public Optional<Document> buscarPorId(int id) { return repository.buscarPorId(id); }
    public Document inserir(Document d) {
        if (d.titulo() == null || d.titulo().isBlank()) throw new IllegalArgumentException("Título é obrigatório.");
        if (!"PDF".equals(d.tipo()) && !"JPEG".equals(d.tipo())) throw new IllegalArgumentException("Tipo deve ser PDF ou JPEG.");
        if (d.arquivoPath() == null || d.arquivoPath().isBlank()) throw new IllegalArgumentException("Arquivo é obrigatório.");
        return repository.inserir(d);
    }
    public String classificarPeriodo(String data) {
        if (data == null || data.length() < 4) return "Data inválida";
        try { int ano = Integer.parseInt(data.substring(0,4)); return ano <= 1900 ? "Século XIX ou anterior" : ano <= 2000 ? "Século XX" : "Século XXI"; }
        catch (NumberFormatException e) { return "Data inválida"; }
    }
}
