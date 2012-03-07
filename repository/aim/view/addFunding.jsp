<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/gateperm" prefix="gateperm" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<%@page import="org.digijava.module.aim.helper.Constants"%>

<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>

<c:set var="baseCurrencyGS" value="<%= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY) %>" scope="request" />

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<jsp:include page="scripts/newCalendar.jsp"  />

<digi:instance property="aimEditActivityForm" />

<script language="JavaScript">

	// these functions are used in addfunding.js
	// 
	function fundingNotEntered() {
		alert("<digi:trn>Funding Id not entered</digi:trn>");
	}
	function noCommitmentsAdded() {
		return "<digi:trn>No commitments have been added. Continue?</digi:trn>";
	}
	//this map will store only the funding items that got changed - they require re-validation
	var forValidation = {};

	var errmsg1="<digi:trn key="aim:addFunding:errmsg:assitanceType">Type Of Assistance not selected</digi:trn>";
	var errmsg2="\n<digi:trn key="aim:addFunding:errmsg:fundOrgId">Funding Id not entered</digi:trn>";
	var errmsg3="\n<digi:trn key="aim:addFunding:errmsg:financeInstrument">Financing Instrument not selected</digi:trn>";
	var errmsg4="\n<digi:trn>Funding status not selected</digi:trn>";
    var msgEnterAmount="\n<digi:trn key="aim:addFunding:errmsg:enterAmount">Please enter the amount for the transaction</digi:trn>";
	var msgInvalidAmount="\n<digi:trn key="aim:addFunding:errmsg:invalidAmount">Invalid amount entered for the transaction</digi:trn>";
	var msgInvalidAmountProj="\n<digi:trn>Invalid amount entered for projection</digi:trn>";
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
//	var msgConfirmFunding="<digi:trn jsFriendly="true" key="aim:addFunding:errmsg:confirmFunding">All funding information should be entered in thousands '000'. Do you wish to proceed with your entry?</digi:trn>";
</gs:test>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
//	var msgConfirmFunding="";
</gs:test>
	var msgConfirmFunding="";
	//var msgConfirmFunding ="\n<digi:trn key="aim:addFunding:errmsg:enterDate">Please enter the transaction date for the transaction</digi:trn>";
	var msgEnterDate="\n<digi:trn key="aim:addFunding:errmsg:enterDate">Please enter the transaction date for the transaction</digi:trn>";
	var msgEnterRate="\n<digi:trn key="aim:addFunding:errmsg:invalidRate">Please enter a valid exchange rate, the decimal symbol is:</digi:trn>";
	//var msgEnterDate="qsfgqsg";


	function addForValidation(item) {
		if(item==null) return;
		item.value=trim(item.value);
		forValidation[item.name]=item;
	}
	
	<!--
	function useFixedRateClicked(field1,field2) {
		var fld1 = document.getElementById(field1);
		var fld2 = document.getElementById(field2);
		
		if (fld1.disabled == true) {
			fld1.disabled = false;
			fld2.value=true;
		} else {
			fld1.disabled = true;
			fld2.value=false;
		}
	}
	function trim(s) {
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  }

	function checkCurrency(element){
		var prefix = element.substring(0,element.indexOf(".")+1);
		var textbox = prefix+"fixedExchangeRate";
		var select = prefix+"currencyCode";
		var selObj = document.getElementsByName(select);
		var textboxobj = document.getElementsByName(textbox);
		var selIndex = selObj[0].selectedIndex;
		var index = element.substring(element.indexOf("[")+1,element.indexOf("[")+2);
		//alert (index);
		var check = document.getElementById("fixedcheck"+index);
		if (selObj[0].options[selIndex].value == '${baseCurrencyGS}'){
			 check.disabled =true;
			 textboxobj.disabled =true;
		}else {
			 check.disabled =false;
			 textboxobj.disabled =false;
			}
		}

	function getCommitmentRows(){
		var j=0;
		var adjLabel="fundingDetail["+j+"].adjustmentType";
		while(document.getElementsByName(adjLabel)[0]!=null){
			j++;
			adjLabel="fundingDetail["+j+"].adjustmentType"
		}
		return j;
	}
	
	function validateAmounts(){
    	var totalComms	= 0;
    	var totalDisbs	= 0;
    	var totalExpen	= 0;
    	var rows = getCommitmentRows();
    	for(i=0; rows!=0 && i<rows; i++){
    		var adjLabel="fundingDetail["+i+"].adjustmentType";
			var transAmountLabel="fundingDetail["+i+"].transactionAmount";
			var transTypeLabel="fundingDetail["+i+"].transactionType";
			var amount = 0;

			var strAmount=document.getElementsByName(transAmountLabel)[0].value;
			var decimalSeparator="<%=FormatHelper.getDecimalSymbol()%>";
			var groupSeparator="<%=FormatHelper.getGroupSymbol()%>";
			strAmount=strAmount.replace(groupSeparator,"");
			strAmount=strAmount.replace(decimalSeparator,".");
			amount=parseFloat(strAmount);
			if ((document.getElementsByName(transTypeLabel)[0].value == 0) && (document.getElementsByName(adjLabel)[0].value == 1)) {
				totalComms	+= amount;
			} else {
				if ((document.getElementsByName(transTypeLabel)[0].value == 1) && (document.getElementsByName(adjLabel)[0].value == 1)) {
					totalDisbs	+= amount;
				} else {
					if ((document.getElementsByName(transTypeLabel)[0].value == 2) && (document.getElementsByName(adjLabel)[0].value == 1)) {
						totalExpen	+= amount;
					}
				}
			}
		}

    	
	<%
		String value=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_DISBURSMENT_BIGGER_COMMITMENTS);
		if (new Boolean(value)){%>
		if (totalDisbs > totalComms) {
			var Warn="<digi:trn key="aim:addFunding:warn:disbSupCom">Sum of Disbursments is bigger than sum of commitments. Do you want to save the funding ?</digi:trn>";
			if(!confirm(Warn)) {	
					return false;	
				}
		}
	<%}%>

	<%
	value=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_EXPENDITURE_BIGGER_DISBURSMENT);
	if (new Boolean(value)){%>
	if (totalExpen > totalDisbs) {
		var Warn="<digi:trn key="aim:addFunding:warn:expenSupDisb">Sum of Expenditures is bigger than sum of Disbursments. Do you want to save the funding ?</digi:trn>";
		if(!confirm(Warn)) {	
				return false;	
			}
		}
	<%}%>
		
		
		<field:display name="Capital Spending" feature="Funding Information">
			var percenageVal = document.getElementsByName("funding.capitalSpendingPercentage")[0].value;
			var CapSpendingErr = "<digi:trn key="aim:addFunding:error:incorrectCapSpendingPercent">Incorrect capital spending percentage (needs to be in 0-100 range)</digi:trn>";
			var intPercentageVal = parseFloat(percenageVal);
			if (intPercentageVal == null || isNaN(intPercentageVal) || intPercentageVal < 0 || intPercentageVal > 100) {
				alert (CapSpendingErr);
				return false;	
			}
		</field:display>


		return true;
		
   	}

	var isAlreadySubmitted = false;

	function addFunding() {
		if(!isAlreadySubmitted)
		{

		var flag = validateFundingTrn(errmsg1,errmsg2,errmsg3,errmsg4,msgEnterAmount,msgInvalidAmount,msgEnterDate, msgEnterRate,"<%=FormatHelper.getDecimalSymbol()%>","<%=FormatHelper.getGroupSymbol()%>",msgConfirmFunding);
		var flagProj	= validateProjection(msgInvalidAmountProj);

		//var exchangeRateValid=validateFundingExchangeRate();
		
		if ( !flag || !flagProj) return false;
		<digi:context name="fundAdded" property="context/module/moduleinstance/fundingAdded.do?edit=true" />;
		document.aimEditActivityForm.action = "<%= fundAdded %>";
		document.aimEditActivityForm.target = "_self";

		if ( !validateAmounts() ) {
			return false;
		}
		
		isAlreadySubmitted = true;
	  	document.aimEditActivityForm.submit();
			//validateFormatUsingSymbos();
		}
	  	
		return true;
	}


	function addFundingDetail(type) {

		//var flag = validateFundingExchangeRate();
		var numComm = document.aimEditActivityForm.numComm.value;
		var numDisb = document.aimEditActivityForm.numDisb.value;
		var numExp = document.aimEditActivityForm.numExp.value;
		
		var flag2 = validateFundingDetails(numComm,numDisb,numExp,msgEnterAmount,msgInvalidAmount,msgEnterDate, msgEnterRate, msgConfirmFunding,"<%=FormatHelper.getDecimalSymbol()%>","<%=FormatHelper.getGroupSymbol()%>");
		var flagProj	= validateProjection(msgInvalidAmountProj);

		if ( !flagProj || !flag2) return false;
		if (type == 0) {
			
			document.getElementsByName("funding.event")[0].value = "addCommitments";
		} else if (type == 1) {
			document.getElementsByName("funding.event")[0].value = "addDisbursements";
		} else if (type == 2) {
			document.getElementsByName("funding.event")[0].value = "addExpenditures";
		}
                else if (type == 4) {
			document.getElementsByName("funding.event")[0].value = "addDisbursementOrders";
		}
                document.aimEditActivityForm.target = "_self";
	 	document.aimEditActivityForm.action="/addFundingDetail.do";
	 	document.aimEditActivityForm.submit();
	}

        function addDisbOrderToDisb(indexId) {
          openNewWindow(400, 100,"addDisbOrderToDisb");
          document.aimEditActivityForm.action= "/addDisbOrderToDisb.do";
          document.getElementById('transIndexId').value = indexId;
          document.aimEditActivityForm.target =  popupPointer.name;
          document.aimEditActivityForm.submit();

	}
        function   addDisbOrderToContract (indexId) {
          openNewWindowWithName(400, 100,"addDisbOrderToContract");
          document.aimEditActivityForm.action= "/addDisbOrderToContract.do";
          document.getElementById('transIndexId').value = indexId;
          document.aimEditActivityForm.target =  popupPointer.name;
          document.aimEditActivityForm.submit();
            
        }


	function addMTEFProjection() {

	//	var flag = validateFundingExchangeRate();
	//	if (flag == false) return false;

	document.getElementsByName("funding.event")[0].value = "addProjections";
 	document.aimEditActivityForm.action="/addMTEFProjection.do";
 	document.aimEditActivityForm.submit();
	}



	function removeFundingDetail(index,type) {
		<c:set var="translation">
			<digi:trn key="aim:areYouSureRemoveTransaction">Are you sure you want to remove the selected transaction ?</digi:trn>
		</c:set>
		var flag = confirm("${translation}");
		if(flag != false) {
			if (type == 0) {
				document.getElementsByName("funding.event")[0].value = "delCommitments";
			} else if (type == 1) {
				document.getElementsByName("funding.event")[0].value = "delDisbursements";
			} else if (type == 2) {
				document.getElementsByName("funding.event")[0].value = "delExpenditures";
			}
                         else if (type == 4) {
				document.getElementsByName("funding.event")[0].value = "delDisbursementOrders";
			}
			document.getElementById('transIndexId').value=index;
		 	document.aimEditActivityForm.action="/addFundingDetail.do";
			document.aimEditActivityForm.submit();
		}
	}

	function removeMTEFProjection(index) 
	{
		var flag = confirm("<digi:trn key="aim:addFunding:warn:removeproj">Are you sure you want to remove the selected projection ?</digi:trn>");
		if(flag != false) {
			document.getElementsByName("funding.event")[0].value = "delProjection";
			document.getElementById('transIndexId').value=index;
		 	document.aimEditActivityForm.action="/addMTEFProjection.do";
			document.aimEditActivityForm.submit();
		}
	}

	function isTotDisbIsBiggerThanTotCom() {
		var Warn="<digi:trn key="aim:addFunding:warn:disbSupCom">The Sum of Actual Disbursement is greater than the Sum of actual commitments.</digi:trn>";
		if(document.getElementById("totDisbIsBiggerThanTotCom").value == "true") {
			alert(Warn)
			return true;					
		} else {
			return false;
		}
	}
	
	function load()
	{
		//add the last row for forced validation (this ensures we validate new rows )
		if( ${fn:length(aimEditActivityForm.funding.fundingDetails)} >0) {
			addForValidation(document.getElementsByName('fundingDetail[${fn:length(aimEditActivityForm.funding.fundingDetails)-1}].transactionAmount')[0]);
			addForValidation(document.getElementsByName('fundingDetail[${fn:length(aimEditActivityForm.funding.fundingDetails)-1}].transactionDate')[0]);
			addForValidation(document.getElementsByName('fundingDetail[${fn:length(aimEditActivityForm.funding.fundingDetails)-1}].fixedExchangeRate')[0]);
		}
		if (!isTotDisbIsBiggerThanTotCom()) {
			var Warn="<digi:trn key="aim:addFunding:warn:dup">This information is a duplicate of existing funding information. Do you wish to proceed?</digi:trn>";
			if (document.getElementById("dupFunding").value == "false")
			{
					<digi:context name="addAct" property="context/module/moduleinstance/addActivity.do?edit=true"/>
					document.aimEditActivityForm.action = "<%=addAct%>";
					document.aimEditActivityForm.target = window.opener.name;
					document.aimEditActivityForm.submit();
					window.close();
			}		
			if(document.getElementById("dupFunding").value == "true")
			{
				if(document.getElementsByName("funding.firstSubmit")[0].value == "true")
				{
					if(confirm(Warn))
					{
						<digi:context name="addAct" property="context/module/moduleinstance/addActivity.do?edit=true"/>
						document.aimEditActivityForm.action = "<%=addAct%>";
						document.aimEditActivityForm.target = window.opener.name;
						document.aimEditActivityForm.submit();
						window.close();
					}
				}
			}
		}
	}
	
	function unload() {
	}

	function closeWindow() {
		window.close();
	}


	-->
