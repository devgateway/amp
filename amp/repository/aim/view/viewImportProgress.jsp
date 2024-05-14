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

                var  datatable = $('#import-projects-table').DataTable();
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
                        data: { fileRecordId: fileRecordId },
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
                            // $("#import-projects-table tbody").empty();
                            $('#import-projects-table').DataTable().clear();

                            // Populate import projects table with new data
                            $.each(importProjects, function(index, project) {
                                var truncatedResponse = JSON.stringify(project.importResponse).substring(0, 50) + "...";
                                var importResponseHtml = '<span class="truncated-response">' + truncatedResponse + '</span><p></p><br><button class="view-more-btn">View More</button>';
                                var rowData = [
                                    project.id,
                                    project.importStatus,
                                    project.newProject,
                                    importResponseHtml
                                ];
                                var hiddenData = JSON.stringify(project.importResponse); // Hidden data
                                var rowNode = datatable.row.add(rowData).node();
                                $(rowNode).data('hiddenData', hiddenData); //
                            });
                            datatable.draw(); // Draw the table after adding all rows

                            // Handle "View More" button click event
                            $('#import-projects-table tbody').on('click', '.view-more-btn', function() {
                                var $row = $(this).closest('tr');
                                var $responseCell = $row.find('.truncated-response');
                                var fullResponse = $row.data('hiddenData'); // Access hidden data stored as jQuery data
                                var $btn = $(this);

                                if ($btn.text() === "View More") {
                                    $responseCell.text(fullResponse);
                                    $btn.text("View Less");
                                } else {
                                    $responseCell.text(fullResponse.substring(0, 50) + "...");
                                    $btn.text("View More");
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
        <div class="pagination"></div>
    </div>

    </body>
</html:html>
