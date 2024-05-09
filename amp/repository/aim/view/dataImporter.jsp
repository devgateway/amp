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

    function sendValuesToBackend(columnName, selectedField, action)
    {
      var xhr = new XMLHttpRequest();
      // Create a FormData object to send data in the request body
      var formData = new FormData();
      formData.append("columnName", columnName);
      formData.append("selectedField", selectedField);
      formData.append(action, action);
      xhr.open("POST", "${pageContext.request.contextPath}/aim/dataImporter.do", true);
      // xhr.setRequestHeader()
      // xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
          // Update UI or perform any additional actions if needed
          console.log("Selected pairs updated successfully.");
          var updatedMapRaw = xhr.getResponseHeader('updatedMap');

          console.log("Raw response: "+updatedMapRaw)
          var updatedMap = JSON.parse(updatedMapRaw);

          // Use updatedMap as needed
          console.log("Updated map received:", updatedMap);
          var tbody= document.getElementById("selected-pairs-table-body");
          // var tbody = table.getElementsByTagName("tbody")[0];

          // Remove all rows from the table body
          while (tbody.firstChild) {
            tbody.removeChild(tbody.firstChild);
          }
          for (var key in updatedMap) {
            if (updatedMap.hasOwnProperty(key)) {
              // Access each property using the key
              var value = updatedMap[key];
              updateTable(key,value, tbody);
              console.log('Key:', key, 'Value:', value);
            }
          }

        }
      };
      xhr.send(formData);
    }
    function updateTable(columnName,selectedField, tbody)
    {

      // Create a new table row
      var row = document.createElement("tr");
      //
      // // Create table cells for column name and selected field
      var columnNameCell = document.createElement("td");
      columnNameCell.textContent = columnName;
      var selectedFieldCell = document.createElement("td");
      selectedFieldCell.textContent=selectedField;


      // Create a remove button
      var removeButtonCell = document.createElement("td");
      var removeButton = document.createElement("button");
      removeButton.textContent = "Remove";
      removeButton.onclick = function() {
        sendValuesToBackend(columnName,selectedField,"removeField") // Remove the row when the remove button is clicked
      };
      removeButtonCell.appendChild(removeButton);

      // Append cells to the row
      row.appendChild(columnNameCell);
      row.appendChild(selectedFieldCell);
      row.appendChild(removeButtonCell);

      // Append the row to the table body
      tbody.appendChild(row);
    }

    function uploadTemplateFile() {
      var formData = new FormData();
      var fileInput = document.getElementById('templateFile');
      formData.append('templateFile', fileInput.files[0]);
      formData.append('uploadTemplate', "uploadTemplate");

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
          }
        } else {
          console.error('Error:', xhr.status);
        }
      };
      xhr.send(formData);
    }


    function uploadDataFile() {
      var formData = new FormData();
      var fileInput = document.getElementById('dataFile');
      // Check if a file is selected
      if (!fileInput.files.length) {
        alert("Please select a file to upload.");
        return;
      }
      formData.append('dataFile', fileInput.files[0]);
      formData.append('uploadDataFile',"uploadDataFile");

      var xhr = new XMLHttpRequest();
      xhr.open('POST', '${pageContext.request.contextPath}/aim/dataImporter.do', true);
      alert("File is uploading and will be parsed shortly.");
      xhr.onload = function () {
        if (xhr.status === 200) {
          console.log("File Parsed successfully")
        } else {
          console.error('Error:', xhr.status);
        }
      };
      xhr.send(formData);
      window.location.href = "/aim/showDesktop.do";
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

<form id="uploadForm" enctype="multipart/form-data">
  <label>Select Template File:</label>
  <input type="file" accept=".xls,.xlsx,.csv" id="templateFile" name="templateFile" />
 <br><br>
  <input type="button" value="Upload Template" onclick="uploadTemplateFile()" />
</form>

<div id="otherComponents" hidden>

<html:form action="${pageContext.request.contextPath}/aim/dataImporter.do" method="post" enctype="multipart/form-data">


<%--  <jsp:useBean id="fileHeaders" scope="request" type="java.util.Set"/>--%>
<%--  <bean:write name="dataImporterForm" property="fileHeaders"/>--%>

<%--  <logic:notEmpty name="dataImporterForm" property="fileHeaders">--%>
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
  <table>
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
  <br><br>
  <label>Select Excel File:</label>
<%--  <html:file property="uploadedFile" name="dataImporterForm"   />--%>
  <input type="file" accept=".xls,.xlsx,.csv" id="dataFile" name="dataFile" />

  <br><br>
<%--  <html:submit property="Upload">Upload</html:submit>--%>
  <input type="button" value="Upload" onclick="uploadDataFile()">

  <%--  </logic:notEmpty>--%>

</html:form>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0/js/select2.min.js"></script>
<script>
  $(document).ready(function() {
    $('.select2').select2();
  });
</script>

</body>
</html:html>
