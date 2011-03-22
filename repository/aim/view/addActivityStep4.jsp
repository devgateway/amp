
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">

	<!--

	function addRegionalFunding() {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=show" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}

	function editFunding(id) {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=showEdit" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>&fundId="+id;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}

	function removeRegFundings() {
		var flag = validate();
		if (flag == false) return false;
		<digi:context name="rem" property="context/module/moduleinstance/removeRegionalFunding.do?edit=true" />
		document.aimEditActivityForm.action = "<%= rem %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}
     function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }
        function totalsPage() {
       var currency=document.aimEditActivityForm.regFundingPageCurrCode.value;
       var url=addActionToURL('getFundingTotals.do')+'?edit=true&regFundingPageCurrCode='+currency+'&isRegcurr=true'+'&isStepPage=true';
       var async=new Asynchronous();
       async.complete=buildFundingTotalsForPage;
       async.call(url);
    }
       function buildFundingTotalsForPage(status, statusText, responseText, responseXML){
        var root=responseXML.getElementsByTagName('total')[0];
        var comm=document.getElementById("comms");
        var disb=document.getElementById("disb");
        var expn=document.getElementById("expn");
        var compTot=document.getElementById("regional_disb");
        var curr=root.getAttribute("curr");
        var totalComm=root.getAttribute("totalComm");
        comm.innerHTML="<digi:trn> Commitments - (Grand Total Actual Allocation</digi:trn> "+totalComm+" "+curr+")";
        disb.innerHTML="<digi:trn> Disbursement - (Total actual to date</digi:trn> "+root.getAttribute("disb")+' '+curr+')';
        expn.innerHTML="<digi:trn> Expenditure - (Total actual to date</digi:trn>   " +root.getAttribute("expn")+' '+curr+')';
        compTot.innerHTML="<digi:trn>Regional Grand Total Actual Disbursements</digi:trn>  " +root.getAttribute("regional_disb")+' '+curr+')';
    }




if(document.addEventListener){
    document.addEventListener("DOMContentLoaded",totalsPage,false);
}
else{
  if (document.attachEvent){
  window.attachEvent('onload', totalsPage);
}
else {
  document.DOMContentLoaded = totalsPage;
}
  
}

	function validateForm() {
		return true;
	}


	function validate() {
		<c:set var="msgErr">
			<digi:trn key="aim:pleaseChooseRegionalFundingToRemove">Please choose a regional funding to remove</digi:trn>
		</c:set>
		if (document.aimEditActivityForm.elements["funding.selRegFundings"].checked != null) {
			if (document.aimEditActivityForm.elements["funding.selRegFundings"].checked == false) {
				alert("${msgErr}");
				return false;
			}
		} else { // many reg fund present
			var length = document.aimEditActivityForm.elements["funding.selRegFundings"].length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.elements["funding.selRegFundings"][i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("${msgErr}");
				return false;
			}
		}
		return true;
	}
	
	-->

</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">
<html:hidden property="step"/>


<html:hidden property="editAct" />
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
  </c:set>

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
	<tr>
		<td width="100%" vAlign="top" align="left">
		<!--  AMP Admin Logo -->
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
		<!-- End of Logo -->
		</td>
	</tr>
	<tr>
		<td width="100%" vAlign="top" align="left">
			<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
				<tr>
					<td class=r-dotted-lg width="10">&nbsp;</td>
					<td align=left vAlign=top class=r-dotted-lg>
						<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
							<tr><td>
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
									<tr>
										<td>
											<span class=crumb>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
													</c:set>
													<c:set var="message">
													<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
													</c:set>
													<c:set var="quote">'</c:set>
													<c:set var="escapedQuote">\'</c:set>
													<c:set var="msg">
													${fn:replace(message,quote,escapedQuote)}
													</c:set>
													<digi:link href="/viewMyDesktop.do" styleClass="comment"
													onclick="return quitRnot1('${msg}')" title="${translation}" >
														<digi:trn key="aim:portfolio">
															Portfolio
														</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
								<c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                               
                               <c:set property="translation" var="trans">
                                   <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                       Click here to goto Add Activity Step ${step.stepActualNumber}
                                   </digi:trn>
                               </c:set>
                               
                               
                               
                               <c:if test="${!index.last}">
                                   
                                   <c:if test="${index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           
                                           
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
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
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
                                       <digi:trn key="aim:addActivityStep${step.stepActualNumber}"> Step ${step.stepActualNumber}</digi:trn>
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
												<digi:trn key="aim:addNewActivity">
													Add New Activity
												</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.editAct == true}">
												<digi:trn key="aim:editActivity">
													Edit Activity
												</digi:trn>:
													<bean:write name="aimEditActivityForm" property="identification.title"/>
											</c:if>
										</td>
									</tr>
								</table>
							</td></tr>
							<tr> <td>
								<digi:errors/>
							</td></tr>
							<tr><td>
								<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
									<tr><td width="75%" vAlign="top">
										<table cellPadding=0 cellSpacing=0 width="100%" vAlign="top">
											<tr>
												<td width="100%">
													<table cellPadding=0 cellSpacing=0 width="100%" border=0>
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"></td>
															<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
															 <digi:trn>Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>${fn:length(aimEditActivityForm.steps)}:
                                                                                                                         <digi:trn key="aim:activity:RegionalFunding">
                                                                                                                             Regional Funding
                                                                                                                         </digi:trn>
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left"
													bgcolor="#006699">
														<tr>
															<td bgColor=#f4f4f2 align="center" vAlign="top">
																<table width="95%" bgcolor="#f4f4f2">
																	<tr>
																		<td>
																			<IMG alt=Link height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																			<a title="<digi:trn key="aim:regionalFunding">Regional funding</digi:trn>">
																				<b><digi:trn key="aim:regionalFunding">Regional funding</digi:trn></b>
																			</a>
																		</td>
																	</tr>
																	<tr><td>&nbsp;</td></tr>
																	<tr>
																		<td align="left">
																			<table width="100%" cellSpacing=5 cellPadding=0 border=0
																			class="box-border-nopadding">
																			<logic:notEmpty name="aimEditActivityForm" property="funding.regionalFundings">
																					<field:display name="Total Donor Commitments" feature="Regional Funding">
																					<tr>
                                                                                        <td>
                                                                                            &nbsp;&nbsp;<b> <digi:trn>Select currency </digi:trn></b>                                                                                                
                                                                                            <html:select property="regFundingPageCurrCode" styleClass="inp-text" onchange="totalsPage()">
                                                                                                <c:forEach var="currency" items="${aimEditActivityForm.funding.validcurrencies}">
                                                                                                    <c:if test="${currency.currencyCode!=aimEditActivityForm.regFundingPageCurrCode}">
                                                                                                        <option value="<c:out value="${currency.currencyCode}"/>">
                                                                                                    </c:if>
                                                                                                    <c:if test="${currency.currencyCode==aimEditActivityForm.regFundingPageCurrCode}">
                                                                                                        <option value="<c:out value="${currency.currencyCode}"/>" selected="true">
                                                                                                        </c:if>
                                                                                                        <c:out value="${currency.currencyName}" />
                                                                                                    </option>
                                                                                                </c:forEach>
                                                                                                    
                                                                                            </html:select>
                                                                                        </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                        <td>
																							<b>
                                                                                               <span  id="comms">
																							<digi:trn key="aim:donorcommitments">Donor Commitments</digi:trn> - (
																							<digi:trn key="aim:totalActualAllocation">Total actual
																							allocation</digi:trn> =
																							<c:out value="${aimEditActivityForm.funding.totalCommitments}"/>
																							<c:out value="${aimEditActivityForm.currCode}"/>)
                                                                                            </span>
																							</b>
																						</td></tr>
																					</field:display>
																					<field:display name="Total Donor Disbursements" feature="Regional Funding">
																						<tr><td >
																							<b>
                                                                                                <span id="disb">
																							<digi:trn key="aim:donordisbursements">Donor Disbursements</digi:trn> - (
																							<digi:trn key="aim:totalActualToDate">Total actual to date
																							</digi:trn> =
																							<c:out value="${aimEditActivityForm.funding.totalDisbursements}"/>
																							<c:out value="${aimEditActivityForm.currCode}"/>)
                                                                                            </span>
																							</b>
																						</td></tr>
																				</field:display>
																				<field:display name="Total Donor Expenditures" feature="Regional Funding">
																						<tr><td>
																							<b>
                                                                                               <span id="expn">
																							<digi:trn key="aim:expenditures">Expenditures</digi:trn> - (
																							<digi:trn key="aim:totalActualToDate">Total actual to date
																							</digi:trn> =
																							<c:out value="${aimEditActivityForm.funding.totalExpenditures}"/>
																							<c:out value="${aimEditActivityForm.currCode}"/>)
                                                                                               </span>
																							</b>
																						</td></tr>
																				</field:display>
																		<tr><td><b>
																		
																		<font 
																		<c:if test="${aimEditActivityForm.funding.totalCommitmentsDouble < aimEditActivityForm.funding.regionTotalDisb }">
																		 color="RED"
																		</c:if>
																		>
                                                                          <span id="regional_disb">

																		<digi:trn key="aim:totalRegionalActualDisbursement">Regional Grand Total Actual Disbursements</digi:trn>=
																		<c:out value="${aimEditActivityForm.funding.regionTotalDisb}"/>
																		<c:out value="${aimEditActivityForm.currCode}"/>
                                                                        </span>
																		</font></b>
																		</td></tr>


																				<logic:iterate name="aimEditActivityForm" property="funding.regionalFundings"
																				id="regionalFunding"
																				type="org.digijava.module.aim.helper.RegionalFunding"> <!-- L1 START-->
																				<tr><td>
																					<!-- Region name -->
																					<html:multibox property="funding.selRegFundings">
																						<bean:write name="regionalFunding" property="regionId"/>
																					</html:multibox>
																					<bean:write name="regionalFunding" property="regionName"/>
																					&nbsp;
																					<field:display name="Edit Funding Link" feature="Regional Funding">
																					<a href="javascript:editFunding('
																						<bean:write name="regionalFunding" property="regionId"/>
																					')"><digi:trn key="aim:editThisFunding">Edit this funding</digi:trn></a>
																					</field:display>
																				</td></tr>
																				<tr><td>
																					<!-- Regional funding details -->
																					<table width="100%" cellSpacing=1 cellPadding=3 border=0
																					bgcolor="#d7eafd">
																					<logic:notEmpty name="regionalFunding" property="commitments">

																						<tr><td>
																							<b>
																							<digi:trn key="aim:donorcommitments">Donor Commitments</digi:trn>
																							<field:display name="Total Donor Commitments" feature="Regional Funding">
																							 - (
																							<digi:trn key="aim:totalActualAllocation">Total actual
																							allocation</digi:trn> =
																							<c:out value="${aimEditActivityForm.funding.totalCommitments}"/>
																							<c:out value="${aimEditActivityForm.currCode}"/>)
																							</field:display>
																							</b>
																						</td></tr>
																						<tr><td bgcolor=#ffffff>
																							<table width="100%" cellSpacing=1 cellPadding=3 border=0
																							bgcolor="#eeeeee">
																								<tr>
																									<field:display name="Actual/Planned Commitments" feature="Regional Funding"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																									<field:display name="Total Amount Commitments" feature="Regional Funding"><td><digi:trn key="aim:totalAmount">Total Amount</digi:trn></td></field:display>
																									<field:display name="Currency Commitments" feature="Regional Funding"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																									<field:display name="Date Commitments" feature="Regional Funding"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>
																								</tr>
																								<logic:iterate name="regionalFunding"
																								property="commitments" id="commitment"
																								type="org.digijava.module.aim.helper.FundingDetail">
																								<!-- L2 START-->
																								<tr bgcolor="#ffffff">
																									<field:display name="Actual/Planned Commitments" feature="Regional Funding">
                                                                                                   		<td><digi:trn key="aim:${commitment.adjustmentTypeName}"><c:out value="${commitment.adjustmentTypeName}"/></digi:trn></td>
                                                                                                    </field:display>
																									<field:display name="Total Amount Commitments" feature="Regional Funding"><td>
																									<FONT color=blue>*</FONT>
																									<c:out value="${commitment.transactionAmount}"/></td></field:display>
																									<field:display name="Currency Commitments" feature="Regional Funding"><td><c:out value="${commitment.currencyCode}"/></td></field:display>
																									<field:display name="Date Commitments" feature="Regional Funding"><td><c:out value="${commitment.transactionDate}"/></td></field:display>																									
																								</tr>
																								</logic:iterate>	<!-- L2 END-->
																							</table>
																						</td></tr>
																					</logic:notEmpty>
																					<logic:notEmpty name="regionalFunding" property="disbursements">
																					<tr><td>
																							<b>
																							<digi:trn key="aim:donordisbursements">Donor Disbursements</digi:trn>
																							<field:display name="Total Donor Disbursements" feature="Regional Funding">
																							 - (
																							<digi:trn key="aim:totalActualToDate">Total actual to date
																							</digi:trn> =
																							<c:out value="${aimEditActivityForm.funding.totalDisbursements}"/>
																							<c:out value="${aimEditActivityForm.currCode}"/>)
																							</field:display>
																							</b>
																						</td></tr>
																						<tr><td bgcolor=#ffffff>
																							<table width="100%" cellSpacing=1 cellPadding=3 border=0
																							bgcolor="#eeeeee">
																								<tr>
																									<field:display name="Actual/Planned Disbursements" feature="Regional Funding"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																									<field:display name="Total Amount Disbursements" feature="Regional Funding"><td><digi:trn key="aim:totalAmount">Total Amount</digi:trn></td></field:display>
																									<field:display name="Currency Disbursements" feature="Regional Funding"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																									<field:display name="Date Disbursements" feature="Regional Funding"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>																									
																								</tr>
																								<logic:iterate name="regionalFunding"
																								property="disbursements" id="disbursement"
																								type="org.digijava.module.aim.helper.FundingDetail">
																								<!-- L3 START-->
																								<tr bgcolor="#ffffff">
																									<field:display name="Actual/Planned Disbursements" feature="Regional Funding"><td><digi:trn key="aim:${disbursement.adjustmentTypeName}"><c:out value="${disbursement.adjustmentTypeName}"/></digi:trn>
																									</td></field:display>
																									<field:display name="Total Amount Disbursements" feature="Regional Funding"><td>
																									<FONT color=blue>*</FONT>
																									<c:out value="${disbursement.transactionAmount}"/>
																									</td></field:display>
																									<field:display name="Currency Disbursements" feature="Regional Funding"><td><c:out value="${disbursement.currencyCode}"/></td></field:display>
																									<field:display name="Date Disbursements" feature="Regional Funding"><td><c:out value="${disbursement.transactionDate}"/></td></field:display>																									
																								</tr>
																								</logic:iterate>	<!-- L3 END-->
																							</table>
																						</td></tr>
																					</logic:notEmpty>
																					<logic:notEmpty name="regionalFunding" property="expenditures">
																						<tr><td>
																							<b>
																							<digi:trn key="aim:expenditures">Expenditures</digi:trn>
																							<field:display name="Total Donor Expenditures" feature="Regional Funding">
																							 - (
																							<digi:trn key="aim:totalActualToDate">Total actual to date
																							</digi:trn> =
																							<c:out value="${aimEditActivityForm.funding.totalExpenditures}"/>
																							<c:out value="${aimEditActivityForm.currCode}"/>)
																							</field:display>
																							</b>
																						</td></tr>
																						<tr><td bgcolor=#ffffff>
																							<table width="100%" cellSpacing=1 cellPadding=3 border=0
																							bgcolor="#eeeeee">
																								<tr>
																									<field:display name="Actual/Planned Expenditures" feature="Regional Funding"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																									<field:display name="Total Amount Expenditures" feature="Regional Funding"><td><digi:trn key="aim:totalAmount">Total Amount</digi:trn></td></field:display>
																									<field:display name="Currency Expenditures" feature="Regional Funding"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																									<field:display name="Date Expenditures" feature="Regional Funding"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>																									
																								</tr>
																								<logic:iterate name="regionalFunding"
																								property="expenditures" id="expenditure"
																								type="org.digijava.module.aim.helper.FundingDetail">
																								<!-- L4 START-->
																								<tr bgcolor="#ffffff">
																									<field:display name="Actual/Planned Expenditures" feature="Regional Funding">
                                                                                                    <td><digi:trn key="aim:${expenditure.adjustmentTypeName}"><c:out value="${expenditure.adjustmentTypeName}"/></digi:trn>
																									</td></field:display>
																									<field:display name="Total Amount Expenditures" feature="Regional Funding"><td>
																									<FONT color=blue>*</FONT>
																									<c:out value="${expenditure.transactionAmount}"/>
																									</td></field:display>
																									<field:display name="Currency Expenditures" feature="Regional Funding"><td><c:out value="${expenditure.currencyCode}"/></td></field:display>
																									<field:display name="Date Expenditures" feature="Regional Funding"><td><c:out value="${expenditure.transactionDate}"/></td></field:display>																									
																								</tr>
																								</logic:iterate>	<!-- L4 END-->
																							</table>
																						</td></tr>
																					</logic:notEmpty>
																					</table>
																				</td></tr>
																				</logic:iterate>	<!-- L1 END-->
																				<TR><TD>
																				<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
																					<FONT color=blue>*
																						<digi:trn key="aim:allTheAmountsInThousands">
																							All the amounts are in thousands (000)
				  																		</digi:trn>
																					</FONT>
																				</gs:test>
																				</TD></TR>
																			</logic:notEmpty>
																			<logic:notEmpty name="aimEditActivityForm" property="funding.fundingRegions">
																			<logic:empty name="aimEditActivityForm" property="funding.regionalFundings">
																				<!-- No fundings -->
																				<field:display name="Add Regional Fundings" feature="Regional Funding">
																				<tr><td>
																					<html:button  styleClass="dr-menu" property="submitButton" onclick="addRegionalFunding()">
																							<digi:trn key="btn:addFundings">Add Fundings</digi:trn>
																					</html:button>

																				</td></tr>
																				</field:display>
																			</logic:empty>
																			<logic:notEmpty name="aimEditActivityForm" property="funding.regionalFundings">
																				<tr><td bgcolor=#ffffff>
																					<table cellSpacing=2 cellPadding=2>
																						<tr>
																						<field:display name="Add Regional Fundings" feature="Regional Funding">
																							<td>
																								<html:button  styleClass="dr-menu" property="submitButton" onclick="addRegionalFunding()">
																									<digi:trn key="btn:addFundings">Add Fundings</digi:trn>
																								</html:button>
																							</td>
																						</field:display>
																						<field:display name="Remove Fundings" feature="Regional Funding">
																							<td>
																								<html:button  styleClass="dr-menu" property="submitButton" onclick="removeRegFundings()">
																									<digi:trn key="btn:removeFundings">Remove Fundings</digi:trn>
																								</html:button>
																							</td>
																						</field:display>
																						</tr>
																					</table>
																				</td></tr>
																			</logic:notEmpty>
																			</logic:notEmpty>
																			<logic:empty name="aimEditActivityForm" property="funding.fundingRegions">
																				<tr><td align="center" class="red-log">
																					<digi:trn key="aim:noRegionsSelected">No regions selected
																					</digi:trn>
																				</td></tr>
																			</logic:empty>
																			</table>
																		</td>
																	</tr>

<!--
																	<tr><td bgColor=#f4f4f2 align="center">
																		<table cellPadding=3>
																			<tr>
																				<td>
																					<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(3)">
																							<< <digi:trn key="btn:back">Back</digi:trn>
																					</html:submit>

																				</td>
																				<td>
																					<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(5)">
																							<digi:trn key="btn:next">Next</digi:trn> >>
																					</html:submit>
																				</td>
																				<td>
																					<html:reset  styleClass="dr-menu" property="submitButton" onclick="return resetAll()">
																							<digi:trn key="btn:reset">Reset</digi:trn>
																					</html:reset>
																				</td>
																			</tr>
																		</table>
																	</td></tr>
 -->
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
									<td width="25%" vAlign="top" align="right">
										<!-- edit activity form menu -->
											<jsp:include page="editActivityMenu.jsp" flush="true" />
										<!-- end of activity form menu -->
									</td></tr>
								</table>
							</td></tr>
							<tr><td>&nbsp;
								
							</td></tr>
						</table>
					</td>
					<td width="10">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
