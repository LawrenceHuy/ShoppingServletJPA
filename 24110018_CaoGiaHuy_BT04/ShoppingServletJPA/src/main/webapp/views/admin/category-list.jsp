<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Category list</title>
</head>
<body>
<div>
  <h3 class="text-danger">Quản lý danh mục</h3>
  <div class="d-flex justify-content-between mb-3">
    <a class="btn btn-success" href="<c:url value="/admin/category/add"/>">Add Category</a>
    <form class="d-flex" action="<c:url value="/admin/categories"/>" method="get">
      <input class="form-control me-2" name="keyword" value="<c:out value='${keyword}'/>" placeholder="Tìm theo tên">
      <button class="btn btn-outline-primary">Tìm</button>
    </form>
  </div>
  <table class="table table-bordered table-striped align-middle">
    <tr><th>STT</th><th>Images</th><th>Category name</th><th>Status</th><th>Action</th></tr>
    <c:forEach items="${listcate}" var="cate" varStatus="STT">
      <tr>
        <td>${STT.index + 1}</td>
        <c:choose>
          <c:when test="${fn:startsWith(cate.images, 'http')}">
            <c:url value="${cate.images}" var="imgUrl"/>
          </c:when>
          <c:otherwise>
            <c:url value="/image?fname=${cate.images}" var="imgUrl"/>
          </c:otherwise>
        </c:choose>
        <td><img height="100" width="130" style="object-fit:contain" src="${imgUrl}" alt=""/></td>
        <td><c:out value="${cate.categoryname}"/></td>
        <td>
          <c:if test="${cate.status == 1}">Hoạt động</c:if>
          <c:if test="${cate.status != 1}">Khóa</c:if>
        </td>
        <td>
          <a href="<c:url value='/admin/category/edit?id=${cate.categoryid}'/>">Sửa</a> |
          <a href="<c:url value='/admin/category/delete?id=${cate.categoryid}'/>"
             onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</a>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty listcate}"><tr><td colspan="5" class="text-center">Không có dữ liệu</td></tr></c:if>
  </table>
</div>
</body>
</html>
