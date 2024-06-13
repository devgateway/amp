<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib prefix="html" uri="/taglib/struts-html" %>
<%@ taglib prefix="bean" uri="/taglib/struts-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html:html>
<head>
  <title>Data Importer</title>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0/css/select2.min.css" rel="stylesheet" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

  <script>
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
      var columnName = document.getElementById("columnName").value;
      var selectedField = document.getElementById("selected-field").value;

      sendValuesToBackend(columnName,selectedField,"addField")


    }
    $(document).ready(function() {

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

        } else if(fileType==="text") {
          $('#select-file-label').html("Select text file");
          $('#data-file').attr("accept", ".txt");
          $('#template-file').attr("accept", ".txt");
          $('#separator-div').show();
        }
        else if(fileType==="excel") {
          $('#select-file-label').html("Select excel file");
          $('#data-file').attr("accept", ".xls,.xlsx");
          $('#template-file').attr("accept", ".xls,.xlsx");
          $('#separator-div').hide();
        }
        else if(fileType==="json") {
          $('#select-file-label').html("Select json file");
          $('#data-file').attr("accept", ".json");
          $('#template-file').attr("accept", ".json");
          $('#separator-div').hide();
        }
      });

      $('.existing-config').change(function() {
        var configName = $(this).val();
        if (configName!=='none'){

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
                  $("#templateUploadForm").hide();
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
                  document.getElementById("otherComponents").removeAttribute("hidden");

                })
                .catch(error => {
                  console.error("There was a problem with the fetch operation:", error);
                });
        }else
        {
          $("#templateUploadForm").show();
        }
      });
      });


    function sendValuesToBackend(columnName, selectedField, action) {
      // Create a FormData object to send data in the request body
      var formData = new FormData();
      formData.append("columnName", columnName);
      formData.append("selectedField", selectedField);
      formData.append("action", action);

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
      selectedFieldCell.textContent=selectedField;


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
      xhr.onload = function () {
        if (xhr.status === 200) {
          if (xhr.getResponseHeader('selectTag') && xhr.getResponseHeader('selectTag').length >= 1) {
            document.getElementById('headers').innerHTML = xhr.getResponseHeader('selectTag');
            alert("The template has been successfully uploaded.");
            document.getElementById("otherComponents").removeAttribute("hidden");
          } else {
            console.error("Unable to extract headers.Please  check the file format and try again");
            alert("Unable to extract headers.Please  check the file format and try again");
          }
        } else {
          console.error('Error:', xhr.status);
        }
      };
      xhr.send(formData);
    }


    function uploadDataFile() {
      var formData = new FormData();
      var fileType = $('#file-type').val();
      var dataSeparator = $('#data-separator').val();
      var fileInput = document.getElementById('data-file');
      // Check if a file is selected
      if (!fileInput.files.length) {
        alert("Please select a file to upload.");
        return;
      }
      formData.append('dataFile', fileInput.files[0]);
      formData.append('action',"uploadDataFile");
      formData.append('fileType', fileType);
      formData.append('dataSeparator', dataSeparator);

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
</head>
<body>
<h2>Data Importer</h2>




<h2>Upload File</h2>
<h3>Data file configuration</h3>

<label>Select file type</label>
<br>
<label class="file-type-label" for="file-type"></label>
<select id="file-type" class="file_type">
  <option value="excel">Excel</option>
  <option value="csv">CSV</option>
  <option value="text">Text</option>
  <option value="json">JSON</option>
  <option value="xml">XML</option>
</select>

<div id="separator-div" hidden="hidden">
  <label for="data-separator">Column Separator:</label>
  <br>
  <select id="data-separator">
    <option value=",">Comma(,)</option>
    <option value="|">Vertical Line(|)</option>
    <option value="||">Pipe(||)</option>
    <option value=" ">Space</option>
  </select>
</div>

<br>
<label for="configuration">Select Existing Configuration by name:</label>
<select id="configuration"  class="existing-config" style="width: 300px;">
  <option value="none">None</option>
  <jsp:useBean id="configNames" scope="request" type="java.util.List"/>
  <c:forEach items="${configNames}" var="configName" varStatus="loop">
    <option value="${configName}">${configName}</option>
    <br>
  </c:forEach>
</select>



<form id="templateUploadForm" enctype="multipart/form-data">
  <label>Select Template File:</label>

  <br>
  <input id="template-file" type="file" accept=".xls,.xlsx,.csv" name="templateFile" />
 <br><br>
  <input type="button" value="Upload Template" onclick="uploadTemplateFile()" />
</form>






<div id="otherComponents" hidden>
<html:form action="${pageContext.request.contextPath}/aim/dataImporter.do" method="post" enctype="multipart/form-data">

    <br><br>
    <div id="headers"></div>

    <br><br>
  <label for="selected-field">Select Entity Field:</label>
  <select id="selected-field"  class="select2" style="width: 300px;">
    <!-- Populate dropdown with entity field names -->
    <jsp:useBean id="fieldsInfo" scope="request" type="java.util.List"/>
    <c:forEach items="${fieldsInfo}" var="fieldInfo" varStatus="loop">

      <option>${fieldInfo}</option>
      <br>
    </c:forEach>
  </select>
  <br><br>

  <input type="button" value="Add Field" onclick="addField()">
  <br><br>

  <!-- Table to display selected pairs -->
  <table class="fields-table">
    <thead>
    <tr>
      <th>Column Name</th>
      <th>Selected Field</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody id="selected-pairs-table-body">
    <!-- Selected pairs will be dynamically added here -->
    </tbody>
  </table>
  <br>
  <br>
  <input type="button" value="Remove Selected Rows" class="remove-row">

  <br><br>

  <label id="select-file-label">Select Excel File:</label>
<%--  <html:file property="uploadedFile" name="dataImporterForm"   />--%>
  <input id="data-file" type="file" accept=".xls,.xlsx,.csv" name="dataFile" />

  <br><br>
<%--  <html:submit property="Upload">Upload</html:submit>--%>
  <input type="button" value="Upload" onclick="uploadDataFile()">

  <%--  </logic:notEmpty>--%>

</html:form>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0/js/select2.min.js"></script>


</body>
</html:html>
