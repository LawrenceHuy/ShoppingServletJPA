package vn.iotstar.form;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import vn.iotstar.util.ValidationUtil;

public class ForgotPasswordForm {
    @NotBlank(message = "Vui lòng nhập email")
    @Pattern(regexp = ValidationUtil.EMAIL_REGEX, message = "Email không đúng định dạng")
    private String email;

    public static ForgotPasswordForm from(HttpServletRequest req) {
        ForgotPasswordForm f = new ForgotPasswordForm();
        f.email = ValidationUtil.trim(req.getParameter("email")).toLowerCase(Locale.ROOT);
        return f;
    }

    public String getEmail() { return email; }
}
