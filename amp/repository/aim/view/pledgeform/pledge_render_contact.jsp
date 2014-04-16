<%--
	renders a single contact from the PledgeForm; his name is given in the mandatory parameter "ct_nr"
	JSP params:
		ct_nr: the contact number [MANDATORY]
		sectiontitle: title text to show [MANDATORY]
		checkboxtext: label to attach to checkbox [OPTIONAL, DO NOT TRANSLATE IT]
	call with <jsp:param name="" value="1"/"2" />
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

<c:set var="ct_nr">${param.ct_nr}</c:set>
<c:set var="feature_name">Pledge Contact ${ct_nr}</c:set>
<c:set var="contact_var">contact${ct_nr}</c:set>
<digi:instance property="pledgeForm" />
<feature:display name="${feature_name}" module="Pledges">
	<aim:renderFormSubsection title="${param.sectiontitle}" styleId="pledge_form_contact_${ct_nr}">
		<div id="pledge_contact_${ct_nr}">
			<c:if test="${not empty param.checkboxtext}">
				<div class="checkbox col-xs-5 col-xs-offset-7">
					<label>
    					<input type="checkbox" id="sameContactCheckBox" class="do_not_copy" />
    					<digi:trn>${param.checkboxtext}</digi:trn>
  					</label>
				</div>
			</c:if>
			<div class="container-fluid">
				<jsp:include page="renderConditionedInput.jsp">
					<jsp:param name="field_name" value="Name" /><jsp:param name="field" value="name" />
					<jsp:param name="text" value="Name" />
				</jsp:include>

				<jsp:include page="renderConditionedInput.jsp">
					<jsp:param name="field_name" value="Title" /><jsp:param name="field" value="title" />
					<jsp:param name="text" value="Title" />
				</jsp:include>
								
				<div class="col-xs-6">
					<field:display name="${feature_name} - Organization" feature="${feature_name}">
						<label for="${contact_var}.orgName"><digi:trn key="pointContactOrganization">Organization</digi:trn></label>
						<html:hidden property="${contact_var}.orgId" styleId="${contact_var}.orgId" />  <!-- leftover from old pledges: why do we duplicate ID and acronym? -->
						<html:text readonly="true" property="${contact_var}.orgName" styleId="${contact_var}.orgName" styleClass="form-control input-sm amp-inline" />
						<span class="aim-button-to-fix">
							<aim:addOrganizationButton useClient="true" useAcronym="true" htmlvalueHolder="${contact_var}.orgId" htmlNameHolder="${contact_var}.orgName" >...</aim:addOrganizationButton>
						</span>												
					</field:display>
				</div>
				
				<jsp:include page="renderConditionedInput.jsp">
					<jsp:param name="field_name" value="Ministry" /><jsp:param name="field" value="ministry" />
					<jsp:param name="text" value="Ministry" /> <jsp:param name="classes" value="" />
				</jsp:include>

				<jsp:include page="renderConditionedInput.jsp">
					<jsp:param name="field_name" value="Address" /><jsp:param name="field" value="address" />
					<jsp:param name="text" value="Address" /> <jsp:param name="classes" value="" />
				</jsp:include>
				
				<jsp:include page="renderConditionedInput.jsp">
					<jsp:param name="field_name" value="Telephone" /><jsp:param name="field" value="telephone" />
					<jsp:param name="text" value="Telephone" /> <jsp:param name="classes" value="validate-phone-number" />
				</jsp:include>
				
				<jsp:include page="renderConditionedInput.jsp">
					<jsp:param name="field_name" value="Email" /><jsp:param name="field" value="email" />
					<jsp:param name="text" value="Email" /> <jsp:param name="classes" value="validate-email-address" />
				</jsp:include>
				
				<jsp:include page="renderConditionedInput.jsp">
					<jsp:param name="field_name" value="Fax" /><jsp:param name="field" value="fax" />
					<jsp:param name="text" value="Fax" /> <jsp:param name="classes" value="validate-phone-number" />
				</jsp:include>
												
				<div class="clearfix">&nbsp</div>
				<div class="underlined text-center h6 bold"><digi:trn key="alternateContactPerson">Alternate Contact Person</digi:trn></div>

				<field:display name="${feature_name} - Alternate Contact" feature="${feature_name}">
				
					<jsp:include page="renderConditionedInput.jsp">
						<jsp:param name="field_name" value="" /><jsp:param name="field" value="alternateName" />
						<jsp:param name="text" value="Name" /> <jsp:param name="classes" value="" />
					</jsp:include>
					
					<jsp:include page="renderConditionedInput.jsp">
						<jsp:param name="field_name" value="" /><jsp:param name="field" value="alternateTelephone" />
						<jsp:param name="text" value="Telephone" /> <jsp:param name="classes" value="validate-phone-number" />
					</jsp:include>
					
					<jsp:include page="renderConditionedInput.jsp">
						<jsp:param name="field_name" value="" /><jsp:param name="field" value="alternateEmail" />
						<jsp:param name="text" value="Email" /> <jsp:param name="classes" value="validate-email-address" />
					</jsp:include>
													
				</field:display>
		</div>
	</div>
	</aim:renderFormSubsection>
</feature:display>
