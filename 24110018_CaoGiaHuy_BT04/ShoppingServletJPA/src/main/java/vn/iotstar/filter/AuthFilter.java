package vn.iotstar.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

import vn.iotstar.entity.User;
import vn.iotstar.util.Constant;

/** /admin/* chỉ cho roleid = 1, /manager/* chỉ cho roleid = 2. */
@WebFilter({ "/admin/*", "/manager/*" })
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User u = (session == null) ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        String path = req.getServletPath();

        boolean ok = u != null
                && ((path.startsWith("/admin") && u.getRoleid() == Constant.ROLE_ADMIN)
                 || (path.startsWith("/manager") && u.getRoleid() == Constant.ROLE_MANAGER));
        if (ok) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
