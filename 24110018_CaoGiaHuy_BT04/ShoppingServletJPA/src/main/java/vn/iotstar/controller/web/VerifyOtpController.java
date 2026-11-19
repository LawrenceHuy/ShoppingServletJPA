package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import vn.iotstar.entity.User;
import vn.iotstar.form.OtpForm;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IOtpService.IssueResult;
import vn.iotstar.service.IOtpService.VerifyResult;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.OtpMessages;
import vn.iotstar.util.ValidationUtil;

/** Bước kích hoạt tài khoản: nhập OTP đã gửi qua email khi đăng ký. */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/verify-otp")
public class VerifyOtpController extends HttpServlet {
    private final IUserService userService = new UserServiceImpl();
    private final IOtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = pendingEmail(req);
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if ("1".equals(req.getParameter("mailerror"))) {
            req.setAttribute("alert", "Không gửi được email, hãy bấm \"Gửi lại mã\" sau ít phút.");
        }
        show(req, resp, email);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = pendingEmail(req);
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = userService.findByEmail(email);
        if (user == null || user.isActive()) { // không còn gì để kích hoạt
            req.getSession().removeAttribute(Constant.SESSION_PENDING_ACTIVATE);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("resend".equals(req.getParameter("action"))) {
            IssueResult r = otpService.issue(user, Constant.OTP_ACTIVATE);
            String[] msg = OtpMessages.resend(r);
            req.setAttribute(msg[0], msg[1]);
            show(req, resp, email);
            return;
        }

        OtpForm form = OtpForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form);
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            show(req, resp, email);
            return;
        }

        VerifyResult r = otpService.verify(user, Constant.OTP_ACTIVATE, form.getOtp());
        if (r != VerifyResult.OK) {
            req.setAttribute("alert", OtpMessages.verifyError(r));
            show(req, resp, email);
            return;
        }

        user.setActive(true);
        userService.update(user);
        req.getSession().removeAttribute(Constant.SESSION_PENDING_ACTIVATE);
        resp.sendRedirect(req.getContextPath() + "/login?activated=1");
    }

    private String pendingEmail(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute(Constant.SESSION_PENDING_ACTIVATE);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp, String email) throws ServletException, IOException {
        req.setAttribute("email", email);
        req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
    }
}
