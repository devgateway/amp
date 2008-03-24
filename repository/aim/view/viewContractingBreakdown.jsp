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

<digi:instance property="aimViewContractingForm"/>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=13&surveyFlag=true&activityId=" + id;
}

function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.location.href = "<%=addUrl%>~pageId=2~activityId=" + id;
}


function previewLogframe(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	var url ="<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~logframepr=true~activityId=" + id + "~actId=" + id;
	openURLinWindow(url,650,500);
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}

</script>


<digi:errors />


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
                                                                 </digi:link>&nbsp;&gt;&nbsp;<digi:trn key="aim:actOverview">Overview</digi:trn><logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">&nbsp;&gt;&nbsp;<digi:trn key="aim:costingPerspective">Costing Perspective</digi:trn></logic:equal></SPAN>
								</TD>
								<TD align="right">

											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Preview Activity" module="Previews">
													<field:display feature="Preview Activity" name="Preview Button">
														<input type="button" value='<digi:trn key="aim:preview">Preview</digi:trn>' class="dr-menu" onclick='preview(${aimViewContractingForm.ampActivityId})'>														
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Edit Activity" module="Previews">
													<field:display feature="Edit Activity" name="Edit Activity Button">
														<c:if test="${aimChannelOverviewForm.buttonText != 'validate'}">              
	                                                        <c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}">    
	                                                                <input type="button" value='<digi:trn key="aim:edit">Edit</digi:trn>' class="dr-menu" onclick='fnEditProject(${aimViewContractingForm.ampActivityId})'>
	                                                        </c:if>
	                                                     </c:if> 															
													</field:display>														
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Logframe" module="Previews">
													<field:display name="Logframe Preview Button" feature="Logframe" >
															<input type="button" value='<digi:trn key="aim:previewLogFrame">Preview LogFrame</digi:trn>' class="dr-menu" onclick="previewLogframe(${aimViewContractingForm.ampActivityId})">															
													</field:display>
												</feature:display>
											</module:display>

											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Project Fiche" module="Previews">
													<field:display name="Project Fiche Button" feature="Project Fiche" >
														<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu' onclick='projectFiche(${aimViewContractingForm.ampActivityId})'>
													</field:display>
												</feature:display>
											</module:display>

								</TD>


							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR>
						<TD width="750" bgcolor="#F4F4F2" height="17">
						<TABLE border="0" cellpadding="0" cellspacing="0"
							bgcolor="#F4F4F2" height="17">
							<TR bgcolor="#F4F4F2" height="17">
								<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp; <digi:trn
									key="aim:contracting">Contracting</digi:trn>
								</TD>
								<TD background="module/aim/images/corner-r.gif" height="17"
									width="17"></TD>
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
                                                                                            
                                                                                        <field:display name="Description" feature="Contracting">
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:description">Description:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                               ${contract.description}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                              <field:display name="Activity Category" feature="Contracting">
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
                                                                                    <field:display name="Type" feature="Contracting">
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
                                                                                         
                                                                                              <field:display name="Start of Tendering" feature="Contracting">
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
                                                                                   
                                                                                    <field:display name="Contracting Organization Text" feature="Contracting">
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
                                                                                    <field:display name="Total Amount" feature="Contracting">
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
                                                                                    <field:display name="Total EC Contribution" feature="Contracting">
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                    <field:display name="IB" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IB">IB</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                 ${contract.totalECContribIBAmount}
                                                                                                ${contract.totalECContribIBCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                          <field:display name="INV" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:INV">INV:</digi:trn></b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalECContribINVAmount}
                                                                                               ${contract.totalECContribINVCurrency}
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                        
                                                                                        
                                                                                       <field:display name="Total National Contribution" feature="Contracting">
                                                                                        
                                                                                        
                                                                                        <tr>
                                                                                            <td align="left" colspan="2">
                                                                                                <b><digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                        
                                                                                    <field:display name="Central" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:Central">Central</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribCentralAmount}
                                                                                                ${contract.totalNationalContribCentralCurrency} 
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                              <field:display name="Regional" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:Regional">Regional</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribRegionalAmount} 
                                                                                              ${contract.totalNationalContribRegionalCurrency}
                                                                                   
                                                                                            </td>
                                                                                        </tr>
                                                                                    </field:display>
                                                                                              <field:display name="IFIs" feature="Contracting">
                                                                                        <tr>
                                                                                            <td align="left">
                                                                                                <b><digi:trn key="aim:IPA:popup:IFIs">IFIs</digi:trn>:</b>
                                                                                            </td>
                                                                                            <td>
                                                                                                ${contract.totalNationalContribIFIAmount}
                                                                                               ${contract.totalNationalContribIFICurrency}
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
                                                                                                ${contract.totalPrivateContribCurrency}
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
                                                                                                         <table>
                                                                                              
                                                                                                        <c:forEach  items="${contract.disbursements}" var="disbursement" >
                                                                                                            <tr>
                                                                                          
                                                                                                                <td align="left" valign="top">
                                                                                                                    <c:if test="${disbursement.adjustmentType==0}">
                                                                                                                          <digi:trn key="aim:actual">Actual</digi:trn>
                                                                                                                   </c:if>
                                                                                                                    <c:if test="${disbursement.adjustmentType==1}">
                                                                                                                          <digi:trn key="aim:planned">Planned</digi:trn>
                                                                                                                   </c:if>
                                                                                                    
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                    ${disbursement.amount}
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                   ${disbursement.currency.currencyName} 
                                                                                                                </td>
                                                                                                                <td align="left" valign="top">
                                                                                                                    ${disbursement.disbDate}
                                                                                                                    
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



