package vn.iotstar.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.iotstar.util.ValidationUtil;

public class VideoForm {
    @NotBlank(message = "Tiêu đề không được rỗng")
    @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
    private String title;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @Pattern(regexp = ValidationUtil.URL_REGEX, message = "Link video phải bắt đầu bằng http:// hoặc https://")
    @Size(max = 500, message = "Link video tối đa 500 ký tự")
    private String videoUrl;

    @Pattern(regexp = ValidationUtil.URL_REGEX, message = "Link poster phải bắt đầu bằng http:// hoặc https://")
    @Size(max = 500, message = "Link poster tối đa 500 ký tự")
    private String poster;

    public static VideoForm from(HttpServletRequest req) {
        VideoForm f = new VideoForm();
        f.title = ValidationUtil.trim(req.getParameter("title"));
        f.description = ValidationUtil.trim(req.getParameter("description"));
        f.videoUrl = ValidationUtil.trim(req.getParameter("videoUrl"));
        f.poster = ValidationUtil.trim(req.getParameter("poster"));
        return f;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getVideoUrl() { return videoUrl; }
    public String getPoster() { return poster; }
}
