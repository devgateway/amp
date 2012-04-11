<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.module.aim.form.FinancingBreakdownForm"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<%@page import="java.math.BigDecimal"%><digi:instance property="aimViewContractingForm"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=13&surveyFlag=true&activityId=" + id;
}


YAHOOAmp.namespace("YAHOOAmp.amp");

var myPanel = new YAHOOAmp.widget.Panel("myPreview", {
	width:"940px",
	fixedcenter: true,
    constraintoviewport: false,
    underlay:"none",
    close:true,
    visible:false,
    modal:true,
    draggable:true,
    context: ["showbtn", "tl", "bl"]
    });
var panelStart=0;

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
	var content = document.getElementById("myContentContent");
	//response = response.split("<!")[0];
	content.innerHTML = response;
	//content.style.visibility = "visible";
	
	showContent();
}

function showPanelLoading(msg){
	   var content = document.getElementById("myContentContent");
	   content.innerHTML = "<div style='text-align: center'>" + "Loading..." +
	   "... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";   
	   showContent();
	 }
	 
var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
	//alert("Connection Failure!"); 
}  
var callback = 
{ 
	success:responseSuccess, 
	failure:responseFailure 
};

function showContent(){
	var element = document.getElementById("myContent");
	element.style.display = "inline";
	if (panelStart < 1){
		myPanel.setBody(element);
	}
	if (panelStart < 2){
		document.getElementById("myContent").scrollTop=0;
		myPanel.show();
		panelStart = 2;
	}
}

function preview(id)
{
	showPanelLoading();
	var postString="&pageId=2&activityId=" + id+"&isPreview=2&previewPopin=true";
	//alert(postString);
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreviewPopin.do" />
	var url = "<%=addUrl %>?"+postString;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	
}

