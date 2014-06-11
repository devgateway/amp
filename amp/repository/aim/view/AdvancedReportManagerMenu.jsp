<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimAdvancedReportForm" />


<script type="text/javascript" language="JavaScript">
function menuA(val){
	if(${aimAdvancedReportForm.reportEdit}== 'false'){
		if (!check())
    	return false;
	}    
	<digi:context name="base" property="context/module/moduleinstance/advancedReportManager.do?check=" />
	href = "<%= base %>";
	switch (val){
    	case 0:
    		href = href + "forward";    		
    		break;
    	case 1:
    		href = href + "SelectCols";    		
    		break;
    	case 2:
    		href = href + "SelectRows";    		
    		break;
    	case 3:
    		href = href + "SelectMeasures";    		
    		break;
    	case 4:
    		href = href + "4";    		
    		break;
    	case 5:
    		href = href + "SelectCols";    		
    		break;
    }
	document.aimAdvancedReportForm.action = href;
	document.aimAdvancedReportForm.target = "_self";
	document.aimAdvancedReportForm.submit();
	
	return true;
}
function nope(){
	alert('<digi:trn key="aim:reportBuilder:ReportSkipValidation">Please do not skip steps!</digi:trn>');
	return true;
}
window.onload = function (){
	assignOriginalValues('aimAdvancedReportForm');
}

function assignOriginalValues(formName){
	var form = document.getElementsByName(formName)[0];
	var formElements = form.elements;
	
	for(idx2 = 0 ; idx2 < formElements.length ; idx2++)
	{
		formElements[idx2].originalValue = formElements[idx2].value;
		if(formElements[idx2].checked != "undefined"){
		formElements[idx2].originalChecked = formElements[idx2].checked;
		}
	}
	
}
function isFormModified(formName)
{
	var form = document.getElementsByName(formName)[0];
	var formElements = form.elements;
	
	for(idx2 = 0 ; idx2 < formElements.length ; idx2++)
	{
		if(formElements[idx2].originalValue!=formElements[idx2].value){
			
			return true;
		}
	}
	return false;
}

