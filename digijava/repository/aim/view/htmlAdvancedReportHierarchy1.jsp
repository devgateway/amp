<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<script language="JavaScript">
	function load() 
	{
		window.print();
	}

	function unload() {}
</script>

<digi:instance property="aimAdvancedReportForm" />

<%boolean typeAssist = false;%>
<c:if test="${!empty aimAdvancedReportForm.titles}">
	<logic:iterate name="aimAdvancedReportForm" id="titles"
		property="titles">
		<c:if test="${titles.columnName == 'Type Of Assistance'}">
			<%typeAssist = true;%>
		</c:if>
	</logic:iterate>
</c:if>
<%request.setAttribute("typeAssist", new Boolean(typeAssist));%>

<table width="580" border="1" bordercolor="#808080" cellspacing=1
	cellpadding=1 valign=top align=left style="border-collapse: collapse">
	<logic:notEmpty name="aimAdvancedReportForm" property="multiReport">
		<%int x = 1;

				%>
		<logic:iterate name="aimAdvancedReportForm" property="multiReport"
			id="multiReport" type="org.digijava.module.aim.helper.multiReport">
			<logic:iterate name="multiReport" property="hierarchy" id="hierarchy"
				type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
				<tr>
				<tr>
					<td
						colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
					<bean:write name="hierarchy" property="label" /> : <b><u><bean:write
						name="hierarchy" property="name" /></u></b></td>
				</tr>
				</tr>

				<%-- Hierarchy 1 begins here	--%>
				<logic:notEmpty name="hierarchy" property="project">
					<tr>
						<logic:iterate name="hierarchy" property="project" id="project"
							type="org.digijava.module.aim.helper.Report">
							<tr>
								<td
									colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>><strong>
								S.No. <%=x%> </strong> <%x++;%></td>
							</tr>
							<logic:iterate name="project" property="records" id="records"
								type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="title">
									<tr>
										<td
											colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Title :</strong> <bean:write name="records"
											property="title" /></td>
									</tr>
								</logic:notEmpty>
							</logic:iterate>
							<logic:iterate name="project" property="records" id="records"
								type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="objective">
									<tr>
										<td
											colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Objective :</strong> <bean:define id="objKey">
											<c:out value="${records.objective}" />
										</bean:define> <digi:edit key="<%=objKey%>" /></td>
									</tr>
								</logic:notEmpty>
							</logic:iterate>
							<logic:iterate name="project" property="records" id="records"
								type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="description">
									<tr>
										<td
											colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Description :</strong> <bean:define id="descKey">
											<c:out value="${records.description}" />
										</bean:define> <digi:edit key="<%=descKey%>"></digi:edit></td>
									</tr>
								</logic:notEmpty>
							</logic:iterate>
							<tr>
								<logic:iterate name="aimAdvancedReportForm" property="titles"
									id="titles" type="org.digijava.module.aim.helper.Column">
																				<td width="9%">
																				<div align="center"><strong> <bean:write
																					name="titles" property="columnName" /> </strong></div>
																				</td>
								</logic:iterate>
							</tr>
							<tr>
								<logic:iterate name="project" property="records" id="records"
									type="org.digijava.module.aim.helper.AdvancedReport">
									<logic:notEmpty name="records" property="ampId">
										<td align="center" width="9%"><bean:write name="records"
											property="ampId" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="actualStartDate">
										<td align="center" width="9%"><bean:write name="records"
											property="actualStartDate" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="sectors">
										<td align="center" width="9%"><logic:iterate name="records"
											id="sectors" property="sectors">
											<%=sectors%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="level">
										<td align="center" width="9%"><bean:write name="records"
											property="level" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="totalCommitment">
										<td align="center" width="9%"><bean:write name="records"
											property="totalCommitment" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="totalDisbursement">
										<td align="center" width="9%"><bean:write name="records"
											property="totalDisbursement" /></td>
									</logic:notEmpty>

									<logic:notEmpty name="records" property="modality">
										<td align="center" width="9%"><logic:iterate name="records"
											id="modality" property="modality">
											<%=modality%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
								</logic:iterate>
							</tr>
							<tr>
								<logic:iterate name="aimAdvancedReportForm" property="titles"
									id="titles" type="org.digijava.module.aim.helper.Column">
									<logic:notEqual name="titles" property="columnName"
										value="Project Title">
										<logic:notEqual name="titles" property="columnName"
											value="Objective">
											<logic:notEqual name="titles" property="columnName"
												value="Description">
												<logic:notEqual name="titles" property="columnName"
													value="Project Id">
													<logic:notEqual name="titles" property="columnName"
														value="Actual Start Date">
														<logic:notEqual name="titles" property="columnName"
															value="Sector">
															<logic:notEqual name="titles" property="columnName"
																value="Implementation Level">
																<logic:notEqual name="titles" property="columnName"
																	value="Cumulative Commitments">
																	<logic:notEqual name="titles" property="columnName"
																		value="Financing Instrument">
																		<logic:notEqual name="titles" property="columnName"
																			value="Total Commitment">
																			<logic:notEqual name="titles" property="columnName"
																				value="Total Disbursement">
																				<td width="9%">
																				<div align="center"><strong> <bean:write
																					name="titles" property="columnName" /> </strong></div>
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
									</logic:notEqual>
								</logic:iterate>
							</tr>
							<tr>
								<logic:iterate name="project" property="records" id="records"
									type="org.digijava.module.aim.helper.AdvancedReport">
									<logic:notEmpty name="records" property="donors">
										<td align="center" width="9%"><logic:iterate name="records"
											id="donors" property="donors">
											<%=donors%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="actualCompletionDate">
										<td align="center" width="9%"><bean:write name="records"
											property="actualCompletionDate" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="regions">
										<td align="center" width="9%"><logic:iterate name="records"
											id="regions" property="regions">
											<%=regions%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="components">
										<td align="center" width="9%"><logic:iterate name="records"
											id="component" property="components">
											<%=component%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>


									<logic:notEmpty name="records" property="status">
										<td align="center" width="9%"><bean:write name="records"
											property="status" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="assistance">
										<td align="center" width="9%"><logic:iterate name="records"
											id="assistance" property="assistance">
											<%=assistance%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="contacts">
										<td align="center" width="9%"><logic:iterate name="records"
											id="contacts" property="contacts">
											<%=contacts%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
								</logic:iterate>
							</tr>
							<tr>
								<td
									colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
								<table width="100%" border="1" bordercolor="#C0C0C0"
									cellspacing=1 cellpadding=1 valign=top align=center
									style="border-collapse:collapse">
									<tr>
										<logic:equal name="aimAdvancedReportForm" property="option"
											value="A">
											<td width="12%"><strong>Year</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="reportType" value="donor">
											<logic:equal name="typeAssist" value="true">
												<td width="13%"><strong>Type of Assistance</strong></td>
											</logic:equal>
										</logic:equal>

										<logic:equal name="aimAdvancedReportForm" property="option"
											value="Q">
											<td width="15%" colspan="2"><strong>Year1</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="acCommFlag" value="true">
											<td width="12%"><strong>Actual Commitment</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="acDisbFlag" value="true">
											<td width="12%"><strong>Actual Disbursement</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag"
											value="true">
											<td width="12%"><strong>Actual Expenditure</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="plCommFlag" value="true">
											<td width="13%"><strong>Planned Commitment</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="plDisbFlag" value="true">
											<td width="13%"><strong>Planned Disbursement</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag"
											value="true">
											<td width="13%"><strong>Planned Expenditure</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag"
											value="true">
											<td width="13%"><strong>UnDisbursed</strong></td>
										</logic:equal>
									</tr>

									<%
