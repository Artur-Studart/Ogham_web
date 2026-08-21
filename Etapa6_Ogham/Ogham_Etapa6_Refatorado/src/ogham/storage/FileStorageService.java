package ogham.storage;

/**
 * Contrato para operações de arquivo (download/cópia de documentos digitalizados).
 *
 * Separar isso em uma interface (ISP/DIP) permite que o futuro projeto web
 * substitua a implementação local em disco por, por exemplo, um serviço de
 * armazenamento em nuvem, sem alterar DocumentService.
 */
public interface FileStorageService {

    /**
     * Copia o arquivo de origem para a pasta de downloads.
     *
     * @param caminhoOrigem caminho do arquivo original
     * @return caminho de destino, se a cópia foi bem-sucedida
     */
    java.util.Optional<String> copiarParaDownload(String caminhoOrigem);
}
