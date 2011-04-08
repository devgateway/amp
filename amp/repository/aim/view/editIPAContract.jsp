<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %> 
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="org.digijava.module.aim.util.CurrencyUtil" %>
<%@ page import="org.digijava.module.aim.dbentity.AmpCurrency" %>
            

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<!-- invoked to close myself and reload my parent (after save was performed) -->
<logic:present name="close">
<script type="text/javascript">
	window.opener.location.href = window.opener.location.href;
	window.close();
</script>	
</logic:present>

<script type="text/javascript">
<!--
function clearDefault(editBox)
{
		if(editBox.value=='Amount') editBox.value='';
}
function fnChk(frmContrl){
  <c:set var="errMsgAddNumericValue">
  <digi:trn key="aim:addNumericValueErrorMessage">
  Please enter numeric value only
  </digi:trn>
  </c:set>
  if (isNaN(frmContrl.value)) {
    alert("${errMsgAddNumericValue}");
    frmContrl.value = "";
    return false;
  }
  return autosum();
}

function fnChk1(frmContrl){
  <c:set var="errMsgAddNumericValue">
  <digi:trn key="aim:addNumericValueErrorMessage">
  Please enter numeric value only
  </digi:trn>
  </c:set>
  if (isNaN(frmContrl.value)) {
    alert("${errMsgAddNumericValue}");
    frmContrl.value = "";
    return false;
  }
  return true;
}

function autosum(){
	var v1 = document.aimIPAContractForm.totalECContribIBAmount ? document.aimIPAContractForm.totalECContribIBAmount.value:0;
	var v2 = document.aimIPAContractForm.totalECContribINVAmount ? document.aimIPAContractForm.totalECContribINVAmount.value:0;
	var v3 = document.aimIPAContractForm.totalNationalContribCentralAmount ? document.aimIPAContractForm.totalNationalContribCentralAmount.value:0;
	var v4 = document.aimIPAContractForm.totalNationalContribIFIAmount ? document.aimIPAContractForm.totalNationalContribIFIAmount.value:0;
	var v5 = document.aimIPAContractForm.totalNationalContribRegionalAmount ? document.aimIPAContractForm.totalNationalContribRegionalAmount.value:0;
	var v6 = document.aimIPAContractForm.totalPrivateContribAmount ? document.aimIPAContractForm.totalPrivateContribAmount.value:0;
	if (document.aimIPAContractForm.totalAmount) {		
		document.aimIPAContractForm.totalAmount.value = (v1*1)+(v2*1)+(v3*1)+(v4*1)+(v5*1)+(v6*1);
	}	
	return true; 
}

function validate(){
    if (trim(document.aimIPAContractForm.contractName.value) == "") {
        <c:set var="translation">
        <digi:trn key="aim:pleaseEnterContractName">Please enter Contract Name</digi:trn>
        </c:set>
        alert("${translation}");
            document.aimIPAContractForm.contractName.focus();
            return false;
        }
    <c:set var="errMsgSelectCurrency">
    <digi:trn key="aim:PleaseSelectCurrency">
    Please Select Currency
    </digi:trn>
    </c:set>
    if(document.aimIPAContractForm.contractTotalValue!=null)
      {
      	if(document.aimIPAContractForm.contractTotalValue.value!='' && (document.aimIPAContractForm.totalAmountCurrency.value==-1))
	     {
	     	alert("${errMsgSelectCurrency}");
		    return false;
	      }
	 
      }
      else{
    	  if((document.aimIPAContractForm.totalECContribIBAmount &&
    		   		document.aimIPAContractForm.totalECContribINVAmount &&
    		    	document.aimIPAContractForm.totalNationalContribIFIAmount &&
    		    	document.aimIPAContractForm.totalNationalContribCentralAmount &&
    		    	document.aimIPAContractForm.totalNationalContribRegionalAmount &&
    		    	document.aimIPAContractForm.totalPrivateContribAmount) &&
    		        (document.aimIPAContractForm.totalAmountCurrency)) {
		    if((document.aimIPAContractForm.totalECContribIBAmount.value!='' ||
		   		document.aimIPAContractForm.totalECContribINVAmount.value!='' ||
		    	document.aimIPAContractForm.totalNationalContribIFIAmount.value!='' ||
		    	document.aimIPAContractForm.totalNationalContribCentralAmount.value!='' ||
		    	document.aimIPAContractForm.totalNationalContribRegionalAmount.value!='' ||
		    	document.aimIPAContractForm.totalPrivateContribAmount.value!='') &&
		        (document.aimIPAContractForm.totalAmountCurrency.value==-1))
		    {
		        alert("${errMsgSelectCurrency}");
		        return false;
		    }
    	  }
	    }
			    
        mySaveReportEngine.saveContract();
        return true;
    }

