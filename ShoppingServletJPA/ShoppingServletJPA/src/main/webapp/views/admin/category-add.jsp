<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Add Category</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<%@ include file="/common/topbar.jsp" %>
<div class="container" style="max-width:640px;">
  <h3>Thêm danh mục</h3>
  <c:if test="${alert != null}"><div class="alert alert-danger">${alert}</div></c:if>
  <form action="<c:url value="/admin/category/insert"/>" method="post" enctype="multipart/form-data">
    <div class="mb-3">
      <label class="form-label" for="categoryname">Category name:</label>
      <input class="form-control" type="text" id="categoryname" name="categoryname">
    </div>
    <div class="mb-3">
      <label class="form-label" for="images">Link images:</label>
      <input class="form-control" type="text" id="images" name="images">
    </div>
    <div class="mb-3">
      <label class="form-label" for="images1">Upload images:</label>
      <input class="form-control" type="file" id="images1" name="images1" accept="image/*">
    </div>
    <div class="mb-3">
      <label class="form-label d-block">Status</label>
      <input type="radio" id="ston" name="status" value="1" checked> <label for="ston">Hoạt động</label>
      <input type="radio" id="stoff" name="status" value="0" class="ms-3"> <label for="stoff">Khóa</label>
    </div>
    <button type="submit" class="btn btn-primary">Insert</button>
    <a class="btn btn-link" href="<c:url value="/admin/categories"/>">Quay lại</a>
  </form>
</div>
</body>
</html>
