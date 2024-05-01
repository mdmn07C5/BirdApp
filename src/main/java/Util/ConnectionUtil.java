package Util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

public class ConnectionUtil {
    private static String url = "jdbc:h2:file:./target/foobar";
    private static String username = "sa";

    private static JdbcDataSource connectionPool = new JdbcDataSource();

    static {
        connectionPool.setURL(url);
        connectionPool.setUser(username);
    }

    /**
     * @return an active connection to the DB
     */
    public static Connection getConnection() {
        try {
            return connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void resetTestDatabase() {
        try {
			FileReader sqlReader = new FileReader("src/main/resources/schema.sql");
			RunScript.execute(getConnection(), sqlReader);
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
    }
}
