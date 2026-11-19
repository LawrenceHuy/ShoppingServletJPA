package vn.iotstar.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

import vn.iotstar.dao.IOtpDao;
import vn.iotstar.dao.impl.OtpDao;
import vn.iotstar.entity.OtpToken;
import vn.iotstar.entity.User;
import vn.iotstar.service.IOtpService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.MailUtil;

public class OtpServiceImpl implements IOtpService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final IOtpDao otpDao = new OtpDao();

    @Override
    public IssueResult issue(User user, String purpose) {
        return issue(user, purpose, user.getEmail());
    }

    @Override
    public IssueResult issue(User user, String purpose, String toEmail) {
        LocalDateTime now = LocalDateTime.now();
        OtpToken last = otpDao.findLatest(user.getId(), purpose);
        if (last != null && !last.isUsed()
                && last.getCreatedAt().plusSeconds(Constant.OTP_RESEND_SECONDS).isAfter(now)) {
            return IssueResult.COOLDOWN;
        }

        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        otpDao.deleteByUserAndPurpose(user.getId(), purpose);
        otpDao.insert(new OtpToken(user.getId(), purpose, hash(user.getId(), purpose, code),
                now, now.plusMinutes(Constant.OTP_TTL_MINUTES)));

        String subject;
        String intro;
        switch (purpose) {
            case Constant.OTP_ACTIVATE -> { subject = "Mã OTP kích hoạt tài khoản"; intro = "Mã OTP để kích hoạt tài khoản của bạn là:"; }
            case Constant.OTP_CHANGE_EMAIL -> { subject = "Mã OTP xác nhận email mới"; intro = "Mã OTP để xác nhận email mới cho tài khoản của bạn là:"; }
            default -> { subject = "Mã OTP đặt lại mật khẩu"; intro = "Mã OTP để đặt lại mật khẩu của bạn là:"; }
        }
        String body = "<div style=\"font-family:Arial,sans-serif;font-size:15px\">"
                + "<p>Xin chào <b>" + escape(user.getFullName() != null ? user.getFullName() : user.getUserName()) + "</b>,</p>"
                + "<p>" + intro + "</p>"
                + "<p style=\"font-size:28px;letter-spacing:6px;font-weight:bold\">" + code + "</p>"
                + "<p>Mã có hiệu lực trong " + Constant.OTP_TTL_MINUTES + " phút. Không chia sẻ mã này cho bất kỳ ai.</p>"
                + "<p>Nếu bạn không thực hiện yêu cầu này, hãy bỏ qua email.</p></div>";

        if (!MailUtil.isConfigured()) {
            // Chế độ dev: chưa cấu hình SMTP thì in OTP ra console Tomcat để test. Cấu hình mail rồi sẽ không in nữa.
            System.out.println("[DEV][OTP " + purpose + "] " + toEmail + " -> " + code);
            return IssueResult.SENT;
        }
        try {
            MailUtil.send(toEmail, subject, body);
            return IssueResult.SENT;
        } catch (Exception e) {
            e.printStackTrace();
            return IssueResult.MAIL_ERROR;
        }
    }

    @Override
    public VerifyResult verify(User user, String purpose, String code) {
        OtpToken t = otpDao.findLatest(user.getId(), purpose);
        if (t == null || t.isUsed()) return VerifyResult.NOT_FOUND;
        if (LocalDateTime.now().isAfter(t.getExpiresAt())) return VerifyResult.EXPIRED;
        if (t.getAttempts() >= Constant.OTP_MAX_ATTEMPTS) return VerifyResult.TOO_MANY;

        boolean match = code != null && MessageDigest.isEqual(
                hash(user.getId(), purpose, code.trim()).getBytes(StandardCharsets.UTF_8),
                t.getCodeHash().getBytes(StandardCharsets.UTF_8));
        if (!match) {
            t.setAttempts(t.getAttempts() + 1);
            otpDao.update(t);
            return VerifyResult.WRONG;
        }
        t.setUsed(true);
        otpDao.update(t);
        return VerifyResult.OK;
    }

    private static String hash(int userId, String purpose, String code) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest((userId + ":" + purpose + ":" + code).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
