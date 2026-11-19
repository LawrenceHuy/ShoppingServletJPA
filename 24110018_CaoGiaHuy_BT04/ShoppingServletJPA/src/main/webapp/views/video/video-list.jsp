<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Video của tôi</title>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-3">
  <h3 class="mb-0">Video của tôi <small class="text-muted fs-6">(${total} video)</small></h3>
  <a class="btn btn-success" href="<c:url value='/video/add'/>">+ Thêm video</a>
</div>

<jsp:include page="/WEB-INF/fragments/video-player.jsp"/>
<jsp:include page="/WEB-INF/fragments/video-grid.jsp">
  <jsp:param name="base" value="/video"/>
  <jsp:param name="owner" value="true"/>
</jsp:include>

<%-- Phân trang: 6 video / trang --%>
<c:if test="${totalPages > 1}">
  <nav class="mt-4" aria-label="Phân trang video">
    <ul class="pagination justify-content-center">
      <li class="page-item ${page <= 1 ? 'disabled' : ''}">
        <a class="page-link" href="<c:url value='/video'><c:param name='page' value='${page - 1}'/></c:url>">&laquo;</a>
      </li>
      <c:forEach begin="1" end="${totalPages}" var="i">
        <li class="page-item ${i == page ? 'active' : ''}">
          <a class="page-link" href="<c:url value='/video'><c:param name='page' value='${i}'/></c:url>">${i}</a>
        </li>
      </c:forEach>
      <li class="page-item ${page >= totalPages ? 'disabled' : ''}">
        <a class="page-link" href="<c:url value='/video'><c:param name='page' value='${page + 1}'/></c:url>">&raquo;</a>
      </li>
    </ul>
  </nav>
</c:if>
</body>
</html>
