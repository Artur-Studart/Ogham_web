package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static boolean copy(String origem, String destino) {
        try {
            Path src = Path.of(origem);
            if (!Files.exists(src)) return false;
            Path dst = Path.of(destino);
            Files.createDirectories(dst.getParent());
            Files.copy(src, dst, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
