package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;

public interface IVideoService {
    /** Tạo id, ngày tạo, gán chủ sở hữu rồi lưu. */
    void insert(Video video, User owner);
    void update(Video video);
    void delete(String videoId);
    Video findById(String videoId);
    List<Video> findLatest(int limit);
    List<Video> findByUser(int userId, int page, int pageSize);
    long countByUser(int userId);
    void increaseViews(String videoId);
}
