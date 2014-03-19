<%-- renders a table of the list of locations for the currently-in-form-pledge --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<!-- <form class="form-group">  -->
<digi:instance property="pledgeForm" />

<div class="container-fluid" id="pledge_add_program_area">
	<div class="row">
		<div class="col-xs-5 text-right"><label class="h5 near-select" for="location_id_select"><digi:trn>Add Theme</digi:trn></label></div>
		<div class="col-xs-7">
			<c:set var="select_id">program_id_select</c:set>
			<c:set var="select_multiple"></c:set>
			<c:set var="select_values" value="${pledgeForm.allRootPrograms}" />
 			<%@include file="select_disableable_items.jspf" %>
		</div>
	</div>
	<div class="col-xs-4 col-xs-offset-2 text-right"><button type="button" class="btn btn-success btn-sm" id='pledges_add_programs_submit_button' onclick='pledges_add_programs_submit()'><digi:trn>Submit</digi:trn></button></div>
	<div class="col-xs-4 col-xs-offset-0 text-left"><button type="button" class="btn btn-success btn-sm" id='pledges_add_programs_cancel_button' onclick='pledges_add_programs_cancel()'><digi:trn>Cancel</digi:trn></button></div>
</div>

<script type="text/javascript">
	on_element_loaded();
	$(document).ready(function(){
		$('#pledge_add_program_area select').attr('data-live-search', 'true');
	}); // Struts is stupid and does not allow to inject custom attributes
</script>


