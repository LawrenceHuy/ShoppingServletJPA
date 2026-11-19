package vn.iotstar.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.iotstar.util.ValidationUtil;

public class ResetPasswordForm {
    @NotBlank(message = "Vui lòng nhập mã OTP")
    @Pattern(regexp = "^[0-9]{6}$", message = "Mã OTP gồm đúng 6 chữ số")
    private String otp;

    @NotBlank(message = "Vui lòng nhập mật khẩu mới")
    @Size(min = 6, max = 50, message = "Mật khẩu từ 6 đến 50 ký tự")
    private String password;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu mới")
    private String repassword;

    public static ResetPasswordForm from(HttpServletRequest req) {
        ResetPasswordForm f = new ResetPasswordForm();
        f.otp = ValidationUtil.trim(req.getParameter("otp"));
        f.password = req.getParameter("password");
        f.repassword = req.getParameter("repassword");
        return f;
    }

    @AssertTrue(message = "Mật khẩu nhập lại không khớp")
    public boolean isRepasswordMatch() {
        return password == null || repassword == null || password.equals(repassword);
    }

    public String getOtp() { return otp; }
    public String getPassword() { return password; }
}
