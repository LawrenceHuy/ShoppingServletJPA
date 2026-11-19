package vn.iotstar.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import vn.iotstar.util.ValidationUtil;

public class LoginForm {
    @NotBlank(message = "Vui lòng nhập tài khoản")
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    private String password;

    public static LoginForm from(HttpServletRequest req) {
        LoginForm f = new LoginForm();
        f.username = ValidationUtil.trim(req.getParameter("username"));
        f.password = req.getParameter("password");
        return f;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
