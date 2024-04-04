<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib prefix="html" uri="/taglib/struts-html" %>
<html:html>
<head>
  <title>Data Importer</title>
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

          for (var key in updatedMap) {
            if (updatedMap.hasOwnProperty(key)) {
              // Access each property using the key
              var value = updatedMap[key];
              updateTable(key,value);
              console.log('Key:', key, 'Value:', value);
            }
          }

        }
      };
      xhr.send(formData);
    }
    function updateTable(columnName,selectedField)
    {
      var table= document.getElementById("selected-pairs-table-body");
      var tbody = table.getElementsByTagName("tbody")[0];

      // Remove all rows from the table body
      while (tbody.firstChild) {
        tbody.removeChild(tbody.firstChild);
      }
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
      table.appendChild(row);
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
<html:form action="${pageContext.request.contextPath}/aim/dataImporter.do" method="post" enctype="multipart/form-data">
  <h3>Data file configuration</h3>
  <label for="columnName">Column Name:</label>
  <input type="text" id="columnName" name="columnName" required>
  <br><br>
  <label for="selected-field">Select Entity Field:</label>
  <select id="selected-field">
    <!-- Populate dropdown with entity field names -->
    <jsp:useBean id="fieldsInfo" scope="request" type="java.util.List"/>
    <c:forEach items="${fieldsInfo}" var="fieldInfo" varStatus="loop">

      <option>${fieldInfo}</option>
      <br>
    </c:forEach>
  </select>
  <br><br>
<%--  <html:text  property="selectedPairs" name="dataImporterForm"/>--%>

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
  <html:file property="uploadedFile" name="dataImporterForm"  />
  <br><br>
  <html:submit property="Upload">Upload</html:submit>
</html:form>


</body>
</html:html>
