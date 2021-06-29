package utils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用数据库操作类
 * 1.对外提供Connection对象
 * 2.提供统一的关闭方法
 */
public class DBUtils {
    private static MysqlDataSource dataSource = null;

    /**
     * 对外提供统一的 Connection 对象
     */
    public static Connection getConnect() throws SQLException {
        if(dataSource==null){//说明首次调用，先初始化
            dataSource = new MysqlDataSource();
            // 1.设置连接的服务器地址
            dataSource.setURL("jdbc:mysql://127.0.0.1:3306/myblog?characterEncoding=utf-8&useSSL=true");
            dataSource.setUser("root");
            dataSource.setPassword("233233");
        }
        return dataSource.getConnection();
    }
    /**
     * 对外提供统一的关闭方法
     */
    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) throws SQLException {
        if(resultSet!=null) resultSet.close();
        if(statement!=null) statement.close();
        if(connection!=null) connection.close();

    }

}
