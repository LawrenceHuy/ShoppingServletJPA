package vn.iotstar.form;

import java.io.Serializable;

/** Thông tin profile đang chờ xác nhận OTP gửi tới email mới (lưu trong session). */
public class PendingProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String fullName;
    private final String phone;   // null nếu để trống
    private final String email;   // email MỚI
    private final String avatar;  // URL/tên file ảnh mới; null nếu không đổi ảnh

    public PendingProfile(String fullName, String phone, String email, String avatar) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
    }

    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
}
