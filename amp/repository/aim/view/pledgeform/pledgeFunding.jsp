<%@page trimDirectiveWhitespaces="true"%>
<%-- renders the funding part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />

<div id='pledge_form_funding_data'>
	<c:if test="${empty pledgeForm.selectedFunding}">
		<div class="text-center"><h3><digi:trn>No Pledge Details</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.selectedFunding}">
		<c:forEach var="fund" items="${pledgeForm.selectedFunding}" varStatus="index">
			<c:set var="indexLoc" value="${indexLoc+1}" />
			<div class="amp-subsection" id="pledge_form_row_for_funding_${fund.uniqueId}">
				<div class="amp-subsection-title">
					<digi:trn>Pledge Detail</digi:trn> nr. ${indexLoc}
					<button type="button" onclick="fundingsController.onDelete(${fund.uniqueId});" class="btn btn-danger btn-xs amp-btn-delete-subsection"><digi:trn>Delete</digi:trn></button></span>
				</div>		
			<div class="container-fluid"> <!-- misc data -->
				<div class="col-xs-5">
					<label for="pledgeTypeDropDown_${indexLoc}"><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></label> <br />					
					<c:set var="select_id" value="pledgeTypeDropDown_${indexLoc}" /><c:set var="extra_tags">data-width="100%"</c:set>
					<c:set var="select_values" value="${pledgeForm.pledgeTypes}" />
					<c:set var="select_init_value" value="${fund.pledgeTypeId}" />
					<%@include file="renderShimList.jspf" %>				
				</div>
				<div class="col-xs-7">
					<div class="form-inline">
						<label for="pledgeAmount_${indexLoc}"><digi:trn key="aim:amount">Amount</digi:trn></label> <br />
						<input type="text" id="pledgeAmount_${indexLoc}" class="form-control input-sm" value="${fund.amount}"/>
					
						<c:set var="select_id" value="pledgeCurrencyDropDown_${indexLoc}" /><c:set var="extra_tags"></c:set>
						<c:set var="select_values" value="${pledgeForm.validCurrencies}" />
						<c:set var="select_init_value" value="${fund.currencyId}" />
						<%@include file="renderShimList.jspf" %>
					</div>										
				</div>
				<div class="col-xs-6">
					<label for="pledgeFundingYear_${indexLoc}"><digi:trn key="aim:year">Year</digi:trn></label>
					<input type="text" id="pledgeFundingYear_${indexLoc}" class="form-control input-sm" value="${fund.fundingYear}"/> 					
				</div>
<%--				<div class="col-xs-5">
					<label for="pledgeCurrencyDropDown_${indexLoc}"><digi:trn key="aim:typeOfCurrency">Currency</digi:trn></label> <br />  
				</div>--%>
				<c:if test="${pledgeForm.fundingShowTypeOfAssistance}">
					<div class="col-xs-5">
						<label for="pledgeTypeOfAssistanceDropDown_${indexLoc}"><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></label> <br />
						<c:set var="select_id" value="pledgeTypeOfAssistanceDropDown_${indexLoc}" /><c:set var="extra_tags">data-width="100%"</c:set>
						<c:set var="select_values" value="${pledgeForm.typesOfAssistance}" />
						<c:set var="select_init_value" value="${fund.typeOfAssistanceId}" />
						<%@include file="renderShimList.jspf" %>
					</div>
				</c:if>
				
				<c:if test="${pledgeForm.fundingShowAidModality}">
					<div class="col-xs-5">
						<label for="pledgeAidModalityDropDown_${indexLoc}"><digi:trn key="aim:aidModality">Aid Modality</digi:trn></label><br />				
						<c:set var="select_id" value="pledgeAidModalityDropDown_${indexLoc}" /><c:set var="extra_tags">data-width="100%"</c:set>
						<c:set var="select_values" value="${pledgeForm.aidModalities}" />
						<c:set var="select_init_value" value="${fund.aidModalityId}" />
						<%@include file="renderShimList.jspf" %>					
					</div>
				</c:if>
			</div>
			</div>
		</c:forEach>
	</c:if>
</div>

<script type="text/javascript">
	on_element_loaded();
</script>
