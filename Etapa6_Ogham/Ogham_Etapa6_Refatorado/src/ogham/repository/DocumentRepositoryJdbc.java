package ogham.repository;

import ogham.db.DBConnection;
import ogham.model.Document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de {@link DocumentRepository} com JDBC/MySQL.
 *
 * Responsabilidade única (SRP): esta classe cuida exclusivamente de traduzir
 * chamadas do repositório em SQL. Ela é o antigo `dao.DocumentDAO` da Etapa 4,
 * refatorado para: (1) implementar a interface DocumentRepository, permitindo
 * troca de implementação sem alterar quem a usa; (2) não engolir exceções —
 * erros de SQL viram RepositoryException, para que quem chamar decida como
 * tratar (mostrar mensagem, logar, tentar novamente etc.).
 */
public class DocumentRepositoryJdbc implements DocumentRepository {

    @Override
    public List<Document> listarTodos() {
        String sql = "SELECT * FROM documentos ORDER BY data DESC";
        List<Document> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(map(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao listar documentos", e);
        }
    }

    @Override
    public List<Document> pesquisar(String termo) {
        String sql = "SELECT * FROM documentos WHERE titulo LIKE ? OR autor LIKE ? OR tags LIKE ? ORDER BY data DESC";
        List<Document> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + termo + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(map(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao pesquisar documentos com termo '" + termo + "'", e);
        }
    }

    @Override
    public Optional<Document> buscarPorId(int id) {
        String sql = "SELECT * FROM documentos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao buscar documento id=" + id, e);
        }
    }

    @Override
    public Document inserir(Document d) {
        String sql = "INSERT INTO documentos (titulo, autor, descricao, tipo, data, arquivo_path, tags) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getTitulo());
            ps.setString(2, d.getAutor());
            ps.setString(3, d.getDescricao());
            ps.setString(4, d.getTipo());
            ps.setString(5, d.getData());
            ps.setString(6, d.getArquivoPath());
            ps.setString(7, d.getTags());
            int affected = ps.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        d.setId(keys.getInt(1));
                    }
                }
            }
            return d;
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao inserir documento '" + d.getTitulo() + "'", e);
        }
    }

    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM documentos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RepositoryException("Falha ao excluir documento id=" + id, e);
        }
    }

    private Document map(ResultSet rs) throws SQLException {
        Document d = new Document();
        d.setId(rs.getInt("id"));
        d.setTitulo(rs.getString("titulo"));
        d.setAutor(rs.getString("autor"));
        d.setDescricao(rs.getString("descricao"));
        d.setTipo(rs.getString("tipo"));
        d.setData(rs.getString("data"));
        d.setArquivoPath(rs.getString("arquivo_path"));
        d.setTags(rs.getString("tags"));
        return d;
    }
}
