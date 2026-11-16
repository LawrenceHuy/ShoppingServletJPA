package vn.iotstar.service.impl;

import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.impl.UserDao;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.PasswordUtil;

public class UserServiceImpl implements IUserService {
    private final IUserDao userDao = new UserDao();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user != null && PasswordUtil.verify(password, user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public User register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return null;
        }
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        User user = new User(email, username, fullname, PasswordUtil.hash(password), null, Constant.ROLE_USER, phone, date);
        user.setActive(false); // phải nhập OTP gửi qua email mới kích hoạt
        userDao.insert(user);
        return user;
    }

    @Override
    public void changePassword(User user, String newRawPassword) {
        user.setPassWord(PasswordUtil.hash(newRawPassword));
        userDao.update(user);
    }

    @Override public boolean checkExistEmail(String email) { return userDao.checkExistEmail(email); }
    @Override public boolean checkExistUsername(String username) { return userDao.checkExistUsername(username); }
    @Override public boolean checkExistPhone(String phone) { return userDao.checkExistPhone(phone); }
}
