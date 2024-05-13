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
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>

        <script>
            $(document).ready(function() {
                var currentPage = 1; // Initial page number
                var pageSize = 10; // Number of items per page
                var dataTable = $('#import-projects-table').DataTable({
                    "serverSide": true,
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/aim/viewImportProgress.do",
                        "type": "POST",
                        "data": function(d) {
                            d.fileRecordId = $(".highlighted-row .view-progress-btn").data("file-record-id");
                            d.pageNumber = d.start / d.length + 1; // Calculate the page number based on start index and page length
                            d.pageSize = d.length; // Set the page size
                            return d;
                        }
                    }
                });

                $(".view-progress-btn").click(function() {
                    var fileRecordId = $(this).data("file-record-id");
                    var currentRow = $(this).closest("tr");

                    // Unhighlight all other rows
                    $(".highlighted-row").removeClass("highlighted-row");

                    // Highlight the clicked row
                    currentRow.addClass("highlighted-row");

                    // Reload DataTable with new data
                    dataTable.ajax.reload(function(json) {
                        // Assuming the server returns a JSON object with importProjects data
                        console.log("Response: " + JSON.stringify(json));
                        $(".countRecords").html(
                            '<h4 style="color: #f1b0b7">All Projects: ' +json.totalProjects+'</h4>' +
                            '<h4 style="color: forestgreen">Successful Projects: ' +json.successfulProjects+'</h4>' +
                            '<h4 style="color: red">Failed Projects: ' +json.failedProjects +'</h4>'
                        );

                        // Clear existing table data
                        dataTable.clear();

                        // Populate table with new data
                        $.each(json.importedProjects, function(index, project) {
                            dataTable.row.add([
                                project.id,
                                project.importStatus,
                                project.newProject,
                                JSON.stringify(project.importResponse).substring(0, 50) + "..."
                            ]).draw();
                        });

                        // Handle "View More" button click event
                        $(".view-more-btn").click(function() {
                            var $tr = $(this).closest("tr");
                            var $responseCell = $tr.find(".truncated-response");
                            var fullResponse = JSON.stringify(json.importedProjects[$tr.index()].importResponse); // Full response string
                            var $btn = $(this);

                            if ($btn.text() === "View More") {
                                $responseCell.text(fullResponse);
                                $btn.text("View Less");
                            } else {
                                $responseCell.text(fullResponse.substring(0, 50) + "...");
                                $btn.text("View More");
                            }
                        });
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
        <div class="pagination"></div>
    </div>

    </body>
</html:html>
