<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html:html>
    <head>
        <title>Imported Files</title>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;700&family=Source+Sans+3:wght@400;600;700&display=swap" rel="stylesheet" />
        <style>
            :root {
                --page-bg: linear-gradient(135deg, #f5efe2 0%, #edf5f7 55%, #fafcfd 100%);
                --panel-bg: rgba(255, 255, 255, 0.9);
                --panel-border: rgba(20, 66, 76, 0.12);
                --shadow: 0 18px 40px rgba(24, 56, 62, 0.12);
                --ink-strong: #18343b;
                --ink-soft: #587076;
                --accent: #0d7a6f;
                --accent-deep: #0b5f57;
                --success: #2b7a59;
                --danger: #a24343;
                --warn: #c9832f;
                --line: #d9e5e6;
            }

            * {
                box-sizing: border-box;
            }

            body {
                margin: 0;
                padding: 34px 24px 50px;
                font-family: 'Source Sans 3', sans-serif;
                color: var(--ink-strong);
                background: var(--page-bg);
            }

            h1, h2, h3, h4 {
                font-family: 'Space Grotesk', sans-serif;
                letter-spacing: -0.03em;
                margin-top: 0;
            }

            .page-shell {
                max-width: 1240px;
                margin: 0 auto;
            }

            .hero,
            .panel {
                background: var(--panel-bg);
                border: 1px solid var(--panel-border);
                border-radius: 24px;
                box-shadow: var(--shadow);
            }

            .hero {
                padding: 28px 30px;
                margin-bottom: 24px;
            }

            .hero-grid {
                display: grid;
                grid-template-columns: 1.2fr 1fr;
                gap: 20px;
                align-items: center;
            }

            .hero-copy {
                color: var(--ink-soft);
                line-height: 1.55;
                margin-bottom: 0;
            }

            .summary-grid {
                display: grid;
                grid-template-columns: repeat(3, minmax(0, 1fr));
                gap: 14px;
            }

            .summary-card {
                padding: 16px 18px;
                border-radius: 16px;
                border: 1px solid var(--line);
                background: linear-gradient(180deg, rgba(255,255,255,0.96), rgba(246,250,250,0.92));
            }

            .summary-card strong {
                display: block;
                font-family: 'Space Grotesk', sans-serif;
                margin-bottom: 6px;
            }

            .summary-card span {
                color: var(--ink-soft);
            }

            .layout-grid {
                display: grid;
                gap: 22px;
            }

            .panel {
                padding: 24px;
            }

            .panel-title {
                margin-bottom: 8px;
            }

            .panel-copy {
                color: var(--ink-soft);
                margin: 0 0 18px;
            }

            table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                overflow: hidden;
                border-radius: 18px;
            }

            th {
                background: #17343b;
                color: #fff;
                text-align: left;
                padding: 14px 16px;
                font-family: 'Space Grotesk', sans-serif;
                font-weight: 500;
                border: 0;
            }

            td {
                padding: 14px 16px;
                border-bottom: 1px solid #e6eeee;
                background: rgba(255, 255, 255, 0.95);
            }

            tr:nth-child(even) td {
                background: rgba(243, 248, 248, 0.95);
            }

            tr.highlighted-row td {
                background: rgba(13, 122, 111, 0.14) !important;
            }

            .view-progress-btn,
            .view-more-btn {
                appearance: none;
                border: 0;
                border-radius: 999px;
                padding: 10px 14px;
                cursor: pointer;
                font-family: 'Space Grotesk', sans-serif;
                transition: transform 140ms ease, box-shadow 140ms ease;
            }

            .view-progress-btn {
                color: #fff;
                background: linear-gradient(135deg, var(--accent) 0%, var(--accent-deep) 100%);
                box-shadow: 0 12px 20px rgba(11, 95, 87, 0.22);
            }

            .view-more-btn {
                color: var(--ink-strong);
                background: rgba(23, 52, 59, 0.08);
                margin-top: 10px;
            }

            .view-progress-btn:hover,
            .view-more-btn:hover {
                transform: translateY(-1px);
            }

            .filter-div {
                display: flex;
                flex-wrap: wrap;
                gap: 14px;
                align-items: center;
                margin-bottom: 16px;
                padding: 14px 16px;
                border: 1px solid var(--line);
                border-radius: 16px;
                background: rgba(248, 251, 251, 0.92);
            }

            .countRecords {
                display: grid;
                grid-template-columns: repeat(3, minmax(0, 1fr));
                gap: 14px;
                margin-bottom: 18px;
            }

            .metric-card {
                padding: 16px 18px;
                border-radius: 16px;
                border: 1px solid var(--line);
                background: rgba(255, 255, 255, 0.94);
            }

            .metric-card.total {
                border-color: rgba(201, 131, 47, 0.2);
            }

            .metric-card.success {
                border-color: rgba(43, 122, 89, 0.2);
            }

            .metric-card.failed {
                border-color: rgba(162, 67, 67, 0.2);
            }

            .metric-card h4 {
                margin: 0 0 4px;
            }

            .metric-card span {
                color: var(--ink-soft);
            }

            .truncated-response {
                white-space: pre-wrap;
                word-break: break-word;
            }

            @media (max-width: 980px) {
                .hero-grid,
                .summary-grid,
                .countRecords {
                    grid-template-columns: 1fr;
                }

                body {
                    padding: 24px 16px 36px;
                }
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
                                '<div class="metric-card total"><h4>All Records: ' + data.totalProjects + '</h4><span>Total rows tracked for this file.</span></div>' +
                                '<div class="metric-card success"><h4>Successful Records: ' + data.successfulProjects + '</h4><span>Rows imported without errors.</span></div>' +
                                '<div class="metric-card failed"><h4>Failed Records: ' + data.failedProjects + '</h4><span>Rows that require review.</span></div>'
                            );
                            var importProjects = data.importedProjects;

                            // Clear existing import projects table
                            // $("#import-projects-table tbody").empty();
                            $('#import-projects-table').DataTable().clear();


                            // Add event listener for radio button click
                            $("input[name='project-filter']").change(function() {
                                var filterValue = $(this).val();
                                if (filterValue === 'ALL') {
                                    // Clear the filter if the value is 'all'
                                    datatable.column(1).search('').draw();
                                } else {
                                    // Apply the filter
                                    datatable.column(1).search(filterValue).draw();
                                }
                            });

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
    <div class="page-shell">
        <section class="hero">
            <div class="hero-grid">
                <div>
                    <h1>View Progress</h1>
                    <p class="hero-copy">Review recent import files, inspect record-level outcomes, and filter the imported rows without changing the underlying import workflow.</p>
                </div>
                <div class="summary-grid">
                    <div class="summary-card">
                        <strong>Select a file</strong>
                        <span>Choose any imported file to inspect its row-by-row results.</span>
                    </div>
                    <div class="summary-card">
                        <strong>Filter status</strong>
                        <span>Switch between all, successful, and failed records instantly.</span>
                    </div>
                    <div class="summary-card">
                        <strong>Inspect responses</strong>
                        <span>Expand detailed import responses only where needed.</span>
                    </div>
                </div>
            </div>
        </section>

        <div class="layout-grid">
            <section class="panel">
                <h3 class="panel-title">Imported Files</h3>
                <p class="panel-copy">Choose a file to load the detailed import results below.</p>
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
            </section>

            <section class="panel file-projects">
                <h3 class="panel-title">File Records</h3>
                <p class="panel-copy">Filter imported records by outcome and expand the full response when troubleshooting a failed row.</p>
                <div class="filter-div">
                    <label for="all-projects">All</label>
                    <input type="radio" id="all-projects" name="project-filter" value="ALL" checked>
                    <label for="success-projects">Success</label>
                    <input type="radio" id="success-projects" name="project-filter" value="SUCCESS">
                    <label for="failed-projects">Failed</label>
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
            </section>
        </div>
    </div>

    </body>
</html:html>
