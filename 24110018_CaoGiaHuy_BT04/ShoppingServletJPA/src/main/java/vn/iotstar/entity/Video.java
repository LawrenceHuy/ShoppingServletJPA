package vn.iotstar.entity;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.*;

@Entity
@Table(name = "Videos")
@NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v")
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "VideoId")
    private String videoId;

    @Column(name = "Active")
    private int active;

    @Column(name = "Description", columnDefinition = "nvarchar(500) null")
    private String description;

    @Column(name = "Poster", columnDefinition = "nvarchar(500) null")
    private String poster;

    @Column(name = "Title", columnDefinition = "nvarchar(500) null")
    private String title;

    @Column(name = "Views")
    private int views;

    // Link video (YouTube / .mp4 ...), URL Cloudinary hoặc tên file upload cục bộ
    @Column(name = "VideoUrl", columnDefinition = "nvarchar(500) null")
    private String videoUrl;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    // many-to-one: nhiều video thuộc về 1 user (quan hệ 1-n User -> Videos)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // bi-directional many-to-one association to Category
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category category;

    public Video() {}

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }
    public int getActive() { return active; }
    public void setActive(int active) { this.active = active; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // ----- Hàm tiện ích cho JSP (không map xuống DB) -----
    private static final Pattern YOUTUBE = Pattern.compile(
            "(?:youtube\\.com/(?:watch\\?(?:[^#]*&)?v=|embed/|shorts/)|youtu\\.be/)([A-Za-z0-9_-]{11})");

    private static String pathOf(String v) {
        if (v == null || v.isBlank()) return "";
        if (v.startsWith("http")) return v;
        return "/image?fname=" + URLEncoder.encode(v, StandardCharsets.UTF_8);
    }

    /** Đường dẫn ảnh poster dùng với &lt;c:url&gt; (link http giữ nguyên, file cục bộ qua /image). */
    public String getPosterPath() { return pathOf(poster); }

    /** Đường dẫn file video dùng với &lt;c:url&gt;. */
    public String getVideoPath() { return pathOf(videoUrl); }

    /** Ngày đăng dạng dd/MM/yyyy HH:mm để hiển thị. */
    public String getCreatedDateText() {
        return createdDate == null ? "" : createdDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public boolean isYoutube() { return videoUrl != null && YOUTUBE.matcher(videoUrl).find(); }

    /** Link nhúng iframe cho video YouTube; rỗng nếu không phải YouTube. */
    public String getEmbedUrl() {
        if (videoUrl == null) return "";
        Matcher m = YOUTUBE.matcher(videoUrl);
        return m.find() ? "https://www.youtube.com/embed/" + m.group(1) : "";
    }
}
