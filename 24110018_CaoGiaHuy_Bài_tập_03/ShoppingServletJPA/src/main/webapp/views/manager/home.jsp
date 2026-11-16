<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Manager</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/common/topbar.jsp" %>
<div class="container">
  <h3>Trang quản lý (Manager)</h3>
  <p>Xin chào ${sessionScope.account.userName} (roleid = ${sessionScope.account.roleid}).</p>
</div>
</body>
</html>
