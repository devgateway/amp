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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ page import="org.digijava.ampModule.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div class="container-fluid">
	<div>
	<div class="col-xs-4 h6 bold">
		<span class="red">*</span> <digi:trn key="aim:pledgeIdentification">Pledge Identification</digi:trn>
	</div>
	<div class="col-xs-6 col-xs-offset-1">
		<%--  normally just one of these will be enabled, no need for <c:if> --%>
		<c:if test="${pledgeForm.useFreeText}">
			<!--Allow user to enter free text if it's configured in the FM AMP-16005-->
			<html:text property="titleFreeText" styleId="titleFreeText" styleClass="form-control input-sm validate-mandatory validate-min-length-5"/>
		</c:if>
		<c:if test="${not pledgeForm.useFreeText}">
			<c:set var="select_id" value="pledgeTitleDropDown" /><c:set var="extra_tags">name="pledgeTitleId" data-live-search="true" class="validate-mandatory form-control"</c:set>
			<c:set var="select_values" value="${pledgeForm.pledgeNames}" />
			<c:set var="select_init_value" value="${pledgeForm.pledgeTitleId}" />
			<%@include file="renderShimList.jspf" %>
		</c:if>
		</div>		
	</div>		
</div>

<%--<div class="highlight text-center h5 bold"><digi:trn key="aim:donorInformation">Donor Information</digi:trn></div>  --%>

<div class="container-fluid">
	<div class="label-and-select">
		<div class="col-xs-4 h6 bold">
			<span class="red">*</span> 
			<label for="org_grp_dropdown_id"><digi:trn>Organization Group</digi:trn></label>
		</div>
	
		<div class="col-xs-6 col-xs-offset-1 pledge-orgs">
			<html:select property="selectedOrgGrpId" styleId="org_grp_dropdown_id" styleClass="validate-mandatory">
				<c:forEach var="orgGroup" items="${pledgeForm.orgGroups}">
					<option value="${orgGroup.id}"<c:if test="${pledgeForm.selectedOrgGrpId == orgGroup.id}">selected="selected"</c:if> >
						<c:out value="${orgGroup.value}" />
					</option>
				</c:forEach>
			</html:select>
		</div>
	</div>
	
	<div class="clearfix">&nbsp;</div>
	<feature:display name="Pledge Status" ampModule="Pledges">
		<div class="label-and-select">
			<div class="col-xs-4 h6 bold">
				<span class="red">*</span> 
				<label for="pledgeStatusDropDown"><digi:trn>Pledge Status</digi:trn></label>
			</div>
	
			<div class="col-xs-6 col-xs-offset-1">
				<c:set var="select_id" value="pledgeStatusDropDown" /><c:set var="extra_tags">name="pledgeStatusId" class="validate-mandatory"</c:set>
				<c:set var="select_values" value="${pledgeForm.pledgeStatuses}" />
				<c:set var="select_init_value" value="${pledgeForm.pledgeStatusId}" />
				<%@include file="renderShimList.jspf" %>
			</div>
		</div>
	</feature:display>
	
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
