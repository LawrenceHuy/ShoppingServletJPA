<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Manager</title>
</head>
<body>
<h3>Trang quản lý (Manager)</h3>
<p>Xin chào <c:out value="${sessionScope.account.userName}"/> (roleid = ${sessionScope.account.roleid}).</p>
</body>
</html>
