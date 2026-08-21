package ogham.app;

import ogham.model.Document;
import ogham.repository.DocumentRepository;
import ogham.repository.InMemoryDocumentRepository;
import ogham.service.DocumentService;
import ogham.service.ValidationException;
import ogham.storage.FileStorageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Testes executáveis via {@code main()}, exigidos pela Etapa 6, para
 * certificar que as classes refatoradas continuam funcionando corretamente
 * após a separação de responsabilidades.
 *
 * Propositalmente NÃO usa DocumentRepositoryJdbc (que exigiria um MySQL
 * ligado): usa InMemoryDocumentRepository, o que só é possível porque
 * DocumentService depende da interface DocumentRepository (DIP). Isso torna
 * o teste rápido, repetível e independente de infraestrutura externa —
 * exatamente o tipo de coisa que a separação da Etapa 6 deveria viabilizar.
 *
 * Execução: java ogham.app.SelfTest
 */
public class SelfTest {

    private static int passou = 0;
    private static int falhou = 0;

    public static void main(String[] args) throws Exception {
        Path pastaTemp = Files.createTempDirectory("ogham-selftest-downloads");
        DocumentRepository repository = new InMemoryDocumentRepository();
        FileStorageService fileStorage = new ogham.storage.LocalFileStorageService(pastaTemp.toString());
        DocumentService service = new DocumentService(repository, fileStorage);

        testInserirDocumentoValido(service);
        testInserirDocumentoSemTituloDeveFalhar(service);
        testInserirDocumentoComTipoInvalidoDeveFalhar(service);
        testPesquisarPorTitulo(service);
        testPesquisarComTermoVazioRetornaTodos(service);
        testExcluirDocumento(service);
        testDownloadDeArquivoInexistente(service);

        System.out.println();
        System.out.println("Resumo: " + passou + " passaram, " + falhou + " falharam.");
        if (falhou > 0) {
            System.exit(1);
        }
    }

    private static void testInserirDocumentoValido(DocumentService service) {
        Document d = new Document();
        d.setTitulo("Carta de teste");
        d.setTipo("PDF");
        d.setArquivoPath("data/docs/carta_1820.pdf");
        Document salvo = service.inserir(d);
        checar("Inserir documento válido gera ID", salvo.getId() > 0);
    }

    private static void testInserirDocumentoSemTituloDeveFalhar(DocumentService service) {
        Document d = new Document();
        d.setTipo("PDF");
        d.setArquivoPath("data/docs/carta_1820.pdf");
        try {
            service.inserir(d);
            checar("Inserir sem título deve lançar ValidationException", false);
        } catch (ValidationException e) {
            checar("Inserir sem título deve lançar ValidationException", true);
        }
    }

    private static void testInserirDocumentoComTipoInvalidoDeveFalhar(DocumentService service) {
        Document d = new Document();
        d.setTitulo("Documento qualquer");
        d.setTipo("DOCX");
        d.setArquivoPath("data/docs/arquivo.docx");
        try {
            service.inserir(d);
            checar("Inserir com tipo inválido deve lançar ValidationException", false);
        } catch (ValidationException e) {
            checar("Inserir com tipo inválido deve lançar ValidationException", true);
        }
    }

    private static void testPesquisarPorTitulo(DocumentService service) {
        Document d = new Document();
        d.setTitulo("Fotografia histórica única-xyz");
        d.setTipo("JPEG");
        d.setArquivoPath("data/docs/foto_1905.jpg");
        service.inserir(d);

        List<Document> resultado = service.pesquisar("única-xyz");
        checar("Pesquisa encontra documento pelo título", resultado.size() == 1);
    }

    private static void testPesquisarComTermoVazioRetornaTodos(DocumentService service) {
        int total = service.listarTodos().size();
        int viaPesquisaVazia = service.pesquisar("   ").size();
        checar("Pesquisa com termo vazio retorna todos os documentos", total == viaPesquisaVazia && total > 0);
    }

    private static void testExcluirDocumento(DocumentService service) {
        Document d = new Document();
        d.setTitulo("Documento a ser excluído");
        d.setTipo("PDF");
        d.setArquivoPath("data/docs/carta_1820.pdf");
        Document salvo = service.inserir(d);

        boolean excluiu = service.excluir(salvo.getId());
        Optional<Document> depois = service.buscarPorId(salvo.getId());
        checar("Excluir documento remove do repositório", excluiu && depois.isEmpty());
    }

    private static void testDownloadDeArquivoInexistente(DocumentService service) {
        Document d = new Document();
        d.setTitulo("Arquivo fantasma");
        d.setTipo("PDF");
        d.setArquivoPath("data/docs/nao_existe_" + System.nanoTime() + ".pdf");
        Optional<String> destino = service.baixarArquivo(d);
        checar("Download de arquivo inexistente retorna vazio (sem travar o sistema)", destino.isEmpty());
    }

    private static void checar(String descricao, boolean condicao) {
        if (condicao) {
            passou++;
            System.out.println("[OK]   " + descricao);
        } else {
            falhou++;
            System.out.println("[FALHOU] " + descricao);
        }
    }
}
