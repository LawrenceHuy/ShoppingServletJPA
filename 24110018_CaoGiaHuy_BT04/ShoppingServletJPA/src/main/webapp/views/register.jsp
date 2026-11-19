<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Đăng ký</title>
</head>
<body>
<div class="mx-auto" style="max-width:480px">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Tạo tài khoản mới</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <form action="${pageContext.request.contextPath}/register" method="post" novalidate>
  <div class="mb-3">
    <input type="text" name="username" value="<c:out value="${param.username}"/>" class="form-control ${not empty errors.username ? 'is-invalid' : ''}" placeholder="Tài khoản (4-30 ký tự)" >
    <div class="invalid-feedback"><c:out value="${errors.username}"/></div>
  </div>
  <div class="mb-3">
    <input type="text" name="fullname" value="<c:out value="${param.fullname}"/>" class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}" placeholder="Họ tên" >
    <div class="invalid-feedback"><c:out value="${errors.fullname}"/></div>
  </div>
  <div class="mb-3">
    <input type="email" name="email" value="<c:out value="${param.email}"/>" class="form-control ${not empty errors.email ? 'is-invalid' : ''}" placeholder="Nhập Email" >
    <div class="invalid-feedback"><c:out value="${errors.email}"/></div>
  </div>
  <div class="mb-3">
    <input type="text" name="phone" value="<c:out value="${param.phone}"/>" class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" placeholder="Số điện thoại (không bắt buộc)" inputmode="tel">
    <div class="invalid-feedback"><c:out value="${errors.phone}"/></div>
  </div>
  <div class="mb-3">
    <input type="password" name="password" class="form-control ${not empty errors.password ? 'is-invalid' : ''}" placeholder="Mật khẩu (từ 6 ký tự)" >
    <div class="invalid-feedback"><c:out value="${errors.password}"/></div>
  </div>
  <div class="mb-3">
    <input type="password" name="repassword" class="form-control ${not empty errors.repassword or not empty errors.repasswordMatch ? 'is-invalid' : ''}" placeholder="Nhập lại mật khẩu" >
    <div class="invalid-feedback"><c:out value="${not empty errors.repassword ? errors.repassword : errors.repasswordMatch}"/></div>
  </div>
      <button type="submit" class="btn btn-primary w-100">Tạo tài khoản</button>
    </form>
    <p class="text-center mt-3 mb-0">Nếu bạn đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
  </div></div>
</div>
</body>
</html>
