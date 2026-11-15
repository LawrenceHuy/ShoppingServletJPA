package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.IUserDao;
import vn.iotstar.entity.User;

public class UserDao implements IUserDao {

    @Override
    public void insert(User user) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public User findByUsername(String username) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            TypedQuery<User> q = enma.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class);
            q.setParameter("username", username);
            List<User> list = q.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            enma.close();
        }
    }

    @Override
    public int count() {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return ((Long) enma.createQuery("SELECT COUNT(u) FROM User u").getSingleResult()).intValue();
        } finally {
            enma.close();
        }
    }

    // field là hằng số do code truyền vào (không lấy từ người dùng) nên an toàn
    private boolean exists(String field, String value) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            TypedQuery<Long> q = enma.createQuery("SELECT COUNT(u) FROM User u WHERE u." + field + " = :v", Long.class);
            q.setParameter("v", value);
            return q.getSingleResult() > 0;
        } finally {
            enma.close();
        }
    }

    @Override public boolean checkExistEmail(String email) { return exists("email", email); }
    @Override public boolean checkExistUsername(String username) { return exists("userName", username); }
    @Override public boolean checkExistPhone(String phone) { return exists("phone", phone); }
}
