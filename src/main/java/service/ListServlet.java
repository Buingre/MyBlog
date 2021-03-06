package service;

import dao.ArticleInfoDao;
import models.vo.ArticleInfoVO;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int succ = -1;
        String msg = "";
        List<ArticleInfoVO> list = null;
        // 1.从前端获取参数（如果没有参数，忽略此步骤）
        int cpage = Integer.parseInt(req.getParameter("cpage"));// 当前页码
        int psize = Integer.parseInt(req.getParameter("psize"));// 每页显示条数
        // 2.操作数据，执行相应的业务逻辑
        ArticleInfoDao dao = new ArticleInfoDao();
        try {
            list = dao.getListByPage(cpage,psize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        succ = 1;
        //3.返回结果给前端
        HashMap<String, Object> result = new HashMap<>();
        result.put("succ", succ);
        result.put("msg", msg);
        result.put("list", list);
        ResultJSONUtils.writeMap(resp, result);
    }
}
