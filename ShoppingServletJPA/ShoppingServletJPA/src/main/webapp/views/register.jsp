<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đăng ký</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container" style="max-width:460px; margin-top:50px;">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Tạo tài khoản mới</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger">${alert}</div></c:if>
    <form action="${pageContext.request.contextPath}/register" method="post">
      <div class="mb-3"><input type="text" name="username" value="${param.username}" class="form-control" placeholder="Tài khoản"></div>
      <div class="mb-3"><input type="text" name="fullname" value="${param.fullname}" class="form-control" placeholder="Họ tên"></div>
      <div class="mb-3"><input type="email" name="email" value="${param.email}" class="form-control" placeholder="Nhập Email"></div>
      <div class="mb-3"><input type="text" name="phone" value="${param.phone}" class="form-control" placeholder="Số điện thoại"></div>
      <div class="mb-3"><input type="password" name="password" class="form-control" placeholder="Mật khẩu"></div>
      <div class="mb-3"><input type="password" name="repassword" class="form-control" placeholder="Nhập lại mật khẩu"></div>
      <button type="submit" class="btn btn-primary w-100">Tạo tài khoản</button>
    </form>
    <p class="text-center mt-3 mb-0">Nếu bạn đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
  </div></div>
</div>
</body>
</html>
