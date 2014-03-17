<%-- renders a table of the list of locations for the currently-in-form-pledge --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<c:set var="numeric_value_only_msg"><digi:trn jsFriendly='true' key="aim:addSecorNumericValueErrorMessage">Please enter numeric value only</digi:trn></c:set>
<c:set var="sum_cannot_exceed_100_msg"><digi:trn jsFriendly='true'>Sum of percentages can not exceed 100</digi:trn></c:set>
<div id='pledge_locations_table'>
	<c:if test="${empty pledgeForm.selectedLocs}">
		<div class="text-center"><h3><digi:trn>No Locations</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.selectedLocs}">
	<table class="table table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th class="col-xs-8 text-right">Location Name</th>
				<th class="col-xs-3 text-center">Percentage</th>
				<th class="col-xs-1 text-center">Delete</th>
			</tr>
		</thead>
		<tbody>	
		<c:forEach var="selectedLocs" items="${pledgeForm.selectedLocs}" varStatus="index">
			<c:set var="indexLoc" value="${indexLoc+1}"/>
			<tr>
				<td class="col-xs-8 text-right">[${selectedLocs.location.name}]</td>
				<td class="col-xs-3">
					<%--<input class="form-control input-sm" type="text" name="locationpercentage" placeholder="Location percentage" size="5" value="25%"/> --%>
					<html:text name="selectedLocs" indexed="true" property="locationpercentage" size="5"  onblur="return pledges_form_check_percentage(this, 'input-pledges-locations', '${numeric_value_only_msg}', '${sum_cannot_exceed_100_msg}')" styleClass="form-control input-sm input-pledges-locations" />
				</td>
				<td class="col-xs-1"><button type="button" onclick="pledges_form_delete_location(${indexLoc});" class="btn btn-danger btn-sm">Delete</button></td>
			</tr>
		</c:forEach>
		</tbody> 
	</table>
	</c:if>
</div>
