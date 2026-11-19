<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Hồ sơ cá nhân</title>
</head>
<body>
<div class="mx-auto" style="max-width:560px">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h4 class="text-center mb-3">Hồ sơ cá nhân</h4>
    <c:if test="${param.updated != null}"><div class="alert alert-success">Cập nhật hồ sơ thành công!</div></c:if>
    <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>

    <div class="text-center mb-3">
      <c:choose>
        <c:when test="${not empty user.avatarPath}">
          <c:url var="avatarUrl" value="${user.avatarPath}"/>
          <img src="${avatarUrl}" alt="avatar" class="rounded-circle border" style="width:120px;height:120px;object-fit:cover">
        </c:when>
        <c:otherwise>
          <div class="rounded-circle bg-secondary text-white d-inline-flex align-items-center justify-content-center" style="width:120px;height:120px;font-size:2.5rem">
            <c:out value="${fn:toUpperCase(fn:substring(user.userName, 0, 1))}"/>
          </div>
        </c:otherwise>
      </c:choose>
      <div class="text-muted small mt-1">@<c:out value="${user.userName}"/></div>
    </div>

    <form action="<c:url value='/profile'/>" method="post" enctype="multipart/form-data" novalidate>
      <div class="mb-3">
        <label class="form-label" for="fullname">Họ tên</label>
        <input class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}" type="text" id="fullname" name="fullname"
               value="<c:out value='${empty errors ? user.fullName : param.fullname}'/>">
        <div class="invalid-feedback"><c:out value="${errors.fullname}"/></div>
      </div>
      <div class="mb-3">
        <label class="form-label" for="phone">Số điện thoại</label>
        <input class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" type="text" id="phone" name="phone" inputmode="tel"
               value="<c:out value='${empty errors ? user.phone : param.phone}'/>">
        <div class="invalid-feedback"><c:out value="${errors.phone}"/></div>
      </div>
      <div class="mb-3">
        <label class="form-label" for="email">Email</label>
        <input class="form-control ${not empty errors.email ? 'is-invalid' : ''}" type="email" id="email" name="email"
               value="<c:out value='${empty errors ? user.email : param.email}'/>">
        <div class="invalid-feedback"><c:out value="${errors.email}"/></div>
        <div class="form-text">Nếu đổi email, hệ thống sẽ gửi mã OTP tới email mới để xác nhận.</div>
      </div>
      <div class="mb-3">
        <label class="form-label" for="avatarFile">Ảnh đại diện</label>
        <input class="form-control ${not empty errors.avatarFile ? 'is-invalid' : ''}" type="file" id="avatarFile" name="avatarFile" accept="image/*">
        <div class="invalid-feedback"><c:out value="${errors.avatarFile}"/></div>
        <div class="form-text">
          jpg, jpeg, png, gif, webp &mdash; tối đa 5MB.
          <c:if test="${not cloudinary}">(Chưa cấu hình Cloudinary nên ảnh đang được lưu cục bộ trên server.)</c:if>
        </div>
      </div>
      <button type="submit" class="btn btn-primary w-100">Lưu thay đổi</button>
    </form>
  </div></div>
</div>
</body>
</html>