int a = 1;
								int b = 1;

								%>
									<logic:equal name="aimAdvancedReportForm" property="option"
										value="A">
										<logic:iterate name="aimAdvancedReportForm"
											property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
												<logic:iterate name="records" property="ampFund"
													id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
													<%
if ((a == 1 && b == 1)
														|| (a == 2 && b == 6)
														|| (a == 3 && b == 11)) {

													%>
													<tr>
														<%int rowspan = project
															.getReportRowSpan(typeAssist);%>
														<td width="12%" rowspan="<%=rowspan%>"><strong> <%=fiscalYearRange%></strong>
														</td>
														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />
													</tr>

													<bean:define id="records" name="records"
														type="org.digijava.module.aim.helper.AdvancedReport"
														toScope="request" />
													<jsp:include page="termView3.jsp" />


													<%
}
												if (b == 12) {

													%>
													<tr>
														<td width="12%"><strong>Total </strong></td>
														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />

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
int p = 1;
								int q = 1;

								%>
									<logic:equal name="aimAdvancedReportForm" property="option"
										value="Q">
										<logic:iterate name="aimAdvancedReportForm"
											property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
												<%int mark = 0;

											%>
												<logic:iterate name="records" property="ampFund"
													id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
													<%
												mark++;
												if ((p == 1 && q == 1)
														|| (p == 1 && q == 2)
														|| (p == 1 && q == 3)
														|| (p == 1 && q == 4)
														|| (p == 2 && q == 18)
														|| (p == 2 && q == 19)
														|| (p == 2 && q == 20)
														|| (p == 2 && q == 21)
														|| (p == 3 && q == 35)
														|| (p == 3 && q == 36)
														|| (p == 3 && q == 37)
														|| (p == 3 && q == 38)) {

													%>
													<tr>
														<%int rowspan = project
															.getReportRowSpan(typeAssist);%>
														<%if (mark % 4 == 1) {%>
														<td width="9%" rowspan="<%=rowspan*4%>"><strong> <%=fiscalYearRange%></strong>
														</td>
														<%}%>
														<td width="6%" rowspan="<%=rowspan%>"><strong> <%int temp = 0;
													temp = mark % 4;
													if (temp == 0)
														temp = 4;

													%> Q<%=temp%></strong></td>



														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />
													</tr>
													<bean:define id="records" name="records"
														type="org.digijava.module.aim.helper.AdvancedReport"
														toScope="request" />
													<jsp:include page="termView3.jsp" />

													<%
}
												if (q == 39) {

													%>
													<tr>
														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />
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
								</td>
							</tr>
						</logic:iterate>
					</tr>

				</logic:notEmpty>
				<%-- 	Hierarchy 1 ends here	--%>

				<%-- 	Hierarchy 2 begins here	--%>
				<logic:notEmpty name="hierarchy" property="levels">
					<logic:iterate name="hierarchy" property="levels" id="levels"
						type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
						<tr>
						<tr>
							<td
								colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
							<bean:write name="levels" property="label" /> : <b><u><bean:write
								name="levels" property="name" /></u></b></td>
						</tr>
						</tr>
						<logic:iterate name="levels" property="project" id="project"
							type="org.digijava.module.aim.helper.Report">
							<logic:iterate name="project" property="records" id="records"
								type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="title">
									<tr>
										<td
											colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Title :</strong> <bean:write name="records"
											property="title" /></td>
									</tr>
								</logic:notEmpty>
							</logic:iterate>
							<logic:iterate name="project" property="records" id="records"
								type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="objective">
									<tr>
										<td
											colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Objective :</strong> <bean:define id="objKey">
											<c:out value="${records.objective}" />
										</bean:define> <digi:edit key="<%=objKey%>" /></td>
									</tr>
								</logic:notEmpty>
							</logic:iterate>
							<logic:iterate name="project" property="records" id="records"
								type="org.digijava.module.aim.helper.AdvancedReport">
								<logic:notEmpty name="records" property="description">
									<tr>
										<td
											colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
										<strong>Description :</strong> <bean:define id="descKey">
											<c:out value="${records.description}" />
										</bean:define> <digi:edit key="<%=descKey%>"></digi:edit></td>
									</tr>
								</logic:notEmpty>
							</logic:iterate>
							<tr>
								<logic:iterate name="aimAdvancedReportForm" property="titles"
									id="titles" type="org.digijava.module.aim.helper.Column">
									<logic:notEqual name="titles" property="columnName"
										value="Project Title">
										<logic:notEqual name="titles" property="columnName"
											value="Objective">
											<logic:notEqual name="titles" property="columnName"
												value="Description">
												<logic:notEqual name="titles" property="columnName"
													value="Donor">
													<logic:notEqual name="titles" property="columnName"
														value="Actual Completion Date">
														<logic:notEqual name="titles" property="columnName"
															value="Region">
															<logic:notEqual name="titles" property="columnName"
																value="Status">
																<logic:notEqual name="titles" property="columnName"
																	value="Cumulative Disbursements">
																	<logic:notEqual name="titles" property="columnName"
																		value="Type Of Assistance">
																		<logic:notEqual name="titles" property="columnName"
																			value="Contact Name">
																			<td width="9%">
																			<div align="center"><strong> <bean:write
																				name="titles" property="columnName" /> </strong></div>
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
								<logic:iterate name="project" property="records" id="records"
									type="org.digijava.module.aim.helper.AdvancedReport">
									<logic:notEmpty name="records" property="ampId">
										<td align="center" width="9%"><bean:write name="records"
											property="ampId" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="actualStartDate">
										<td align="center" width="9%"><bean:write name="records"
											property="actualStartDate" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="sectors">
										<td align="center" width="9%"><logic:iterate name="records"
											id="sectors" property="sectors">
											<%=sectors%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="level">
										<td align="center" width="9%"><bean:write name="records"
											property="level" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="totalCommitment">
										<td align="center" width="9%"><bean:write name="records"
											property="totalCommitment" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="totalDisbursement">
										<td align="center" width="9%"><bean:write name="records"
											property="totalDisbursement" /></td>
									</logic:notEmpty>

									<logic:notEmpty name="records" property="modality">
										<td align="center" width="9%"><logic:iterate name="records"
											id="modality" property="modality">
											<%=modality%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
								</logic:iterate>
							</tr>
							<tr>
								<logic:iterate name="aimAdvancedReportForm" property="titles"
									id="titles" type="org.digijava.module.aim.helper.Column">
									<logic:notEqual name="titles" property="columnName"
										value="Project Title">
										<logic:notEqual name="titles" property="columnName"
											value="Objective">
											<logic:notEqual name="titles" property="columnName"
												value="Description">
												<logic:notEqual name="titles" property="columnName"
													value="Project Id">
													<logic:notEqual name="titles" property="columnName"
														value="Actual Start Date">
														<logic:notEqual name="titles" property="columnName"
															value="Sector">
															<logic:notEqual name="titles" property="columnName"
																value="Implementation Level">
																<logic:notEqual name="titles" property="columnName"
																	value="Cumulative Commitments">
																	<logic:notEqual name="titles" property="columnName"
																		value="Financing Instrument">
																		<logic:notEqual name="titles" property="columnName"
																			value="Total Commitment">
																			<logic:notEqual name="titles" property="columnName"
																				value="Total Disbursement">

																				<td width="9%">
																				<div align="center"><strong> <bean:write
																					name="titles" property="columnName" /> </strong></div>
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
									</logic:notEqual>
								</logic:iterate>
							</tr>
							<tr>
								<logic:iterate name="project" property="records" id="records"
									type="org.digijava.module.aim.helper.AdvancedReport">
									<logic:notEmpty name="records" property="donors">
										<td align="center" width="9%"><logic:iterate name="records"
											id="donors" property="donors">
											<%=donors%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="actualCompletionDate">
										<td align="center" width="9%"><bean:write name="records"
											property="actualCompletionDate" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="regions">
										<td align="center" width="9%"><logic:iterate name="records"
											id="regions" property="regions">
											<%=regions%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="components">
										<td align="center" width="9%"><logic:iterate name="records"
											id="component" property="components">
											<%=component%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>

									<logic:notEmpty name="records" property="status">
										<td align="center" width="9%"><bean:write name="records"
											property="status" /></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="assistance">
										<td align="center" width="9%"><logic:iterate name="records"
											id="assistance" property="assistance">
											<%=assistance%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
									<logic:notEmpty name="records" property="contacts">
										<td align="center" width="9%"><logic:iterate name="records"
											id="contacts" property="contacts">
											<%=contacts%>
											<br>
										</logic:iterate></td>
									</logic:notEmpty>
								</logic:iterate>
							</tr>
							<tr>
								<td
									colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
								<table width="100%" border="1" bordercolor="#C0C0C0"
									cellspacing=1 cellpadding=1 valign=top align=center
									style="border-collapse:collapse">
									<tr>

										<logic:equal name="aimAdvancedReportForm" property="option"
											value="A">
											<td width="12%"><strong>Year</strong></td>
											<logic:equal name="aimAdvancedReportForm"
												property="reportType" value="donor">
												<logic:equal name="typeAssist" value="true">
													<td width="13%"><strong>Type of Assistance</strong></td>
												</logic:equal>
											</logic:equal>
										</logic:equal>



										<logic:equal name="aimAdvancedReportForm" property="option"
											value="Q">
											<td width="15%" colspan="2"><strong>Year</strong></td>
											<logic:equal name="aimAdvancedReportForm"
												property="reportType" value="donor">
												<logic:equal name="typeAssist" value="true">
													<td colspan="2" width="13%"><strong>Type of Assistance</strong></td>
												</logic:equal>
											</logic:equal>

										</logic:equal>


										<logic:equal name="aimAdvancedReportForm"
											property="acCommFlag" value="true">
											<td width="12%"><strong>Actual Commitment</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="acDisbFlag" value="true">
											<td width="12%"><strong>Actual Disbursement</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag"
											value="true">
											<td width="12%"><strong>Actual Expenditure</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="plCommFlag" value="true">
											<td width="13%"><strong>Planned Commitment</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm"
											property="plDisbFlag" value="true">
											<td width="13%"><strong>Planned Disbursement</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag"
											value="true">
											<td width="13%"><strong>Planned Expenditure</strong></td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag"
											value="true">
											<td width="13%"><strong>UnDisbursed</strong></td>
										</logic:equal>
									</tr>
									<%
									int a3 = 1;
									int b3 = 1;

									%>
									<logic:equal name="aimAdvancedReportForm" property="option"
										value="A">
										<logic:iterate name="aimAdvancedReportForm"
											property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
												<logic:iterate name="records" property="ampFund"
													id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
													<%
													if ((a3 == 1 && b3 == 1)
															|| (a3 == 2 && b3 == 6)
															|| (a3 == 3 && b3 == 11)) {

														%>
													<tr>
														<%int rowspan = project
																.getReportRowSpan(typeAssist);%>
														<td width="12%" rowspan="<%=rowspan%>"><strong> <%=fiscalYearRange%></strong>
														</td>
														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />
													</tr>

													<bean:define id="records" name="records"
														type="org.digijava.module.aim.helper.AdvancedReport"
														toScope="request" />
													<jsp:include page="termView3.jsp" />


													<%
													}
													if (b3 == 12) {

														%>
													<tr>
														<td width="12%"><strong> Total</strong></td>
														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />
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
									int p3 = 1;
									int q3 = 1;

									%>
									<logic:equal name="aimAdvancedReportForm" property="option"
										value="Q">
										<logic:iterate name="aimAdvancedReportForm"
											property="fiscalYearRange" id="fiscalYearRange">
											<logic:notEmpty name="records" property="ampFund">
												<%int mark3 = 0;

												%>
												<logic:iterate name="records" property="ampFund"
													id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
													<%
mark3++;
													if ((p3 == 1 && q3 == 1)
															|| (p3 == 1 && q3 == 2)
															|| (p3 == 1 && q3 == 3)
															|| (p3 == 1 && q3 == 4)
															|| (p3 == 2 && q3 == 18)
															|| (p3 == 2 && q3 == 19)
															|| (p3 == 2 && q3 == 20)
															|| (p3 == 2 && q3 == 21)
															|| (p3 == 3 && q3 == 35)
															|| (p3 == 3 && q3 == 36)
															|| (p3 == 3 && q3 == 37)
															|| (p3 == 3 && q3 == 38)) {

														%>
													<tr>
														<%int rowspan = project
																.getReportRowSpan(typeAssist);%>
														<%if (mark3 % 4 == 1) {%>
														<td width="9%" rowspan="<%=rowspan*4%>"><strong> <%=fiscalYearRange%></strong>
														</td>
														<%}%>
														<td width="6%" rowspan="<%=rowspan%>"><strong> <%int temp = 0;
														temp = mark3 % 4;
														if (temp == 0)
															temp = 4;

														%> Q<%=temp%></strong></td>



														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />
													</tr>
													<bean:define id="records" name="records"
														type="org.digijava.module.aim.helper.AdvancedReport"
														toScope="request" />
													<jsp:include page="termView3.jsp" />



													<%
													}
													if (q3 == 39) {

														%>
													<tr>
														<td width="12%" colspan="2"><strong> Total</strong></td>
														<bean:define id="ampFund" name="ampFund"
															type="org.digijava.module.aim.helper.AmpFund"
															toScope="request" />
														<jsp:include page="htmlFundView.jsp" />
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
								</td>
							</tr>
						</logic:iterate>

						<tr>
							<td
								colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
							<table width="100%" border="1" bordercolor="#C0C0C0"
								cellspacing=1 cellpadding=1 valign=top align=center
								style="border-collapse:collapse">
								<tr>
									<td align="left"
										colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" />><b>
									<bean:write name="levels" property="name" /> &nbsp;Total</b></td>
								</tr>
								<tr>
									<logic:equal name="aimAdvancedReportForm" property="option"
										value="A">
										<td width="12%"><strong>Year</strong></td>
										<logic:equal name="aimAdvancedReportForm"
											property="reportType" value="donor">
											<logic:equal name="typeAssist" value="true">
												<td width="13%"><strong>Type of Assistance</strong></td>
											</logic:equal>
										</logic:equal>
									</logic:equal>



									<logic:equal name="aimAdvancedReportForm" property="option"
										value="Q">
										<td width="15%" colspan="2"><strong>Year</strong></td>
										<logic:equal name="aimAdvancedReportForm"
											property="reportType" value="donor">
											<logic:equal name="typeAssist" value="true">
												<td colspan="2" width="13%"><strong>Type of Assistance</strong></td>
											</logic:equal>
										</logic:equal>

									</logic:equal>



									<logic:equal name="aimAdvancedReportForm" property="acCommFlag"
										value="true">
										<td width="12%"><strong>Actual Commitment</strong></td>
									</logic:equal>
									<logic:equal name="aimAdvancedReportForm" property="acDisbFlag"
										value="true">
										<td width="12%"><strong>Actual Disbursement</strong></td>
									</logic:equal>
									<logic:equal name="aimAdvancedReportForm" property="acExpFlag"
										value="true">
										<td width="12%"><strong>Actual Expenditure</strong></td>
									</logic:equal>
									<logic:equal name="aimAdvancedReportForm" property="plCommFlag"
										value="true">
										<td width="13%"><strong>Planned Commitment</strong></td>
									</logic:equal>
									<logic:equal name="aimAdvancedReportForm" property="plDisbFlag"
										value="true">
										<td width="13%"><strong>Planned Disbursement</strong></td>
									</logic:equal>
									<logic:equal name="aimAdvancedReportForm" property="plExpFlag"
										value="true">
										<td width="13%"><strong>Planned Expenditure</strong></td>
									</logic:equal>
									<logic:equal name="aimAdvancedReportForm" property="acBalFlag"
										value="true">
										<td width="13%"><strong>UnDisbursed</strong></td>
									</logic:equal>
								</tr>
								<%
								int a4 = 1;
								int b4 = 1;

								%>
								<logic:equal name="aimAdvancedReportForm" property="option"
									value="A">
									<logic:iterate name="aimAdvancedReportForm"
										property="fiscalYearRange" id="fiscalYearRange">
										<logic:iterate name="levels" property="fundSubTotal"
											id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
											<%
if ((a4 == 1 && b4 == 1)
													|| (a4 == 2 && b4 == 6)
													|| (a4 == 3 && b4 == 11)) {

												%>
											<tr>
												<td width="12%"><strong> <%=fiscalYearRange%></strong></td>
												<bean:define id="ampFund" name="ampFund"
													type="org.digijava.module.aim.helper.AmpFund"
													toScope="request" />
												<jsp:include page="htmlFundView.jsp" />
											</tr>
											<%
											}
											if (b4 == 12) {

												%>
											<tr>
												<td width="12%"><strong> Total</strong></td>
												<bean:define id="ampFund" name="ampFund"
													type="org.digijava.module.aim.helper.AmpFund"
													toScope="request" />
												<jsp:include page="htmlFundView.jsp" />
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
int p4 = 1;
								int q4 = 1;

								%>
								<logic:equal name="aimAdvancedReportForm" property="option"
									value="Q">
									<logic:iterate name="aimAdvancedReportForm"
										property="fiscalYearRange" id="fiscalYearRange">
										<%int mark4 = 0;

										%>
										<logic:iterate name="hierarchy" property="fundSubTotal"
											id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
											<%
											mark4++;
											if ((p4 == 1 && q4 == 1)
													|| (p4 == 1 && q4 == 2)
													|| (p4 == 1 && q4 == 3)
													|| (p4 == 1 && q4 == 4)
													|| (p4 == 2 && q4 == 18)
													|| (p4 == 2 && q4 == 19)
													|| (p4 == 2 && q4 == 20)
													|| (p4 == 2 && q4 == 21)
													|| (p4 == 3 && q4 == 35)
													|| (p4 == 3 && q4 == 36)
													|| (p4 == 3 && q4 == 37)
													|| (p4 == 3 && q4 == 38)) {

												%>
											<tr>
												<td width="9%"><strong> <%=fiscalYearRange%></strong></td>
												<td width="6%"><strong> <%int temp4 = 0;
												temp4 = mark4 % 4;
												if (temp4 == 0)
													temp4 = 4;

												%> Q<%=temp4%></strong></td>
												<bean:define id="ampFund" name="ampFund"
													type="org.digijava.module.aim.helper.AmpFund"
													toScope="request" />
												<jsp:include page="htmlFundView.jsp" />
											</tr>
											<%
											}
											if (q4 == 39) {

												%>
											<tr>
												<td width="15%" colspan="2"><strong> Total</strong></td>
												<bean:define id="ampFund" name="ampFund"
													type="org.digijava.module.aim.helper.AmpFund"
													toScope="request" />
												<jsp:include page="htmlFundView.jsp" />
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
							</td>
						</tr>
						<%-- 	Hierarchy 2 ends here	--%>

						<%-- 	Hierarchy 3 begins here	--%>
						<logic:notEmpty name="levels" property="levels">
							<jsp:include page="htmlAdvancedReportHierarchy2.jsp" />
						</logic:notEmpty>
					</logic:iterate>
				</logic:notEmpty>
				<%-- 	Hierarchy 3 ends here	--%>

				<tr>
					<td
						colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
					<table width="100%" border="1" bordercolor="#C0C0C0" cellspacing=1
						cellpadding=1 valign=top align=center
						style="border-collapse:collapse">
						<tr>
							<td align="left"
								colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" />><b>
							<bean:write name="hierarchy" property="name" /> &nbsp;Total</b></td>
						</tr>
						<tr>
							<logic:equal name="aimAdvancedReportForm" property="option"
								value="A">
								<td width="12%"><strong>Year </strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="option"
								value="Q">
								<td width="15%" colspan="2"><strong>Year</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acCommFlag"
								value="true">
								<td width="12%"><strong>Actual Commitment</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acDisbFlag"
								value="true">
								<td width="12%"><strong>Actual Disbursement</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acExpFlag"
								value="true">
								<td width="12%"><strong>Actual Expenditure</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plCommFlag"
								value="true">
								<td width="13%"><strong>Planned Commitment</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plDisbFlag"
								value="true">
								<td width="13%"><strong>Planned Disbursement</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plExpFlag"
								value="true">
								<td width="13%"><strong>Planned Expenditure</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acBalFlag"
								value="true">
								<td width="13%"><strong>UnDisbursed</strong></td>
							</logic:equal>
						</tr>
						<%
int a7 = 1;
						int b7 = 1;

						%>
						<logic:equal name="aimAdvancedReportForm" property="option"
							value="A">
							<logic:iterate name="aimAdvancedReportForm"
								property="fiscalYearRange" id="fiscalYearRange">
								<logic:iterate name="hierarchy" property="fundSubTotal"
									id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
									<%
if ((a7 == 1 && b7 == 1)
											|| (a7 == 2 && b7 == 6)
											|| (a7 == 3 && b7 == 11)) {

										%>
									<tr>
										<td width="12%"><strong> <%=fiscalYearRange%></strong></td>
										<bean:define id="ampFund" name="ampFund"
											type="org.digijava.module.aim.helper.AmpFund"
											toScope="request" />
										<jsp:include page="htmlFundView.jsp" />
									</tr>
									<%
}
									if (b7 == 12) {

										%>
									<tr>
										<td width="12%"><strong> Total</strong></td>
										<bean:define id="ampFund" name="ampFund"
											type="org.digijava.module.aim.helper.AmpFund"
											toScope="request" />
										<jsp:include page="htmlFundView.jsp" />
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
int p7 = 1;
						int q7 = 1;

						%>
						<logic:equal name="aimAdvancedReportForm" property="option"
							value="Q">
							<logic:iterate name="aimAdvancedReportForm"
								property="fiscalYearRange" id="fiscalYearRange">
								<%int mark7 = 0;

								%>
								<logic:iterate name="hierarchy" property="fundSubTotal"
									id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
									<%
mark7++;
									if ((p7 == 1 && q7 == 1)
											|| (p7 == 1 && q7 == 2)
											|| (p7 == 1 && q7 == 3)
											|| (p7 == 1 && q7 == 4)
											|| (p7 == 2 && q7 == 18)
											|| (p7 == 2 && q7 == 19)
											|| (p7 == 2 && q7 == 20)
											|| (p7 == 2 && q7 == 21)
											|| (p7 == 3 && q7 == 35)
											|| (p7 == 3 && q7 == 36)
											|| (p7 == 3 && q7 == 37)
											|| (p7 == 3 && q7 == 38)) {

										%>
									<tr>
										<td width="9%"><strong> <%=fiscalYearRange%></strong></td>
										<td width="6%"><strong> <%int temp7 = 0;
										temp7 = mark7 % 4;
										if (temp7 == 0)
											temp7 = 4;

										%> Q<%=temp7%></strong></td>
										<bean:define id="ampFund" name="ampFund"
											type="org.digijava.module.aim.helper.AmpFund"
											toScope="request" />
										<jsp:include page="htmlFundView.jsp" />
									</tr>
									<%
}
									if (q7 == 39) {

										%>
									<tr>
										<td width="15%" colspan="2"><strong> Total</strong></td>
										<bean:define id="ampFund" name="ampFund"
											type="org.digijava.module.aim.helper.AmpFund"
											toScope="request" />
										<jsp:include page="htmlFundView.jsp" />
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
					</td>
				</tr>
			</logic:iterate>
		</logic:iterate>

		<tr>
			<td
				colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
			<table width="100%" border="1" bordercolor="#C0C0C0" cellspacing=1
				cellpadding=1 valign=top align=center
				style="border-collapse:collapse">
				<tr>
					<td align="left"
						colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns" />
						class="head2-name"><b> Grand Total</b></td>
				</tr>
				<tr>
					<logic:equal name="aimAdvancedReportForm" property="option"
						value="A">
						<td width="12%"><strong>Year</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="option"
						value="Q">
						<td width="15%" colspan="2"><strong>Year</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag"
						value="true">
						<td width="12%"><strong>Actual Commitment</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag"
						value="true">
						<td width="12%"><strong>Actual Disbursement</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag"
						value="true">
						<td width="12%"><strong>Actual Expenditure</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag"
						value="true">
						<td width="13%"><strong>Planned Commitment</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag"
						value="true">
						<td width="13%"><strong>Planned Disbursement</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag"
						value="true">
						<td width="13%"><strong>Planned Expenditure</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acBalFlag"
						value="true">
						<td width="13%"><strong>UnDisbursed</strong></td>
					</logic:equal>
				</tr>
				<%
int a8 = 1;
				int b8 = 1;

				%>
				<logic:equal name="aimAdvancedReportForm" property="option"
					value="A">
					<logic:iterate name="aimAdvancedReportForm"
						property="fiscalYearRange" id="fiscalYearRange">
						<logic:iterate name="multiReport" property="fundTotal"
							id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
							<%
if ((a8 == 1 && b8 == 1) || (a8 == 2 && b8 == 6)
									|| (a8 == 3 && b8 == 11)) {

								%>
							<tr>
								<td width="12%"><strong> <%=fiscalYearRange%></strong></td>
								<bean:define id="ampFund" name="ampFund"
									type="org.digijava.module.aim.helper.AmpFund" toScope="request" />
								<jsp:include page="htmlFundView.jsp" />
							</tr>
							<%
}
							if (b8 == 12) {

								%>
							<tr>
								<td width="12%"><strong> Total</strong></td>
								<bean:define id="ampFund" name="ampFund"
									type="org.digijava.module.aim.helper.AmpFund" toScope="request" />
								<jsp:include page="htmlFundView.jsp" />
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
int p8 = 1;
				int q8 = 1;

				%>
				<logic:equal name="aimAdvancedReportForm" property="option"
					value="Q">
					<logic:iterate name="aimAdvancedReportForm"
						property="fiscalYearRange" id="fiscalYearRange">
						<%int mark8 = 0;

						%>
						<logic:iterate name="multiReport" property="fundTotal"
							id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
							<%
mark8++;
							if ((p8 == 1 && q8 == 1) || (p8 == 1 && q8 == 2)
									|| (p8 == 1 && q8 == 3)
									|| (p8 == 1 && q8 == 4)
									|| (p8 == 2 && q8 == 18)
									|| (p8 == 2 && q8 == 19)
									|| (p8 == 2 && q8 == 20)
									|| (p8 == 2 && q8 == 21)
									|| (p8 == 3 && q8 == 35)
									|| (p8 == 3 && q8 == 36)
									|| (p8 == 3 && q8 == 37)
									|| (p8 == 3 && q8 == 38)) {

								%>
							<tr>
								<td width="9%"><strong> <%=fiscalYearRange%></strong></td>
								<td width="6%"><strong> <%int temp8 = 0;
								temp8 = mark8 % 4;
								if (temp8 == 0)
									temp8 = 4;

								%> Q<%=temp8%></strong></td>
								<bean:define id="ampFund" name="ampFund"
									type="org.digijava.module.aim.helper.AmpFund" toScope="request" />
								<jsp:include page="htmlFundView.jsp" />
							</tr>
							<%
}
							if (q8 == 39) {

								%>
							<tr>
								<td width="15%" colspan="2"><strong> Total</strong></td>
								<bean:define id="ampFund" name="ampFund"
									type="org.digijava.module.aim.helper.AmpFund" toScope="request" />
								<jsp:include page="htmlFundView.jsp" />
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
			</td>
		</tr>
	</logic:notEmpty>

	<logic:empty name="aimAdvancedReportForm" property="multiReport">
		<tr>
			<td
				colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>
				align="center"><b> <digi:trn key="aim:noRecords">No Records</digi:trn>
			</b></td>
		</tr>
	</logic:empty>
</table>
