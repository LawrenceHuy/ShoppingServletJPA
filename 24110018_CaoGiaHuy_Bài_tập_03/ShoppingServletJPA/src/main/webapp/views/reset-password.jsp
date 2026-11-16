<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đặt lại mật khẩu</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container" style="max-width:420px; margin-top:60px;">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Đặt lại mật khẩu</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <c:if test="${info != null}"><div class="alert alert-success"><c:out value="${info}"/></div></c:if>
    <p>Nếu <b><c:out value="${email}"/></b> đã đăng ký, mã OTP 6 chữ số đã được gửi tới email này (hiệu lực 5 phút).</p>
    <form action="${pageContext.request.contextPath}/reset-password" method="post">
      <div class="mb-3">
        <input type="text" name="otp" class="form-control text-center fs-4" maxlength="6" inputmode="numeric"
               pattern="[0-9]{6}" placeholder="______" autocomplete="one-time-code" autofocus>
      </div>
      <div class="mb-3"><input type="password" name="password" class="form-control" placeholder="Mật khẩu mới" autocomplete="new-password"></div>
      <div class="mb-3"><input type="password" name="repassword" class="form-control" placeholder="Nhập lại mật khẩu mới" autocomplete="new-password"></div>
      <button type="submit" class="btn btn-primary w-100">Đổi mật khẩu</button>
      <button type="submit" name="action" value="resend" class="btn btn-link w-100 mt-2">Gửi lại mã</button>
    </form>
    <p class="text-center mt-3 mb-0"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
  </div></div>
</div>
</body>
</html>
