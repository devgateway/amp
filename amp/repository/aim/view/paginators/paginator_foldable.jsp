<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="generatedReport" name="reportCD" property="generatedReport" type="org.dgfoundation.amp.ar.GroupReportData" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>

<div class="paging">
	<c:if test="${(generatedReport.visibleRows / recordsPerPage > 1) && (recordsPerPage ne max_value)}">
		<c:if test="${generatedReport.startRow != 0}">
			<!-- Go to FIRST PAGE -->
			<c:choose>
				<c:when test="${param.queryEngine!='true' }">
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
		           		&lt;&lt;
		           	</a>
		       		&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${generatedReport.startRow-recordsPerPage}"/>~endRow=<c:out value="${generatedReport.startRow-1}"/>');">	
						<digi:trn key="aim:previous">Previous</digi:trn>
					</a>
					&nbsp;|&nbsp;
				</c:when>
				<c:otherwise>
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=0~endRow=${recordsPerPage-1}~queryEngine=true');">	
						&lt;&lt;
					</a>
					&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${generatedReport.startRow-recordsPerPage}~endRow=${generatedReport.startRow-1}~queryEngine=true');">	
						<digi:trn key="aim:previous">Previous</digi:trn>
					</a>
					&nbsp;|&nbsp;
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:forEach var="i" begin="${startPageRow}" end="${endPageRow}" step="${recordsPerPage}">
			<c:choose>
				<c:when test="${param.queryEngine!='true' }">
		           	<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">
				</c:when>
				<c:otherwise>
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${i}~endRow=${i+recordsPerPage-1}~queryEngine=true');">	
				</c:otherwise>
			</c:choose>	
			<b ${i == generatedReport.startRow ? 'class="paging_sel"' : ''}>
				<digi:easternArabicNumber><fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/></digi:easternArabicNumber>
			</b>										
			</a>  
			&nbsp;|&nbsp;
		</c:forEach>

		<c:if test="${(generatedReport.startRow+recordsPerPage+1) <= generatedReport.visibleRows}">
			<c:choose>
				<c:when test="${param.queryEngine!='true' }">
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${generatedReport.startRow+recordsPerPage}"/>~endRow=<c:out value="${generatedReport.startRow+(recordsPerPage*2)-1}"/>');">	
						<digi:trn key="aim:next">Next</digi:trn>
					</a>
					&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
						&gt;&gt;
					</a>
				</c:when>
				<c:otherwise>
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${generatedReport.startRow+recordsPerPage}~endRow=${generatedReport.startRow+(recordsPerPage*2)-1}~queryEngine=true');">	
						<digi:trn key="aim:next">Next</digi:trn>
					</a>
					&nbsp;|&nbsp;
					<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${((lastPage-1)*recordsPerPage)}~endRow=${(lastPage*recordsPerPage)}~queryEngine=true');">	
				       	&gt;&gt;
					</a>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:if>
</div>
