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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<%@page import="java.math.BigDecimal"%><script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronousSendNotNull.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/jquery-latest.pack.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/jquery.disable.text.select.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
<!--  -->


<div id="myContract" style="display: none">
	<div id="myContractContent" class="content">
		<jsp:include page="/aim/editIPAContract.do?new=true" />
	</div>
</div>


<script type="text/javascript">
		YAHOOAmp.namespace("YAHOOAmp.amptab");
		YAHOOAmp.amptab.init = function() {
		    		var tabView = new YAHOOAmp.widget.TabView('tabview_container');
		};
		
		/*
		YAHOOAmp.amptab.handleClose = function() {
			var wrapper			= document.getElementById('myFilterWrapper');
			var filter			= document.getElementById('myFilter');
			if (filter.parent != null)
					filter.parent.removeChild(filter);
			wrapper.appendChild(filter);
		};
		*/
		    
	    var myPanelIPAContracting = new YAHOOAmp.widget.Panel("newmyContract", {
			width:"730px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"] 
		    }
		     );
	
	//myPanel1.beforeHideEvent.subscribe(YAHOOAmp.amptab.handleClose);
	var panelStart;	    
	function initScripts() {
		var msgIPAPanel='\n<digi:trn key="aim:addeditContracting">Add/Edit Contracting</digi:trn>';
		myPanelIPAContracting.setHeader(msgIPAPanel);
		myPanelIPAContracting.setBody("");
		myPanelIPAContracting.render(document.body);
		panelStart = 0;
	}
	
	function showSave() {
		var element5 = document.getElementById("myContract");
		element5.style.display = "inline";
		if (panelStart < 1){
			myPanelIPAContracting.setBody(element5);
		}
		if (panelStart < 2){
			document.getElementById("myContractContent").scrollTop=0;
			myPanelIPAContracting.show();
			panelStart = 2;
		}
	}
	function hideAddContract() {
		panelStart = 1;
		myPanelIPAContracting.hide();
	}
	
	function resetFilter(){
		if (aimReportsFilterPickerForm.text)
			aimReportsFilterPickerForm.text.value="";

		if (aimReportsFilterPickerForm.selectedDonorGroups)
			aimReportsFilterPickerForm.selectedDonorGroups.selectedIndex=-1;
			
		if (aimReportsFilterPickerForm.governmentApprovalProcedures){
			aimReportsFilterPickerForm.governmentApprovalProcedures[0].checked=false;
			aimReportsFilterPickerForm.governmentApprovalProcedures[1].checked=false;
		}
	}

	window.onload=initScripts();
	
	<logic:present parameter="displayAdd" >
			var current2 = window.onload;
			window.onload = function() {
	            current2.apply(current2);
				showSave();
        	};
	</logic:present>
	
