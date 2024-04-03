<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Data Importer</title>
  <script>
    function addField() {
      var columnName = document.getElementById("columnName").value;
      // var selectedColumn = columnsDropdown.options[columnsDropdown.selectedIndex].value;
      var selectedField = document.getElementById("selected-field").value;
      var form = document.getElementById("data-importer-form");

      var div = document.createElement("div");
      var label = document.createElement("label");
      var input = document.createElement("input");
      label.textContent = columnName;
      input.type = "hidden";
      input.name = columnName;
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
  <label for="columnName">Enter Excel Column:</label>

  <input id="columnName" type="text">
  <br><br>
  <label for="selected-field">Select Entity Field:</label>
  <select id="selected-field">
    <!-- Populate dropdown with entity field names -->
    <jsp:useBean id="fieldsInfo" scope="request" type="java.util.List"/>
    <c:forEach items="${fieldsInfo}" var="fieldInfo" varStatus="loop">
      <c:if test="${loop.first || !fieldInfo.subclass.equals(fieldsInfo[loop.index - 1].subclass)}">
        <h3>${fieldInfo.subclass}</h3>
      </c:if>
      <label for="${fieldInfo.fieldName}">${fieldInfo.fieldName}</label>
      <input type="text" id="${fieldInfo.fieldName}" name="${fieldInfo.fieldName}">
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
