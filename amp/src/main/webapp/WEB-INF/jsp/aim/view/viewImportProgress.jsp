<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html:html>
    <head>
        <title>Imported Files</title>
        <style>
            :root {
                --page-bg: linear-gradient(180deg, #eef6f7 0%, #f7f3ea 50%, #fcfcfb 100%);
                --panel-bg: rgba(255, 255, 255, 0.92);
                --panel-border: rgba(22, 53, 67, 0.12);
                --text-strong: #163543;
                --text-soft: #647b86;
                --accent: #0d7c86;
                --accent-deep: #0a5c66;
                --success: #2a7d57;
                --danger: #a63f3f;
                --warning: #c7772f;
                --row-alt: #f8fbfb;
                --shadow: 0 24px 60px rgba(19, 42, 53, 0.12);
            }

            html {
                scroll-behavior: smooth;
            }

            body {
                margin: 0;
                font-family: Georgia, "Times New Roman", serif;
                color: var(--text-strong);
                background: var(--page-bg);
            }

            .progress-page {
                max-width: 1220px;
                margin: 0 auto;
                padding: 40px 20px 56px;
            }

            .hero-card,
            .panel-card,
            .records-card {
                background: var(--panel-bg);
                border: 1px solid var(--panel-border);
                box-shadow: var(--shadow);
                border-radius: 28px;
            }

            .hero-card {
                padding: 34px;
                margin-bottom: 22px;
                background:
                    radial-gradient(circle at top right, rgba(13, 124, 134, 0.16), transparent 34%),
                    radial-gradient(circle at top left, rgba(199, 119, 47, 0.16), transparent 30%),
                    rgba(255, 255, 255, 0.92);
            }

            .hero-card h1,
            .panel-card h2,
            .records-card h2 {
                margin: 0 0 10px;
            }

            .section-label {
                display: inline-block;
                margin-bottom: 8px;
                font-size: 12px;
                letter-spacing: 0.18em;
                text-transform: uppercase;
                color: var(--accent);
                font-weight: 700;
            }

            .section-copy {
                margin: 0;
                color: var(--text-soft);
                line-height: 1.6;
            }

            .panel-card,
            .records-card {
                padding: 24px;
                margin-bottom: 22px;
            }

            table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                background: #fff;
                border: 1px solid rgba(22, 53, 67, 0.1);
                border-radius: 18px;
                overflow: hidden;
            }

            td, th {
                text-align: left;
                padding: 14px 16px;
                border-bottom: 1px solid rgba(22, 53, 67, 0.08);
            }

            th {
                background: linear-gradient(180deg, #f3faf9 0%, #eaf4f5 100%);
                font-size: 12px;
                letter-spacing: 0.12em;
                text-transform: uppercase;
            }

            tr:nth-child(even) {
                background: var(--row-alt);
            }

            tr.highlighted-row {
                background: rgba(42, 125, 87, 0.16) !important;
            }

            .view-progress-btn,
            .view-more-btn {
                border: none;
                border-radius: 999px;
                padding: 10px 16px;
                font-weight: 700;
                cursor: pointer;
                color: #fff;
                background: linear-gradient(135deg, var(--accent) 0%, var(--accent-deep) 100%);
                box-shadow: 0 14px 28px rgba(13, 124, 134, 0.18);
            }

            .view-more-btn {
                margin-top: 8px;
                padding: 8px 14px;
                font-size: 12px;
            }

            .status-summary {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
                gap: 14px;
                margin: 18px 0;
            }

            .status-pill {
                padding: 14px 16px;
                border-radius: 18px;
                font-weight: 700;
                background: rgba(13, 124, 134, 0.08);
            }

            .status-pill.all {
                color: var(--warning);
            }

            .status-pill.success {
                color: var(--success);
            }

            .status-pill.failed {
                color: var(--danger);
            }

            .filter-div {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                align-items: center;
                margin-bottom: 18px;
                padding: 14px 16px;
                border-radius: 18px;
                background: linear-gradient(180deg, rgba(13, 124, 134, 0.06), rgba(13, 124, 134, 0.02));
            }

            .filter-div label {
                font-weight: 700;
            }

            .filter-div input {
                margin-right: 4px;
            }

            #records-section {
                scroll-margin-top: 18px;
            }

            .dataTables_wrapper .dataTables_filter input,
            .dataTables_wrapper .dataTables_length select {
                border: 1px solid rgba(22, 53, 67, 0.18);
                border-radius: 10px;
                padding: 6px 10px;
                background: #fff;
            }

            .truncated-response {
                display: inline-block;
                max-width: 100%;
                white-space: pre-wrap;
                word-break: break-word;
                color: var(--text-soft);
            }

            @media (max-width: 768px) {
                .progress-page {
                    padding: 24px 14px 40px;
                }

                .hero-card,
                .panel-card,
                .records-card {
                    padding: 18px;
                    border-radius: 20px;
                }

                .filter-div {
                    align-items: flex-start;
                }
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>

        <script>

            $(document).ready(function() {

                var  datatable = $('#import-projects-table').DataTable();
                $("input[name='project-filter']").off('change.projectFilter').on('change.projectFilter', function() {
                    var filterValue = $(this).val();
                    if (filterValue === 'ALL') {
                        datatable.column(1).search('').draw();
                    } else {
                        datatable.column(1).search(filterValue).draw();
                    }
                });

                $('#import-projects-table tbody').off('click.viewMore').on('click.viewMore', '.view-more-btn', function() {
                    var $row = $(this).closest('tr');
                    var $responseCell = $row.find('.truncated-response');
                    var fullResponse = $row.data('hiddenData');
                    var $btn = $(this);

                    if ($btn.text() === "View More") {
                        $responseCell.text(fullResponse);
                        $btn.text("View Less");
                    } else {
                        $responseCell.text(fullResponse.substring(0, 50) + "...");
                        $btn.text("View More");
                    }
                });

                $(".view-progress-btn").click(function() {
                    var fileRecordId = $(this).data("file-record-id");
                    var currentRow = $(this).closest("tr");
                    $(".highlighted-row").removeClass("highlighted-row");
                    currentRow.addClass("highlighted-row");

                    $.ajax({
                        url: "${pageContext.request.contextPath}/aim/viewImportProgress.do",
                        type: "POST",
                        data: { fileRecordId: fileRecordId },
                        success: function(response) {
                            console.log("Response: " + JSON.stringify(response));
                            var data = JSON.parse(JSON.stringify(response));
                            $(".countRecords").html(
                                '<div class="status-summary">' +
                                '<div class="status-pill all">All Records: ' + data.totalProjects + '</div>' +
                                '<div class="status-pill success">Successful Records: ' + data.successfulProjects + '</div>' +
                                '<div class="status-pill failed">Failed Records: ' + data.failedProjects + '</div>' +
                                '</div>'
                            );
                            var importProjects = data.importedProjects;
                            $('#import-projects-table').DataTable().clear();

                            $.each(importProjects, function(index, project) {
                                var truncatedResponse = JSON.stringify(project.importResponse).substring(0, 50) + "...";
                                var importResponseHtml = '<span class="truncated-response">' + truncatedResponse + '</span><p></p><br><button class="view-more-btn">View More</button>';
                                var rowData = [
                                    project.id,
                                    project.importStatus,
                                    project.newProject,
                                    importResponseHtml
                                ];
                                var hiddenData = JSON.stringify(project.importResponse);
                                var rowNode = datatable.row.add(rowData).node();
                                $(rowNode).data('hiddenData', hiddenData);
                            });
                            datatable.draw();

                            var recordsSection = document.getElementById('records-section');
                            if (recordsSection) {
                                recordsSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
                            }

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
    <div class="progress-page">
        <div class="hero-card">
            <span class="section-label">Import Tracking</span>
            <h1>View Progress</h1>
            <p class="section-copy">Open any imported file to inspect record-by-record status, success counts, and detailed responses.</p>
        </div>

        <div class="panel-card">
            <span class="section-label">Imported Files</span>
            <h2>Recent Uploads</h2>
            <p class="section-copy">Click a file to load its records and jump directly to the detailed results table.</p>

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
        </div>

        <div class="records-card file-projects" id="records-section">
            <span class="section-label">Record Details</span>
            <h2>Imported Records</h2>

            <div class="filter-div">
                <label for="all-projects">All:</label>
                <input type="radio" id="all-projects" name="project-filter" value="ALL" checked>
                <label for="success-projects">Success:</label>
                <input type="radio" id="success-projects" name="project-filter" value="SUCCESS">
                <label for="failed-projects">Failed:</label>
                <input type="radio" id="failed-projects" name="project-filter" value="FAILED">
            </div>

            <div class="countRecords"></div>

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
                </tbody>
            </table>
            <div class="pagination"></div>
        </div>
    </div>

    </body>
</html:html>
