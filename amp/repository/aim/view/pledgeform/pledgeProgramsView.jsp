<%-- renders the programs part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/aim" prefix="aim" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div id='pledge_form_programs_data'>
	<c:if test="${empty pledgeForm.allUsedRootProgs}">
		<div class="text-center"><h3><digi:trn>No Programs</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.allUsedRootProgs}">
		<c:forEach var="rootTheme" items="${pledgeForm.allUsedRootProgs}">
			<div class="amp-subsection">
			<div class="amp-subsection-title"><c:out value="${rootTheme.name}" /></div>
			<table class="table table-striped table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th class="col-xs-8 text-right"><digi:trn>Program Name</digi:trn></th>
						<th class="col-xs-3 text-center"><digi:trn>Percentage</digi:trn></th>
					</tr>
				</thead>
				<tbody>	
					<c:forEach var="selectedProgs" items="${pledgeForm.selectedProgsList}" varStatus="index">
						<c:set var="indexLoc" value="${indexLoc+1}" />
						<c:if test="${selectedProgs.rootId eq rootTheme.keyAsLong}">
							<tr id="pledge_form_row_forprog_${selectedProgs.uniqueId}">
								<td class="text-right"><c:out value="${selectedProgs.hierarchicalName}" /></td>
								<td class="text-left bold">${selectedProgs.percentageDisplayed}%</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</c:forEach>
	</c:if>
</div>
