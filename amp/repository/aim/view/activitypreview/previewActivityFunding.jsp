<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.digijava.module.aim.helper.ChartGenerator"%>
<%@ page import="java.io.PrintWriter, java.util.*"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@ taglib uri="/taglib/aim" prefix="aim"%>
<div id="donor_fund">
	<table width="95%" cellspacing="1" cellpadding="0" border="0" align="center">
				<tr>
					<td>
					<table width="100%" border="0" align="right" cellpadding="0" cellSpacing=8 class="">
						<logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
							<logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">

								<logic:notEmpty name="fundingOrganization" property="fundings">
									<logic:iterate name="fundingOrganization" indexId="index" property="fundings" id="funding" type="org.digijava.module.aim.helper.Funding">
										<tr>
											<td>
											<table cellspacing="1" cellpadding="0" border="0" width="100%">
												<%--<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification" 
													     parentModule="/Activity Form/Funding/Funding Group/Funding Item">  --%>
												<tr>
													<td>
													<table cellspacing="1" cellpadding="0" border="0" width="100%">
														<tr>
															<td>
															<table width="100%" border="0" cellpadding="0" bgcolor="#dddddd" cellspacing="1" style="font-size:11px;">
<%--														<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Organization Id"
															 parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification"> --%>

																<logic:notEmpty name="funding" property="orgFundingId">
																	<tr>
																		<td align="left" width="150">
																			<a title='<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>'>
																				<digi:trn>Funding Organization Id</digi:trn>
																			</a>																		
																		</td>
																		<td width="1">:</td>
																		<td align="left">
																			<b><bean:write name="funding" property="orgFundingId"/></b>
																		</td>
																	</tr>
																</logic:notEmpty>
																<%-- </module:display>  --%>
																<%-- <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Donor Organisation" 
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item"> --%>
																<tr>
																	<td align="left" width="150">
																		<a title='<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>'>
																			<digi:trn key="aim:OrgName">Organization Name</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td align="left">
																		<b>${fundingOrganization.orgName}</b>
																	</td>
																</tr>
																<logic:present name="funding" property="sourceRole">
																<tr>
																	<td align="left" width="150">
																		<a title='<digi:trn key="aim:orgRole">Organization Role</digi:trn>'>
																			<digi:trn key="aim:OrgRole">Organization Role</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td align="left">
																			<b><digi:trn><bean:write name="funding" property="sourceRole"/></digi:trn></b>
																	</td>
																</tr>
																</logic:present>
																
																<%-- </module:display> --%>
																<!-- type of assistance -->
																<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Type of Assistence"
																	    parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>'>
																		<digi:trn key="aim:typeOfAssist">Type of Assistance</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																		<logic:notEmpty name="funding" property="typeOfAssistance">
																			<b><digi:trn><bean:write name="funding" property="typeOfAssistance.value"/></digi:trn></b>
																		</logic:notEmpty></td>
																	</tr>
																</module:display>
																
																<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Financing Instrument"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<logic:notEmpty name="funding" property="financingInstrument">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>'>
																		<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																		
																			<b><digi:trn><bean:write name="funding" property="financingInstrument.value"/></digi:trn></b>
																		</td>
																	</tr>
																	</logic:notEmpty>
																</module:display>
																<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Status"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn>Funding Status</digi:trn>'>
																		<digi:trn>Funding Status</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																			<logic:notEmpty name="funding" property="fundingStatus">
																				<b><digi:trn><bean:write name="funding" property="fundingStatus.value" /></digi:trn></b>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</module:display>
																<logic:notEmpty name="funding" property="modeOfPayment">
																	<tr>
																		<td align="left" width="150"><a
																			title='<digi:trn>Mode of Payment</digi:trn>'>
																		<digi:trn>Mode of Payment</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td align="left">
																			<logic:notEmpty name="funding" property="modeOfPayment">
																				<b><digi:trn><bean:write name="funding" property="modeOfPayment.value"/></digi:trn></b>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</logic:notEmpty>
																
																<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
																<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Donor Objective" parentModule="/Activity Form/Funding/Funding Group/Funding Item">
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

                                                                <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Conditions" parentModule="/Activity Form/Funding/Funding Group/Funding Item">
                                                                    <tr>
                                                                        <td align="left" width="150"><a
                                                                                title='<digi:trn key="aim:donorobjective">Conditions</digi:trn>'>
                                                                            <digi:trn key="aim:donorobjective">Conditions</digi:trn>
                                                                        </a></td>
                                                                        <td width="1">:</td>
                                                                        <td align="left">
                                                                            <b><bean:write name="funding" property="conditions" /></b>
                                                                        </td>
                                                                    </tr>
                                                                </module:display>

                                                                <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td align="left" width="150">
																			<a title='<digi:trn>Agreement Title</digi:trn>'>
																				<digi:trn>Agreement Title</digi:trn>
																			</a>
																		</td>
																		<td width="1">:</td>
																		<td align="left">
																			<b>${funding.title}</b>
																		</td>
																	</tr>
																	<tr>
																		<td align="left" width="150">
																			<a title='<digi:trn>Agreement Code</digi:trn>'>
																				<digi:trn>Agreement Code</digi:trn>
																			</a>
																		</td>
																		<td width="1">:</td>
																		<td align="left">
																			<b>${funding.code}</b>
																		</td>
																	</tr>
																</module:display>
																<logic:notEmpty name="funding" property="fundingClassificationDate">
																<tr>
																	<td align="left" width="150">
																		<a title='<digi:trn>Funding Classification Date</digi:trn>'>
																			<digi:trn>Funding Classification Date</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td align="left">
																			<b><bean:write name="funding" property="fundingClassificationDate"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
															</table>
															</td>
														</tr>
													</table>
													</td>
												</tr>
												<%-- </module:display> --%>

												<tr>
												<td>
												<!-- Begin funding detail -->
												<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" style="font-size:11px;">
													<bean:define id="funding" name="funding" scope="page" toScope="request"
														type="org.digijava.module.aim.helper.Funding">
													</bean:define>
													<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Commitments" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingCommitments.jsp" />
													</module:display>
													<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements" 
							 							parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingDisbursement.jsp" />
													</module:display>

													<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingExpenditures.jsp" />
													</module:display>

                                                    <module:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections"
                                                            parentModule="/Activity Form/Funding/Funding Group/Funding Item">
                                                        <jsp:include page="previewActivityFundingMTEF.jsp" />
                                                    </module:display>

 												    <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Release of Funds"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingRoF.jsp" />
													</module:display>

													<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingEDD.jsp" />
													</module:display>

                                                    <%-- Do not display disbursement orders for now --%>
                                                    <%--module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders"
                                                        parentModule="/Activity Form/Funding/Funding Group/Funding Item">
                                                        <jsp:include page="previewActivityFundingDisbursementOrders.jsp" />
                                                    </module:display--%>

													<feature:display module="Funding" name="Undisbursed Balance">
													    <jsp:include page="previewActivityFundingUndisbursedBalance.jsp" />
													</feature:display>
												</table>

												<!-- end funding detail --></td>
												</tr>
												<tr>

													<td bgcolor="#ffffff" style="font-size:11px; color:#000000;">
														<FONT color='blue'>
															<jsp:include page="../utils/amountUnitsUnformatted.jsp">
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
                        	<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Commitments" 
									parentModule="/Activity Form/Funding/Funding Group/Funding Item">
							<c:if test="${aimEditActivityForm.funding.showPlanned}">
							<c:if test="${not empty aimEditActivityForm.funding.totalPlannedCommitments}">							
	                        <tr>
	                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">
	                            	<digi:trn key='aim:totalplannedcommittment'>Total Planned Commitments</digi:trn>:
	                            </td>
	                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">
		                                <b>
		                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedCommitments" /> 
		                                <digi:currency code="${aimEditActivityForm.currCode}"/>
		                                </b>
		                             &nbsp;
                        		</td>
	                        </tr>
	                        </c:if>
							</c:if>
	                    <c:if test="${aimEditActivityForm.funding.showActual}">
	                    <c:if test="${not empty aimEditActivityForm.funding.totalCommitments}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"> <digi:trn
                                key='aim:totalactualcommittment'>Total Actual Commitments</digi:trn> :
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">
                                
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalCommitments"/> 
	                                <digi:currency code="${aimEditActivityForm.currCode}"/>
		                           
	                                </b>
	                        	&nbsp;        
	                        </td>
                        </tr>
                        </c:if>
                        </c:if>
                        <c:if test="${aimEditActivityForm.funding.showPipeline}">
                        <c:if test="${not empty aimEditActivityForm.funding.totalPipelineCommitments}">
	                        <field:display name="Pipeline" feature="Commitments">
								<tr>
		                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
		                            	<digi:trn key='aim:totalactualcommittment'> Total Pipeline Commitments</digi:trn>:
		                            </td>
		                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">		                                
		                                     <b>
		                                     <bean:write name="aimEditActivityForm" property="funding.totalPipelineCommitments" /> 
	                                         <digi:currency code="${aimEditActivityForm.currCode}"/>
		                       			    </b>
		                                &nbsp;        
		                            </td>
		                        </tr>
	                        </field:display>
                        </c:if>
                        </c:if>
                        </module:display>
                        <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
						<c:if test="${aimEditActivityForm.funding.showPlanned}">
						<c:if test="${not empty aimEditActivityForm.funding.totalPlannedDisbursements}">
                        <tr>
                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                            	<digi:trn key='aim:totalplanneddisbursement'>
                                Total Planned Disbursement	
                                </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">                                
	                               <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedDisbursements" /> 
                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                           </b>
                                &nbsp;
                             </td>
                        </tr>
                        </c:if>
                        </c:if>
                        <c:if test="${aimEditActivityForm.funding.showActual}">
                        <c:if test="${not empty aimEditActivityForm.funding.totalDisbursements}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                                <digi:trn key='aim:totalActualdisbursement'>Total Actual Disbursement</digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">                                
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalDisbursements" /> 
                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                            </b>
                                &nbsp;
                             </td>
                        </tr>
                        </c:if>
                        </c:if>
                        </module:display>
                         <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
						<c:if test="${aimEditActivityForm.funding.showPlanned}">
						<c:if test="${not empty aimEditActivityForm.funding.totalPlannedExpenditures}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                                <digi:trn
                                	key="aim:totalActualExpenditures">Total Planned Expenditures</digi:trn>: 
                             </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">                                
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedExpenditures" /> 
                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                           </b>
                                &nbsp;
                             </td>
                        </tr>
                        </c:if>
                        </c:if>
                        <c:if test="${aimEditActivityForm.funding.showActual}">
                        <c:if test="${not empty aimEditActivityForm.funding.totalExpenditures}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"><digi:trn
                                key="aim:totalplannedExpenditures">Total Actual Expenditures</digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalExpenditures" /> 
                                    <digi:currency code="${aimEditActivityForm.currCode}"/>
		                            </b>
                                &nbsp;
                           </td>
                        </tr>
                        </c:if>
                        </c:if>
                        </module:display>

                        <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders"
								parentModule="/Activity Form/Funding/Funding Group/Funding Item">
						<c:if test="${not empty aimEditActivityForm.funding.totalActualDisbursementsOrders}">
                            <tr>
                                <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">

                                    <a title='<digi:trn> Release of funds to,
                                    or the purchase of goods or services for a recipient; by
                                    extension, the amount thus spent. Disbursements record the actual
                                    international transfer of financial resources, or of goods or
                                    services valued at the cost to the donor</digi:trn>'>
                                <digi:trn>Total Actual Disbursment Orders</digi:trn></a>:
                                </td>
                                <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                    style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">
                                    
                                        <b>
                                        <bean:write name="aimEditActivityForm" property="funding.totalActualDisbursementsOrders" />
                                         <digi:currency code="${aimEditActivityForm.currCode}"/>
		                        	   </b>
                                    &nbsp;
                                </td>
                            </tr>
                         </c:if>   
                        </module:display>

 			<!--  RELEASE OF FUNDS START -->
                        <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Release of Funds"
								parentModule="/Activity Form/Funding/Funding Group/Funding Item">
							<c:if test="${aimEditActivityForm.funding.showPlanned}">
							<c:if test="${not empty aimEditActivityForm.funding.totalPlannedRoF}">
                                <tr>
                                    <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">
                                        <digi:trn>Total Planned Release of Funds</digi:trn>:
                                    </td>
                                    <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">                                         
										<b>
											<bean:write name="aimEditActivityForm" property="funding.totalPlannedRoF" />
										     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                        		</b>
                                         &nbsp;
                                    </td>
                                </tr>
	                        </c:if>
							</c:if>	                        
                            <c:if test="${aimEditActivityForm.funding.showActual}">
                            <c:if test="${not empty aimEditActivityForm.funding.totalActualRoF}">
                                <tr>
                                    <td bgcolor="#eeeeee"
                                        style="border-top: 1px solid #BBBBBB; text-transform: uppercase"> <digi:trn>Total Actual Release of Funds</digi:trn> :
                                    </td>
                                    <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                        style="border-top: 1px solid #BBBBBB">                                        
                                            <b>
                                                <bean:write name="aimEditActivityForm" property="funding.totalActualRoF"/>
                               	                 <digi:currency code="${aimEditActivityForm.currCode}"/>
		                                    </b>
                                        &nbsp;
                                    </td>
                                </tr>
                            </c:if>
                            </c:if>
                        <%--
                        <c:if test="${aimEditActivityForm.funding.showPipeline}">
	                        <field:display name="Pipeline" feature="Commitments">
								<tr>
		                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
		                            	<digi:trn> Total Pipeline Release of Funds</digi:trn>:
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
	                        </field:display>
                        </c:if>--%>
                        </module:display>
			<!--  RELEASE OF FUNDS END -->
 
  			<!--  EDD START -->
                
							<c:if test="${aimEditActivityForm.funding.showPlanned}">
							<c:if test="${not empty aimEditActivityForm.funding.totalPlannedEDD}">
	                        <tr>
	                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase;">
	                            	<digi:trn>Total Planned EDD</digi:trn>:
	                            </td>
	                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">	                                 
									<b>
		                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedEDD" /> 
	                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                            </b>		                             
									&nbsp;
                        		</td>
	                        </tr>
	                        </c:if>
	                        </c:if>
	                    <c:if test="${aimEditActivityForm.funding.showActual}">
	                    <c:if test="${not empty aimEditActivityForm.funding.totalActualEDD}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"> 
                                <digi:trn>Total Actual EDD</digi:trn> :
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB">                                
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalActualEDD"/> 
                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                            </b>
	                        	&nbsp;
	                        </td>
                        </tr>
                        </c:if>
                        </c:if>
	                    <c:if test="${aimEditActivityForm.funding.showOfficialDevelopmentAid}">
	                    <c:if test="${not empty aimEditActivityForm.funding.totalOdaSscCommitments}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"> 
                                <digi:trn>Total Official Development Aid SSC</digi:trn> :
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">                                
								<b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalOdaSscCommitments"/> 
                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                       </b>
	                        	&nbsp;
	                        </td>
                        </tr>
                        </c:if>
                        </c:if>
	                    <c:if test="${aimEditActivityForm.funding.showBilateralSsc}">
	                    <c:if test="${not empty aimEditActivityForm.funding.totalBilateralSscCommitments}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"> 
                                <digi:trn>Total Bilateral SSC</digi:trn> :
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">                                
								<b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalBilateralSscCommitments"/> 
                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                       </b>
	                        	&nbsp;
	                        </td>
                        </tr>
                        </c:if>
                        </c:if>
                                                
	                    <c:if test="${aimEditActivityForm.funding.showTriangularSsc}">
	                    <c:if test="${not empty aimEditActivityForm.funding.totalTriangularSscCommitments}">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"> 
                                <digi:trn>Total Triangular SSC</digi:trn> :
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">                                
								<b>
	                                <bean:write name="aimEditActivityForm" property="funding.totalTriangularSscCommitments"/> 
                                     <digi:currency code="${aimEditActivityForm.currCode}"/>
		                       </b>
	                        	&nbsp;
	                        </td>
                        </tr>
                        </c:if>
                        </c:if>                        
                                                
                        <%--
                        <c:if test="${aimEditActivityForm.funding.showPipeline}">
	                        <field:display name="Pipeline" feature="Commitments">
								<tr>
		                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
		                            	<digi:trn key='aim:totalactualcommittment'> Total Pipeline Commitments</digi:trn>:
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
	                        </field:display>
                        </c:if> --%>                      
			<!--  EDD END -->
 
                      	
 						<%-- activity-global undisbursed balance --%>
                        <feature:display module="Funding" name="Undisbursed Balance">
                        <c:if test="${not empty aimEditActivityForm.funding.unDisbursementsBalance}">
                      	<tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #BBBBBB; text-transform: uppercase"> 
                                <digi:trn key="aim:undisbursedBalance"> Undisbursed Balance</digi:trn>:
                             </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">                                
	                                <b>
	                                <bean:write name="aimEditActivityForm" property="funding.unDisbursementsBalance" /> 
                                    <digi:currency code="${aimEditActivityForm.currCode}"/>
		                           	</b>
                                &nbsp;
                            </td>
                        </tr>
                        </c:if>
                        </feature:display>
                        <module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
							<c:if test="${not empty aimEditActivityForm.funding.consumptionRate}">														
							<tr>
	                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
	                                <digi:trn key="aim:undisbursedBalance"> Consumption Rate</digi:trn>: </td>
	                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">
	                                	<b>${aimEditActivityForm.funding.consumptionRate}</b>	                                
	                                &nbsp;
	                            </td>
	                        </tr>
	                        </c:if>
	                     </module:display>
	                     <c:if test="${not empty aimEditActivityForm.funding.deliveryRate}">
                         <tr>
                            <td bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB; text-transform: uppercase">
                                <digi:trn>Delivery Rate</digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #BBBBBB">                                
                                <b>${aimEditActivityForm.funding.deliveryRate}</b>
                                &nbsp;
                            </td>
                        </tr>
						</c:if>                        
                        </table>
                        </td>
                        </tr>

						</logic:notEmpty>
					</table>
					</td>
				</tr>
			</table>
</div>			
