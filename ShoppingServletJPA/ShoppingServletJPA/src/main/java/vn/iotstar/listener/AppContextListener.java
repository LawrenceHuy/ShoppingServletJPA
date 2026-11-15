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

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            JpaConfig.getFactory(); // Hibernate tự tạo bảng (hbm2ddl.auto=update)
            IUserDao userDao = new UserDao();
            if (userDao.count() == 0) {
                Date now = new Date(System.currentTimeMillis());
                userDao.insert(new User("admin@demo.vn", "admin", "Quản trị viên", "123456", null, Constant.ROLE_ADMIN, "0900000001", now));
                userDao.insert(new User("manager@demo.vn", "manager", "Quản lý", "123456", null, Constant.ROLE_MANAGER, "0900000002", now));
                userDao.insert(new User("user@demo.vn", "user", "Người dùng", "123456", null, Constant.ROLE_USER, "0900000003", now));
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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaConfig.shutdown();
    }
}
