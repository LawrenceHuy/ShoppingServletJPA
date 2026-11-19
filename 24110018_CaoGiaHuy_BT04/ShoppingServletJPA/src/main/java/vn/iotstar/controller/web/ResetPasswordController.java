package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import vn.iotstar.entity.User;
import vn.iotstar.form.ResetPasswordForm;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IOtpService.VerifyResult;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.OtpMessages;
import vn.iotstar.util.ValidationUtil;

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

        ResetPasswordForm form = ResetPasswordForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form); // lỗi định dạng không làm tốn lượt thử OTP
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            show(req, resp, email);
            return;
        }
        String otp = form.getOtp();
        String password = form.getPassword();

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
