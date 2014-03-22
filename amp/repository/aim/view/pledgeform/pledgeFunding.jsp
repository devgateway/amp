<%@page trimDirectiveWhitespaces="true"%>
<%-- renders the funding part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div id='pledge_form_funding_data'>
	<c:if test="${empty pledgeForm.selectedFunding}">
		<div class="text-center"><h3><digi:trn>No Funding</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.selectedFunding}">
		<table class="table table-striped table-bordered table-hover table-condensed">
			<thead>
				<tr>
					<th class="col-xs-2 text-center"><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></th>
					<c:if test="${pledgeForm.fundingShowTypeOfAssistance}"><th class="col-xs-2 text-center"><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></th></c:if>
					<c:if test="${pledgeForm.fundingShowAidModality}"><th class="col-xs-2 text-center"><digi:trn key="aim:aidModality">Aid Modality</digi:trn></th></c:if>					
					<th class="col-xs-1 text-center"><digi:trn key="aim:amount">Amount</digi:trn></th>
					<th class="col-xs-1 text-center"><digi:trn key="aim:typeOfCurrency">Currency</digi:trn></th>
					<th class="col-xs-1 text-center"><digi:trn key="aim:year">Year</digi:trn></th>
					<th class="col-xs-1"><%--<digi:trn>Delete</digi:trn> --%></th>					
				</tr>
				</thead>
				<tbody>	
					<c:forEach var="fund" items="${pledgeForm.selectedFunding}" varStatus="index">
						<c:set var="indexLoc" value="${indexLoc+1}" />
						<tr id="pledge_form_row_for_funding_${fund.uniqueId}">
							<td class="text-center">
								<c:set var="select_id" value="pledgeTypeDropDown_${indexLoc}" /><c:set var="extra_tags">data-width="100%"</c:set>
								<c:set var="select_values" value="${pledgeForm.pledgeTypes}" />
								<c:set var="select_init_value" value="${fund.pledgeTypeId}" />
								<%@include file="renderShimList.jspf" %>
							</td>
							<c:if test="${pledgeForm.fundingShowTypeOfAssistance}"><td class="text-center">
								<c:set var="select_id" value="pledgeTypeOfAssistanceDropDown_${indexLoc}" /><c:set var="extra_tags">data-width="100%"</c:set>
								<c:set var="select_values" value="${pledgeForm.typesOfAssistance}" />
								<c:set var="select_init_value" value="${fund.typeOfAssistanceId}" />
								<%@include file="renderShimList.jspf" %>
							</td></c:if>
							<c:if test="${pledgeForm.fundingShowAidModality}"><td class="text-center">
								<c:set var="select_id" value="pledgeAidModalityDropDown_${indexLoc}" /><c:set var="extra_tags">data-width="100%"</c:set>
								<c:set var="select_values" value="${pledgeForm.aidModalities}" />
								<c:set var="select_init_value" value="${fund.aidModalityId}" />
								<%@include file="renderShimList.jspf" %>
							</td></c:if>							
							<td class="text-center">
								<input type="text" id="pledgeAmount_${indexLoc}" class="form-control input-sm" value="<c:out value='${fund.amount}' /> "/> 
							</td>
							<td class="text-center">
								<c:set var="select_id" value="pledgeCurrencyDropDown_${indexLoc}" /><c:set var="extra_tags">data-width="100%"</c:set>
								<c:set var="select_values" value="${pledgeForm.validCurrencies}" />
								<c:set var="select_init_value" value="${fund.currencyId}" />
								<%@include file="renderShimList.jspf" %>
							</td>
							<td class="text-center">
								<input type="text" id="pledgeFundingYear_${indexLoc}" class="form-control input-sm" value="<c:out value='${fund.fundingYear}' /> "/> 
							</td>
							<td class="text-center"><button type="button" onclick="fundingsController.onDelete(${fund.uniqueId});" class="btn btn-danger btn-xs"><digi:trn>Delete</digi:trn></button></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</c:if>
</div>

<script type="text/javascript">
	on_element_loaded();
</script>
