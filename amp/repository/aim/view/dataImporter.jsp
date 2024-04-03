<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Data Importer</title>
  <script>
      function addField() {
      var columnName = document.getElementById("columnName").value;
      var selectedField = document.getElementById("selected-field").value;

      // Create or retrieve the hidden input field for column name
      var columnNameInput = document.createElement("input");
      columnNameInput.setAttribute("type", "hidden");
      columnNameInput.setAttribute("name", "columnName");
      columnNameInput.setAttribute("value", columnName);

      // Create or retrieve the hidden input field for selected field
      var selectedFieldInput = document.createElement("input");
      selectedFieldInput.setAttribute("type", "hidden");
      selectedFieldInput.setAttribute("name", "selectedField");
      selectedFieldInput.setAttribute("value", selectedField);

      // Append the hidden input fields to the form
      document.getElementById("data-importer-form").appendChild(columnNameInput);
      document.getElementById("data-importer-form").appendChild(selectedFieldInput);

      // Display the added pair
      var pairContainer = document.createElement("div");
      pairContainer.textContent = "Column: " + columnName + ", Field: " + selectedField;
      document.getElementById("pairs-container").appendChild(pairContainer);
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
  <input type="button" value="Add Field" onclick="addField()">
  <br><br>
  <input type="submit" value="Upload">
</form>
</body>
</html>
