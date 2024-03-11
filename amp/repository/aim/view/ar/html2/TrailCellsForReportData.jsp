<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

<!-- generate total row 3 -->
	<c:if test="${reportData.levelDepth == 1}">
		<td nowrap="nowrap" style="border-bottom:#E2E2E2 1px solid;border-right:#E2E2E2 1px solid;" height="18px" >
		<span style="font-family: Arial;color:black; font-weight: bold;font-size: 12px;margin-left: 2px">
				<digi:trn key="rep:popup:reporttotals">Report Totals:</digi:trn>
			</span>
	</c:if>
	
	
	<c:if test="${reportData.levelDepth == 2}">
		<td rowspan="${reportData.rowSpan}" class="clsTableCellDataHtml firstLevel hierarchyCell" height="15px" title='<bean:write name="reportData" property="repName"/>' >
			<span>
				<%if(reportData.getRepName().length()<60){ %>
					<% if (!("".equals(reportData.getRepName()))){ %>
					<%-- *************************************************** 
							WARNING:
								Do not add Translations here!
						 ***************************************************--%> 
		 				${reportData.repName}
					<% 
					} 
				}else{%>
					<%=reportData.getRepName().substring(0,59)%>...
				<%} %>	
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth == 3}">
		<td rowspan="${reportData.rowSpan}" class="clsTableCellDataHtml secondLevel hierarchyCell" height="13px" >
			<span>
			<% if (!("".equals(reportData.getRepName()))){ %>
			<%-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--%> 
				${reportData.repName}
			<% } %>
			</span>
		</c:if>
	
	<c:if test="${reportData.levelDepth >= 4}">
		<td class="clsTableCellDataHtml ${reportData.htmlClassName} hierarchyCell" 
				height="13px" rowspan="${reportData.rowSpan}" >
		<span>
			<% if (!("".equals(reportData.getRepName()))){ %>
			<%-- *************************************************** 
					WARNING:
						Do not add Translations here!
				 ***************************************************--%> 
		 		${reportData.repName}
			<% } %>
		</span>
	</c:if>
	</td>
	


