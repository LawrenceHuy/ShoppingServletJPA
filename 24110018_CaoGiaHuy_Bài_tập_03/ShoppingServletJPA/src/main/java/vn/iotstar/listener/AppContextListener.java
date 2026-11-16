package vn.iotstar.listener;

import java.sql.Date;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.impl.CategoryDao;
import vn.iotstar.dao.impl.UserDao;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.util.Constant;
import vn.iotstar.util.PasswordUtil;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            JpaConfig.getFactory(); // Hibernate tự tạo bảng (hbm2ddl.auto=update)
            IUserDao userDao = new UserDao();
            // Chuyển mật khẩu cũ (plain text) trong DB sang dạng băm, đồng thời coi là đã kích hoạt
            for (User u : userDao.findAll()) {
                if (!PasswordUtil.isHashed(u.getPassWord())) {
                    u.setPassWord(PasswordUtil.hash(u.getPassWord()));
                    u.setActive(true);
                    userDao.update(u);
                }
            }
            if (userDao.count() == 0) {
                Date now = new Date(System.currentTimeMillis());
                insertSeed(userDao, new User("admin@demo.vn", "admin", "Quản trị viên", PasswordUtil.hash("123456"), null, Constant.ROLE_ADMIN, "0900000001", now));
                insertSeed(userDao, new User("manager@demo.vn", "manager", "Quản lý", PasswordUtil.hash("123456"), null, Constant.ROLE_MANAGER, "0900000002", now));
                insertSeed(userDao, new User("user@demo.vn", "user", "Người dùng", PasswordUtil.hash("123456"), null, Constant.ROLE_USER, "0900000003", now));
            }
            ICategoryDao cateDao = new CategoryDao();
            if (cateDao.count() == 0) {
                cateDao.insert(new Category(0, "Quần Áo Nam", Constant.DEFAULT_ICON, 1));
                cateDao.insert(new Category(0, "Quần Áo Nữ", Constant.DEFAULT_ICON, 1));
            }
        } catch (Exception e) {
            System.err.println("Không kết nối được database - kiểm tra persistence.xml");
            e.printStackTrace();
        }
    }

    private void insertSeed(IUserDao dao, User u) {
        u.setActive(true); // tài khoản mẫu không cần OTP
        dao.insert(u);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaConfig.shutdown();
    }
}
