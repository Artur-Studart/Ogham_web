package ogham.service;

/**
 * Regra de negócio de cálculo simples, criada na Etapa 7 especificamente para
 * ter uma funcionalidade fácil de testar com JUnit (sem depender de banco de
 * dados), conforme pedido na atividade.
 *
 * Responsabilidade única (SRP): calcula, a partir da data de um documento
 * (formato "yyyy-MM-dd"), a idade em anos e o período histórico
 * correspondente. Não acessa banco, não conhece Swing — é uma função pura,
 * o tipo de código mais simples de cobrir com testes unitários.
 */
public class HistoricalPeriodCalculator {

    /**
     * Calcula quantos anos se passaram entre a data do documento e o ano de
     * referência informado (normalmente o ano atual).
     *
     * @throws IllegalArgumentException se a data estiver em formato inválido
     *                                  ou for uma data no futuro em relação ao ano de referência
     */
    public int calcularIdadeAnos(String dataIso, int anoReferencia) {
        int ano = extrairAno(dataIso);
        int idade = anoReferencia - ano;
        if (idade < 0) {
            throw new IllegalArgumentException("A data do documento está no futuro em relação ao ano de referência.");
        }
        return idade;
    }

    /**
     * Classifica o documento em um período histórico, a partir do ano de sua data.
     *
     * @throws IllegalArgumentException se a data estiver em formato inválido
     */
    public String classificarPeriodo(String dataIso) {
        int ano = extrairAno(dataIso);
        if (ano <= 1900) {
            return "Século XIX ou anterior";
        } else if (ano <= 2000) {
            return "Século XX";
        } else {
            return "Século XXI";
        }
    }

    private int extrairAno(String dataIso) {
        if (dataIso == null || dataIso.length() < 4) {
            throw new IllegalArgumentException("Data em formato inválido. Use yyyy-MM-dd.");
        }
        String prefixoAno = dataIso.substring(0, 4);
        try {
            return Integer.parseInt(prefixoAno);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Data em formato inválido. Use yyyy-MM-dd.", e);
        }
    }
}
