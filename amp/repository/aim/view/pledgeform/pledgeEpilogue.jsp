<%-- renders the contacts part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<feature:display name="Pledge Additional Information" module="Pledges">
	<aim:renderFormSubsection title="Additional Information">
		<html:textarea property="additionalInformation" rows="6" cols="80" styleClass="form-control" style="width: 80%; margin-left: 7%" />
	</aim:renderFormSubsection>
</feature:display>
