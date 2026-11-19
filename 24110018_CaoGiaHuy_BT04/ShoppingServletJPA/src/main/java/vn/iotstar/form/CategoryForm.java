package vn.iotstar.form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.iotstar.util.ValidationUtil;

public class CategoryForm {
    @NotBlank(message = "Tên danh mục không được rỗng")
    @Size(max = 50, message = "Tên danh mục tối đa 50 ký tự")
    private String categoryname;

    @Pattern(regexp = ValidationUtil.URL_REGEX, message = "Link ảnh phải bắt đầu bằng http:// hoặc https://")
    @Size(max = 500, message = "Link ảnh tối đa 500 ký tự")
    private String images;

    public static CategoryForm from(HttpServletRequest req) {
        CategoryForm f = new CategoryForm();
        f.categoryname = ValidationUtil.trim(req.getParameter("categoryname"));
        f.images = ValidationUtil.trim(req.getParameter("images"));
        return f;
    }

    public String getCategoryname() { return categoryname; }
    public String getImages() { return images; }
}
