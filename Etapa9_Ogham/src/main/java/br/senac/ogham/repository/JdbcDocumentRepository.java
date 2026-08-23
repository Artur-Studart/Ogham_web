package br.senac.ogham.repository;

import br.senac.ogham.model.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcDocumentRepository implements DocumentRepository {
    private final JdbcTemplate jdbc;
    public JdbcDocumentRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private Document map(ResultSet rs, int row) throws SQLException {
        return new Document(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
            rs.getString("descricao"), rs.getString("tipo"), rs.getString("data"),
            rs.getString("arquivo_path"), rs.getString("tags"));
    }
    private static final String SELECT = "SELECT id,titulo,autor,descricao,tipo,data,arquivo_path,tags FROM documentos";

    @Override public List<Document> listarTodos() {
        return jdbc.query(SELECT + " ORDER BY data DESC, id DESC", this::map);
    }
    @Override public List<Document> pesquisar(String termo) {
        String like = "%" + termo.trim() + "%";
        return jdbc.query(SELECT + " WHERE titulo LIKE ? OR autor LIKE ? OR tags LIKE ? ORDER BY data DESC, id DESC",
            this::map, like, like, like);
    }
    @Override public Optional<Document> buscarPorId(int id) {
        return jdbc.query(SELECT + " WHERE id = ?", this::map, id).stream().findFirst();
    }
    @Override public Document inserir(Document d) {
        KeyHolder key = new GeneratedKeyHolder();
        jdbc.update(c -> {
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO documentos (titulo,autor,descricao,tipo,data,arquivo_path,tags) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,d.titulo()); ps.setString(2,d.autor()); ps.setString(3,d.descricao());
            ps.setString(4,d.tipo()); ps.setString(5,d.data()); ps.setString(6,d.arquivoPath()); ps.setString(7,d.tags());
            return ps;
        }, key);
        return new Document(key.getKey().intValue(), d.titulo(), d.autor(), d.descricao(), d.tipo(), d.data(), d.arquivoPath(), d.tags());
    }
}
