package vn.iotstar.util;

import vn.iotstar.service.IOtpService.IssueResult;
import vn.iotstar.service.IOtpService.VerifyResult;

/** Thông báo hiển thị cho người dùng tương ứng với kết quả gửi / kiểm tra OTP. */
public final class OtpMessages {
    private OtpMessages() {}

    public static String verifyError(VerifyResult r) {
        return switch (r) {
            case WRONG -> "Mã OTP không đúng!";
            case EXPIRED -> "Mã OTP đã hết hạn, hãy bấm \"Gửi lại mã\"!";
            case TOO_MANY -> "Bạn đã nhập sai quá " + Constant.OTP_MAX_ATTEMPTS + " lần, hãy bấm \"Gửi lại mã\"!";
            case NOT_FOUND -> "Chưa có mã OTP hợp lệ, hãy bấm \"Gửi lại mã\"!";
            case OK -> null;
        };
    }

    /** @return {loại("alert"|"info"), nội dung} */
    public static String[] resend(IssueResult r) {
        return switch (r) {
            case SENT -> new String[] { "info", "Đã gửi mã OTP mới tới email của bạn." };
            case COOLDOWN -> new String[] { "alert", "Vui lòng chờ " + Constant.OTP_RESEND_SECONDS + " giây rồi hãy gửi lại mã." };
            case MAIL_ERROR -> new String[] { "alert", "Không gửi được email, vui lòng thử lại sau." };
        };
    }
}
