package br.senac.ogham.web;

import br.senac.ogham.model.Document;
import br.senac.ogham.service.DocumentService;
import br.senac.ogham.service.FileStorageService;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documentos")
public class DocumentController {
    private final DocumentService service;
    private final FileStorageService storage;
    public DocumentController(DocumentService service, FileStorageService storage) { this.service = service; this.storage = storage; }

    @GetMapping public List<Document> listar(@RequestParam(required=false) String busca) { return service.pesquisar(busca); }
    @GetMapping("/{id}") public ResponseEntity<Document> buscar(@PathVariable int id) { return service.buscarPorId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()); }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> inserir(@RequestParam String titulo, @RequestParam(required=false) String autor,
        @RequestParam(required=false) String descricao, @RequestParam String tipo, @RequestParam(required=false) String data,
        @RequestParam(required=false) String tags, @RequestParam MultipartFile arquivo, jakarta.servlet.http.HttpSession session) throws IOException {
        if (session.getAttribute("ADMIN") == null) return ResponseEntity.status(401).body("Login de administrador necessário.");
        String nome = storage.salvar(arquivo, tipo);
        Document salvo = service.inserir(new Document(null, titulo, autor, descricao, tipo, data, nome, tags));
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/{id}/download") public ResponseEntity<?> download(@PathVariable int id) throws IOException {
        var doc = service.buscarPorId(id);
        if (doc.isEmpty()) return ResponseEntity.notFound().build();
        var path = storage.caminho(doc.get().arquivoPath());
        if (!Files.exists(path)) return ResponseEntity.notFound().build();
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        String mime = "PDF".equals(doc.get().tipo()) ? "application/pdf" : "image/jpeg";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mime)).header(HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment().filename(doc.get().arquivoPath()).build().toString()).body(resource);
    }
}
