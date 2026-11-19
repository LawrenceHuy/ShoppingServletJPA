# ShoppingServletJPA

Bài tập JPA (Hibernate) với Servlet/JSP — SQL Server.

Nội dung:
1. **Bài tập 01 với JPA**: Login (Session + Cookie "Nhớ tôi"), Register, phân quyền admin/manager/user — tầng DAO dùng JPA thay JDBC.
2. **Cấu hình và test JPA**: `persistence.xml`, entity `Category` / `Video` / `User`, `JpaConfig`, lớp `vn.iotstar.config.Test`.
3. **CRUD Category bằng JPA**: Thêm / Sửa / Xóa / Tìm kiếm danh mục (có upload ảnh hoặc dán link ảnh).

4. **Bài tập 02**: Kích hoạt tài khoản bằng OTP qua email khi đăng ký; đăng nhập với mật khẩu băm (PBKDF2-HMAC-SHA256 + salt) và đăng xuất; quên mật khẩu bằng OTP qua email.

5. **Bài tập 03**: Sitemesh 3 + Bootstrap, validation form, profile (upload Cloudinary, đổi email bằng OTP), bảng Videos (quan hệ 1-n với Users).

## Chạy
1. SSMS: chạy `sql/create-database.sql` (chỉ tạo database rỗng `ShoppingServletJPA`; bảng do Hibernate tự tạo).
2. Sửa port / user / password trong `src/main/resources/META-INF/persistence.xml`.
3. Cấu hình gửi email: copy `src/main/resources/mail.properties.example` thành `mail.properties` rồi điền Gmail + App password (hoặc đặt biến môi trường `MAIL_USERNAME`, `MAIL_PASSWORD`). Nếu chưa cấu hình, OTP được in ra console Tomcat (`[DEV][OTP ...]`) để test.
3b. (Tuỳ chọn) chạy thử JPA: chuột phải `vn.iotstar.config.Test` → Run As → Java Application.
3c. Upload ảnh/video lên Cloudinary: copy `src/main/resources/cloudinary.properties.example` thành `cloudinary.properties` rồi điền cloud_name / api_key / api_secret (hoặc đặt biến môi trường `CLOUDINARY_URL`). Chưa cấu hình thì file được lưu cục bộ trong thư mục `~/upload` (xem qua `/image?fname=`).
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

## Bài tập 03
**1. Sitemesh 3** — `vn.iotstar.filter.SiteMeshConfig` (đăng ký trong `web.xml`, sau `EncodingFilter`) gắn decorator `/WEB-INF/decorators/main.jsp` (Bootstrap 5: navbar, container, footer) cho mọi trang. Các trang nội dung chỉ còn `<title>` + `<body>`; Sitemesh ghép vào `<sitemesh:write property="title|head|body"/>`. `/image` được loại trừ.

**2. Validation** — Jakarta Bean Validation (Hibernate Validator) trên các form bean ở `vn.iotstar.form` (`LoginForm`, `RegisterForm`, `ForgotPasswordForm`, `ResetPasswordForm`, `OtpForm`, `CategoryForm`, `ProfileForm`, `VideoForm`). `ValidationUtil.validate(form)` trả về `Map<field, message>`; JSP hiển thị lỗi dưới từng ô (`is-invalid` + `invalid-feedback`). Lỗi nhập lại mật khẩu có key `repasswordMatch`.

**3. Profile** (`/profile`, `/profile/verify-email`) — sửa fullname, phone, email, ảnh đại diện (multipart → Cloudinary). Đổi email: OTP gửi tới **email mới**, nhập đúng OTP mới lưu toàn bộ thay đổi (chờ trong session, có "Gửi lại mã" / "Hủy").

**4. Videos** — entity `Video` có `@ManyToOne User` (và `User` có `@OneToMany videos`). `/home` hiển thị 10 video mới nhất; `/video` (cần đăng nhập) liệt kê video của user, **6 video/trang**; bấm tiêu đề → `?vid=<id>` hiện trình phát (YouTube iframe hoặc thẻ `<video>`) và tăng lượt xem. CRUD: `/video/add|insert|edit|update|delete` (upload poster/video bằng `@MultipartConfig` + `Part`, đẩy lên Cloudinary).
