<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Quên mật khẩu</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container" style="max-width:420px; margin-top:80px;">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Quên mật khẩu</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <p>Nhập email đã đăng ký, chúng tôi sẽ gửi mã OTP để đặt lại mật khẩu.</p>
    <form action="${pageContext.request.contextPath}/forgot-password" method="post">
      <div class="mb-3"><input type="email" name="email" class="form-control" placeholder="Email" required autofocus></div>
      <button type="submit" class="btn btn-primary w-100">Gửi mã OTP</button>
    </form>
    <p class="text-center mt-3 mb-0"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
  </div></div>
</div>
</body>
</html>
