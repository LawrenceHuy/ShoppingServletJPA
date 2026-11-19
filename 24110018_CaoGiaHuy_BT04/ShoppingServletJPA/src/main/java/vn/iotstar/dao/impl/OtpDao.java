package vn.iotstar.dao.impl;

import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.IOtpDao;
import vn.iotstar.entity.OtpToken;

public class OtpDao implements IOtpDao {

    private void inTransaction(Consumer<EntityManager> work) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            work.accept(enma);
            trans.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void insert(OtpToken token) {
        inTransaction(em -> em.persist(token));
    }

    @Override
    public void update(OtpToken token) {
        inTransaction(em -> em.merge(token));
    }

    @Override
    public void deleteByUserAndPurpose(int userId, String purpose) {
        inTransaction(em -> em
                .createQuery("DELETE FROM OtpToken t WHERE t.userId = :uid AND t.purpose = :p")
                .setParameter("uid", userId)
                .setParameter("p", purpose)
                .executeUpdate());
    }

    @Override
    public OtpToken findLatest(int userId, String purpose) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            List<OtpToken> list = enma.createQuery(
                    "SELECT t FROM OtpToken t WHERE t.userId = :uid AND t.purpose = :p ORDER BY t.createdAt DESC",
                    OtpToken.class)
                    .setParameter("uid", userId)
                    .setParameter("p", purpose)
                    .setMaxResults(1)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            enma.close();
        }
    }
}
