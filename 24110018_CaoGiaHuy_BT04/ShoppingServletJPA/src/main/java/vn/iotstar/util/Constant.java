package vn.iotstar.util;

import java.io.File;

public class Constant {
    // Thư mục lưu ảnh upload (đổi nếu muốn, ví dụ "E:\\upload")
    public static final String DIR = System.getProperty("user.home") + File.separator + "upload";
    public static final String DEFAULT_ICON = "avatar.png";

    public static final String SESSION_ACCOUNT = "account";
    public static final String COOKIE_REMEMBER = "username";

    // Email đang chờ kích hoạt / đang quên mật khẩu (lưu trong session giữa các bước)
    public static final String SESSION_PENDING_ACTIVATE = "pendingActivateEmail";
    public static final String SESSION_PENDING_RESET = "pendingResetEmail";

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MANAGER = 2;
    public static final int ROLE_USER = 5;

    // OTP
    public static final String OTP_ACTIVATE = "ACTIVATE";
    public static final String OTP_RESET = "RESET";
    public static final String OTP_CHANGE_EMAIL = "CHANGE_EMAIL"; // xác nhận email mới ở trang profile
    public static final int OTP_TTL_MINUTES = 5;      // OTP hết hạn sau 5 phút
    public static final int OTP_MAX_ATTEMPTS = 5;     // nhập sai tối đa 5 lần
    public static final int OTP_RESEND_SECONDS = 60;  // giãn cách giữa 2 lần gửi

    public static final int PASSWORD_MIN_LENGTH = 6;

    // Profile đang chờ xác nhận email mới (lưu trong session)
    public static final String SESSION_PENDING_PROFILE = "pendingProfile";

    // Video
    public static final int HOME_VIDEO_LIMIT = 10; // trang chủ: 10 video mới nhất
    public static final int VIDEO_PAGE_SIZE = 6;   // trang /video: 6 video / trang

    public static class Path {
        public static final String LOGIN = "/views/login.jsp";
        public static final String REGISTER = "/views/register.jsp";
        public static final String VERIFY_OTP = "/views/verify-otp.jsp";
        public static final String FORGOT_PASSWORD = "/views/forgot-password.jsp";
        public static final String RESET_PASSWORD = "/views/reset-password.jsp";
        public static final String PROFILE = "/views/profile.jsp";
        public static final String PROFILE_VERIFY = "/views/profile-verify-email.jsp";
        public static final String VIDEO_LIST = "/views/video/video-list.jsp";
        public static final String VIDEO_FORM = "/views/video/video-form.jsp";
    }
}
