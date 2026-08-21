package ogham.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Implementação de {@link FileStorageService} usando o sistema de arquivos local.
 *
 * Responsabilidade única (SRP): apenas copia arquivos em disco. É o antigo
 * `util.FileUtil`, agora atrás de uma interface e sem imprimir stack trace
 * diretamente no console (o chamador decide o que fazer com uma cópia sem
 * sucesso, que aqui é representada por Optional.empty() em vez de um boolean
 * “mágico”).
 */
public class LocalFileStorageService implements FileStorageService {

    private final String pastaDownloads;

    public LocalFileStorageService(String pastaDownloads) {
        this.pastaDownloads = pastaDownloads;
    }

    @Override
    public Optional<String> copiarParaDownload(String caminhoOrigem) {
        try {
            Path origem = Path.of(caminhoOrigem);
            if (!Files.exists(origem)) {
                return Optional.empty();
            }
            Path destino = Path.of(pastaDownloads, origem.getFileName().toString());
            Files.createDirectories(destino.getParent());
            Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
            return Optional.of(destino.toString());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
