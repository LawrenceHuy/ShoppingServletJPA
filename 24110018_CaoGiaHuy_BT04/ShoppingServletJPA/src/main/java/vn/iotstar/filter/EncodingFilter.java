package vn.iotstar.filter;

import jakarta.servlet.*;
import java.io.IOException;

/** Đăng ký trong web.xml (để chạy TRƯỚC Sitemesh). */
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        chain.doFilter(req, resp);
    }
}
