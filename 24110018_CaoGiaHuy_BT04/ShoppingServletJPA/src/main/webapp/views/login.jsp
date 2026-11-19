<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Đăng nhập</title>
</head>
<body>
<div class="mx-auto" style="max-width:420px">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Đăng Nhập Vào Hệ Thống</h4>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
    <c:if test="${param.activated != null}"><div class="alert alert-success">Kích hoạt tài khoản thành công, hãy đăng nhập!</div></c:if>
    <c:if test="${param.reset != null}"><div class="alert alert-success">Đổi mật khẩu thành công, hãy đăng nhập bằng mật khẩu mới!</div></c:if>
    <form action="${pageContext.request.contextPath}/login" method="post" novalidate>
  <div class="mb-3">
    <input type="text" name="username" value="<c:out value="${param.username}"/>" class="form-control ${not empty errors.username ? 'is-invalid' : ''}" placeholder="Tài khoản" autofocus>
    <div class="invalid-feedback"><c:out value="${errors.username}"/></div>
  </div>
  <div class="mb-3">
    <input type="password" name="password" class="form-control ${not empty errors.password ? 'is-invalid' : ''}" placeholder="Mật khẩu" >
    <div class="invalid-feedback"><c:out value="${errors.password}"/></div>
  </div>
      <div class="form-check mb-3">
        <input class="form-check-input" type="checkbox" name="remember" id="remember">
        <label class="form-check-label" for="remember">Nhớ tôi</label>
      </div>
      <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
    </form>
    <p class="text-center mt-3 mb-1"><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></p>
    <p class="text-center mb-0">Nếu bạn chưa có tài khoản, hãy <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
  </div></div>
</div>
</body>
</html>
