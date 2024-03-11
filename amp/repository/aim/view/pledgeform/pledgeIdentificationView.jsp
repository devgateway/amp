<%--
	renders the "Identification" & "Donor Information" parts of the Pledge View
--%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/aim" prefix="aim" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

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
<feature:display name="Pledge Status" module="Pledges">
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