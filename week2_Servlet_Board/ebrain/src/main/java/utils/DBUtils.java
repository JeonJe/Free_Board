package utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
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
     * My batis SQL session
     */
    private static SqlSessionFactory sqlSessionFactory = null;

    /**
     * Read Mybatis-config
     */
    public static final String MYBATIS_CONFIG = "mybatis-config.xml";


    /**
     * open session using by mybatis
     * @return
     */
    public static SqlSession openSession() {
        if (sqlSessionFactory == null) {
            try {
                InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sqlSessionFactory.openSession();
    }

    /**
     * close session using by mybatis
     * @param session
     */
    public static void sessionClose(SqlSession session){
        if(session != null){
            try{
                session.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
