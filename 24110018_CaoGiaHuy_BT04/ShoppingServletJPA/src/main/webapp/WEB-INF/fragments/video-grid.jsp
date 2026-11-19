<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Lưới video. Tham số: base = "/home" hoặc "/video"; owner = true nếu hiển thị nút Sửa/Xóa. --%>
<c:set var="isOwner" value="${param.owner == 'true'}"/>
<c:if test="${empty videos}">
  <div class="alert alert-info">Chưa có video nào.</div>
</c:if>
<div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-3">
  <c:forEach items="${videos}" var="v">
    <c:url var="watchUrl" value="${param.base}">
      <c:param name="vid" value="${v.videoId}"/>
      <c:if test="${isOwner and page != null}"><c:param name="page" value="${page}"/></c:if>
    </c:url>
    <div class="col">
      <div class="card h-100 shadow-sm ${current != null and current.videoId == v.videoId ? 'border-primary' : ''}">
        <a href="${watchUrl}">
          <c:choose>
            <c:when test="${not empty v.posterPath}">
              <c:url var="posterUrl" value="${v.posterPath}"/>
              <img class="video-poster card-img-top" src="${posterUrl}" alt="">
            </c:when>
            <c:otherwise>
              <div class="video-poster card-img-top d-flex align-items-center justify-content-center text-white-50">Không có poster</div>
            </c:otherwise>
          </c:choose>
        </a>
        <div class="card-body">
          <h6 class="card-title mb-1">
            <a class="text-decoration-none" href="${watchUrl}"><c:out value="${v.title}"/></a>
            <c:if test="${v.active != 1}"><span class="badge text-bg-secondary">Ẩn</span></c:if>
          </h6>
          <p class="text-muted small mb-0">
            <c:out value="${empty v.user.fullName ? v.user.userName : v.user.fullName}"/> &bull; ${v.views} lượt xem
          </p>
        </div>
        <c:if test="${isOwner}">
          <div class="card-footer bg-white d-flex gap-2">
            <a class="btn btn-sm btn-outline-primary" href="<c:url value='/video/edit'><c:param name='id' value='${v.videoId}'/></c:url>">Sửa</a>
            <form action="<c:url value='/video/delete'/>" method="post" class="d-inline"
                  onsubmit="return confirm('Bạn có chắc muốn xóa video này?')">
              <input type="hidden" name="videoId" value="${v.videoId}">
              <button type="submit" class="btn btn-sm btn-outline-danger">Xóa</button>
            </form>
          </div>
        </c:if>
      </div>
    </div>
  </c:forEach>
</div>
