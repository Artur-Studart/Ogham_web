package ogham.ui;

import ogham.model.Document;
import ogham.repository.RepositoryException;
import ogham.service.DocumentService;
import ogham.service.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Formulário de inserção (Swing).
 *
 * Responsabilidade única (SRP): coletar os dados digitados e delegar
 * validação + persistência ao {@link DocumentService}. Na Etapa 4 a
 * verificação "título obrigatório" estava escrita aqui dentro; agora essa
 * regra vive em DocumentValidator (via DocumentService) e pode mostrar
 * múltiplos erros de uma vez, não apenas o primeiro problema encontrado.
 */
public class InsertDialog extends JDialog {

    private final DocumentService service;
    private JTextField tfTitle, tfAuthor, tfDate, tfPath, tfTags;
    private JComboBox<String> cbTipo;

    public InsertDialog(Frame owner, DocumentService service) {
        super(owner, "Inserir Documento", true);
        this.service = service;
        setSize(600, 300);
        setLocationRelativeTo(owner);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Título*"), c);
        c.gridx = 1; tfTitle = new JTextField(30); p.add(tfTitle, c);

        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Autor"), c);
        c.gridx = 1; tfAuthor = new JTextField(30); p.add(tfAuthor, c);

        c.gridx = 0; c.gridy = 2; p.add(new JLabel("Tipo*"), c);
        c.gridx = 1; cbTipo = new JComboBox<>(new String[]{"PDF", "JPEG"}); p.add(cbTipo, c);

        c.gridx = 0; c.gridy = 3; p.add(new JLabel("Data (yyyy-MM-dd)"), c);
        c.gridx = 1; tfDate = new JTextField(12); p.add(tfDate, c);

        c.gridx = 0; c.gridy = 4; p.add(new JLabel("Caminho do arquivo*"), c);
        c.gridx = 1; tfPath = new JTextField(30); p.add(tfPath, c);
        JButton btnBrowse = new JButton("Procurar");
        btnBrowse.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(new File("."));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                tfPath.setText(fc.getSelectedFile().getPath());
            }
        });
        c.gridx = 2; p.add(btnBrowse, c);

        c.gridx = 0; c.gridy = 5; p.add(new JLabel("Tags"), c);
        c.gridx = 1; tfTags = new JTextField(30); p.add(tfTags, c);

        c.gridx = 1; c.gridy = 6;
        JButton btnSave = new JButton("Salvar");
        btnSave.addActionListener(e -> doSave());
        p.add(btnSave, c);

        add(p);
    }

    private void doSave() {
        Document d = new Document();
        d.setTitulo(tfTitle.getText().trim());
        d.setAutor(tfAuthor.getText().trim());
        d.setTipo((String) cbTipo.getSelectedItem());
        d.setData(tfDate.getText().trim());
        d.setArquivoPath(tfPath.getText().trim());
        d.setTags(tfTags.getText().trim());

        try {
            service.inserir(d);
            JOptionPane.showMessageDialog(this, "Inserido com sucesso");
            setVisible(false);
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this, String.join("\n", e.getErros()),
                    "Dados inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (RepositoryException e) {
            JOptionPane.showMessageDialog(this, "Falha ao inserir: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
