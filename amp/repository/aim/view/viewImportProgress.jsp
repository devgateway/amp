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
                var currentPage = 1; // Initial page number
                var pageSize = 10; // Number of items per page

                $(".view-progress-btn").click(function() {
                    // $(".file-projects").empty();
                    var fileRecordId = $(this).data("file-record-id");
                    var currentRow = $(this).closest("tr");
                    // Unhighlight all other rows
                    $(".highlighted-row").removeClass("highlighted-row");

                    // Highlight the clicked row
                    currentRow.addClass("highlighted-row");

                    $.ajax({
                        url: "${pageContext.request.contextPath}/aim/viewImportProgress.do",
                        type: "POST",
                        data: { fileRecordId: fileRecordId, pageNumber: currentPage, pageSize: pageSize },
                        success: function(response) {
                            // Assuming the server returns a JSON object with importProjects data
                            console.log("Response: " + JSON.stringify(response));
                            var data = JSON.parse(JSON.stringify(response));
                            $(".countRecords").html(
                                '<h4 style="color: #f1b0b7">All Projects: ' +data.totalProjects+'</h4>' +
                                '<h4 style="color: forestgreen">Successful Projects: ' +data.successfulProjects+'</h4>' +
                                '<h4 style="color: red">Failed Projects: ' +data.failedProjects +'</h4>'
                            );
                            var importProjects = data.importedProjects;

                            // Clear existing import projects table
                            $("#import-projects-table tbody").empty();

                            // Populate import projects table with new data
                            $.each(importProjects, function(index, project) {
                                addProjectRow(project);
                            });

                            // Add pagination controls
                            var totalPages = data.totalPages;
                            $("#import-projects-table").remove(".pagination");
                            $("#import-projects-table").after('<div class="pagination"></div>');
                            generatePaginationHtml(currentPage, totalPages);
                            // Handle page click event
                            $(".page-link").click(function() {
                                currentPage = parseInt($(this).text());
                                $(".page-link").removeClass("active");
                                $(this).addClass("active");

                                // Make new AJAX request with updated pageNumber
                                makeRequest(fileRecordId, currentPage, pageSize);
                            });

                            // Handle previous and next button click events
                            $(".prev-btn").click(function() {
                                if (currentPage > 1) {
                                    currentPage--;
                                    $(".page-link").removeClass("active");
                                    $(".page-link").eq(currentPage - startPage).addClass("active");
                                    generatePaginationHtml(currentPage, totalPages);

                                    // Make new AJAX request with updated pageNumber
                                    makeRequest(fileRecordId, currentPage, pageSize)
                                }
                            });

                            $(".next-btn").click(function() {
                                if (currentPage < totalPages) {
                                    currentPage++;
                                    $(".page-link").removeClass("active");
                                    $(".page-link").eq(currentPage - startPage).addClass("active");
                                    generatePaginationHtml(currentPage, totalPages);
                                    // Make new AJAX request with updated pageNumber
                                    makeRequest(fileRecordId, currentPage, pageSize);
                                }
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
    </div>

    </body>
</html:html>
