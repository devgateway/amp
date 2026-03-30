<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<html:html>
<head>
  <title><digi:trn>Data Importer</digi:trn></title>
  <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0/css/select2.min.css" rel="stylesheet" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

  <%
  // Prepare fields info as JSON for JavaScript
  java.util.Map<String, String> fieldsInfo = (java.util.Map<String, String>) request.getAttribute("fieldsInfo");
  String fieldsInfoJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(fieldsInfo);
  String fieldsInfoJsonEscaped = fieldsInfoJson
          .replace("\\", "\\\\")
          .replace("'", "\\'")
          .replace("\r", "\\r")
          .replace("\n", "\\n")
          .replace("</", "<\\/");
  request.setAttribute("fieldsInfoJsonEscaped", fieldsInfoJsonEscaped);
%>

<script>
    // Store the field translations for JavaScript use
    var fieldsInfoMap = JSON.parse('${fieldsInfoJsonEscaped}');

    function initializeSelectControls() {
      if (!window.jQuery || !$.fn || !$.fn.select2) {
        return;
      }

      function applySelect2(selector, options) {
        var $element = $(selector);
        if (!$element.length) {
          return;
        }
        if ($element.hasClass('select2-hidden-accessible')) {
          $element.select2('destroy');
        }
        $element.select2(options);
      }

      if ($('#configuration').length) {
        applySelect2('#configuration', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Choose an existing configuration</digi:trn>'
        });
      }

      if ($('#selected-field').length) {
        applySelect2('#selected-field', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Search entity fields</digi:trn>'
        });
      }

      if ($('#orgGroupId').length) {
        applySelect2('#orgGroupId', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Select organization group</digi:trn>'
        });
      }

      if ($('#defaultActivityStatusId').length) {
        applySelect2('#defaultActivityStatusId', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Use system default activity status</digi:trn>'
        });
      }

      if ($('#defaultLocationId').length) {
        applySelect2('#defaultLocationId', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Select fallback location</digi:trn>'
        });
      }

      if ($('#defaultProgramClassification').length) {
        applySelect2('#defaultProgramClassification', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Select fallback program classification</digi:trn>'
        });
      }

      if ($('#data-sheet').length) {
        applySelect2('#data-sheet', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Select sheet</digi:trn>'
        });
      }

      if ($('#template-sheet').length) {
        applySelect2('#template-sheet', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Select sheet</digi:trn>'
        });
      }

      if ($('#columnName').length) {
        applySelect2('#columnName', {
          width: '100%',
          placeholder: '<digi:trn jsFriendly="true">Search template columns</digi:trn>'
        });
      }
    }

    function toggleUploadSection() {
      var hasMappings = $('#selected-pairs-table-body tr').length > 0;
      $('#data-upload-section').toggle(hasMappings);
      $('#config-empty-note').toggle(!hasMappings);
      toggleDefaultLocationFallback();
    }

    function hasMappedProjectLocationField() {
      return $('#selected-pairs-table-body tr').filter(function() {
        return $(this).attr('data-selected-field') === 'Project Location';
      }).length > 0;
    }

    function toggleDefaultLocationFallback() {
      var hasMappings = $('#selected-pairs-table-body tr').length > 0;
      var showFallback = hasMappings && !hasMappedProjectLocationField();
      $('#default-location-fallback').toggle(showFallback);
      if (!showFallback) {
        var defaultLocationId = $('#defaultLocationId').attr('data-default-location-id');
        if (defaultLocationId) {
          $('#defaultLocationId').val(defaultLocationId).trigger('change.select2');
        }
      }
      toggleProgramClassificationFallback();
    }

    function hasMappedProgramField() {
      return $('#selected-pairs-table-body tr').filter(function() {
        return $(this).attr('data-selected-field') === 'Program Name';
      }).length > 0;
    }

    function hasMappedProgramClassificationField() {
      return $('#selected-pairs-table-body tr').filter(function() {
        return $(this).attr('data-selected-field') === 'Program Classification';
      }).length > 0;
    }

    function toggleProgramClassificationFallback() {
      var hasMappings = $('#selected-pairs-table-body tr').length > 0;
      var showFallback = hasMappings && hasMappedProgramField() && !hasMappedProgramClassificationField();
      $('#program-classification-fallback').toggle(showFallback);
      if (!showFallback) {
        $('#defaultProgramClassification').val('').trigger('change.select2');
      }
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
      var columnNameElement = document.getElementById("columnName");
      var columnName = columnNameElement ? columnNameElement.value : '';
      var selectedField = document.getElementById("selected-field").value;
      if (!columnName || !selectedField) {
        alert("<digi:trn jsFriendly='true'>Please select a column name and entity field.</digi:trn>");
        return;
      }
      sendValuesToBackend(columnName, selectedField, "addField");
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
        if (fileType === "json" || fileType === "xml") {
          alert("<digi:trn jsFriendly='true'>JSON and XML import are coming soon. Excel has been reselected.</digi:trn>");
          $(this).val('excel');
          fileType = 'excel';
        }
        if (fileType === "csv") {
          $('#select-file-label').html("<digi:trn jsFriendly='true'>Select CSV File</digi:trn>");
          $('#data-file').attr("accept", ".csv");
          $('#template-file').attr("accept", ".csv");
          $('#separator-div').hide();
          $('#data-sheet-choice-div').hide();
        } else if(fileType==="text") {
          $('#select-file-label').html("<digi:trn jsFriendly='true'>Select Text File</digi:trn>");
          $('#data-file').attr("accept", ".txt");
          $('#template-file').attr("accept", ".txt");
          $('#separator-div').show();
          $('#data-sheet-choice-div').hide();
        }
        else if(fileType==="excel") {
          $('#select-file-label').html("<digi:trn jsFriendly='true'>Select Excel File</digi:trn>");
          $('#data-file').attr("accept", ".xls,.xlsx");
          $('#template-file').attr("accept", ".xls,.xlsx");
          $('#separator-div').hide();
          $('#data-sheet-choice-div').show();
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
          alert("<digi:trn jsFriendly='true'>Please select a data file first.</digi:trn>");
          return;
        }
        var formData = new FormData();
        formData.append('dataFile', fileInput.files[0]);
        formData.append('action', 'getDataFileSheets');
        formData.append('fileType', $('#file-type').val());
        var $select = $('#data-sheet');
        $select.prop('disabled', true).empty().append('<option value=""><digi:trn jsFriendly="true">-- Loading... --</digi:trn></option>');
        fetch("${pageContext.request.contextPath}/aim/dataImporter.do", { method: "POST", body: formData })
          .then(function(r) { return r.json(); })
          .then(function(names) {
            $select.empty().append('<option value=""><digi:trn jsFriendly="true">-- Select Sheet --</digi:trn></option>');
            if (Array.isArray(names)) {
              names.forEach(function(name) {
                $select.append($('<option></option>').attr('value', name).text(name));
              });
              $select.prop('disabled', false);
            }
          })
          .catch(function() {
            $select.empty().append('<option value=""><digi:trn jsFriendly="true">-- Error Loading Sheets --</digi:trn></option>').prop('disabled', false);
            alert("<digi:trn jsFriendly='true'>Could not load sheets from file.</digi:trn>");
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

                })
                .catch(error => {
                  console.error("There was a problem with the fetch operation:", error);
                });
        }else
        {
          $('#current-config-name').val('');
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
      row.setAttribute('data-selected-field', selectedField);
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

    function hasMappedField(fieldName) {
      return $('#selected-pairs-table-body tr').filter(function() {
        return $(this).attr('data-selected-field') === fieldName;
      }).length > 0;
    }

    function hasAnyMappedOrgMissingGroupMapping() {
      if (hasMappedField('Organization Group')) {
        return false;
      }
      var roleMappings = [
        ['Donor Agency', 'Donor Organization Group'],
        ['Responsible Organization', 'Responsible Organization Group'],
        ['Beneficiary Agency', 'Beneficiary Agency Group'],
        ['Executing Agency', 'Executing Agency Group'],
        ['Implementing Agency', 'Implementing Agency Group'],
        ['Contracting Agency', 'Contracting Agency Group']
      ];
      for (var i = 0; i < roleMappings.length; i++) {
        var orgField = roleMappings[i][0];
        var groupField = roleMappings[i][1];
        if (hasMappedField(orgField) && !hasMappedField(groupField)) {
          return true;
        }
      }
      return false;
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
                alert("<digi:trn jsFriendly='true'>Unable to parse template. Please try again.</digi:trn>");
                return;
              }
            } else {
              document.getElementById('headers').innerHTML = xhr.responseText;
            }
            alert("<digi:trn jsFriendly='true'>The template has been successfully uploaded.</digi:trn>");
            revealConfigWorkspace();
          } else {
            console.error("Unable to extract headers. Please check the file format and try again.");
            alert("<digi:trn jsFriendly='true'>Unable to extract headers. Please check the file format and try again.</digi:trn>");
          }
        } else {
          console.error('Error:', xhr.status);
          alert("<digi:trn jsFriendly='true'>File upload failed. Please try again.</digi:trn>");
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
        headersDiv.innerHTML = '<p><digi:trn jsFriendly="true">No sheets found in the template.</digi:trn></p>';
        return;
      }
      var firstSheet = sheetNames[0];
      var html = '<label for="template-sheet"><digi:trn jsFriendly="true">Select Sheet</digi:trn>:</label><br>';
      html += '<select class="select2" style="width: 300px;" id="template-sheet">';
      for (var i = 0; i < sheetNames.length; i++) {
        html += '<option value="' + escapeHtml(sheetNames[i]) + '">' + escapeHtml(sheetNames[i]) + '</option>';
      }
      html += '</select><br><br>';
      html += '<label for="columnName"><digi:trn jsFriendly="true">Select Column Name</digi:trn>:</label><br>';
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
      var skipRecordsWithoutTransactions = $('#skipRecordsWithoutTransactions').prop('checked');
      var validateActivities = $('#validateActivities').prop('checked');
      var addDisbursementForCommitment = $('#addDisbursementForCommitment').prop('checked');
      var createMissingOrgs = $('#createMissingOrgs').prop('checked');
      var createMissingSectors = $('#createMissingSectors').prop('checked');
      var createMissingOrgGroups = $('#createMissingOrgGroups').prop('checked');
      var createMissingPrograms = $('#createMissingPrograms').prop('checked');
      var replaceExistingTransactions = $('#replaceExistingTransactions').prop('checked');
      var orgGroupId = $('#orgGroupId').val();
      var defaultActivityStatusId = $('#defaultActivityStatusId').val();
      var defaultLocationId = $('#defaultLocationId').val() || $('#defaultLocationId').attr('data-default-location-id') || '';
      var defaultProgramClassification = $('#defaultProgramClassification').val();
      var anyMappedOrgMissingGroupMapping = hasAnyMappedOrgMissingGroupMapping();
      var hasProjectLocationMapping = hasMappedProjectLocationField();
      var hasProgramMapping = hasMappedProgramField();
      var hasProgramClassificationMapping = hasMappedProgramClassificationField();
      console.log("Internal", internal);
      console.log("Skip existing", skipExisting);
      console.log("Skip records without transactions", skipRecordsWithoutTransactions);
      console.log("Validate activities", validateActivities);
      console.log("Add disbursement for commitment", addDisbursementForCommitment);
      console.log("Create missing orgs", createMissingOrgs);
      console.log("Create missing sectors", createMissingSectors);
      console.log("Create missing org groups", createMissingOrgGroups);
      console.log("Create missing programs", createMissingPrograms);
      console.log("Replace existing transactions", replaceExistingTransactions);
      console.log("Org group id", orgGroupId);
      console.log("Default activity status id", defaultActivityStatusId);
      console.log("Default location id", defaultLocationId);
      console.log("Default program classification", defaultProgramClassification);
      if (createMissingOrgs && !orgGroupId && !createMissingOrgGroups && anyMappedOrgMissingGroupMapping) {
        alert("<digi:trn jsFriendly='true'>Please select a fallback Organization Group (or enable organization-group creation) when any mapped organization field has no corresponding group mapping.</digi:trn>");
        return;
      }
      if (!hasProjectLocationMapping && !defaultLocationId) {
        alert("<digi:trn jsFriendly='true'>Please select a fallback location when no Project Location column is mapped.</digi:trn>");
        return;
      }
      if (hasProgramMapping && !hasProgramClassificationMapping && !defaultProgramClassification) {
        alert("<digi:trn jsFriendly='true'>Please select a default program classification when no Program Classification column is mapped.</digi:trn>");
        return;
      }
      var dataSeparator = $('#data-separator').val();
      var currentConfigName = $('#current-config-name').val();
      var existingConfig = (currentConfigName && currentConfigName.trim() !== '') ? currentConfigName.trim() : $('#existing-config').val();
      console.log("Existing configuration: "  + existingConfig);
      var fileInput = document.getElementById('data-file');
      // Check if a file is selected
      if (!fileInput.files.length) {
        alert("<digi:trn jsFriendly='true'>Please select a file to upload.</digi:trn>");
        return;
      }
      var dataSheetChoice = $('input[name="dataSheetChoice"]:checked').val();
      var dataSheetName = $('#data-sheet').val() || '';
      if (fileType === 'excel' && dataSheetChoice === 'sheet' && !dataSheetName) {
        alert("<digi:trn jsFriendly='true'>Please load sheets and select a sheet, or choose 'Whole file'.</digi:trn>");
        return;
      }
      formData.append('dataFile', fileInput.files[0]);
      formData.append('internal', internal);
      formData.append('skipExisting', skipExisting);
      formData.append('skipRecordsWithoutTransactions', skipRecordsWithoutTransactions);
      formData.append('validateActivities', validateActivities);
      formData.append('addDisbursementForCommitment', addDisbursementForCommitment);
      formData.append('createMissingOrgs', createMissingOrgs);
      formData.append('createMissingSectors', createMissingSectors);
      formData.append('createMissingOrgGroups', createMissingOrgGroups);
      formData.append('createMissingPrograms', createMissingPrograms);
      formData.append('replaceExistingTransactions', replaceExistingTransactions);
      if (createMissingOrgs && orgGroupId) {
        formData.append('orgGroupId', orgGroupId);
      }
      if (defaultActivityStatusId) {
        formData.append('defaultActivityStatusId', defaultActivityStatusId);
      }
      if (defaultLocationId) {
        formData.append('defaultLocationId', defaultLocationId);
      }
      if (defaultProgramClassification) {
        formData.append('defaultProgramClassification', defaultProgramClassification);
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
      var redirectedToProgress = false;
      function redirectToProgressPage() {
        if (redirectedToProgress) {
          return;
        }
        redirectedToProgress = true;
        window.location.href = '${pageContext.request.contextPath}/aim/viewImportProgress.do';
      }

      xhr.open('POST', '${pageContext.request.contextPath}/aim/dataImporter.do', true);
      alert("<digi:trn jsFriendly='true'>File is uploading and will be parsed shortly.</digi:trn>");
      if (xhr.upload) {
        xhr.upload.onload = function () {
          redirectToProgressPage();
        };
      }
      xhr.onload = function () {
        console.log("Status: " + xhr.status);
        if (xhr.status === 400) {
          console.error("Unable to parse the file. Please check the file format and try again.");
          alert( xhr.getResponseHeader('errorMessage'));
          redirectedToProgress = false;
        }
        if (xhr.status === 200) {
          console.log("File Parsed successfully")
        } else {
          console.error('Error:', xhr.status);
        }
      };
      xhr.send(formData);
    }
  </script>
  <style>
    :root {
      --page-bg: #f5faff;
      --panel-bg: #ffffff;
      --panel-border: #c8daea;
      --text-strong: #22313a;
      --text-soft: #4a6880;
      --accent: #21587b;
      --accent-deep: #164060;
      --accent-warm: #2480c6;
      --surface-muted: #e7f1fd;
      --row-alt: #f8f9fa;
      --shadow: 0 4px 12px rgba(25, 39, 52, 0.05);
      --radius-lg: 14px;
      --radius-md: 10px;
      --radius-sm: 6px;
    }

    html {
      scroll-behavior: smooth;
    }

    body {
      margin: 0;
      font-family: 'Open Sans', "Helvetica Neue", Helvetica, Arial, sans-serif;
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

    .importer-page {
      max-width: 1180px;
      margin: 0 auto;
      padding: 22px 16px 34px;
    }

    .hero-card,
    .panel-card,
    .workspace-card,
    .upload-stage {
      background: var(--panel-bg);
      border: 1px solid var(--panel-border);
      box-shadow: var(--shadow);
    }

    .hero-card {
      border-radius: 16px;
      padding: 20px;
      margin-bottom: 14px;
      background: var(--panel-bg);
    }

    .hero-card h1,
    .workspace-card h2,
    .upload-stage h3 {
      margin: 0 0 8px;
      font-size: 18px;
      font-weight: 700;
      letter-spacing: 0;
    }

    .hero-card p,
    .section-copy,
    .helper-note {
      color: var(--text-soft);
      line-height: 1.45;
      margin: 0;
      font-size: 12px;
    }

    .panel-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 14px;
      margin-bottom: 14px;
    }

    .panel-card,
    .workspace-card,
    .upload-stage {
      border-radius: var(--radius-lg);
      padding: 16px;
    }

    .workspace-card {
      margin-top: 14px;
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

    label {
      display: inline-block;
      margin-bottom: 4px;
      font-size: 12px;
      font-weight: 700;
      color: var(--text-strong);
    }

    input[type="text"],
    input[type="file"],
    select {
      width: 100%;
      max-width: 100%;
      box-sizing: border-box;
      padding: 8px 10px;
      border-radius: var(--radius-sm);
      border: 1px solid rgba(22, 53, 67, 0.18);
      background: #fff;
      color: var(--text-strong);
      font-size: 12px;
    }

    input[type="file"] {
      padding: 7px 9px;
      background: var(--surface-muted);
    }

    input[type="button"],
    button {
      border: 1px solid #21587b;
      border-radius: 999px;
      padding: 8px 14px;
      font-weight: 700;
      cursor: pointer;
      color: #fff;
      background: var(--accent);
      font-size: 12px;
      box-shadow: none;
      transition: background-color 0.18s ease, border-color 0.18s ease, color 0.18s ease;
    }

    input[type="button"]:hover,
    button:hover {
      background: var(--accent-deep);
      border-color: var(--accent-deep);
    }

    .remove-row {
      background: #6f5a5a;
      border-color: #6f5a5a;
    }

    .inline-field,
    .toggle-grid,
    .sheet-choice-card {
      margin-top: 12px;
    }

    .toggle-grid {
      display: grid;
      gap: 8px;
      margin-top: 14px;
      padding: 12px;
      border-radius: var(--radius-md);
      background: var(--surface-muted);
      border: 1px solid var(--panel-border);
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
      padding: 10px 12px;
      font-size: 12px;
      border-bottom: 1px solid rgba(22, 53, 67, 0.08);
    }

    table th {
      background: #d6e7fd;
      color: #21587b;
      font-size: 11px;
      letter-spacing: 0.08em;
      text-transform: uppercase;
    }

    table tr:nth-child(even) {
      background: var(--row-alt);
    }

    table tr:hover {
      background: #f2f5f6;
    }

    .mapping-toolbar {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 12px;
      align-items: end;
      margin-bottom: 12px;
    }

    .mapping-actions {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
      margin-top: 10px;
    }

    .upload-stage {
      margin-top: 14px;
    }

    #config-empty-note {
      margin-top: 12px;
      padding: 10px 12px;
      border-radius: var(--radius-md);
      background: #e7f1fd;
      color: #21587b;
      border: 1px solid #a3c4e4;
    }

    .select2-container--default .select2-selection--single {
      height: 34px;
      border-radius: var(--radius-sm);
      border: 1px solid rgba(22, 53, 67, 0.18);
      padding: 3px 8px;
      display: flex;
      align-items: center;
      background: #fff;
    }

    .select2-container--default .select2-selection--single .select2-selection__rendered {
      color: var(--text-strong);
      font-size: 12px;
      line-height: 24px;
      padding-left: 0;
    }

    .select2-dropdown {
      border-radius: 8px;
      border-color: rgba(22, 53, 67, 0.18);
      overflow: hidden;
    }

    .select2-search__field {
      border-radius: 6px;
      padding: 6px 8px;
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
    <span class="section-label"><digi:trn>Import Workspace</digi:trn></span>
    <h1><digi:trn>Import Data</digi:trn></h1>
    <p><digi:trn>Prepare a template, map source columns to AMP entity fields, and upload your data only after the configuration table is ready.</digi:trn></p>
  </div>

  <div class="panel-grid">
    <div class="panel-card">
      <span class="section-label"><digi:trn>Step 1</digi:trn></span>
      <h2><digi:trn>Choose File Settings</digi:trn></h2>
      <p class="section-copy"><digi:trn>Pick the source format and optionally load an existing configuration before uploading a template.</digi:trn></p>

      <div class="inline-field">
        <label class="file-type-label" for="file-type"><digi:trn>Select file type</digi:trn></label>
        <select id="file-type" class="file_type">
          <option value="excel"><digi:trn>Excel</digi:trn></option>
          <option value="csv"><digi:trn>CSV</digi:trn></option>
          <option value="text"><digi:trn>Text</digi:trn></option>
          <option value="json"><digi:trn>JSON</digi:trn></option>
          <option value="xml"><digi:trn>XML</digi:trn></option>
        </select>
      </div>

      <div id="separator-div" class="inline-field" hidden="hidden">
        <label for="data-separator"><digi:trn>Column Separator</digi:trn></label>
        <select id="data-separator">
          <option value=","><digi:trn>Comma(,)</digi:trn></option>
          <option value="|"><digi:trn>Vertical Line(|)</digi:trn></option>
          <option value="||"><digi:trn>Pipe(||)</digi:trn></option>
          <option value=" "><digi:trn>Space</digi:trn></option>
        </select>
      </div>
    </div>

    <div class="panel-card">
      <span class="section-label"><digi:trn>Step 2</digi:trn></span>
      <h2><digi:trn>Load Configuration</digi:trn></h2>
      <p class="section-copy"><digi:trn>Resume from a saved mapping or start with a fresh template upload.</digi:trn></p>

      <div class="inline-field">
        <label for="configuration"><digi:trn>Select Existing Configuration by name</digi:trn></label>
        <select id="configuration" class="existing-config" style="width: 100%;">
          <option value="none"><digi:trn>None</digi:trn></option>
          <jsp:useBean id="configNames" scope="request" type="java.util.List"/>
          <c:forEach items="${configNames}" var="configName" varStatus="loop">
            <option value="${configName}">${configName}</option>
            <br>
          </c:forEach>
        </select>
      </div>

      <form id="templateUploadForm" enctype="multipart/form-data" class="inline-field">
        <label for="template-file"><digi:trn>Select Template File</digi:trn></label>
        <input id="template-file" type="file" accept=".xls,.xlsx,.csv" name="templateFile" />
        <div class="mapping-actions">
          <input type="button" value="<digi:trn>Upload Template</digi:trn>" onclick="uploadTemplateFile()" />
        </div>
      </form>
    </div>
  </div>

  <div id="otherComponents" class="workspace-card" hidden>
    <html:form action="${pageContext.request.contextPath}/aim/dataImporter.do" method="post" enctype="multipart/form-data">
      <input type="hidden" id="current-config-name" value="">

      <span class="section-label"><digi:trn>Step 3</digi:trn></span>
      <h2><digi:trn>Build Your Mapping</digi:trn></h2>
      <p class="section-copy"><digi:trn>Search entity fields, add mappings, and review the configuration table before uploading the actual data file.</digi:trn></p>

      <div id="headers" class="inline-field"></div>

      <div class="mapping-toolbar">
        <div>
          <label for="columnName"><digi:trn>Select Column Name</digi:trn></label>
          <div class="helper-note" style="margin-bottom: 10px;"><digi:trn>Upload a template or load its sheet columns, then choose a source column from the searchable list.</digi:trn></div>
        </div>

        <div>
          <label for="selected-field"><digi:trn>Select Entity Field</digi:trn></label>
          <select id="selected-field" class="select2" style="width: 100%;">
            <jsp:useBean id="fieldsInfo" scope="request" type="java.util.Map"/>
            <c:forEach items="${fieldsInfo}" var="fieldEntry">
              <option value="${fieldEntry.key}">${fieldEntry.value}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="mapping-actions">
        <input type="button" id="add-field" value="<digi:trn>Add Field</digi:trn>" onclick="addField()">
        <input type="button" value="<digi:trn>Remove Selected Rows</digi:trn>" class="remove-row">
      </div>

      <table class="fields-table">
        <thead>
        <tr>
          <th><digi:trn>Column Name</digi:trn></th>
          <th><digi:trn>Selected Field</digi:trn></th>
          <th><digi:trn>Action</digi:trn></th>
        </tr>
        </thead>
        <tbody id="selected-pairs-table-body">
        </tbody>
      </table>

      <div id="config-empty-note" class="helper-note">
        <digi:trn>Add at least one column-to-field pair to unlock data file upload.</digi:trn>
      </div>

      <div id="data-upload-section" class="upload-stage" style="display: none;">
        <span class="section-label"><digi:trn>Step 4</digi:trn></span>
        <h3><digi:trn>Upload Data File</digi:trn></h3>
        <p class="section-copy"><digi:trn>This section appears only when the configuration table contains mappings.</digi:trn></p>

        <label id="select-file-label" for="data-file"><digi:trn>Select Excel File</digi:trn></label>
        <input id="data-file" type="file" accept=".xls,.xlsx,.csv" name="dataFile" />

        <div id="data-sheet-choice-div" class="sheet-choice-card" style="display: none;">
          <label><digi:trn>Process data from</digi:trn></label><br>
          <input type="radio" name="dataSheetChoice" id="data-sheet-choice-all" value="all" checked> <label for="data-sheet-choice-all"><digi:trn>Whole file (all sheets)</digi:trn></label><br>
          <input type="radio" name="dataSheetChoice" id="data-sheet-choice-sheet" value="sheet"> <label for="data-sheet-choice-sheet"><digi:trn>Specific sheet</digi:trn></label><br>
          <div id="data-sheet-select-wrap" style="display: none; margin-top: 8px;">
            <input type="button" id="load-sheets-btn" value="<digi:trn>Load sheets from file</digi:trn>">
            <select id="data-sheet" style="width: 100%; margin-top: 10px;" disabled title="<digi:trn>Select a file and click Load sheets</digi:trn>">
              <option value=""><digi:trn>-- Select sheet --</digi:trn></option>
            </select>
          </div>
        </div>

        <input type="text" id="existing-config" hidden="hidden"/>

        <div class="toggle-grid">
          <label class="toggle-item" for="internal"><input type="checkbox" id="internal" name="internal"> <digi:trn>Internal</digi:trn></label>
          <label class="toggle-item" for="skipExisting"><input type="checkbox" id="skipExisting" name="skipExisting"> <digi:trn>Skip existing activities (only insert new)</digi:trn></label>
          <label class="toggle-item" for="skipRecordsWithoutTransactions"><input type="checkbox" id="skipRecordsWithoutTransactions" name="skipRecordsWithoutTransactions"> <digi:trn>Skip records with no transactions</digi:trn></label>
          <label class="toggle-item" for="validateActivities"><input type="checkbox" id="validateActivities" name="validateActivities"> <digi:trn>Validate imported activities (set as approved, non-draft)</digi:trn></label>
          <label class="toggle-item" for="addDisbursementForCommitment"><input type="checkbox" id="addDisbursementForCommitment" name="addDisbursementForCommitment"> <digi:trn>Add Disbursement for any Commitment</digi:trn></label>
          <label class="toggle-item" for="createMissingOrgs"><input type="checkbox" id="createMissingOrgs" name="createMissingOrgs"> <digi:trn>Create missing organizations</digi:trn></label>
          <label class="toggle-item" for="createMissingSectors"><input type="checkbox" id="createMissingSectors" name="createMissingSectors"> <digi:trn>Create missing sectors</digi:trn></label>
          <label class="toggle-item" for="createMissingOrgGroups"><input type="checkbox" id="createMissingOrgGroups" name="createMissingOrgGroups"> <digi:trn>Create missing organization groups for new organizations</digi:trn></label>
          <label class="toggle-item" for="createMissingPrograms"><input type="checkbox" id="createMissingPrograms" name="createMissingPrograms"> <digi:trn>Create missing programs</digi:trn></label>
          <label class="toggle-item" for="replaceExistingTransactions"><input type="checkbox" id="replaceExistingTransactions" name="replaceExistingTransactions"> <digi:trn>Replace existing transactions</digi:trn></label>
        </div>

        <div id="orgGroupDiv" style="display:none; margin-top:16px;">
          <label for="orgGroupId"><digi:trn>Organization Group for new organizations</digi:trn></label>
          <select id="orgGroupId" name="orgGroupId" style="width: 100%;">
            <option value=""><digi:trn>-- Select Organization Group --</digi:trn></option>
            <c:forEach var="orgGroup" items="${orgGroups}">
              <option value="${orgGroup.ampOrgGrpId}">${orgGroup.orgGrpName}</option>
            </c:forEach>
          </select>
        </div>

        <div style="margin-top:16px;">
          <label for="defaultActivityStatusId"><digi:trn>Default Activity Status</digi:trn></label>
          <select id="defaultActivityStatusId" name="defaultActivityStatusId" style="width: 100%;">
            <option value=""><digi:trn>-- Use system default --</digi:trn></option>
            <c:forEach var="activityStatus" items="${activityStatuses}">
              <option value="${activityStatus.id}">${activityStatus.label}</option>
            </c:forEach>
          </select>
        </div>

        <div id="default-location-fallback" style="display:none; margin-top:16px;">
          <label for="defaultLocationId"><digi:trn>Fallback Location</digi:trn></label>
          <div class="helper-note" style="margin-bottom: 10px;"><digi:trn>Used only when no Project Location column is mapped. The instance country is preselected by default.</digi:trn></div>
          <select id="defaultLocationId" name="defaultLocationId" data-default-location-id="${defaultLocationId}" style="width: 100%;">
            <option value=""><digi:trn>-- Select Location --</digi:trn></option>
            <c:forEach var="location" items="${availableLocations}">
              <option value="${location.id}" <c:if test="${defaultLocationId == location.id}">selected="selected"</c:if>>${location.hierarchicalName}</option>
            </c:forEach>
          </select>
        </div>

        <div id="program-classification-fallback" style="display:none; margin-top:16px;">
          <label for="defaultProgramClassification"><digi:trn>Fallback Program Classification</digi:trn></label>
          <div class="helper-note" style="margin-bottom: 10px;"><digi:trn>Used only when Program Name is mapped but Program Classification is not.</digi:trn></div>
          <select id="defaultProgramClassification" name="defaultProgramClassification" style="width: 100%;">
            <option value=""><digi:trn>-- Select Program Classification --</digi:trn></option>
            <c:forEach var="programClassification" items="${programClassifications}">
              <option value="${programClassification}">${programClassification}</option>
            </c:forEach>
          </select>
        </div>

        <div class="mapping-actions">
          <input type="button" value="<digi:trn>Upload</digi:trn>" onclick="uploadDataFile()">
        </div>
      </div>
    </html:form>
  </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0/js/select2.min.js"></script>
<script>
  $(document).ready(function() {
    initializeSelectControls();
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
