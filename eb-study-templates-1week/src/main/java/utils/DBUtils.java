package utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtils {
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
