<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Edit Category</title>
</head>
<body>
<div class="mx-auto" style="max-width:640px">
  <h3>Chỉnh sửa danh mục</h3>
  <c:if test="${alert != null}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>
  <form action="<c:url value="/admin/category/update"/>" method="post" enctype="multipart/form-data" novalidate>
    <input type="hidden" name="categoryid" value="${cate.categoryid}">
    <div class="mb-3">
      <label class="form-label" for="categoryname">Category name:</label>
      <input class="form-control ${not empty errors.categoryname ? 'is-invalid' : ''}" type="text" id="categoryname" name="categoryname" value="<c:out value="${cate.categoryname}"/>">
      <div class="invalid-feedback"><c:out value="${errors.categoryname}"/></div>
    </div>
    <div class="mb-3">
      <label class="form-label" for="images">Link images:</label>
      <input class="form-control ${not empty errors.images ? 'is-invalid' : ''}" type="text" id="images" name="images" value="<c:out value="${cate.images}"/>">
      <div class="invalid-feedback"><c:out value="${errors.images}"/></div>
    </div>
    <c:choose>
      <c:when test="${fn:startsWith(cate.images, 'http')}"><c:url value="${cate.images}" var="imgUrl"/></c:when>
      <c:otherwise><c:url value="/image?fname=${cate.images}" var="imgUrl"/></c:otherwise>
    </c:choose>
    <img height="150" width="200" style="object-fit:contain" src="${imgUrl}" alt=""/><br>
    <div class="mb-3">
      <label class="form-label" for="images1">Upload images (bỏ trống nếu giữ ảnh cũ):</label>
      <input class="form-control" type="file" id="images1" name="images1" accept="image/*">
    </div>
    <div class="mb-3">
      <label class="form-label d-block">Status</label>
      <input type="radio" id="ston" name="status" value="1" ${cate.status == 1 ? 'checked' : ''}> <label for="ston">Hoạt động</label>
      <input type="radio" id="stoff" name="status" value="0" class="ms-3" ${cate.status != 1 ? 'checked' : ''}> <label for="stoff">Khóa</label>
    </div>
    <button type="submit" class="btn btn-primary">Update</button>
    <a class="btn btn-link" href="<c:url value="/admin/categories"/>">Quay lại</a>
  </form>
</div>
</body>
</html>
