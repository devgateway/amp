<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

&nbsp;-&nbsp;
<logic:iterate name="reportData" property="trailCells" id="cell"  type="org.dgfoundation.amp.ar.cell.Cell" >
<%  if (cell.getColumn().getName().indexOf("Cumulative") != -1) {%>
<i><bean:write name="cell" property="column.name"/></i>
(<bean:write name="cell"/>),
<%}%>
</logic:iterate>