</script>

<%! long t = System.currentTimeMillis(); %>

<body onLoad="load()">

<c:set var="formatTip">
	<digi:trn key="aim:decimalforma">Format has to be: </digi:trn> <%=FormatHelper.formatNumber(FormatHelper.parseDouble("100000"+FormatHelper.getDecimalSymbol()+"150"))%>
</c:set>


<digi:form action="/addFundingDetail.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden name="aimEditActivityForm" styleId="dupFunding"  property="funding.dupFunding"/>
<html:hidden name="aimEditActivityForm" styleId="event" property="funding.event"/>
<html:hidden name="aimEditActivityForm" styleId="transIndexId" property="funding.transIndexId"/>
<html:hidden name="aimEditActivityForm" styleId="numComm" property="funding.numComm" />
<html:hidden name="aimEditActivityForm" styleId="numDisb" property="funding.numDisb"/>
<html:hidden name="aimEditActivityForm" styleId="numExp" property="funding.numExp"/>
<html:hidden name="aimEditActivityForm" styleId="numDisbOrder" property="funding.numDisbOrder"/>
<html:hidden name="aimEditActivityForm" styleId="numProjections" property="funding.numProjections"/>
<html:hidden name="aimEditActivityForm" property="editAct"/>
<html:hidden name="aimEditActivityForm" property="funding.firstSubmit"/>
<html:hidden name="aimEditActivityForm" styleId="totDisbIsBiggerThanTotCom" property="totDisbIsBiggerThanTotCom"/>


