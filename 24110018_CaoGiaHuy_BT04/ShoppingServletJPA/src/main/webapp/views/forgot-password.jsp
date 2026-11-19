<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Quên mật khẩu</title>
</head>
<body>
<div class="mx-auto" style="max-width:440px">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Quên mật khẩu</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <p>Nhập email đã đăng ký, chúng tôi sẽ gửi mã OTP để đặt lại mật khẩu.</p>
    <form action="${pageContext.request.contextPath}/forgot-password" method="post" novalidate>
  <div class="mb-3">
    <input type="email" name="email" value="<c:out value="${param.email}"/>" class="form-control ${not empty errors.email ? 'is-invalid' : ''}" placeholder="Email" autofocus>
    <div class="invalid-feedback"><c:out value="${errors.email}"/></div>
  </div>
      <button type="submit" class="btn btn-primary w-100">Gửi mã OTP</button>
    </form>
    <p class="text-center mt-3 mb-0"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
  </div></div>
</div>
</body>
</html>
