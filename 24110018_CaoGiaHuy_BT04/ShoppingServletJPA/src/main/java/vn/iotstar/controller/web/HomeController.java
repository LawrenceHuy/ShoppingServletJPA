package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import vn.iotstar.entity.Video;
import vn.iotstar.service.IVideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/home", "/admin/home", "/manager/home" })
public class HomeController extends HttpServlet {
    private final IVideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        String view = "/views/home.jsp";
        if (path.startsWith("/admin")) {
            view = "/views/admin/home.jsp";
        } else if (path.startsWith("/manager")) {
            view = "/views/manager/home.jsp";
        } else {
            // Trang chủ: 10 video mới nhất; bấm tiêu đề (?vid=...) để xem chi tiết và play 1 video
            req.setAttribute("videos", videoService.findLatest(Constant.HOME_VIDEO_LIMIT));
            Video current = openVideo(req, videoService, req.getParameter("vid"), null);
            if (current != null && current.getActive() == 1) {
                req.setAttribute("current", current);
            }
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }

    /**
     * Mở 1 video để xem: tăng lượt xem (mỗi video chỉ tính 1 lần / session) rồi trả về bản mới nhất.
     * @param ownerId nếu khác null thì video phải thuộc user này.
     */
    static Video openVideo(HttpServletRequest req, IVideoService service, String vid, Integer ownerId) {
        if (vid == null || vid.isBlank()) return null;
        Video v = service.findById(vid);
        if (v == null) return null;
        if (ownerId != null && (v.getUser() == null || v.getUser().getId() != ownerId)) return null;

        HttpSession session = req.getSession();
        @SuppressWarnings("unchecked")
        Set<String> watched = (Set<String>) session.getAttribute("watchedVideos");
        if (watched == null) {
            watched = new HashSet<>();
            session.setAttribute("watchedVideos", watched);
        }
        if (watched.add(vid)) {
            service.increaseViews(vid);
            v = service.findById(vid);
        }
        return v;
    }
}
