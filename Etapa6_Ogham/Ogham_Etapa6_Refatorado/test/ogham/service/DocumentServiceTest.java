package ogham.service;

import ogham.model.Document;
import ogham.repository.DocumentRepository;
import ogham.repository.InMemoryDocumentRepository;
import ogham.storage.FileStorageService;
import ogham.storage.LocalFileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários de {@link DocumentService}.
 *
 * A atividade da Etapa 7 permite dispensar de testes automatizados as
 * funcionalidades que acessam banco de dados. Aqui usamos
 * {@link InMemoryDocumentRepository} em vez de {@code DocumentRepositoryJdbc}
 * — o que só é possível porque DocumentService depende da interface
 * DocumentRepository (Inversão de Dependência, aplicada na Etapa 6). Assim
 * testamos toda a regra de orquestração (validar → inserir, pesquisar,
 * excluir) sem precisar de um MySQL real rodando durante os testes.
 */
class DocumentServiceTest {

    private DocumentService service;

    @BeforeEach
    void setUp(@TempDir Path pastaTemporaria) {
        DocumentRepository repository = new InMemoryDocumentRepository();
        FileStorageService fileStorage = new LocalFileStorageService(pastaTemporaria.toString());
        service = new DocumentService(repository, fileStorage);
    }

    private Document documentoValido(String titulo) {
        Document d = new Document();
        d.setTitulo(titulo);
        d.setTipo("PDF");
        d.setData("1905-01-01");
        d.setArquivoPath("data/docs/carta_1820.pdf");
        return d;
    }

    @Test
    @DisplayName("Inserir documento válido deve gerar um ID maior que zero")
    void inserir_documentoValido_geraId() {
        Document salvo = service.inserir(documentoValido("Carta de teste"));
        assertTrue(salvo.getId() > 0);
    }

    @Test
    @DisplayName("Inserir documento inválido deve lançar ValidationException")
    void inserir_documentoInvalido_lancaExcecao() {
        Document semTitulo = new Document();
        semTitulo.setTipo("PDF");
        semTitulo.setArquivoPath("x.pdf");
        assertThrows(ValidationException.class, () -> service.inserir(semTitulo));
    }

    @Test
    @DisplayName("Pesquisar por termo presente no título deve retornar o documento")
    void pesquisar_encontraPorTitulo() {
        service.inserir(documentoValido("Fotografia única de 1905"));
        List<Document> resultado = service.pesquisar("única");
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Pesquisar com termo vazio deve retornar todos os documentos cadastrados")
    void pesquisar_termoVazio_retornaTodos() {
        service.inserir(documentoValido("Documento A"));
        service.inserir(documentoValido("Documento B"));
        assertEquals(service.listarTodos().size(), service.pesquisar("").size());
    }

    @Test
    @DisplayName("Excluir documento existente deve removê-lo do repositório")
    void excluir_documentoExistente_remove() {
        Document salvo = service.inserir(documentoValido("Documento a excluir"));
        boolean excluiu = service.excluir(salvo.getId());
        assertTrue(excluiu);
        Optional<Document> apos = service.buscarPorId(salvo.getId());
        assertTrue(apos.isEmpty());
    }

    @Test
    @DisplayName("Classificar período de um documento inserido deve refletir sua data")
    void classificarPeriodo_documentoDe1905_retornaSeculoXX() {
        Document salvo = service.inserir(documentoValido("Documento de 1905"));
        assertEquals("Século XX", service.classificarPeriodo(salvo));
    }
}
