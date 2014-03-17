<%--
	renders a single contact from the PledgeForm; his name is given in the mandatory parameter "ct_nr"
	JSP params:
		ct_nr: the contact number [MANDATORY]
		sectiontitle: title text to show [MANDATORY]
		checkboxtext: label to attach to checkbox [OPTIONAL, DO NOT TRANSLATE IT]
	call with <jsp:param name="" value="1"/"2" />
--%>
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
		<div id="pledge_contact_${ct_nr}">
			<div class="highlight text-center h5 bold">
				<digi:trn key="aim:pointContactDonorsConferenceMarch31st">${param.sectiontitle}</digi:trn>
			</div>
			<c:if test="${not empty param.checkboxtext}">
				<div class="checkbox col-xs-5 col-xs-offset-7">
					<label>
    					<input type="checkbox" id="sameContactCheckBox" class="do_not_copy" />
    					<digi:trn>${param.checkboxtext}</digi:trn>
  					</label>
				</div>
			</c:if>
			<div class="container-fluid">
				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Name" feature="${feature_name}">
						<label for="${contact_var}.name"><digi:trn key="pointContactName">Name</digi:trn></label>
						<html:text property="${contact_var}.name" styleClass="form-control input-sm" />
					</field:display>
				</div>
				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Title" feature="${feature_name}">
						<label for="${contact_var}.title"><digi:trn key="pointContactTitle">Title</digi:trn></label>
						<html:text property="${contact_var}.title" styleClass="form-control input-sm" />
					</field:display>
				</div>					
				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Organization" feature="${feature_name}">
						<label for="${contact_var}.orgName"><digi:trn key="pointContactOrganization">Organization</digi:trn></label>
						<html:hidden property="${contact_var}.orgId" styleId="${contact_var}.orgId" />  <!-- leftover from old pledges: why do we duplicate ID and acronym? -->
						<html:text readonly="true" property="${contact_var}.orgName" styleId="${contact_var}.orgName" styleClass="form-control input-sm amp-inline" />
						<span class="aim-button-to-fix">
							<aim:addOrganizationButton useClient="true" useAcronym="true" htmlvalueHolder="${contact_var}.orgId" htmlNameHolder="${contact_var}.orgName" >...</aim:addOrganizationButton>
						</span>												
					</field:display>
				</div>
				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Ministry" feature="${feature_name}">
						<label for="${contact_var}.ministry"><digi:trn key="pointContactMinistry">Ministry</digi:trn></label>
						<html:text property="${contact_var}.ministry" styleClass="form-control input-sm" />
					</field:display>
				</div>					

				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Address" feature="${feature_name}">
						<label for="${contact_var}.address"><digi:trn key="pointContactAddress">Address</digi:trn></label>
						<html:text property="${contact_var}.address" styleClass="form-control input-sm" />
					</field:display>
				</div>

				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Telephone" feature="${feature_name}">
						<label for="${contact_var}.telephone"><digi:trn key="pointContactTelephone">Telephone</digi:trn></label>
						<html:text property="${contact_var}.telephone" styleClass="form-control input-sm phone-number" />
					</field:display>
				</div>

				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Email" feature="${feature_name}">
						<label for="${contact_var}.email"><digi:trn key="pointContactEmail">Email</digi:trn></label>
						<html:text property="${contact_var}.email" styleClass="form-control input-sm email-address" />
					</field:display>
				</div>

				<div class="col-xs-6 form-group">
					<field:display name="${feature_name} - Fax" feature="${feature_name}">
						<label for="${contact_var}.fax"><digi:trn key="pointContactFax">Fax</digi:trn></label>
						<html:text property="${contact_var}.fax" styleClass="form-control input-sm phone-number email-address" />
					</field:display>
				</div>
				
				<div class="clearfix">&nbsp</div>
				<div class="underlined text-center h6 bold"><digi:trn key="alternateContactPerson">Alternate Contact Person</digi:trn></div>

				<field:display name="${feature_name} - Alternate Contact" feature="${feature_name}">
					<div class="col-xs-6 form-group">
						<label for="${contact_var}.alternateName"><digi:trn key="pointContactName">Name</digi:trn></label>
						<html:text property="${contact_var}.alternateName" styleClass="form-control input-sm" />
					</div>

					<div class="col-xs-6 form-group">
						<label for="${contact_var}.alternateTelephone"><digi:trn key="pointContactTelephone">Telephone</digi:trn></label>
						<html:text property="${contact_var}.alternateTelephone" styleClass="form-control input-sm phone-number" />
					</div>
					
					<div class="col-xs-6 form-group">
						<label for="${contact_var}.alternateEmail"><digi:trn key="pointContactEmail">Email</digi:trn></label>
						<html:text property="${contact_var}.alternateEmail" styleClass="form-control input-sm email-address" />
					</div>									
				</field:display>
		</div>
	</div>
</feature:display>
