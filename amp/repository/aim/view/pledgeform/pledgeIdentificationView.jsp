<%--
	renders the "Identification" & "Donor Information" parts of the Pledge View
--%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ page import="org.digijava.ampModule.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<c:set var="viewFieldName"><digi:trn>Pledge Identification</digi:trn></c:set>
<c:set var="viewFieldValue">${pledgeForm.effectiveName}</c:set>
<%@include file="pledgeViewField.jspf" %>
<c:set var="viewFieldName"><digi:trn>Creation date</digi:trn></c:set>
<c:set var="viewFieldValue">${pledgeForm.createdDate}</c:set>
<%@include file="pledgeViewField.jspf" %>

<c:set var="viewFieldName"><digi:trn>Organization Group</digi:trn></c:set>
<c:set var="viewFieldValue">${pledgeForm.selectedOrgGrpName}</c:set>
<%@include file="pledgeViewField.jspf" %>

<c:set var="viewFieldName"><digi:trn>Pledge Status</digi:trn></c:set>
<c:set var="viewFieldValue">${pledgeForm.pledgeStatus}</c:set>
<feature:display name="Pledge Status" ampModule="Pledges">
<%@include file="pledgeViewField.jspf" %>
</feature:display>
<field:display name="Who Authorized Pledge" feature="Pledge Donor Information">
<c:set var="viewFieldName"><digi:trn>Who Has Authorized Pledge?</digi:trn></c:set>
<c:set var="viewFieldValue">${pledgeForm.whoAuthorizedPledge}</c:set>
<%@include file="pledgeViewField.jspf" %>
</field:display>
<field:display name="Further Approval Needed" feature="Pledge Donor Information">
<c:set var="viewFieldName"><digi:trn>Further Approval Needed</digi:trn></c:set>
<c:set var="viewFieldValue">${pledgeForm.furtherApprovalNedded}</c:set>
<%@include file="pledgeViewField.jspf" %>
</field:display>