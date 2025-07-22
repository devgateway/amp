<%@page import="org.dgfoundation.amp.ar.ARUtil"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>

<!-- generate total row -->
<tr>
	<c:if test="${reportData.levelDepth == 1}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid;" height="18px" >
		<span style="font-family: Arial;color:black; font-weight: bold;font-size: 12px;margin-left: 2px">
				<digi:trn key="rep:popup:reporttotals">Report Totals:</digi:trn>
			</span>
	</c:if>
	
	
	<c:if test="${reportData.levelDepth == 2}">
		<td nowrap="nowrap" style="background:#99CCFF; border-bottom: #E2E2E2 1px solid;" height="15px" title='<bean:write name="reportData" property="repName"/>' >
			<span style="font-family: Arial;font-weight: bold;font-size: 10px;padding-left: 5px;padding-right: 3px;">
				<%= ARUtil.getNameFromReportData(reportData) %>
					<!-- *************************************************** 
							WARNING:
								Do not add Translations here!
						 ***************************************************--> 
			</span>
		</c:if>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
	
	<c:if test="${reportData.levelDepth == 3}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid" height="13px" >
			<span style="font-family: Arial;font-size: 9px;font-weight: bold;padding-left: 15px;padding-right: 3px">
				<img src="module/aim/images/hierarchies.gif" align="top">
			<%= ARUtil.getNameFromReportData(reportData) %>
			<!-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--> 
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth >= 4}">
		<c:set var="paddingLeft" value="${15+(reportData.levelDepth-3)*10}" />
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid" height="13px" >
		<span style="font-family: Arial;font-size: 9px;font-weight: bold;padding-left: ${paddingLeft}px;padding-right: 3px">
			<img src="module/aim/images/hierarchies.gif" align="top">
			<%= ARUtil.getNameFromReportData(reportData) %>
			<!-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--> 
		</span>
	</c:if>
	</td>
	<c:set var="firstCell" value="${true}"></c:set>
	<c:set var="t3" value="${false}"></c:set>
	<c:if test="${reportMeta.hideActivities != null && reportMeta.hideActivities }">
		<c:set var="firstCell" value="${false}"></c:set>
	</c:if>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
	
	<logic:iterate name="reportData" property="trailCells"  id="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="page">
			<c:if test="${cell!=null}">
				<bean:define id="column" scope="page" toScope="page"  name="cell" property="column"/>
				<c:if test="${column.worker!=null}">
					<bean:define id="worker" name="column" property="worker" scope="page"></bean:define>
					<bean:define id="ampColumn" name="worker" property="relatedColumn" scope="page"></bean:define>
					<c:set var="total" scope="page" value="${ampColumn.totalExpression}"/>
					<c:set var="token" scope="page" value="${ampColumn.tokenExpression}"/>
					<c:set var="t1" value="${total!=null}"/>
					<c:set var="t2" value="${token!=null}"/>
					<c:set var="t3" value="${t1||t2}"/>
				</c:if>	
			</c:if>
		<c:if test="${ firstCell == false || t3}">
				<c:if test="${reportData.levelDepth == 1}">
					<td align="center" style="border-bottom: #E0E0E0 1px solid;border-right: #E0E0E0 1px solid;font-family: Arial;font-weight: bold">
				</c:if>
				<c:if test="${reportData.levelDepth == 2}">
					<td style="border-bottom: #E0E0E0 1px solid;background:#99CCFF;font-weight: bold;font-family: Arial;">
				</c:if>  
			
				<c:if test="${reportData.levelDepth > 2}">
					<td style="border-bottom: #E2E2E2 1px solid;border-right: #E2E2E2 1px solid">
				</c:if>  
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
<%-- UNUSED FILE, TO BE REMOVED --%>
			
					<c:if test="${cell!=null}">
						<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
						<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />	 		
						<jsp:include page="<%=viewable.getViewerPath()%>"/>	
					</c:if>
					<c:if test="${cell==null}">
					&nbsp;
					</c:if>	
				</td>
		</c:if>
		<c:set var="firstCell" value="${false}"></c:set>
		<c:set var="total" scope="page" value=""/>
		<c:set var="token" scope="page" value=""/>
	</logic:iterate>
</tr>



