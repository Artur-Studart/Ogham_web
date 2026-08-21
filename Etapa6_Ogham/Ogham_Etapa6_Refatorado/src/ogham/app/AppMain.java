package ogham.app;

import ogham.repository.DocumentRepository;
import ogham.repository.DocumentRepositoryJdbc;
import ogham.service.DocumentService;
import ogham.storage.FileStorageService;
import ogham.storage.LocalFileStorageService;
import ogham.ui.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do sistema desktop.
 *
 * Esta é a única classe que "conhece" as implementações concretas
 * (DocumentRepositoryJdbc, LocalFileStorageService) e as monta dentro de
 * DocumentService antes de passar tudo para a interface gráfica — um
 * "Composition Root". Nenhuma outra classe usa a palavra "new" para criar
 * essas dependências, o que é o que permite trocar a implementação (por
 * exemplo, em testes) sem tocar em MainFrame, InsertDialog ou DocumentService.
 */
public class AppMain {

    public static void main(String[] args) {
        DocumentRepository repository = new DocumentRepositoryJdbc();
        FileStorageService fileStorage = new LocalFileStorageService("data/downloads");
        DocumentService service = new DocumentService(repository, fileStorage);

        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame(service).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Não foi possível iniciar o Ogham. Verifique a conexão com o banco de dados.\n" + e.getMessage(),
                        "Erro ao iniciar", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
