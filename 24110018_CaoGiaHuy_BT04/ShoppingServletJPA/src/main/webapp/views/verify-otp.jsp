<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Kích hoạt tài khoản</title>
</head>
<body>
<div class="mx-auto" style="max-width:440px">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Kích hoạt tài khoản</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <c:if test="${info != null}"><div class="alert alert-success"><c:out value="${info}"/></div></c:if>
    <p>Mã OTP gồm 6 chữ số đã được gửi tới <b><c:out value="${email}"/></b>. Mã có hiệu lực trong 5 phút.</p>
    <form action="${pageContext.request.contextPath}/verify-otp" method="post" novalidate>
  <div class="mb-3">
    <input type="text" name="otp" value="<c:out value="${param.otp}"/>" class="form-control text-center fs-4 ${not empty errors.otp ? 'is-invalid' : ''}" placeholder="______" maxlength="6" inputmode="numeric" autocomplete="one-time-code" autofocus>
    <div class="invalid-feedback"><c:out value="${errors.otp}"/></div>
  </div>
      <button type="submit" class="btn btn-primary w-100">Xác nhận</button>
      <button type="submit" name="action" value="resend" class="btn btn-link w-100 mt-2">Gửi lại mã</button>
    </form>
    <p class="text-center mt-3 mb-0"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
  </div></div>
</div>
</body>
</html>
