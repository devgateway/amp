<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>

<c:set var="markerColor" target="page">#E7E7FF</c:set>
<c:set var="skippedClass" target="page">hierarchyCell</c:set>
<c:set var="baseId" target="page">report_row_</c:set>

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.ampModule.aim.dbentity.AmpReports" toScope="page"/>
<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>

<!-- generate report headings -->
<bean:define id="viewable" name="groupReport" property="firstColumnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="reportHeading.jsp"/>


<c:set var="skipRowTag" scope="request">${skipRowTag}</c:set>
<c:set var="trailCellsFile">TrailCellsForReportData.jsp</c:set>

<!-- generate total row 1 -->
<logic:present name="groupReport" property="parent">
	<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
	<bean:define id="grandTotal" toScope="request" value="yes" property="java.lang.String" />
	<!-- GRD-before trailCells!! -->
	<c:if test="${groupReport.levelDepth == 1}">
		<c:set var="trailCellsFile">TrailCells.jsp</c:set>
	</c:if>
	<%-- SKIP_ROW_TAG is ${skipRowTag} || LEVEL_DEPTH is ${groupReport.levelDepth} --%>
	<c:choose>
		<c:when test="${skipRowTag=='true'}">
			<!-- skipping row tag -- ${trailCellsFile} -->
			<jsp:include page="${trailCellsFile}"/>
			<c:set var="skipRowTag" scope="request">false</c:set>
		</c:when>
		<c:otherwise>
			<!-- printing row tag -- ${trailCellsFile} -->
				<c:choose>
					<c:when test="${reportMeta.hideActivities==null || !reportMeta.hideActivities}">
						<tr onmousedown="getRowSelectorInstance(this, {baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' } ).toggleRow()" 
						onMouseover="getRowSelectorInstance(this, {baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' } ).markRow(false)" 
						onMouseout="getRowSelectorInstance(this, {baseId:'${baseId}', markerColor:'${markerColor}', skippedClass:'${skippedClass}' } ).unmarkRow(false)">
					</c:when>
					<c:otherwise>
						<tr>
					</c:otherwise>
				</c:choose>
				<jsp:include page="${trailCellsFile}"/>
		</c:otherwise>
	</c:choose>
	<!-- GRD-after trailCells!! -->
	<%-- <tr>
		<td height="5px" colspan='<bean:write name="groupReport" property="totalDepth"/>'></td>
	</tr>  --%> 
</logic:present>

<c:if test="${groupReport.levelDepth > 1}">
	<c:set var="skipRowTag" scope="request">true</c:set>
</c:if>

	<logic:iterate name="groupReport"  property="items" id="item" scope="page">
		<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<logic:equal name="viewable" property="renderBody" value="true">
			<jsp:include page="<%=viewable.getViewerPath()%>"/>	
				</tr> <%-- This closes a row started earlier in this file before including TrailCells.jsp. 
							This needs to be treated here since if this is a GroupReportData of a 2nd or 3rd Hierarchy 
							then no row tag need to be created (for the first activity) --%>
				<c:set var="skipRowTag" scope="request">false</c:set>	
		</logic:equal>
	</logic:iterate>

<c:if test="${groupReport.levelDepth > 1}">	
	<%-- Here we include the totals for this GroupReportData --%>
	<tr  class="${groupReport.htmlClassName}" >
		<c:forEach var="idx" begin="${groupReport.levelDepth}" end="${reportMeta.numOfHierarchies+1}">
			<c:if test="${idx != reportMeta.numOfHierarchies}">
				<td class="hierarchyCell">&nbsp;</td>
			</c:if>
		</c:forEach>
		<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
		<jsp:include page="TrailCells.jsp" />
	</tr>
</c:if>
	</td>
</tr>