function initPopin() {
	var msg='\n<digi:trn>Activity Preview</digi:trn>';
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

window.onload=initPopin();

function viewChanges(id){
	openNewWindow(650,200);
	<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />
	popupPointer.document.location.href = "<%= showLog %>?activityId=" + id;
}

function expandAll() {
   
	$("img[id$='_minus']").show();
	$("img[id$='_plus']").hide();	
	$("div[id$='_dots']").hide();
	$("div[id^='act_']").show('fast');
}

function collapseAll() {

	$("img[id$='_minus']").hide();
	$("img[id$='_plus']").show();	
	$("div[id$='_dots']").show();
	$("div[id^='act_']").hide();
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}

</script>


<digi:errors />

<div id="myContent" style="display: none;">
	<div id="myContentContent" class="content" style="overflow: scroll; height: 500px;">
	</div>
</div>
<digi:context name="digiContext" property="context" />


<TABLE cellSpacing="0" cellPadding="0" align="center" vAlign="top"
	border="0" width="100%">
	<TR>
		<TD vAlign="top" align="center"><!-- contents -->
		<TABLE width="99%" cellSpacing="0" cellPadding="0" vAlign="top"
			align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR>
				<TD bgcolor="#f4f4f4">
				<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top"
					align="center" bgcolor="#f4f4f4">
					<TR bgColor="#f4f4f2">
						<TD align="left">
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left"
							vAlign="top">
							<TR>
								<TD align="left"><SPAN class="crumb"> <jsp:useBean
									id="urlContrcatingBreakdown" type="java.util.Map"
									class="java.util.HashMap" /> <c:set
									target="${urlContrcatingBreakdown}" property="ampActivityId">
									<bean:write name="aimMainProjectDetailsForm" property="ampActivityId" />
								</c:set> <c:set target="${urlContrcatingBreakdown}"
									property="tabIndex" value="9" /> <c:set var="translation">
									<digi:trn key="aim:clickToViewContracting">Click here to view Contracting</digi:trn>
								</c:set> <digi:link href="/viewProjectCostsBreakdown.do"
									name="urlContrcatingBreakdown" styleClass="comment"
									title="${translation}">
									<digi:trn key="aim:contracting">Contracting</digi:trn>
                                                                 </digi:link>&nbsp;&gt;&nbsp;<digi:trn key="aim:actOverview">Overview</digi:trn></SPAN>
								</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top"
							align="center" bgcolor="#f4f4f4">
							<TR bgColor="#f4f4f2">
								<td>
                                                                                         
                                                                         	<!-- IPA Contracting -->
                                                                                
									
										<!-- contents -->
								 <logic:notEmpty name="aimViewContractingForm" property="contracts">
                                                                                <table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
                                                                                <c:forEach items="${aimViewContractingForm.contracts}" var="contract" varStatus="idx">
                                                                                
                                                                                <tr><td bgColor=#f4f4f2 align="center" vAlign="top">
                                                                                
                                                                                    <table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
                                                                                        
                                                                                      <field:display name="Contract Name" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:name">Contract name:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                
                                                                                                ${contract.contractName}
                                                                                             
                                                                                               
                                                                                            </td>
                                                                                            
                                                                                        </tr>
                                                                                        </field:display>
                                                                                            
                                                                                        <field:display name="Contract Description" feature="Contracting">
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:description">Description:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                               ${contract.description}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                              <field:display name="Contracting Activity Category" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                  <c:if test ="${not empty contract.activityCategory}">
                                                                                                    ${contract.activityCategory.value}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                    <field:display name="Contracting Type" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:Type">Type</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                  <c:if test ="${not empty contract.type}">
                                                                                                    ${contract.type.value}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                         
                                                                                              <field:display name="Contracting Start of Tendering" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:startOfTendering">Start of Tendering:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.formattedStartOfTendering}
                                                                                           </td>
                                                                                            
                                                                                        </tr>	
                                                                                    </field:display>
                                                                                    <field:display name="Contract Validity Date" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractValidityDate">Contract Validity Date</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.formattedContractValidity}
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                    </field:display>
                                                                                              <field:display name="Signature of Contract" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.formattedSignatureOfContract}
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                    </field:display>
                                                                                    <field:display name="Contract Organization" feature="Contracting">
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:if test="${not empty contract.organization}">
                                                                                                     ${contract.organization.name}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                    </field:display>
                                                                                   
                                                                                    <field:display name="Contracting Contractor Name" feature="Contracting">
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractOrg">Contract Organization</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                     ${contract.contractingOrganizationText}
                                                                                                
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                    </field:display>
                                                                                   
                                                                                        
                                                                                              <field:display name="Contract Completion" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.formattedContractCompletion}
                                                                                            </td>
                                                                                            
                                                                                        </tr>	
                                                                                    </field:display>
                                                                                              <field:display name="Contracting Tab Status" feature="Contracting">
                                                                                         <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:status">Status:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                
                                                                                                <c:if test ="${not empty contract.status}">
                                                                                 
                                                                                                    ${contract.status.value}
                                                                                                </c:if>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                    <field:display name="Contracting Total Amount" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalAmount">Total Amount</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.totalAmount}
                                                                                                ${contract.totalAmountCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                    <field:display name="Contract Total Value" feature="Contracting">
												                                         <tr>
												                                             <td align="left">
												                                                 <b><digi:trn key="aim:ipa:popup:contractTotalValue">Contract Total Value</digi:trn>:</b>
												                                             </td>
												                                             <td>
												                                                 ${contract.contractTotalValue}
												                                                 ${contract.totalAmountCurrency} 
												                                             </td>
												                                         </tr>
											                                         </field:display>
                                                                                    <field:display name="Total EC Contribution" feature="Contracting">
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                    <field:display name="Contracting IB" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IB">IB</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.totalECContribIBAmount}
                                                                                                ${contract.totalAmountCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                          <field:display name="Contracting INV" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:INV">INV:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalECContribINVAmount}
                                                                                               ${contract.totalAmountCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                        
                                                                                        
                                                                                       <field:display name="Contracting Total National Contribution" feature="Contracting">
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                        
                                                                                    <field:display name="Contracting Central Amount" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:Central">Central</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribCentralAmount}
                                                                                                ${contract.totalAmountCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                              <field:display name="Contracting Regional Amount" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:Regional">Regional</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribRegionalAmount} 
                                                                                              ${contract.totalAmountCurrency}
                                                                                   
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                              <field:display name="Contracting IFIs" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IFIs">IFIs</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribIFIAmount}
                                                                                               ${contract.totalAmountCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                        
                                                                                    <field:display name="Total Private Contribution" feature="Contracting">
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                   
                                                                                
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IB">IB:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalPrivateContribAmount}
                                                                                                ${contract.totalAmountCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                    <field:display name="Total Disbursements of Contract" feature="Contracting">
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalDisbursements">Total Disbursements</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                            									${contract.totalDisbursements} &nbsp; 
                                                            									<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
                                                            										&nbsp; ${aimViewContractingForm.currCode}
                                                            									</logic:empty>
                                                            									<logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
                                                            										&nbsp; ${contract.dibusrsementsGlobalCurrency}
                                                            									</logic:notEmpty>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>    
                                                                                    <field:display name="Contract Execution Rate" feature="Contracting">
                                                                                
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                            									&nbsp; ${contract.executionRate}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>    
                                                                                   
                                                                                    
											                                      <field:display name="Total Funding Disbursements of Contract" feature="Contracting">
											                                          <tr>
											                                              <td align="left">
											                                                  <b><digi:trn key="aim:IPA:popup:totalFundingDisbursements">Total Funding Disbursements</digi:trn>:</b>
											                                              </td>
											                                              <td>
											              									${contract.fundingTotalDisbursements} &nbsp;
											              									<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
											              										&nbsp; ${contract.totalAmountCurrency}
											              									</logic:empty>
											              									<logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
											              										&nbsp; ${contract.dibusrsementsGlobalCurrency}
											              									</logic:notEmpty>
											                                              </td>
											                                          </tr>
											                                      </field:display>
											                                      <field:display name="Contract Funding Execution Rate" feature="Contracting">
											                                          <tr>
											                                              <td align="left">
											                                                  <b><digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:</b>
											                                              </td>
											                                              <td>
											              										&nbsp; ${contract.fundingExecutionRate}
											                                              </td>
											                                          </tr>
											                                      </field:display> 
                                                                                        
                                                                                    <field:display name="Disbursements" feature="Contracting">
                                                                                        <tr>
                                                                                    
                                                                                            <td colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                         
                                                                                        <tr>
                                                                                            <td>&nbsp;
                                                                                            </td>
                                                                                            <td>
                                                                                               
                                                                                    
                                                                                                    <logic:notEmpty name="contract" property="disbursements">
                                                                                                         <table width="100%">
                                                                                              					<tr>
																													<td><b><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></b></td>
																													<td><b><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></b></td>
																													<td><b><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></b></td>
																													<td><b><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></b></td>
																													
																												</tr>
	                                                                                                        <c:forEach  items="${contract.disbursements}" var="disbursement" >
	                                                                                                            <tr>
	                                                                                          
	                                                                                                                <td align="center" valign="top">
	                                                                                                                 <digi:trn key="aim:actual">${disbursement.adjustmentTypeName.value}</digi:trn>
<%-- 	                                                                                                                    <c:if test="${disbursement.adjustmentType==0}"> --%>
<%-- 	                                                                                                                          <digi:trn key="aim:actual">Actual</digi:trn> --%>
<%-- 	                                                                                                                   </c:if> --%>
<%-- 	                                                                                                                    <c:if test="${disbursement.adjustmentType==1}"> --%>
<%-- 	                                                                                                                          <digi:trn key="aim:planned">Planned</digi:trn> --%>
<%-- 	                                                                                                                   </c:if> --%>
	                                                                                                    
	                                                                                                                </td>
	                                                                                                                <td align="center" valign="top">
	                                                                                                                    ${disbursement.amount}
	                                                                                                                </td>
	                                                                                                                <td align="center" valign="top">
	                                                                                                                   ${disbursement.currency.currencyName} 
	                                                                                                                </td>
	                                                                                                                <td align="center" valign="top">
	                                                                                                                    ${disbursement.disbDate}
	                                                                                                                </td>
	                                                                                                            </tr>
	                                                                                                        </c:forEach>
                                                                                                        </table>
                                                                                                        
                                                                                                    </logic:notEmpty>						
                                                                                                		
                                                                                            </td>		
                                                                                        </tr>
                                                                                    </field:display>
                                                                                    <field:display name="Contracting Funding Disbursements" feature="Contracting">
											                                          <tr>
											                                              <td colspan="2">
											                                                  <b><digi:trn key="aim:IPA:popup:fundingDisbursements">Funding Disbursements:</digi:trn></b>
											                                              </td>
											                                          </tr>
											                                          <tr>
											                                              <td>&nbsp;
											                                              </td>
											                                              <td>
										                                                     <table width="100%">
																							    <tr>
																									<td><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></td>
																									<td><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></td>
																									<td><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></td>
																									<td><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></td>
																									
																								</tr>
										                                                           <c:forEach  items="${aimViewContractingForm.fundingDetailsLinked}" var="fundingDetail" >
										                                                           		<logic:equal name="contract" property="contractName" value="${fundingDetail.contract.contractName}">
										                                                           		<c:if test="${fundingDetail.transactionType == 1}">
										                                                               <tr>
										                                                                   <td align="center" valign="top">
										                                                                   <digi:trn key="aim:actual">${disbursement.adjustmentTypeName.value}</digi:trn>
<%-- 										                                                                       <c:if test="${fundingDetail.adjustmentType==0}"> --%>
<%-- 									                                                                             <digi:trn key="aim:actual">Actual</digi:trn> --%>
<%-- 										                                                                       </c:if> --%>
<%-- 										                                                                       <c:if test="${fundingDetail.adjustmentType==1}"> --%>
<%-- 									                                                                             <digi:trn key="aim:planned">Planned</digi:trn> --%>
<%-- 										                                                                       </c:if> --%>
										                                                                   </td>
										                                                                   <td align="center" valign="top">
										                                                                       ${fundingDetail.transactionAmount}
										                                                                   </td>
										                                                                   <td align="center" valign="top">
										                                                                      ${fundingDetail.ampCurrencyId.currencyCode} 
										                                                                   </td>
										                                                                   <td align="center" valign="top">
										                                                                       ${fundingDetail.transactionDate}
										                                                                   </td>
										                                                               </tr>
										                                                               </c:if>
										                                                               </logic:equal>
										                                                           </c:forEach>
									                                                           </table>
											                                               </td>		
											                                           </tr>
										                                            </field:display>
										                                            <field:display name="Contracting Amendments" feature="Contracting">
		                                          		<bean:define id="ct" name="contract" type="org.digijava.module.aim.dbentity.IPAContract"/>
		                                          		<tr>
			                                              <td align="left">
			                                                  <b><digi:trn key="aim:IPA:newPopup:donorContractFundinAmount">Part du contrat financé par le bailleur</digi:trn>:</b>
			                                              </td>
			                                              <td>			                                              																	
			              										&nbsp; <%=BigDecimal.valueOf(ct.getDonorContractFundinAmount()).toPlainString() %>  &nbsp;&nbsp;&nbsp;&nbsp;${contract.donorContractFundingCurrency.currencyName}
			                                              </td>
			                                          </tr>
		                                          		<tr>
			                                              <td align="left">
			                                                  <b><digi:trn>Montant total du contrat part du bailleur</digi:trn>:</b>
			                                              </td>
			                                              <td>
			              										&nbsp; <%=BigDecimal.valueOf(ct.getTotAmountDonorContractFunding()).toPlainString() %> &nbsp;&nbsp;&nbsp;&nbsp;${contract.totalAmountCurrencyDonor.currencyName}
			                                              </td>
			                                          </tr>
		                                          		<tr>
			                                              <td align="left">
			                                                  <b><digi:trn>Montant total du contrat comprise la part de l'Etat</digi:trn>:</b>
			                                              </td>
			                                              <td>
			              										&nbsp; <%=BigDecimal.valueOf(ct.getTotAmountCountryContractFunding()).toPlainString() %> &nbsp;&nbsp;&nbsp;&nbsp;${contract.totalAmountCurrencyCountry.currencyName}
			                                              </td>
			                                          </tr>
			                                          <tr>
			                                              <td colspan="2">
			                                                  <b><digi:trn>Amendments :</digi:trn></b>
			                                              </td>
			                                          </tr>
			                                          <tr>
			                                              <td>&nbsp;
			                                              </td>
			                                              <td>
		                                                      <logic:notEmpty name="contract" property="amendments">
		                                                      
		                                                           <table width="100%">
																    <tr>
																		<th><digi:trn>Amount</digi:trn></th>
																		<th><digi:trn>Currency</digi:trn></th>
																		<th><digi:trn>Date</digi:trn></th>
																		<th><digi:trn>Reference</digi:trn></th>																		
																	</tr>
			                                                           <c:forEach  items="${contract.amendments}" var="amendment" >
			                                                           <bean:define id="am" name="amendment" type="org.digijava.module.aim.dbentity.IPAContractAmendment"/>
			                                                               <tr>
			                                                                   <td align="center" valign="top">
			                                                                    ${amendment.amoutStr}
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${amendment.currency.currencyName}
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                      ${amendment.amendDate} 
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${amendment.reference}
			                                                                   </td>
			                                                               </tr>
			                                                           </c:forEach>
		                                                           </table>
		                                                       </logic:notEmpty>						
			                                               </td>		
			                                           </tr>
		                                            </field:display>
                                                                                    </table>
                                                                             
                                                                                   </td></tr>
                                                                           
                                                                                  
                                                                                </c:forEach>
                                                                                
                                                                                
		                                          
                                                                                </table>
                                                                                
                                                                            </logic:notEmpty>
								<!-- end contents -->
                                                                 </td>
							</TR>
						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
</TABLE>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>