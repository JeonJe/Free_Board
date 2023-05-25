<%@ page import="category.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="category.CategoryDAO" %><%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/16
  Time: 11:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 작성</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

    <script>
        document.getElementById('post-form').addEventListener('submit', function (event) {
            var writer = document.getElementById('writer').value;
            var password = document.getElementById('password').value;
            var passwordConfirm = document.getElementById('password-confirm').value;
            var title = document.getElementById('title').value;
            var content = document.getElementById('content').value;

            if (writer.length < 3 || writer.length >= 5) {
                alert('작성자는 3글자 이상 5글자 미만이어야 합니다.');
                event.preventDefault();
                return;
            }

            if (password.length < 4 || password.length >= 16 || !/[A-Za-z0-9_$@#%&*]/.test(password) || password !== passwordConfirm) {
                alert('비밀번호는 4글자 이상 16글자 미만이어야 하며, 영문, 숫자, 특수문자를 포함해야 하며, 비밀번호 확인과 일치해야 합니다.');
                event.preventDefault();
                return;
            }

            if (title.length < 4 || title.length >= 100) {
                alert('제목은 4글자 이상 100글자 미만이어야 합니다.');
                event.preventDefault();
                return;
            }

            if (content.length < 4 || content.length >= 2000) {
                alert('내용은 4글자 이상 2000글자 미만이어야 합니다.');
                event.preventDefault();
                return;
            }
        });
    </script>
</head>
<body>

<div class="container my-4">
    <h1 class="my-4 ">게시판 - 등록</h1>
    <div class="row justify-content-center ">
        <div class="col-md-12 bg-light">

            <form id="post-form" action="save" method="post" enctype="multipart/form-data">
                <div class="form-group row border-bottom p-3">
                    <label for="category_id" class="col-sm-2 col-form-label d-flex align-items-center">카테고리:</label>

                    <div class="col-sm-8">
                        <select id="category_id" name="category_id" class="form-control" required>

                            <%
                                // 카테고리 목록 가져오기
                                List<Category> categories = CategoryDAO.getAllCategory(); // CategoryDAO에서 카테고리 목록을 가져오는 메서드를 구현해야 함

                                for (Category category : categories) {
                                    int categoryId = category.getCategoryId(); // 카테고리의 ID
                                    String categoryName = category.getCategoryName(); // 카테고리의 이름
                            %>
                            <option value="<%= categoryId %>"><%= categoryName %>
                            </option>
                            <%
                                }
                            %>
                        </select>
                        </select>
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="writer" class="col-sm-2 col-form-label d-flex align-items-center">작성자:</label>
                    <div class="col-sm-8">
                        <input autofocus type="text" id="writer" name="writer" class="form-control" required>
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="password" class="col-sm-2 col-form-label d-flex align-items-center">비밀번호:</label>
                    <div class="col-sm-4">
                        <input type="password" id="password" name="password" placeholder="비밀번호" class="form-control"
                               required>
                    </div>
                    <div class="col-sm-4">
                        <input type="password" id="password-confirm" name="password-confirm" placeholder="비밀번호 확인"
                               class="form-control" required>
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="title" class="col-sm-2 col-form-label d-flex align-items-center">제목:</label>
                    <div class="col-sm-8">
                        <input type="text" id="title" name="title" class="form-control" required>
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="content" class="col-sm-2 col-form-label d-flex align-items-center">내용:</label>
                    <div class="col-sm-8">
                        <textarea id="content" name="content" class="form-control" rows="6" required></textarea>
                    </div>
                </div>

                <div class="form-group row p-3">
                    <label for="attachment1" class="col-sm-2 col-form-label d-flex align-items-center">첨부파일:</label>
                    <div class="col-sm-8">
                        <input type="file" id="attachment1" name="attachment1" class="form-control-file mb-2">
                        <input type="file" id="attachment2" name="attachment2" class="form-control-file mb-2">
                        <input type="file" id="attachment3" name="attachment3" class="form-control-file mb-2">
                    </div>
                </div>

                <div class="row mt-3 justify-content-center">
                    <div class="col-md-6">
                        <input type="button" value="취소" onclick="location.href='list'"
                               class="btn btn-secondary btn-block">
                    </div>
                    <div class="col-md-6">
                        <input type="submit" value="저장" class="btn btn-primary btn-block">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>