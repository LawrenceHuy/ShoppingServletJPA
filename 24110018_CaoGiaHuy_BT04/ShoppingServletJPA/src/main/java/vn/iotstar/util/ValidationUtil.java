package vn.iotstar.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

/**
 * Validate form bean bằng Jakarta Bean Validation (Hibernate Validator).
 * Dùng ParameterMessageInterpolator nên KHÔNG cần thư viện EL.
 */
public final class ValidationUtil {
    private static final Validator VALIDATOR = Validation.byDefaultProvider().configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory().getValidator();

    private ValidationUtil() {}

    /** @return map tên-field -> thông báo lỗi (rỗng nếu hợp lệ). Mỗi field chỉ giữ 1 lỗi. */
    public static <T> Map<String, String> validate(T bean) {
        Map<String, String> errors = new LinkedHashMap<>();
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(bean);
        for (ConstraintViolation<T> v : violations) {
            String key = v.getPropertyPath().toString();
            // "Không được rỗng" ưu tiên hơn các lỗi khác (độ dài, định dạng...) của cùng field
            if (v.getConstraintDescriptor().getAnnotation() instanceof NotBlank) {
                errors.put(key, v.getMessage());
            } else {
                errors.putIfAbsent(key, v.getMessage());
            }
        }
        return errors;
    }

    public static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    // Regex dùng chung
    public static final String PHONE_REGEX = "^$|^(0|\\+84)[0-9]{9}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$";
    public static final String URL_REGEX = "^$|^https?://\\S+$";
}
