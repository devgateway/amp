<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

&nbsp;-&nbsp;
<logic:iterate name="reportData" property="trailCells" id="cell"  type="org.dgfoundation.amp.ar.cell.Cell" >
<%  if (cell.getColumn().getName().indexOf("Cumulative") != -1) {%>
<i><bean:write name="cell" property="column.name"/></i>
(<bean:write name="cell"/>),
<%}%>
</logic:iterate>
