# ShoppingServletJPA

Bài tập JPA (Hibernate) với Servlet/JSP — SQL Server.

Nội dung:
1. **Bài tập 01 với JPA**: Login (Session + Cookie "Nhớ tôi"), Register, phân quyền admin/manager/user — tầng DAO dùng JPA thay JDBC.
2. **Cấu hình và test JPA**: `persistence.xml`, entity `Category` / `Video` / `User`, `JpaConfig`, lớp `vn.iotstar.config.Test`.
3. **CRUD Category bằng JPA**: Thêm / Sửa / Xóa / Tìm kiếm danh mục (có upload ảnh hoặc dán link ảnh).

## Chạy
1. SSMS: chạy `sql/create-database.sql` (chỉ tạo database rỗng `ShoppingServletJPA`; bảng do Hibernate tự tạo).
2. Sửa port / user / password trong `src/main/resources/META-INF/persistence.xml`.
3. (Tuỳ chọn) chạy thử JPA: chuột phải `vn.iotstar.config.Test` → Run As → Java Application.
4. Chạy web: Run As → Run on Server (Tomcat 10.1), mở `http://localhost:8080/ShoppingServletJPA/`.

Tài khoản mẫu tự tạo lần chạy đầu (mật khẩu `123456`): `admin` (roleid 1), `manager` (2), `user` (5).

## URL
`/login`, `/register`, `/logout`, `/waiting`, `/admin/categories`, `/admin/category/add|edit|delete`, `/image?fname=`

## Cấu trúc
controller → service (I*Service) → dao (I*Dao, JPA EntityManager) → SQL Server
