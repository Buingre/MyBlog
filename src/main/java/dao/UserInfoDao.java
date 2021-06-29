package dao;

import models.UserInfo;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * userinfo的增删改查方法
 */
public class UserInfoDao {
    /**
     * 【增】--添加用户
     * @param userInfo 前端传过来一个userinfo对象
     * @return 如果返回的为true说明  操作成功
     */
    public boolean add(UserInfo userInfo) throws SQLException {
        boolean result = false;
        //1.非空校验
        if(userInfo.getUsername()!=null && userInfo.getPassword()!=null){
            //说明是正确的参数
            //2.数据库的经典操作
            Connection connection = DBUtils.getConnect();
            String sql = "insert into userinfo(username,password) values(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userInfo.getUsername());
            statement.setString(2,userInfo.getPassword());
            result = statement.executeUpdate()>=1?true:false;
            DBUtils.close(connection,statement,null);
        }

        return result;

    }

    /**
     * 【查】-->对应登录功能
     * @param userInfo
     * @return
     */
    public boolean isLogin(UserInfo userInfo) throws SQLException {
        boolean result = false;
        //1.校验参数
        if(userInfo.getUsername()!=null && userInfo.getPassword()!=null &&
            !userInfo.getUsername().equals("") && !userInfo.getPassword().equals("")){
            //说明是正确的参数
            //2.数据库的经典操作
            Connection connection = DBUtils.getConnect();
            String sql = "select * from userinfo where username=? and password=? and state=1 ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,userInfo.getUsername());
            statement.setString(2,userInfo.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = true;
            }
            DBUtils.close(connection,statement,resultSet);
        }
        return result;
    }


    /**
     * 获取userInfo对象
     * 主要是为了得到uid
     * @param userInfo
     * @return
     * @throws SQLException
     */
    public UserInfo getUserInfo(UserInfo userInfo) throws SQLException {
        UserInfo user = new UserInfo();
        //todo:如果出问题可能是这里的问题

        //非空校验
        if(userInfo.getUsername()!=null && userInfo.getPassword()!=null &&
                !userInfo.getUsername().equals("") && !userInfo.getPassword().equals("")) {
            Connection connection = DBUtils.getConnect();
            String sql = "select * from userinfo where username=? and password=? and state = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = null;
            if (userInfo.getUsername() != null && userInfo.getPassword() != null
                    && !userInfo.getUsername().equals("") && !userInfo.getPassword().equals("")) {

                statement.setString(1, userInfo.getUsername());
                statement.setString(2, userInfo.getPassword());
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    //在这里获取到的id，并设置id
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                }
            }
            // 关闭连接
            DBUtils.close(connection, statement, resultSet);
        }
        return user;
    }



}
