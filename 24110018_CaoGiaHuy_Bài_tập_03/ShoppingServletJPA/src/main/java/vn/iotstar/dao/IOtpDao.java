package vn.iotstar.dao;

import vn.iotstar.entity.OtpToken;

public interface IOtpDao {
    void insert(OtpToken token);
    void update(OtpToken token);
    /** Xoá mọi OTP cũ của user theo mục đích (mỗi lần gửi mới thì mã cũ mất hiệu lực). */
    void deleteByUserAndPurpose(int userId, String purpose);
    OtpToken findLatest(int userId, String purpose);
}
