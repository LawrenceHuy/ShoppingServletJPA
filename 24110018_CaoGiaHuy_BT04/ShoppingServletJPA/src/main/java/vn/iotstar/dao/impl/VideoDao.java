package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.IVideoDao;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;

public class VideoDao implements IVideoDao {

    @Override
    public void insert(Video video) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            // dùng reference tới user (chỉ cần khóa ngoại) vì user trong session đã detach
            video.setUser(enma.getReference(User.class, video.getUser().getId()));
            enma.persist(video);
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
    public void update(Video video) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(video);
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
    public void delete(String videoId) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Video v = enma.find(Video.class, videoId);
            if (v != null) enma.remove(v);
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
    public Video findById(String videoId) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return enma.find(Video.class, videoId); // ManyToOne user mặc định EAGER nên đã có user
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findLatest(int limit) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return enma.createQuery("SELECT v FROM Video v LEFT JOIN FETCH v.user "
                    + "WHERE v.active = 1 ORDER BY v.createdDate DESC, v.videoId", Video.class)
                    .setMaxResults(limit).getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findByUser(int userId, int page, int pageSize) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return enma.createQuery("SELECT v FROM Video v LEFT JOIN FETCH v.user "
                    + "WHERE v.user.id = :uid ORDER BY v.createdDate DESC, v.videoId", Video.class)
                    .setParameter("uid", userId)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize).getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public long countByUser(int userId) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return enma.createQuery("SELECT COUNT(v) FROM Video v WHERE v.user.id = :uid", Long.class)
                    .setParameter("uid", userId).getSingleResult();
        } finally {
            enma.close();
        }
    }

    @Override
    public void increaseViews(String videoId) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.createQuery("UPDATE Video v SET v.views = v.views + 1 WHERE v.videoId = :id")
                    .setParameter("id", videoId).executeUpdate();
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
        } finally {
            enma.close();
        }
    }
}