function selectOrganisation1() {
		openNewWindow(650, 420);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=3" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}



var responseSuccess = function(o){ 
/* Please see the Success Case section for more
 * details on the response object's properties.
 * o.tId
 * o.status
 * o.statusText
 * o.getResponseHeader[ ]
 * o.getAllResponseHeaders
 * o.responseText
 * o.responseXML
 * o.argument
 */
 
	var response = o.responseText; 
	var content = document.getElementById("myContractContent");
    //response = response.split("<!")[0];
	content.innerHTML = response;
	showAddContract();
}
	 
var responseFailure = function(o){ 
// Access the response object's properties in the 
// same manner as listed in responseSuccess( ). 
// Please see the Failure Case section and 
// Communication Error sub-section for more details on the 
// response object's properties.
	alert("Connection Failure!"); 
}  
var callback = 
{ 
	success:responseSuccess, 
	failure:responseFailure 
};

function calculer1() {
	<c:set var="errMsgAddNumericValue">
	  <digi:trn key="aim:addNumericValueErrorMessage">
	  Please enter numeric value only
	  </digi:trn>
	  </c:set> 
	  var tmp = document.getElementsByName("donorContractFundinAmount")[0].value;
	  if (isNaN(tmp)) {
	    alert("${errMsgAddNumericValue}");
	    document.getElementsByName("donorContractFundinAmount")[0].value = 0;
	    return false;
	  } else {
		  if (tmp == "") tmp = 0; 
		  var sum = parseFloat(tmp);
		  //
		  var divId = document.getElementById("ContractAmendmentsList");
			if (divId != null){
				var elems = divId.getElementsByTagName("input");
				for (var i=0; i<elems.length; i++) {
					//contractAmendment[0].amount
					var str = elems[i].name;
					var debut = str.length - 8;
					if (str.substring(debut, str.length) == "amoutStr") {
						if (isNaN(elems[i].value)) {
							alert("${errMsgAddNumericValue}");
							elems[i].value = 0;
							sum = sum + 0;
						    //return false;
						} else {
							sum = sum + parseFloat(elems[i].value);
						}
					}
				}	
			}
		  //
		  //alert(sum);
		  document.aimIPAContractForm.totAmountDonorContractFunding.value = sum;
		  //document.getElementsByName("totAmountDonorContractFunding")[0].value += sum;
		  //
		  return true;
	  }
}

function miseajourdevise() {
	var curId = document.getElementsByName("donorContractFundingCurrency")[0].value;
	//
	document.getElementsByName("totalAmountCurrencyDonor")[0].value = curId;
	document.getElementsByName("totalAmountCurrencyCountry")[0].value = curId;
	//
	var divId = document.getElementById("ContractAmendmentsList");
	if (divId != null){
		var elems = divId.getElementsByTagName("select");
		for (var i=0; i<elems.length; i++) {
			elems[i].value = curId;
		}	
	}
}

function getContractDisbursments(){
	var divId = document.getElementById("ContractDisbursmentsList");
	var ret = "";
	if (divId != null){
		var elems = divId.getElementsByTagName("input");
		
		for (var i=0; i<elems.length; i++) {
			if (elems[i].type != "checkbox"){
				ret += elems[i].name + "=" + elems[i].value + "&";
			}
		}
		elems = divId.getElementsByTagName("select");
		for (var i=0; i<elems.length; i++) {
			ret += elems[i].name + "=" + elems[i].value + "&";
		}	
	}
	return ret;
}
function getContractAmendments(){
	var divId = document.getElementById("ContractAmendmentsList");
	var ret = "";
	if (divId != null){
		var elems = divId.getElementsByTagName("input");
		
		for (var i=0; i<elems.length; i++) {
			if (elems[i].type != "checkbox"){
				ret += elems[i].name + "=" + elems[i].value + "&";
			}
		}
		elems = divId.getElementsByTagName("select");
		for (var i=0; i<elems.length; i++) {
			ret += elems[i].name + "=" + elems[i].value + "&";
		}	
	}
	return ret;
}

function generateFields(){
	var ret = "";
	ret =			    
						
							"contractName="+document.getElementsByName("contractName")[0].value+"&"
						<field:display name="Contract Description" feature="Contracting">
							+ "description="+document.getElementsByName("description")[0].value+"&"
						</field:display>
						<field:display name="Contracting IPA Activity Category" feature="Contracting">
							+ "activityCategoryId="+document.getElementsByName("activityCategoryId")[0].value+"&"
						</field:display>						
						<field:display name="Contracting Start of Tendering" feature="Contracting"> 
			 				+ "startOfTendering="+document.getElementsByName("startOfTendering")[0].value+"&"
						</field:display>
						<field:display name="Contract Validity Date" feature="Contracting">
							+ "contractValidity="+document.getElementsByName("contractValidity")[0].value+"&"
						</field:display>
						<field:display name="Contracting Tab Status" feature="Contracting">
							+ "statusId="+document.getElementsByName("statusId")[0].value+"&"
						</field:display>
						<field:display name="Signature of Contract" feature="Contracting">
							+ "signatureOfContract="+document.getElementsByName("signatureOfContract")[0].value+"&"
						</field:display>
						<field:display name="Contract Completion" feature="Contracting">
							+ "contractCompletion="+document.getElementsByName("contractCompletion")[0].value+"&"
						</field:display>
						<field:display name="Contracting Contractor Name" feature="Contracting">
							+ "contractingOrganizationText="+document.getElementsByName("contractingOrganizationText")[0].value+"&"
						</field:display>
						<field:display name="Contracting Total Amount" feature="Contracting">
							+ "totalAmount="+document.getElementsByName("totalAmount")[0].value+"&"
						</field:display>
						<field:display name="Contract Total Value" feature="Contracting">
							+ "contractTotalValue="+document.getElementsByName("contractTotalValue")[0].value+"&"
							+ "totalAmountCurrency="+document.getElementsByName("totalAmountCurrency")[0].value+"&"
						</field:display>				
						
						<field:display name="Contracting IB" feature="Contracting">
							+ "totalECContribIBAmount="+document.getElementsByName("totalECContribIBAmount")[0].value+"&"
							+ "totalECContribIBAmountDate="+document.getElementsByName("totalECContribIBAmountDate")[0].value+"&"
						</field:display>
						//+ "totalECContribIBCurrency="+document.getElementsByName("totalECContribIBCurrency")[0].value+"&"
						<field:display name="Contracting INV" feature="Contracting">
							+ "totalECContribINVAmount="+document.getElementsByName("totalECContribINVAmount")[0].value+"&"
							+ "totalECContribINVAmountDate="+document.getElementsByName("totalECContribINVAmountDate")[0].value+"&"
						</field:display>
						//+ "totalECContribINVCurrency="+document.getElementsByName("totalECContribINVCurrency")[0].value+"&"
						<field:display name="Contracting Central Amount" feature="Contracting">
							+ "totalNationalContribCentralAmount="+document.getElementsByName("totalNationalContribCentralAmount")[0].value+"&"
							+ "totalNationalContribCentralAmountDate="+document.getElementsByName("totalNationalContribCentralAmountDate")[0].value+"&"
						</field:display>
						//+ "totalNationalContribCentralCurrency="+document.getElementsByName("totalNationalContribCentralCurrency")[0].value+"&"
						<field:display name="Contracting IFIs" feature="Contracting">
							+ "totalNationalContribIFIAmount="+document.getElementsByName("totalNationalContribIFIAmount")[0].value+"&"
							+ "totalNationalContribIFIAmountDate="+document.getElementsByName("totalNationalContribIFIAmountDate")[0].value+"&"
						</field:display>
						//+ "totalNationalContribIFICurrency="+document.getElementsByName("totalNationalContribIFICurrency")[0].value+"&"
						<field:display name="Contracting Regional Amount" feature="Contracting">
							+ "totalNationalContribRegionalAmount="+document.getElementsByName("totalNationalContribRegionalAmount")[0].value+"&"
							+ "totalNationalContribRegionalAmountDate="+document.getElementsByName("totalNationalContribRegionalAmountDate")[0].value+"&"
						</field:display>
						<field:display name="Total Private Contribution" feature="Contracting">
						//+ "totalNationalContribRegionalCurrency="+document.getElementsByName("totalNationalContribRegionalCurrency")[0].value+"&"
						+ "totalPrivateContribAmount="+document.getElementsByName("totalPrivateContribAmount")[0].value+"&"
						+ "totalPrivateContribAmountDate="+document.getElementsByName("totalPrivateContribAmountDate")[0].value+"&"
						</field:display>
						<field:display name="Contracting Disbursements Global Currency" feature="Contracting">
						//+ "totalPrivateContribCurrency="+document.getElementsByName("totalPrivateContribCurrency")[0].value+"&"
						+ "dibusrsementsGlobalCurrency="+document.getElementsByName("dibusrsementsGlobalCurrency")[0].value
						+ "&"
						</field:display>
						<field:display name="Contracting Amendments" feature="Contracting">
							+ "donorContractFundinAmount="+document.getElementsByName("donorContractFundinAmount")[0].value+"&"
							+ "donorContractFundingCurrency="+document.getElementsByName("donorContractFundingCurrency")[0].value+"&"
						</field:display>
							<field:display name="Contracting Amendments" feature="Contracting">
							+ "totAmountDonorContractFunding="+document.getElementsByName("totAmountDonorContractFunding")[0].value+"&"
							+ "totalAmountCurrencyDonor="+document.getElementsByName("totalAmountCurrencyDonor")[0].value+"&"
						</field:display>
							<field:display name="Contracting Amendments" feature="Contracting">
							+ "totAmountCountryContractFunding="+document.getElementsByName("totAmountCountryContractFunding")[0].value+"&"
							+ "totalAmountCurrencyCountry="+document.getElementsByName("totalAmountCurrencyCountry")[0].value+"&"
						</field:display>
						 + getContractDisbursments()
						 + getContractAmendments();
	
		//alert(ret);
		<field:display name="Contracting IPA Contract Type" feature="Contracting">
			if(document.getElementsByName("typeId")[0] != null){
				ret = ret + "typeId="+document.getElementsByName("typeId")[0].value+"&";
			}
			if(document.getElementsByName("contractTypeId")[0] != null){
				ret = ret + "contractTypeId="+document.getElementsByName("contractTypeId")[0].value+"&"
			}
		</field:display>
		//alert(ret);
		//
		
		//
		return ret;
}

function addDisb() {
	var postString		= "addFields=true&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}
function addAmendment() {
	var postString		= "addAmendments=true&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}

function orgsAdded() {
	var postString		= generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}

function delOrgs() {
	var postString		= "removeOrgs=true&" + getCheckedFields("selOrgs")+"&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);	
}

function getCheckedFields(name) {
	var ret			= "";
	//var ulEl		= document.getElementById( ulId );
	//var fields		= ulEl.getElementsByTagName( "input" );
	
	var elems = document.getElementsByName(name);
	
	for ( var i=0; i<elems.length; i++ ) {
		if (elems[i].checked){
			ret += name+"=";
			ret +=elems[i].value;//"true";
			if ( i < elems.length-1 )
				ret += "&";
		}
		//else
		//    ret +="false";
	}
	return ret;	
}

function delDisb() {
	var postString		= "removeFields=true&" + getCheckedFields("selContractDisbursements")+"&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}
function delAmendment() {
	var postString		= "delAmendments=true&" + getCheckedFields("selContractAmendments")+"&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}




function getContractActivityCategoryId() {
	return aimIPAContractForm.activityCategoryId.value;
}

function getContractActivityCategoryId() {
	return aimIPAContractForm.activityCategoryId.value;
}


function SaveReportEngine ( savingMessage, failureMessage ) {
;
}

function initScripts(){
	autosum();
}

SaveReportEngine.prototype.success		= function (o) {
	window.location.replace(window.location.href);
}
SaveReportEngine.prototype.failure			= function(o) {
	alert("Could not connect!");
}

SaveReportEngine.prototype.saveContract	= function () {
	var postString		= "save=true&"+generateFields();
	//alert (postString);
	
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", this, postString);
}

mySaveReportEngine = new SaveReportEngine();
window.onload=autosum;
-->
</script>

<!-- code for rendering that nice calendar -->


<body onLoad="load()">
<digi:instance property="aimIPAContractForm" />
<digi:form action="/editIPAContract.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden property="id"/>
<html:hidden property="indexId"/>

<digi:errors/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
<b><digi:trn key="aim:IPA:newPopup:title">Add/Edit Contracting</digi:trn></b>
</td></tr>
<field:display name="Contract Name" feature="Contracting">
	<tr>
		<td align="left" nowrap>
                     <FONT color=red>*</FONT>
		<digi:trn key="aim:IPA:newPopup:name">Contract Name</digi:trn>
		</td>
		<td>
		         	<html:text property="contractName" size="90"/> 
		</td>
	</tr>
</field:display>
<field:display name="Contract Description" feature="Contracting">
	<tr>
		<td align="left" valign="top" nowrap>
			<digi:trn key="aim:IPA:newPopup:description">Contract Description</digi:trn>
		</td>
		<td>
			<html:textarea property="description" rows="5" cols="87" styleClass="inp-text"/>
		</td>
	</tr>
</field:display>
<field:display name="Contracting Activity Category" feature="Contracting">
	<tr>
		
		<td colspan="2">
			<field:display name="Contracting IPA Activity Category" feature="Contracting">
				<digi:trn key="aim:IPA:newPopup:actCat">Activity Category</digi:trn>&nbsp;&nbsp;
				<category:showoptions name="aimIPAContractForm" property="activityCategoryId"  
									  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IPA_ACTIVITY_CATEGORY_KEY %>" 
									  styleClass="inp-text" />
				
				&nbsp;&nbsp;&nbsp;
			</field:display>
			<field:display name="Contracting IPA Contract Type" feature="Contracting">
				<digi:trn key="aim:IPA:newPopup:contractType">Contract Type</digi:trn>
				&nbsp;&nbsp;
	
				<category:showoptions name="aimIPAContractForm" property="contractTypeId"  
									  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IPA_TYPE_KEY %>" 
									  styleClass="inp-text" />
	
				&nbsp;&nbsp;&nbsp;
			</field:display>
			<field:display name="Contracting IPA Activity Type" feature="Contracting">
				<digi:trn key="aim:IPA:newPopup:actType">Activity Type</digi:trn>
				&nbsp;&nbsp;
				
				<category:showoptions name="aimIPAContractForm" property="typeId"  
									  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IPA_ACTIVITY_TYPE_KEY %>" 
									  styleClass="inp-text" />
			</field:display>
			
		</td>
	</tr>
</field:display>
         
         
	<tr><td id="calendarPosition" colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:details">Details</digi:trn></b>
	</td></tr>
         
	<tr>
	<td colspan="2">
	<table cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<field:display name="Contracting Start of Tendering" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup">Start of Tendering</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="startOfTendering" styleClass="inp-text" styleId="startOfTendering"/>
				<a id ="startOfTenderingDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","startOfTendering",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
				</a>
			</td>
		</field:display>

		<field:display name="Contract Validity Date" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractValidityDate">Contract Validity Date</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="contractValidity" styleClass="inp-text" styleId="contractValidity"/>
				<a id="contractValidityDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","contractValidity",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
				</a>
			</td>
		</field:display>
		
		<field:display name="Contracting Tab Status" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:status">Status</digi:trn>
			</td>
			<td align="left">
				<category:showoptions name="aimIPAContractForm" property="statusId"  
					  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IPA_STATUS_KEY %>" 
					  styleClass="inp-text" />
			</td>
		</field:display>
	</tr>
	<tr>
		<field:display name="Signature of Contract" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:signatureOfContract">Signature of Contract</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="signatureOfContract" styleClass="inp-text" styleId="signatureOfContract"/>
				<a id="signatureOfContractDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","signatureOfContract",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
				</a>
			</td>
		</field:display>
		<field:display name="Contract Completion" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractCompletion">Contract Completion</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" size="9" property="contractCompletion" styleClass="inp-text" styleId="contractCompletion"/>
				<a id="contractCompletionDate" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","contractCompletion",-250,-230)'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
				</a>
			</td>
		</field:display>
	</tr>

	
	<field:display name="Contracting Contractor Name" feature="Contracting">
		<tr>
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractingContractorName">Contractor Name</digi:trn>
			</td>
			<td colspan="5" align="left">
				<html:text  property="contractingOrganizationText" size="75" styleClass="inp-text"/>
			</td>
		</tr>				
	</field:display>
	</table>
	</td>
	</tr>	


	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:organizations">Organization(s)</digi:trn></b>
	</td></tr>
	
	<tr>
		<td colspan="6" align="left">
			<field:display name="Contract Organization" feature="Contracting">
				<aim:addOrganizationButton form="${aimIPAContractForm}" collection="organisations" refreshParentDocument="false" callBackFunction="orgsAdded()" useClient="false" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
				&nbsp;
				<c:if test="${!empty aimIPAContractForm.organisations}">
					<html:button styleClass="dr-menu" property="deleteOrgs" onclick="delOrgs();">
						<digi:trn key="aim:IPA:newPopup:removeOrganizations">Remove Organizations</digi:trn>
					</html:button>
				</c:if>
			</field:display>
		</td>
	</tr>
	<tr>
		<td colspan="6" align="left">
			<table>
				<c:forEach items="${aimIPAContractForm.organisations}" var="selectedOrganizations" varStatus="selIdx">
					<c:if test="${!empty selectedOrganizations.ampOrgId}">
						<tr>
							<td align="left" width=3>
								<html:multibox property="selOrgs" >
									<c:out value="${selIdx.count}"/>
								</html:multibox>
							</td>
							<td align="left" width="367">
								<c:out value="${selectedOrganizations.name}"/>
							</td>
						</tr>
					</c:if>	
				</c:forEach>
			</table>
		</td>
	</tr>	


	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:donorContractFundinAmount">Part du contrat financé par le bailleur</digi:trn></b>
	</td></tr>

	<tr>
	<td colspan="2">
	<table cellpadding="2" cellspacing="2" width="100%">	
		<field:display name="Contracting Amendments" feature="Contracting">
			<tr>
				<td align="left">
					<digi:trn>Montant</digi:trn>
				</td>
				<td align="left">
					<html:text property="donorContractFundinAmount" style="text-align:right" onkeyup="calculer1()" />
				</td>
				<td align="left" colspan="4">
					<digi:trn>Devise</digi:trn>
					&nbsp;&nbsp;
					<html:select property="donorContractFundingCurrency" styleClass="inp-text" onchange="miseajourdevise();">
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
					</html:select>
					
<!--					<html:select property="donorContractFundingCurrency" styleClass="inp-text" onchange="miseajourdevise();">-->
<!--						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>-->
<!--					</html:select>-->
				</td>
			</tr>			
		</field:display>
	</table>
	</td>
	</tr>	

	

	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:fundingAllocation">Funding Allocation</digi:trn></b>
	</td></tr>

	<tr>
	<td colspan="2">
	<table cellpadding="2" cellspacing="2" width="100%">
	
		<field:display name="Contract Total Value" feature="Contracting">
			<tr>
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:contractTotalValue">Contract Total Value</digi:trn>
				</td>
				<td align="left">
					<c:set var="trnSum">
						<digi:trn key="aim:addNumericValueErrorMessage">
							Please enter the amount
						</digi:trn>
					</c:set>
					<html:text title="${trnSum}" property="contractTotalValue" style="text-align:right" onkeyup="fnChk1(this)" />
				</td>
				<td align="left" colspan="4">
					<digi:trn key="aim:ipa:newPopup:currencyType">Currency Type</digi:trn>
					&nbsp;&nbsp;
					<html:select property="totalAmountCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</tr>
		</field:display>
	
		<field:display name="Contracting Total Amount" feature="Contracting">
			<tr>
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:totalAmount">Total Amount</digi:trn>
				</td>
				<td align="left">
					<c:set var="trnAutosum">
						<digi:trn key="aim:addNumericValueErrorMessage">
							This amount will auto-calculate!
						</digi:trn>
					</c:set>
					<html:text readonly="true" title="${trnAutosum}" property="totalAmount" style="text-align:right" onkeyup="fnChk(this)" />
				</td>
				<td align="left" colspan="4">
					<digi:trn key="aim:ipa:newPopup:currencyType">Currency Type</digi:trn>
					&nbsp;&nbsp;
					<html:select property="totalAmountCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</tr>
		</field:display>

		<field:display name="Total EC Contribution" feature="Contracting">
			<tr>
				<td colspan="6" align="center">
					<b><digi:trn key="aim:ipa:newPopup:totalECContribution">Total EC Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
			<field:display name="Contracting IB" feature="Contracting">
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:ib">IB</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalECContribIBAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate1" property="totalECContribIBAmountDate"/>
					<a id="fimage1" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate1",-250,-230)'>
						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
					</a>
				</td>
		    </field:display>
		     
			<field:display name="Contracting INV" feature="Contracting">
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:inv">INV</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalECContribINVAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate2" property="totalECContribINVAmountDate"/>
					<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate2",-250,-230)'>
						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
					</a>
				</td>
			</field:display>
			</tr>
		</field:display>
		<field:display name="Contracting Total National Contribution" feature="Contracting">
			<tr>
				<td align="center" colspan="6">
					<b><digi:trn key="aim:IPA:newPopup:totalNationalContribution">Total National Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
				<field:display name="Contracting Central Amount" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:central">Central</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribCentralAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate3" property="totalNationalContribCentralAmountDate"/>
						<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate3",-250,-230)'>
							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
						</a>
					</td>
 				</field:display>

				<field:display name="Contracting IFIs" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:ifis">IFIs</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribIFIAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate4" property="totalNationalContribIFIAmountDate"/>
						<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate4",-250,-230)'>
							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
						</a>
					</td>
				</field:display>
			</tr>
			
			<tr>
				<field:display name="Contracting Regional Amount" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:regional">Regional</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribRegionalAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate5" property="totalNationalContribRegionalAmountDate"/>
						<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate5",-250,-230)'>
							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
						</a>
					</td>
				</field:display>
			</tr>
		</field:display>
		
		<field:display name="Total Private Contribution" feature="Contracting">
			<tr>
				<td align="center" colspan="6">
					<b><digi:trn key="aim:IPA:newPopup:totalPrivateContribution">Total Private Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:ib">IB</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalPrivateContribAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:text readonly="true" size="9" styleClass="inp-text" styleId="fdate6" property="totalPrivateContribAmountDate"/>
					<a href='javascript:pickDateByIdDxDyWOScroll("newmyContract","fdate6",-250,-230)'>
						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
					</a>
				</td>
			</tr>
		</field:display>
		
	</table>
	</td>
	</tr>

	
	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:disbursements">Disbursements</digi:trn></b>
	</td></tr>
	<field:display name="Contract Donor Disbursements" feature="Contracting">
	<logic:notEmpty name="aimIPAContractForm" property="fundingDetailsLinked">
	<tr>
		<td colspan="2">
			<table width="100%" >
			<tr>
				<th><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></th>
				<th><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></th>
				<th><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></th>
				<th><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></th>
				
			</tr>
				<logic:iterate name="aimIPAContractForm" property="fundingDetailsLinked" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
					<logic:equal name="fundingDetail" property="transactionType" value="1">		
																									<tr bgcolor="#ffffff">
																										<td align="center">
																											<field:display name="Adjustment Type Disbursement" feature="Disbursement">
																												<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																													<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																												</digi:trn>
																											</field:display>
																										</td>
																										<td align="center">
																											<field:display name="Amount Disbursement" feature="Disbursement">
																												<FONT color=blue>*</FONT>
																												<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																											</field:display>
																										</td>
																										<td align="center">
																											<field:display name="Currency Disbursement" feature="Disbursement">
																												<bean:write name="fundingDetail" property="currencyCode"/>
																											</field:display>
																										</td>
																										<td align="center">
																											<field:display name="Date Disbursement" feature="Disbursement">
																												<bean:write name="fundingDetail" property="transactionDate"/>
																											</field:display>
																										</td>
																																																	
																									</tr>
																						</logic:equal>
				</logic:iterate>
			</table>
		</td>
	</tr>
	</logic:notEmpty>
	</field:display>
	
	<field:display name="Contracting Disbursements" feature="Contracting">
	<tr>
		<td colspan="2" align="left">
			<field:display name="Contracting Add Disbursement" feature="Contracting">
				<html:button styleClass="dr-menu" property="adddisb" onclick="addDisb()">
					<digi:trn key="aim:IPA:newPopup:addDisbursement">Add Disbursement</digi:trn>
				</html:button>
			</field:display>
			&nbsp;	
			<field:display name="Contracting Remove Disbursements" feature="Contracting">
				<c:if test="${!empty aimIPAContractForm.contractDisbursements}">
					<html:button styleClass="dr-menu" property="deldisbursement" onclick="delDisb();">
						<digi:trn key="aim:IPA:newPopup:removeDisbursements">Remove Disbursements</digi:trn>
					</html:button>
				</c:if>			
			</field:display>				
		</td>
	</tr>
	</field:display>
	<field:display name="Contracting Disbursements Global Currency" feature="Contracting">
	<tr>
		<td colspan="2">
		<table width="100%">
			<tr>
				<td align="left" width="30%">
					<digi:trn key="aim:ipa:newPopup:dibusrsementsGlobalCurrency">Disbursements Global Currency</digi:trn>
				</td>
				<td align="left">
					<html:select property="dibusrsementsGlobalCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	</field:display>
	
	<field:display name="Contracting Disbursements" feature="Contracting">
	<logic:notEmpty name="aimIPAContractForm" property="contractDisbursements">
		<tr>
			<td colspan="2">
				<table id="ContractDisbursmentsList">
					<c:forEach  items="${aimIPAContractForm.contractDisbursements}" var="contractDisbursement"  varStatus="idx" >
						<tr>
						<td align="left" colspan="2">
							<html:hidden property="${contractDisbursement}" value="${id}"/>
							&nbsp;
							<html:multibox property="selContractDisbursements" value="${idx.count}"/>
							<html:select indexed="true" name="contractDisbursement" property="adjustmentType">
								<html:option value="0"><digi:trn key="aim:ipa:popup:actual"> Actual</digi:trn></html:option>
								<html:option value="1"><digi:trn key="aim:ipa:popup:planned">Planned</digi:trn></html:option>							
							</html:select>
							&nbsp;
							<html:text indexed="true" name="contractDisbursement" property="amount" onkeyup="fnChk(this)"><digi:trn key="aim:ipa:popup:amount">Amount</digi:trn></html:text>
							&nbsp;
							<html:select name="contractDisbursement" indexed="true" property="currCode" styleClass="inp-text">
								<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
							</html:select>
							&nbsp;
							<html:text readonly="true" size="9" indexed="true" name="contractDisbursement" property="disbDate" styleClass="inp-text" styleId="date${idx.count}"/>
							<a id="image${idx.count}" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","date${idx.count}",-250,-230)'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
							</a>
						</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</logic:notEmpty>						
	</field:display>

	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn>Amendments</digi:trn></b>
	</td></tr>
	<field:display name="Contracting Amendments" feature="Contracting">
	<tr>
		<td colspan="2" align="left">
			<field:display name="Contracting Add Amendments" feature="Contracting">
				<html:button styleClass="dr-menu" property="addamendments" onclick="addAmendment();">
					<digi:trn>Add Amendments</digi:trn>
				</html:button>
			</field:display>
			&nbsp;	
			<field:display name="Contracting Remove Amendments" feature="Contracting">
				<c:if test="${!empty aimIPAContractForm.contractAmendments}">
					<html:button styleClass="dr-menu" property="delamendments" onclick="delAmendment();calculer1();">
						<digi:trn>Remove Amendments</digi:trn>
					</html:button>
				</c:if>			
			</field:display>				
		</td>
	</tr>
	</field:display>
	<field:display name="Contracting Amendments" feature="Contracting">
	<logic:notEmpty name="aimIPAContractForm" property="contractAmendments">
		<tr>
			<td colspan="2">
				<table id="ContractAmendmentsList">
					<c:forEach  items="${aimIPAContractForm.contractAmendments}" var="contractAmendment"  varStatus="idx" >
						<tr>
						<td align="left" colspan="2">
							<html:hidden property="${contractAmendment}" value="${id}"/>
							&nbsp;
							<html:multibox property="selContractAmendments" value="${idx.count}"/>
							<html:text indexed="true" name="contractAmendment" property="amoutStr" onkeyup="fnChk(this);calculer1();"><digi:trn key="aim:ipa:popup:amount">Amount</digi:trn></html:text>
							&nbsp;			
							<html:select indexed="true" name="contractAmendment" disabled="true" property="currCode" styleClass="inp-text">
								<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
							</html:select>
							&nbsp;
							<html:text readonly="true" size="9" indexed="true" name="contractAmendment" property="amendDate" styleClass="inp-text" styleId="date_a${idx.count}"/>
							<a id="image${idx.count}" href='javascript:pickDateByIdDxDyWOScroll("newmyContract","date_a${idx.count}",-250,-230)'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
							</a>
							&nbsp;
							<digi:trn>Reference:</digi:trn>&nbsp;<html:text size="12" indexed="true" name="contractAmendment" property="reference" styleClass="inp-text" />							
						</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</logic:notEmpty>						
	</field:display>
	
	
	<tr>
	<td><br/><br/><br/>
	<table cellpadding="2" cellspacing="2" width="50%">	
		<field:display name="Contracting Amendments" feature="Contracting">
			<tr>
				<td align="left" colspan="2">
					<digi:trn>Montant total du contrat part du bailleur</digi:trn>
				</td>
			</tr>
			<tr>
				<td align="left">
					<html:text property="totAmountDonorContractFunding" style="text-align:right" readonly="true" />
				</td>
				<td align="left">				
					<html:select disabled="true" property="totalAmountCurrencyDonor" styleClass="inp-text">
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
					</html:select>
				</td>
			</tr>			
		</field:display>
	</table>
	</td>
	<td><br/><br/><br/>
	<table cellpadding="2" cellspacing="2" width="50%">	
		<field:display name="Contracting Amendments" feature="Contracting">
			<tr>
				<td align="left" colspan="2">
					<digi:trn>Montant total du contrat comprise la part de l'Etat</digi:trn>
				</td>
			</tr>
			<tr>
				<td align="left">
					<html:text property="totAmountCountryContractFunding" style="text-align:right" />
				</td>
				<td align="left">			
					<html:select disabled="true" property="totalAmountCurrencyCountry" styleClass="inp-text">
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
					</html:select>				
				</td>
			</tr>			
		</field:display>
	</table>
	</td>
	</tr>	
	<tr>
		<td colspan="2" align="center">
			<field:display name="Contracting Save Button" feature="Contracting">
				<html:button property="submit" styleClass="dr-menu" onclick="validate()"><digi:trn key="aim:save">Save</digi:trn></html:button>
			</field:display>
			&nbsp;&nbsp;
			<field:display name="Contracting Cancel Saving" feature="Contracting">
				<html:button styleClass="dr-menu" property="cancel" onclick="hideAddContract()">
					<digi:trn key="aim:addEditActivityCancel">Cancel</digi:trn>
				</html:button>
			</field:display>
		</td>
	</tr>	
</table>
</digi:form>