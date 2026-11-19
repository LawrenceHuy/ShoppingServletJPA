<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Đặt lại mật khẩu</title>
</head>
<body>
<div class="mx-auto" style="max-width:440px">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Đặt lại mật khẩu</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <c:if test="${info != null}"><div class="alert alert-success"><c:out value="${info}"/></div></c:if>
    <p>Nếu <b><c:out value="${email}"/></b> đã đăng ký, mã OTP 6 chữ số đã được gửi tới email này (hiệu lực 5 phút).</p>
    <form action="${pageContext.request.contextPath}/reset-password" method="post" novalidate>
  <div class="mb-3">
    <input type="text" name="otp" value="<c:out value="${param.otp}"/>" class="form-control text-center fs-4 ${not empty errors.otp ? 'is-invalid' : ''}" placeholder="______" maxlength="6" inputmode="numeric" autocomplete="one-time-code" autofocus>
    <div class="invalid-feedback"><c:out value="${errors.otp}"/></div>
  </div>
  <div class="mb-3">
    <input type="password" name="password" class="form-control ${not empty errors.password ? 'is-invalid' : ''}" placeholder="Mật khẩu mới (từ 6 ký tự)" autocomplete="new-password">
    <div class="invalid-feedback"><c:out value="${errors.password}"/></div>
  </div>
  <div class="mb-3">
    <input type="password" name="repassword" class="form-control ${not empty errors.repassword or not empty errors.repasswordMatch ? 'is-invalid' : ''}" placeholder="Nhập lại mật khẩu mới" autocomplete="new-password">
    <div class="invalid-feedback"><c:out value="${not empty errors.repassword ? errors.repassword : errors.repasswordMatch}"/></div>
  </div>
      <button type="submit" class="btn btn-primary w-100">Đổi mật khẩu</button>
      <button type="submit" name="action" value="resend" class="btn btn-link w-100 mt-2">Gửi lại mã</button>
    </form>
    <p class="text-center mt-3 mb-0"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
  </div></div>
</div>
</body>
</html>
