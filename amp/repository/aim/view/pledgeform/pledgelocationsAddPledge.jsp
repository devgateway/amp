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

<div id="pledge_add_location_area">
	<digi:form action="/pledgeLocationSelected.do">

	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-5 text-right"><label class="h5 near-select" for="impl_level_select"><digi:trn>Implementation level</digi:trn></label></div>
			<div class="col-xs-7">	
				<c:set var="translation"><digi:trn key="aim:addActivityImplLevelFirstLine">Please select from below</digi:trn></c:set>
				<category:showoptions multiselect="false" styleId="impl_level_select" firstLine="${translation}" name="pledgeForm" property="levelId" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LEVEL_KEY %>" />
  			</div>
		</div>
		<div class="row">
			<div class="col-xs-5 text-right"><label class="h5 near-select" for="impl_location_select"><digi:trn>Implementation location</digi:trn></label></div> <%--getAllValidImplementationLocationChoices --%>
			<div class="col-xs-7">
				<html:select styleId="impl_location_select" property="implemLocationLevel">
					<c:forEach var="ch" items="${pledgeForm.allValidImplementationLocationChoices}">
						<html:option value="${ch.key}"><c:out value="${ch.value}" /></html:option> <%-- c:out does automatic escaping, unlike ${} --%>
					</c:forEach>
				</html:select>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-5 text-right"><label class="h5 near-select" for="location_id_select"><digi:trn>Location</digi:trn></label></div>
			<div class="col-xs-7">
					<c:set var="select_id">location_id_select</c:set>
					<c:set var="select_multiple">multiple</c:set>
					<c:set var="select_values" value="${pledgeForm.allValidLocations}" />
					<%@include file="select_disableable_items.jspf" %>
			</div>
		</div>
		<div class="text-center"><button type="button" class="btn btn-success btn-sm" id='pledges_submit_locations_button' onclick='pledges_submit_add_locations()'>Submit</button></div>
	</div>
	</digi:form>
</div>
<script type="text/javascript">
	on_element_loaded();
</script>
