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

<table border="1" bordercolor="#808080" width="90%" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
	<logic:notEmpty name="aimAdvancedReportForm" property="multiReport"> 
		<% int x = 1; %>
		<logic:iterate name="aimAdvancedReportForm" property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
			<logic:iterate name="multiReport"  property="hierarchy" id="hierarchy" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
				<tr>
					<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
						<bean:write name="hierarchy" property="label" /> :
 							<b><u><bean:write name="hierarchy" property="name" /></u></b>
					</td></tr>
				</tr>

	<%-- Hierarchy 1 begins here	--%>
				<logic:notEmpty name="hierarchy"  property="project">
					<tr>
						<logic:iterate name="hierarchy"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
							<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>><strong>
								S.No. <%= x %>	</strong>	<% x++ ;%>
							</td></tr>
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
												<td width="9%">
													<div align="center"><strong>
														<bean:write name="titles" property="columnName" />
													</strong></div>
												</td>
											</logic:notEqual>
										</logic:notEqual>
									</logic:notEqual>
								</logic:iterate>
							</tr>
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
								<logic:notEmpty name="records" property="actualCompletionDate">
									<td align="center" width="9%">
										<bean:write name="records" property="actualCompletionDate" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="status">
									<td align="center" width="9%">
										<bean:write name="records" property="status" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="level">
									<td align="center" width="9%">
										<bean:write name="records" property="level" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="totalCommitment">
									<td align="center" width="9%">
										<bean:write name="records" property="totalCommitment" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="totalDisbursement">
									<td align="center" width="9%">
										<bean:write name="records" property="totalDisbursement" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="assistance">
									<td align="center" width="9%">
										<logic:iterate name="records" id="assistance" property="assistance"> 
											<%=assistance%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="sectors">
									<td align="center" width="9%">
										<logic:iterate name="records" id="sectors" property="sectors"> 
											<%=sectors%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="regions">
									<td align="center" width="9%">
										<logic:iterate name="records" id="regions" property="regions">
											<%=regions%>	<br>
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
								<logic:notEmpty name="records" property="modality">
									<td align="center" width="9%">
										<logic:iterate name="records" id="modality" property="modality"> 
											<%=modality%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="donors">
									<td align="center" width="9%">
										<logic:iterate name="records" id="donors" property="donors"> 
											<%=donors%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
							</logic:iterate>
							<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
								<table border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse:collapse">
									<tr>
										<logic:equal name="aimAdvancedReportForm" property="option" value="A">
											<td width="12%"><strong>Year</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
											<td width="12%" colspan="2"><strong>Year</strong></td>
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
										int a = 1;
										int b = 1;
									%>	
									<logic:equal name="aimAdvancedReportForm" property="option" value="A">
										<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
												<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
												<%
													if((a==1 && b==1) || (a==2 && b==6) || (a==3 && b==11))
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
													if(b==12)
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
													b++;
												%>	
												</logic:iterate>
											</logic:notEmpty>
										<%
											a++;
										%>
										</logic:iterate>
									</logic:equal>
									
								<%
									int p=1;
									int q=1;
								%>
									<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
										<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
											<% int mark=0; %>
												<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
		  									<%
												mark++;
												if((p==1 && q==1) || (p==1 && q==2) || (p==1 && q==3) || (p==1 && q==4) || (p==2 && q==18) || (p==2 && q==19) || (p==2 && q==20) || (p==2 && q==21) || (p==3 && q==35) || (p==3 && q==36) || (p==3 && q==37) || (p==3 && q==38))
								  				{		
											%>
													<tr>
														<td width="7%"><strong>
															<%=fiscalYearRange%></strong>
														</td>
														<td width="5%"><strong>
														<% 
															int temp = 0;
															temp = mark%4;
															if(temp == 0)
																temp = 4;
														%>
														Q <%= temp%></strong>
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
													if(q == 39)
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
														q++;
												%>	
												</logic:iterate>
											</logic:notEmpty>
										<%
											p++;
										%>
										</logic:iterate>
									</logic:equal>
								</table>
							</td></tr>
						</logic:iterate>
					</tr>
					
					<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
						<table border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse:collapse">
							<tr>
								<td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" />><b>
									<bean:write name="hierarchy" property="name"/> 
									&nbsp;Total</b>
								</td>
							</tr>
							<tr>
								<logic:equal name="aimAdvancedReportForm" property="option" value="A">
									<td width="12%"><strong>Year</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
									<td width="12%" colspan="2"><strong>Year</strong></td>
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
								int a2 = 1;
								int b2 = 1;
							%>	
							<logic:equal name="aimAdvancedReportForm" property="option" value="A">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
									<logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
									<%
										if((a2==1 && b2==1) || (a2==2 && b2==6) || (a2==3 && b2==11))
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
											if(b2==12)
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
											b2++;
										%>	
										</logic:iterate>
								<%
									a2++;
								%>
								</logic:iterate>
							</logic:equal>
						<%
							int p2=1;
							int q2=1;
						%>
							<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
										<% int mark2=0; %>
										<logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		  								<%
											mark2++;
											if((p2==1 && q2==1) || (p2==1 && q2==2) || (p2==1 && q2==3) || (p2==1 && q2==4) || (p2==2 && q2==18) || (p2==2 && q2==19) || (p2==2 && q2==20) || (p2==2 && q2==21) || (p2==3 && q2==35) || (p2==3 && q2==36) || (p2==3 && q2==37) || (p2==3 && q2==38))
								  			{		
										%>
											<tr>
												<td width="7%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<td width="5%"><strong>
												<% 
													int temp2 = 0;
													temp2 = mark2%4;
													if(temp2 == 0)
													temp2 = 4;
												%>
												Q <%= temp2%></strong>
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
											if(q2 == 39)
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
											q2++;
										%>	
										</logic:iterate>
									<%
										p2++;
									%>
								</logic:iterate>
							</logic:equal>
						</table>
					</td></tr>
				</logic:notEmpty>
<%-- 	Hierarchy 1 ends here	--%>

<%-- 	Hierarchy 2 begins here	--%>
				<logic:notEmpty name="hierarchy"  property="levels">
					<logic:iterate name="hierarchy"  property="levels" id="levels" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
						<tr>
							<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
								<bean:write name="levels" property="label" /> :
 								<b><u><bean:write name="levels" property="name" /></u></b>
							</td></tr>
						</tr>
						<logic:iterate name="levels"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
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
												<td width="9%">
													<div align="center"><strong>
														<bean:write name="titles" property="columnName" />
													</strong></div>
												</td>
											</logic:notEqual>
										</logic:notEqual>
									</logic:notEqual>
								</logic:iterate>
							</tr>
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
								<logic:notEmpty name="records" property="actualCompletionDate">
									<td align="center" width="9%">
										<bean:write name="records" property="actualCompletionDate" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="status">
									<td align="center" width="9%">
										<bean:write name="records" property="status" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="level">
									<td align="center" width="9%">
										<bean:write name="records" property="level" />
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="assistance">
									<td align="center" width="9%">
										<logic:iterate name="records" id="assistance" property="assistance"> 
											<%=assistance%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="sectors">
									<td align="center" width="9%">
										<logic:iterate name="records" id="sectors" property="sectors"> 
											<%=sectors%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="regions">
									<td align="center" width="9%">
										<logic:iterate name="records" id="regions" property="regions">
											<%=regions%>	<br>
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
								<logic:notEmpty name="records" property="modality">
									<td align="center" width="9%">
										<logic:iterate name="records" id="modality" property="modality"> 
											<%=modality%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
								<logic:notEmpty name="records" property="donors">
									<td align="center" width="9%">
										<logic:iterate name="records" id="donors" property="donors"> 
											<%=donors%>	<br>
										</logic:iterate>
									</td>
								</logic:notEmpty>
							</logic:iterate>	
							<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
								<table border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse:collapse">
									<tr>
										<logic:equal name="aimAdvancedReportForm" property="option" value="A">
											<td width="12%"><strong>Year</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
											<td width="12%" colspan="2"><strong>Year</strong></td>
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
									int a3 = 1;
									int b3 = 1;
								%>	
									<logic:equal name="aimAdvancedReportForm" property="option" value="A">
										<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
												<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
												<%
													if((a3==1 && b3==1) || (a3==2 && b3==6) || (a3==3 && b3==11))
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
													if(b3==12)
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
													b3++;
												%>	
												</logic:iterate>
											</logic:notEmpty>
										<%
											a3++;
										%>
										</logic:iterate>
									</logic:equal>
								<%
									int p3=1;
									int q3=1;
								%>
									<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
										<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
											<% int mark3=0; %>
												<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
		  									<%
												mark3++;
												if((p3==1 && q3==1) || (p3==1 && q3==2) || (p3==1 && q3==3) || (p3==1 && q3==4) || (p3==2 && q3==18) || (p3==2 && q3==19) || (p3==2 && q3==20) || (p3==2 && q3==21) || (p3==3 && q3==35) || (p3==3 && q3==36) || (p3==3 && q3==37) || (p3==3 && q3==38))
								  				{		
											%>
													<tr>
														<td width="7%"><strong>
															<%=fiscalYearRange%></strong>
														</td>
														<td width="5%"><strong>
														<% 
															int temp3 = 0;
															temp3 = mark3%4;
															if(temp3 == 0)
																temp3 = 4;
														%>
														Q <%= temp3%></strong>
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
													if(q3 == 39)
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
														q3++;
												%>	
												</logic:iterate>
											</logic:notEmpty>
										<%
											p3++;
										%>
										</logic:iterate>
									</logic:equal>	
								</table>
							</td></tr>
						</logic:iterate>

					<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
						<table border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse:collapse">
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
									<td width="12%" colspan="2"><strong>Year</strong></td>
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
									<logic:iterate name="levels"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
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
											if(b4==12)
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
											b4++;
										%>	
										</logic:iterate>
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
										<% int mark4=0; %>
										<logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		  								<%
											mark4++;
											if((p4==1 && q4==1) || (p4==1 && q4==2) || (p4==1 && q4==3) || (p4==1 && q4==4) || (p4==2 && q4==18) || (p4==2 && q4==19) || (p4==2 && q4==20) || (p4==2 && q4==21) || (p4==3 && q4==35) || (p4==3 && q4==36) || (p4==3 && q4==37) || (p4==3 && q4==38))
								  			{		
										%>
											<tr>
												<td width="7%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<td width="5%"><strong>
												<% 
													int temp4 = 0;
													temp4 = mark4%4;
													if(temp4 == 0)
													temp4 = 4;
												%>
												Q <%= temp4%></strong>
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
											if(q4 == 39)
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
											q4++;
										%>	
										</logic:iterate>
									<%
										p4++;
									%>
								</logic:iterate>
							</logic:equal>
						</table>
					</td></tr>
<%-- 	Hierarchy 2 ends here	--%>

<%-- 	Hierarchy 3 begins here	--%>
					<logic:notEmpty name="levels" property="levels">
						<jsp:include page="htmlAdvancedReportHierarchy2.jsp"/>
					</logic:notEmpty>
				</logic:iterate>
			</logic:notEmpty>	
<%-- 	Hierarchy 3 ends here	--%>

				<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
					<table border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse:collapse">
						<tr>
							<td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" />><b>
								<bean:write name="hierarchy" property="name"/> 
								&nbsp;Total</b>
							</td>
						</tr>
						<tr>
							<logic:equal name="aimAdvancedReportForm" property="option" value="A">
								<td width="12%"><strong>Year</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
								<td width="12%" colspan="2"><strong>Year</strong></td>
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
							int a7 = 1;
							int b7 = 1;
						%>	
						<logic:equal name="aimAdvancedReportForm" property="option" value="A">
							<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
								<logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
									<%
										if((a7==1 && b7==1) || (a7==2 && b7==6) || (a7==3 && b7==11))
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
											if(b7==12)
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
											b7++;
										%>	
										</logic:iterate>
								<%
									a7++;
								%>
								</logic:iterate>
							</logic:equal>
						<%
							int p7=1;
							int q7=1;
						%>
							<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
										<% int mark7=0; %>
										<logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		  								<%
											mark7++;
											if((p7==1 && q7==1) || (p7==1 && q7==2) || (p7==1 && q7==3) || (p7==1 && q7==4) || (p7==2 && q7==18) || (p7==2 && q7==19) || (p7==2 && q7==20) || (p7==2 && q7==21) || (p7==3 && q7==35) || (p7==3 && q7==36) || (p7==3 && q7==37) || (p7==3 && q7==38))
								  			{		
										%>
											<tr>
												<td width="7%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<td width="5%"><strong>
												<% 
													int temp7 = 0;
													temp7 = mark7%4;
													if(temp7 == 0)
													temp7 = 4;
												%>
												Q <%= temp7%></strong>
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
											if(q7 == 39)
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
											q7++;
										%>	
										</logic:iterate>
									<%
										p7++;
									%>
								</logic:iterate>
							</logic:equal>
						</table>
					</td></tr>
			</logic:iterate>			
		</logic:iterate>
					
				<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
						<table border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse:collapse">
							<tr>
								<td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" /> class="head2-name"><b>
									Grand Total</b>
								</td>
							</tr>
							<tr>
								<logic:equal name="aimAdvancedReportForm" property="option" value="A">
									<td width="12%"><strong>Year</strong></td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
									<td width="12%" colspan="2"><strong>Year</strong></td>
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
								int a8 = 1;
								int b8 = 1;
							%>	
							<logic:equal name="aimAdvancedReportForm" property="option" value="A">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
									<logic:iterate name="multiReport"  property="fundTotal" id="fundTotal" type="org.digijava.module.aim.helper.AmpFund">
									<%
										if((a8==1 && b8==1) || (a8==2 && b8==6) || (a8==3 && b8==11))
										{		
									%>
											<tr>
												<td width="12%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="commAmount" value="0">
															<bean:write name="fundTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="disbAmount" value="0">
															<bean:write name="fundTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="expAmount" value="0">
															<bean:write name="fundTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
															<bean:write name="fundTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
															<bean:write name="fundTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
															<bean:write name="fundTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
															<bean:write name="fundTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											if(b8==12)
						  					{
										%>
											<tr>
												<td width="12%"><strong>
													Total</strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="commAmount" value="0">
															<bean:write name="fundTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="disbAmount" value="0">
															<bean:write name="fundTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="expAmount" value="0">
															<bean:write name="fundTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
															<bean:write name="fundTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
															<bean:write name="fundTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
															<bean:write name="fundTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
															<bean:write name="fundTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											b8++;
										%>	
										</logic:iterate>
								<%
									a8++;
								%>
								</logic:iterate>
							</logic:equal>
						<%
							int p8=1;
							int q8=1;
						%>
							<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
								<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
										<% int mark8=0; %>
										<logic:iterate name="multiReport"  property="fundTotal" id="fundTotal" type="org.digijava.module.aim.helper.AmpFund">
		  								<%
											mark8++;
											if((p8==1 && q8==1) || (p8==1 && q8==2) || (p8==1 && q8==3) || (p8==1 && q8==4) || (p8==2 && q8==18) || (p8==2 && q8==19) || (p8==2 && q8==20) || (p8==2 && q8==21) || (p8==3 && q8==35) || (p8==3 && q8==36) || (p8==3 && q8==37) || (p8==3 && q8==38))
								  			{		
										%>
											<tr>
												<td width="7%"><strong>
													<%=fiscalYearRange%></strong>
												</td>
												<td width="5%"><strong>
												<% 
													int temp8 = 0;
													temp8 = mark8%4;
													if(temp8 == 0)
													temp8 = 4;
												%>
												Q <%= temp8%></strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="commAmount" value="0">
															<bean:write name="fundTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="disbAmount" value="0">
															<bean:write name="fundTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="expAmount" value="0">
															<bean:write name="fundTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
															<bean:write name="fundTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
															<bean:write name="fundTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
															<bean:write name="fundTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
															<bean:write name="fundTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											if(q8 == 39)
						  					{
										%>
											<tr>
												<td width="12%" colspan="2"><strong>
													Total</strong>
												</td>
												<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="commAmount" value="0">
															<bean:write name="fundTotal" property="commAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="disbAmount" value="0">
															<bean:write name="fundTotal" property="disbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
													<td width="12%">
														<logic:notEqual name="fundTotal" property="expAmount" value="0">
															<bean:write name="fundTotal" property="expAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
															<bean:write name="fundTotal" property="plCommAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
															<bean:write name="fundTotal" property="plDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
															<bean:write name="fundTotal" property="plExpAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
												<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
													<td width="13%">
														<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
															<bean:write name="fundTotal" property="unDisbAmount" />
														</logic:notEqual>
													</td>
												</logic:equal>
											</tr>
										<%
											}
											q8++;
										%>	
										</logic:iterate>
									<%
										p8++;
									%>
								</logic:iterate>
							</logic:equal>
						</table>
					</td></tr>
	</logic:notEmpty>

	<logic:empty name="aimAdvancedReportForm" property="multiReport"> 
		<tr>
			<td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> align="center">
				<b>
					<digi:trn key="aim:noRecords">No Records</digi:trn>
				</b>
			</td>
		</tr>
	</logic:empty>
</table>
