package ogham.ui;

import ogham.model.Document;
import ogham.repository.RepositoryException;
import ogham.service.DocumentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Tela principal (Swing).
 *
 * Responsabilidade única (SRP): apenas desenhar componentes e reagir a
 * eventos de UI. Toda regra de negócio (validação, busca, persistência,
 * download) foi extraída para {@link DocumentService}; esta classe só chama
 * a camada de serviço e mostra o resultado.
 *
 * Refatorações em relação à Etapa 4:
 *  - loadAll() e doSearch() foram unificadas em refresh(String), eliminando
 *    duplicação de código (code smell: Duplicated Code).
 *  - getSelected() não dispara mais uma nova consulta ao banco a cada clique
 *    na tabela (code smell: consulta redundante); usa um cache em memória dos
 *    documentos exibidos, atualizado a cada refresh().
 *  - Erros de repositório (RepositoryException) são tratados aqui, mostrando
 *    uma mensagem amigável, em vez de o programa simplesmente imprimir stack
 *    trace e continuar com uma tela "vazia" sem explicação ao usuário.
 */
public class MainFrame extends JFrame {

    private final DocumentService service;

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;

    private final Map<Integer, Document> documentosExibidos = new HashMap<>();

    public MainFrame(DocumentService service) {
        this.service = service;
        setTitle("Ogham - Etapa 6 (refatorado)");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        refresh(null);
    }

    private void initUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(40);
        JButton btnSearch = new JButton("Pesquisar");
        btnSearch.addActionListener(e -> refresh(txtSearch.getText()));
        top.add(new JLabel("Pesquisar: "));
        top.add(txtSearch);
        top.add(btnSearch);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Tipo", "Data", "Arquivo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDownload = new JButton("Download");
        JButton btnDelete = new JButton("Excluir");
        JButton btnInsert = new JButton("Inserir novo");
        bottom.add(btnInsert);
        bottom.add(btnDelete);
        bottom.add(btnDownload);
        add(bottom, BorderLayout.SOUTH);

        btnInsert.addActionListener(e -> openInsertDialog());
        btnDelete.addActionListener(e -> doDelete());
        btnDownload.addActionListener(e -> doDownload());
    }

    private void refresh(String termo) {
        try {
            List<Document> lista = service.pesquisar(termo);
            model.setRowCount(0);
            documentosExibidos.clear();
            for (Document d : lista) {
                documentosExibidos.put(d.getId(), d);
                model.addRow(new Object[]{d.getId(), d.getTitulo(), d.getAutor(), d.getTipo(), d.getData(), d.getArquivoPath()});
            }
        } catch (RepositoryException e) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Document getSelected() {
        int r = table.getSelectedRow();
        if (r < 0) return null;
        int id = (int) model.getValueAt(r, 0);
        return documentosExibidos.get(id);
    }

    private void openInsertDialog() {
        InsertDialog dialog = new InsertDialog(this, service);
        dialog.setVisible(true);
        refresh(txtSearch.getText());
    }

    private void doDelete() {
        Document d = getSelected();
        if (d == null) {
            JOptionPane.showMessageDialog(this, "Selecione um documento.");
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?");
        if (c == JOptionPane.YES_OPTION) {
            try {
                boolean ok = service.excluir(d.getId());
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Removido.");
                    refresh(txtSearch.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao remover.");
                }
            } catch (RepositoryException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doDownload() {
        Document d = getSelected();
        if (d == null) {
            JOptionPane.showMessageDialog(this, "Selecione um documento.");
            return;
        }
        Optional<String> destino = service.baixarArquivo(d);
        if (destino.isPresent()) {
            JOptionPane.showMessageDialog(this, "Arquivo copiado para: " + destino.get());
        } else {
            JOptionPane.showMessageDialog(this, "Falha no download. Verifique o caminho.");
        }
    }
}
