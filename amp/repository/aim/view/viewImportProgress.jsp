<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib prefix="html" uri="/taglib/struts-html" %>
<%@ taglib prefix="bean" uri="/taglib/struts-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html:html>
    <head>
        <title>Imported Files</title>
        <style>
            table {
                font-family: arial, sans-serif;
                border-collapse: collapse;
                width: 100%;
            }

            td, th {
                border: 1px solid #dddddd;
                text-align: left;
                padding: 8px;
            }

            tr:nth-child(even) {
                background-color: #dddddd;
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script>
            $(document).ready(function() {
                $(".view-progress-btn").click(function() {
                    var fileRecordId = $(this).data("file-record-id");

                    $.ajax({
                        url: "${pageContext.request.contextPath}/aim/viewImportProgress.do",
                        type: "POST",
                        data: { fileRecordId: fileRecordId },
                        success: function(response) {
                            // Assuming the server returns a JSON object with importProjects data
                            console.log("Response: " + response);
                            response = JSON.parse(response);
                            var importProjects = response.importedProjects;

                            // Clear existing import projects table
                            $("#import-projects-table tbody").empty();

                            // Populate import projects table with new data
                            $.each(importProjects, function(index, project) {
                                console.log("Project " + project);
                                var row = "<tr>" +
                                    "<td>" + project.id + "</td>" +
                                    "<td>" + project.importStatus + "</td>" +
                                    "<td>" + project.newProject + "</td>" +
                                    "<td>" + JSON.stringify(project.importResponse) + "</td>" +
                                    "</tr>";
                                $("#import-projects-table tbody").append(row);
                            });
                        },
                        error: function(xhr, status, error) {
                            console.error("Error: " + error);
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
    <h2>Imported Files</h2>

    <!-- Table to display ImportedFilesRecord list -->
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>File Name</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="importedFilesRecords" scope="request" type="java.util.List"/>

        <c:forEach items="${importedFilesRecords}" var="record" varStatus="loop">
            <tr>
                <td>${record.id}</td>
                <td>${record.fileName}</td>
                <td>${record.importStatus}</td>
                <td>
                    <button class="view-progress-btn" data-file-record-id="${record.id}">View Progress</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Table to display import projects for a particular ImportedFilesRecord -->
    <h3>Import Projects</h3>
    <table id="import-projects-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Project Status</th>
            <th>Is New?</th>
            <th>Response String</th>
        </tr>
        </thead>
        <tbody>
        <!-- Import projects will be populated dynamically using AJAX -->
        </tbody>
    </table>

    </body>
</html:html>
