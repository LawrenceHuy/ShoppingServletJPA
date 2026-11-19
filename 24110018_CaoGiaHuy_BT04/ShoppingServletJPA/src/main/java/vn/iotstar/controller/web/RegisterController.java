package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import vn.iotstar.entity.User;
import vn.iotstar.form.RegisterForm;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IOtpService.IssueResult;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.ValidationUtil;

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
        RegisterForm form = RegisterForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form);

        // Kiểm tra trùng dữ liệu (chỉ khi field đó đã hợp lệ)
        if (!errors.containsKey("email") && service.checkExistEmail(form.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (!errors.containsKey("username") && service.checkExistUsername(form.getUsername())) {
            errors.put("username", "Tài khoản đã tồn tại!");
        }
        if (!errors.containsKey("phone") && !form.getPhone().isEmpty() && service.checkExistPhone(form.getPhone())) {
            errors.put("phone", "Số điện thoại đã tồn tại!");
        }
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        String username = form.getUsername();
        String password = form.getPassword();
        String email = form.getEmail();
        String fullname = form.getFullname();
        String phone = form.getPhone();

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
}
