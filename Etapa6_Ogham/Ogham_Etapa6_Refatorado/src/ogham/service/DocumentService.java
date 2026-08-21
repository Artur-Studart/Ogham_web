package ogham.service;

import ogham.model.Document;
import ogham.repository.DocumentRepository;
import ogham.storage.FileStorageService;

import java.util.List;
import java.util.Optional;

/**
 * Camada de regras de negócio do Ogham.
 *
 * Responsabilidade única (SRP): orquestrar validação + persistência + download
 * de documentos. Não desenha nenhuma tela (não conhece Swing, JOptionPane,
 * JTable etc.) e não sabe se os dados vêm de MySQL, memória ou uma API — ela
 * só enxerga as abstrações {@link DocumentRepository} e {@link FileStorageService}.
 *
 * Inversão de Dependência (DIP): as dependências são recebidas via
 * construtor (injeção de dependência manual), nunca instanciadas com "new"
 * dentro da classe. Isso é o que permite:
 *   1) testar esta classe com um repositório em memória (ver app.SelfTest),
 *      sem precisar de um banco MySQL de verdade;
 *   2) reaproveitar exatamente esta classe no futuro projeto web, bastando
 *      passar a ela uma implementação de repositório adequada ao novo ambiente.
 *
 * Esta classe é o ponto central do trabalho de Etapa 6: era código que, no
 * projeto desktop, estava espalhado dentro de MainFrame e InsertDialog
 * (Swing), fortemente acoplado à interface gráfica.
 */
public class DocumentService {

    private final DocumentRepository repository;
    private final FileStorageService fileStorage;
    private final DocumentValidator validator;

    public DocumentService(DocumentRepository repository, FileStorageService fileStorage) {
        this(repository, fileStorage, new DocumentValidator());
    }

    public DocumentService(DocumentRepository repository, FileStorageService fileStorage,
                            DocumentValidator validator) {
        this.repository = repository;
        this.fileStorage = fileStorage;
        this.validator = validator;
    }

    public List<Document> listarTodos() {
        return repository.listarTodos();
    }

    /**
     * Pesquisa por termo. Termos vazios ou nulos retornam a listagem completa
     * — essa regra antes vivia implicitamente em duas cópias quase idênticas
     * de código dentro de MainFrame (loadAll() e doSearch()); agora existe em
     * um único lugar.
     */
    public List<Document> pesquisar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return repository.listarTodos();
        }
        return repository.pesquisar(termo.trim());
    }

    public Optional<Document> buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    /**
     * Insere um novo documento após validar seus dados.
     *
     * @throws ValidationException se o documento não atender às regras de negócio
     */
    public Document inserir(Document documento) {
        List<String> erros = validator.validar(documento);
        if (!erros.isEmpty()) {
            throw new ValidationException(erros);
        }
        return repository.inserir(documento);
    }

    public boolean excluir(int id) {
        return repository.excluir(id);
    }

    /**
     * Copia o arquivo do documento para a pasta de downloads.
     *
     * @return caminho de destino, se a cópia foi bem-sucedida
     */
    public Optional<String> baixarArquivo(Document documento) {
        return fileStorage.copiarParaDownload(documento.getArquivoPath());
    }
}
