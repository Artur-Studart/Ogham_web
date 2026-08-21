package ogham.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Carrega a configuração de conexão a partir de {@code resources/db.properties}.
 *
 * Refatoração de code smell (Hardcoded Credentials): na Etapa 4, host,
 * usuário e senha estavam escritos diretamente em DBConnection.java. Isso
 * obriga a recompilar o código para trocar de ambiente (ex.: dev → produção)
 * e expõe a senha no controle de versão. Agora os valores ficam em um arquivo
 * de propriedades externo ao código-fonte, com valores padrão apenas como
 * fallback para não quebrar o ambiente de sala de aula.
 */
public final class DBConfig {

    private static final Properties PROPS = new Properties();

    static {
        PROPS.setProperty("db.url", "jdbc:mysql://localhost:3306/oghamdb?useSSL=false&serverTimezone=UTC");
        PROPS.setProperty("db.user", "root");
        PROPS.setProperty("db.password", "1888");

        try (InputStream in = DBConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            System.err.println("Não foi possível ler db.properties, usando valores padrão: " + e.getMessage());
        }
    }

    private DBConfig() {
    }

    public static String url() { return PROPS.getProperty("db.url"); }
    public static String user() { return PROPS.getProperty("db.user"); }
    public static String password() { return PROPS.getProperty("db.password"); }
}
