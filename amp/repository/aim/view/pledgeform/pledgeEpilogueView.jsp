<%-- renders the contacts part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<c:if test="${not empty pledgeForm.additionalInformation}">
	<aim:renderFormSubsection title="Additional Information">
		<div class="view-textarea" style="width: 80%; margin-left: 7%">
			<c:out value="${pledgeForm.additionalInformation}" />
		</div>
	</aim:renderFormSubsection>
</c:if>