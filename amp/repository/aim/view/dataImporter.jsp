<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
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
<digi:form action="${pageContext.request.contextPath}/dataImporter.do" method="post" enctype="multipart/form-data">

  <label for="file">Select Excel File:</label>
  <input type="file" id="file" name="file" required>
  <br><br>
  <!-- Iterate over fieldsInfo and group fields by subclass -->
  <label><digi:trn key="aim:fieldName">Field Name</digi:trn>&nbsp;</label>

    <html:select property="fieldName" styleClass="inp-text-fieldOption">

      <logic:notEmpty name="dataImporterForm" property="fieldInfos">
        <html:optionsCollection name="dataImporterForm" property="fieldInfos" value="fieldName" label="fieldName" />
      </logic:notEmpty>
    </html:select>
    <br>
    <label>Column Name:</label>
<%--    <input type="text" id="columnName" name="columnName">--%>
  <html:text property="columnName" styleClass="inp-text" styleId="keyWordTextField"/>

  <button type="button" onclick="addColumnMapping()">Add Mapping</button>
    <br>
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
</digi:form>
</body>
</html>
