package vn.iotstar.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.form.CategoryForm;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.FileUtil;
import vn.iotstar.util.ValidationUtil;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 25)
@WebServlet(urlPatterns = { "/admin/categories", "/admin/category/add", "/admin/category/insert",
        "/admin/category/edit", "/admin/category/update", "/admin/category/delete" })
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ICategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (path.equals("/admin/categories")) {
            String keyword = req.getParameter("keyword");
            List<Category> list = (keyword == null || keyword.isBlank())
                    ? cateService.findAll()
                    : cateService.searchByName(keyword.trim());
            req.setAttribute("listcate", list);
            req.setAttribute("keyword", keyword);
            req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);
        } else if (path.equals("/admin/category/add")) {
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
        } else if (path.equals("/admin/category/edit")) {
            Category category = findFromParam(req);
            if (category == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
                return;
            }
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
        } else if (path.equals("/admin/category/delete")) {
            Category category = findFromParam(req);
            if (category != null) {
                try {
                    cateService.delete(category.getCategoryid());
                    deleteOldImage(category.getImages());
                } catch (Exception e) {
                    // ví dụ: danh mục đang có video tham chiếu -> không xóa được
                    e.printStackTrace();
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (path.equals("/admin/category/insert")) {
            CategoryForm form = CategoryForm.from(req);
            Map<String, String> errors = ValidationUtil.validate(form);
            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
                return;
            }
            String categoryname = form.getCategoryname();
            Category category = new Category();
            category.setCategoryname(categoryname.trim());
            category.setStatus(parseStatus(req.getParameter("status")));

            String link = form.getImages();
            String fname = saveUpload(req);
            if (fname != null) {
                category.setImages(fname);
            } else if (link != null && !link.isBlank()) {
                category.setImages(link.trim());
            } else {
                category.setImages(Constant.DEFAULT_ICON);
            }
            cateService.insert(category);
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } else if (path.equals("/admin/category/update")) {
            int id;
            try {
                id = Integer.parseInt(req.getParameter("categoryid"));
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
                return;
            }
            Category category = cateService.findById(id);
            if (category == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
                return;
            }
            CategoryForm form = CategoryForm.from(req);
            Map<String, String> errors = ValidationUtil.validate(form);
            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.setAttribute("cate", category);
                req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
                return;
            }
            String fileold = category.getImages();
            category.setCategoryname(form.getCategoryname());
            category.setStatus(parseStatus(req.getParameter("status")));

            String link = form.getImages();
            String fname = saveUpload(req);
            if (fname != null) {
                deleteOldImage(fileold); // xóa file cũ trên thư mục
                category.setImages(fname);
            } else if (link != null && !link.isBlank()) {
                category.setImages(link.trim());
            } else {
                category.setImages(fileold);
            }
            cateService.update(category);
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Category findFromParam(HttpServletRequest req) {
        try {
            return cateService.findById(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parseStatus(String s) {
        return "0".equals(s) ? 0 : 1;
    }

    private String saveUpload(HttpServletRequest req) {
        try {
            Part part = req.getPart("images1");
            return FileUtil.saveImage(part);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Chỉ xóa file do mình upload (không xóa link https hay ảnh mặc định). */
    private void deleteOldImage(String fileold) {
        if (fileold == null || fileold.startsWith("http") || fileold.equals(Constant.DEFAULT_ICON)) return;
        new File(Constant.DIR, new File(fileold).getName()).delete();
    }
}
