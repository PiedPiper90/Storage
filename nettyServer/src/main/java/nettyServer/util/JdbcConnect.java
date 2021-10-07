package nettyServer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnect {
    private static final String login = "root";
    private static final String password = "root";
    private static final String url = "jdbc:mysql://localhost:3306/db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url,login,password);
    }
}
