<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="aimReportWizardForm" />

<c:choose>
	<c:when test="${aimReportWizardForm.duplicateName == true}">
		<div  class="invisible-item">duplicateName</div>
		<digi:trn key="aim:reportwizard:duplicateName">There is already a report with the same name. Please choose a different one. </digi:trn>
	</c:when>
	<c:when test="${aimReportWizardForm.noReportNameSupplied == true}">
		<div  class="invisible-item">noReportNameSupplied</div>
		<digi:trn key="aim:reportwizard:duplicateName">Please enter a report name. </digi:trn>
	</c:when>	
	<c:when test="${aimReportWizardForm.overwritingForeignReport == true}">
		<div  class="invisible-item">overwritingForeignReport</div>
		<digi:trn key="aim:reportwizard:overwritingForeignReport">There is already a report with the same name belonging to a different user. Please choose a different name </digi:trn>
	</c:when>
	<c:otherwise>
		<digi:trn key="aim:reportwizard:savingProblems">There were some problems while trying to save this report. The report was not saved. </digi:trn>
	</c:otherwise>
</c:choose>
