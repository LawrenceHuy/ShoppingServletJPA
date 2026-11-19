package vn.iotstar.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * Cấu hình Sitemesh 3: một decorator Bootstrap duy nhất áp dụng cho toàn bộ trang HTML.
 * Đăng ký trong web.xml.
 */
public class SiteMeshConfig extends ConfigurableSiteMeshFilter {
    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
    	builder.addDecoratorPath("/*", "/main.jsp")
               .addExcludedPath("/image")        // trả về ảnh/video, không phải HTML
               .addExcludedPath("/assets/*");
    }
}
