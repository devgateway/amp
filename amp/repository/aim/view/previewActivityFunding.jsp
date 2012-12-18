<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.digijava.module.aim.helper.ChartGenerator"%>
<%@ page import="java.io.PrintWriter, java.util.*"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@ taglib uri="/taglib/aim" prefix="aim"%>
<div id="donor_fund">
	<table width="95%" cellspacing="1" cellpadding="0" border="0" align="center">
				<tr>
					<td>
					<table width="100%" border="0" align="right" cellpadding="0"
						cellSpacing=8 class="">
						<logic:notEmpty name="aimEditActivityForm"
							property="funding.fundingOrganizations">
							<logic:iterate name="aimEditActivityForm"
								property="funding.fundingOrganizations" id="fundingOrganization"
								type="org.digijava.module.aim.helper.FundingOrganization">

								<logic:notEmpty name="fundingOrganization" property="fundings">
									<logic:iterate name="fundingOrganization" indexId="index"
										property="fundings" id="funding"
										type="org.digijava.module.aim.helper.Funding">
										<tr>
											<td>
											<table cellspacing="1" cellpadding="0" border="0" width="100%">
												<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification" 
													parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
												<tr>
													<td>
													<table cellspacing="1" cellpadding="0" border="0" width="100%">
														<tr>
															<td>
															<table width="100%" border="0" cellpadding="0" bgcolor="#dddddd" cellspacing="1" style="font-size:11px;">
																<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification/Funding Organization Id"
																	parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification">
																<tr>
																	<td align="left" width="150">
																		<a title='<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>'>
																			<digi:trn key="aim:fundingOrgId">Funding Organization Id</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td align="left">
																		<b><bean:write name="funding" property="orgFundingId"/></b>
																	</td>
																</tr>
																</module:display>
																<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Donor Organisation" 
																	parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
																<tr>
																	<td align="left" width="150">
																		<a title='<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>'>
																			<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td align="left">
																		<b>${fundingOrganization.orgName}</b>
																	</td>
																</tr>
																</module:display>
																<!-- type of assistance -->
																<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification/Type of Assistence"
																	parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>'>
																		<digi:trn key="aim:typeOfAssist">Type of Assistance </digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																		<logic:notEmpty name="funding" property="typeOfAssistance">
																			<b><bean:write name="funding" property="typeOfAssistance.value"/></b>
																		</logic:notEmpty></td>
																	</tr>
																</module:display>
																
																<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification/Financing Instrument"
																	parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>'>
																		<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																		<logic:notEmpty name="funding" property="financingInstrument">
																			<b><bean:write name="funding" property="financingInstrument.value"/></b>
																		</logic:notEmpty></td>
																	</tr>
																</module:display>
																<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification/Funding Status"
																	parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn>Funding Status</digi:trn>'>
																		<digi:trn>Funding Status</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																			<logic:notEmpty name="funding" property="fundingStatus">
																				<b><bean:write name="funding" property="fundingStatus.value" /></b>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</module:display>
																<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification/Mode of Payment"
																	parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn>Mode of Payment</digi:trn>'>
																		<digi:trn>Mode of Payment</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																			<logic:notEmpty name="funding" property="modeOfPayment">
																				<b><bean:write name="funding" property="modeOfPayment.value"/></b>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</module:display>
																
																<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
																<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Donor Objective" parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn key="aim:donorobjective">Donor Objective</digi:trn>'>
																		<digi:trn key="aim:donorobjective">Donor Objective</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																			<b><bean:write name="funding" property="donorObjective" /></b>
																		</td>
																	</tr>
																</module:display>
															</table>
															</td>
														</tr>
													</table>
													</td>
												</tr>
												</module:display>
												<tr>
												<td>
												<!-- Begin funding detail -->
												<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" style="font-size:11px;">
													<bean:define id="funding" name="funding" scope="page" toScope="request"
														type="org.digijava.module.aim.helper.Funding">
													</bean:define>
													<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Commitments" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingCommitments.jsp" />
													</module:display>
													<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Disbursements" 
							 							parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingDisbursement.jsp" />
													</module:display>

													<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Expenditures" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingExpenditures.jsp" />
													</module:display>

													<feature:display module="Funding" name="Undisbursed Balance">
													<jsp:include page="previewActivityFundingUndisbursedBalance.jsp" />
													</feature:display>
												</table>
												
												<!-- end funding detail --></td>
												</tr>
												<tr>

													<td bgcolor="#ffffff" style="font-size:11px; color:#000000;">
														<FONT color='blue'>
															<jsp:include page="utils/amountUnitsUnformatted.jsp">
																<jsp:param value="* " name="amount_prefix"/>
															</jsp:include>	
														</FONT>
													</td>
												</tr>
											</table>
											
											<br><br>
											</td>
										</tr>
									</logic:iterate>
								</logic:notEmpty>
							</logic:iterate>
							<tr>
								<td>&nbsp;</td>
							</tr>
                        
                    <tr>
                        <td>
                        <table cellspacing="1" cellpadding="0" border="0" style="font-size:11px;" width="100%">
                        	<module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Commitments" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
							<c:if test="${aimEditActivityForm.funding.showPlanned}">
	                        <tr>
	                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">
	                            	<digi:trn key='aim:totalplannedcommittment'> TOTAL PLANNED COMMITMENTS </digi:trn>:
	                            </td>
	                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">
	                                 <c:if test="${not empty aimEditActivityForm.funding.totalPlannedCommitments}">
		                                <b>
		                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedCommitments" /> 
		                                <bean:write name="aimEditActivityForm" property="currCode" />
		                                </b>
		                             </c:if>
		                             &nbsp;
                        		</td>
	                        </tr>
	                        </c:if>
	                    <c:if test="${aimEditActivityForm.funding.showActual}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"><digi:trn
                                key='aim:totalactualcommittment'> TOTAL ACTUAL COMMITMENTS </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">
                                <c:if test="${not empty aimEditActivityForm.funding.totalCommitments}">
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalCommitments"/> 
	                                <bean:write name="aimEditActivityForm" property="currCode"/>
	                                </b>
	                        	</c:if>&nbsp;        
	                        </td>
                        </tr>
                        </c:if>
                        <c:if test="${aimEditActivityForm.funding.showPipeline}">
						<tr>
                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase"><digi:trn
                                key='aim:totalactualcommittment'> TOTAL PIPELINE COMMITMENTS </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">
                                <c:if test="${not empty aimEditActivityForm.funding.totalPipelineCommitments}">
                                     <b>
                                     <bean:write name="aimEditActivityForm" property="funding.totalPipelineCommitments" /> 
                                     <bean:write name="aimEditActivityForm" property="currCode" />
                                     </b>
                                </c:if>&nbsp;        
                            </td>
                        </tr>
                        </c:if>
                        </module:display>
                        <module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Disbursements" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
						<c:if test="${aimEditActivityForm.funding.showPlanned}">
                        <tr>
                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                            	<digi:trn key='aim:totalplanneddisbursement'>
                                TOTAL PLANNED DISBURSEMENT	
                                </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">
                                <c:if test="${not empty aimEditActivityForm.funding.totalPlannedDisbursements}">
	                               <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedDisbursements" /> 
	                                <bean:write name="aimEditActivityForm" property="currCode" />
	                               </b>
                                </c:if> &nbsp;
                             </td>
                        </tr>
                        </c:if>
                        <c:if test="${aimEditActivityForm.funding.showActual}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB"><digi:trn
                                key='aim:totalActualdisbursement'>
                                TOTAL ACTUAL DISBURSEMENT </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">
                                <c:if test="${not empty aimEditActivityForm.funding.totalDisbursements}">
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalDisbursements" /> 
	                                <bean:write name="aimEditActivityForm" property="currCode" />
	                                </b>
                                </c:if>&nbsp;
                             </td>
                        </tr>
                        </c:if>
                        </module:display>
                         <module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Expenditures" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
						<c:if test="${aimEditActivityForm.funding.showPlanned}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"><digi:trn
                                key="aim:totalActualExpenditures">
                                  TOTAL PLANNED EXPENDITURES             </digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">
                                <c:if test="${not empty aimEditActivityForm.funding.totalPlannedExpenditures}">
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedExpenditures" /> 
	                                <bean:write name="aimEditActivityForm" property="currCode" />
	                                </b>
                                </c:if>&nbsp;
                             </td>
                        </tr>
                        </c:if>
                        <c:if test="${aimEditActivityForm.funding.showActual}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"><digi:trn
                                key="aim:totalplannedExpenditures">
                                    TOTAL ACTUAL EXPENDITURES       	 	 </digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">
                                
                                <c:if test="${not empty aimEditActivityForm.funding.totalExpenditures}">
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalExpenditures" /> 
	                                <bean:write name="aimEditActivityForm" property="currCode" />
	                                </b>
                                </c:if>&nbsp;
                           </td>
                        </tr>
                        </c:if>
                        </module:display>
                          <module:display name="/Activity Form/Donor Funding/Funding Group/Funding Item/Disbursement Orders" 
														parentModule="/Activity Form/Donor Funding/Funding Group/Funding Item">
   
                        <tr>
                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">
                        <digi:trn
                                key='aim:totalActualDisbursementOrder'>
                                  <a
                                title='<digi:trn key="aim:FundRelease"> Release of funds to,
                                or the purchase of goods or services for a recipient; by
                                extension, the amount thus spent. Disbursements record the actual
                                international transfer of financial resources, or of goods or
                                services valued at the cost to the donor</digi:trn>'>
                          TOTAL ACTUAL DISBURSMENT ORDERS </a></digi:trn>: 
                        </td>
                          <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">
                                <c:if test="${not empty aimEditActivityForm.funding.totalActualDisbursementsOrders}">
		                        	<b>
		                        	<bean:write name="aimEditActivityForm" property="funding.totalActualDisbursementsOrders" />	
		                            <bean:write name="aimEditActivityForm" property="currCode" />
		                           	</b>
                                </c:if> &nbsp;
                           </td>
                      	</tr>
                        </module:display>
                        <feature:display module="Funding" name="Undisbursed Balance">
                      	<tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                                <digi:trn key="aim:undisbursedBalance"> UNDISBURSED BALANCE</digi:trn>:
                             </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">
                                <c:if test="${not empty aimEditActivityForm.funding.unDisbursementsBalance}">
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.unDisbursementsBalance" /> 
	                                <bean:write name="aimEditActivityForm" property="currCode" />
	                               	</b>
                                </c:if>&nbsp;
                            </td>
                        </tr>
                        </feature:display>
                       <tr>
                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                                <digi:trn key="aim:undisbursedBalance"> CONSUMPTION RATE</digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">
                            	 <c:if test="${not empty aimEditActivityForm.funding.consumptionRate}">
                                	<b>${aimEditActivityForm.funding.consumptionRate}</b>
                                </c:if>
                                &nbsp;
                            </td>
                        </tr>
                         <tr>
                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                                <digi:trn>DELIVERY RATE</digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">
                                <c:if test="${not empty aimEditActivityForm.funding.consumptionRate}">
                                <b>${aimEditActivityForm.funding.deliveryRate}</b>
                                </c:if>
                                &nbsp;
                            </td>
                        </tr>
                        </table>
                        </td>
                        </tr>

						</logic:notEmpty>
					</table>
					</td>
				</tr>
			</table>
</div>			
