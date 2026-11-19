package vn.iotstar.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

public class FileUtil {
    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> ALLOWED_VIDEO = Set.of("mp4", "webm", "ogg", "mov");

    /** Part có thật sự chứa file được chọn không. */
    public static boolean hasFile(Part part) {
        return part != null && part.getSize() > 0 && part.getSubmittedFileName() != null
                && !part.getSubmittedFileName().isBlank();
    }

    private static String ext(Part part) {
        String name = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        int idx = name.lastIndexOf('.');
        return idx < 0 ? "" : name.substring(idx + 1).toLowerCase();
    }

    public static boolean isImage(Part part) { return hasFile(part) && ALLOWED.contains(ext(part)); }

    public static boolean isVideo(Part part) { return hasFile(part) && ALLOWED_VIDEO.contains(ext(part)); }

    /** Lưu file video vào thư mục upload cục bộ (dùng khi chưa cấu hình Cloudinary). */
    public static String saveVideo(Part part) throws Exception {
        if (!isVideo(part)) return null;
        File dir = new File(Constant.DIR);
        if (!dir.exists()) dir.mkdirs();
        String fname = System.currentTimeMillis() + "." + ext(part);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, new File(dir, fname).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return fname;
    }

    /** Lưu ảnh upload với tên mới (timestamp.ext). Trả về tên file hoặc null nếu không có file hợp lệ. */
    public static String saveImage(Part part) throws Exception {
        if (part == null || part.getSize() <= 0) return null;
        String submitted = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        int idx = submitted.lastIndexOf('.');
        if (idx < 0) return null;
        String ext = submitted.substring(idx + 1).toLowerCase();
        if (!ALLOWED.contains(ext)) return null;

        File dir = new File(Constant.DIR);
        if (!dir.exists()) dir.mkdirs();
        String fname = System.currentTimeMillis() + "." + ext;
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, new File(dir, fname).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return fname;
    }

    public static void deleteImage(String fname) {
        if (fname == null || fname.isBlank() || fname.equals(Constant.DEFAULT_ICON)) return;
        File f = new File(Constant.DIR, Paths.get(fname).getFileName().toString());
        if (f.exists()) f.delete();
    }
}
