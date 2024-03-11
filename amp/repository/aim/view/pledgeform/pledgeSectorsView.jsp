<%-- renders the programs part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div id='pledge_form_sectors_data'>
<%--<div class="highlight text-center h5 bold"><digi:trn key="aim:sector">Sector</digi:trn></div> --%>
		<c:if test="${empty pledgeForm.allUsedRootSectors}">
			<div class="text-center"><h3><digi:trn>No Sectors</digi:trn></h3></div>
		</c:if>
	<c:if test="${not empty pledgeForm.allUsedRootSectors}">
		<c:forEach var="rootTheme" items="${pledgeForm.allUsedRootSectors}">
			<div class="amp-subsection">
			<div class="amp-subsection-title"><c:out value="${rootTheme.name}" /></div>
			<table class="table table-striped table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th class="col-xs-8 text-right"><digi:trn>Sector Name</digi:trn></th>
						<th class="col-xs-3 text-center"><digi:trn>Percentage</digi:trn></th>
					</tr>
				</thead>
				<tbody>	
					<c:forEach var="selectedSectors" items="${pledgeForm.selectedSectorsList}" varStatus="index">
						<c:set var="indexLoc" value="${indexLoc+1}" />
						<c:if test="${selectedSectors.rootId eq rootTheme.keyAsLong}">
							<tr id="pledge_form_row_for_sector_${selectedSectors.uniqueId}">
								<td class="text-right"><c:out value="${selectedSectors.hierarchicalName}" /></td>
								<td class="text-left bold">${selectedSectors.percentageDisplayed}%</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</c:forEach>
	</c:if>
</div>
