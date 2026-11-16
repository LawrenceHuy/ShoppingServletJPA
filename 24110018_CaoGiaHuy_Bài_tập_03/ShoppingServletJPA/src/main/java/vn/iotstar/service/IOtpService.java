package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface IOtpService {
    enum IssueResult { SENT, COOLDOWN, MAIL_ERROR }
    enum VerifyResult { OK, WRONG, EXPIRED, TOO_MANY, NOT_FOUND }

    /** Sinh OTP 6 số, lưu bản băm, gửi qua email của user. Mã cũ cùng mục đích bị huỷ. */
    IssueResult issue(User user, String purpose);

    /** Kiểm tra mã người dùng nhập. Mã đúng chỉ dùng được 1 lần. */
    VerifyResult verify(User user, String purpose, String code);
}
