package ui;

import dao.DocumentDAO;
import model.Document;
import util.FileUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    private DocumentDAO dao = new DocumentDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;

    public MainFrame() {
        setTitle("Ogham - Etapa 4");
        setSize(1000,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadAll();
    }

    private void initUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(40);
        JButton btnSearch = new JButton("Pesquisar");
        btnSearch.addActionListener(e -> doSearch());
        top.add(new JLabel("Pesquisar: "));
        top.add(txtSearch);
        top.add(btnSearch);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID","Título","Autor","Tipo","Data","Arquivo"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDownload = new JButton("Download");
        JButton btnDelete = new JButton("Excluir");
        JButton btnInsert = new JButton("Inserir novo");
        bottom.add(btnInsert); bottom.add(btnDelete); bottom.add(btnDownload);
        add(bottom, BorderLayout.SOUTH);

        btnInsert.addActionListener(e -> openInsertDialog());
        btnDelete.addActionListener(e -> doDelete());
        btnDownload.addActionListener(e -> doDownload());
    }

    private void loadAll() {
        model.setRowCount(0);
        List<Document> list = dao.listarTodos();
        for (Document d : list) {
            model.addRow(new Object[]{d.getId(), d.getTitulo(), d.getAutor(), d.getTipo(), d.getData(), d.getArquivoPath()});
        }
    }

    private void doSearch() {
        String termo = txtSearch.getText().trim();
        model.setRowCount(0);
        List<Document> list = dao.pesquisar(termo);
        for (Document d : list) {
            model.addRow(new Object[]{d.getId(), d.getTitulo(), d.getAutor(), d.getTipo(), d.getData(), d.getArquivoPath()});
        }
    }

    private Document getSelected() {
        int r = table.getSelectedRow();
        if (r < 0) return null;
        int id = (int) model.getValueAt(r, 0);
        return dao.listarTodos().stream().filter(x->x.getId()==id).findFirst().orElse(null);
    }

    private void openInsertDialog() {
        InsertDialog id = new InsertDialog(this, dao);
        id.setVisible(true);
        loadAll();
    }

    private void doDelete() {
        Document d = getSelected();
        if (d==null) { JOptionPane.showMessageDialog(this, "Selecione um documento."); return; }
        int c = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?");
        if (c==JOptionPane.YES_OPTION) {
            boolean ok = dao.excluir(d.getId());
            if (ok) { JOptionPane.showMessageDialog(this, "Removido."); loadAll(); }
            else JOptionPane.showMessageDialog(this, "Falha ao remover."); 
        }
    }

    private void doDownload() {
        Document d = getSelected();
        if (d==null) { JOptionPane.showMessageDialog(this, "Selecione um documento."); return; }
        String origem = d.getArquivoPath();
        String destino = "data/downloads/" + new File(origem).getName();
        boolean ok = FileUtil.copy(origem, destino);
        if (ok) JOptionPane.showMessageDialog(this, "Arquivo copiado para: " + destino);
        else JOptionPane.showMessageDialog(this, "Falha no download. Verifique o caminho.");
    }
}
