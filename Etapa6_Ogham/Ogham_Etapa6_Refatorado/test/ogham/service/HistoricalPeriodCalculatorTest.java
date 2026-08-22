package ogham.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testes unitários de {@link HistoricalPeriodCalculator}.
 *
 * Esta é a funcionalidade "de cálculo simples" pedida pela Etapa 7: não
 * acessa banco de dados nem arquivos, então pode ser testada de forma
 * totalmente isolada e rápida.
 */
class HistoricalPeriodCalculatorTest {

    private final HistoricalPeriodCalculator calculadora = new HistoricalPeriodCalculator();

    @Test
    @DisplayName("Documento de 1820 deve ser classificado como Século XIX ou anterior")
    void classificarPeriodo_documentoAntigo() {
        assertEquals("Século XIX ou anterior", calculadora.classificarPeriodo("1820-05-10"));
    }

    @Test
    @DisplayName("Documento de 1905 deve ser classificado como Século XX")
    void classificarPeriodo_seculoXX() {
        assertEquals("Século XX", calculadora.classificarPeriodo("1905-01-01"));
    }

    @Test
    @DisplayName("Documento do ano 2000 deve ser classificado como Século XX (limite da faixa)")
    void classificarPeriodo_limiteAno2000() {
        assertEquals("Século XX", calculadora.classificarPeriodo("2000-12-31"));
    }

    @Test
    @DisplayName("Documento de 2023 deve ser classificado como Século XXI")
    void classificarPeriodo_seculoXXI() {
        assertEquals("Século XXI", calculadora.classificarPeriodo("2023-07-14"));
    }

    @Test
    @DisplayName("Data em formato inválido deve lançar IllegalArgumentException")
    void classificarPeriodo_dataInvalida_lancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> calculadora.classificarPeriodo("data-invalida"));
    }

    @Test
    @DisplayName("Calcular idade em anos deve ser a diferença entre o ano de referência e o ano do documento")
    void calcularIdadeAnos_calculoCorreto() {
        int idade = calculadora.calcularIdadeAnos("1905-01-01", 2026);
        assertEquals(121, idade);
    }

    @Test
    @DisplayName("Calcular idade com data no futuro deve lançar IllegalArgumentException")
    void calcularIdadeAnos_dataNoFuturo_lancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> calculadora.calcularIdadeAnos("2030-01-01", 2026));
    }
}
