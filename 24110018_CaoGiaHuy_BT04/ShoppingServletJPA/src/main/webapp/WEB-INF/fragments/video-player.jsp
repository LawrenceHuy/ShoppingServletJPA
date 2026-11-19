<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Chi tiết + trình phát 1 video (khi có ?vid=...). Dùng chung cho /home và /video. --%>
<c:if test="${not empty current}">
  <div class="card shadow-sm mb-4">
    <div class="ratio ratio-16x9 bg-dark">
      <c:choose>
        <c:when test="${current.youtube}">
          <iframe src="${current.embedUrl}" title="<c:out value='${current.title}'/>" allowfullscreen
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"></iframe>
        </c:when>
        <c:when test="${not empty current.videoPath}">
          <c:url var="videoSrc" value="${current.videoPath}"/>
          <c:url var="posterSrc" value="${current.posterPath}"/>
          <video controls autoplay playsinline src="${videoSrc}" poster="${posterSrc}"></video>
        </c:when>
        <c:otherwise>
          <div class="d-flex align-items-center justify-content-center text-white-50">Video này chưa có nguồn phát</div>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="card-body">
      <h4 class="card-title"><c:out value="${current.title}"/></h4>
      <p class="text-muted small mb-2">
        Đăng bởi <b><c:out value="${empty current.user.fullName ? current.user.userName : current.user.fullName}"/></b>
        &bull; ${current.views} lượt xem
        <c:if test="${not empty current.createdDateText}">&bull; ${current.createdDateText}</c:if>
      </p>
      <p class="card-text"><c:out value="${current.description}"/></p>
    </div>
  </div>
</c:if>
