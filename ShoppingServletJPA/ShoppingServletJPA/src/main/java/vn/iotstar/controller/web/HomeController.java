package vn.iotstar.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/home", "/admin/home", "/manager/home" })
public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        String view = "/views/home.jsp";
        if (path.startsWith("/admin")) view = "/views/admin/home.jsp";
        else if (path.startsWith("/manager")) view = "/views/manager/home.jsp";
        req.getRequestDispatcher(view).forward(req, resp);
    }
}
