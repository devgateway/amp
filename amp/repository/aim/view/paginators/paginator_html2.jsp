<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<%-- paginator for the html and html2 view formats, since they are identical --%>
<div class="paging">
	<c:if test="${(report.visibleRows / recordsPerPage > 1) && (recordsPerPage ne max_value)}">
	
		<c:if test="${startPageRow != 0}"> <!-- first page <<  -->
			<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>';">
				<b>&lt;&lt;</b>										
			</a>&nbsp;|&nbsp;
		</c:if>
		
		<!-- all pages in vicinity  -->
		<c:forEach var="i" begin="${startPageRow}" end="${endPageRow}" step="${recordsPerPage}">
			<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
				<b ${i == report.startRow ? 'class="paging_sel"' : ''}>
					<fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/>
				</b>										
			</a>
			&nbsp;|&nbsp;
		</c:forEach>
		
		<!--  last displayed record of last displayed page: ${endPageRow - 1}, total records: ${report.visibleRows} >>  -->
		<c:if test="${endPageRow < report.visibleRows}">
			<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=${viewFormat}~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>';">
				<b>
					&gt;&gt;
				</b>										
			</a>			
		</c:if>
	</c:if>
</div>
