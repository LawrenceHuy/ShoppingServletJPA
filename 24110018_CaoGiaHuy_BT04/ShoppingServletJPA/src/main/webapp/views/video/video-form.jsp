<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="isNew" value="${empty video.videoId}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>${isNew ? 'Thêm video' : 'Sửa video'}</title>
</head>
<body>
<div class="mx-auto" style="max-width:720px">
  <h3>${isNew ? 'Thêm video' : 'Chỉnh sửa video'}</h3>
  <form action="<c:url value='${isNew ? "/video/insert" : "/video/update"}'/>" method="post" enctype="multipart/form-data" novalidate>
    <c:if test="${not isNew}"><input type="hidden" name="videoId" value="<c:out value='${video.videoId}'/>"></c:if>

    <div class="mb-3">
      <label class="form-label" for="title">Tiêu đề</label>
      <input class="form-control ${not empty errors.title ? 'is-invalid' : ''}" type="text" id="title" name="title" maxlength="200" value="<c:out value='${video.title}'/>">
      <div class="invalid-feedback"><c:out value="${errors.title}"/></div>
    </div>

    <div class="mb-3">
      <label class="form-label" for="description">Mô tả</label>
      <textarea class="form-control ${not empty errors.description ? 'is-invalid' : ''}" id="description" name="description" rows="3" maxlength="500"><c:out value="${video.description}"/></textarea>
      <div class="invalid-feedback"><c:out value="${errors.description}"/></div>
    </div>

    <div class="mb-3">
      <label class="form-label" for="videoUrl">Link video (YouTube hoặc link .mp4)</label>
      <input class="form-control ${not empty errors.videoUrl ? 'is-invalid' : ''}" type="text" id="videoUrl" name="videoUrl"
             value="<c:out value='${fn:startsWith(video.videoUrl, "http") ? video.videoUrl : ""}'/>" placeholder="https://www.youtube.com/watch?v=...">
      <div class="invalid-feedback"><c:out value="${errors.videoUrl}"/></div>
    </div>
    <div class="mb-3">
      <label class="form-label" for="videoFile">Hoặc upload file video (mp4, webm, ogg, mov &mdash; tối đa 100MB)</label>
      <input class="form-control ${not empty errors.videoFile ? 'is-invalid' : ''}" type="file" id="videoFile" name="videoFile" accept="video/*">
      <div class="invalid-feedback"><c:out value="${errors.videoFile}"/></div>
      <c:if test="${not isNew and not empty video.videoUrl}"><div class="form-text">Bỏ trống để giữ video hiện tại.</div></c:if>
    </div>

    <div class="mb-3">
      <label class="form-label" for="poster">Link poster (ảnh bìa)</label>
      <input class="form-control ${not empty errors.poster ? 'is-invalid' : ''}" type="text" id="poster" name="poster"
             value="<c:out value='${fn:startsWith(video.poster, "http") ? video.poster : ""}'/>" placeholder="https://...">
      <div class="invalid-feedback"><c:out value="${errors.poster}"/></div>
    </div>
    <c:if test="${not empty video.posterPath}">
      <c:url var="posterUrl" value="${video.posterPath}"/>
      <img src="${posterUrl}" alt="" height="120" class="mb-2 rounded"><br>
    </c:if>
    <div class="mb-3">
      <label class="form-label" for="posterFile">Hoặc upload ảnh poster</label>
      <input class="form-control ${not empty errors.posterFile ? 'is-invalid' : ''}" type="file" id="posterFile" name="posterFile" accept="image/*">
      <div class="invalid-feedback"><c:out value="${errors.posterFile}"/></div>
      <c:if test="${not isNew}"><div class="form-text">Bỏ trống để giữ poster hiện tại.</div></c:if>
    </div>

    <div class="mb-3">
      <label class="form-label d-block">Trạng thái</label>
      <input type="radio" id="actOn" name="active" value="1" ${video.active != 0 or isNew ? 'checked' : ''}> <label for="actOn">Hiển thị</label>
      <input type="radio" id="actOff" name="active" value="0" class="ms-3" ${video.active == 0 and not isNew ? 'checked' : ''}> <label for="actOff">Ẩn</label>
    </div>

    <button type="submit" class="btn btn-primary">${isNew ? 'Thêm' : 'Cập nhật'}</button>
    <a class="btn btn-link" href="<c:url value='/video'/>">Quay lại</a>
  </form>
</div>
</body>
</html>
