<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	function load() 
	{
		window.print();
	}

	function unload() {}
</script>

<digi:instance property="aimAdvancedReportForm" />

<table width="570" border="1" bordercolor="#808080" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
	<logic:notEmpty name="aimAdvancedReportForm" property="multiReport"> 
		<logic:iterate name="aimAdvancedReportForm" property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
			<logic:iterate name="multiReport"  property="hierarchy" id="hierarchy" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">

<%-- 	Hierarchy 3 begins here	--%>
				<logic:notEmpty name="levels" property="levels">
					<logic:iterate name="levels"  property="levels" id="level" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
						<tr>
							<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
								<bean:write name="level" property="label" /> :
 								<b><u><bean:write name="level" property="name" /></u></b>
							</td></tr>
						</tr>
						<logic:iterate name="level"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
							<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="title">
									<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Title			:</strong>
										<bean:write name="records" property="title" />
									</td></tr>
								</logic:notEmpty>	
							</logic:iterate>
							<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="objective">
									<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Objective			:</strong>
										<bean:define id="objKey">
											<c:out value="${records.objective}"/>
										</bean:define>
										<digi:edit key="<%=objKey%>"/>
									</td></tr>
								</logic:notEmpty>	
							</logic:iterate>
							<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="description">
									<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Description			:</strong>
										<bean:define id="descKey">
											<c:out value="${records.description}"/>
										</bean:define>
										<digi:edit key="<%=descKey%>"></digi:edit>
									</td></tr>
								</logic:notEmpty>	
							</logic:iterate>

							<tr>	
								<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
									<logic:notEqual name="titles" property="columnName" value="Project Title">
										<logic:notEqual name="titles" property="columnName" value="Objective">	
											<logic:notEqual name="titles" property="columnName" value="Description">
												<logic:notEqual name="titles" property="columnName" value="Donor">
													<logic:notEqual name="titles" property="columnName" value="Actual Completion Date">
														<logic:notEqual name="titles" property="columnName" value="Region">
															<logic:notEqual name="titles" property="columnName" value="Status">
																<logic:notEqual name="titles" property="columnName" value="Cumulative Disbursements">
																	<logic:notEqual name="titles" property="columnName" value="Type Of Assistance">
																		<logic:notEqual name="titles" property="columnName" value="Contact Name">
																			<td width="9%">
																				<div align="center"><strong>
																					<bean:write name="titles" property="columnName" />
																				</strong></div>
																			</td>
																		</logic:notEqual>
																	</logic:notEqual>
																</logic:notEqual>
															</logic:notEqual>
														</logic:notEqual>
													</logic:notEqual>
												</logic:notEqual>
											</logic:notEqual>
										</logic:notEqual>
									</logic:notEqual>
								</logic:iterate>
							</tr>
							<tr>
							<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="ampId">
									<td align="center" width="9%">
										<bean:write name="records" property="ampId" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="actualStartDate">
									<td align="center" width="9%">
										<bean:write name="records" property="actualStartDate" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="sectors">
									<td align="center" width="9%">
										<logic:iterate name="records" id="sectors" property="sectors"> 
											<%=sectors%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="level">
									<td align="center" width="9%">
										<bean:write name="records" property="level" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="totalCommitment">
									<td align="center" width="9%">
										<bean:write name="records" property="totalCommitment"/>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="modality">
									<td align="center" width="9%">
										<logic:iterate name="records" id="modality" property="modality"> 
											<%=modality%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
							</logic:iterate>
							</tr>
							<tr>	
								<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
									<logic:notEqual name="titles" property="columnName" value="Project Title">
										<logic:notEqual name="titles" property="columnName" value="Objective">	
											<logic:notEqual name="titles" property="columnName" value="Description">
												<logic:notEqual name="titles" property="columnName" value="Project Id">
													<logic:notEqual name="titles" property="columnName" value="Actual Start Date">
														<logic:notEqual name="titles" property="columnName" value="Sector">
															<logic:notEqual name="titles" property="columnName" value="Implementation Level">
																<logic:notEqual name="titles" property="columnName" value="Cumulative Commitments">
																	<logic:notEqual name="titles" property="columnName" value="Financing Instrument">
																		<td width="9%">
																			<div align="center"><strong>
																				<bean:write name="titles" property="columnName" />
																			</strong></div>
																		</td>
																	</logic:notEqual>
																</logic:notEqual>
															</logic:notEqual>
														</logic:notEqual>
													</logic:notEqual>
												</logic:notEqual>
											</logic:notEqual>
										</logic:notEqual>
									</logic:notEqual>
								</logic:iterate>
							</tr>
							<tr>
							<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="donors">
									<td align="center" width="9%">
										<logic:iterate name="records" id="donors" property="donors"> 
											<%=donors%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="actualCompletionDate">
									<td align="center" width="9%">
										<bean:write name="records" property="actualCompletionDate" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="regions">
									<td align="center" width="9%">
										<logic:iterate name="records" id="regions" property="regions">
											<%=regions%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="status">
									<td align="center" width="9%">
										<bean:write name="records" property="status" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="totalDisbursement">
									<td align="center" width="9%">
										<bean:write name="records" property="totalDisbursement"/>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="assistance">
									<td align="center" width="9%">
										<logic:iterate name="records" id="assistance" property="assistance"> 
											<%=assistance%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="contacts">
									<td align="center" width="9%">
										<logic:iterate name="records" id="contacts" property="contacts"> 
											<%=contacts%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
							</logic:iterate>
							</tr>


							<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
								<table width="100%" border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=center style="border-collapse:collapse">
									<tr>
										<logic:equal name="aimAdvancedReportForm" property="option" value="A">
											<td width="12%"><strong>Year</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
											<td width="15%" colspan="2"><strong>Year</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
											<td width="12%"><strong>Actual Commitment</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
											<td width="12%"><strong>Actual Disbursement</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
											<td width="12%"><strong>Actual Expenditure</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
											<td width="13%"><strong>Planned Commitment</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
											<td width="13%"><strong>Planned Disbursement</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
											<td width="13%"><strong>Planned Expenditure</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
											<td width="13%"><strong>UnDisbursed</strong></td>
										</logic:equal>
									</tr>
								<%
									int a4 = 1;
									int b4 = 1;
								%>	
									<logic:equal name="aimAdvancedReportForm" property="option" value="A">
										<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
												<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
												<%
													if((a4==1 && b4==1) || (a4==2 && b4==6) || (a4==3 && b4==11))
								  					{		
												%>
													<tr>
														<td width="12%"><strong>
															<%=fiscalYearRange%></strong>
														</td>
														<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="commAmount" value="0">
																	<bean:write name="ampFund" property="commAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="disbAmount" value="0">
																	<bean:write name="ampFund" property="disbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="expAmount" value="0">
																	<bean:write name="ampFund" property="expAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plCommAmount" value="0">
																	<bean:write name="ampFund" property="plCommAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
																	<bean:write name="ampFund" property="plDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plExpAmount" value="0">
																	<bean:write name="ampFund" property="plExpAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
																	<bean:write name="ampFund" property="unDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
													</tr>
												<%
													}
													if(b4==12)
								  					{
												%>
													<tr>
														<td width="12%"><strong>
															Total</strong>
														</td>
														<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="commAmount" value="0">
																	<bean:write name="ampFund" property="commAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="disbAmount" value="0">
																	<bean:write name="ampFund" property="disbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="expAmount" value="0">
																	<bean:write name="ampFund" property="expAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plCommAmount" value="0">
																	<bean:write name="ampFund" property="plCommAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
																	<bean:write name="ampFund" property="plDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plExpAmount" value="0">
																	<bean:write name="ampFund" property="plExpAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
																	<bean:write name="ampFund" property="unDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
													</tr>
												<%
													}
													b4++;
												%>	
												</logic:iterate>
											</logic:notEmpty>
										<%
											a4++;
										%>
										</logic:iterate>
									</logic:equal>
								<%
									int p4=1;
									int q4=1;
								%>
									<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
										<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
											<% int mark4=0; %>
												<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
		  									<%
												mark4++;
												if((p4==1 && q4==1) || (p4==1 && q4==2) || (p4==1 && q4==3) || (p4==1 && q4==4) || (p4==2 && q4==18) || (p4==2 && q4==19) || (p4==2 && q4==20) || (p4==2 && q4==21) || (p4==3 && q4==35) || (p4==3 && q4==36) || (p4==3 && q4==37) || (p4==3 && q4==38))
								  				{		
											%>
													<tr>
														<td width="9%"><strong>
															<%=fiscalYearRange%></strong>
														</td>
														<td width="6%"><strong>
														<% 
															int temp4 = 0;
															temp4 = mark4%4;
															if(temp4 == 0)
																temp4 = 4;
														%>
														Q<%= temp4%></strong>
														</td>
														<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="commAmount" value="0">
																	<bean:write name="ampFund" property="commAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="disbAmount" value="0">
																	<bean:write name="ampFund" property="disbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="expAmount" value="0">
																	<bean:write name="ampFund" property="expAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plCommAmount" value="0">
																	<bean:write name="ampFund" property="plCommAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
																	<bean:write name="ampFund" property="plDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plExpAmount" value="0">
																	<bean:write name="ampFund" property="plExpAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
																	<bean:write name="ampFund" property="unDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
													</tr>
												<%
													}
													if(q4 == 39)
								  					{
												%>
													<tr>
														<td width="12%" colspan="2"><strong>
															Total</strong>
														</td>
														<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="commAmount" value="0">
																	<bean:write name="ampFund" property="commAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="disbAmount" value="0">
																	<bean:write name="ampFund" property="disbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
															<td width="12%">
																<logic:notEqual name="ampFund" property="expAmount" value="0">
																	<bean:write name="ampFund" property="expAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plCommAmount" value="0">
																	<bean:write name="ampFund" property="plCommAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
																	<bean:write name="ampFund" property="plDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="plExpAmount" value="0">
																	<bean:write name="ampFund" property="plExpAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
														<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
															<td width="13%">
																<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
																	<bean:write name="ampFund" property="unDisbAmount" />
																</logic:notEqual>
															</td>
														</logic:equal>
													</tr>
												<%
													}
													q4++;
												%>	
												</logic:iterate>
											</logic:notEmpty>
										<%
											p4++;
										%>
										</logic:iterate>
									</logic:equal>	
								</table>
							</td></tr>
						</logic:iterate>
					</logic:iterate>
					<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
						<table width="100%" border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=center style="border-collapse:collapse">
							<tr>
								<td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" />><b>
									<bean:write name="level" property="name"/> 
									&nbsp;Total</b>
								</td>
							</tr>
							<tr>
								<logic:equal name="aimAdvancedReportForm" property="option" value="A">
									<td width="12%"><strong>Year</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
									<td width="15%" colspan="2"><strong>Year</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
									<td width="12%"><strong>Actual Commitment</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
									<td width="12%"><strong>Actual Disbursement</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
									<td width="12%"><strong>Actual Expenditure</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
									<td width="13%"><strong>Planned Commitment</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
									<td width="13%"><strong>Planned Disbursement</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
									<td width="13%"><strong>Planned Expenditure</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
									<td width="13%"><strong>UnDisbursed</strong></td>
								</logic:equal>
							</tr>
							<%
								int a5 = 1;
								int b5 = 1;
							%>	
							<logic:equal name="aimAdvancedReportForm" property="option" value="A">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
									<logic:iterate name="level"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
									<%
										if((a5==1 && b5==1) || (a5==2 && b5==6) || (a5==3 && b5==11))
										{		
									%>
											<tr>
												<td width="12%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											if(b5==12)
						  					{
										%>
											<tr>
												<td width="12%"><strong>
													Total</strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											b5++;
										%>	
										</logic:iterate>
								<%
									a5++;
								%>
								</logic:iterate>
							</logic:equal>
						<%
							int p5=1;
							int q5=1;
						%>
							<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
									<% int mark5=0; %>
										<logic:iterate name="level"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		  								<%
											mark5++;
											if((p5==1 && q5==1) || (p5==1 && q5==2) || (p5==1 && q5==3) || (p5==1 && q5==4) || (p5==2 && q5==18) || (p5==2 && q5==19) || (p5==2 && q5==20) || (p5==2 && q5==21) || (p5==3 && q5==35) || (p5==3 && q5==36) || (p5==3 && q5==37) || (p5==3 && q5==38))
								  			{		
										%>
											<tr>
												<td width="9%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<td width="6%"><strong>
												<% 
													int temp5 = 0;
													temp5 = mark5%4;
													if(temp5 == 0)
													temp5 = 4;
												%>
												Q<%= temp5%></strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											if(q5 == 39)
						  					{
										%>
											<tr>
												<td width="12%" colspan="2"><strong>
													Total</strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											q5++;
										%>	
										</logic:iterate>
									<%
										p5++;
									%>
								</logic:iterate>
							</logic:equal>
						</table>
					</td></tr>
					



					<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
						<table width="100%" border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=center style="border-collapse:collapse">
							<tr>
								<td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" />><b>
									<bean:write name="levels" property="name"/> 
									&nbsp;Total</b>
								</td>
							</tr>
							<tr>
								<logic:equal name="aimAdvancedReportForm" property="option" value="A">
									<td width="12%"><strong>Year</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
									<td width="15%" colspan="2"><strong>Year</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
									<td width="12%"><strong>Actual Commitment</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
									<td width="12%"><strong>Actual Disbursement</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
									<td width="12%"><strong>Actual Expenditure</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
									<td width="13%"><strong>Planned Commitment</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
									<td width="13%"><strong>Planned Disbursement</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
									<td width="13%"><strong>Planned Expenditure</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
									<td width="13%"><strong>UnDisbursed</strong></td>
								</logic:equal>
							</tr>
							<%
								int a6 = 1;
								int b6 = 1;
							%>	
							<logic:equal name="aimAdvancedReportForm" property="option" value="A">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
									<logic:iterate name="levels"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
									<%
										if((a6==1 && b6==1) || (a6==2 && b6==6) || (a6==3 && b6==11))
										{		
									%>
											<tr>
												<td width="12%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											if(b6==12)
						  					{
										%>
											<tr>
												<td width="12%"><strong>
													Total</strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											b6++;
										%>	
										</logic:iterate>
								<%
									a6++;
								%>
								</logic:iterate>
							</logic:equal>
						<%
							int p6=1;
							int q6=1;
						%>
							<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
									<% int mark6=0; %>
										<logic:iterate name="levels"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		  								<%
											mark6++;
											if((p6==1 && q6==1) || (p6==1 && q6==2) || (p6==1 && q6==3) || (p6==1 && q6==4) || (p6==2 && q6==18) || (p6==2 && q6==19) || (p6==2 && q6==20) || (p6==2 && q6==21) || (p6==3 && q6==35) || (p6==3 && q6==36) || (p6==3 && q6==37) || (p6==3 && q6==38))
								  			{		
										%>
											<tr>
												<td width="9%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<td width="6%"><strong>
												<% 
													int temp6 = 0;
													temp6 = mark6%4;
													if(temp6 == 0)
													temp6 = 4;
												%>
												Q<%= temp6%></strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											if(q6 == 39)
						  					{
										%>
											<tr>
												<td width="12%" colspan="2"><strong>
													Total</strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
															<bean:write name="fundSubTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
															<bean:write name="fundSubTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
															<bean:write name="fundSubTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
															<bean:write name="fundSubTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
															<bean:write name="fundSubTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
															<bean:write name="fundSubTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											q6++;
										%>	
										</logic:iterate>
									<%
										p6++;
									%>
								</logic:iterate>
							</logic:equal>
						</table>
					</td></tr>
				</logic:notEmpty>

<%-- 	Hierarchy 3 ends here	--%>

			</logic:iterate>
		</logic:iterate>
	</logic:notEmpty>

</table>
