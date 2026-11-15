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
