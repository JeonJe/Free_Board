package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBUtils {
    static ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
    private static final String DB_URL = resourceBundle.getString("DB_URL");
    private static final String USER = resourceBundle.getString("DB_ID");
    private static final String PASS = resourceBundle.getString("DB_PASSWORD");

    /**
     * DB 객체 생성 후 반환
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * DB객체와 연결 종료
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
