<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<%-- paginator for the html and html2 view formats, since they are identical --%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="generatedReport" name="reportCD" property="generatedReport" type="org.dgfoundation.amp.ar.GroupReportData" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>

<!-- html2 paginator: visibleRows = ${generatedReport.visibleRows}, recordsPerPage = ${recordsPerPage}  -->

<div class="paging">
	<c:if test="${(generatedReport.visibleRows / recordsPerPage > 1) && (recordsPerPage ne max_value)}">
	
		<c:if test="${startPageRow != 0}"> <!-- first page <<  -->
			<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>';">
				<b>&lt;&lt;</b>										
			</a>&nbsp;|&nbsp;
            <a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${generatedReport.startRow-recordsPerPage}"/>~endRow=<c:out value="${generatedReport.startRow-1}"/>';">
                <digi:trn key="aim:previous">Previous</digi:trn>
            </a>&nbsp;|&nbsp;
		</c:if>
		
		<!-- all pages in vicinity  -->
		<c:forEach var="i" begin="${startPageRow}" end="${endPageRow}" step="${recordsPerPage}">
			<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
				<b ${i == generatedReport.startRow ? 'class="paging_sel"' : ''}>
					<digi:easternArabicNumber><fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/></digi:easternArabicNumber>
				</b>										
			</a>
			&nbsp;|&nbsp;
		</c:forEach>
		
		<!--  last displayed record of last displayed page: ${endPageRow - 1}, total records: ${generatedReport.visibleRows} >>  -->
		<c:if test="${(generatedReport.startRow+recordsPerPage+1) <= generatedReport.visibleRows}">
            <a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${generatedReport.startRow+recordsPerPage}"/>~endRow=<c:out value="${generatedReport.startRow+(recordsPerPage*2)-1}"/>';">
                <digi:trn key="aim:next">Next</digi:trn>
            </a>&nbsp;|&nbsp;
			<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>';">
				<b>
					&gt;&gt;
				</b>										
			</a>
		</c:if>
	</c:if>
</div>
