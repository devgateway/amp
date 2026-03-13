<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html:html>
<head>
  <title>Data Importer</title>
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;700&family=Source+Sans+3:wght@400;600;700&display=swap" rel="stylesheet" />
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

    function initEnhancedSelects(scope) {
      var $scope = scope ? $(scope) : $(document);
      $scope.find('#selected-field, #configuration, #template-sheet, #columnName, #data-sheet, #orgGroupId').each(function() {
        var $element = $(this);
        if ($element.hasClass('select2-hidden-accessible')) {
          return;
        }
        $element.select2({
          width: '100%',
          placeholder: $element.attr('data-placeholder') || 'Search and select',
          allowClear: false
        });
      });
    }

    function showMappingWorkspace() {
      $('#otherComponents').prop('hidden', false).show();
      $('.table-panel').show();
      $('#add-field').show();
      $('.remove-row').show();
      $('#selected-field').show();
    }

    function hideMappingWorkspaceIfEmpty() {
      if (!hasMappedPairs() && !$('#headers').html().trim().length && !$('#current-config-name').val()) {
        $('#otherComponents').prop('hidden', true).hide();
      }
    }

    function hasMappedPairs() {
      return $('#selected-pairs-table-body tr').length > 0;
    }

    function refreshImporterVisibility() {
      var hasPairs = hasMappedPairs();
      if (hasPairs || $('#headers').html().trim().length > 0 || $('#current-config-name').val()) {
        showMappingWorkspace();
      }
      if (hasPairs) {
        $('#data-upload-panel').show();
        $('#mapping-status').text($('#selected-pairs-table-body tr').length + ' mapped column pairs ready for import.');
      } else {
        $('#data-upload-panel').hide();
        $('#mapping-status').text('Add at least one column pair to enable data upload.');
        hideMappingWorkspaceIfEmpty();
      }
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
      initEnhancedSelects();
      if ($('#file-type').val() === 'excel') {
        $('#data-sheet-choice-div').show();
      }
      refreshImporterVisibility();
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
        refreshImporterVisibility();
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

                  var tbody = document.getElementById("selected-pairs-table-body");
                  tbody.innerHTML = "";

                  for (var key in updatedMap) {
                    if (updatedMap.hasOwnProperty(key)) {
                      var value = updatedMap[key];
                      updateTable(key, value, tbody);
                      console.log('Key:', key, 'Value:', value);
                    }
                  }
                  showMappingWorkspace();
                  $('#add-field').show();
                  $('.remove-row').show();
                  $('#selected-field').show();
                  $('#add-pair-edit-section').show();
                  $('#column-name-edit').val('');
                  refreshImporterVisibility();

                })
                .catch(error => {
                  console.error("There was a problem with the fetch operation:", error);
                });
        }else
        {
          $('#current-config-name').val('');
          $('#add-pair-edit-section').hide();
          $("#templateUploadForm").show();
          refreshImporterVisibility();
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

                // Update UI or perform any additional actions if needed
                console.log("Selected pairs updated successfully.");
                console.log("Updated map received:", updatedMap);
                var tbody = document.getElementById("selected-pairs-table-body");

                // Remove all rows from the table body
                tbody.innerHTML = "";

                for (var key in updatedMap) {
                  if (updatedMap.hasOwnProperty(key)) {
                    // Access each property using the key
                    var value = updatedMap[key];
                    updateTable(key, value, tbody);
                    console.log('Key:', key, 'Value:', value);
                  }
                }
                refreshImporterVisibility();
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
            showMappingWorkspace();
            $('#add-field').show();
            $('.remove-row').show();
            $('#selected-field').show();
            initEnhancedSelects('#headers');
            refreshImporterVisibility();
          } else {
            console.error("Unable to extract headers. Please check the file format and try again.");
            alert("Unable to extract headers. Please check the file format and try again.");
          }
        } else {
          console.error('Error:', xhr.status);
          alert("File upload failed. Please try again.");
        }
      };

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
      initEnhancedSelects('#headers');

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
      --page-bg: linear-gradient(135deg, #f4efe4 0%, #eef6f4 52%, #f9fbfd 100%);
      --panel-bg: rgba(255, 255, 255, 0.88);
      --panel-border: rgba(17, 64, 79, 0.12);
      --panel-shadow: 0 18px 40px rgba(24, 56, 62, 0.12);
      --ink-strong: #17343b;
      --ink-soft: #4e666c;
      --accent: #0d7a6f;
      --accent-deep: #0b5f57;
      --accent-warm: #d38b3b;
      --line: #d9e5e6;
      --danger: #a33f3f;
      --success: #2b7a59;
      --radius-lg: 22px;
      --radius-md: 16px;
      --radius-sm: 12px;
    }

    * {
      box-sizing: border-box;
    }

    body {
      margin: 0;
      padding: 36px 24px 56px;
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
      max-width: 1220px;
      margin: 0 auto;
    }

    .hero {
      display: grid;
      grid-template-columns: 1.35fr 0.9fr;
      gap: 24px;
      align-items: stretch;
      margin-bottom: 26px;
    }

    .hero-card,
    .panel,
    .table-panel,
    .upload-panel {
      background: var(--panel-bg);
      backdrop-filter: blur(10px);
      border: 1px solid var(--panel-border);
      border-radius: var(--radius-lg);
      box-shadow: var(--panel-shadow);
    }

    .hero-card {
      padding: 30px 32px;
      position: relative;
      overflow: hidden;
    }

    .hero-card::after {
      content: '';
      position: absolute;
      inset: auto -80px -80px auto;
      width: 240px;
      height: 240px;
      background: radial-gradient(circle, rgba(211, 139, 59, 0.18) 0%, rgba(211, 139, 59, 0) 68%);
      pointer-events: none;
    }

    .hero-title {
      font-size: 2.3rem;
      margin-bottom: 10px;
    }

    .hero-copy {
      color: var(--ink-soft);
      font-size: 1.06rem;
      line-height: 1.55;
      max-width: 62ch;
      margin-bottom: 0;
    }

    .hero-metrics {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 14px;
      margin-top: 22px;
    }

    .metric {
      padding: 16px 18px;
      border-radius: var(--radius-md);
      background: linear-gradient(180deg, rgba(13, 122, 111, 0.08), rgba(13, 122, 111, 0.02));
      border: 1px solid rgba(13, 122, 111, 0.12);
    }

    .metric-label {
      display: block;
      color: var(--ink-soft);
      font-size: 0.9rem;
      margin-bottom: 6px;
    }

    .metric-value {
      font-family: 'Space Grotesk', sans-serif;
      font-size: 1.1rem;
    }

    .layout-grid {
      display: grid;
      gap: 22px;
    }

    .panel,
    .table-panel,
    .upload-panel {
      padding: 24px;
    }

    .panel-title {
      font-size: 1.25rem;
      margin-bottom: 8px;
    }

    .panel-copy,
    .status-copy,
    .helper-copy {
      color: var(--ink-soft);
      margin: 0 0 18px;
      line-height: 1.5;
    }

    .form-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 18px;
      align-items: end;
    }

    .field-block {
      min-width: 0;
    }

    .field-block label,
    .toggle-stack label:first-child,
    .headers-panel label,
    #add-pair-edit-section label,
    .sheet-choice label {
      display: inline-block;
      font-weight: 700;
      margin-bottom: 8px;
    }

    .field-block input[type="text"],
    .field-block input[type="file"],
    .field-block select,
    #column-name-edit,
    #data-file,
    #template-file,
    #orgGroupId,
    #data-sheet {
      width: 100%;
      min-height: 46px;
      padding: 11px 14px;
      border: 1px solid var(--line);
      border-radius: var(--radius-sm);
      background: rgba(255, 255, 255, 0.96);
      color: var(--ink-strong);
    }

    .inline-actions {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      align-items: center;
    }

    .btn,
    input[type="button"],
    button {
      appearance: none;
      border: 0;
      border-radius: 999px;
      padding: 12px 18px;
      font-family: 'Space Grotesk', sans-serif;
      font-size: 0.95rem;
      cursor: pointer;
      transition: transform 140ms ease, box-shadow 140ms ease, background 140ms ease;
    }

    .btn-primary,
    #add-field,
    input[value="Upload Template"],
    input[value="Upload"],
    #load-sheets-btn {
      background: linear-gradient(135deg, var(--accent) 0%, var(--accent-deep) 100%);
      color: #fff;
      box-shadow: 0 14px 22px rgba(11, 95, 87, 0.22);
    }

    .btn-secondary,
    .remove-row {
      background: rgba(23, 52, 59, 0.08);
      color: var(--ink-strong);
    }

    .btn-warm {
      background: linear-gradient(135deg, #f2b562 0%, var(--accent-warm) 100%);
      color: #fff;
      box-shadow: 0 14px 22px rgba(211, 139, 59, 0.22);
    }

    .btn:hover,
    input[type="button"]:hover,
    button:hover {
      transform: translateY(-1px);
    }

    .mapping-layout {
      display: grid;
      grid-template-columns: minmax(0, 0.88fr) minmax(0, 1.12fr);
      gap: 22px;
      align-items: start;
    }

    .headers-panel {
      min-height: 100%;
      padding: 22px;
      background: linear-gradient(180deg, rgba(255,255,255,0.92), rgba(247,250,250,0.92));
      border: 1px solid var(--line);
      border-radius: var(--radius-md);
    }

    .table-panel table {
      width: 100%;
      border-collapse: separate;
      border-spacing: 0;
      overflow: hidden;
      border-radius: 16px;
    }

    .table-panel thead th {
      background: #17343b;
      color: #fff;
      font-family: 'Space Grotesk', sans-serif;
      font-weight: 500;
      letter-spacing: 0.02em;
      border: 0;
      padding: 14px 16px;
    }

    .table-panel tbody td {
      padding: 14px 16px;
      border-bottom: 1px solid #e8efef;
      background: rgba(255, 255, 255, 0.94);
    }

    .table-panel tbody tr:nth-child(even) td {
      background: rgba(242, 248, 248, 0.94);
    }

    .table-panel tbody tr:hover td {
      background: rgba(225, 241, 239, 0.94);
    }

    .status-pill {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      padding: 8px 12px;
      border-radius: 999px;
      background: rgba(13, 122, 111, 0.08);
      color: var(--accent-deep);
      font-weight: 700;
      margin-bottom: 16px;
    }

    .toggle-stack {
      display: grid;
      gap: 12px;
      margin-top: 12px;
    }

    .toggle-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 16px;
      padding: 13px 16px;
      border: 1px solid var(--line);
      border-radius: var(--radius-sm);
      background: rgba(252, 253, 253, 0.94);
    }

    .toggle-item strong {
      display: block;
      margin-bottom: 2px;
    }

    .toggle-item span {
      color: var(--ink-soft);
      font-size: 0.95rem;
    }

    .toggle-item input[type="checkbox"] {
      width: 20px;
      height: 20px;
      accent-color: var(--accent);
      flex: 0 0 auto;
    }

    .sheet-choice {
      padding: 18px;
      border: 1px solid var(--line);
      border-radius: var(--radius-md);
      background: rgba(248, 251, 251, 0.92);
    }

    .sheet-choice .inline-actions {
      margin-top: 10px;
    }

    #orgGroupDiv {
      margin: 12px 0 0;
      padding: 14px;
      border-radius: var(--radius-sm);
      background: rgba(13, 122, 111, 0.06);
      border: 1px solid rgba(13, 122, 111, 0.12);
    }

    .select2-container--default .select2-selection--single {
      min-height: 46px;
      border: 1px solid var(--line);
      border-radius: var(--radius-sm);
      padding: 8px 12px;
      background: rgba(255, 255, 255, 0.96);
    }

    .select2-container .select2-selection__rendered {
      line-height: 28px !important;
      color: var(--ink-strong) !important;
    }

    .select2-container .select2-selection__arrow {
      height: 44px !important;
    }

    .select2-dropdown {
      border: 1px solid var(--line) !important;
      border-radius: 12px !important;
      overflow: hidden;
    }

    @media (max-width: 980px) {
      .hero,
      .mapping-layout {
        grid-template-columns: 1fr;
      }

      body {
        padding: 24px 16px 40px;
      }
    }
  </style>
