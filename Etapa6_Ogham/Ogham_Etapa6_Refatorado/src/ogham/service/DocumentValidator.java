package ogham.service;

import ogham.model.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Valida os dados de um {@link Document} antes de persistir.
 *
 * Responsabilidade única (SRP): a única tarefa desta classe é decidir se um
 * documento é válido e por quê. Na Etapa 4 essa regra ("título obrigatório")
 * estava dentro de InsertDialog (uma classe de interface gráfica), misturando
 * validação de negócio com desenho de tela — um code smell clássico de
 * "God Class" / baixa coesão. Extraindo para cá, a mesma regra pode ser usada
 * tanto pelo Swing quanto por um futuro formulário web, sem duplicação.
 */
public class DocumentValidator {

    public List<String> validar(Document d) {
        List<String> erros = new ArrayList<>();

        if (d.getTitulo() == null || d.getTitulo().trim().isEmpty()) {
            erros.add("Título é obrigatório.");
        }
        if (d.getTipo() == null || d.getTipo().trim().isEmpty()) {
            erros.add("Tipo é obrigatório.");
        } else if (!d.getTipo().equals("PDF") && !d.getTipo().equals("JPEG")) {
            erros.add("Tipo deve ser 'PDF' ou 'JPEG'.");
        }
        if (d.getArquivoPath() == null || d.getArquivoPath().trim().isEmpty()) {
            erros.add("Caminho do arquivo é obrigatório.");
        }

        return erros;
    }

    public boolean valido(Document d) {
        return validar(d).isEmpty();
    }
}
