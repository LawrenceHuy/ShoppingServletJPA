package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface IUserService {
    /** Kiểm tra tài khoản + mật khẩu (so khớp với bản băm). Trả về null nếu sai. Không kiểm tra active. */
    User login(String username, String password);
    User get(String username);
    User findByEmail(String email);
    void insert(User user);
    void update(User user);
    /** Tạo tài khoản CHƯA kích hoạt (active = false), mật khẩu được băm. Trả về null nếu trùng username. */
    User register(String username, String password, String email, String fullname, String phone);
    void changePassword(User user, String newRawPassword);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}
