package vn.iotstar.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import vn.iotstar.dao.IVideoDao;
import vn.iotstar.dao.impl.VideoDao;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.IVideoService;

public class VideoServiceImpl implements IVideoService {
    private final IVideoDao dao = new VideoDao();

    @Override
    public void insert(Video video, User owner) {
        video.setVideoId(UUID.randomUUID().toString());
        video.setCreatedDate(LocalDateTime.now());
        video.setUser(owner);
        dao.insert(video);
    }

    @Override public void update(Video video) { dao.update(video); }
    @Override public void delete(String videoId) { dao.delete(videoId); }
    @Override public Video findById(String videoId) { return videoId == null ? null : dao.findById(videoId); }
    @Override public List<Video> findLatest(int limit) { return dao.findLatest(limit); }
    @Override public List<Video> findByUser(int userId, int page, int pageSize) { return dao.findByUser(userId, page, pageSize); }
    @Override public long countByUser(int userId) { return dao.countByUser(userId); }
    @Override public void increaseViews(String videoId) { dao.increaseViews(videoId); }
}
