package dao;

import models.ArticleInfo;
import models.vo.ArticleInfoVO;
import utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对 articleinfo 表的数据操作（增删改查用户表）
 */
public class ArticleInfoDao {
    /**
     * 用来查询个人的文章列表
     */
    public List<ArticleInfo> getMyArtList(int uid) throws SQLException {
        List<ArticleInfo> list = new ArrayList<>();
        Connection connection = DBUtils.getConnect();
        String sql = "select * from articleinfo where uid=?";
        PreparedStatement statement =connection.prepareStatement(sql);
        statement.setInt(1,uid);
        ResultSet resultSet = statement.executeQuery();
        //可能会返回多条记录，因此while
        while (resultSet.next()){
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setId(resultSet.getInt("id"));
            articleInfo.setTitle(resultSet.getString("title"));
            articleInfo.setContent(resultSet.getString("content"));
            articleInfo.setCreatetime(resultSet.getDate("createtime"));
            articleInfo.setUpdatetime(resultSet.getDate("updatetime"));
            articleInfo.setRcount(resultSet.getInt("rcount"));
            list.add(articleInfo);
        }
        DBUtils.close(connection,statement,resultSet);
        return list;
    }

    /**
     * 删除文章的数据库操作
     */
    public int delArtileById(int id) throws SQLException {
        int result = 0;
        //数据库经典操作
        Connection connection = DBUtils.getConnect();
        String sql = "delete from articleinfo where id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        //真正的操作现在开始
        result = statement.executeUpdate();//会返回影响的行数
        //关闭数据库连接
        DBUtils.close(connection,statement,null);

        return result;
    }

    /**
     * 添加文章的数据库操作--要知道uid
     */
    public  int addArt(String title,String content,int uid) throws SQLException {
        int result = 0;
        //数据库经典操作
        Connection connection = DBUtils.getConnect();
        String sql = "insert into articleinfo(title,content,uid) values(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, content);
        statement.setInt(3, uid);
        //真正的操作现在开始
        result  = statement.executeUpdate();
        //关闭连接
        DBUtils.close(connection,statement,null);

        return result;
    }

    /**
     * 文章修改。要有文章的id!!
     */
    public int updateArt(String title, String content, int uid,int id) throws SQLException {
        int result = 0;
        //JDBC经典4步
        Connection connection = DBUtils.getConnect();
        String sql = "update articleinfo set title=?,content=? where id=? and uid=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,title);
        statement.setString(2,content);
        statement.setInt(3,id);
        statement.setInt(4,uid);
        //真正的操作就要开始
        result = statement.executeUpdate();
        //关闭连接
        DBUtils.close(connection,statement,null);

        return result;
    }


    /**
     * 文章查询方法：在文章修改之前先展示本文章的内容
     * 写显示文章详情时  修改了一下本方法
     */
    public ArticleInfoVO getArtById(int id) throws SQLException {
        ArticleInfoVO articleInfo =new ArticleInfoVO();
        //JDBC经典4步
        Connection connection = DBUtils.getConnect();
        String sql = "select a.*,u.username from articleinfo a left join userinfo u on a.uid=u.id where a.id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        //真正的操作就要开始
        ResultSet resultSet  = statement.executeQuery();
        if(resultSet.next()){
            articleInfo.setTitle(resultSet.getString("title"));
            articleInfo.setContent(resultSet.getString("content"));
            articleInfo.setCreatetime(resultSet.getDate("createtime"));
            articleInfo.setRcount(resultSet.getInt("rcount"));
            articleInfo.setUsername(resultSet.getString("username"));
        }
        //关闭连接
        DBUtils.close(connection,statement,resultSet);
        return articleInfo;
    }

    /**
     * 获取所有人的文章列表
     */
    public List<ArticleInfoVO> getList() throws SQLException {
        List<ArticleInfoVO> list = new ArrayList<>();
        Connection connection = DBUtils.getConnect();
        String sql = "select a.*,u.username from articleinfo a left join userinfo u on a.uid=u.id";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        //多行数据用 while
        while (resultSet.next()){
            ArticleInfoVO vo = new ArticleInfoVO();
            vo.setId(resultSet.getInt("id"));
            vo.setUsername(resultSet.getString("username"));
            vo.setTitle(resultSet.getString("title"));
            vo.setRcount(resultSet.getInt("rcount"));
            vo.setCreatetime(resultSet.getDate("createtime"));
            list.add(vo);
        }
        DBUtils.close(connection, statement, resultSet);
        return list;
    }

    /**
     *根据分页查询文章列表
     */
    public List<ArticleInfoVO> getListByPage(int cpage, int psize) throws SQLException {
        List<ArticleInfoVO> list = new ArrayList<>();
        Connection connection = DBUtils.getConnect();
        String sql = "select a.*,u.username from articleinfo a left join userinfo u on a.uid=u.id limit ?,?";
        PreparedStatement statement = connection.prepareStatement(sql);
        //todo:自己推倒的公式
        statement.setInt(1, (cpage - 1) * psize);
        statement.setInt(2, psize);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ArticleInfoVO vo = new ArticleInfoVO();
            vo.setId(resultSet.getInt("id"));
            vo.setUsername(resultSet.getString("username"));
            vo.setTitle(resultSet.getString("title"));
            vo.setRcount(resultSet.getInt("rcount"));
            vo.setCreatetime(resultSet.getDate("createtime"));
            list.add(vo);
        }
        DBUtils.close(connection, statement, resultSet);
        return list;
    }

    /**
     * 修改阅读量
     */
    public int upcount(int id) throws SQLException {
        int result = 0;
        Connection connection = DBUtils.getConnect();
        String sql = "update articleinfo set rcount=rcount+1 where id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        result = statement.executeUpdate();
        DBUtils.close(connection, statement, null);
        return  result;
    }
}
