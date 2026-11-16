# ShoppingServletJPA

Bài tập JPA (Hibernate) với Servlet/JSP — SQL Server.

Nội dung:
1. **Bài tập 01 với JPA**: Login (Session + Cookie "Nhớ tôi"), Register, phân quyền admin/manager/user — tầng DAO dùng JPA thay JDBC.
2. **Cấu hình và test JPA**: `persistence.xml`, entity `Category` / `Video` / `User`, `JpaConfig`, lớp `vn.iotstar.config.Test`.
3. **CRUD Category bằng JPA**: Thêm / Sửa / Xóa / Tìm kiếm danh mục (có upload ảnh hoặc dán link ảnh).

4. **Bài tập 02**: Kích hoạt tài khoản bằng OTP qua email khi đăng ký; đăng nhập với mật khẩu băm (PBKDF2-HMAC-SHA256 + salt) và đăng xuất; quên mật khẩu bằng OTP qua email.

## Chạy
1. SSMS: chạy `sql/create-database.sql` (chỉ tạo database rỗng `ShoppingServletJPA`; bảng do Hibernate tự tạo).
2. Sửa port / user / password trong `src/main/resources/META-INF/persistence.xml`.
3. Cấu hình gửi email: copy `src/main/resources/mail.properties.example` thành `mail.properties` rồi điền Gmail + App password (hoặc đặt biến môi trường `MAIL_USERNAME`, `MAIL_PASSWORD`). Nếu chưa cấu hình, OTP được in ra console Tomcat (`[DEV][OTP ...]`) để test.
3b. (Tuỳ chọn) chạy thử JPA: chuột phải `vn.iotstar.config.Test` → Run As → Java Application.
4. Chạy web: Run As → Run on Server (Tomcat 10.1), mở `http://localhost:8080/ShoppingServletJPA/`.

Tài khoản mẫu tự tạo lần chạy đầu (mật khẩu `123456`): `admin` (roleid 1), `manager` (2), `user` (5).

## URL
`/login`, `/register`, `/verify-otp`, `/forgot-password`, `/reset-password`, `/logout`, `/waiting`, `/admin/categories`, `/admin/category/add|edit|delete`, `/image?fname=`

## Cấu trúc
controller → service (I*Service) → dao (I*Dao, JPA EntityManager) → SQL Server

## Bài tập 02 - luồng chức năng
- Đăng ký → tạo user `active=false`, mật khẩu băm → gửi OTP 6 số (hết hạn 5 phút, sai tối đa 5 lần, gửi lại cách 60 giây) → `/verify-otp` → `active=true`.
- Đăng nhập: so khớp mật khẩu với bản băm; tài khoản chưa kích hoạt sẽ được gửi OTP mới và chuyển sang `/verify-otp`. Đăng xuất: huỷ session + xoá cookie "Nhớ tôi".
- Quên mật khẩu: `/forgot-password` (nhập email) → OTP → `/reset-password` (OTP + mật khẩu mới).
- Lần chạy đầu, mật khẩu plain text cũ trong DB (nếu có) tự được băm lại.
