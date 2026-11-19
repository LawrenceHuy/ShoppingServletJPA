<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Decorator Sitemesh 3 (Bootstrap 5). Các thẻ <sitemesh:write> được Sitemesh thay bằng nội dung của từng trang. --%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title"/> | Shopping MVC</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
<style>
  .avatar-sm { width: 32px; height: 32px; object-fit: cover; border-radius: 50%; }
  .video-poster { height: 160px; object-fit: cover; width: 100%; background: #212529; }
</style>
<sitemesh:write property="head"/>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/waiting">Shopping MVC</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false" aria-label="Menu">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="mainNav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
        <c:if test="${sessionScope.account != null}">
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/video">Video của tôi</a></li>
        </c:if>
        <c:if test="${sessionScope.account.roleid == 1}">
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/categories">Quản lý danh mục</a></li>
        </c:if>
      </ul>
      <ul class="navbar-nav ms-auto align-items-lg-center">
        <c:choose>
          <c:when test="${sessionScope.account == null}">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/register">Đăng ký</a></li>
          </c:when>
          <c:otherwise>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle d-flex align-items-center gap-2" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <c:if test="${not empty sessionScope.account.avatarPath}">
                  <c:url var="navAvatar" value="${sessionScope.account.avatarPath}"/>
                  <img class="avatar-sm" src="${navAvatar}" alt="">
                </c:if>
                <span><c:out value="${empty sessionScope.account.fullName ? sessionScope.account.userName : sessionScope.account.fullName}"/></span>
              </a>
              <ul class="dropdown-menu dropdown-menu-end">
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Hồ sơ cá nhân</a></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/video">Video của tôi</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
              </ul>
            </li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
</nav>

<main class="container py-4 flex-grow-1">
  <sitemesh:write property="body"/>
</main>

<footer class="bg-dark text-white-50 text-center py-3 mt-auto">
  <small>&copy; Shopping MVC &mdash; Lập trình Web (Servlet/JSP + JPA + Sitemesh)</small>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
