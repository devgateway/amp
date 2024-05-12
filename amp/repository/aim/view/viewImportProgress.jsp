<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib prefix="html" uri="/taglib/struts-html" %>
<%@ taglib prefix="bean" uri="/taglib/struts-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Imported Projects</title>
    <style>
        .failed-row {
            background-color: #ffcccc;
        }
    </style>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {
            function loadProjects(startPage) {
                $.ajax({
                    url: "/aim/viewImportProgress.do",
                    type: "POST",
                    data: {
                        startPage: startPage,
                        endPage: ${endPage}
                    },
                    success: function(response) {
                        $("#importedProjectsTable tbody").html(response.importedProjectsTableBody);
                    }
                });
            }

            $(".pagination-link").click(function(event) {
                event.preventDefault();
                var startPage = $(this).data("start-page");
                loadProjects(startPage);
            });

            $(".view-more-button").click(function(event) {
                event.preventDefault();
                var responseId = $(this).data("response-id");
                var response = $("#response-" + responseId);
                response.toggleClass("show");
            });
        });
    </script>
</head>
<body>
<div>
    <h1>Imported Projects</h1>

    <table id="importedProjectsTable">
        <thead>
        <tr>
            <th>ID</th>
            <th>Import Response</th>
            <th>Import Status</th>
            <th>New Project</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="project" items="${importedProjects}">
            <tr class="${project.importStatus eq 'FAILED'? 'failed-row' : ''}">
                <td>${project.id}</td>
                <td>
                    <button type="button" class="view-more-button" data-response-id="${project.id}">View More</button>
                    <div id="response-${project.id}" class="collapse">
                            ${project.importResponse}
                    </div>
                </td>
                <td>${project.importStatus}</td>
                <td>${project.newProject}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div>
        <c:forEach begin="1" end="${totalPages}" var="page">
            <c:choose>
                <c:when test="${page eq startPage}">
                    <span>${page}</span>
                </c:when>
                <c:otherwise>
                    <a href="#" class="pagination-link" data-start-page="${page}">${page}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</div>
</body>
</html>
