<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>



<li noDrag="true">
<a onMouseOver="stm(['Totals for <bean:write name="columnReport" property="name"/>',document.getElementById('<bean:write name="columnReport" property="absoluteReportNameMD5"/>').innerHTML],Style[0])" onMouseOut="htm()"><bean:write name="columnReport" property="name"/></a>
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="subtotalsNodeLine.jsp"/>
<ul>
<table class=clsInnerTable cellspacing="0" cellpadding="0" width="100%" border="0">

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="reportHeadings.jsp"/>

<%int rowIdx = 2;%>

<!-- generate report data -->

<logic:notEqual name="reportMeta" property="hideActivities" value="true">
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">
<%rowIdx++; %>
<tr onmousedown="setPointer(this, <%=rowIdx%>, 'click', '#FFFFFF', '#FFFFFF', '#FFFF00');">
	<logic:iterate name="columnReport" property="items" id="column" scope="page">
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
	</logic:iterate>
</tr>
</logic:iterate>
</logic:notEqual>


<!-- generate total row -->
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.ColumnReportData" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>
</table>
</ul>
</li>

<div style='position:relative;display:none;' id='<bean:write name="columnReport" property="absoluteReportNameMD5"/>'> 
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.ColumnReportData" scope="page" toScope="request"/>
<jsp:include page="/src/main/webapp/repository/aim/view/ar/print/TrailCells.jsp"/>
</div>