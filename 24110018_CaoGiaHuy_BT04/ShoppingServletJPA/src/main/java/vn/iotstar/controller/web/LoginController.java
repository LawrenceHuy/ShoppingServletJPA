package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import java.util.Map;

import vn.iotstar.entity.User;
import vn.iotstar.form.LoginForm;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IOtpService.IssueResult;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.ValidationUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private final IUserService service = new UserServiceImpl();
    private final IOtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1) Đã đăng nhập bằng Session
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }
        // 2) Đăng nhập bằng Cookie ("Nhớ tôi")
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
                    User user = service.get(cookie.getValue());
                    if (user != null && user.isActive()) {
                        req.getSession(true).setAttribute(Constant.SESSION_ACCOUNT, user);
                        resp.sendRedirect(req.getContextPath() + "/waiting");
                        return;
                    }
                }
            }
        }
        req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isRememberMe = "on".equals(req.getParameter("remember"));

        LoginForm form = LoginForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form);
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }
        String username = form.getUsername();
        String password = form.getPassword();

        // login() so khớp mật khẩu nhập vào với bản băm trong DB
        User user = service.login(username.trim(), password);
        if (user == null) {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng");
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }

        if (!user.isActive()) {
            // Đúng mật khẩu nhưng chưa kích hoạt: gửi OTP mới và chuyển sang trang nhập mã
            req.getSession(true).setAttribute(Constant.SESSION_PENDING_ACTIVATE, user.getEmail());
            IssueResult r = otpService.issue(user, Constant.OTP_ACTIVATE);
            resp.sendRedirect(req.getContextPath() + "/verify-otp" + (r == IssueResult.MAIL_ERROR ? "?mailerror=1" : ""));
            return;
        }

        HttpSession session = req.getSession(true);
        req.changeSessionId(); // chống session fixation
        session.setAttribute(Constant.SESSION_ACCOUNT, user);
        if (isRememberMe) {
            saveRememberMe(resp, user.getUserName());
        }
        resp.sendRedirect(req.getContextPath() + "/waiting");
    }

    private void saveRememberMe(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
        cookie.setMaxAge(30 * 60); // 30 phút
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
