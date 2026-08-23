package br.senac.ogham.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private static final Set<String> EXTENSOES = Set.of("pdf", "jpg", "jpeg");
    private final Path raiz;
    public FileStorageService(@Value("${ogham.storage.dir}") String dir) { this.raiz = Path.of(dir).toAbsolutePath().normalize(); }
    public String salvar(MultipartFile arquivo, String tipo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) throw new IllegalArgumentException("Arquivo é obrigatório.");
        String nome = Path.of(arquivo.getOriginalFilename() == null ? "arquivo" : arquivo.getOriginalFilename()).getFileName().toString();
        int ponto = nome.lastIndexOf('.');
        String ext = ponto >= 0 ? nome.substring(ponto + 1).toLowerCase() : "";
        if (!EXTENSOES.contains(ext)) throw new IllegalArgumentException("Somente arquivos PDF ou JPEG são permitidos.");
        if (("PDF".equals(tipo) && !ext.equals("pdf")) || ("JPEG".equals(tipo) && !(ext.equals("jpg") || ext.equals("jpeg"))) throw new IllegalArgumentException("O arquivo não corresponde ao tipo selecionado.");
        Files.createDirectories(raiz);
        String destino = System.currentTimeMillis() + "_" + nome.replaceAll("[^a-zA-Z0-9._-]", "_");
        Files.copy(arquivo.getInputStream(), raiz.resolve(destino), StandardCopyOption.REPLACE_EXISTING);
        return destino;
    }
    public Path caminho(String nome) { return raiz.resolve(Path.of(nome).getFileName()).normalize(); }
}
