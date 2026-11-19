package vn.iotstar.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import vn.iotstar.util.ValidationUtil;

/** Form nhập mã OTP 6 số (kích hoạt tài khoản, xác nhận email mới ở profile). */
public class OtpForm {
    @NotBlank(message = "Vui lòng nhập mã OTP")
    @Pattern(regexp = "^[0-9]{6}$", message = "Mã OTP gồm đúng 6 chữ số")
    private String otp;

    public static OtpForm from(HttpServletRequest req) {
        OtpForm f = new OtpForm();
        f.otp = ValidationUtil.trim(req.getParameter("otp"));
        return f;
    }

    public String getOtp() { return otp; }
}
