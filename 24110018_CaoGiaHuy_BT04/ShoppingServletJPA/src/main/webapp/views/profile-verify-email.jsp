<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Xác nhận email mới</title>
</head>
<body>
<div class="mx-auto" style="max-width:440px">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Xác nhận email mới</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <c:if test="${info != null}"><div class="alert alert-success"><c:out value="${info}"/></div></c:if>
    <p>Mã OTP gồm 6 chữ số đã được gửi tới <b><c:out value="${newEmail}"/></b> (hiệu lực 5 phút). Nhập đúng mã để hoàn tất cập nhật hồ sơ.</p>
    <form action="<c:url value='/profile/verify-email'/>" method="post" novalidate>
      <div class="mb-3">
        <input type="text" name="otp" class="form-control text-center fs-4 ${not empty errors.otp ? 'is-invalid' : ''}" maxlength="6"
               inputmode="numeric" autocomplete="one-time-code" placeholder="______" autofocus>
        <div class="invalid-feedback"><c:out value="${errors.otp}"/></div>
      </div>
      <button type="submit" class="btn btn-primary w-100">Xác nhận</button>
      <button type="submit" name="action" value="resend" class="btn btn-link w-100 mt-2">Gửi lại mã</button>
      <button type="submit" name="action" value="cancel" class="btn btn-outline-secondary w-100 mt-1">Hủy thay đổi</button>
    </form>
  </div></div>
</div>
</body>
</html>
