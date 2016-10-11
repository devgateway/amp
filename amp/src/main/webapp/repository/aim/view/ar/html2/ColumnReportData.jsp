<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>

<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>


<c:set var="trailCellsFile">TrailCellsForReportData.jsp</c:set>
<c:if test="${columnReport.levelDepth == 1}">
	<c:set var="trailCellsFile">TrailCells.jsp</c:set>
</c:if>

<c:set var="markerColor" target="page">#E7E7FF</c:set>
<c:set var="skippedClass" target="page">hierarchyCell</c:set>
<c:set var="baseId" target="page">report_row_</c:set>

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<!-- CRD-before trailCells!! -->
	<c:choose>
		<c:when test="${skipRowTag=='true'}">
			<!-- skipping row tag -->
			<jsp:include page="${trailCellsFile}"/>
			<c:set var="skipRowTag" scope="request">false</c:set>
		</c:when>
		<c:otherwise>
		<!-- printing row tag -->
				<c:choose>
					<c:when test="${reportMeta.hideActivities==null || !reportMeta.hideActivities}">
						<tr onmousedown="getRowSelectorInstance(this, {baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).toggleRow()" 
						onMouseover="getRowSelectorInstance(this, {baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).markRow(false)" 
						onMouseout="getRowSelectorInstance(this,  {baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' } ).unmarkRow(false)">
					</c:when>
					<c:otherwise>
						<tr>
					</c:otherwise>
				</c:choose>
				<jsp:include page="${trailCellsFile}"/>
		</c:otherwise>
	</c:choose>
<!-- CRD-after trailCells!! -->
<%int rowIdx = 2;%>

<!-- generate report data -->


<c:set var="skipRowTag">true</c:set>
<c:if test="${columnReport.levelDepth == 1}">
	<c:set var="skipRowTag">false</c:set>
</c:if>

<logic:notEqual name="reportMeta" property="hideActivities" value="true">	
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">
<logic:equal name="columnReport" property="canDisplayRow" value="true">
<%
	rowIdx++;	
	String width="0";
%>

<% 
	if(columnReport.getLevelDepth()<=2){
		request.setAttribute("pading","20px");
	}
	if (columnReport.getLevelDepth()==3){
		request.setAttribute("pading","30px");
	}
	if (columnReport.getLevelDepth()==4){
		request.setAttribute("pading","35px");
	}
%>
<!-- SSSS -${columnReport.currentRowNumber}- SSSS -->
<c:if test="${skipRowTag!='true'}">

	<%if (rowIdx%2==0){ %>
		<tr class="oddActivity" height="16px" 
			onmousedown="getRowSelectorInstance(this,{baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).toggleRow()" 
			onMouseover="getRowSelectorInstance(this,{baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).markRow(false)" 
			onMouseout="getRowSelectorInstance(this,{baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).unmarkRow(false)">
	<%}else{%>
		<tr height="16px" 
			onmousedown="getRowSelectorInstance(this,{baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).toggleRow()" 
			onMouseover="getRowSelectorInstance(this,{baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).markRow(false)" 
			onMouseout="getRowSelectorInstance(this,{baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' }).unmarkRow(false)">
	<%}%>
</c:if>

		<c:if test="${addFakeColumn}">
			<td></td>
		</c:if>
	<logic:iterate name="columnReport" property="items" id="column" scope="page">
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>
	</logic:iterate>
<c:if test="${skipRowTag!='true'}">	
	</tr>
</c:if>
<c:set var="skipRowTag">false</c:set>
</logic:equal>
</logic:iterate>
</logic:notEqual>

<c:if test="${columnReport.levelDepth > 1}">	
	<%-- Here we include the totals for this ColumnReportData --%>
	<c:if test="${reportMeta.hideActivities==null || !reportMeta.hideActivities }">
		<tr class="${columnReport.htmlClassName}">
	</c:if>
		<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<jsp:include page="TrailCells.jsp" />
	<c:if test="${reportMeta.hideActivities==null || !reportMeta.hideActivities }">
		</tr>
	</c:if>
</c:if>
