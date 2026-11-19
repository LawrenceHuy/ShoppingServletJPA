package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import vn.iotstar.entity.User;
import vn.iotstar.form.OtpForm;
import vn.iotstar.form.PendingProfile;
import vn.iotstar.form.ProfileForm;
import vn.iotstar.service.IOtpService;
import vn.iotstar.service.IOtpService.IssueResult;
import vn.iotstar.service.IOtpService.VerifyResult;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.OtpServiceImpl;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.FileUtil;
import vn.iotstar.util.OtpMessages;
import vn.iotstar.util.UploadUtil;
import vn.iotstar.util.ValidationUtil;

/**
 * Hồ sơ cá nhân: cập nhật fullname, phone, email, ảnh đại diện (upload multipart lên Cloudinary).
 * Nếu đổi email: gửi OTP tới EMAIL MỚI, nhập đúng OTP thì mới lưu toàn bộ thay đổi.
 */
@SuppressWarnings("serial")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
@WebServlet(urlPatterns = { "/profile", "/profile/verify-email" })
public class ProfileController extends HttpServlet {
    private final IUserService userService = new UserServiceImpl();
    private final IOtpService otpService = new OtpServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = freshUser(req);
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        if (req.getServletPath().equals("/profile/verify-email")) {
            PendingProfile pending = pending(req);
            if (pending == null) { resp.sendRedirect(req.getContextPath() + "/profile"); return; }
            showVerify(req, resp, pending);
        } else {
            showProfile(req, resp, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = freshUser(req);
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        if (req.getServletPath().equals("/profile/verify-email")) {
            verifyEmail(req, resp, user);
        } else {
            updateProfile(req, resp, user);
        }
    }

    // ---------------------------------------------------------------- bước 1: gửi form profile
    private void updateProfile(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        ProfileForm form = ProfileForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form);

        boolean emailChanged = !form.getEmail().equalsIgnoreCase(user.getEmail());
        String phone = form.getPhone().isEmpty() ? null : form.getPhone();
        boolean phoneChanged = phone != null && !phone.equals(user.getPhone());

        if (!errors.containsKey("email") && emailChanged && userService.checkExistEmail(form.getEmail())) {
            errors.put("email", "Email đã được sử dụng!");
        }
        if (!errors.containsKey("phone") && phoneChanged && userService.checkExistPhone(phone)) {
            errors.put("phone", "Số điện thoại đã được sử dụng!");
        }
        Part avatarPart = req.getPart("avatarFile");
        if (FileUtil.hasFile(avatarPart) && !FileUtil.isImage(avatarPart)) {
            errors.put("avatarFile", "Ảnh đại diện chỉ nhận jpg, jpeg, png, gif, webp");
        }

        String newAvatar = null;
        if (errors.isEmpty() && FileUtil.hasFile(avatarPart)) {
            try {
                newAvatar = UploadUtil.image(avatarPart, "avatars"); // Cloudinary (hoặc thư mục cục bộ nếu chưa cấu hình)
            } catch (Exception e) {
                e.printStackTrace();
                errors.put("avatarFile", "Upload ảnh thất bại: " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            showProfile(req, resp, user);
            return;
        }

        if (!emailChanged) {
            apply(req, user, form.getFullname(), phone, null, newAvatar);
            resp.sendRedirect(req.getContextPath() + "/profile?updated=1");
            return;
        }

        // Đổi email: giữ thay đổi trong session, gửi OTP tới email mới
        IssueResult r = otpService.issue(user, Constant.OTP_CHANGE_EMAIL, form.getEmail());
        if (r == IssueResult.MAIL_ERROR) {
            req.setAttribute("alert", "Không gửi được email xác nhận, vui lòng thử lại sau.");
            showProfile(req, resp, user);
            return;
        }
        req.getSession().setAttribute(Constant.SESSION_PENDING_PROFILE,
                new PendingProfile(form.getFullname(), phone, form.getEmail(), newAvatar));
        if (r == IssueResult.COOLDOWN) {
            req.getSession().setAttribute("profileInfo", "Mã OTP vừa được gửi trước đó, hãy kiểm tra email (hoặc chờ " + Constant.OTP_RESEND_SECONDS + " giây để gửi lại).");
        }
        resp.sendRedirect(req.getContextPath() + "/profile/verify-email");
    }

    // ---------------------------------------------------------------- bước 2: nhập OTP gửi tới email mới
    private void verifyEmail(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        PendingProfile pending = pending(req);
        if (pending == null) { resp.sendRedirect(req.getContextPath() + "/profile"); return; }

        String action = req.getParameter("action");
        if ("cancel".equals(action)) {
            req.getSession().removeAttribute(Constant.SESSION_PENDING_PROFILE);
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }
        if ("resend".equals(action)) {
            String[] msg = OtpMessages.resend(otpService.issue(user, Constant.OTP_CHANGE_EMAIL, pending.getEmail()));
            req.setAttribute(msg[0], msg[1]);
            showVerify(req, resp, pending);
            return;
        }

        OtpForm form = OtpForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form);
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            showVerify(req, resp, pending);
            return;
        }
        VerifyResult r = otpService.verify(user, Constant.OTP_CHANGE_EMAIL, form.getOtp());
        if (r != VerifyResult.OK) {
            req.setAttribute("alert", OtpMessages.verifyError(r));
            showVerify(req, resp, pending);
            return;
        }
        // email có thể đã bị người khác dùng trong lúc chờ
        if (userService.checkExistEmail(pending.getEmail())) {
            req.getSession().removeAttribute(Constant.SESSION_PENDING_PROFILE);
            req.getSession().setAttribute("profileError", "Email này vừa được người khác sử dụng, hãy chọn email khác.");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }
        apply(req, user, pending.getFullName(), pending.getPhone(), pending.getEmail(), pending.getAvatar());
        req.getSession().removeAttribute(Constant.SESSION_PENDING_PROFILE);
        resp.sendRedirect(req.getContextPath() + "/profile?updated=1");
    }

