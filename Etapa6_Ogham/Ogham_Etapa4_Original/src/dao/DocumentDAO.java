package dao;

import db.DBConnection;
import model.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public List<Document> listarTodos() {
        List<Document> lista = new ArrayList<>();
        String sql = "SELECT * FROM documentos ORDER BY data DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Document d = map(rs);
                lista.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Document> pesquisar(String termo) {
        List<Document> lista = new ArrayList<>();
        String sql = "SELECT * FROM documentos WHERE titulo LIKE ? OR autor LIKE ? OR tags LIKE ? ORDER BY data DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + termo + "%";
            ps.setString(1, like); ps.setString(2, like); ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean inserir(Document d) {
        String sql = "INSERT INTO documentos (titulo, autor, descricao, tipo, data, arquivo_path, tags) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                    if (keys.next()) d.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM documentos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
