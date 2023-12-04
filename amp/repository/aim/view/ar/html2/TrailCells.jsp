<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>
<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>

<!-- generate total row 2 -->

	<c:if test="${reportData.levelDepth == 1}">
		<td nowrap="nowrap" style="border-bottom:black 1px solid;border-right:#E2E2E2 1px solid;" height="18px" >
		<span style="font-family: Arial;color:black; font-weight: bold;font-size: 12px;margin-left: 2px">
			<digi:trn key="rep:popup:reporttotals">Report Totals:</digi:trn>
		</span>
		<logic:iterate name="reportMeta" property="hierarchies" type="org.digijava.module.aim.dbentity.AmpReportHierarchy" id="repHierarchy" scope="page" indexId="hierIdx">
			<c:if test="${ hierIdx > 0}">
	  			<td style="border-bottom:black 1px solid;border-right:#E2E2E2 1px solid;" height="18px">&nbsp;</td>
	  		</c:if>
	  	</logic:iterate>
		
	</c:if>
	
	<%-- 
	<c:if test="${reportData.levelDepth == 2}">
		<td nowrap="nowrap" style="background:#99CCFF; border-bottom: #E2E2E2 1px solid;" height="15px" title='<bean:write name="reportData" property="repName"/>' >
			<span style="font-family: Arial;font-weight: bold;font-size: 10px;padding-left: 5px;padding-right: 3px;">
				<%if(reportData.getRepName().length()<40){ %>
					<% if (!("".equals(reportData.getRepName()))){ %>
					<!-- *************************************************** 
							WARNING:
								Do not add Translations here!
						 ***************************************************--> 
		 				${reportData.repName}
					<% 
					} 
				}else{%>
					<%=reportData.getRepName().substring(0,39)%>...
				<%} %>	
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth == 3}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid" height="13px" >
			<span style="font-family: Arial;font-size: 9px;font-weight: bold;padding-left: 15px;padding-right: 3px">
				<img src="module/aim/images/hierarchies.gif" align="top">
			<% if (!("".equals(reportData.getRepName()))){ %>
			<!-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--> 
				${reportData.repName}
			<% } %>
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth == 4}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid" height="13px" >
		<span style="font-family: Arial;font-size: 9px;font-weight: bold;padding-left: 25px;padding-right: 3px">
			<img src="module/aim/images/hierarchies.gif" align="top">
			<% if (!("".equals(reportData.getRepName()))){ %>
			<!-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--> 
		 		${reportData.repName}
			<% } %>
		</span>
	</c:if>
	</td>
	<c:set var="firstCell" value="${true}"></c:set>
	<c:set var="t3" value="${false}"></c:set>
	<c:if test="${reportMeta.hideActivities != null && reportMeta.hideActivities }">
		<c:set var="firstCell" value="${false}"></c:set>
	</c:if>
	--%>
	<c:if test="${addFakeColumn}">
		<td></td>
	</c:if>
	<c:set var="firstCell" value="${false}"></c:set>
	<logic:iterate name="reportData" property="trailCells"  id="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="page" indexId="cellIdx">
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

		<c:if test="${firstCell == false || t3}">
				<c:if test="${reportData.levelDepth == 1}">
					<c:if test="${reportMeta.numOfHierarchies!=0 || cellIdx >0}">
						<td align="center" style="border-right: #E0E0E0 1px solid;font-weight:bold; border-bottom:black 1px solid;">
					</c:if>
				</c:if>
				<c:if test="${reportData.levelDepth == 2}">
					<c:choose>
						<c:when test="${reportMeta.hideActivities==null || !reportMeta.hideActivities}">
							<td>
						</c:when>
						<c:otherwise>
							<td class="${reportData.htmlClassName}">
						</c:otherwise>
					</c:choose>
				</c:if>  
			
				<c:if test="${reportData.levelDepth > 2}">
					<c:choose>
						<c:when test="${reportMeta.hideActivities==null || !reportMeta.hideActivities}">
							<td style="border-bottom: #E2E2E2 1px solid;">
						</c:when>
						<c:otherwise>
							<td class="${reportData.htmlClassName}">
						</c:otherwise>
					</c:choose>
				</c:if>  
			
					<c:if test="${cell!=null}">
						<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
						<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />	 		
						<jsp:include page="<%=viewable.getViewerPath()%>"/>	
					</c:if>
					<c:if test="${cell==null}">
					&nbsp;
					</c:if>	
				<c:if test="${reportData.levelDepth != 1 || reportMeta.numOfHierarchies !=0 || cellIdx > 0 }">
				</td>
				</c:if>
		</c:if>
		<c:set var="firstCell" value="${false}"></c:set>
		<c:set var="total" scope="page" value=""/>
		<c:set var="token" scope="page" value=""/>
	</logic:iterate>




