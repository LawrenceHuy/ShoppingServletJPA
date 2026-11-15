<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đăng nhập</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container" style="max-width:420px; margin-top:80px;">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Đăng Nhập Vào Hệ Thống</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger">${alert}</div></c:if>
    <c:if test="${param.registered != null}"><div class="alert alert-success">Đăng ký thành công, hãy đăng nhập!</div></c:if>
    <form action="${pageContext.request.contextPath}/login" method="post">
      <div class="mb-3"><input type="text" name="username" class="form-control" placeholder="Tài khoản"></div>
      <div class="mb-3"><input type="password" name="password" class="form-control" placeholder="Mật khẩu"></div>
      <div class="form-check mb-3">
        <input class="form-check-input" type="checkbox" name="remember" id="remember">
        <label class="form-check-label" for="remember">Nhớ tôi</label>
      </div>
      <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
    </form>
    <p class="text-center mt-3 mb-0">Nếu bạn chưa có tài khoản, hãy <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
  </div></div>
</div>
</body>
</html>
