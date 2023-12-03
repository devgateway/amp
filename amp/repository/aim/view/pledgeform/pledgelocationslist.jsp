<%-- renders a table of the list of locations for the currently-in-form-pledge --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.ampModule.fundingpledges.form.PledgeForm"%>

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
				<th class="col-xs-1 text-center"><%--<digi:trn>Delete</digi:trn> --%></th>
			</tr>
		</thead>
		<tbody>	
		<c:forEach var="selectedLocs" items="${pledgeForm.selectedLocsList}" varStatus="index">
			<c:set var="indexLoc" value="${indexLoc+1}"/>
			<tr id="pledge_form_row_for_location_${selectedLocs.uniqueId}">
				<td class="text-right">${selectedLocs.hierarchicalName}</td>
				<td>
					<%--<input class="form-control input-sm" type="text" name="locationpercentage" placeholder="Location percentage" size="5" value="25%"/> --%>
					<html:text name="selectedLocs" indexed="true" property="percentage" size="5" styleClass="form-control input-sm validate-percentage validate-percentage-input-pledges-locations" />
				</td>
				<td class="text-center"><button type="button" onclick="locationsController.onDelete(${selectedLocs.uniqueId});" class="btn btn-danger btn-xs"><digi:trn>Delete</digi:trn></button></td>
			</tr>
		</c:forEach>
		</tbody> 
	</table>
	</c:if>
</div>
