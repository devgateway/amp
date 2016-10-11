<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimChannelOverviewForm" />
<%--
<jsp:include page="teamPagesHeader.jsp"  />
--%>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=16 valign="center" width=650><span class=subtitle-blue>
						<bean:write name="aimChannelOverviewForm" property="name"/>
						</span>
					</td>
				</tr>				
				<tr>
					<td>
						<logic:notEmpty name="aimChannelOverviewForm" property="description"><b>
							<digi:trn key="aim:description">
							Description
							</digi:trn> : </b>&nbsp;
							<bean:write name="aimChannelOverviewForm" property="description"/>
						</logic:notEmpty>
					</td>
				</tr>				
				<tr>
					<td>
						<logic:notEmpty name="aimChannelOverviewForm" property="objective"><b>
							<digi:trn key="aim:objective">
							Objective
							</digi:trn> : </b>&nbsp;
							<bean:write name="aimChannelOverviewForm" property="objective"/>
						</logic:notEmpty>						
					</td>
				</tr>					
				<tr>
					<td>
						<b><digi:trn key="aim:condition">Condition</digi:trn>: </b>
						<bean:write name="aimChannelOverviewForm" property="condition" />
					</td>
				</tr>					
				<tr>
					<td noWrap width="100%" vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="95%">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table cellpadding="0" cellspacing="0" width="100%">
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title align="center" height="20">
																<digi:trn key="aim:activityDetails">
																	Activity Details
																</digi:trn>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table width="100%" border="0" cellspacing="4">
													<tr>
														<td bgcolor="#f4f4f2" align="center" width="50%">
															<table width="100%">
																<tr>
																	<td><b>
																		<digi:trn key="aim:financingInstrument">
																			Financing Instrument
																		</digi:trn> :</b>&nbsp;
																		<c:if test="${!empty aimChannelOverviewForm.modal}">
																			<logic:iterate name="aimChannelOverviewForm" id="modal" property="modal"
																				type="org.digijava.module.categorymanager.dbentity.AmpCategoryValue">
																				<bean:write name="modal" property="value" />&nbsp;
																			</logic:iterate>
																		</c:if>
																	</td>
																</tr>
															</table>														
														</td>
														<td align="center" width="50%"  bgcolor="#f4f4f2">
															<table width="100%" border="0">
																<tr>
																	<td><b>
																		<digi:trn key="aim:fundingAgency">
																			Funding agency
																		</digi:trn> :</b>&nbsp;
																		<bean:write name="aimChannelOverviewForm" property="fundingagency" />
																	</td>
																</tr>											
															</table>														
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2" align="center" width="50%">
															<table width="100%">
																<tr>
																	<td><b>
																		<digi:trn key="aim:status">
																			Status
																		</digi:trn> :</b>&nbsp;
																		<bean:write name="aimChannelOverviewForm" property="status" />
																	</td>
																</tr>
															</table>														
														</td>
														<td align="center" width="50%"  bgcolor="#f4f4f2">
															<table width="100%" border="0">
																<tr>
																	<td><b>
																		<digi:trn key="aim:implementingAgency">
																			Implementing Agency
																		</digi:trn> :</b>&nbsp;
																		<bean:write name="aimChannelOverviewForm" property="implagency" />
																	</td>
																</tr>											
															</table>														
														</td>
														<%--
														<td align="center" width="50%"  bgcolor="#f4f4f2">
															<table width="100%" border="0">
																<tr>
																	<td><b>
																		<digi:trn key="aim:reportingAgency">
																			Reporting Agency
																		</digi:trn> :</b>&nbsp;
																		<bean:write name="aimChannelOverviewForm" property="reportingagency" />
																	</td>
																</tr>											
															</table>														
														</td>
														--%>
													</tr>		
													<%--
													<tr>
														<td bgcolor="#f4f4f2" align="center" width="50%">
															<table width="100%">
																<tr>
																	<td><b>
																		<digi:trn key="aim:theme">
																			Theme
																		</digi:trn> :</b>&nbsp;
																		<bean:write name="aimChannelOverviewForm" property="theme" />
																	</td>
																</tr>
															</table>														
														</td>
														<td align="center" width="50%"  bgcolor="#f4f4f2">
															<table width="100%" border="0">
																<tr>
																	<td><b>
																		<digi:trn key="aim:implementingAgency">
																			Implementing Agency
																		</digi:trn> :</b>&nbsp;
																		<bean:write name="aimChannelOverviewForm" property="implagency" />
																	</td>
																</tr>											
															</table>														
														</td>
													</tr>
													--%>	
													<tr>
														<td align="center" width="50%"  bgcolor="#f4f4f2">
															<table width="100%" border="0">
																<tr>
																	<td><b>
																		<digi:trn key="aim:typeOfAssistance">
																			Type of Assistance
																		</digi:trn> :</b>&nbsp;
																		<logic:notEmpty name="aimChannelOverviewForm" property="assistance">
																		<logic:iterate name="aimChannelOverviewForm" id="assistance"
																		property="assistance" 
																		type="org.digijava.module.aim.helper.Assistance">
																			<bean:write name="assistance" property="assistanceType" />&nbsp;
																		</logic:iterate>
																		</logic:notEmpty>
																	</td>
																</tr>											
															</table>														
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2">
														<b>
	    													<digi:trn key="aim:sector">Sector</digi:trn>: </b>
															<c:if test="${!empty aimChannelOverviewForm.sectors}">
																<logic:iterate name="aimChannelOverviewForm" property="sectors" id="sector"
																type="org.digijava.module.aim.dbentity.AmpSector">
																	<bean:write name="sector" property="name"/><BR>
																</logic:iterate>
															</c:if>
														</td>
														<td bgcolor="#f4f4f2">
															<b><digi:trn key="aim:projectIds">Project IDs</digi:trn>:</b>
															<c:if test="${!empty aimChannelOverviewForm.internalIds}">
				       	 									<logic:iterate name="aimChannelOverviewForm" property="internalIds" id="internalId" 
																type="org.digijava.module.aim.dbentity.AmpActivityInternalId">
																	<bean:write name="internalId" property="internalId"/><BR>
																</logic:iterate>
																</c:if>
														</td>
													</tr>	
													<tr>
														<td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:implementationLevel">
																Implementation Level</digi:trn>:</b>
		        												<bean:write name="aimChannelOverviewForm" property="level" />
														</td>
														<TD bgcolor="#f4f4f2">
															<b><digi:trn key="aim:startDate">Start Date</digi:trn>:</b>
															<bean:write name="aimChannelOverviewForm" property="activityStartDate" />
														</TD>
													</tr>
													<tr>
														<TD bgcolor="#f4f4f2">
															<b><digi:trn key="aim:totalCostOfActivity">
															Total Cost Of Activity</digi:trn>:</b> 
															<bean:write name="aimChannelOverviewForm" property="grandTotal" />
														</TD>
														<TD bgcolor="#f4f4f2">
															<b><digi:trn key="aim:completionDate">Completion Date</digi:trn>:</b>
															<bean:write name="aimChannelOverviewForm" property="activityCloseDate" />
														</TD>
														
													</tr>	
																									
												</table>	
											</td>
										</tr>

									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>
						</table>			
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" value="Close" onclick="window.close()">
					</td>
				</tr>				
			</table>
		</td>
	</tr>
</table>