<input type="hidden" name="funding.isEditFunfing" value="true"/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
	<!-- funding -->
	<tr>
    	<td vAlign="top">

			<table width="99%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellPadding="1" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
						<digi:trn key="aim:FundingItemEditor">Funding Item Editor</digi:trn>
					</td>
				</tr>
				<tr>
					<td vAlign="top" align="center" width="100%">
						<table cellpadding=4 cellspacing="1" bgcolor="#ffffff" width="100%">
							<tr>
								<td align="right" bgcolor="#ECF3FD" width="50%">
			                	<b><digi:trn key="aim:organization">Organization</digi:trn></b>
								</td>
								<td align="left" bgcolor="#ECF3FD" width="50%">
			                	<bean:write name="aimEditActivityForm" property="funding.orgName"/>
								</td>
							</tr>
							<tr>
								<td align="right" bgcolor="#ECF3FD">
			                	<FONT color=red>*</FONT><b>
									<a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
									<digi:trn key="aim:typeOfAssistance">Type of Assistance</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
									<c:set var="translation">
										<digi:trn key="aim:addActivityTypeOfAssistenceFirstLine">Please select from below</digi:trn>
									</c:set>
									
									<bean:define id="contentDisabled">false</bean:define>
									<c:set var="contentDisabled"><field:display name="Type Of Assistance" feature="Funding Information">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
									<c:if test="${contentDisabled=='false'}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.assistanceType"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text"/>
									</c:if>
									
									<c:if test="${contentDisabled=='true'}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.assistanceType"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text" outerdisabled="true"/>
									</c:if>
									
								</td>
							</tr>
							<tr>
								<td align="right" bgcolor="#ECF3FD">
			                	<%-- FONT color=red>*</FONT--%><b>
										<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">
										<digi:trn key="aim:fundingOrgId">
										Funding Organization Id</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
								
								<c:set var="contentDisabled"><field:display name="Funding Organization Id" feature="Funding Information">false</field:display></c:set>
								<c:if test="${contentDisabled==''}">
									<c:set var="contentDisabled">true</c:set>
								</c:if>
								
										<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation.
										This item may be useful when one project has two or more different financial instruments.
										If the project has a unique financial operation, the ID can be the same as the project ID
										</digi:trn>">
			   	             		<html:text  styleId="orgFundingId" property="funding.orgFundingId" size="10" disabled="${contentDisabled}"/>  </a>
											   	             	

								</td>
							</tr>
							<tr>
								<td align="right" bgcolor="#ECF3FD">
								<FONT color=red>*</FONT><b>
									<a title="<digi:trn key="aim:FinanceInstrument">Method by which aid is delivered to an activity</digi:trn>">
									<b><digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn></b></a>
								</td>
								<td align="left" bgcolor="#ECF3FD">
								
								<c:set var="contentDisabled"><field:display name="Financing Instrument" feature="Funding Information">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
									<c:if test="${contentDisabled=='false'}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="funding.modality" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text" />
									</c:if>
									
									<c:if test="${contentDisabled=='true'}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="funding.modality" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text"  outerdisabled="true"/>
									</c:if>
									
								</td>
							</tr>
							
							<field:display name="Mode of Payment" feature="Funding Information">
							<tr>
								<td align="right" bgcolor="#ECF3FD">
									<b>
									<a title="<digi:trn>The mode of payment</digi:trn>">
									<digi:trn>Mode Of Payment</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
									<c:set var="translation">
										<digi:trn>Please select from below</digi:trn>
									</c:set>
									
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.modeOfPayment"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.MODE_OF_PAYMENT_KEY %>" styleClass="inp-text"/>
									
								</td>
							</tr>
							</field:display>
							
							<field:display name="Capital Spending" feature="Funding Information">
							<tr>
								<td align="right" bgcolor="#ECF3FD">
									<b>
									<a title="<digi:trn>Capital Spending Percentage</digi:trn>">
									<digi:trn>Capital Spending Percentage</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
									<html:text size="5" name="aimEditActivityForm"   property="funding.capitalSpendingPercentage"/>%
								</td>
							</tr>
							</field:display>

							<field:display name="Funding Status" feature="Funding Information">
							<tr>
								<td align="right" bgcolor="#ECF3FD">
			                	<FONT color=red>*</FONT><b>
									<a title="<digi:trn>The status of the funding</digi:trn>">
									<digi:trn>Funding Status</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
									<c:set var="translation">
										<digi:trn>Please select from below</digi:trn>
									</c:set>
									
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.fundingStatus"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FUNDING_STATUS_KEY %>" styleClass="inp-text"/>
									
								</td>
							</tr>
							</field:display>

							<field:display name="Funding start date" feature="Funding Information">
							<tr>
								<td align="right" bgcolor="#ECF3FD">
			                	<b>
									<a title="<digi:trn>Funding start date</digi:trn>">
									<digi:trn>Funding start date</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
									<html:text name="aimEditActivityForm" readonly="true" property="funding.actualStartDate" styleId="actualStartDate"  size="10"  />
									<a id="actualStartDate1" href='javascript:pickDateById("actualStartDate1", "actualStartDate")'>
										<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
									</a>
								</td>
							</tr>
							</field:display>

							<field:display name="Funding end date" feature="Funding Information">
							<tr>
								<td align="right" bgcolor="#ECF3FD">
			                	<b>
									<a title="<digi:trn>Funding end date</digi:trn>">
									<digi:trn>Funding end date</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
									<html:text name="aimEditActivityForm" readonly="true" property="funding.actualCompletionDate" styleId="actualCompletionDate"  size="10"  />
									<a id="actualCompletionDate1" href='javascript:pickDateById("actualCompletionDate1", "actualCompletionDate")'>
										<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
									</a>
								</td>
							</tr>
							</field:display>
							<tr>
							
						</table>
					</td>
				</tr>
			</table>

			</td></tr>
			</table>

		</td>
	</tr>
	<tr><td>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
		<FONT color="blue"><B><BIG>*</BIG></B></FONT>
			<digi:trn key="aim:pleaseEnterTheAmountInThousands">
			Please enter amount in thousands (000)</digi:trn>
