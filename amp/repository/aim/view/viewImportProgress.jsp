<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib prefix="html" uri="/taglib/struts-html" %>
<%@ taglib prefix="bean" uri="/taglib/struts-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html:html>
    <head>
        <title>Imported Files</title>
        <style>
            tr.highlighted-row {
                background-color: #a3c293;
            }
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
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.11.3/datatables.min.css"/>
        <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.11.3/datatables.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script>

            function makeRequest(fileRecordId,currentPage, pageSize)
            {
                $.ajax({
                    url: "${pageContext.request.contextPath}/aim/viewImportProgress.do",
                    type: "POST",
                    data: { fileRecordId: fileRecordId, pageNumber: currentPage, pageSize: pageSize },
                    success: function(response) {
                        var data = JSON.parse(JSON.stringify(response));
                        var importProjects = data.importedProjects;
                        $("#import-projects-table tbody").empty();
                        $.each(importProjects, function(index, project) {
                            addProjectRow(project);
                        });
                    },
                    error: function(xhr, status, error) {
                        console.error("Error getting projects: " + error);
                    }
                });
            }

            function generatePaginationHtml(currentPage, totalPages) {
                var paginationHtml = '';
                paginationHtml += '<button class="prev-btn" disabled>Previous</button>';

                // Show 10 page numbers at a time
                var startPage = currentPage - 5 > 0? currentPage - 5 : 1;
                var endPage = startPage + 9 <= totalPages? startPage + 9 : totalPages;

                for (var i = startPage; i <= endPage; i++) {
                    paginationHtml += '<a class="page-link' + (i === currentPage? 'active' : '') + '">' + i + '</a> | ';
                }

                paginationHtml += '<button class="next-btn">Next</button>';

                $(".pagination").html(paginationHtml);

            }

            function addProjectRow(project) {
                var responseString = JSON.stringify(project.importResponse);
                var truncatedResponse = responseString.substring(0, 50) + "..."; // Limit to 50 characters

                var row = "<tr>" +
                    "<td>" + project.id + "</td>" +
                    "<td>" + project.importStatus + "</td>" +
                    "<td>" + project.newProject + "</td>" +
                    "<td>" +
                    "<span class='truncated-response'>" + truncatedResponse + "</span>" +
                    "<button class='view-more-btn'>View More</button>" +
                    "</td>" +
                    "</tr>";
                var $row = $(row); // Convert row to jQuery object
                $("#import-projects-table tbody").append($row);

                // Handle View More button click event for this row
                $row.find(".view-more-btn").click(function() {
                    var $tr = $(this).closest("tr");
                    var $responseCell = $tr.find(".truncated-response");
                    var fullResponse = JSON.stringify(project.importResponse); // Full response string
                    var $btn = $(this);

                    if ($btn.text() === "View More") {
                        $responseCell.text(fullResponse);
                        $btn.text("View Less");
                    } else {
                        $responseCell.text(truncatedResponse);
                        $btn.text("View More");
                    }
                });
            }

            $(document).ready(function() {
                var fileRecordId; // Declare fileRecordId variable

                $('#import-projects-table').DataTable({
                    processing: true,
                    serverSide: true,
                    ajax: {
                        url: "${pageContext.request.contextPath}/aim/viewImportProgress.do",
                        type: "POST",
                        data: function (d) {
                            d.fileRecordId = fileRecordId;
                        }
                    },
                    columns: [
                        { data: "id" },
                        { data: "importStatus" },
                        { data: "newProject" },
                        {
                            data: "importResponse",
                            render: function (data, type, row) {
                                var truncatedResponse = data.substring(0, 50) + "...";
                                return '<span class="truncated-response">' + truncatedResponse + '</span>' +
                                    '<button class="view-more-btn">View More</button>';
                            }
                        }
                    ]
                });

                $(".view-progress-btn").click(function() {
                     fileRecordId = $(this).data("file-record-id");
                    var currentRow = $(this).closest("tr");

                    // Unhighlight all other rows
                    $(".highlighted-row").removeClass("highlighted-row");

                    // Highlight the clicked row
                    currentRow.addClass("highlighted-row");

                    // Reload DataTable with new data
                    $('#import-projects-table').DataTable().ajax.reload();
                });

                $('#import-projects-table tbody').on('click', '.view-more-btn', function () {
                    var $row = $(this).closest('tr');
                    var $responseCell = $row.find('.truncated-response');
                    var fullResponse = $responseCell.data('full-response');
                    var $btn = $(this);

                    if ($btn.text() === "View More") {
                        $responseCell.text(fullResponse);
                        $btn.text("View Less");
                    } else {
                        var truncatedResponse = fullResponse.substring(0, 50) + "...";
                        $responseCell.text(truncatedResponse);
                        $btn.text("View More");
                    }
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
    <div class="file-projects">
    <h3>File Projects</h3>
    <div class="countRecords">

    </div>
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
<%--        <div class="pagination"></div>--%>
    </div>

    </body>
</html:html>
