<%@page trimDirectiveWhitespaces="true"%>
<%-- renders the funding part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>
<%@ page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil"%>

<digi:instance property="pledgeForm" />

<div id='pledge_form_funding_data'>
	<c:if test="${empty pledgeForm.selectedFundingList}">
		<div class="text-center"><h3><digi:trn>No Pledge Details</digi:trn></h3></div>
	</c:if>
	<c:if test="${not empty pledgeForm.selectedFundingList}">
		<c:set var="indexLoc" value="-1" />
		<c:forEach var="selectedFunding" items="${pledgeForm.selectedFundingList}" varStatus="index">
			<c:set var="indexLoc" value="${indexLoc+1}" />
			<div class="amp-subsection" id="pledge_form_row_for_funding_${selectedFunding.uniqueId}">
				<div class="amp-subsection-title">
					<digi:trn>Pledge Detail</digi:trn> nr. ${indexLoc+1}
					<button type="button" onclick="fundingsController.onDelete(${selectedFunding.uniqueId});" class="btn btn-danger btn-xs amp-btn-delete-subsection"><digi:trn>Delete</digi:trn></button></span>
				</div>		
			<div class="container-fluid"> <!-- misc data -->
				<div class="col-xs-6">
					<label for="pledgeTypeDropDown_${indexLoc}"><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></label> <br />					
					<c:set var="select_id" value="pledgeTypeDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].pledgeTypeId" data-width="100%"</c:set>
					<c:set var="select_values" value="${pledgeForm.pledgeTypes}" />
					<c:set var="select_init_value" value="${selectedFunding.pledgeTypeId}" />
					<%@include file="renderShimList.jspf" %>				
				</div>
				<div class="col-xs-6">
					<div class="form-inline" >
						<label for="pledgeAmount_${indexLoc}"><digi:trn key="aim:amount">Amount</digi:trn></label> <br />
						<input name="selectedFunding[${indexLoc}].amount" type="text" dir="ltr" id="pledgeAmount_${indexLoc}" class="form-control input-sm validate-mandatory-number ltr-input" 
								value="${selectedFunding.amount}"/>
					
						<c:set var="select_id" value="pledgeCurrencyDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].currencyId" class="validate-mandatory"</c:set>
						<c:set var="select_values" value="${pledgeForm.validCurrencies}" />
						<c:set var="select_init_value" value="${selectedFunding.currencyId}" />
						<%@include file="renderShimList.jspf" %>
					</div>										
				</div>
				
				<div class="col-xs-6">				
					<c:choose>
						<c:when test="${pledgeForm.fundingShowDateRange}">
							<div >
								<div class="pledge-date">
							<label for="pledgeFundingDateStart_${indexLoc}">
								<digi:trn key="aim:year">Pledge start date</digi:trn></label> <br />
							<span>
								<input name="selectedFunding[${indexLoc}].fundingDateStart" type="text" id="pledgeFundingDateStart_${indexLoc}" 
								data-date-format="${pledgeForm.globalDateFormat}" 
								class="form-control input-sm inline-input validate-date-range-start date-range-start validate-date-range-group-${selectedFunding.uniqueId}" value="${selectedFunding.fundingDateStartSettingsFormat}"/>
							</span>
							</div>
								<div class="pledge-date">
							<label for="pledgeFundingDateEnd_${indexLoc}">
								<digi:trn key="aim:year">Pledge end date</digi:trn> </label> <br />
							<span>
								<input name="selectedFunding[${indexLoc}].fundingDateEnd" type="text" id="pledgeFundingDateEnd_${indexLoc}" 
								data-date-format="${pledgeForm.globalDateFormat}" 
								class="form-control input-sm inline-input validate-date-range-end date-range-end validate-date-range-group-${selectedFunding.uniqueId}" value="${selectedFunding.fundingDateEndSettingsFormat}"/>
							</span>
							</div>
				</div>
						</c:when>
						<c:otherwise>
							<label for="pledgeFundingYear_${indexLoc}"><digi:trn key="aim:year">Year</digi:trn></label>
							<input name="selectedFunding[${indexLoc}].fundingYear" type="text" id="pledgeFundingYear_${indexLoc}" class="form-control input-sm validate-year" value="${selectedFunding.fundingYear}"/>
						</c:otherwise> 
					</c:choose>
					</div>					
				<c:if test="${pledgeForm.fundingShowTypeOfAssistance}">
					<div class="col-xs-6">
						<label for="pledgeTypeOfAssistanceDropDown_${indexLoc}"><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></label> <br />
						<c:set var="select_id" value="pledgeTypeOfAssistanceDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].typeOfAssistanceId" data-width="100%"</c:set>
						<c:set var="select_values" value="${pledgeForm.typesOfAssistance}" />
						<c:set var="select_init_value" value="${selectedFunding.typeOfAssistanceId}" />
						<%@include file="renderShimList.jspf" %>
					</div>
				</c:if>
				
				<c:if test="${pledgeForm.fundingShowAidModality}">
					<div class="col-xs-6">
						<label for="pledgeAidModalityDropDown_${indexLoc}"><digi:trn key="aim:aidModality">Aid Modality</digi:trn></label><br />				
						<c:set var="select_id" value="pledgeAidModalityDropDown_${indexLoc}" /><c:set var="extra_tags">name="selectedFunding[${indexLoc}].aidModalityId"</c:set>
						<c:set var="select_values" value="${pledgeForm.aidModalities}" />
						<c:set var="select_init_value" value="${selectedFunding.aidModalityId}" />
						<%@include file="renderShimList.jspf" %>					
					</div>
				</c:if>
			</div>
			</div>
		</c:forEach>
	</c:if>
</div>