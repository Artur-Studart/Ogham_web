package ogham.service;

import java.util.List;

/**
 * Lançada pelo {@link DocumentService} quando um documento não passa nas
 * regras de {@link DocumentValidator}. Carrega a lista de erros para que a
 * interface (Swing hoje, web amanhã) possa exibi-los ao usuário.
 */
public class ValidationException extends RuntimeException {
    private final List<String> erros;

    public ValidationException(List<String> erros) {
        super(String.join(" | ", erros));
        this.erros = erros;
    }

    public List<String> getErros() {
        return erros;
    }
}
