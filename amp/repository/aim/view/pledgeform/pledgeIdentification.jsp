<%--
	renders the "Identification" & "Donor Information" parts of the Pledge Form
--%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div class="container-fluid">
	<div class="col-xs-4 h6 bold">
		<span class="red">*</span> <digi:trn key="aim:pledgeIdentification">Pledge Identification</digi:trn>
	</div>
	<div class="col-xs-6 col-xs-offset-1">
		<%--  normally just one of these will be enabled, no need for <c:if> --%>
		<field:display name="Use Free Text" feature="Pledges Names">			
			<!--Allow user to enter free text if it's configured in the FM AMP-16005-->
			<html:text property="titleFreeText" styleId="titleFreeText" styleClass="form-control input-sm" style="width:400px"/>
		</field:display>
		<field:display name="Use Category Manager" feature="Pledges Names">
			<c:set var="select_id" value="pledgeTitleDropDown" /><c:set var="extra_tags">name="pledgeTitleId" data-live-search="true"</c:set>
			<c:set var="select_values" value="${pledgeForm.pledgeNames}" />
			<c:set var="select_init_value" value="${pledgeForm.pledgeTitleId}" />
			<%@include file="renderShimList.jspf" %>
		</field:display>		
	</div>		
</div>

<div class="highlight text-center h5 bold"><digi:trn key="aim:donorInformation">Donor Information</digi:trn></div>

<div class="container-fluid">
	<div class="col-xs-4 h6 bold">
		<span class="red">*</span> <digi:trn>Organization Group</digi:trn>
	</div>
	
	<div class="col-xs-6 col-xs-offset-1">
		<html:select property="selectedOrgGrpId" styleId="org_grp_dropdown_id">
			<c:forEach var="orgGroup" items="${pledgeForm.orgGroups}">
				<option value="${orgGroup.id}"<c:if test="${pledgeForm.selectedOrgGrpId == orgGroup.id}">selected="selected"</c:if> >
					<c:out value="${orgGroup.value}" />
				</option>
			</c:forEach>
		</html:select>
	</div>
	
	<div class="clearfix">&nbsp;</div>
	<field:display name="Who Authorized Pledge" feature="Pledge Donor Information">
		<div class="col-xs-9">
			<label for="whoAuthorizedPledge"><digi:trn key="whoHasAuthorizedPledge">Who Has Authorized Pledge?</digi:trn></label>
			<html:text property="whoAuthorizedPledge" styleClass="form-control input-sm"/>
		</div>		
	</field:display>
	

	<field:display name="Further Approval Needed" feature="Pledge Donor Information">
		<div class="col-xs-9">
			<label for="furtherApprovalNedded"><digi:trn key="pleaseIndicateFurtherApprovalNeeded">Please Indicate any Further Approval Needed</digi:trn></label>
			<html:text property="furtherApprovalNedded" styleClass="form-control input-sm"/>
		</div>		
	</field:display>	
</div>
