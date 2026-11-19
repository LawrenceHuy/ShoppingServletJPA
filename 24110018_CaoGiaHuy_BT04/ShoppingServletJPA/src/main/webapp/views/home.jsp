<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Trang chủ</title>
</head>
<body>
<h3 class="mb-3">Video mới nhất</h3>
<jsp:include page="/WEB-INF/fragments/video-player.jsp"/>
<jsp:include page="/WEB-INF/fragments/video-grid.jsp">
  <jsp:param name="base" value="/home"/>
  <jsp:param name="owner" value="false"/>
</jsp:include>
</body>
</html>
