package vn.iotstar.util;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Upload file lên Cloudinary. Cấu hình lấy từ biến môi trường CLOUDINARY_URL (ưu tiên)
 * hoặc file src/main/resources/cloudinary.properties (KHÔNG commit file này).
 */
public final class CloudinaryUtil {
    private static final Properties CFG = load();
    private static Cloudinary client;

    private CloudinaryUtil() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = CloudinaryUtil.class.getResourceAsStream("/cloudinary.properties")) {
            if (in != null) p.load(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public static boolean isConfigured() {
        String url = System.getenv("CLOUDINARY_URL");
        if (url != null && !url.isBlank()) return true;
        String name = CFG.getProperty("cloudinary.cloud_name", "");
        String key = CFG.getProperty("cloudinary.api_key", "");
        String secret = CFG.getProperty("cloudinary.api_secret", "");
        return !name.isBlank() && !key.isBlank() && !secret.isBlank() && !name.startsWith("your_");
    }

    private static synchronized Cloudinary get() {
        if (client == null) {
            String url = System.getenv("CLOUDINARY_URL");
            if (url != null && !url.isBlank()) {
                client = new Cloudinary(url);
            } else {
                client = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", CFG.getProperty("cloudinary.cloud_name"),
                        "api_key", CFG.getProperty("cloudinary.api_key"),
                        "api_secret", CFG.getProperty("cloudinary.api_secret"),
                        "secure", true));
            }
        }
        return client;
    }

    /**
     * @param resourceType "image" hoặc "video"
     * @return secure_url của file trên Cloudinary
     */
    @SuppressWarnings("rawtypes")
    public static String upload(byte[] data, String folder, String resourceType) throws Exception {
        Map result = get().uploader().upload(data, ObjectUtils.asMap(
                "folder", folder,
                "resource_type", resourceType));
        return (String) result.get("secure_url");
    }
}
