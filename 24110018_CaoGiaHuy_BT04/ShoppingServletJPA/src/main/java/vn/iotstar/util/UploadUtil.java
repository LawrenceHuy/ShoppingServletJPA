package vn.iotstar.util;

import jakarta.servlet.http.Part;

/**
 * Upload ảnh / video: có cấu hình Cloudinary thì đẩy lên Cloudinary và trả về URL (https://...),
 * chưa cấu hình thì lưu vào thư mục cục bộ (Constant.DIR) và trả về tên file (xem qua /image?fname=).
 */
public final class UploadUtil {
    private UploadUtil() {}

    /** @return URL hoặc tên file; null nếu không có file hợp lệ. */
    public static String image(Part part, String folder) throws Exception {
        if (!FileUtil.isImage(part)) return null;
        if (CloudinaryUtil.isConfigured()) {
            try (var in = part.getInputStream()) {
                return CloudinaryUtil.upload(in.readAllBytes(), folder, "image");
            }
        }
        return FileUtil.saveImage(part);
    }

    public static String video(Part part, String folder) throws Exception {
        if (!FileUtil.isVideo(part)) return null;
        if (CloudinaryUtil.isConfigured()) {
            try (var in = part.getInputStream()) {
                return CloudinaryUtil.upload(in.readAllBytes(), folder, "video");
            }
        }
        return FileUtil.saveVideo(part);
    }

    /** Chỉ xóa file cục bộ do mình lưu (không đụng tới link http / Cloudinary / ảnh mặc định). */
    public static void deleteLocal(String value) {
        FileUtil.deleteImage(value == null || value.startsWith("http") ? null : value);
    }
}
