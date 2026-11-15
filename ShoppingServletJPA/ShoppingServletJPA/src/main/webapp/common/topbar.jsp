<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-dark bg-primary px-3 mb-4">
  <a class="navbar-brand" href="${pageContext.request.contextPath}/waiting">Shopping MVC</a>
  <div class="text-white">
    <c:choose>
      <c:when test="${sessionScope.account == null}">
        <a class="text-white" href="${pageContext.request.contextPath}/login">Đăng nhập</a> |
        <a class="text-white" href="${pageContext.request.contextPath}/register">Đăng ký</a>
      </c:when>
      <c:otherwise>
        Xin chào <b>${sessionScope.account.fullName}</b> |
        <c:if test="${sessionScope.account.roleid == 1}">
          <a class="text-white" href="${pageContext.request.contextPath}/admin/categories">Quản lý danh mục</a> |
        </c:if>
        <a class="text-white" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
      </c:otherwise>
    </c:choose>
  </div>
</nav>
