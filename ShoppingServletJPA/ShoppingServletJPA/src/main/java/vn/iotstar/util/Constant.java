package vn.iotstar.util;

import java.io.File;

public class Constant {
    // Thư mục lưu ảnh upload (đổi nếu muốn, ví dụ "E:\\upload")
    public static final String DIR = System.getProperty("user.home") + File.separator + "upload";
    public static final String DEFAULT_ICON = "avatar.png";

    public static final String SESSION_ACCOUNT = "account";
    public static final String COOKIE_REMEMBER = "username";

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MANAGER = 2;
    public static final int ROLE_USER = 5;

    public static class Path {
        public static final String LOGIN = "/views/login.jsp";
        public static final String REGISTER = "/views/register.jsp";
    }
}
