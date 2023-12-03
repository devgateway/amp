<%@page trimDirectiveWhitespaces="true"%>
<%-- renders the funding part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.ampModule.fundingpledges.form.PledgeForm"%>

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
				</div>		
				
			<div class="container-fluid"> <!-- misc data -->			
				<c:set var="acvlId" value="${selectedFunding.pledgeTypeId}" />
				<c:set var="acvlLabel"><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></c:set>
				<%@include file="renderAcvlView.jspf" %>
				
				<div class="col-xs-6">
					<div class="form-inline" >
						<span class="bold"><digi:trn key="aim:amount">Amount</digi:trn></span>: 
						<span dir="ltr"><c:out value="${selectedFunding.amount}"/></span>&nbsp;<span class="bold">${selectedFunding.currencyCode}</span>
					</div>										
				</div>
				
				<div class="col-xs-6">
					<span class="bold"><digi:trn key="aim:year">Pledge Time Frame</digi:trn></span>:
					<span>${selectedFunding.fundingTimeFrame}</span>
				</div>
				
				<c:set var="acvlId" value="${selectedFunding.typeOfAssistanceId}" />
				<c:set var="acvlLabel"><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></c:set>
				<%@include file="renderAcvlView.jspf" %>
				
				<c:set var="acvlId" value="${selectedFunding.aidModalityId}" />
				<c:set var="acvlLabel"><digi:trn key="aim:aidModality">Aid Modality</digi:trn></c:set>
				<%@include file="renderAcvlView.jspf" %>
								
			</div>
			</div>
		</c:forEach>
	</c:if>
</div>