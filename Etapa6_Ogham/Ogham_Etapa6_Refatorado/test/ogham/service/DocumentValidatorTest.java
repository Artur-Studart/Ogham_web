package ogham.service;

import ogham.model.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários de {@link DocumentValidator}.
 *
 * Assim como a calculadora de período, a validação é uma regra de negócio
 * pura: recebe um Document e devolve uma lista de erros, sem tocar em banco
 * de dados nem em componentes de interface gráfica.
 */
class DocumentValidatorTest {

    private final DocumentValidator validador = new DocumentValidator();

    private Document documentoValido() {
        Document d = new Document();
        d.setTitulo("Carta de 1820");
        d.setTipo("PDF");
        d.setArquivoPath("data/docs/carta_1820.pdf");
        return d;
    }

    @Test
    @DisplayName("Documento com todos os campos obrigatórios preenchidos deve ser válido")
    void documentoCompleto_valido() {
        assertTrue(validador.valido(documentoValido()));
    }

    @Test
    @DisplayName("Documento sem título deve ser inválido")
    void documentoSemTitulo_invalido() {
        Document d = documentoValido();
        d.setTitulo("  ");
        List<String> erros = validador.validar(d);
        assertFalse(erros.isEmpty());
        assertTrue(erros.contains("Título é obrigatório."));
    }

    @Test
    @DisplayName("Documento sem caminho de arquivo deve ser inválido")
    void documentoSemArquivo_invalido() {
        Document d = documentoValido();
        d.setArquivoPath(null);
        List<String> erros = validador.validar(d);
        assertTrue(erros.contains("Caminho do arquivo é obrigatório."));
    }

    @Test
    @DisplayName("Documento com tipo fora de PDF/JPEG deve ser inválido")
    void documentoComTipoInvalido_invalido() {
        Document d = documentoValido();
        d.setTipo("DOCX");
        List<String> erros = validador.validar(d);
        assertTrue(erros.contains("Tipo deve ser 'PDF' ou 'JPEG'."));
    }

    @Test
    @DisplayName("Documento sem título e sem arquivo deve acumular os dois erros")
    void documentoComMultiplosProblemas_acumulaErros() {
        Document d = new Document();
        d.setTipo("PDF");
        List<String> erros = validador.validar(d);
        assertTrue(erros.size() >= 2);
    }
}
