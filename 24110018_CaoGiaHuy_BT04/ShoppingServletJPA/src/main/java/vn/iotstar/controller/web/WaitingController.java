package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import vn.iotstar.entity.User;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/waiting")
public class WaitingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            User u = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
            if (u.getRoleid() == Constant.ROLE_ADMIN) {
                resp.sendRedirect(req.getContextPath() + "/admin/home");
            } else if (u.getRoleid() == Constant.ROLE_MANAGER) {
                resp.sendRedirect(req.getContextPath() + "/manager/home");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