function saveEditedReport(){
	debugger;
	if (${aimAdvancedReportForm.reportIsModified} == true || isFormModified('aimAdvancedReportForm')) {
		if (confirm('<digi:trn key="aim:reportBuilder:saveReportConfirmation">Do you want to save the changes you made to the report ?</digi:trn>')) {
			<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SaveReport" />
			document.aimAdvancedReportForm.action = "<%= step %>";
			document.aimAdvancedReportForm.target = "_self";
			document.aimAdvancedReportForm.submit();
			return true;
		}
		return false;
	} else {
		document.aimAdvancedReportForm.action = "/viewMyDesktop.do";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
</script>


								<tr width="100%" valign="top">
									<td height="20">
										<table bgcolor="#f4f4f4" align="left" valign="bottom" cellpadding="0" cellspacing="1" height="20">
											<tr>
												<td noWrap align=left> 
													<c:set var="translation">
														<digi:trn key="aim:clickToSelectReportType">Click here to Select Report Type</digi:trn>
													</c:set>
													<c:if test="${aimAdvancedReportForm.reportEdit==true}">	
														<c:if test="${aimAdvancedReportForm.currentTabName=='forward'}">
															<a class="sub-navSelected" style="cursor:pointer;" title="${translation}"  onclick="return menuA(0)">
																1 :   <digi:trn key="aim:SelectedReportType2">Report Type</digi:trn>
															</a>
														</c:if>	
														<c:if test="${aimAdvancedReportForm.currentTabName!='forward'}">
															<a class="sub-nav" style="cursor:pointer;" title="${translation}"  onclick="return menuA(0)">
																1 :   <digi:trn key="aim:SelectedReportType2">Report Type</digi:trn>
															</a>
														</c:if>														
														
													 </c:if>
													 <c:if test="${aimAdvancedReportForm.reportEdit==false}">
														<c:if test="${aimAdvancedReportForm.currentTabName=='forward'}">
															<a class="sub-navSelected" style="cursor:pointer;" title="${translation}"  onclick="return menuA(0)">
																1 :   <digi:trn key="aim:SelectedReportType2">Select Report Type</digi:trn>
															</a>
														</c:if>	
														<c:if test="${aimAdvancedReportForm.currentTabName!='forward'}">
															<a class="sub-nav" style="cursor:pointer;" title="${translation}"  onclick="return menuA(0)">
																1 :   <digi:trn key="aim:SelectedReportType2">Select Report Type</digi:trn>
															</a>
														</c:if>	
													 </c:if>													
												</td>
											<!--ends here-->
												<td noWrap align=left> 
													<c:set var="translation">
														<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
													</c:set>
													<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="0">
														<c:if test="${aimAdvancedReportForm.currentTabName=='SelectCols'}">
															<a class="sub-navSelected" style="cursor:pointer;" title="${translation}"  onclick="return menuA(1)">
																2 :   <digi:trn key="aim:SelectColumns2">Select Columns</digi:trn>
															</a>	
														</c:if>
														<c:if test="${aimAdvancedReportForm.currentTabName!='SelectCols'}">
															<a class="sub-nav" style="cursor:pointer;" title="${translation}"  onclick="return menuA(1)">
																2 :   <digi:trn key="aim:SelectColumns2">Select Columns</digi:trn>
															</a>	
														</c:if>												
													</logic:greaterEqual>
													<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="0">
														<c:if test="${aimAdvancedReportForm.currentTabName=='SelectCols'}">
															<a class="sub-navSelected" style="cursor:move;" title="${translation}"  onclick="return nope()">
																2 :   <digi:trn key="aim:SelectColumns2">Select Columns</digi:trn>
															</a>	
														</c:if>
														<c:if test="${aimAdvancedReportForm.currentTabName!='SelectCols'}">
															<a class="sub-nav" style="cursor:move;" title="${translation}"  onclick="return nope()">
																2 :   <digi:trn key="aim:SelectColumns2">Select Columns</digi:trn>
															</a>	
														</c:if>																									
													</logic:lessThan>
												</td>											
												<td noWrap align=left>
													<c:set var="translation">
														<digi:trn key="aim:clickToselectrows/hierarchies" >Click here to select rows/hierarchies</digi:trn>
													</c:set>													
													<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="1">
														<c:if test="${aimAdvancedReportForm.currentTabName=='SelectRows'}">
															<a class="sub-navSelected" style="cursor:pointer;" title="${translation}"  onclick="return menuA(2)">
																3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
															</a>	
														</c:if>
														<c:if test="${aimAdvancedReportForm.currentTabName!='SelectRows'}">
															<a class="sub-nav" style="cursor:pointer;" title="${translation}"  onclick="return menuA(2)">
																3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
															</a>	
														</c:if>													
													</logic:greaterEqual>
													<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="1">
														<c:if test="${aimAdvancedReportForm.currentTabName=='SelectRows'}">
															<a class="sub-navSelected" style="cursor:move;" title="${translation}"  onclick="return nope()">
																3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
															</a>	
														</c:if>
														<c:if test="${aimAdvancedReportForm.currentTabName!='SelectRows'}">
															<a class="sub-nav" style="cursor:move;" title="${translation}"  onclick="return nope()">
																3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
															</a>	
														</c:if>			
													</logic:lessThan>													
													
												</td>										
												<td noWrap align=left>
													<c:set var="translation">
														<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
													</c:set>
													
													<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="2">
														<c:if test="${aimAdvancedReportForm.currentTabName=='SelectMeasures'}">
															<a class="sub-navSelected" style="cursor:pointer;" title="${translation}"  onclick="return menuA(3)">
																4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
															</a>	
														</c:if>
														<c:if test="${aimAdvancedReportForm.currentTabName!='SelectMeasures'}">
															<a class="sub-nav" style="cursor:pointer;" title="${translation}"  onclick="return menuA(3)">
																4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
															</a>	
														</c:if>													
													</logic:greaterEqual>
													<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="2">
														<c:if test="${aimAdvancedReportForm.currentTabName=='SelectMeasures'}">
															<a class="sub-navSelected" style="cursor:move;" title="${translation}"  onclick="return nope()">
																4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
															</a>	
														</c:if>
														<c:if test="${aimAdvancedReportForm.currentTabName!='SelectMeasures'}">
															<a class="sub-nav" style="cursor:move;" title="${translation}"  onclick="return nope()">
																4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
															</a>	
														</c:if>			
													</logic:lessThan>
													
												</td>											
												
											</tr>
										</table>	
									</td>
								</tr>
								<TR>
									<td noWrap valign="top" align=left>
									 <table cellpadding="0" cellspacing="1" valign="top" align=left>	<tr>	
									 <td noWrap align=left> 
										<c:set var="translation">
											<digi:trn key="aim:clickToViewReportDetails">Click here to view Report Details</digi:trn>
										</c:set>
										<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="3">
											<c:if test="${aimAdvancedReportForm.currentTabName=='ReportDetails'}">
												<a class="sub-navSelected" style="cursor:pointer;" title="${translation}"  onclick="return menuA(4)">
													5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
												</a>	
											</c:if>
											<c:if test="${aimAdvancedReportForm.currentTabName!='ReportDetails'}">
												<a class="sub-nav" style="cursor:pointer;" title="${translation}"  onclick="return menuA(4)">
													5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
												</a>	
											</c:if>									
										</logic:greaterEqual>
										<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="3">
											<c:if test="${aimAdvancedReportForm.currentTabName=='ReportDetails'}">
												<a class="sub-navSelected" style="cursor:move;" title="${translation}"  onclick="return nope()">
													5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
												</a>	
											</c:if>
											<c:if test="${aimAdvancedReportForm.currentTabName!='ReportDetails'}">
												<a class="sub-nav" style="cursor:move;" title="${translation}"  onclick="return nope()">
													5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
												</a>	
											</c:if>									
										</logic:lessThan>
									</td>
									<c:if test="${aimAdvancedReportForm.reportEdit == true}">
										<td valign="top">
											<c:set var="translation">
												<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
											</c:set>
											<a class="sub-nav" style="cursor:pointer;" title="${translation}"  onclick="return saveEditedReport()">
														6 : <digi:trn key="aim:SaveReport">Save Report</digi:trn>
											</a>										
										</td>
									</c:if>
								</tr>	
									</table>
									</td>	
								</tr>
								