</gs:test>
	</td></tr>
	<% int tempIndex = 0; %>
	<% String tempIndexStr = ""; %>
	
	<c:set var="translation">
		<digi:trn key="aim:currencieswithexchange">Only currencies having exchange rate are listed here</digi:trn>
	</c:set>
	<feature:display module="Funding" name="MTEF Projections">
	<field:display feature="MTEF Projections" name="MTEFProjections">
	<tr>
    	<td vAlign="top">

			<table width="99%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellPadding="1" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center" colspan="5">
						<digi:trn key="aim:MTEF Projection">MTEF Projection</digi:trn>
					</td>
				</tr>
				<tr bgcolor="#003366" class="textalb" align="center">
					<field:display name="Projection Name" feature="MTEF Projections">
						<td align="center"><b><font color="white">						
							<digi:trn key="aim:Projected">Projected</digi:trn>/<br><digi:trn key="aim:Pipeline">Pipeline</digi:trn></font></b>						
						</td>
					</field:display>
					<field:display name="Projection Amount" feature="MTEF Projections">
					<td align="center"><b><font color="white"><digi:trn key="aim:amount">Amount</digi:trn></font></b>
					</td>
					</field:display>
					<field:display name="Projection Currency Code" feature="MTEF Projections">
					<td align="center"><b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b>
					<img src= "../ampTemplate/images/help.gif" border="0" align="absmiddle" title="${translation}" />
					</td>
					</field:display>
					<field:display name="Projection Date" feature	="MTEF Projections">
					<td align="center"><b><font color="white"><digi:trn key="aim:ProjectionDate">Projection Date</digi:trn></font></b>
					</td>
					</field:display>
					<td><b><font color="white">&nbsp;</font></b></td>
				</tr>


				<c:if test="${ !empty aimEditActivityForm.funding.fundingMTEFProjections}">
				<c:set var="indexMTEF" value="-1"/>
				<c:forEach var="mtefProjection" items="${aimEditActivityForm.funding.fundingMTEFProjections}">

				 	<tr>
				 	
				 		<c:set var="contentDisabled"><field:display name="Projection Name" feature="MTEF Projections">false</field:display></c:set>
						<c:if test="${contentDisabled==''}">
							<c:set var="contentDisabled">true</c:set>
						</c:if>
						
						<td valign="bottom" align="center">
						<c:set var="indexMTEF" value="${indexMTEF+1}"/>
						<html:select indexed="true" name="mtefProjection" property="projected" disabled="${contentDisabled}" styleClass="inp-text">
							<logic:iterate name="aimEditActivityForm" property="funding.projections" id="projection" type="org.digijava.module.categorymanager.dbentity.AmpCategoryValue">
							<html:option value="${projection.id}" >
								<digi:trn key="<%= org.digijava.module.categorymanager.util.CategoryManagerUtil.getTranslationKeyForCategoryValue(projection) %>">
									<bean:write name="projection" property="value"/>
								</digi:trn>
							</html:option>
							</logic:iterate>
						</html:select>
						</td>
						
				 		<c:set var="contentDisabled"><field:display name="Projection Amount" feature="MTEF Projections">false</field:display></c:set>
						<c:if test="${contentDisabled==''}">
							<c:set var="contentDisabled">true</c:set>
						</c:if>
						<td valign="bottom" align="center">
							<html:text title="${tip}" name="mtefProjection" indexed="true" property="amount" size="17" onchange="this.value=trim(this.value);" styleClass="amt" disabled="${contentDisabled}"/>
						</td>
							
				 		<c:set var="contentDisabled"><field:display name="Projection Currency Code" feature="MTEF Projections">false</field:display></c:set>
						<c:if test="${contentDisabled==''}">
							<c:set var="contentDisabled">true</c:set>
						</c:if>
						<td valign="bottom" align="center">
							<html:select name="mtefProjection" indexed="true" property="currencyCode" styleClass="inp-text" disabled="${contentDisabled}" onchange="checkCurrency(this.name);">
								<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode"
													label="currencyName"/>
							</html:select>
						</td>
						
				 		<c:set var="contentDisabled"><field:display name="Projection Date" feature	="MTEF Projections">false</field:display></c:set>
						<c:if test="${contentDisabled==''}">
							<c:set var="contentDisabled">true</c:set>
						</c:if>
						<td valign="bottom" align="center">
							<table cellPadding="0" cellSpacing="0">
							<tr>
								<td>
									<html:hidden name="mtefProjection" indexed="true" property="projectionDate"/>
									<html:text name="mtefProjection" indexed="true" property="projectionDateLabel" readonly="true" size="10" disabled="${contentDisabled}"/>
								</td>
							</tr>
							</table>
						</td>

											<td>&nbsp;
												<field:display name="Remove MTEF Projection" feature	="MTEF Projections">
													<a href="javascript:removeMTEFProjection(${indexMTEF})">
													 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this projection"/>
													</a>
												</field:display>
											</td>
										</tr>

				</c:forEach>
				</c:if>

				<c:set var="contentDisabled"><field:display name="Add Projection" feature	="MTEF Projections">false</field:display></c:set>
				<c:if test="${contentDisabled==''}">
					<c:set var="contentDisabled">true</c:set>
				</c:if>

				
				<tr bgcolor="#ffffff">
					<td align="center" colspan="5">
						<table cellPadding="3">
							<tr>
								<td>
									<html:select property="funding.selectedMTEFProjectionYear" name="aimEditActivityForm" disabled="${contentDisabled}" styleClass="inp-text">
										<html:optionsCollection name="aimEditActivityForm" property="funding.availableMTEFProjectionYears" label="value" value="key"/>
									</html:select>
									<c:if test="${contentDisabled=='true'}">
										<input class="dr-menu" type="button" disabled="disabled" name="addMTEFProj" value="<digi:trn key='aim:addMTEFProjection'>Add Projection</digi:trn>" onClick="addMTEFProjection()">
									</c:if>
									<c:if test="${contentDisabled=='false'}">
										<input class="dr-menu" type="button" name="addMTEFProj" value="<digi:trn key='aim:addMTEFProjection'>Add Projection</digi:trn>" onClick="addMTEFProjection()">
									</c:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>

				</table>
				</td>
				</tr>
				</table>
				</td>
				</tr>
	</field:display>
	</feature:display>

	<!-- commitments -->
