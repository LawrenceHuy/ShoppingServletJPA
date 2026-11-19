package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.Video;

public interface IVideoDao {
    void insert(Video video);
    void update(Video video);
    void delete(String videoId);
    Video findById(String videoId);
    /** N video đang hoạt động, mới nhất trước. */
    List<Video> findLatest(int limit);
    /** Video của 1 user, phân trang (page bắt đầu từ 1). */
    List<Video> findByUser(int userId, int page, int pageSize);
    long countByUser(int userId);
    void increaseViews(String videoId);
}
