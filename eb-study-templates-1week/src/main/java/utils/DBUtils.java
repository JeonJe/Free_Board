package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    private static final String USER = "ebsoft";
    private static final String PASS = "ebsoft";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
    public static void closeConnection(Connection conn){
        if (conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