</head>
<body>
<div class="page-shell">
  <section class="hero">
    <div class="hero-card">
      <h1 class="hero-title">Import Data</h1>
      <p class="hero-copy">Configure a template, map source columns to AMP entity fields, and launch imports from a cleaner workspace built for high-volume data handling.</p>
      <div class="hero-metrics">
        <div class="metric">
          <span class="metric-label">Step 1</span>
          <span class="metric-value">Choose file type and configuration</span>
        </div>
        <div class="metric">
          <span class="metric-label">Step 2</span>
          <span class="metric-value">Map template columns to entity fields</span>
        </div>
        <div class="metric">
          <span class="metric-label">Step 3</span>
          <span class="metric-value">Upload source data when mappings are ready</span>
        </div>
        <div class="metric">
          <span class="metric-label">Built for</span>
          <span class="metric-value">Excel, CSV, text, JSON and XML workflows</span>
        </div>
      </div>
    </div>
    <div class="panel">
      <h3 class="panel-title">Import Setup</h3>
      <p class="panel-copy">This page now keeps configuration work visible and only enables the data upload panel once column mappings exist.</p>
      <div class="form-grid">
        <div class="field-block">
          <label class="file-type-label" for="file-type">Select file type</label>
          <select id="file-type" class="file_type" data-placeholder="Choose file type">
            <option value="excel">Excel</option>
            <option value="csv">CSV</option>
            <option value="text">Text</option>
            <option value="json">JSON</option>
            <option value="xml">XML</option>
          </select>
        </div>
        <div id="separator-div" class="field-block" hidden="hidden">
          <label for="data-separator">Column Separator</label>
          <select id="data-separator" data-placeholder="Choose separator">
            <option value=",">Comma(,)</option>
            <option value="|">Vertical Line(|)</option>
            <option value="||">Pipe(||)</option>
            <option value=" ">Space</option>
          </select>
        </div>
        <div class="field-block">
          <label for="configuration">Select Existing Configuration by name</label>
          <select id="configuration" class="existing-config" data-placeholder="Search configuration">
            <option value="none">None</option>
            <jsp:useBean id="configNames" scope="request" type="java.util.List"/>
            <c:forEach items="${configNames}" var="configName" varStatus="loop">
              <option value="${configName}">${configName}</option>
              <br>
            </c:forEach>
          </select>
        </div>
      </div>
    </div>
  </section>

  <section class="layout-grid">
    <div class="panel">
      <h3 class="panel-title">Template Mapping</h3>
      <p class="panel-copy">Upload a template to inspect its headers, then pair each source column with an AMP entity field. The entity field selector is searchable.</p>
      <form id="templateUploadForm" enctype="multipart/form-data">
        <div class="form-grid">
          <div class="field-block">
            <label for="template-file">Select Template File</label>
            <input id="template-file" type="file" accept=".xls,.xlsx,.csv" name="templateFile" />
          </div>
          <div class="field-block inline-actions">
            <input type="button" class="btn btn-primary" value="Upload Template" onclick="uploadTemplateFile()" />
          </div>
        </div>
      </form>
    </div>

    <div id="otherComponents" hidden>
      <html:form action="${pageContext.request.contextPath}/aim/dataImporter.do" method="post" enctype="multipart/form-data">
        <input type="hidden" id="current-config-name" value="">

        <div class="mapping-layout">
          <div class="headers-panel">
            <h3 class="panel-title">Detected Template Columns</h3>
            <p class="helper-copy">Choose the source column you want to bind. When working from an existing configuration, you can keep adding new pairs without losing the old ones.</p>
            <div id="headers"></div>
          </div>

          <div class="table-panel">
            <span class="status-pill">Mapping Workspace</span>
            <p id="mapping-status" class="status-copy">Add at least one column pair to enable data upload.</p>
            <div id="add-pair-edit-section" style="display: none; margin-bottom: 12px;">
              <label for="column-name-edit">Column name (for new pair)</label>
              <input type="text" id="column-name-edit" placeholder="e.g. Column A">
            </div>

            <div class="form-grid">
              <div class="field-block">
                <label for="selected-field">Select Entity Field</label>
                <select id="selected-field" class="select2" data-placeholder="Search entity field">
                  <jsp:useBean id="fieldsInfo" scope="request" type="java.util.Map"/>
                  <c:forEach items="${fieldsInfo}" var="fieldEntry">
                    <option value="${fieldEntry.key}">${fieldEntry.value}</option>
                  </c:forEach>
                </select>
              </div>
              <div class="field-block inline-actions">
                <input type="button" id="add-field" class="btn btn-primary" value="Add Field" onclick="addField()">
              </div>
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

            <div class="inline-actions" style="margin-top: 18px;">
              <input type="button" value="Remove Selected Rows" class="btn btn-secondary remove-row">
            </div>
          </div>
        </div>

        <div id="data-upload-panel" class="upload-panel" style="display: none; margin-top: 22px;">
          <h3 class="panel-title">Upload Data File</h3>
          <p class="panel-copy">Once mappings are present, upload the data file using the same configuration. This section stays hidden until the mapping table has entries.</p>

          <div class="field-block" style="margin-bottom: 18px;">
            <label id="select-file-label" for="data-file">Select Excel File</label>
            <input id="data-file" type="file" accept=".xls,.xlsx,.csv" name="dataFile" />
          </div>

          <div id="data-sheet-choice-div" class="sheet-choice" style="display: none;">
            <label>Process data from</label><br>
            <input type="radio" name="dataSheetChoice" id="data-sheet-choice-all" value="all" checked> <label for="data-sheet-choice-all">Whole file (all sheets)</label><br>
            <input type="radio" name="dataSheetChoice" id="data-sheet-choice-sheet" value="sheet"> <label for="data-sheet-choice-sheet">Specific sheet</label><br>
            <div id="data-sheet-select-wrap" class="inline-actions" style="display: none; margin-top: 8px;">
              <input type="button" id="load-sheets-btn" class="btn btn-secondary" value="Load sheets from file">
              <select id="data-sheet" data-placeholder="Select sheet" disabled title="Select a file and click Load sheets">
                <option value="">-- Select sheet --</option>
              </select>
            </div>
          </div>

          <input type="text" id="existing-config" hidden="hidden"/>

          <div class="toggle-stack">
            <div class="toggle-item">
              <div>
                <strong>Internal</strong>
                <span>Use internal donor handling for the uploaded file.</span>
              </div>
              <input type="checkbox" id="internal" name="internal">
            </div>
            <div class="toggle-item">
              <div>
                <strong>Skip existing activities</strong>
                <span>Only insert new activities.</span>
              </div>
              <input type="checkbox" id="skipExisting" name="skipExisting">
            </div>
            <div class="toggle-item">
              <div>
                <strong>Validate imported activities</strong>
                <span>Set imported activities as approved and non-draft.</span>
              </div>
              <input type="checkbox" id="validateActivities" name="validateActivities">
            </div>
            <div class="toggle-item">
              <div>
                <strong>Add disbursement for commitment</strong>
                <span>Create a matching disbursement where commitment rows require it.</span>
              </div>
              <input type="checkbox" id="addDisbursementForCommitment" name="addDisbursementForCommitment">
            </div>
            <div class="toggle-item">
              <div>
                <strong>Create missing organizations</strong>
                <span>Allow the importer to create organizations that do not already exist.</span>
              </div>
              <input type="checkbox" id="createMissingOrgs" name="createMissingOrgs">
            </div>
          </div>

          <div id="orgGroupDiv" style="display:none;">
            <label for="orgGroupId">Organization Group for new organizations</label>
            <select id="orgGroupId" name="orgGroupId" data-placeholder="Search organization group">
              <option value="">-- Select Organization Group --</option>
              <c:forEach var="orgGroup" items="${orgGroups}">
                <option value="${orgGroup.ampOrgGrpId}">${orgGroup.orgGrpName}</option>
              </c:forEach>
            </select>
          </div>

          <div class="inline-actions" style="margin-top: 22px;">
            <input type="button" class="btn btn-warm" value="Upload" onclick="uploadDataFile()">
          </div>
        </div>
      </html:form>
    </div>
  </section>
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
