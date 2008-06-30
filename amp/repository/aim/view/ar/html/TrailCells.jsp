<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

<!-- generate total row -->
<tr>
	<c:if test="${reportData.levelDepth == 1}">
		<td nowrap="nowrap" style="background:#376091; border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid;" height="22px" colspan='<bean:write name="reportData" property="sourceColsCount"/>'>
		<span style="font-family: Arial;color:white; font-weight: bold;font-size: 14px;margin-left: 2px">
				<digi:trn key="rep:popup:reporttotals ">Report Totals:</digi:trn>
			</span>
	</c:if>
	
	<c:if test="${reportData.levelDepth == 2}">
		<td nowrap="nowrap" style="background:#A5A5A5; border-bottom:#868383 1px solid;border-right:#868383 1px solid;" height="23px" title='<bean:write name="reportData" property="repName"/>' colspan='<bean:write name="reportData" property="sourceColsCount"/>'>
			<span style="font-family: Arial;font-weight: bold;font-size: 12px;padding-left: 5px;padding-right: 3px;">
				<%if(reportData.getRepName().length()<40){ %>
					<% if (!("".equals(reportData.getRepName()))){ %>
		 				<digi:trn key="rep:pop:${reportData.repName}">${reportData.repName}</digi:trn>
					<% 
					} 
				}else{%>
					<%=reportData.getRepName().substring(0,39)%>...
				<%} %>	
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth == 3}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid" height="23px" colspan='<bean:write name="reportData" property="sourceColsCount"/>'>
			<span style="font-family: Arial;font-size: 11px;font-weight: bold;padding-left: 15px;padding-right: 3px">
				<img src="module/aim/images/hierarchies.gif" align="top">
			<% if (!("".equals(reportData.getRepName()))){ %>
				<digi:trn key="rep:pop:${reportData.repNameTrn}">${reportData.repName}</digi:trn>
			<% } %>
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth == 4}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid" height="23px" colspan='<bean:write name="reportData" property="sourceColsCount"/>'>
		<span style="font-family: Arial;font-size: 11px;font-weight: bold;padding-left: 25px;padding-right: 3px">
			<img src="module/aim/images/hierarchies.gif" align="top">
			<% if (!("".equals(reportData.getRepName()))){ %>
		 		<digi:trn key="rep:pop:${reportData.repNameTrn}">${reportData.repName}</digi:trn>
			<% } %>
		</span>
	</c:if>
	
	
	</td>
	<logic:iterate name="reportData" property="trailCells" id="cell" scope="page">
		<c:if test="${reportData.levelDepth == 1}">
			<td align="center" height="25px" style="border-bottom: #E0E0E0 1px solid;border-right: #E0E0E0 1px solid;font-family: Arial;font-weight: bold">
		</c:if>
		<c:if test="${reportData.levelDepth == 2}">
			<td height="22px" style="background: #A5A5A5;border-bottom: #868383 1px solid;border-right: #868383 1px solid;font-weight: bold;font-family: Arial;">
		</c:if>  
		<c:if test="${reportData.levelDepth > 2}">
			<td height="22px" style="border-bottom: #E2E2E2 1px solid;border-right: #E2E2E2 1px solid">
		</c:if>  
			<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
			<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />			
			<jsp:include page="<%=viewable.getViewerPath()%>"/>	
		</td>
	</logic:iterate>
</tr>