package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserInfoDao;
import models.UserInfo;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 注册和登录
 */
public class RegLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setCharacterEncoding("utf-8");
        //response.setContentType("application/json");
        int state = -1;//200表示接口执行成功
        String msg = "";//返回给前端的失败的具体信息
        //PrintWriter writer = response.getWriter();

        /**
         * 注册
         * */
        String flag = request.getParameter("flag");
        if(flag==null || flag.equals("")){
            msg = "flag参数出错";
        }
        if(flag.equals("1")){//-------------注册
            //1.接受前端参数
            String username = request.getParameter("username");
            String password1 = request.getParameter("password1");
            //1-1参数判断--》非空校验

            if(username==null || password1==null ||username=="" || password1==""){
                msg = "参数不正确";
            }else {
                //2.进行业务处理-->操作数据库进行插入操作
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(username);
                userInfo.setPassword(password1);
                UserInfoDao userInfoDao = new UserInfoDao();
                try {
                    boolean result = userInfoDao.add(userInfo);
                    if(result){
                        //数据库操作成功
                        state = 200;
                    }else {
                        //数据库操作失败
                        state = 100;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }else {//------------------登录
            //1.接受前端参数
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            //1-1非空校验
            if(name==null || password==null ||name=="" || password==""){
                msg = "操作失败，参数不正确";
            }else {
                //2.业务处理--》查询数据库
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(name);
                userInfo.setPassword(password);
                UserInfoDao userInfoDao = new UserInfoDao();
                boolean result = false;
                try {
                    //result = userInfoDao.isLogin(userInfo);
                    userInfo = userInfoDao.getUserInfo(userInfo);
                    if(userInfo.getId()>=1){//表示登录成功
                        state = 200;
                        //如果登录成功，创建session信息
                        HttpSession session = request.getSession();
                        session.setAttribute("userinfo",userInfo);//将当前用户存储在session
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        }
//        3.向前端返回信息
//        {"state":100,"msg":msg}
        //writer.println("{\"state\":"+state+",\"msg\":\""+msg+"\"}");
        HashMap<String,Object> result = new HashMap<>();
        result.put("state",state);
        result.put("msg",msg);
        ObjectMapper mapper = new ObjectMapper();
        //调用统一的输出方法进行输出
        ResultJSONUtils.write(response,mapper.writeValueAsString(result));
        //或者  ResultJSONUtils.writeMap(response,result);



    }
}
