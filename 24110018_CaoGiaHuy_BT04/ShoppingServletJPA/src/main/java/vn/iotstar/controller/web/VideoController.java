package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.form.VideoForm;
import vn.iotstar.service.IVideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.FileUtil;
import vn.iotstar.util.UploadUtil;
import vn.iotstar.util.ValidationUtil;

/**
 * CRUD Video của user đang đăng nhập.
 * GET  /video            : danh sách video của tôi (6 video / trang), ?vid=... để xem + play 1 video
 * GET  /video/add        : form thêm
 * POST /video/insert     : thêm (multipart)
 * GET  /video/edit?id=   : form sửa
 * POST /video/update     : sửa (multipart)
 * POST /video/delete     : xóa
 */
@SuppressWarnings("serial")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 100, maxRequestSize = 1024 * 1024 * 110)
@WebServlet(urlPatterns = { "/video", "/video/add", "/video/insert", "/video/edit", "/video/update", "/video/delete" })
public class VideoController extends HttpServlet {
    private final IVideoService service = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        switch (req.getServletPath()) {
            case "/video" -> showList(req, resp, user);
            case "/video/add" -> {
                req.setAttribute("video", new Video());
                req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            }
            case "/video/edit" -> {
                Video v = ownedVideo(req.getParameter("id"), user);
                if (v == null) { resp.sendRedirect(req.getContextPath() + "/video"); return; }
                req.setAttribute("video", v);
                req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            }
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        switch (req.getServletPath()) {
            case "/video/insert" -> save(req, resp, user, null);
            case "/video/update" -> {
                Video v = ownedVideo(req.getParameter("videoId"), user);
                if (v == null) { resp.sendRedirect(req.getContextPath() + "/video"); return; }
                save(req, resp, user, v);
            }
            case "/video/delete" -> {
                Video v = ownedVideo(req.getParameter("videoId"), user);
                if (v != null) {
                    service.delete(v.getVideoId());
                    UploadUtil.deleteLocal(v.getPoster());
                    UploadUtil.deleteLocal(v.getVideoUrl());
                }
                resp.sendRedirect(req.getContextPath() + "/video");
            }
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ---------------------------------------------------------------- list
    private void showList(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        int pageSize = Constant.VIDEO_PAGE_SIZE;
        long total = service.countByUser(user.getId());
        int totalPages = (int) Math.max(1, (total + pageSize - 1) / pageSize);
        int page = 1;
        try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (NumberFormatException ignored) { }
        page = Math.min(Math.max(page, 1), totalPages);

        req.setAttribute("videos", service.findByUser(user.getId(), page, pageSize));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("total", total);

        Video current = HomeController.openVideo(req, service, req.getParameter("vid"), user.getId());
        if (current != null) req.setAttribute("current", current);
        req.getRequestDispatcher(Constant.Path.VIDEO_LIST).forward(req, resp);
    }

    // ---------------------------------------------------------------- insert / update
    /** @param existing null = thêm mới; khác null = cập nhật video đó. */
    private void save(HttpServletRequest req, HttpServletResponse resp, User user, Video existing)
            throws ServletException, IOException {
        boolean isNew = existing == null;
        Video video = isNew ? new Video() : existing;

        VideoForm form = VideoForm.from(req);
        Map<String, String> errors = ValidationUtil.validate(form);

        Part posterPart = req.getPart("posterFile");
        Part videoPart = req.getPart("videoFile");
        if (FileUtil.hasFile(posterPart) && !FileUtil.isImage(posterPart)) {
            errors.put("posterFile", "Poster chỉ nhận ảnh jpg, jpeg, png, gif, webp");
        }
        if (FileUtil.hasFile(videoPart) && !FileUtil.isVideo(videoPart)) {
            errors.put("videoFile", "Video chỉ nhận mp4, webm, ogg, mov");
        }
        boolean hasNewVideo = FileUtil.hasFile(videoPart);
        if (isNew && form.getVideoUrl().isEmpty() && !hasNewVideo && !errors.containsKey("videoUrl")) {
            errors.put("videoUrl", "Vui lòng nhập link video hoặc chọn file video");
        }

        String oldPoster = video.getPoster();
        String oldVideo = video.getVideoUrl();
        // đưa dữ liệu form lên đối tượng (chưa lưu) để hiển thị lại nếu có lỗi
        video.setTitle(form.getTitle());
        video.setDescription(form.getDescription().isEmpty() ? null : form.getDescription());
        video.setActive("0".equals(req.getParameter("active")) ? 0 : 1);

        if (errors.isEmpty()) {
            try {
                String newPoster = UploadUtil.image(posterPart, "videos/posters");
                String newVideo = UploadUtil.video(videoPart, "videos");

                if (newPoster != null) video.setPoster(newPoster);
                else if (!form.getPoster().isEmpty()) video.setPoster(form.getPoster());
                else if (isNew) video.setPoster(null);

                if (newVideo != null) video.setVideoUrl(newVideo);
                else if (!form.getVideoUrl().isEmpty()) video.setVideoUrl(form.getVideoUrl());

                if (isNew) service.insert(video, user);
                else service.update(video);

                // xóa file cục bộ cũ khi đã được thay thế
                if (!isNew) {
                    if (newPoster != null || !form.getPoster().isEmpty()) {
                        if (oldPoster != null && !oldPoster.equals(video.getPoster())) UploadUtil.deleteLocal(oldPoster);
                    }
                    if (oldVideo != null && !oldVideo.equals(video.getVideoUrl())) UploadUtil.deleteLocal(oldVideo);
                }
                resp.sendRedirect(req.getContextPath() + "/video");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                errors.put("videoFile", "Upload thất bại: " + e.getMessage());
                video.setPoster(oldPoster);
                video.setVideoUrl(oldVideo);
            }
        }

        // có lỗi: hiển thị lại form với dữ liệu người dùng vừa nhập
        if (!form.getVideoUrl().isEmpty()) video.setVideoUrl(form.getVideoUrl());
        if (!form.getPoster().isEmpty()) video.setPoster(form.getPoster());
        req.setAttribute("errors", errors);
        req.setAttribute("video", video);
        req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
    }

    // ---------------------------------------------------------------- helpers
    private User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
    }

    /** Video có thật và thuộc về user, ngược lại null. */
    private Video ownedVideo(String id, User user) {
        Video v = service.findById(id);
        return (v != null && v.getUser() != null && v.getUser().getId() == user.getId()) ? v : null;
    }
}