<feature:display module="Funding" name="Commitments">
	<tr>
		<td vAlign="top">

			<table width="99%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
			<tr><td>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
					<a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient 	</digi:trn>"><digi:trn key="aim:commitments">Commitments</digi:trn></a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="2" class="box-border-nopadding">
							<tr bgcolor="#003366" class="textalb">
								<field:display name="Adjustment Type Commitment" feature="Commitments">
								<td align="center" valign="middle">
									<b><font color="white"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn>/<br><digi:trn key="aim:Actual">Actual</digi:trn>/<digi:trn>Pipeline</digi:trn></font></b>
								</td>
								</field:display>
								<field:display name="Amount Commitment" feature="Commitments">
								<td align="center" valign="middle" >
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommittedNoThousand">Full amount of expected transfer, irrespective of the time required for the completion of disbursements.</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
								</td>
								</field:display>
								<field:display name="Currency Commitment" feature="Commitments">
								<td align="center" valign="middle" >
								<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">											  <b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b></a>
								<img src= "../ampTemplate/images/help.gif" border="0" align="absmiddle" title="${translation}" /></td>
								<td align="center" valign="middle" >
									<a title="<digi:trn key="aim:CommitmentDate">The date (day, month, year) when funding commitment was signed</digi:trn>">
									<b><font color="white"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn>/<digi:trn>Pipeline</digi:trn><br><digi:trn key="aim:CommitmentDateFIE">Commitment Date</digi:trn></font></b></a>
								</td>
								</field:display>
								<field:display name="Related Pledge" feature="Commitments">
									<td align="center" valign="middle" >
										<b><font color="white"><digi:trn>Pledges</digi:trn></font></b>
									</td>
								</field:display>
								<td align="center" valign="middle" width="*">&nbsp;</td>
							</tr>

							<c:if test="${ !empty aimEditActivityForm.funding.fundingDetails}">
							<c:set var="index" value="-1"/>
						    <c:forEach var="fundingDetail" items="${aimEditActivityForm.funding.fundingDetails}" varStatus="status">
						 	<c:if test="${fundingDetail.transactionType==0}">
									 	<tr>
									 		<c:set var="contentDisabled"><field:display name="Adjustment Type Commitment" feature="Commitments">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
												<td valign="bottom" align="center">
													<c:set var="index" value="${index+1}"/>
													<c:if test="${aimEditActivityForm.identification.statusId==1}">
														<html:select name="fundingDetail" indexed="true" property="adjustmentType"  disabled="${contentDisabled}" styleClass="inp-text">
															<html:option value="0"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn></html:option>
														</html:select>
	                                                </c:if>	
												<c:if test="${aimEditActivityForm.identification.statusId!=1}">
													<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text"  disabled="${contentDisabled}">
														<html:option value="<%=new Integer(Constants.ACTUAL).toString()%>"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
														<html:option value="<%=new Integer(Constants.PLANNED).toString()%>"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn></html:option>
														<field:display name="Pipeline" feature="Commitments">
															<html:option value="<%=new Integer(Constants.ADJUSTMENT_TYPE_PIPELINE).toString()%>"><digi:trn>Pipeline</digi:trn></html:option>
														</field:display>
													</html:select>
												</c:if>
													<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
												</td>
									 		<c:set var="contentDisabled"><field:display name="Amount Commitment" feature="Commitments">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
												<td valign="bottom" align="center">
														<html:text name="fundingDetail" title="${formatTip}"  disabled="${contentDisabled}" indexed="true" property="transactionAmount" onchange="addForValidation(this)" onclick="checkCurrency(this.name);" size="17"/>
												</td>
									 		<c:set var="contentDisabled"><field:display name="Currency Commitment" feature="Commitments">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="bottom" align="center">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" disabled="${contentDisabled}" onchange="checkCurrency(this.name);" onfocus="checkCurrency(this.name);" styleClass="inp-text">
													<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode"
													label="currencyName"/>
												</html:select>		
											</td>
											<c:set var="contentDisabled">
											<field:display name="Date Commitment" feature="Commitments">false</field:display></c:set>
												<c:if test="${contentDisabled==''}">
													<c:set var="contentDisabled">true</c:set>
												</c:if>
												<td align="center" vAlign="bottom">
													<table cellPadding="0" cellSpacing="0">
														<tr>
															<td>
																<% tempIndexStr = "" + tempIndex; tempIndex++;%>
																<html:text name="fundingDetail" readonly="true" indexed="true" property="transactionDate"
																styleId="<%=tempIndexStr%>"  size="10" onchange="addForValidation(this)"/>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<c:if test="${contentDisabled=='false'}">
																	<a id="trans3Date<%=tempIndexStr%>" href='javascript:pickDateById("trans3Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																	</a>
																</c:if>
															</td>														
														</tr>
													</table>												
												</td>
											<field:display name="Related Pledge" feature="Commitments">
												<td valign="bottom" align="center">
													<html:select name="fundingDetail" indexed="true" property="pledge" styleClass="inp-text">
														<html:option value="0"><digi:trn>Select Pledge</digi:trn> </html:option>
														<html:optionsCollection name="aimEditActivityForm" property="funding.pledgeslist" value="id" label="title"/>
												</html:select>		
												</td> 
											</field:display>
											
											<field:display name="Remove Commitment Link" feature="Commitments">
												<td align="right">
														<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,0)">
											 				<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
														</a>
												</td>
											</field:display>
											</tr>	
										<tr bgcolor="#ecf3fd">
											<td>&nbsp;</td>
											<td align="left">&nbsp;</td>
											<td colspan="5"><b><digi:trn key="aim:fixedRate">Fixed Exchange Rate</digi:trn> - <digi:trn>compared to</digi:trn><digi:trn><gs:value name="<%=GlobalSettingsConstants.BASE_CURRENCY %>" /> </digi:trn></b></td>
										</tr>
										<tr><td>&nbsp;</td>
										<td align="right">
								<c:set var="contentDisabled"><field:display name="Exchange Rate" feature="Funding Information">false</field:display></c:set>
								<c:if test="${contentDisabled==''}">
									<c:set var="contentDisabled">true</c:set>
								</c:if>
											
									<% String exchRatefldId = "exchFld"+(t++);
										String exchCurrfldId = "exchCurr"+(t++);
										String exchHidden = "useFixedRate"+(t++);
										String jsUrl = "useFixedRateClicked('" + exchRatefldId + "','" + exchHidden + "')";
										
									%>	
									
												<html:hidden  styleId="<%=exchHidden%>" name="fundingDetail"  property="useFixedRate" indexed="true" />
														<input type="checkbox" id="fixedcheck${status.index}" onclick="<%=jsUrl%>" 
														<c:if test="${contentDisabled}">
															disabled 
														</c:if>
														<c:if test="${fundingDetail.useFixedRate}">
															checked="true"
														</c:if>
														/>
											</td>
											<td colspan="5">
												<logic:equal name="fundingDetail" property="useFixedRate" value="true">
													<html:text name="fundingDetail" indexed="true" property="fixedExchangeRate" onchange="addForValidation(this)" styleClass="amt" disabled="false" styleId="<%=exchRatefldId%>"/>
												</logic:equal>
												<logic:equal name="fundingDetail" property="useFixedRate" value="false">
													<html:text name="fundingDetail" indexed="true" property="fixedExchangeRate" onchange="addForValidation(this)" styleClass="amt" disabled="true" styleId="<%=exchRatefldId%>"/>
												</logic:equal>
												<script type="text/javascript">
													<!-- check for when editing the page -->
													checkCurrency("fundingDetail[${status.index}].currencyCode");												
												</script>											
											</td>
										</tr>
									</c:if>
						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td align="center">
						<table cellPadding="3">
							<tr>
								<%--
								<td>
									<input class="dr-menu" type="button" name="xx1" value="Delete Commitments"
									onclick="this.form.event.value='delCommitments';addFundingDetail();">
								</td> --%>
								
								<c:set var="contentDisabled"><field:display name="Add Commitment Button" feature="Commitments">false</field:display></c:set>
								<c:if test="${contentDisabled==''}">
									<c:set var="contentDisabled">true</c:set>
								</c:if>
								<td>
									<c:if test="${contentDisabled=='false'}">
										<input class="dr-menu" type="button" name="xx1" value="<digi:trn key='aim:addCommitment'>Add Commitment</digi:trn>" onClick="addFundingDetail(0)">
									</c:if>
									<c:if test="${contentDisabled=='true'}">
										<input class="dr-menu" type="button" disabled="disabled" name="xx1" value="<digi:trn key='aim:addCommitment'>Add Commitment</digi:trn>" onClick="addFundingDetail(0)">
									</c:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			</td></tr>
			</table>
		</td>
	</tr>