    // ---------------------------------------------------------------- helpers
    /** Lưu thay đổi vào DB và cập nhật user trong session. newEmail/newAvatar = null nghĩa là không đổi. */
    private void apply(HttpServletRequest req, User user, String fullName, String phone, String newEmail, String newAvatar) {
        user.setFullName(fullName);
        user.setPhone(phone);
        if (newEmail != null) user.setEmail(newEmail);
        if (newAvatar != null) {
            UploadUtil.deleteLocal(user.getAvatar());
            user.setAvatar(newAvatar);
        }
        userService.update(user);
        req.getSession().setAttribute(Constant.SESSION_ACCOUNT, user);
    }

    private PendingProfile pending(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (PendingProfile) session.getAttribute(Constant.SESSION_PENDING_PROFILE);
    }

    /** Đọc lại user từ DB theo username trong session để dữ liệu luôn mới nhất. */
    private User freshUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        User u = session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        return u == null ? null : userService.get(u.getUserName());
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        moveFlash(req, "profileError", "alert");
        req.setAttribute("user", user);
        req.setAttribute("cloudinary", vn.iotstar.util.CloudinaryUtil.isConfigured());
        req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
    }

    private void showVerify(HttpServletRequest req, HttpServletResponse resp, PendingProfile pending) throws ServletException, IOException {
        moveFlash(req, "profileInfo", "info");
        req.setAttribute("newEmail", pending.getEmail());
        req.getRequestDispatcher(Constant.Path.PROFILE_VERIFY).forward(req, resp);
    }

    /** Chuyển thông báo 1 lần từ session sang request. */
    private void moveFlash(HttpServletRequest req, String sessionKey, String requestKey) {
        HttpSession session = req.getSession(false);
        if (session == null) return;
        Object v = session.getAttribute(sessionKey);
        if (v != null && req.getAttribute(requestKey) == null) req.setAttribute(requestKey, v);
        session.removeAttribute(sessionKey);
    }
}
