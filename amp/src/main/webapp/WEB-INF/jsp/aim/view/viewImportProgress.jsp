<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<html:html>
    <head>
        <title><digi:trn>Imported Files</digi:trn></title>
        <style>
            :root {
                --page-bg: #f4f5f7;
                --panel-bg: #ffffff;
                --panel-border: #d7dde3;
                --text-strong: #22313a;
                --text-soft: #667784;
                --accent: #40606f;
                --accent-deep: #314b57;
                --success: #4a6b58;
                --danger: #7a5555;
                --warning: #7d6a53;
                --row-alt: #f8f9fa;
                --shadow: 0 4px 12px rgba(25, 39, 52, 0.05);
            }

            html {
                scroll-behavior: smooth;
            }

            body {
                margin: 0;
                font-family: Arial, Helvetica, sans-serif;
                font-size: 12px;
                line-height: 1.4;
                color: var(--text-strong);
                background: var(--page-bg);
            }

            input,
            select,
            textarea,
            button {
                font-family: inherit;
            }

            .progress-page {
                max-width: 1220px;
                margin: 0 auto;
                padding: 22px 16px 34px;
            }

            .hero-card,
            .panel-card,
            .records-card {
                background: var(--panel-bg);
                border: 1px solid var(--panel-border);
                box-shadow: var(--shadow);
                border-radius: 14px;
            }

            .hero-card {
                padding: 20px;
                margin-bottom: 14px;
                background: var(--panel-bg);
            }

            .hero-card h1,
            .panel-card h2,
            .records-card h2 {
                margin: 0 0 8px;
                font-size: 18px;
            }

            .section-label {
                display: inline-block;
                margin-bottom: 6px;
                font-size: 11px;
                letter-spacing: 0.12em;
                text-transform: uppercase;
                color: var(--accent);
                font-weight: 700;
            }

            .section-copy {
                margin: 0;
                color: var(--text-soft);
                line-height: 1.45;
                font-size: 12px;
            }

            .panel-card,
            .records-card {
                padding: 16px;
                margin-bottom: 14px;
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
                padding: 10px 12px;
                font-size: 12px;
                border-bottom: 1px solid rgba(22, 53, 67, 0.08);
            }

            th {
                background: #eef1f3;
                font-size: 11px;
                letter-spacing: 0.08em;
                text-transform: uppercase;
            }

            tr:nth-child(even) {
                background: var(--row-alt);
            }

            tr.highlighted-row {
                background: #e8eef0 !important;
            }

            .view-progress-btn,
            .view-more-btn,
            .nav-action-btn {
                border: 1px solid #506673;
                border-radius: 999px;
                padding: 8px 14px;
                font-size: 12px;
                font-weight: 700;
                cursor: pointer;
                color: #fff;
                background: var(--accent);
                box-shadow: none;
            }

            .view-more-btn {
                margin-top: 6px;
                padding: 6px 12px;
                font-size: 11px;
            }

            .hero-actions {
                margin-top: 12px;
                display: flex;
                justify-content: flex-end;
            }

            .status-summary {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
                gap: 10px;
                margin: 12px 0;
            }

            .status-pill {
                padding: 10px 12px;
                border-radius: 10px;
                font-weight: 700;
                background: #f1f3f5;
                border: 1px solid var(--panel-border);
                font-size: 12px;
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

            .status-pill.skipped {
                color: var(--warning);
            }

            .filter-div {
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
                align-items: center;
                margin-bottom: 12px;
                padding: 10px 12px;
                border-radius: 10px;
                background: #f1f3f5;
                border: 1px solid var(--panel-border);
            }

            .filter-div label {
                font-size: 12px;
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
                border-radius: 6px;
                padding: 5px 8px;
                background: #fff;
                font-size: 12px;
            }

            .uploads-filter-bar {
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
                align-items: end;
                margin: 10px 0 12px;
                padding: 10px 12px;
                border-radius: 10px;
                background: #f1f3f5;
                border: 1px solid var(--panel-border);
            }

            .uploads-filter-field {
                display: flex;
                flex-direction: column;
                gap: 4px;
                min-width: 160px;
            }

            .uploads-filter-field label {
                font-size: 12px;
                font-weight: 700;
            }

            .uploads-filter-field input {
                border: 1px solid rgba(22, 53, 67, 0.18);
                border-radius: 6px;
                padding: 6px 8px;
                background: #fff;
                font-size: 12px;
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
                    padding: 14px;
                    border-radius: 12px;
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

            var currentViewedFileRecordId = null;

            $(document).ready(function() {

                var datatable = $('#import-projects-table').DataTable();
                var recentUploadsTable = $('#recent-uploads-table').DataTable({
                    pageLength: 10,
                    order: [[4, 'desc'], [0, 'desc']],
                    columnDefs: [
                        { targets: 4, visible: false, searchable: false }
                    ]
                });

                $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
                    if (settings.nTable.id !== 'recent-uploads-table') {
                        return true;
                    }

                    var minDateValue = $('#upload-date-from').val();
                    var maxDateValue = $('#upload-date-to').val();
                    var rowNode = recentUploadsTable.row(dataIndex).node();
                    var uploadedAtEpoch = parseInt($(rowNode).find('.uploaded-at-cell').attr('data-epoch'), 10);

                    if (isNaN(uploadedAtEpoch)) {
                        return true;
                    }

                    var uploadedAtDate = new Date(uploadedAtEpoch);
                    uploadedAtDate.setHours(0, 0, 0, 0);

                    var minDate = minDateValue ? new Date(minDateValue) : null;
                    var maxDate = maxDateValue ? new Date(maxDateValue) : null;
                    if (minDate) {
                        minDate.setHours(0, 0, 0, 0);
                    }
                    if (maxDate) {
                        maxDate.setHours(23, 59, 59, 999);
                    }

                    if (minDate && uploadedAtDate < minDate) {
                        return false;
                    }
                    if (maxDate && uploadedAtEpoch > maxDate.getTime()) {
                        return false;
                    }
                    return true;
                });

                $('#upload-date-from, #upload-date-to').on('change', function() {
                    recentUploadsTable.draw();
                });

                function loadFileProgress(fileRecordId, currentRow) {
                    if (!fileRecordId) {
                        return;
                    }

                    currentViewedFileRecordId = fileRecordId;
                    $('#refresh-progress-btn').prop('disabled', false);

                    if (currentRow && currentRow.length) {
                        $(".highlighted-row").removeClass("highlighted-row");
                        currentRow.addClass("highlighted-row");
                    }

                    $.ajax({
                        url: "${pageContext.request.contextPath}/aim/viewImportProgress.do",
                        type: "POST",
                        data: { fileRecordId: fileRecordId },
                        success: function(response) {
                            console.log("Response: " + JSON.stringify(response));
                            var data = JSON.parse(JSON.stringify(response));
                            $(".countRecords").html(
                                '<div class="status-summary">' +
                                '<div class="status-pill all"><digi:trn>All Records</digi:trn>: ' + data.totalProjects + '</div>' +
                                '<div class="status-pill success"><digi:trn>Successful Records</digi:trn>: ' + data.successfulProjects + '</div>' +
                                '<div class="status-pill failed"><digi:trn>Failed Records</digi:trn>: ' + data.failedProjects + '</div>' +
                                '<div class="status-pill skipped"><digi:trn>Skipped Records</digi:trn>: ' + data.skippedProjects + '</div>' +
                                '</div>'
                            );
                            var importProjects = data.importedProjects;
                            $('#import-projects-table').DataTable().clear();

                            $.each(importProjects, function(index, project) {
                                var truncatedResponse = JSON.stringify(project.importResponse).substring(0, 50) + "...";
                                var importResponseHtml = '<span class="truncated-response">' + truncatedResponse + '</span><p></p><br><button class="view-more-btn"><digi:trn>View More</digi:trn></button>';
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
                }

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

                    if ($btn.text() === "<digi:trn jsFriendly='true'>View More</digi:trn>") {
                        $responseCell.text(fullResponse);
                        $btn.text("<digi:trn jsFriendly='true'>View Less</digi:trn>");
                    } else {
                        $responseCell.text(fullResponse.substring(0, 50) + "...");
                        $btn.text("<digi:trn jsFriendly='true'>View More</digi:trn>");
                    }
                });

                $(".view-progress-btn").click(function() {
                    var fileRecordId = $(this).data("file-record-id");
                    var currentRow = $(this).closest("tr");
                    loadFileProgress(fileRecordId, currentRow);
                });

                $('#refresh-progress-btn').click(function() {
                    if (!currentViewedFileRecordId) {
                        return;
                    }

                    var currentRow = $('.view-progress-btn[data-file-record-id="' + currentViewedFileRecordId + '"]').closest('tr');
                    loadFileProgress(currentViewedFileRecordId, currentRow);
                });
            });
        </script>
    </head>
    <body>
    <div class="progress-page">
        <div class="hero-card">
            <span class="section-label"><digi:trn>Import Tracking</digi:trn></span>
            <h1><digi:trn>View Import Progress</digi:trn></h1>
            <p class="section-copy"><digi:trn>Open any imported file to inspect record-by-record status, success counts, and detailed responses.</digi:trn></p>
            <div class="hero-actions">
                <button type="button" class="nav-action-btn" onclick="window.location.href='${pageContext.request.contextPath}/aim/dataImporter.do';"><digi:trn>Import More Data</digi:trn></button>
            </div>
        </div>

        <div class="panel-card">
            <span class="section-label"><digi:trn>Imported Files</digi:trn></span>
            <h2><digi:trn>Recent Uploads</digi:trn></h2>
            <p class="section-copy"><digi:trn>Click a file to load its records and jump directly to the detailed results table.</digi:trn></p>

            <div class="uploads-filter-bar">
                <div class="uploads-filter-field">
                    <label for="upload-date-from"><digi:trn>Uploaded From</digi:trn></label>
                    <input type="date" id="upload-date-from">
                </div>
                <div class="uploads-filter-field">
                    <label for="upload-date-to"><digi:trn>Uploaded To</digi:trn></label>
                    <input type="date" id="upload-date-to">
                </div>
            </div>

            <table id="recent-uploads-table">
                <thead>
                <tr>
                    <th><digi:trn>ID</digi:trn></th>
                    <th><digi:trn>File Name</digi:trn></th>
                    <th><digi:trn>Status</digi:trn></th>
                    <th><digi:trn>Processing Time</digi:trn></th>
                    <th><digi:trn>Upload Date</digi:trn></th>
                    <th><digi:trn>Upload Date Sort</digi:trn></th>
                    <th><digi:trn>Action</digi:trn></th>
                </tr>
                </thead>
                <tbody>
                <jsp:useBean id="importedFilesRecords" scope="request" type="java.util.List"/>

                <c:forEach items="${importedFilesRecords}" var="record" varStatus="loop">
                    <tr>
                        <td>${record.id}</td>
                        <td>${record.fileName}</td>
                        <td>${record.importStatus}</td>
                        <td>${record.formattedProcessingTime}</td>
                        <td class="uploaded-at-cell" data-epoch="${record.uploadedAtEpochMillis}">${record.formattedUploadedAt}</td>
                        <td>${record.uploadedAtEpochMillis}</td>
                        <td>
                            <button class="view-progress-btn" data-file-record-id="${record.id}"><digi:trn>View Progress</digi:trn></button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="records-card file-projects" id="records-section">
            <span class="section-label"><digi:trn>Record Details</digi:trn></span>
            <h2><digi:trn>Imported Records</digi:trn></h2>

            <div class="filter-div">
                <label for="all-projects"><digi:trn>All</digi:trn>:</label>
                <input type="radio" id="all-projects" name="project-filter" value="ALL" checked>
                <label for="success-projects"><digi:trn>Success</digi:trn>:</label>
                <input type="radio" id="success-projects" name="project-filter" value="SUCCESS">
                <label for="failed-projects"><digi:trn>Failed</digi:trn>:</label>
                <input type="radio" id="failed-projects" name="project-filter" value="FAILED">
                <label for="skipped-projects"><digi:trn>Skipped</digi:trn>:</label>
                <input type="radio" id="skipped-projects" name="project-filter" value="SKIPPED">
                <button type="button" id="refresh-progress-btn" disabled><digi:trn>Refresh Records</digi:trn></button>
            </div>

            <div class="countRecords"></div>

            <table id="import-projects-table">
                <thead>
                <tr>
                    <th><digi:trn>ID</digi:trn></th>
                    <th><digi:trn>Project Status</digi:trn></th>
                    <th><digi:trn>Is New?</digi:trn></th>
                    <th><digi:trn>Response String</digi:trn></th>
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
