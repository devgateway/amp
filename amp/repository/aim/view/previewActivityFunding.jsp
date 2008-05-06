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

<%@ taglib uri="/taglib/aim" prefix="aim"%>
		<tr>
			<td width="30%" align="right" valign="top" nowrap="nowrap"
				bgcolor="#f4f4f2" class="t-name"><img id="group_funding_plus"
				onclick="toggleGroup('group_funding')"
				src="/TEMPLATE/ampTemplate/images/arrow_right.gif" /> <img
				id="group_funding_minus" onclick="toggleGroup('group_funding')"
				src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
				style="display: none" /> <digi:trn key="aim:funding">Funding</digi:trn>
			</td>
			<td bgcolor="#ffffff"><b>
			<div id="group_funding_dots" style="display: block"><bean:write
				name="aimEditActivityForm" property="totalCommitted" /> <bean:write
				name="aimEditActivityForm" property="currCode" /> &nbsp; ...</div>
			</b> <br />
			<div id="act_group_funding"
				style="display: none; position: relative; left: 10px;">
			<table width="95%" cellSpacing=1 cellPadding=0 border=0
				align="center">
				<tr>
					<td>
					<table width="100%" border=0 align="right" cellPadding=0
						cellSpacing=8 class="">
						<logic:notEmpty name="aimEditActivityForm"
							property="fundingOrganizations">
							<logic:iterate name="aimEditActivityForm"
								property="fundingOrganizations" id="fundingOrganization"
								type="org.digijava.module.aim.helper.FundingOrganization">

								<logic:notEmpty name="fundingOrganization" property="fundings">
									<logic:iterate name="fundingOrganization" indexId="index"
										property="fundings" id="funding"
										type="org.digijava.module.aim.helper.Funding">
										<tr>
											<td>
											<table cellSpacing=1 cellPadding=0 border="0" width="100%"
												class="box-border-nopadding">
												<tr>
													<td>
													<table cellSpacing=1 cellPadding=0 border="0" width="100%">
														<tr>
															<td>
															<table width="100%" border="0" cellpadding="1"
																bgcolor="#ffffff" cellspacing="1">
																<field:display name="Funding Organization Id"
																	feature="Funding Organizations">
																	<tr>
																		<td bgcolor="#FFFFFF" align="left" width="150"><a
																			title='<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>'>
																		<digi:trn key="aim:fundingOrgId">
                                                                                    Funding Organization Id
                                                                          </digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td bgcolor="#FFFFFF" align="left"><bean:write
																			name="funding" property="orgFundingId" /></td>
																	</tr>
																</field:display>
																<field:display name="Funding Organization Name"
																	feature="Funding Organizations">
																	<tr>
																		<td bgcolor="#FFFFFF" align="left" width="150"><a
																			title='<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>'>
																		<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td bgcolor="#FFFFFF" align="left">
																		${fundingOrganization.orgName}</td>
																	</tr>
																</field:display>

																<!-- type of assistance -->
																<field:display name="Type Of Assistance"
																	feature="Funding Organizations">
																	<tr>
																		<td bgcolor="#FFFFFF" align="left" width="150"><a
																			title='<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>'>
																		<digi:trn key="aim:typeOfAssist">
                                                                                    Type of Assistance </digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td bgcolor="#FFFFFF" align="left"><logic:notEmpty
																			name="funding" property="typeOfAssistance">
																			<bean:write name="funding"
																				property="typeOfAssistance.value" />
																		</logic:notEmpty></td>
																	</tr>
																</field:display>
																<field:display name="Type Of Assistance"
																	feature="Funding Organizations">
																	<tr>
																		<td bgcolor="#FFFFFF" align="left" width="150"><a
																			title='<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>'>
																		<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>
																		</a></td>
																		<td width="1">:</td>
																		<td bgcolor="#FFFFFF" align="left"><logic:notEmpty
																			name="funding" property="financingInstrument">
																			<bean:write name="funding"
																				property="financingInstrument.value" />
																		</logic:notEmpty></td>
																	</tr>
																</field:display>
															</table>
															</td>
														</tr>
													</table>
													</td>
												</tr>

												<td><!-- Begin funding detail -->
												<table width="100%" border="0" align="center"
													cellpadding="2" cellspacing="0">
													<bean:define id="funding" name="funding" scope="page"
														toScope="request"
														type="org.digijava.module.aim.helper.Funding"></bean:define>
													<jsp:include page="previewActivityFundingCommitments.jsp" />

													<feature:display module="Funding"
														name="Disbursement">
														<jsp:include page="previewActivityFundingDisbursement.jsp" />
													</feature:display>

													<feature:display module="Funding" name="Expenditures">
														<jsp:include page="previewActivityFundingExpenditures.jsp" />
													</feature:display>
													<tr>
														
														<feature:display module="Funding"
															name="Disbursement Orders">
															<jsp:include page="previewActivityFundingDisbursementOrders.jsp" />
														</feature:display>
														
														<td colspan="4" height="18"
															style="border-bottom: 1px dotted #000000"></td>
													<tr>
												</table>
												<!-- end funding detail --></td>
												</tr>
												<tr>

													<td bgcolor="#ffffff"><FONT color=blue>* <digi:trn
														key="aim:theAmountEnteredAreInThousands">
																				The amount entered are in thousands (000)		  																	</digi:trn>
													</FONT></td>
												</tr>
											</table>
											</td>
										</tr>
									</logic:iterate>
								</logic:notEmpty>
							</logic:iterate>
							<tr>
								<td>&nbsp;</td>
							</tr>
						</logic:notEmpty>
					</table>
					</td>
				</tr>
			</table>
			</div>
			</td>
		</tr>


