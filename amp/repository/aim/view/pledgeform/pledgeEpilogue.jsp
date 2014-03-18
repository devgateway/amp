<%-- renders the contacts part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<feature:display name="Pledge Additional Information" module="Pledges">
	<div class="highlight text-center h5 bold"><digi:trn key="aim:additionalInformation">Additional Information</digi:trn></div>
	<html:textarea property="additionalInformation" rows="6" cols="80" styleClass="form-control" style="width: 80%; margin-left: 7%" />
</feature:display>
