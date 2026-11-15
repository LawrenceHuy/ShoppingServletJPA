package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    private final IUserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String repassword = req.getParameter("repassword");
        String email = trim(req.getParameter("email"));
        String fullname = trim(req.getParameter("fullname"));
        String phone = trim(req.getParameter("phone"));

        String alertMsg = null;
        if (username.isEmpty() || password == null || password.isEmpty() || email.isEmpty()) {
            alertMsg = "Vui lòng nhập đầy đủ tài khoản, email và mật khẩu!";
        } else if (!password.equals(repassword)) {
            alertMsg = "Mật khẩu nhập lại không khớp!";
        } else if (service.checkExistEmail(email)) {
            alertMsg = "Email đã tồn tại!";
        } else if (service.checkExistUsername(username)) {
            alertMsg = "Tài khoản đã tồn tại!";
        } else if (!phone.isEmpty() && service.checkExistPhone(phone)) {
            alertMsg = "Số điện thoại đã tồn tại!";
        }

        if (alertMsg != null) {
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (service.register(username, password, email, fullname, phone)) {
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } else {
            req.setAttribute("alert", "System error!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
