<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Data Importer</title>
  <script>
    function addColumnMapping() {
      // Get selected field name and column name
      var fieldName = document.getElementById("fieldName").value;
      var columnName = document.getElementById("columnName").value;

      // Create new row in the table to display mapping
      var table = document.getElementById("mappingTable");
      var row = table.insertRow(-1);
      var cell1 = row.insertCell(0);
      var cell2 = row.insertCell(1);
      cell1.innerHTML = fieldName;
      cell2.innerHTML = columnName;
    }
  </script>
</head>
<body>
<h2>Data Importer</h2>
<form action="uploadData" method="post" enctype="multipart/form-data">
  <label for="file">Select Excel File:</label>
  <input type="file" id="file" name="file" required>
  <br><br>
  <!-- Iterate over fieldsInfo and group fields by subclass -->
  <c:forEach items="${fieldsInfo}" var="fieldInfo" varStatus="loop">
    <c:if test="${loop.first || !fieldInfo.subclass.equals(fieldsInfo[loop.index - 1].subclass)}">
      <h3>${fieldInfo.subclass}</h3>
    </c:if>
    <label for="fieldName">Field Name:</label>
    <select id="fieldName" name="fieldName">
      <c:forEach items="${fieldsInfo}" var="field">
        <option value="${field.fieldName}">${field.fieldName}</option>
      </c:forEach>
    </select>
    <br>
    <label for="columnName">Column Name:</label>
    <input type="text" id="columnName" name="columnName">
    <button type="button" onclick="addColumnMapping()">Add Mapping</button>
    <br>
  </c:forEach>
  <br>
  <table id="mappingTable">
    <tr>
      <th>Field Name</th>
      <th>Column Name</th>
    </tr>
    <!-- Existing mappings will be added dynamically here -->
  </table>
  <br>
  <input type="submit" value="Upload">
</form>
</body>
</html>
