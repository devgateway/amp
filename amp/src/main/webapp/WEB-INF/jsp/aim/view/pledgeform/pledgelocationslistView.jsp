<%-- renders a table of the list of locations for the currently-in-form-pledge --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div id='pledge_form_locations_data'>
	<c:if test="${empty pledgeForm.selectedLocsList}">
		<div class="text-center"><h3><digi:trn>No Locations</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.selectedLocsList}">
	<table class="table table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th class="col-xs-8 text-right"><digi:trn>Location Name</digi:trn></th>
				<th class="col-xs-3 text-center"><digi:trn>Percentage</digi:trn></th>
			</tr>
		</thead>
		<tbody>	
		<c:forEach var="selectedLocs" items="${pledgeForm.selectedLocsList}" varStatus="index">
			<c:set var="indexLoc" value="${indexLoc+1}"/>
			<tr id="pledge_form_row_for_location_${selectedLocs.uniqueId}">
				<td class="text-right">${selectedLocs.hierarchicalName}</td>
				<td class="text-left bold">${selectedLocs.percentageDisplayed}%</td>
			</tr>
		</c:forEach>
		</tbody> 
	</table>
	</c:if>
</div>
