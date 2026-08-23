package br.senac.ogham.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JdbcTemplate jdbc;
    public AuthService(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public boolean autenticar(String usuario, String senha) {
        if (usuario == null || senha == null || usuario.isBlank() || senha.isBlank()) return false;
        String hash = sha256(senha);
        return !jdbc.query("SELECT usuario FROM administradores WHERE usuario=? AND senha_hash=?", (ResultSet rs, int row) -> rs.getString(1), usuario, hash).isEmpty();
    }
    private String sha256(String valor) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(valor.getBytes(StandardCharsets.UTF_8));
            StringBuilder out = new StringBuilder();
            for (byte b : bytes) out.append(String.format("%02x", b));
            return out.toString();
        } catch (Exception e) { throw new IllegalStateException("Não foi possível calcular a credencial.", e); }
    }
}
