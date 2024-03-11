<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

&nbsp;-&nbsp;
<logic:iterate name="reportData" property="trailCells" id="cell"  type="org.dgfoundation.amp.ar.cell.Cell" >
<%  if (cell.getColumn().getName().indexOf("Cumulative") != -1) {%>
<i><bean:write name="cell" property="column.name"/></i>
(<bean:write name="cell"/>),
<%}%>
</logic:iterate>
