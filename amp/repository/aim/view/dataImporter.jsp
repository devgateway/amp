<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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

      // Create a new table row
      var row = document.createElement("tr");

      // Create table cells for column name and selected field
      var columnNameCell = document.createElement("td");
      columnNameCell.textContent = columnName;
      var selectedFieldCell = document.createElement("td");
      selectedFieldCell.textContent = selectedField;

      var selectedPairsInput = document.getElementById("selected-pairs");
      var currentPairs = selectedPairsInput.value;
      if (currentPairs === "") {
        // If the input field is empty, simply set it to the new pair
        selectedPairsInput.value = columnName + ":" + selectedField;
      } else {
        // If the input field already contains pairs, append the new pair
        selectedPairsInput.value += ";" + columnName + ":" + selectedField;
      }


      // Create a remove button
      var removeButtonCell = document.createElement("td");
      var removeButton = document.createElement("button");
      removeButton.textContent = "Remove";
      removeButton.onclick = function() {
        selectedPairsInput.value = replaceLastOccurrence(currentPairs, ";" + columnName + ":" + selectedField, '');
        row.remove(); // Remove the row when the remove button is clicked
      };
      removeButtonCell.appendChild(removeButton);

      // Append cells to the row
      row.appendChild(columnNameCell);
      row.appendChild(selectedFieldCell);
      row.appendChild(removeButtonCell);

      // Append the row to the table body
      document.getElementById("selected-pairs-table-body").appendChild(row);
    }
  </script>
</head>
<body>
<h2>Data Importer</h2>
<form id="data-importer-form" action="uploadData" method="post" enctype="multipart/form-data">
  <label for="file">Select Excel File:</label>
  <input type="file" id="file" name="file" required>
  <br><br>
  <label for="columnName">Column Name:</label>
  <input type="text" id="columnName" name="columnName" required>
  <br><br>
  <label for="selected-field">Select Entity Field:</label>
  <select id="selected-field">
    <!-- Populate dropdown with entity field names -->
    <jsp:useBean id="fieldsInfo" scope="request" type="java.util.List"/>
    <c:forEach items="${fieldsInfo}" var="fieldInfo" varStatus="loop">
      <c:if test="${loop.first || !fieldInfo.subclass.equals(fieldsInfo[loop.index - 1].subclass)}">
        <h3>${fieldInfo.subclass}</h3>
      </c:if>
      <option>${fieldInfo.fieldName}</option>
      <br>
    </c:forEach>
  </select>
  <br><br>
  <input type="hidden" id="selected-pairs" name="selectedPairs">

  <input type="button" value="Add Field" onclick="addField()">
  <br><br>
  <input type="submit" value="Upload">
</form>

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
</body>
</html>
