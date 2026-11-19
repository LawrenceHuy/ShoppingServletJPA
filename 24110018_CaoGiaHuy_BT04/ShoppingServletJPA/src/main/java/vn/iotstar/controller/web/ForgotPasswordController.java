package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import vn.iotstar.entity.User;
import vn.iotstar.form.ForgotPasswordForm;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.ValidationUtil;

/** Quên mật khẩu - bước 1: nhập email, hệ thống gửi OTP. */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final IUserService userService = new UserServiceImpl();
    private final IOtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ForgotPasswordForm form = ForgotPasswordForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form);
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }
        String email = form.getEmail();

        User user = userService.findByEmail(email);
        if (user != null && user.isActive()) {
            otpService.issue(user, Constant.OTP_RESET);
        }
        // Luôn chuyển sang bước 2 dù email có tồn tại hay không (tránh lộ email nào đã đăng ký)
        req.getSession(true).setAttribute(Constant.SESSION_PENDING_RESET, email);
        resp.sendRedirect(req.getContextPath() + "/reset-password");
    }
}
