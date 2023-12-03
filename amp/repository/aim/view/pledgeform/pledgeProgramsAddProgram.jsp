<%-- renders a table of the list of locations for the currently-in-form-pledge --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="org.digijava.ampModule.fundingpledges.form.PledgeForm"%>

<!-- <form class="form-group">  -->
<digi:instance property="pledgeForm" />

<div class="container-fluid" id="pledge_form_programs_change">
<c:if test="${not param.DISABLE_AJAX_BODIES}">
	<div class="row">
		<div class="col-xs-5 text-right"><label class="h5 near-select" for="program_id_select"><digi:trn>Add Theme</digi:trn></label></div>
		<div class="col-xs-7">
			<c:set var="select_id">program_id_select</c:set>
			<c:set var="extra_tags">onchange="programsController.selectChanged(this);"</c:set>
			<c:set var="select_values" value="${pledgeForm.allRootPrograms}" />
			<c:set var="select_init_value" value="${pledgeForm.selectedRootProgram}" />			
 			<%@include file="select_disableable_items.jspf" %>
		</div>
	</div>	
	<div class="row">
		<div class="col-xs-5 text-right"><label class="h5 near-select" for="program_item_select"><digi:trn>Select Program</digi:trn></label></div>
		<div class="col-xs-7">
			<c:set var="select_id">program_item_select</c:set>
			<c:set var="extra_tags">onchange="programsController.selectChanged(this);" class="live-search"</c:set>
			<c:set var="select_values" value="${pledgeForm.allLegalPrograms}" />
			<c:set var="select_init_value" value="-2" />
 			<%@include file="select_disableable_items.jspf" %>
		</div>
	</div>	
	<div class="col-xs-4 col-xs-offset-2 text-right"><button type="button" class="btn btn-success btn-sm" id='pledge_form_programs_change_submit' onclick="programsController.submitClicked(this);" ><digi:trn>Submit</digi:trn></button></div>
	<div class="col-xs-4 col-xs-offset-0 text-left"><button type="button" class="btn btn-success btn-sm" id='pledge_form_programs_change_cancel' onclick="programsController.cancelClicked(this);" ><digi:trn>Cancel</digi:trn></button></div>
</c:if>	
</div>
