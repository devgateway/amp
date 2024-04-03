<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Data Importer</title>
  <script>
    function addField() {
      var columnsDropdown = document.getElementById("columns-dropdown");
      var selectedColumn = columnsDropdown.options[columnsDropdown.selectedIndex].value;
      var selectedField = document.getElementById("selected-field").value;
      var form = document.getElementById("data-importer-form");

      var div = document.createElement("div");
      var label = document.createElement("label");
      var input = document.createElement("input");
      label.textContent = selectedColumn;
      input.type = "hidden";
      input.name = selectedColumn;
      input.value = selectedField;
      div.appendChild(label);
      div.appendChild(input);
      form.appendChild(div);
    }
  </script>
</head>
<body>
<h2>Data Importer</h2>
<form id="data-importer-form" action="uploadData" method="post" enctype="multipart/form-data">
  <label for="file">Select Excel File:</label>
  <input type="file" id="file" name="file" required>
  <br><br>
  <label for="columns-dropdown">Select Excel Column:</label>
  <select id="columns-dropdown">
    <!-- Populate dropdown with Excel column names -->
    <c:forEach items="${excelColumns}" var="column">
      <option>${column}</option>
    </c:forEach>
  </select>
  <br><br>
  <label for="selected-field">Select Entity Field:</label>
  <select id="selected-field">
    <!-- Populate dropdown with entity field names -->
    <c:forEach items="${fieldsInfo}" var="fieldInfo">
      <option>${fieldInfo.fieldName}</option>
    </c:forEach>
  </select>
  <br><br>
  <input type="button" value="Add Field" onclick="addField()">
  <br><br>
  <input type="submit" value="Upload">
</form>
</body>
</html>
