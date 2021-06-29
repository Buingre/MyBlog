package service;

import dao.ArticleInfoDao;
import models.ArticleInfo;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class InitUpdateArtServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       int state = -1;
       String msg = "";
        ArticleInfo articleInfo = null;//要返回给前端，所以定义在最外层
        //1.获取前端信息
        int id = Integer.parseInt(request.getParameter("id"));
        if(id > 0){
            //2.进行业务处理【进行数据库查询】
            ArticleInfoDao articleInfoDao = new ArticleInfoDao();
            //调用查询方法,返回一个ArticleInfo给前端
            try {
                articleInfo = articleInfoDao.getArtById(id);
                state = 1;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else {
            msg = "参数有误！";
        }
        //3.返回给前端信息
        HashMap<String,Object> result = new HashMap<>();
        result.put("state",state);
        result.put("msg",msg);
        result.put("art",articleInfo);
        ResultJSONUtils.writeMap(response,result);
    }
}
