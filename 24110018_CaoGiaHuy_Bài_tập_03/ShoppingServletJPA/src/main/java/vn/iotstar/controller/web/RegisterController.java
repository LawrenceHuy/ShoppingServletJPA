package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Locale;

import vn.iotstar.entity.User;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IOtpService.IssueResult;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    private final IUserService service = new UserServiceImpl();
    private final IOtpService otpService = new OtpServiceImpl();

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
        String email = trim(req.getParameter("email")).toLowerCase(Locale.ROOT);
        String fullname = trim(req.getParameter("fullname"));
        String phone = trim(req.getParameter("phone"));

        String alertMsg = null;
        if (username.isEmpty() || password == null || password.isEmpty() || email.isEmpty()) {
            alertMsg = "Vui lòng nhập đầy đủ tài khoản, email và mật khẩu!";
        } else if (password.length() < Constant.PASSWORD_MIN_LENGTH) {
            alertMsg = "Mật khẩu phải có ít nhất " + Constant.PASSWORD_MIN_LENGTH + " ký tự!";
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

        User user = service.register(username, password, email, fullname, phone);
        if (user == null) {
            req.setAttribute("alert", "System error!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // Tài khoản chưa kích hoạt: gửi OTP qua email rồi chuyển sang trang nhập mã
        req.getSession(true).setAttribute(Constant.SESSION_PENDING_ACTIVATE, user.getEmail());
        IssueResult r = otpService.issue(user, Constant.OTP_ACTIVATE);
        resp.sendRedirect(req.getContextPath() + "/verify-otp" + (r == IssueResult.MAIL_ERROR ? "?mailerror=1" : ""));
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