</feature:display>



        <!-- disbursement orders -->
        <feature:display module="Funding" name="Disbursement Orders">
	<tr>
		<td vAlign="top">

			<table width="99%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
			<tr><td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td bgcolor="#006699" class="textalb" height="20" align="center">
					<a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>"><digi:trn key="DisbursementOrdes">Disbursement Orders</digi:trn></a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="3" class="box-border-nopadding">
							<tr bgcolor="#003366" class="textalb">
								<field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders">
								<td align="center" valign="middle" >
									<b><font color="white"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn>/<br><digi:trn key="aim:Actual">Actual</digi:trn></font></b>
								</td>
								</field:display>
								<field:display name="Amount of Disbursement Order" feature="Disbursement Orders">
								<td align="center" valign="middle" >
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommittedNoThousand">Full amount of expected transfer, irrespective of the time required for the completion of disbursements.</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
								</td>
								</field:display>
								<field:display name="Currency of Disbursement Order" feature="Disbursement Orders">
								<td align="center" valign="middle" >
									<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">
									<b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b></a>
									<img src= "../ampTemplate/images/help.gif" border="0" align="absmiddle" title="${translation}" />
								</td>
								</field:display>
								<field:display name="Date of Disbursement Order" feature="Disbursement Orders">
								<td align="center" valign="middle" style="width: 15%;">
								<a title="<digi:trn key="aim:DateofDisbursementOrder">Date of actual international transfer of financial resources</digi:trn>">
								<b><font color="white">
                                  <digi:trn key="aim:DisbursementOrderDateFIE">Planned/Actual Disbursement Order Date</digi:trn></font></b></a>
								</td>
								</field:display>
                                <td align="center" valign="middle" style="width: 15%;">
								<feature:display module="Funding" name="Disbursement Orders">
									<b><font color="white"><digi:trn key="aim:DisbursementOrderIDFIE">Disbursement Order ID</digi:trn></font></b>
								</feature:display>
									
								</td>
								<field:display name="Contract of Disbursement Order" feature="Disbursement Orders">
                                <td align="center" valign="middle" colspan="1" style="width: 15%;">
									<b><font color="white"><digi:trn key="aim:DisbursementOrderContractId">Contract ID</digi:trn></font></b>
								</td>
								</field:display>
								<field:display name="Rejected Disbursement Order" feature="Disbursement Orders">
                                <td align="center" valign="middle">
									<b><font color="white"><digi:trn key="aim:DisbursementOrderRejected">Rejected</digi:trn></font></b>
								</td>
								</field:display>
								<td align="center" valign="middle">*&nbsp;</td>
							</tr>
							<c:if test="${ !empty aimEditActivityForm.funding.fundingDetails}">
							<c:forEach var="fundingDetail" items="${aimEditActivityForm.funding.fundingDetails}">
						 	<c:if test="${fundingDetail.transactionType==4}">


										<tr>
											<c:set var="contentDisabled"><field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
												<td valign="center" align="center">
													<c:set var="index" value="${index+1}"/>
													<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="${contentDisabled}" styleClass="inp-text">
														<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													</html:select>
													<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
												</td>

											<c:set var="contentDisabled"><field:display name="Amount of Disbursement Order" feature="Disbursement Orders">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="center" align="center">
												<html:text name="fundingDetail" disabled="${contentDisabled}" title="${formatTip}"  indexed="true" property="transactionAmount" onchange="addForValidation(this)" size="14" />
											</td>

											<c:set var="contentDisabled"><field:display name="Currency of Disbursement Order" feature="Disbursement Orders">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="center" align="center">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" disabled="${contentDisabled}" onchange="checkCurrency(this.name);" styleClass="inp-text">
													<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
											</td>

											<c:set var="contentDisabled"><field:display name="Date of Disbursement Order" feature="Disbursement Orders">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="center" align="center">
												<table cellpadding="0" cellspacing="0">
													<tr>

														<td>
															<% tempIndexStr = "" + tempIndex; tempIndex++;%>
															<html:text name="fundingDetail" readonly="true" indexed="true" property="transactionDate" 
															styleId="<%=tempIndexStr%>" size="10"  onchange="addForValidation(this)"/>
														</td>
														<td align="left" vAlign="center">&nbsp;
															<c:if test="${contentDisabled=='false'}">
																<a id="trans4Date<%=tempIndexStr%>" href='javascript:pickDateById("trans4Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</c:if>
														</td>

													</tr>
												</table>
											</td>

                                            
											<c:set var="contentDisabled"><field:display name="Disbursement Order Number" feature="Disbursement Orders">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
                                            
                                             <td valign="center" align="center">
												<html:text name="fundingDetail" property="disbOrderId" readonly="true" disabled="${contentDisabled}" size="10"/>
											</td>
                                            
                                            <field:display name="Disbursement Order Contract ID" feature="Disbursement Orders">
											
                                            
                                             <td align="center">
                                                 <c:if test="${empty fundingDetail.contract}">
												 	<input type="text" value="" disabled="true" readonly="true" size="12"/>
                                                 </c:if>
                                                 <c:if test="${not empty fundingDetail.contract}">
													<input type="text" value="${fundingDetail.contract.contractName}" readonly="true" size="10"/>
                                                 </c:if>
                                                 <input type="submit" value="<digi:trn key='aim:LinkContract'>Link to Contract</digi:trn>" onclick='return addDisbOrderToContract("${fundingDetail.indexId}")'/>
											</td>
											</field:display>

												
											<field:display name="Rejected Disbursement Order" feature="Disbursement Orders">
											
                                            <td valign="center" align="center">
												<html:checkbox name="fundingDetail" indexed="true" property="disbursementOrderRejected" />
											</td>
											</field:display>
                                            
                                            <field:display name="Remove Disbursement Order Link" feature="Disbursement Orders">											
												<td valign="center">
													<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,4)">
													 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
													</a>
												</td>
											</field:display>
										</tr>

								</c:if>


						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td align="center">
						<table cellPadding="3">
							<tr>


								<td>
									<c:set var="contentDisabled"><field:display name="Add Disbursement Order Button" feature="Disbursement Orders">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
									<c:if test="${contentDisabled=='false'}">
										<input class="dr-menu" type="button" name="xx1" value="<digi:trn key='aim:addDisbursementOrder'>Add Disbursement Order</digi:trn>" onClick="addFundingDetail(4)">
									</c:if>
									<c:if test="${contentDisabled=='true'}">
										<input class="dr-menu" type="button" disabled="disabled" name="xx1" value="<digi:trn key='aim:addDisbursementOrder'>Add Disbursement Order</digi:trn>" onClick="addFundingDetail(4)">
									</c:if>
								</td>

							</tr>

						</table>
					</td>
				</tr>
			</table>
			</td></tr>
			</table>
		</td>
	</tr>
         </feature:display>




	<!-- disbursements -->
