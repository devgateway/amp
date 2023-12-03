<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.digijava.ampModule.aim.helper.ChartGenerator"%>
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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@ taglib uri="/taglib/aim" prefix="aim"%>
<div id="donor_fund">
	<table width="95%" cellspacing="1" cellpadding="0" border="0" align="center">
				<tr>
					<td>
					<table width="100%" border="0" cellpadding="0" cellSpacing=8 class="">
						<logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
							<logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.ampModule.aim.helper.FundingOrganization">

								<logic:notEmpty name="fundingOrganization" property="fundings">
									<logic:iterate name="fundingOrganization" indexId="index" property="fundings" id="funding" type="org.digijava.ampModule.aim.helper.Funding">
										<tr>
											<td>
											<table cellspacing="1" cellpadding="0" border="0" width="100%">
												<%--<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification"
													     parentModule="/Activity Form/Funding/Funding Group/Funding Item">  --%>
												<tr>
													<td>
													<table cellspacing="1" cellpadding="0" border="0" width="100%">
														<tr>
															<td>
															<table width="100%" border="0" cellpadding="0" bgcolor="#dddddd" cellspacing="1" style="font-size:11px;">
<%--														<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Organization Id"
															 parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification"> --%>

																<logic:notEmpty name="funding" property="orgFundingId">
																	<tr>
																		<td width="150">
																			<a title='<digi:trn key="aim:FundOrgId" jsFriendly="true">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>'>
																				<digi:trn>Funding Organization Id</digi:trn>
																			</a>																		
																		</td>
																		<td width="1">:</td>
																		<td>
																			<b><bean:write name="funding" property="orgFundingId"/></b>
																		</td>
																	</tr>
																</logic:notEmpty>
																<%-- </ampModule:display>  --%>
																<%-- <ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Donor Organisation"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item"> --%>
																<tr>
																	<td width="150">
																		<a title='<digi:trn key="aim:fundOrgName" jsFriendly="true">Funding Organization Name</digi:trn>'>
																			<digi:trn key="aim:OrgName">Organization Name</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																		<b>${fundingOrganization.orgName}</b>
																	</td>
																</tr>
																<logic:present name="funding" property="sourceRole">
																<tr>
																	<td width="150">
																		<a title='<digi:trn key="aim:orgRole" jsFriendly="true">Organization Role</digi:trn>'>
																			<digi:trn key="aim:OrgRole">Organization Role</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><digi:trn><bean:write name="funding" property="sourceRole"/></digi:trn></b>
																	</td>
																</tr>
																</logic:present>
																
																<%-- </ampModule:display> --%>
																<!-- type of assistance -->
																<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Type of Assistence"
																	    parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td width="150"><a
																			title='<digi:trn key="aim:AssitanceType" jsFriendly="true">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>'>
																		<digi:trn key="aim:typeOfAssist">Type of Assistance</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td>
																		<logic:notEmpty name="funding" property="typeOfAssistance">
																			<b><digi:trn><bean:write name="funding" property="typeOfAssistance.value"/></digi:trn></b>
																		</logic:notEmpty></td>
																	</tr>
																</ampModule:display>
																
																<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Financing Instrument"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<logic:notEmpty name="funding" property="financingInstrument">
																	<tr>
																		<td width="150"><a
																			title='<digi:trn key="aim:financialInst" jsFriendly="true">Financing Instrument</digi:trn>'>
																		<digi:trn key="aim:financialInst">Financing Instrument</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td>
																		
																			<b><digi:trn><bean:write name="funding" property="financingInstrument.value"/></digi:trn></b>
																		</td>
																	</tr>
																	</logic:notEmpty>
																</ampModule:display>
																<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Status"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td width="150"><a
																			title='<digi:trn jsFriendly="true">Funding Status</digi:trn>'>
																		<digi:trn>Funding Status</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td>
																			<logic:notEmpty name="funding" property="fundingStatus">
																				<b><digi:trn><bean:write name="funding" property="fundingStatus.value" /></digi:trn></b>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</ampModule:display>
																<logic:notEmpty name="funding" property="modeOfPayment">
																	<tr>
																		<td width="150"><a
																			title='<digi:trn jsFriendly="true">Mode of Payment</digi:trn>'>
																		<digi:trn>Mode of Payment</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td>
																			<logic:notEmpty name="funding" property="modeOfPayment">
																				<b><digi:trn><bean:write name="funding" property="modeOfPayment.value"/></digi:trn></b>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</logic:notEmpty>
																
																<logic:notEmpty name="funding" property="concessionalityLevel">
																	<tr>
																		<td width="150"><a
																			title='<digi:trn jsFriendly="true">Concessionality Leve</digi:trn>'>
																		<digi:trn>Concessionality Level</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td>
																			<logic:notEmpty name="funding" property="concessionalityLevel">
																				<b><digi:trn><bean:write name="funding" property="concessionalityLevel.value"/></digi:trn></b>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</logic:notEmpty>
																
																<logic:notEmpty name="funding" property="ratificationDate">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Ratification Date</digi:trn>'>
																			<digi:trn>Ratification Date</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="ratificationDate"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																<logic:notEmpty name="funding" property="maturity">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Maturity</digi:trn>'>
																			<digi:trn>Maturity</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="maturity"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																<logic:notEmpty name="funding" property="interestRate">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Interest Rate</digi:trn>'>
																			<digi:trn>Interest Rate</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="interestRate"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																<logic:notEmpty name="funding" property="gracePeriod">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Grace Period</digi:trn>'>
																			<digi:trn>Grace Period</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="gracePeriod"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																
																<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
																<logic:notEmpty name="funding" property="donorObjective">
																<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Donor Objective" parentModule="/Activity Form/Funding/Funding Group/Funding Item">
																	<tr>
																		<td width="150"><a
																			title='<digi:trn jsFriendly="true" key="aim:donorobjective">Donor Objective</digi:trn>'>
																		<digi:trn key="aim:donorobjective">Donor Objective</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td>
																			<span class="word_break bold"><bean:write name="funding" property="donorObjective" /></span>
																		</td>
																	</tr>
																</ampModule:display>
																</logic:notEmpty>
                                                             <logic:notEmpty name="funding" property="conditions">
                                                                <ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Conditions" parentModule="/Activity Form/Funding/Funding Group/Funding Item">
                                                                    <tr>
                                                                        <td width="150"><a
                                                                                title='<digi:trn jsFriendly="true" key="aim:donorobjective">Conditions</digi:trn>'>
                                                                            <digi:trn key="aim:donorobjective">Conditions</digi:trn>
                                                                        </a></td>
                                                                        <td width="1">:</td>
                                                                        <td>
                                                                            <span class="word_break bold"><bean:write name="funding" property="conditions" /></span>
                                                                        </td>
                                                                    </tr>
                                                                </ampModule:display>
                                                              </logic:notEmpty>
                                                                <ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																	<tr>
																		<td width="150">
																			<a title='<digi:trn jsFriendly="true">Agreement Title</digi:trn>'>
																				<digi:trn>Agreement Title</digi:trn>
																			</a>
																		</td>
																		<td width="1">:</td>
																		<td>
																			<b>${funding.title}</b>
																		</td>
																	</tr>
																	<tr>
																		<td width="150">
																			<a title='<digi:trn jsFriendly="true">Agreement Code</digi:trn>'>
																				<digi:trn>Agreement Code</digi:trn>
																			</a>
																		</td>
																		<td width="1">:</td>
																		<td>
																			<b>${funding.code}</b>
																		</td>
																	</tr>
																</ampModule:display>
																<logic:notEmpty name="funding" property="fundingClassificationDate">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Funding Classification Date</digi:trn>'>
																			<digi:trn>Funding Classification Date</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="fundingClassificationDate"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Effective Funding Date"
																	parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																<logic:notEmpty name="funding" property="effectiveFundingDate">
																	<tr>
																		<td width="150">
																			<a title='<digi:trn jsFriendly="true">Effective Funding Date</digi:trn>'>
																				<digi:trn>Effective Funding Date</digi:trn>
																			</a>
																		</td>
																		<td width="1">:</td>
																		<td>
																			<b><bean:write name="funding" property="effectiveFundingDate"/></b>
																		</td>
																	</tr>
																</logic:notEmpty>
																</ampModule:display>
																<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Funding Closing Date"
																		parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																<logic:notEmpty name="funding" property="fundingClosingDate">
																	<tr>
																		<td width="150">
																			<a title='<digi:trn jsFriendly="true">Funding Closing Date</digi:trn>'>
																				<digi:trn>Funding Closing Date</digi:trn>
																			</a>
																		</td>
																		<td width="1">:</td>
																		<td>
																			<b><bean:write name="funding" property="fundingClosingDate"/></b>
																		</td>
																	</tr>
																</logic:notEmpty>
																</ampModule:display>
															</table>
															</td>
														</tr>
													</table>
													</td>
												</tr>
												<%-- </ampModule:display> --%>

												<tr>
												<td>
												<!-- Begin funding detail -->
												<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" style="font-size:11px;">
													<bean:define id="funding" name="funding" scope="page" toScope="request"
														type="org.digijava.ampModule.aim.helper.Funding">
														</bean:define>
													<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Commitments"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<bean:define id="transaction" value="Commitments" type="java.lang.String" toScope="request"/>
														<jsp:include page="previewActivityFundingCommitments.jsp" />
													</ampModule:display>
													<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements"
							 							parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<bean:define id="transaction" value="Disbursements" type="java.lang.String" toScope="request"/>
														<jsp:include page="previewActivityFundingDisbursement.jsp" />
													</ampModule:display>
													<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<bean:define id="transaction" value="Expenditures" type="java.lang.String" toScope="request"/>
														<jsp:include page="previewActivityFundingExpenditures.jsp" />
													</ampModule:display>
                                                    <ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections"
                                                            parentModule="/Activity Form/Funding/Funding Group/Funding Item">
                                                        <jsp:include page="previewActivityFundingMTEF.jsp" />
                                                    </ampModule:display>
 												    <ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Release of Funds"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingRoF.jsp" />
													</ampModule:display>
													<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<jsp:include page="previewActivityFundingEDD.jsp" />
													</ampModule:display>
													<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Arrears"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
														<bean:define id="transaction" value="Arrears" type="java.lang.String" toScope="request"/>
														<jsp:include page="previewActivityFundingArrears.jsp" />
													</ampModule:display>

                                                    <%-- Do not display disbursement orders for now --%>
                                                    <%--ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders"
                                                        parentModule="/Activity Form/Funding/Funding Group/Funding Item">
                                                        <jsp:include page="previewActivityFundingDisbursementOrders.jsp" />
                                                    </ampModule:display--%>

													<feature:display ampModule="Funding" name="Undisbursed Balance">
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
                        	<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Commitments"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item">
								<c:if test="${aimEditActivityForm.funding.showPlanned}"><c:if test="${not empty aimEditActivityForm.funding.totalPlannedCommitments}">
									<c:set var="activity_funding_text"><digi:trn key='aim:totalplannedcommittment'>Total Planned Commitments</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPlannedCommitments}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                        	</c:if></c:if>
	                        
	                    		<c:if test="${aimEditActivityForm.funding.showActual}"><c:if test="${not empty aimEditActivityForm.funding.totalCommitments}">
	                    			<c:set var="activity_funding_text"><digi:trn key='aim:totalactualcommittment'>Total Actual Commitments</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalCommitments}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                    		</c:if></c:if>
	                    	
                        		<c:if test="${aimEditActivityForm.funding.showPipeline}"><c:if test="${not empty aimEditActivityForm.funding.totalPipelineCommitments}">
                        			<c:set var="activity_funding_text"><digi:trn key='aim:totalpipelinecommittment'> Total Pipeline Commitments</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPipelineCommitments}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
                        		</c:if></c:if>                        	
                        	</ampModule:display>
                        
                        	<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements"
											parentModule="/Activity Form/Funding/Funding Group/Funding Item">
								<c:if test="${aimEditActivityForm.funding.showPlanned}"><c:if test="${not empty aimEditActivityForm.funding.totalPlannedDisbursements}">
									<c:set var="activity_funding_text"><digi:trn key='aim:totalplanneddisbursement'>Total Planned Disbursements</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPlannedDisbursements}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                        	</c:if></c:if>
	                        
	                    		<c:if test="${aimEditActivityForm.funding.showActual}"><c:if test="${not empty aimEditActivityForm.funding.totalDisbursements}">
	                    			<c:set var="activity_funding_text"><digi:trn key='aim:totalactualdisbursement'>Total Actual Disbursements</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalDisbursements}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                    		</c:if></c:if>
	                    	
