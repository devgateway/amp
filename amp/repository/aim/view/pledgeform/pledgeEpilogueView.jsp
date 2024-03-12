<%-- renders the contacts part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/aim.tld" prefix="aim" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/featureVisibility.tld" prefix="feature" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />
<feature:display name="Pledge Attached Files" module="Pledges">
	<aim:renderFormSubsection title="Pledge Documents">
		<jsp:include page="pledgeDocumentsView.jsp"></jsp:include>
	</aim:renderFormSubsection>
</feature:display>
<feature:display name="Pledge Additional Information" module="Pledges">
<c:if test="${not empty pledgeForm.additionalInformation}">
		<aim:renderFormSubsection title="Additional Information">
			<div class="view-textarea" style="width: 80%; margin-left: 7%">
				<c:out value="${pledgeForm.additionalInformation}" />
			</div>
		</aim:renderFormSubsection>
	</c:if>
</feature:display>

