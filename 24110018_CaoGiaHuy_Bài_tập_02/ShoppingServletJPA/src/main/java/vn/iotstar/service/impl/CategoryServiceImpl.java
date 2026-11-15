package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.dao.impl.CategoryDao;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;

public class CategoryServiceImpl implements ICategoryService {
    public ICategoryDao cateDao = new CategoryDao();

    @Override public List<Category> findAll() { return cateDao.findAll(); }
    @Override public Category findById(int id) { return cateDao.findById(id); }
    @Override public List<Category> searchByName(String keyword) { return cateDao.searchByName(keyword); }
    @Override public int count() { return cateDao.count(); }
    @Override public List<Category> findAll(int page, int pagesize) { return cateDao.findAll(page, pagesize); }
    @Override public Category findByCategoryname(String name) { return cateDao.findByCategoryname(name); }

    @Override
    public void insert(Category category) {
        // chỉ thêm khi tên danh mục chưa tồn tại
        if (this.findByCategoryname(category.getCategoryname()) == null) {
            cateDao.insert(category);
        }
    }

    @Override
    public void update(Category category) {
        if (this.findById(category.getCategoryid()) != null) {
            cateDao.update(category);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        cateDao.delete(id);
    }
}