<feature:display module="Funding" name="Disbursement">
	<tr>
		<td vAlign="top">

			<table width="99%" cellpadding="0" cellspacing="1" valign="top" align="left" bgcolor="#006699">
			<tr><td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td bgcolor="#006699" class="textalb" height="20" align="center">
					<a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>"><digi:trn key="Disbursements">Disbursements</digi:trn></a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="3" class="box-border-nopadding">
							<tr bgcolor="#003366" class="textalb">
								<field:display name="Adjustment Type Disbursement" feature="Disbursement">
								<td align="center" valign="middle" style="width: 9%;">
									<b><font color="white"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn>/<br><digi:trn key="aim:Actual">Actual</digi:trn></font></b>
								</td>
                                </field:display>
								<field:display name="Amount Disbursement" feature="Disbursement">
								<td align="center" valign="middle" >
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommittedNoThousand">Full amount of expected transfer, irrespective of the time required for the completion of disbursements.</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
								</td>
                                </field:display>
								<field:display name="Currency Disbursement" feature="Disbursement">
								<td align="center" valign="middle" >
									<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">
									<b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b></a>
								    <img src= "../ampTemplate/images/help.gif" border="0" align="absmiddle" title="${translation}" /></td>
                                </field:display>
								<field:display name="Date Disbursement" feature="Disbursement">
								<td align="center" valign="middle" >
								<a title="<digi:trn key="aim:DateofDisbursement">Date of actual international transfer of financial resources</digi:trn>">
								<b><font color="white">
                                  <digi:trn useKey="true" key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br><digi:trn key="aim:DisbursementDateFIE">Disbursement Date</digi:trn></font></b></a>
								</td>
                                </field:display>
                                  <feature:display module="Funding" name="Disbursement Orders">
                                <td align="center" valign="middle" style="width: 10%;">
									<field:display name="Link to Disbursement Order ID" feature="Disbursement">
	                                <b><font color="white"><digi:trn key="aim:DisbursementOrderIDFIE">Disbursement Order ID</digi:trn></font></b>
									</field:display>
                                </td>
                                </feature:display>
                                
								<td align="center" valign="middle" colspan="1" style="width: 15%;">
									<field:display name="Contract of Disbursement" feature="Disbursement">
									<b><font color="white"><digi:trn key="aim:DisbursementOrderContractId">Contract ID</digi:trn></font></b>
									</field:display>
								</td>
								
								<td align="center" valign="middle" colspan="1" style="width: 15%;">
									<field:display name="Related Pledge" feature="Disbursement">
										<b><font color="white"><digi:trn>Pledges</digi:trn></font></b>
									</field:display>
								</td>
								<td align="center" valign="middle">*&nbsp;</td>
							</tr>
							<c:if test="${ !empty aimEditActivityForm.funding.fundingDetails}">
							<c:forEach var="fundingDetail" items="${aimEditActivityForm.funding.fundingDetails}">
							<gateperm:putInScope key="currentFundingDetail" name="fundingDetail">
						 	<c:if test="${fundingDetail.transactionType==1}">
										<tr>
										
											<c:set var="contentDisabled"><field:display name="Adjustment Type Disbursement" feature="Disbursement">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="middle">
												<c:set var="index" value="${index+1}"/>
											<c:if test="${aimEditActivityForm.identification.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
													<html:option value="0">Planned</html:option>
												</html:select>
											</c:if>
											<c:if test="${aimEditActivityForm.identification.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="${contentDisabled}" styleClass="inp-text">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
												<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
											</td>

											<c:set var="contentDisabled"><field:display name="Amount Disbursement" feature="Disbursement">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="middle">
												<html:text name="fundingDetail" disabled="${contentDisabled}" indexed="true" title="${formatTip}"  property="transactionAmount" onchange="addForValidation(this)" size="17"/>
											</td>


											<c:set var="contentDisabled"><field:display name="Currency Disbursement" feature="Disbursement">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="middle">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" disabled="${contentDisabled}" onchange="checkCurrency(this.name);" styleClass="inp-text">
													<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
											</td>



											<c:set var="contentDisabled"><field:display name="Date Disbursement" feature="Disbursement">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<td valign="center" align="center">
												<table cellpadding="2" cellspacing="0">
													<tr>
														<td>
															<% tempIndexStr = "" + tempIndex; tempIndex++;%>
															<html:text name="fundingDetail" readonly="true" indexed="true" property="transactionDate" 
															styleId="<%=tempIndexStr%>" size="10" onchange="addForValidation(this)"/>
														</td>
														<td valign="center" align="center">
															<c:if test="${contentDisabled=='false'}">
																<a id="transDate<%=tempIndexStr%>" href='javascript:pickDateById("transDate<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																</a>
															</c:if>
														</td>
													</tr>
												</table>
											</td>
											
											
											<c:set var="contentDisabled"><field:display name="Amount of Disbursement Order" feature="Disbursement Orders">false</field:display></c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
                                           <feature:display module="Funding" name="Disbursement Orders">
	  											<td valign="middle" align="center">
	                                                <c:if test="${aimEditActivityForm.funding.disbursementOrders}">
													 <html:select name="fundingDetail" indexed="true" property="disbOrderId" disabled="${contentDisabled}" styleClass="inp-text">
														<html:option value="">&nbsp;</html:option>
			   										    <c:forEach var="funding" items="${aimEditActivityForm.funding.fundingDetails}">
															<c:if test="${funding.transactionType==4}">
																<html:option value="${funding.disbOrderId}">${funding.disbOrderId}</html:option>
															</c:if>
														</c:forEach>
													</html:select>
	                                               </c:if>&nbsp;
												</td>
                                           </feature:display>
        	
											<!-- 
                                            <td>
    	                                        <html:text name="fundingDetail" property="disbOrderId" readonly="true"/>
                                            </td>
                                            <td>
	                                            <input type="submit" value="<digi:trn key='aim:LinkDisbOrder'>Link to Disbursement Order</digi:trn>" onclick='return addDisbOrderToDisb("${fundingDetail.indexId}")'/>
											</td>
											 -->
											         
										<field:display name="Link to Disbursement Order ID" feature="Disbursement">
										 	<td align="center" valign="middle">
                                                	<c:if test="${empty fundingDetail.contract}">
														<input type="text" value="" size="10"/>
                                                
                                                    </c:if>
                                                    <c:if test="${not empty fundingDetail.contract}">
        												<input type="text" value="${fundingDetail.contract.contractName}" size="10"/>
                                                    </c:if>
                                                    <field:display name="Contract of Disbursement" feature="Disbursement">
                                            
                                               	<input type="submit" value="<digi:trn key='aim:LinkContract'>Link to Contract</digi:trn>" onclick='return addDisbOrderToContract("${fundingDetail.indexId}")'/>
												
											
											</field:display>
											</td>
										</field:display>

											
											<field:display name="Related Pledge" feature="Disbursement">
											<td valign="middle">
													<html:select name="fundingDetail" indexed="true" property="pledge" styleClass="inp-text">
														<html:option value="0"><digi:trn>Select Pledge</digi:trn> </html:option>
														<html:optionsCollection name="aimEditActivityForm" property="funding.pledgeslist" value="id" label="title"/>
												</html:select>		
												</td> 
											</field:display>
											
											<field:display name="Remove Disbursement Link" feature="Disbursement">
												<td align="right" valign="middle">
													<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,1)">
													 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
													</a>
												</td>
											</field:display>
										</tr>
							</c:if>
							</gateperm:putInScope>
						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td align="center">
						<table cellPadding="3">
							<tr>
								<%--
								<td>
									<input class="dr-menu" type="button" name="xx1" value="Delete Disbursements"
								   onclick="this.form.event.value='delDisbursements';addFundingDetail();">
								</td>--%>
								<c:set var="contentDisabled"><field:display name="Add Disbursement Button" feature="Disbursement">false</field:display></c:set>
								<c:if test="${contentDisabled==''}">
									<c:set var="contentDisabled">true</c:set>
								</c:if>
								<td>
									<c:if test="${contentDisabled=='false'}">
										<input class="dr-menu" type="button" name="xx1" value="<digi:trn key='aim:addDisbursement'>Add Disbursement</digi:trn>" onClick="addFundingDetail(1)">
									</c:if>
									<c:if test="${contentDisabled=='true'}">
										<input class="dr-menu" disabled="disabled" type="button" name="xx1" value="<digi:trn key='aim:addDisbursement'>Add Disbursement</digi:trn>" onClick="addFundingDetail(1)">
									</c:if>
								</td>
							</tr>

						</table>
					</td>
				</tr>
			</table>
			</td></tr>
			</table>
		</td>
	</tr>
