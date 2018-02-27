<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<%-- This JSP is used when "Save Report" is pressed from within an opened report/tab --%>
<logic:notEmpty name="reportCD" property="reportMeta">

<bean:define id="reportObject" name="reportCD" property="reportMeta" toScope="page" />

<c:set var="failureMessage">
	<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>
</c:set>
<c:set var="errorMessage">
	<digi:trn key="rep:dynamicsave:error">Apparently there are some problems in the saving process. Please try again at a later moment.</digi:trn>
</c:set>
<c:set var="savingDataMessage">
	<digi:trn key="aim:reportwizard:savingData">Saving data. Please wait.</digi:trn>
</c:set>

<c:choose>
	<c:when test="${reportObject.drilldownTab}">
		<c:set var="plsEnterTitle">
			<digi:trn key="rep:wizard:enterTitleForTab">Please enter a title for this tab: </digi:trn>
		</c:set>
		<c:set var="saveBtn">
			<digi:trn key="btn:saveTab">Save Tab</digi:trn>
		</c:set>
		<c:set var="overwrite">
			<digi:trn key="rep:dynamicsave:overwriteTab">Overwrite original tab</digi:trn>
		</c:set>
		<c:set var="saveFilters" scope="request">
			<digi:trn key="rep:pop:saveTab">Save Tab</digi:trn>
		</c:set>
		<c:set var="saveFiltersTooltip" scope="request">
			<digi:trn key="rep:pop:saveTabTooltip">Save the tab with filter and sorting criteria</digi:trn>
		</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="plsEnterTitle">
			<digi:trn key="rep:wizard:enterTitleForReport">Please enter a title for this report: </digi:trn>
		</c:set>
		<c:set var="saveBtn">
			<digi:trn key="btn:saveReport">Save Report</digi:trn>
		</c:set>
		<c:set var="overwrite">
			<digi:trn key="rep:dynamicsave:overwriteReport">Overwrite original report</digi:trn>
		</c:set>
		<c:set var="saveFilters" scope="request" >
			<digi:trn key="rep:pop:saveReport">Save Report</digi:trn>
		</c:set>
		<c:set var="saveFiltersTooltip" scope="request">
			<digi:trn key="rep:pop:saveReportTooltip">Save the report with filter and sorting criteria</digi:trn>
		</c:set>
	</c:otherwise>
</c:choose>
 
 <%-- UGLY! preexisting implementation creates multiple divs with the same ID. Should be cleaned up during report wizard cleanup --%>
<div id="saveTitlePanel" style="display:none">
	<div class="hd" style="font-size: 8pt">
		${plsEnterTitle}
	</div>
	<div class="bd search-panel-title" id="titlePanelBody">
		<jsp:include page="/repository/aim/view/multilingual/multilingualFieldEntry.jsp">
			<jsp:param name="attr_name" value="multilingual_tab_title" />
			<jsp:param name="onkeypress" value="return saveReportEngine.checkEnter(event);" />
		</jsp:include>
		<%--<jsp:param name="onkeyup" value="return saveReportEngine.checkTabName();" />  --%>
		
		<input type="hidden" id="saveReportId" value="${reportObject.ampReportId}" />
		<div align="center">
			<button id="dynamic_save_button" type="button" class="buttonx" onclick="$(this).attr('disabled',true);saveReportEngine.saveReport();">
			    ${saveBtn}
			</button>
			&nbsp;&nbsp;&nbsp;
		</div>
	</div>
	<div class="ft" align="center">
	</div>
</div>

</logic:notEmpty>
