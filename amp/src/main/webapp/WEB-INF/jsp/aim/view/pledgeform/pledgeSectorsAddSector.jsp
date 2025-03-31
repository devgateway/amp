<%-- renders the "add a sector" part of a page --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<!-- <form class="form-group">  -->
<digi:instance property="pledgeForm" />

<div class="container-fluid" id="pledge_form_sectors_change">
<c:if test="${not param.DISABLE_AJAX_BODIES}">
	<div class="row">
		<div class="col-xs-5 text-right"><label class="h5 near-select" for="sector_id_select"><digi:trn>Sector Scheme</digi:trn></label></div>
		<div class="col-xs-7">
			<c:set var="select_id">sector_id_select</c:set>
			<c:set var="extra_tags">onchange="sectorsController.selectChanged(this);"</c:set>
			<c:set var="select_values" value="${pledgeForm.allRootSectors}" />
			<c:set var="select_init_value" value="${pledgeForm.selectedRootSector}" />			
 			<%@include file="select_disableable_items.jspf" %>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-5 text-right"><label class="h5 near-select" for="sector_item_select"><digi:trn>Select Sector</digi:trn></label></div>
		<div class="col-xs-7">
			<c:set var="select_id">sector_item_select</c:set>
			<c:set var="extra_tags">onchange="sectorsController.selectChanged(this);" class="live-search"</c:set>
			<c:set var="select_values" value="${pledgeForm.allLegalSectors}" />
			<c:set var="select_init_value" value="-2" />
 			<%@include file="select_disableable_items.jspf" %>
		</div>
	</div>	
	<div class="col-xs-4 col-xs-offset-2 text-right"><button type="button" class="btn btn-success btn-sm" id='pledge_form_sectors_change_submit' onclick="sectorsController.submitClicked(this);" ><digi:trn>Submit</digi:trn></button></div>
	<div class="col-xs-4 col-xs-offset-0 text-left"><button type="button" class="btn btn-success btn-sm" id='pledge_form_sectors_change_cancel' onclick="sectorsController.cancelClicked(this);" ><digi:trn>Cancel</digi:trn></button></div>
</c:if>	
</div>


