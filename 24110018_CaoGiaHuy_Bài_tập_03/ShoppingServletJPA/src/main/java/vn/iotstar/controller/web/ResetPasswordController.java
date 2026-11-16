package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import vn.iotstar.entity.User;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IOtpService.VerifyResult;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.OtpMessages;

/** Quên mật khẩu - bước 2: nhập OTP + mật khẩu mới. */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final IUserService userService = new UserServiceImpl();
    private final IOtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = pendingEmail(req);
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        show(req, resp, email);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = pendingEmail(req);
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        User user = userService.findByEmail(email);

        if ("resend".equals(req.getParameter("action"))) {
            if (user != null && user.isActive()) {
                String[] msg = OtpMessages.resend(otpService.issue(user, Constant.OTP_RESET));
                req.setAttribute(msg[0], msg[1]);
            } else {
                req.setAttribute("info", "Nếu email tồn tại, mã OTP mới đã được gửi.");
            }
            show(req, resp, email);
            return;
        }

        String otp = req.getParameter("otp");
        String password = req.getParameter("password");
        String repassword = req.getParameter("repassword");

        // Kiểm tra mật khẩu trước để nhập sai mật khẩu không làm tốn lượt thử OTP
        if (password == null || password.length() < Constant.PASSWORD_MIN_LENGTH) {
            req.setAttribute("alert", "Mật khẩu mới phải có ít nhất " + Constant.PASSWORD_MIN_LENGTH + " ký tự!");
            show(req, resp, email);
            return;
        }
        if (!password.equals(repassword)) {
            req.setAttribute("alert", "Mật khẩu nhập lại không khớp!");
            show(req, resp, email);
            return;
        }

        if (user == null || !user.isActive()) {
            req.setAttribute("alert", "Mã OTP không hợp lệ hoặc đã hết hạn!");
            show(req, resp, email);
            return;
        }

        VerifyResult r = otpService.verify(user, Constant.OTP_RESET, otp);
        if (r != VerifyResult.OK) {
            req.setAttribute("alert", OtpMessages.verifyError(r));
            show(req, resp, email);
            return;
        }

        userService.changePassword(user, password); // lưu bản băm của mật khẩu mới
        req.getSession().removeAttribute(Constant.SESSION_PENDING_RESET);
        resp.sendRedirect(req.getContextPath() + "/login?reset=1");
    }

    private String pendingEmail(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute(Constant.SESSION_PENDING_RESET);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp, String email) throws ServletException, IOException {
        req.setAttribute("email", email);
        req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
    }
}
