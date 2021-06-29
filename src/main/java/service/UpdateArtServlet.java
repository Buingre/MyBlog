package service;

import dao.ArticleInfoDao;
import models.UserInfo;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class UpdateArtServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int succ =-1;
        String msg = "";
        //1.获得前端参数
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int id = Integer.parseInt(request.getParameter("id"));
        //非空校验
        if(title!=null && content !=null && !title.equals("") && !content.equals("")
                && id > 0 ){
            //2-2获取uid----->从session中
            HttpSession session = request.getSession(false);
            if(session!=null && session.getAttribute("userinfo")!=null){//说明是登录状态
                UserInfo userInfo = (UserInfo) session.getAttribute("userinfo");
                //2.进行业务处理【去数据库执行修改操作】
                ArticleInfoDao articleInfoDao = new ArticleInfoDao();
                try {
                    succ = articleInfoDao.updateArt(title,content,userInfo.getId(),id);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else {//非登录状态
                msg = "非法操作，非登录状态，请先登录！";
            }
        }else {
            msg = "非法操作，参数传递不全！";
        }
        //3.向前端返回数据
        HashMap<String, Object> result = new HashMap<>();
        result.put("succ", succ);
        result.put("msg", msg);
        ResultJSONUtils.writeMap(response, result);
    }
}
