package br.senac.ogham.web;

import br.senac.ogham.service.AuthService;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }
    @PostMapping("/login") public ResponseEntity<?> login(@RequestBody Map<String,String> body, HttpSession session) {
        if (!auth.autenticar(body.get("usuario"), body.get("senha"))) return ResponseEntity.status(401).body(Map.of("erro","Usuário ou senha inválidos."));
        session.setAttribute("ADMIN", body.get("usuario"));
        return ResponseEntity.ok(Map.of("autenticado", true, "usuario", body.get("usuario")));
    }
    @PostMapping("/logout") public ResponseEntity<?> logout(HttpSession session) { session.invalidate(); return ResponseEntity.ok(Map.of("autenticado", false)); }
    @GetMapping("/me") public ResponseEntity<?> me(HttpSession session) { return session.getAttribute("ADMIN") == null ? ResponseEntity.status(401).body(Map.of("autenticado",false)) : ResponseEntity.ok(Map.of("autenticado",true,"usuario",session.getAttribute("ADMIN"))); }
}
