package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBUtils {

    /**
     * Read DB properties file
     */
    static ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
    /**
     * Read DB URL
     */
    private static final String DB_URL = resourceBundle.getString("DB_URL");
    /**
     * Read DB UserName
     */
    private static final String USER = resourceBundle.getString("DB_ID");
    /**
     * Read DB Password
     */
    private static final String PASS = resourceBundle.getString("DB_PASSWORD");

    /**
     * Create DB instance and Return connection Instance
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Close DB Connection
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
