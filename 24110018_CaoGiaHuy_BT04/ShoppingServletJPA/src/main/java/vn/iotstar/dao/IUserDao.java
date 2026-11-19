package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.User;

public interface IUserDao {
    void insert(User user);
    void update(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    int count();
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}
