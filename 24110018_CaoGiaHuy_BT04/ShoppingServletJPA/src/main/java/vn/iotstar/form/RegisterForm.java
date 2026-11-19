package vn.iotstar.form;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.iotstar.util.ValidationUtil;

public class RegisterForm {
    @NotBlank(message = "Vui lòng nhập tài khoản")
    @Size(min = 4, max = 30, message = "Tài khoản từ 4 đến 30 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9_.]*$", message = "Tài khoản chỉ gồm chữ, số, dấu _ và dấu .")
    private String username;

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullname;

    @NotBlank(message = "Vui lòng nhập email")
    @Pattern(regexp = ValidationUtil.EMAIL_REGEX, message = "Email không đúng định dạng")
    @Size(max = 255, message = "Email tối đa 255 ký tự")
    private String email;

    @Pattern(regexp = ValidationUtil.PHONE_REGEX, message = "Số điện thoại phải có 10 số, bắt đầu bằng 0 (hoặc +84)")
    private String phone;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 6, max = 50, message = "Mật khẩu từ 6 đến 50 ký tự")
    private String password;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu")
    private String repassword;

    public static RegisterForm from(HttpServletRequest req) {
        RegisterForm f = new RegisterForm();
        f.username = ValidationUtil.trim(req.getParameter("username"));
        f.fullname = ValidationUtil.trim(req.getParameter("fullname"));
        f.email = ValidationUtil.trim(req.getParameter("email")).toLowerCase(Locale.ROOT);
        f.phone = ValidationUtil.trim(req.getParameter("phone"));
        f.password = req.getParameter("password");
        f.repassword = req.getParameter("repassword");
        return f;
    }

    /** Lỗi hiển thị ở key "repasswordMatch". */
    @AssertTrue(message = "Mật khẩu nhập lại không khớp")
    public boolean isRepasswordMatch() {
        return password == null || repassword == null || password.equals(repassword);
    }

    public String getUsername() { return username; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
}