</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myContract .content { 
	    overflow:auto; 
	    height:600px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	
</style>



<script language="JavaScript">
    <!--
    
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
         window.location.reload();
    }
    
    
    function callUrl(indexId) {
	    var async=new Asynchronous();
	    async.complete=mapCallBack;
	    async.call("/aim/editIPAContract.do?deleteEU&indexId="+indexId);
    }
    
    
    
    function addIPAContract() {
        openNewWindow(900, 600);
        <digi:context name="editIPAContract" property="context/module/moduleinstance/editIPAContract.do?new" />
        document.aimEditActivityForm.action = "<%= editIPAContract %>";
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
	    //content.style.visibility = "visible";
		showSave();
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
    
	function addContract(){
        var postString		= "new=true";
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
	}
    
    function editContract(indexId) {
        var postString		= "editEU=true&indexId="+indexId;
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
    }
    
    function deleteContract(indexId) {
        
        <digi:context name="editIPAContract" property="context/module/moduleinstance/deleteIPAContract.do?indexId=" />
        document.aimEditActivityForm.action = "<%=editIPAContract%>"+indexId;
        document.aimEditActivityForm.target = "_self";
        document.aimEditActivityForm.submit();
    }
    
    
  
    function validateForm() {
        return true;
    }
    -->
    </script>


<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">

<html:hidden property="step"/>

<html:hidden property="editAct"/>
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
</c:set>



<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border=0>
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td><jsp:include page="t.jsp"/>
								<span class=crumb>
									<c:set var="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>
									</c:set>
									<c:set var="message">
										<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>
									<c:set var="quote">'</c:set>
									<c:set var="escapedQuote">\'</c:set>
									<c:set var="msg">
										${fn:replace(message,quote,escapedQuote)}
									</c:set>

									<digi:link href="/viewMyDesktop.do" styleClass="comment"  onclick="return quitRnot1('${msg}')" title="${translation}">

										<digi:trn key="aim:portfolio">Portfolio</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								<c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                                   <c:set property="translation" var="trans">
                                       <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                           Click here to goto Add Activity Step ${step.stepActualNumber}
                                       </digi:trn>
                                   </c:set>
                                   <c:set var="link">
										<c:if test="${step.stepNumber==9}">
										  /editSurveyList.do?edit=true
										</c:if>
                                        <c:if test="${step.stepNumber!=9}">
                                        
                                            /addActivity.do?step=${step.stepNumber}&edit=true
                                            
                                        </c:if>
                                   </c:set>
                                   <c:if test="${!index.last}">
                                       <c:if test="${index.first}">
                                           <digi:link href=" ${link}" styleClass="comment" title="${trans}">
                                               <c:if test="${aimEditActivityForm.editAct == true}">
                                                   <digi:trn key="aim:editActivityStep1">
                                                       Edit Activity - Step 1
                                                   </digi:trn>
                                               </c:if>
                                               <c:if test="${aimEditActivityForm.editAct == false}">
                                                   <digi:trn key="aim:addActivityStep1">
                                                       Add Activity - Step 1
                                                   </digi:trn>
                                               </c:if>
                                           </digi:link>
                                           &nbsp;&gt;&nbsp;
                                       </c:if>
                                       <c:if test="${!index.first}">
                                           <digi:link href="${link}" styleClass="comment" title="${trans}">
                                               <digi:trn key="aim:addActivityStep${step.stepActualNumber}">
    	                                           Step ${step.stepActualNumber}
	                                           </digi:trn>
                                           </digi:link>
                                           &nbsp;&gt;&nbsp;
                                       </c:if>
                                   </c:if>
                                   <c:if test="${index.last}">
                                       <c:if test="${index.first}">
                                           <c:if test="${aimEditActivityForm.editAct == true}">
                                               <digi:trn key="aim:editActivityStep1">
                                                   Edit Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           <c:if test="${aimEditActivityForm.editAct == false}">
                                               <digi:trn key="aim:addActivityStep1">
                                                   Add Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                       </c:if>
                                       <c:if test="${!index.first}">
                                           <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
                                       </c:if>
                                   </c:if>
                               </c:forEach>
							   </span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>
									<bean:write name="aimEditActivityForm" property="identification.title"/>
								</c:if>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<digi:errors/>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%" border=0>
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
                                                                                             <digi:trn>
													Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>
                                                                                            ${fn:length(aimEditActivityForm.steps)}:
												 <digi:trn key="aim:stepContracting">IPA Contracting</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td>
							<field:display name="Add IPA Contract" feature="Contracting">
								<div style="width:99.5%;height:18px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
						            <span style="cursor:pointer;float:left;">
						                <html:button styleClass="dr-menu" property="submitButton" onclick="addContract();">
						                	<digi:trn key="aim:addIPAContract">Add IPA Contract</digi:trn>
						                </html:button>
					                </span>
				                </div> 
							</field:display>
                            </td></tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
								<!-- contents -->
								 <logic:notEmpty name="aimEditActivityForm" property="contracts.contracts">
				                      <table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
					                      <c:forEach items="${aimEditActivityForm.contracts.contracts}" var="contract" varStatus="idx">
						                      <tr><td bgColor=#f4f4f2 align="center" vAlign="top">
					                          <table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
					                               <field:display name="Contract Name" feature="Contracting">
					                               <tr>
					                                   <td align="left" width="25%">
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
				                                              <b><digi:trn key="aim:IPA:popup:type">type</digi:trn>:</b>
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
		                                        <field:display name="Contract Organization" feature="Contracting">
			                                         <tr>
			                                            <td align="left" valign="top">
			                                                <b><digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn></b>
			                                            </td>
			                                            <td>
															<c:forEach items="${contract.organizations}" var="org">
																<c:out value="${org.name}"/><br/>
															</c:forEach>			                                                
			                                            </td>
			                                        </tr>	
		                                         </field:display>
		                                        <field:display name="Contracting Contractor Name" feature="Contracting">
			                                         <tr>
			                                            <td align="left">
			                                                 <b><digi:trn key="aim:IPA:newPopup:contractingContractorName">Contractor Name</digi:trn>:</b>
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
		                                         <field:display name="Contracting Status" feature="Contracting">
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
			                                                 <b><digi:trn key="aim:ipa:popup:totalAmount">Total Amount</digi:trn>:</b>
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
			                                                 <b><digi:trn key="aim:ipa:popup:ib">IB</digi:trn>:</b>
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
			                                                 <b><digi:trn key="aim:ipa:popup:inv">INV</digi:trn>:</b>
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
			                                                 <b><digi:trn key="aim:ipa:popup:central">Central</digi:trn>:</b>
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
			                                                 <b><digi:trn key="aim:ipa:popup:regional">Regional</digi:trn>:</b>
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
			                                                  <b><digi:trn key="aim:ipa:popup:ifis">IFIs</digi:trn>:</b>
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
			                                                  <b><digi:trn key="aim:ipa:popup:ib">IB</digi:trn>:</b>
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
			              										&nbsp; ${contract.totalAmountCurrency}
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
		                                          <field:display name="Contracting Disbursements" feature="Contracting">
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
																		<th><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></th>
																		
																	</tr>
			                                                           <c:forEach  items="${contract.disbursements}" var="disbursement" >
			                                                               <tr>
			                                                                   <td align="center" valign="top">
			                                                                       <c:if test="${disbursement.adjustmentType==0}">
		                                                                             <digi:trn key="aim:actual">Actual</digi:trn>
			                                                                       </c:if>
			                                                                       <c:if test="${disbursement.adjustmentType==1}">
		                                                                             <digi:trn key="aim:planned">Planned</digi:trn>
			                                                                       </c:if>
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
		                                                      <logic:notEmpty name="aimEditActivityForm" property="funding">
		                                                           <table width="100%">
																    <tr>
																		<th><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></th>
																		
																	</tr>
			                                                           <c:forEach  items="${aimEditActivityForm.funding.fundingDetails}" var="fundingDetail" >
			                                                           		<logic:equal name="contract" property="contractName" value="${fundingDetail.contract.contractName}">
			                                                           		<c:if test="${fundingDetail.transactionType == 1}">
			                                                               <tr>
			                                                                   <td align="center" valign="top">
			                                                                       <c:if test="${fundingDetail.adjustmentType==0}">
		                                                                             <digi:trn key="aim:actual">Actual</digi:trn>
			                                                                       </c:if>
			                                                                       <c:if test="${fundingDetail.adjustmentType==1}">
		                                                                             <digi:trn key="aim:planned">Planned</digi:trn>
			                                                                       </c:if>
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${fundingDetail.transactionAmount}
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                      ${fundingDetail.currencyCode} 
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${fundingDetail.transactionDate}
			                                                                   </td>
			                                                               </tr>
			                                                               </c:if>
			                                                               </logic:equal>
			                                                           </c:forEach>
		                                                           </table>
		                                                       </logic:notEmpty>						
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
			                                              	<c:if test="${ct.donorContractFundinAmount != null}">			                                              																	
			              										&nbsp; <%=BigDecimal.valueOf(ct.getDonorContractFundinAmount()).toPlainString() %>  &nbsp;&nbsp;&nbsp;&nbsp;${contract.donorContractFundingCurrency.currencyName}
			              									</c:if>
			                                              </td>
			                                          </tr>
		                                          		<tr>
			                                              <td align="left">
			                                                  <b><digi:trn>Montant total du contrat part du bailleur</digi:trn>:</b>
			                                              </td>
			                                              <td>
			                                              	<c:if test="${ct.totAmountDonorContractFunding != null}">
			              										&nbsp; <%=BigDecimal.valueOf(ct.getTotAmountDonorContractFunding()).toPlainString() %> &nbsp;&nbsp;&nbsp;&nbsp;${contract.totalAmountCurrencyDonor.currencyName}
			              									</c:if>
			                                              </td>
			                                          </tr>
		                                          		<tr>
			                                              <td align="left">
			                                                  <b><digi:trn>Montant total du contrat comprise la part de l'Etat</digi:trn>:</b>
			                                              </td>
			                                              <td>
			                                              	<c:if test="${ct.totAmountCountryContractFunding != null}">
			              										&nbsp; <%=BigDecimal.valueOf(ct.getTotAmountCountryContractFunding()).toPlainString() %> &nbsp;&nbsp;&nbsp;&nbsp;${contract.totalAmountCurrencyCountry.currencyName}
			              									</c:if>
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
		                                            <tr><td colspan="2">  
			                                            <field:display name="Edit Contract" feature="Contracting">
			                                            		<a style="cursor:pointer;color:#006699; text-decoration: underline" title="Click to edit the contract" onClick='editContract(${idx.count})'><b><digi:trn key="aim:editThisItem">Edit this item</b></digi:trn></a> 
			                                            </field:display>
			                                           <field:display name="Delete Contract" feature="Contracting">
			                                  				&nbsp;&nbsp;&nbsp; 
			                                  				<a style="cursor:pointer;color:#006699;text-decoration: underline" title="Click to remove the contract" onClick='callUrl(${idx.count})'><b><digi:trn key="aim:deleteThisItem">Delete this item</digi:trn></b></a>
			                                           </field:display>
		                                          	</td></tr>
		                                       </table>
		                                      </td></tr>
		                                   </c:forEach>
	                                   </table>
	                               </logic:notEmpty>
								  <!-- end contents -->
								</td></tr>
                                <tr><td>
                                    &nbsp;
                                </td></tr>
						</table>
						</td>
						<td width="25%" vAlign="top" align="right">
							<!-- edit activity form menu -->
								<jsp:include page="editActivityMenu.jsp" flush="true" />
							<!-- end of activity form menu -->
						</td></tr>
					</table>
				</td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>
