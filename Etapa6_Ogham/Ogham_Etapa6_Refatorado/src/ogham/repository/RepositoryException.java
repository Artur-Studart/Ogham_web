package ogham.repository;

/**
 * Exceção lançada quando uma operação de persistência falha.
 *
 * Refatoração de code smell: no código original (Etapa 4), o DAO capturava
 * SQLException, imprimia no console (printStackTrace) e retornava null/false/
 * lista vazia. Isso escondia erros reais (ex.: banco fora do ar) do restante
 * do sistema, que não tinha como diferenciar "nenhum resultado" de "ocorreu
 * um erro". Agora a falha é propagada como uma exceção de negócio (unchecked,
 * sem acoplar o restante do sistema a java.sql.SQLException), permitindo que
 * a camada de serviço e a interface tratem o erro de forma explícita.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
