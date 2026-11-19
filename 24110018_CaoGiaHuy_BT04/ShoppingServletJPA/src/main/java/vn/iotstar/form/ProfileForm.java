package vn.iotstar.form;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.iotstar.util.ValidationUtil;

public class ProfileForm {
    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullname;

    @NotBlank(message = "Vui lòng nhập email")
    @Pattern(regexp = ValidationUtil.EMAIL_REGEX, message = "Email không đúng định dạng")
    @Size(max = 255, message = "Email tối đa 255 ký tự")
    private String email;

    @Pattern(regexp = ValidationUtil.PHONE_REGEX, message = "Số điện thoại phải có 10 số, bắt đầu bằng 0 (hoặc +84)")
    private String phone;

    public static ProfileForm from(HttpServletRequest req) {
        ProfileForm f = new ProfileForm();
        f.fullname = ValidationUtil.trim(req.getParameter("fullname"));
        f.email = ValidationUtil.trim(req.getParameter("email")).toLowerCase(Locale.ROOT);
        f.phone = ValidationUtil.trim(req.getParameter("phone"));
        return f;
    }

    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
