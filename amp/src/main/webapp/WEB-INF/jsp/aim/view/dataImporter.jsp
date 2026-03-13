<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html:html>
<head>
  <title>Data Importer</title>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0/css/select2.min.css" rel="stylesheet" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

  <%
  // Prepare fields info as JSON for JavaScript
  java.util.Map<String, String> fieldsInfo = (java.util.Map<String, String>) request.getAttribute("fieldsInfo");
  StringBuilder jsonBuilder = new StringBuilder("{");
  boolean first = true;
  for (java.util.Map.Entry<String, String> entry : fieldsInfo.entrySet()) {
    if (!first) jsonBuilder.append(",");
    jsonBuilder.append("\"").append(entry.getKey().replace("\"", "\\\"")).append("\":")
               .append("\"").append(entry.getValue().replace("\"", "\\\"")).append("\"");
    first = false;
  }
  jsonBuilder.append("}");
  request.setAttribute("fieldsInfoJson", jsonBuilder.toString());
%>

<script>
    // Store the field translations for JavaScript use
    var fieldsInfoMap = JSON.parse('${fieldsInfoJson}');

    function initializeSelectControls() {
      if (!window.jQuery || !$.fn || !$.fn.select2) {
        return;
      }

      if ($('#configuration').length) {
        $('#configuration').select2({
          width: '100%',
          placeholder: 'Choose an existing configuration'
        });
      }

      if ($('#selected-field').length) {
        $('#selected-field').select2({
          width: '100%',
          placeholder: 'Search entity fields'
        });
      }

      if ($('#orgGroupId').length) {
        $('#orgGroupId').select2({
          width: '100%',
          placeholder: 'Select organization group'
        });
      }

      if ($('#data-sheet').length) {
        $('#data-sheet').select2({
          width: '100%',
          placeholder: 'Select sheet'
        });
      }

      if ($('#template-sheet').length) {
        $('#template-sheet').select2({
          width: '100%',
          placeholder: 'Select sheet'
        });
      }

      if ($('#columnName').length) {
        $('#columnName').select2({
          width: '100%',
          placeholder: 'Search template columns'
        });
      }
    }

    function toggleUploadSection() {
      var hasMappings = $('#selected-pairs-table-body tr').length > 0;
      $('#data-upload-section').toggle(hasMappings);
      $('#config-empty-note').toggle(!hasMappings);
    }

    function repopulateSelectedPairs(updatedMap) {
      var tbody = document.getElementById('selected-pairs-table-body');
      tbody.innerHTML = '';

      for (var key in updatedMap) {
        if (updatedMap.hasOwnProperty(key)) {
          updateTable(key, updatedMap[key], tbody);
        }
      }

      toggleUploadSection();
    }

    function revealConfigWorkspace() {
      document.getElementById('otherComponents').removeAttribute('hidden');
      $('#add-field').show();
      $('.remove-row').show();
      $('#selected-field').show();
      initializeSelectControls();
      toggleUploadSection();
    }
    
    function replaceLastOccurrence(inputString, search, replacement) {
      var lastIndex = inputString.lastIndexOf(search);
      if (lastIndex === -1) {
        // If the search string is not found, return the original string
        return inputString;
      } else {
        // Construct the new string with the replacement
        return inputString.substring(0, lastIndex) + replacement + inputString.substring(lastIndex + search.length);
      }
    }
    function addField() {
      var columnName = ($('#current-config-name').val())
        ? $('#column-name-edit').val()
        : document.getElementById("columnName").value;
      var selectedField = document.getElementById("selected-field").value;
      if (!columnName || !selectedField) {
        alert("Please enter column name and select a field.");
        return;
      }
      sendValuesToBackend(columnName, selectedField, "addField");
      if ($('#current-config-name').val()) {
        $('#column-name-edit').val('');
      }
    }
    $(document).ready(function() {
      $('#existing-config').val('0');
      initializeSelectControls();
      toggleUploadSection();
      if ($('#file-type').val() === 'excel') {
        $('#data-sheet-choice-div').show();
      }
      $('.remove-row').click(function() {
        var selectedRows = $('.fields-table tbody').find('.remove-checkbox:checked').closest('tr');

        selectedRows.each(function() {
          var columnName = $(this).find('.column-name').text();
          var selectedField = $(this).find('.selected-field').text();

          // You can now use the columnName and selectedField variables to perform any desired action
          console.log('Selected row:', columnName, '-', selectedField);
          sendValuesToBackend(columnName,selectedField,"removeField");


          // Remove the row from the table
          $(this).remove();
        });
        });

      $('.file_type').change(function() {
        var fileType = $(this).val();
        console.log("File type selected: " + fileType==='excel');
        if (fileType === "csv") {
          $('#select-file-label').html("Select csv file");
          $('#data-file').attr("accept", ".csv");
          $('#template-file').attr("accept", ".csv");
          $('#separator-div').hide();
          $('#data-sheet-choice-div').hide();
        } else if(fileType==="text") {
          $('#select-file-label').html("Select text file");
          $('#data-file').attr("accept", ".txt");
          $('#template-file').attr("accept", ".txt");
          $('#separator-div').show();
          $('#data-sheet-choice-div').hide();
        }
        else if(fileType==="excel") {
          $('#select-file-label').html("Select excel file");
          $('#data-file').attr("accept", ".xls,.xlsx");
          $('#template-file').attr("accept", ".xls,.xlsx");
          $('#separator-div').hide();
          $('#data-sheet-choice-div').show();
        }
        else if(fileType==="json") {
          $('#select-file-label').html("Select json file");
          $('#data-file').attr("accept", ".json");
          $('#template-file').attr("accept", ".json");
          $('#separator-div').hide();
          $('#data-sheet-choice-div').hide();
        }
      });

      $('input[name="dataSheetChoice"]').change(function() {
        var v = $(this).val();
        if (v === 'sheet') {
          $('#data-sheet-select-wrap').show();
        } else {
          $('#data-sheet-select-wrap').hide();
        }
      });

      $('#load-sheets-btn').click(function() {
        var fileInput = document.getElementById('data-file');
        if (!fileInput.files || !fileInput.files.length) {
          alert("Please select a data file first.");
          return;
        }
        var formData = new FormData();
        formData.append('dataFile', fileInput.files[0]);
        formData.append('action', 'getDataFileSheets');
        formData.append('fileType', $('#file-type').val());
        var $select = $('#data-sheet');
        $select.prop('disabled', true).empty().append('<option value="">-- Loading... --</option>');
        fetch("${pageContext.request.contextPath}/aim/dataImporter.do", { method: "POST", body: formData })
          .then(function(r) { return r.json(); })
          .then(function(names) {
            $select.empty().append('<option value="">-- Select sheet --</option>');
            if (Array.isArray(names)) {
              names.forEach(function(name) {
                $select.append($('<option></option>').attr('value', name).text(name));
              });
              $select.prop('disabled', false);
            }
          })
          .catch(function() {
            $select.empty().append('<option value="">-- Error loading sheets --</option>').prop('disabled', false);
            alert("Could not load sheets from file.");
          });
      });

      $('.existing-config').change(function() {
        var configName = $(this).val();
        if (configName!=='none' && configName!=='0'){

        var formData = new FormData();
        formData.append("configName", configName);
        formData.append("action", "configByName");

        fetch("${pageContext.request.contextPath}/aim/dataImporter.do", {
          method: "POST",
          body: formData
        })
                .then(response =>{
                  if (!response.ok) {
                    throw new Error("Network response was not ok");
                  }
                  console.log("Response: ",response);
                  $('#existing-config').val('0');
                  $('#current-config-name').val(configName);
                  return response.json();
                })
                .then(updatedMap => {
                  console.log("Map :" ,updatedMap)

                  repopulateSelectedPairs(updatedMap);
                  revealConfigWorkspace();
                  $('#add-pair-edit-section').show();
                  $('#column-name-edit').val('');

                })
                .catch(error => {
                  console.error("There was a problem with the fetch operation:", error);
                });
        }else
        {
          $('#current-config-name').val('');
          $('#add-pair-edit-section').hide();
          $("#templateUploadForm").show();
          toggleUploadSection();
        }
      });
      });


    function sendValuesToBackend(columnName, selectedField, action) {
      var formData = new FormData();
      formData.append("columnName", columnName);
      formData.append("selectedField", selectedField);
      formData.append("action", action);
      var configName = $('#current-config-name').val();
      if (configName) {
        formData.append("configName", configName);
      }

      fetch("${pageContext.request.contextPath}/aim/dataImporter.do", {
        method: "POST",
        body: formData
      })
              .then(response =>{
                if (!response.ok) {
                  throw new Error("Network response was not ok");
                }
                // console.log("Response: ",response.json());

                return response.json();
              })
              .then(updatedMap => {
                console.log("Map :" ,updatedMap)
                console.log("Selected pairs updated successfully.");
                console.log("Updated map received:", updatedMap);
                repopulateSelectedPairs(updatedMap);
              })
              .catch(error => {
                console.error("There was a problem with the fetch operation:", error);
              });
    }

    function updateTable(columnName,selectedField, tbody)
    {

      // Create a new table row
      var row = document.createElement("tr");
      //
      // // Create table cells for column name and selected field
      var columnNameCell = document.createElement("td");
      columnNameCell.className="column-name";
      columnNameCell.textContent = columnName;
      var selectedFieldCell = document.createElement("td");
      selectedFieldCell.className="selected-field"
      // Get the translated text from fieldsInfoMap
      selectedFieldCell.textContent = fieldsInfoMap[selectedField] || selectedField;


      // Create a checkbox cell
      var checkboxCell = document.createElement("td");
      var checkbox = document.createElement("input");
      checkbox.type = "checkbox";
      checkbox.className = "remove-checkbox";
      checkbox.value = columnName;
      checkboxCell.appendChild(checkbox);

      // Append cells to the row
      row.appendChild(columnNameCell);
      row.appendChild(selectedFieldCell);
      row.appendChild(checkboxCell);

      // Append the row to the table body
      tbody.appendChild(row);
    }

    function uploadTemplateFile() {
      $('#existing-config').val('0');
      var formData = new FormData();
      var fileInput = document.getElementById('template-file');
      var fileType = $('#file-type').val();
      var dataSeparator = $('#data-separator').val();

      formData.append('templateFile', fileInput.files[0]);
      formData.append('action', "uploadTemplate");
      formData.append('uploadTemplate', "uploadTemplate");
      formData.append('fileType', fileType);
      formData.append('dataSeparator', dataSeparator);

      var xhr = new XMLHttpRequest();
      xhr.open('POST', '${pageContext.request.contextPath}/aim/dataImporter.do', true);
      xhr.setRequestHeader("Accept", "application/json, text/html");

      xhr.onload = function () {
        if (xhr.status === 200) {
          if (xhr.responseText && xhr.responseText.trim().length >= 1) {
            var ct = xhr.getResponseHeader("Content-Type") || "";
            if (ct.indexOf("application/json") !== -1) {
              try {
                var data = JSON.parse(xhr.responseText);
                renderTemplateSheetAndColumns(data);
              } catch (e) {
                console.error("Invalid JSON response", e);
                alert("Unable to parse template. Please try again.");
                return;
              }
            } else {
              document.getElementById('headers').innerHTML = xhr.responseText;
            }
            alert("The template has been successfully uploaded.");
            revealConfigWorkspace();
          } else {
            console.error("Unable to extract headers. Please check the file format and try again.");
            alert("Unable to extract headers. Please check the file format and try again.");
          }
        } else {
          console.error('Error:', xhr.status);
          alert("File upload failed. Please try again.");
        }
      };

      initializeSelectControls();
      xhr.send(formData);
    }

    function renderTemplateSheetAndColumns(data) {
      var sheetNames = data.sheetNames || [];
      var columnsBySheet = data.columnsBySheet || {};
      var headersDiv = document.getElementById('headers');
      if (sheetNames.length === 0) {
        headersDiv.innerHTML = '<p>No sheets found in the template.</p>';
        return;
      }
      var firstSheet = sheetNames[0];
      var html = '<label for="template-sheet">Select Sheet:</label><br>';
      html += '<select class="select2" style="width: 300px;" id="template-sheet">';
      for (var i = 0; i < sheetNames.length; i++) {
        html += '<option value="' + escapeHtml(sheetNames[i]) + '">' + escapeHtml(sheetNames[i]) + '</option>';
      }
      html += '</select><br><br>';
      html += '<label for="columnName">Select Column Name:</label><br>';
      html += '<select class="select2" style="width: 300px;" id="columnName">';
      var firstCols = columnsBySheet[firstSheet] || [];
      for (var j = 0; j < firstCols.length; j++) {
        html += '<option>' + escapeHtml(firstCols[j]) + '</option>';
      }
      html += '</select>';
      headersDiv.innerHTML = html;

      window._templateColumnsBySheet = columnsBySheet;
      $('#template-sheet').off('change.templateColumns').on('change.templateColumns', function() {
        var sheet = $(this).val();
        var cols = window._templateColumnsBySheet[sheet] || [];
        var $colSelect = $('#columnName');
        $colSelect.empty();
        for (var k = 0; k < cols.length; k++) {
          $colSelect.append($('<option></option>').text(cols[k]));
        }
        $colSelect.trigger('change.select2');
      });
    }

    function escapeHtml(text) {
      var div = document.createElement('div');
      div.textContent = text;
      return div.innerHTML;
    }

    function uploadDataFile() {
      var formData = new FormData();
      var fileType = $('#file-type').val();
      var internal = $('#internal').prop('checked');
      var skipExisting = $('#skipExisting').prop('checked');
      var validateActivities = $('#validateActivities').prop('checked');
      var addDisbursementForCommitment = $('#addDisbursementForCommitment').prop('checked');
      var createMissingOrgs = $('#createMissingOrgs').prop('checked');
      var orgGroupId = $('#orgGroupId').val();
      console.log("Internal", internal);
      console.log("Skip existing", skipExisting);
      console.log("Validate activities", validateActivities);
      console.log("Add disbursement for commitment", addDisbursementForCommitment);
      console.log("Create missing orgs", createMissingOrgs);
      console.log("Org group id", orgGroupId);
      if (createMissingOrgs && !orgGroupId) {
        alert("Please select an Organization Group for newly created organizations.");
        return;
      }
      var dataSeparator = $('#data-separator').val();
      var currentConfigName = $('#current-config-name').val();
      var existingConfig = (currentConfigName && currentConfigName.trim() !== '') ? currentConfigName.trim() : $('#existing-config').val();
      console.log("Existing configuration: "  + existingConfig);
      var fileInput = document.getElementById('data-file');
      // Check if a file is selected
      if (!fileInput.files.length) {
        alert("Please select a file to upload.");
        return;
      }
      var dataSheetChoice = $('input[name="dataSheetChoice"]:checked').val();
      var dataSheetName = $('#data-sheet').val() || '';
      if (fileType === 'excel' && dataSheetChoice === 'sheet' && !dataSheetName) {
        alert("Please load sheets and select a sheet, or choose 'Whole file'.");
        return;
      }
      formData.append('dataFile', fileInput.files[0]);
      formData.append('internal', internal);
      formData.append('skipExisting', skipExisting);
      formData.append('validateActivities', validateActivities);
      formData.append('addDisbursementForCommitment', addDisbursementForCommitment);
      formData.append('createMissingOrgs', createMissingOrgs);
      if (createMissingOrgs && orgGroupId) {
        formData.append('orgGroupId', orgGroupId);
      }
      formData.append('action',"uploadDataFile");
      formData.append('fileType', fileType);
      formData.append('dataSeparator', dataSeparator);
      formData.append('existingConfig', existingConfig);
      if (currentConfigName && currentConfigName.trim() !== '') {
        formData.append('configName', currentConfigName.trim());
      }
      formData.append('dataSheetChoice', dataSheetChoice || 'all');
      formData.append('dataSheetName', dataSheetName);

      var xhr = new XMLHttpRequest();
      xhr.open('POST', '${pageContext.request.contextPath}/aim/dataImporter.do', true);
      alert("File is uploading and will be parsed shortly.");
      xhr.onload = function () {
        console.log("Status: " + xhr.status);
        if (xhr.status === 400) {
          console.error("Unable to parse the file. Please check the file format and try again.");
          alert( xhr.getResponseHeader('errorMessage'));
        }
        if (xhr.status === 200) {
          console.log("File Parsed successfully")
        } else {
          console.error('Error:', xhr.status);
        }
      };
      xhr.send(formData);
      // window.location.href = "/aim/showDesktop.do";
    }
  </script>
  <style>
    :root {
      --page-bg: linear-gradient(180deg, #f7f2e8 0%, #eef6f5 50%, #f8fbff 100%);
      --panel-bg: rgba(255, 255, 255, 0.9);
      --panel-border: rgba(25, 57, 71, 0.12);
      --text-strong: #163543;
      --text-soft: #58707c;
      --accent: #0d7c86;
      --accent-deep: #0a5c66;
      --accent-warm: #d77a35;
      --surface-muted: #eef5f6;
      --row-alt: #f8fbfb;
      --shadow: 0 24px 60px rgba(19, 42, 53, 0.12);
      --radius-lg: 24px;
      --radius-md: 16px;
      --radius-sm: 12px;
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

    .importer-page {
      max-width: 1180px;
      margin: 0 auto;
      padding: 40px 20px 56px;
    }

    .hero-card,
    .panel-card,
    .workspace-card,
    .upload-stage {
      background: var(--panel-bg);
      border: 1px solid var(--panel-border);
      box-shadow: var(--shadow);
      backdrop-filter: blur(6px);
    }

    .hero-card {
      border-radius: 32px;
      padding: 36px;
      margin-bottom: 24px;
      background:
        radial-gradient(circle at top right, rgba(13, 124, 134, 0.16), transparent 34%),
        radial-gradient(circle at top left, rgba(215, 122, 53, 0.14), transparent 32%),
        rgba(255, 255, 255, 0.92);
    }

    .hero-card h1,
    .workspace-card h2,
    .upload-stage h3 {
      margin: 0 0 10px;
      font-weight: 700;
      letter-spacing: 0.02em;
    }

    .hero-card p,
    .section-copy,
    .helper-note {
      color: var(--text-soft);
      line-height: 1.6;
      margin: 0;
    }

    .panel-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 20px;
      margin-bottom: 24px;
    }

    .panel-card,
    .workspace-card,
    .upload-stage {
      border-radius: var(--radius-lg);
      padding: 24px;
    }

    .workspace-card {
      margin-top: 24px;
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

    label {
      display: inline-block;
      margin-bottom: 6px;
      font-weight: 700;
      color: var(--text-strong);
    }

    input[type="text"],
    input[type="file"],
    select {
      width: 100%;
      max-width: 100%;
      box-sizing: border-box;
      padding: 12px 14px;
      border-radius: var(--radius-sm);
      border: 1px solid rgba(22, 53, 67, 0.18);
      background: #fff;
      color: var(--text-strong);
    }

    input[type="file"] {
      padding: 10px 12px;
      background: var(--surface-muted);
    }

    input[type="button"],
    button {
      border: none;
      border-radius: 999px;
      padding: 12px 18px;
      font-weight: 700;
      cursor: pointer;
      color: #fff;
      background: linear-gradient(135deg, var(--accent) 0%, var(--accent-deep) 100%);
      box-shadow: 0 14px 28px rgba(13, 124, 134, 0.18);
      transition: transform 0.18s ease, box-shadow 0.18s ease, opacity 0.18s ease;
    }

    input[type="button"]:hover,
    button:hover {
      transform: translateY(-1px);
      box-shadow: 0 18px 34px rgba(13, 124, 134, 0.22);
    }

    .remove-row {
      background: linear-gradient(135deg, #b24747 0%, #8b2e2e 100%);
      box-shadow: 0 14px 28px rgba(139, 46, 46, 0.18);
    }

    .inline-field,
    .toggle-grid,
    .sheet-choice-card {
      margin-top: 16px;
    }

    .toggle-grid {
      display: grid;
      gap: 10px;
      margin-top: 18px;
      padding: 18px;
      border-radius: var(--radius-md);
      background: linear-gradient(180deg, rgba(13, 124, 134, 0.06), rgba(13, 124, 134, 0.02));
    }

    .toggle-item {
      display: flex;
      align-items: center;
      gap: 10px;
      color: var(--text-strong);
    }

    .toggle-item input {
      width: auto;
      margin: 0;
    }

    .fields-table,
    #import-projects-table,
    .records-table {
      width: 100%;
      border-collapse: separate;
      border-spacing: 0;
      overflow: hidden;
      border-radius: 18px;
      background: #fff;
      border: 1px solid rgba(22, 53, 67, 0.1);
    }

    table th,
    table td {
      text-align: left;
      padding: 14px 16px;
      border-bottom: 1px solid rgba(22, 53, 67, 0.08);
    }

    table th {
      background: linear-gradient(180deg, #f3faf9 0%, #eaf4f5 100%);
      color: var(--text-strong);
      font-size: 12px;
      letter-spacing: 0.12em;
      text-transform: uppercase;
    }

    table tr:nth-child(even) {
      background: var(--row-alt);
    }

    table tr:hover {
      background: rgba(13, 124, 134, 0.06);
    }

    .mapping-toolbar {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 16px;
      align-items: end;
      margin-bottom: 18px;
    }

    .mapping-actions {
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
      margin-top: 16px;
    }

    .upload-stage {
      margin-top: 22px;
    }

    #config-empty-note {
      margin-top: 18px;
      padding: 16px 18px;
      border-radius: var(--radius-md);
      background: linear-gradient(180deg, rgba(215, 122, 53, 0.12), rgba(215, 122, 53, 0.06));
      color: #75431f;
    }

    .select2-container--default .select2-selection--single {
      height: 46px;
      border-radius: var(--radius-sm);
      border: 1px solid rgba(22, 53, 67, 0.18);
      padding: 8px 12px;
      display: flex;
      align-items: center;
      background: #fff;
    }

    .select2-container--default .select2-selection--single .select2-selection__rendered {
      color: var(--text-strong);
      line-height: 28px;
      padding-left: 0;
    }

    .select2-dropdown {
      border-radius: 14px;
      border-color: rgba(22, 53, 67, 0.18);
      overflow: hidden;
    }

    .select2-search__field {
      border-radius: 10px;
      padding: 8px 10px;
    }

    @media (max-width: 768px) {
      .importer-page {
        padding: 24px 14px 40px;
      }

      .hero-card,
      .panel-card,
      .workspace-card,
      .upload-stage {
        padding: 18px;
        border-radius: 18px;
      }

      .mapping-toolbar {
        grid-template-columns: 1fr;
      }
    }
  </style>
</head>
<body>
<div class="importer-page">
  <div class="hero-card">
    <span class="section-label">Import Workspace</span>
    <h1>Import Data</h1>
    <p>Prepare a template, map source columns to AMP entity fields, and upload your data only after the configuration table is ready.</p>
  </div>

  <div class="panel-grid">
    <div class="panel-card">
      <span class="section-label">Step 1</span>
      <h2>Choose File Settings</h2>
      <p class="section-copy">Pick the source format and optionally load an existing configuration before uploading a template.</p>

      <div class="inline-field">
        <label class="file-type-label" for="file-type">Select file type</label>
        <select id="file-type" class="file_type">
          <option value="excel">Excel</option>
          <option value="csv">CSV</option>
          <option value="text">Text</option>
          <option value="json">JSON</option>
          <option value="xml">XML</option>
        </select>
      </div>

      <div id="separator-div" class="inline-field" hidden="hidden">
        <label for="data-separator">Column Separator</label>
        <select id="data-separator">
          <option value=",">Comma(,)</option>
          <option value="|">Vertical Line(|)</option>
          <option value="||">Pipe(||)</option>
          <option value=" ">Space</option>
        </select>
      </div>
    </div>

    <div class="panel-card">
      <span class="section-label">Step 2</span>
      <h2>Load Configuration</h2>
      <p class="section-copy">Resume from a saved mapping or start with a fresh template upload.</p>

      <div class="inline-field">
        <label for="configuration">Select Existing Configuration by name</label>
        <select id="configuration" class="existing-config" style="width: 100%;">
          <option value="none">None</option>
          <jsp:useBean id="configNames" scope="request" type="java.util.List"/>
          <c:forEach items="${configNames}" var="configName" varStatus="loop">
            <option value="${configName}">${configName}</option>
            <br>
          </c:forEach>
        </select>
      </div>

      <form id="templateUploadForm" enctype="multipart/form-data" class="inline-field">
        <label for="template-file">Select Template File</label>
        <input id="template-file" type="file" accept=".xls,.xlsx,.csv" name="templateFile" />
        <div class="mapping-actions">
          <input type="button" value="Upload Template" onclick="uploadTemplateFile()" />
        </div>
      </form>
    </div>
  </div>

  <div id="otherComponents" class="workspace-card" hidden>
    <html:form action="${pageContext.request.contextPath}/aim/dataImporter.do" method="post" enctype="multipart/form-data">
      <input type="hidden" id="current-config-name" value="">

      <span class="section-label">Step 3</span>
      <h2>Build Your Mapping</h2>
      <p class="section-copy">Search entity fields, add mappings, and review the configuration table before uploading the actual data file.</p>

      <div id="headers" class="inline-field"></div>

      <div class="mapping-toolbar">
        <div id="add-pair-edit-section" style="display: none;">
          <label for="column-name-edit">Column name for new pair</label>
          <input type="text" id="column-name-edit" placeholder="e.g. Column A">
        </div>

        <div>
          <label for="selected-field">Select Entity Field</label>
          <select id="selected-field" class="select2" style="width: 100%;">
            <jsp:useBean id="fieldsInfo" scope="request" type="java.util.Map"/>
            <c:forEach items="${fieldsInfo}" var="fieldEntry">
              <option value="${fieldEntry.key}">${fieldEntry.value}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="mapping-actions">
        <input type="button" id="add-field" value="Add Field" onclick="addField()">
        <input type="button" value="Remove Selected Rows" class="remove-row">
      </div>

      <table class="fields-table">
        <thead>
        <tr>
          <th>Column Name</th>
          <th>Selected Field</th>
          <th>Action</th>
        </tr>
        </thead>
        <tbody id="selected-pairs-table-body">
        </tbody>
      </table>

      <div id="config-empty-note" class="helper-note">
        Add at least one column-to-field pair to unlock data file upload.
      </div>

      <div id="data-upload-section" class="upload-stage" style="display: none;">
        <span class="section-label">Step 4</span>
        <h3>Upload Data File</h3>
        <p class="section-copy">This section appears only when the configuration table contains mappings.</p>

        <label id="select-file-label" for="data-file">Select Excel File</label>
        <input id="data-file" type="file" accept=".xls,.xlsx,.csv" name="dataFile" />

        <div id="data-sheet-choice-div" class="sheet-choice-card" style="display: none;">
          <label>Process data from</label><br>
          <input type="radio" name="dataSheetChoice" id="data-sheet-choice-all" value="all" checked> <label for="data-sheet-choice-all">Whole file (all sheets)</label><br>
          <input type="radio" name="dataSheetChoice" id="data-sheet-choice-sheet" value="sheet"> <label for="data-sheet-choice-sheet">Specific sheet</label><br>
          <div id="data-sheet-select-wrap" style="display: none; margin-top: 8px;">
            <input type="button" id="load-sheets-btn" value="Load sheets from file">
            <select id="data-sheet" style="width: 100%; margin-top: 10px;" disabled title="Select a file and click Load sheets">
              <option value="">-- Select sheet --</option>
            </select>
          </div>
        </div>

        <input type="text" id="existing-config" hidden="hidden"/>

        <div class="toggle-grid">
          <label class="toggle-item" for="internal"><input type="checkbox" id="internal" name="internal"> Internal</label>
          <label class="toggle-item" for="skipExisting"><input type="checkbox" id="skipExisting" name="skipExisting"> Skip existing activities (only insert new)</label>
          <label class="toggle-item" for="validateActivities"><input type="checkbox" id="validateActivities" name="validateActivities"> Validate imported activities (set as approved, non-draft)</label>
          <label class="toggle-item" for="addDisbursementForCommitment"><input type="checkbox" id="addDisbursementForCommitment" name="addDisbursementForCommitment"> Add Disbursement for any Commitment</label>
          <label class="toggle-item" for="createMissingOrgs"><input type="checkbox" id="createMissingOrgs" name="createMissingOrgs"> Create missing organizations</label>
        </div>

        <div id="orgGroupDiv" style="display:none; margin-top:16px;">
          <label for="orgGroupId">Organization Group for new organizations</label>
          <select id="orgGroupId" name="orgGroupId" style="width: 100%;">
            <option value="">-- Select Organization Group --</option>
            <c:forEach var="orgGroup" items="${orgGroups}">
              <option value="${orgGroup.ampOrgGrpId}">${orgGroup.orgGrpName}</option>
            </c:forEach>
          </select>
        </div>

        <div class="mapping-actions">
          <input type="button" value="Upload" onclick="uploadDataFile()">
        </div>
      </div>
    </html:form>
  </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0/js/select2.min.js"></script>
<script>
  $(document).ready(function() {
    $('#createMissingOrgs').change(function() {
      if ($(this).is(':checked')) {
        $('#orgGroupDiv').show();
      } else {
        $('#orgGroupDiv').hide();
        $('#orgGroupId').val('');
      }
    });
  });
</script>

</body>
</html:html>