</feature:display>

	<!-- expenditures -->
	<feature:display module="Funding" name="Expenditures">
	<tr>
		<td vAlign="top">
			<table width="99%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
 						<a title="<digi:trn key='aim:ExpenditureofFund'>Amount effectively spent by the implementing agency</digi:trn>"><digi:trn key="aim:expenditures">Expenditures</digi:trn></a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="2" class="box-border-nopadding">
							<tr bgcolor="#003366" class="textalb">
								<field:display name="Adjustment Type Expenditure" feature="Expenditures">
								<td align="center" valign="middle" >
									<b><font color="white"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn>/<br><digi:trn key="aim:Actual">Actual</digi:trn></font></b>
								</td>
								</field:display>
								<field:display name="Amount Expenditure" feature="Expenditures">
								<td align="center" valign="middle" >
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommittedNoThousand">Full amount of expected transfer, irrespective of the time required for the completion of disbursements.</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
									</gs:test>
								</td>
								</field:display>
								
								<field:display name="Currency Expenditure" feature="Expenditures">
									<td align="center" valign="middle">
										<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">								   		  
											<b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b>
										</a>
									    <img src= "../ampTemplate/images/help.gif" border="0" align="absmiddle"  title="${translation}" />
									</td>
								</field:display>
								<field:display name="Date Expenditure" feature="Expenditures">
								<td align="center" valign="middle">
									<a title="<digi:trn key="aim:DateofExpenditure">Date of actual expenditure</digi:trn>">
									<b><font color="white"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br><digi:trn key="aim:ExpenditureDateFIE">Expenditure Date</digi:trn></font></b></a>
								</td>
								</field:display>
								<field:display name="Remove Expenditure Link" feature="Expenditures">
											<td>&nbsp;</td>
								</field:display>
							</tr>
							<c:if test="${ !empty aimEditActivityForm.funding.fundingDetails}">
						 	<c:forEach var="fundingDetail" items="${aimEditActivityForm.funding.fundingDetails}">
						 	<c:if test="${fundingDetail.transactionType==2}">
							 	<tr bgcolor="#ffffff">

							 	
									<c:set var="contentDisabled"><field:display name="Adjustment Type Expenditure" feature="Expenditures">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
										<td valign="bottom" align="center">
											<c:set var="index" value="${index+1}"/>
											<c:if test="${aimEditActivityForm.identification.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
													<html:option value="0">Planned</html:option>
												</html:select>
											</c:if>
											<c:if test="${aimEditActivityForm.identification.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text" disabled="${contentDisabled}">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn useKey="true" key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
											<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
										</td>


									<c:set var="contentDisabled"><field:display name="Amount Expenditure" feature="Expenditures">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
										<td valign="bottom" align="center">
											<html:text name="fundingDetail" disabled="${contentDisabled}" indexed="true" title="${formatTip}"  property="transactionAmount" onchange="addForValidation(this)" size="17" styleClass="amt"/>
										</td>


									<c:set var="contentDisabled"><field:display name="Currency Expenditure" feature="Expenditures">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
										<td valign="bottom" align="center">
											<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text" disabled="${contentDisabled}" onchange="checkCurrency(this.name);">
												<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode" label="currencyName"/>
											</html:select>
										</td>


									<c:set var="contentDisabled"><field:display name="Date Expenditure" feature="Expenditures">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
										<td valign="bottom" align="center">
											<table cellpadding="0" cellspacing="0">
												<tr>
														<td>
															<% tempIndexStr = "" + tempIndex; tempIndex++;%>
															<html:text name="fundingDetail" readonly="true" indexed="true" property="transactionDate" styleId="<%=tempIndexStr%>"  size="10" onchange="addForValidation(this)"/>
														</td>
														<td align="left" vAlign="center">&nbsp;
															<c:if test="${contentDisabled=='false'}">
																<a id="trans6Date<%=tempIndexStr%>" href='javascript:pickDateById("trans6Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</c:if>
															<% tempIndex++; %>
														</td>
													
												</tr>
											</table>
										</td>


									<field:display name="Remove Expenditure Link" feature="Expenditures">
											<td align="center"> 
												<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,2)">
								 					<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
												</a>
											</td>
									</field:display>
									
								</tr>
									<c:set var="contentDisabled"><field:display name="Classification Expenditure" feature="Funding Information">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
								
								<field:display name="Classification Expenditure" feature="Funding Information">
								<tr bgcolor="#ffffff">
									<td colspan="6">
										<b><digi:trn key="aim:classification">Classification</digi:trn></b>&nbsp;
									
										<html:text name="fundingDetail" indexed="true" property="classification" size="75" styleClass="inp-text" disabled="${contentDisabled}"/>
									</td>
								</tr>
								
								</field:display>
								<tr>
									<td colspan="6" bgcolor="#dddddd"></td>
								</tr>
							</c:if>
					 	</c:forEach>
				 	</c:if>
				</table>
			</td>
		</tr>
				<tr bgcolor="#ffffff">
					<td align="center">
						<table cellPadding="3">
							<tr>
								<td>
									<c:set var="contentDisabled"><field:display name="Add Expenditure Button" feature="Expenditures">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
									
									<c:if test="${contentDisabled=='false'}">
										<input class="dr-menu" type="button" name="xx1" value="<digi:trn key='aim:addExpediture'>Add Expenditure</digi:trn>" onClick="addFundingDetail(2)">
									</c:if>
									<c:if test="${contentDisabled=='true'}">
										<input class="dr-menu" disabled="disabled" type="button" name="xx1" value="<digi:trn key='aim:addExpediture'>Add Expenditure</digi:trn>" onClick="addFundingDetail(2)">
									</c:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</td></tr>
			</table>
		</td>
	</tr>
    <!--end expenditures-->
    </feature:display>
    <field:display name="Conditions for Fund Release" feature="Funding Information">
	<tr>
		<td>
			<table width="100%" cellspacing="1" cellPadding="1">
				<tr>
					<td align="left" bgcolor="#ECF3FD" valign="top" width="80">
						<b>
						<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<digi:trn key="aim:conditions">Conditions</digi:trn></b></a>
					</td>
					<td align="left" bgcolor="#ECF3FD">
					<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<html:textarea property="funding.fundingConditions" rows="3" cols="75" styleClass="inp-text"/>
					</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</field:display>
	<field:display name="Donor Objective" feature="Funding Information">
	<tr>
		<td>
			<table width="100%" cellspacing="1" cellPadding="1">
				<tr>
					<td align="left" bgcolor="#ECF3FD" valign="top" width="80">
						<b>
						<a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>">
						<digi:trn key="aim:donorobjective">Donor Objective</digi:trn></b></a>
					</td>
					<td align="left" bgcolor="#ECF3FD">
					<a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>">
						<html:textarea property="funding.donorObjective" rows="3" cols="75" styleClass="inp-text"/>
					</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</field:display>
	<tr>
		<td width="100%" vAlign="top">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
						<table cellPadding="3">
							<tr>
								<td>
									<input type="button" id="saveFundingBtn" value="<digi:trn key='btn:save'>Save</digi:trn>" class="inp-text" onClick="return addFunding()">
								</td>
								<td>
									<input type="reset" value="<digi:trn key='btn:reset'>Reset</digi:trn>" class="inp-text">
								</td>
								<td>
									<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="inp-text" onClick="closeWindow()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

</table>
</digi:form>
</body>


<script type="text/javascript">
var enterBinder	= new EnterHitBinder('saveFundingBtn');
</script>