<%--                        		<c:if test="${aimEditActivityForm.funding.showPipeline}"><c:if test="${not empty aimEditActivityForm.funding.totalPipelineDisbursements}">
                        			<c:set var="activity_funding_text"><digi:trn key='aim:totalpipelinedisbursement'>Total Pipeline Disbursements</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPipelineDisbursements}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
                        		</c:if></c:if> --%>
                        	</ampModule:display>
                        
							<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
								<c:if test="${aimEditActivityForm.funding.showPlanned}"><c:if test="${not empty aimEditActivityForm.funding.totalPlannedExpenditures}">
									<c:set var="activity_funding_text"><digi:trn key='aim:totalplannedexpenditure'>Total Planned Expenditures</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPlannedExpenditures}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                        	</c:if></c:if>
	                        
	                    		<c:if test="${aimEditActivityForm.funding.showActual}"><c:if test="${not empty aimEditActivityForm.funding.totalExpenditures}">
	                    			<c:set var="activity_funding_text"><digi:trn key='aim:totalactualexpenditure'>Total Actual Expenditures</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalExpenditures}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                    		</c:if></c:if>
	                    	
<%--                        		<c:if test="${aimEditActivityForm.funding.showPipeline}"><c:if test="${not empty aimEditActivityForm.funding.totalPipelineExpenditures}">
                        			<c:set var="activity_funding_text"><digi:trn key='aim:totalpipelineexpenditure'>Total Pipeline Expenditures</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPipelineExpenditures}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
                        		</c:if></c:if> --%>
                        	</ampModule:display>


							<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Arrears"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
								<c:if test="${aimEditActivityForm.funding.showPlanned}"><c:if test="${not empty aimEditActivityForm.funding.totalPlannedArrears}">
									<c:set var="activity_funding_text"><digi:trn key='aim:totalplannedarrears'>Total Planned Arrears</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPlannedArrears}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                        	</c:if></c:if>
	                    		<c:if test="${aimEditActivityForm.funding.showActual}"><c:if test="${not empty aimEditActivityForm.funding.totalArrears}">
	                    			<c:set var="activity_funding_text"><digi:trn key='aim:totalactualarrears'>Total Actual Arrears</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalArrears}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                    		</c:if></c:if>
                        	</ampModule:display>

                        	<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursement Orders"
								parentModule="/Activity Form/Funding/Funding Group/Funding Item">
								
								<c:if test="${not empty aimEditActivityForm.funding.totalActualDisbursementsOrders}">
									<c:set var="activity_funding_text"><digi:trn>Total Actual Disbursment Orders</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalActualDisbursementsOrders}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
								</c:if>
                        	</ampModule:display>

 			<!--  RELEASE OF FUNDS START -->
							<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Release of Funds"
								parentModule="/Activity Form/Funding/Funding Group/Funding Item">
								
								<c:if test="${aimEditActivityForm.funding.showPlanned}"><c:if test="${not empty aimEditActivityForm.funding.totalPlannedRoF}">
									<c:set var="activity_funding_text"><digi:trn>Total Planned Release of Funds</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPlannedRoF}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                        	</c:if></c:if>
	                        
	                    		<c:if test="${aimEditActivityForm.funding.showActual}"><c:if test="${not empty aimEditActivityForm.funding.totalActualRoF}">
	                    			<c:set var="activity_funding_text"><digi:trn>Total Actual Release of Funds</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalActualRoF}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
	                    		</c:if></c:if>
                        	</ampModule:display>
			<!--  RELEASE OF FUNDS END -->
 
  			<!--  EDD START -->
							<c:if test="${aimEditActivityForm.funding.showPlanned}"><c:if test="${not empty aimEditActivityForm.funding.totalPlannedEDD}">
								<c:set var="activity_funding_text"><digi:trn>Total Planned EDD</digi:trn></c:set>
								<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalPlannedEDD}</c:set>
								<%@include file="preview_activity_funding_detail.jspf" %>
                        	</c:if></c:if>
	                        
                    		<c:if test="${aimEditActivityForm.funding.showActual}"><c:if test="${not empty aimEditActivityForm.funding.totalActualEDD}">
                    			<c:set var="activity_funding_text"><digi:trn>Total Actual EDD</digi:trn></c:set>
								<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalActualEDD}</c:set>
								<%@include file="preview_activity_funding_detail.jspf" %>
                    		</c:if></c:if>
                
	                    	<c:if test="${aimEditActivityForm.funding.showOfficialDevelopmentAid}"><c:if test="${not empty aimEditActivityForm.funding.totalOdaSscCommitments}">
	                    		<c:set var="activity_funding_text"><digi:trn>Total Official Development Aid SSC</digi:trn></c:set>
								<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalOdaSscCommitments}</c:set>
								<%@include file="preview_activity_funding_detail.jspf" %>
                        	</c:if></c:if>
                        
	                    	<c:if test="${aimEditActivityForm.funding.showBilateralSsc}"><c:if test="${not empty aimEditActivityForm.funding.totalBilateralSscCommitments}">
	                       		<c:set var="activity_funding_text"><digi:trn>Total Bilateral SSC</digi:trn></c:set>
								<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalBilateralSscCommitments}</c:set>
								<%@include file="preview_activity_funding_detail.jspf" %>
                        	</c:if></c:if>
                                                
	                    	<c:if test="${aimEditActivityForm.funding.showTriangularSsc}"><c:if test="${not empty aimEditActivityForm.funding.totalTriangularSscCommitments}">
								<c:set var="activity_funding_text"><digi:trn>Total Triangular SSC</digi:trn></c:set>
								<c:set var="activity_funding_amount">${aimEditActivityForm.funding.totalTriangularSscCommitments}</c:set>
								<%@include file="preview_activity_funding_detail.jspf" %>
	                    	</c:if></c:if>
                      	
 							<%-- activity-global undisbursed balance --%>
                        	<feature:display ampModule="Funding" name="Undisbursed Balance">
                        		<c:if test="${not empty aimEditActivityForm.funding.unDisbursementsBalance}">
	                        		<c:set var="activity_funding_text"><digi:trn key="aim:undisbursedBalance">Undisbursed Balance</digi:trn></c:set>
									<c:set var="activity_funding_amount">${aimEditActivityForm.funding.unDisbursementsBalance}</c:set>
									<%@include file="preview_activity_funding_detail.jspf" %>
            	            	</c:if>
                	        </feature:display>
                        
	                     	<c:if test="${not empty aimEditActivityForm.funding.deliveryRate}">
                         		<tr>
                            		<td class="preview-funding-total">
                                		<digi:trn>Delivery rate</digi:trn>:
                                	</td>
                            		<td nowrap="nowrap" class="preview-align preview-funding-total bold">
                            			${aimEditActivityForm.funding.deliveryRate}&nbsp;
                            		</td>
                            		<td class="preview-funding-total">&nbsp;</td>
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
