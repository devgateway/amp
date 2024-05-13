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
                    var xhr = new XMLHttpRequest();
                    // Create a FormData object to send data in the request body
                    var formData = new FormData();
                    formData.append("fileRecordId", fileRecordId);


                    xhr.open("GET", "${pageContext.request.contextPath}/aim/viewImportProgress.do", true);
                    // xhr.setRequestHeader()
                    // xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState === 4 && xhr.status === 200) {
                            // Update UI or perform any additional actions if needed
                            console.log("Selected pairs updated successfully.");
                            var jsonData = xhr.getResponseHeader('jsonData');

                            console.log("Raw response: "+jsonData)
                            var data = JSON.parse(jsonData);
                            var importProjects = data.importedProjects;
                            $("#import-projects-table tbody").empty();

                            // Populate import projects table with new data
                            $.each(importProjects, function(index, project) {
                                var row = "<tr>" +
                                    "<td>" + project.id + "</td>" +
                                    "<td>" + project.importStatus + "</td>" +
                                    "<td>" + project.newProject + "</td>" +
                                    "<td>" + JSON.stringify(project.importResponse) + "</td>" +
                                    "</tr>";
                                $("#import-projects-table tbody").append(row);
                            });

                        }
                        else
                        {
                            console.error("Error loading :")
                        }
                    };
                    xhr.send(formData);

                    <%--$.ajax({--%>
                    <%--    url: "${pageContext.request.contextPath}/aim/viewImportProgress.do",--%>
                    <%--    type: "POST",--%>
                    <%--    data: { fileRecordId: fileRecordId },--%>
                    <%--    success: function(response) {--%>
                    <%--        // Assuming the server returns a JSON object with importProjects data--%>
                    <%--        console.log("Response: " + response);--%>
                    <%--        response = JSON.parse(response);--%>
                    <%--        var importProjects = response.importedProjects;--%>

                    <%--        // Clear existing import projects table--%>
                    <%--        $("#import-projects-table tbody").empty();--%>

                    <%--        // Populate import projects table with new data--%>
                    <%--        $.each(importProjects, function(index, project) {--%>
                    <%--            var row = "<tr>" +--%>
                    <%--                "<td>" + project.id + "</td>" +--%>
                    <%--                "<td>" + project.importStatus + "</td>" +--%>
                    <%--                "<td>" + project.newProject + "</td>" +--%>
                    <%--                "<td>" + JSON.stringify(project.importResponse) + "</td>" +--%>
                    <%--                "</tr>";--%>
                    <%--            $("#import-projects-table tbody").append(row);--%>
                    <%--        });--%>
                    <%--    },--%>
                    <%--    error: function(xhr, status, error) {--%>
                    <%--        console.error("Error: " + error);--%>
                    <%--    }--%>
                    <%--});--%>
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
