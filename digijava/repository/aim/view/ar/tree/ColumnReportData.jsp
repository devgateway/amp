<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>




<li noDrag="true">
<a onMouseOver="stm(['Totals for <bean:write name="columnReport" property="name"/>',document.getElementById('<bean:write name="columnReport" property="absoluteReportName"/>').innerHTML],Style[0])" onMouseOut="htm()"><bean:write name="columnReport" property="name"/></a>
<ul>
<table class=clsInnerTable cellSpacing=0 cellPadding=0 width="100%" border=0>

<% int rowIdx=2; %>

<!-- generate report headings -->
<%int maxDepth=columnReport.getMaxColumnDepth(); columnReport.setGlobalHeadingsDisplayed(new Boolean(true)); %>
<%for(int curDepth=0;curDepth<=columnReport.getMaxColumnDepth();curDepth++,rowIdx++) {%>
<tr>
<logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column">
	<% column.setCurrentDepth(curDepth);%>
	<% int rowsp=column.getCurrentRowSpan(); %>
	<logic:iterate name="column" property="subColumnList" id="subColumn" scope="page" type="org.dgfoundation.amp.ar.Column">
	<td align="center" class=clsTableTitleCol rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
	
	<logic:equal name="column" property="columnDepth" value="1">
	<html:link styleClass="reportHeading" page="/viewNewAdvancedReport.do" paramName="column" paramProperty="name" paramId="sortBy">
		<%=subColumn.getName(reportMeta.getHideActivities())%>
	</html:link>
	<c:if test="${column.name == columnReport.sorterColumn}">
	<logic:equal name="columnReport" property="sortAscending" value="false">
	<img src= "../ampTemplate/images/down.gif" align="absmiddle" border="0"/>
	</logic:equal>
	<logic:equal name="columnReport" property="sortAscending" value="true">
	<img src= "../ampTemplate/images/up.gif" align="absmiddle" border="0"/>
	</logic:equal>

	</c:if>
	
	</logic:equal>
	<logic:notEqual name="column" property="columnDepth" value="1">
		<%=subColumn.getName(reportMeta.getHideActivities())%>
	</logic:notEqual>
	</td>
	
	</logic:iterate>
</logic:iterate>
</tr>
<% } %>

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

<div style='position:relative;display:none;' id='<bean:write name="columnReport" property="absoluteReportName"/>'> 
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.ColumnReportData" scope="page" toScope="request"/>
<jsp:include page="/repository/aim/view/ar/print/TrailCells.jsp"/>
</div>