package ogham.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsabilidade única (SRP): abrir conexões JDBC. Não sabe mais os valores
 * de host/usuário/senha — isso é responsabilidade de {@link DBConfig}.
 */
public class DBConnection {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.url(), DBConfig.user(), DBConfig.password());
    }
}
