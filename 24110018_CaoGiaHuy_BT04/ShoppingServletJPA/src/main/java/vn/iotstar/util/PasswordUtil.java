package vn.iotstar.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Băm mật khẩu bằng PBKDF2-HMAC-SHA256 (có sẵn trong JDK) + salt ngẫu nhiên cho mỗi mật khẩu.
 * Định dạng lưu DB: pbkdf2$<số vòng lặp>$<salt base64>$<hash base64>
 */
public final class PasswordUtil {
    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {}

    public static String hash(String rawPassword) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] key = derive(rawPassword, salt, ITERATIONS);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + b64.encodeToString(salt) + "$" + b64.encodeToString(key);
    }

    public static boolean verify(String rawPassword, String stored) {
        if (rawPassword == null || !isHashed(stored)) return false;
        try {
            String[] p = stored.split("\\$");
            if (p.length != 4) return false;
            int iterations = Integer.parseInt(p[1]);
            byte[] salt = Base64.getDecoder().decode(p[2]);
            byte[] expected = Base64.getDecoder().decode(p[3]);
            byte[] actual = derive(rawPassword, salt, iterations);
            return MessageDigest.isEqual(expected, actual); // so sánh hằng thời gian
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isHashed(String stored) {
        return stored != null && stored.startsWith(PREFIX + "$");
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Không băm được mật khẩu", e);
        }
    }
}
