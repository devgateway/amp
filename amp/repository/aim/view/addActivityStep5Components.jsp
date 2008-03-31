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
<%@page import="org.digijava.module.aim.form.EditActivityForm"%>
<digi:instance property="aimEditActivityForm"/>


								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">
										<b><digi:trn key="aim:components">Components</digi:trn></b></a>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>

									<tr><td align="left">
										<table width="100%" cellSpacing=5 cellPadding=0 border=0 class="box-border-nopadding">

										<logic:notEmpty name="aimEditActivityForm" property="selectedComponents">
																	<tr><td>
																		<b><field:display name="Components Grand Total Commitments" feature="Components">
																		&nbsp;&nbsp;
																			<digi:trn key="aim:commitments">Commitments</digi:trn> - (
																			<digi:trn key="aim:grantTotalActualAllocation">Grand Total actual
																			allocation</digi:trn> = 
																				<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getTotalCommitted()%>
																			
																			<c:out value="${aimEditActivityForm.currCode}"/>)
																		<br/></field:display>
																		<field:display name="Components Grand Total Disbursements" feature="Components">
																				&nbsp;&nbsp;
																			<digi:trn key="aim:disbursements">Disbursements</digi:trn> - (
																			<digi:trn key="aim:totalActualToDate">Total actual to date
																			</digi:trn> =
																			<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getTotalDisbursed()%>
																			
																			<c:out value="${aimEditActivityForm.currCode}"/>)
																		<br/>
																		</field:display>
																		<field:display name="Components Grand Total Expenditures" feature="Components">
																		&nbsp;&nbsp;
																			<digi:trn key="aim:expenditures">Expenditures</digi:trn> - (
																			<digi:trn key="aim:totalActualToDate">Total actual to date
																			</digi:trn> =
																			<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getTotalExpenditures()%>
																			
																			<c:out value="${aimEditActivityForm.currCode}"/>)
																		<br/>
																		</field:display>
																		&nbsp;&nbsp;

																		<font
																		<c:if test="${aimEditActivityForm.totalCommitmentsDouble < aimEditActivityForm.compTotalDisb }">
																		 color="RED"
																		</c:if>
																		>
																		<digi:trn key="aim:totalComponentActualDisbursement">Component Grand Total Actual Disbursements</digi:trn>=
																			<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getCompTotalDisb()%>
																	
																		<c:out value="${aimEditActivityForm.currCode}"/>
																		</font>
																		</b></td></tr>

											<logic:iterate name="aimEditActivityForm" property="selectedComponents"
											id="selComponents" type="org.digijava.module.aim.helper.Components">

												<tr><td align="center">
													<table width="98%" cellSpacing=1 cellPadding=4 vAlign="top" align="center" border=0
													class="box-border-nopadding">
														<tr bgcolor="#fffffc">
															<td vAlign="center" align="left" width="95%">
																<html:multibox property="selComp">
																	<c:out value="${selComponents.componentId}"/>
																</html:multibox>
																<a title="<digi:trn key="aim:TitleofComponent">Title of the project component specified</digi:trn>">											<b>
																<digi:trn key="aim:TitleofComponent">Component Title</digi:trn></a> :
																<c:out value="${selComponents.title}"/></b>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																<field:display name="Edit Components Link" feature="Components">
																<a href="javascript:editFunding('<bean:write name="selComponents"
																property="componentId"/>')"><digi:trn key="aim:edit">Edit</digi:trn></a>
																</field:display>
																<br>
																<digi:trn key="aim:description">Description</digi:trn>:&nbsp;<c:out value="${selComponents.description}"/>
																<br>
															</td>
														</tr>

																<tr><td>
																<!-- Component funding details -->
																	<table width="100%" cellSpacing=1 cellPadding=3 border=0
																	bgcolor="#d7eafd">
																		<logic:notEmpty name="selComponents" property="commitments">
																		<tr><td>
																		<b><field:display name="Grand Total Commitments" feature="Components">
																		&nbsp;&nbsp;
																			<digi:trn key="aim:commitments">Commitments</digi:trn>
																		</field:display>
																		</b></td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellSpacing=1 cellPadding=3 border=0
																			bgcolor="#eeeeee">
																				<tr>
																					<field:display name="Components Actual/Planned Commitments" feature="Components"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																					<field:display name="Components Total Amount Commitments" feature="Components"><td><digi:trn key="aim:totalAmount">Total Amount</digi:trn></td></field:display>
																					<field:display name="Components Currency Commitments" feature="Components"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																					<field:display name="Components Date Commitments" feature="Components"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>
																					<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																						<field:display name="Components Perspective Commitments" feature="Components"><td><digi:trn key="aim:perspective">Perspective</digi:trn></td></field:display>
																					</logic:equal>
																				</tr>
																				<logic:iterate name="selComponents" property="commitments"
																				id="commitment"
																				type="org.digijava.module.aim.helper.FundingDetail"> 																						           <!-- L2 START-->
																					<tr bgcolor="#ffffff">
																						<field:display name="Components Actual/Planned Commitments" feature="Components"><td>
																							<digi:trn key="aim:${commitment.adjustmentTypeName}"><c:out value="${commitment.adjustmentTypeName}"/></digi:trn></td></field:display>
																						<field:display name="Components Total Amount Commitments" feature="Components"><td align="right">
																							<FONT color=blue>*</FONT>
																							<c:out value="${commitment.transactionAmount}"/>
																						</td></field:display>
																						<field:display name="Components Currency Commitments" feature="Components"><td>
																							<c:out value="${commitment.currencyCode}"/>
																						</td></field:display>
																						<field:display name="Components Date Commitments" feature="Components">
																						<td>
																							<c:out value="${commitment.transactionDate}"/>
																						</td></field:display>
																						<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																							<field:display name="Components Perspective Commitments" feature="Components">
																								<td>
																									<digi:trn key='<%="aim:"+commitment.getPerspectiveNameTrimmed() %>'>
																											<bean:write name="commitment" property="perspectiveName"/>
																									</digi:trn>
																								</td>
																							</field:display>
																						</logic:equal>
																					</tr>
																				</logic:iterate>	<!-- L2 END-->
																			</table>
																		</td></tr>
																	</logic:notEmpty>
																	<logic:notEmpty name="selComponents" property="disbursements">
																	<tr>
																	<td>
																	<field:display name="Grand Total Disbursements" feature="Components">
																				&nbsp;&nbsp;
																		<b>	<digi:trn key="aim:disbursements">Disbursements</digi:trn> </b>

																	</field:display>
																	</td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellSpacing=1 cellPadding=3 border=0
																			bgcolor="#eeeeee">
																				<tr>

																					<field:display name="Components Actual/Planned Disbursements" feature="Components"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																					<field:display name="Components Total Amount Disbursements" feature="Components"><td><digi:trn key="aim:totalAmount">Total Amount</digi:trn></td></field:display>
																					<field:display name="Components Currency Disbursements" feature="Components"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																					<field:display name="Components Date Disbursements" feature="Components"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>
																					<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																						<field:display name="Components Perspective Disbursements" feature="Components"><td><digi:trn key="aim:perspective">Perspective</digi:trn></td></field:display>
																					</logic:equal>
																				</tr>
																				<logic:iterate name="selComponents" property="disbursements"
																				id="disbursement"
																				type="org.digijava.module.aim.helper.FundingDetail">
																				<!-- L3 START-->
																					<tr bgcolor="#ffffff">
																						<field:display name="Components Actual/Planned Disbursements" feature="Components"><td>
																							<digi:trn key="aim:${disbursement.adjustmentTypeName}"><c:out value="${disbursement.adjustmentTypeName}"/></digi:trn>
																						</td></field:display>
																						<field:display name="Components Total Amount Disbursements" feature="Components"><td align="right">
																							<FONT color=blue>*</FONT>
																							<c:out value="${disbursement.transactionAmount}"/>
																						</td></field:display>
																						<field:display name="Components Currency Disbursements" feature="Components"><td>
																							<c:out value="${disbursement.currencyCode}"/>
																						</td></field:display>
																						<field:display name="Components Date Disbursements" feature="Components">
																						<td>
																							<c:out value="${disbursement.transactionDate}"/>
																						</td></field:display>
																						<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																							<field:display name="Components Perspective Disbursements" feature="Components">
																							<td>
																								<digi:trn key='<%="aim:"+disbursement.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="disbursement" property="perspectiveName"/>
																								</digi:trn>
																							</td></field:display>
																						</logic:equal>
																					</tr>
																				</logic:iterate>
																				<!-- L3 END-->
																			</table>
																		</td></tr>

																	</logic:notEmpty>
																	<logic:notEmpty name="selComponents" property="expenditures">
																		<tr><td><b>
																			<digi:trn key="aim:expenditures">Expenditures</digi:trn>
																	</b>
																		</td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellSpacing=1 cellPadding=3 border=0
																			bgcolor="#eeeeee">
																				<tr>
																					<field:display name="Components Actual/Planned Expenditures" feature="Components"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																					<field:display name="Components Total Amount Expenditures" feature="Components"><td><digi:trn key="aim:totalAmount">Total Amount</digi:trn></td></field:display>
																					<field:display name="Components Currency Expenditures" feature="Components"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																					<field:display name="Components Date Expenditures" feature="Components"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>
																					<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																						<field:display name="Components Perspective Expenditures" feature="Components"><td><digi:trn key="aim:perspective">Perspective</digi:trn></td></field:display>
																					</logic:equal>
																				</tr>
																				<logic:iterate name="selComponents" property="expenditures"
																				id="expenditure"
																				type="org.digijava.module.aim.helper.FundingDetail">
																				<!-- L4 START-->
																				<tr bgcolor="#ffffff">
																					<field:display name="Components Actual/Planned Expenditures" feature="Components"><td>
																						<digi:trn key="aim:${expenditure.adjustmentTypeName}"><c:out value="${expenditure.adjustmentTypeName}"/></digi:trn>
																					</td></field:display>
																					<field:display name="Components Total Amount Expenditures" feature="Components">
																					<td align="right">
																						<FONT color=blue>*</FONT>
																						<c:out value="${expenditure.transactionAmount}"/>
																					</td></field:display>
																					<field:display name="Components Currency Expenditures" feature="Components">
																					<td>
																						<c:out value="${expenditure.currencyCode}"/>
																					</td></field:display>
																					<field:display name="Components Date Expenditures" feature="Components">
																					<td>
																						<c:out value="${expenditure.transactionDate}"/>
																					</td></field:display>
																					<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																						<field:display name="Components Perspective Expenditures" feature="Components">
																						<td>
																								<digi:trn key='<%="aim:"+expenditure.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="expenditure" property="perspectiveName"/>
																								</digi:trn>
																						</td></field:display>
																					</logic:equal>
																				</tr>
																			</logic:iterate>
																			<!-- L4 END-->
																		</table>
																	</td></tr>

																</logic:notEmpty>

															</table>
															<logic:notEmpty name="selComponents" property="commitments">
															<TR><TD>
																<FONT color=blue>*
																	<digi:trn key="aim:allTheAmountsInThousands">
																		All the amounts are in thousands (000)
				  													</digi:trn>
																</FONT>
															</TD></TR>
															</logic:notEmpty>
														</td></tr>
													</tr>
													<feature:display name="Physical Progress" module="Components">
													<tr>
														<td colspan="2">
															<table width="100%" cellPadding=1 cellSpacing=1 vAlign="top" border=0
															class="box-border-nopadding">
															<tr><td width="100%" bgcolor="#dddddd" height="15">
																<a title="<digi:trn key="aim:physicalProgressTitleDesc">Measurable task done on a
																project to achieve component objectives</digi:trn>">
																	<digi:trn key="aim:physicalProgres">Physical Progress</digi:trn>
																</a>
															</td></tr>
															<field:display name="Add Physical Progress Link" feature="Physical Progress">
															<c:if test="${empty selComponents.phyProgress}">
																<tr><td colspan="2"><b>
																	<a href="javascript:
																	addPhyProgess(-1,<c:out value="${selComponents.componentId}"/>)">
																	<digi:trn key="aim:addPhysicalProgress">Add Physical Progress</digi:trn></a>
																	</b>
																</td></tr>
															</c:if>
															</field:display>
															<c:if test="${!empty selComponents.phyProgress}">
															<c:forEach var="phyProg" items="${selComponents.phyProgress}">
																<tr><td>
																	<table width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="left">
																		<tr>
																		<field:display name="Add Physical Progress Link" feature="Physical Progress">
																		<td vAlign="center" align="left" width="95%">
																			<a href="javascript:addPhyProgess(<bean:write name="phyProg"
																			property="pid" />,<c:out value="${selComponents.componentId}"/>)">
																				<bean:write name="phyProg" property="title" /> -
																				<bean:write name="phyProg" property="reportingDate" />
																			</a>
																		</td>
																		</field:display>
																		<field:display name="Remove Physical Progress Link" feature="Physical Progress">
																		<td align="right">
																			<bean:define id="id" property="pid" name="phyProg"/>
																			<bean:define id="compId" property="componentId" name="selComponents"/>
																			<% String url1 =
																			"/removeSelPhyProg.do~edit=true~pid="+id+"~cid="+compId;%>
																			<digi:link href="<%=url1%>">
																				<digi:img src="module/cms/images/deleteIcon.gif" border="0"
																				alt="Delete this physical progress"/>
																			</digi:link>
																		</td>																			
																		</field:display>
																		</tr>
																	</table>
																</td></tr>
															</c:forEach>
															<field:display name="Add Physical Progress Link" feature="Physical Progress">
																<tr><td>
																
																	<table cellSpacing=2 cellPadding=2>
																		<tr><td>
																			<b>
																				<a href="javascript:
																				addPhyProgess(-1,<c:out value="${selComponents.componentId}"/>)">
																				<digi:trn key="aim:addPhysicalProgress">Add Physical Progress</digi:trn></a>
																			</b>
																		</td></tr>
																	</table>
																</td></tr>
																</field:display>
															</c:if>
															</table>
														</td></tr>
														</feature:display>
													</table>
												</td></tr>
											</logic:iterate>
											<tr><td align="center">
												<table cellSpacing=2 cellPadding=2>
													<tr><td>
														<field:display name="Add Components Button" feature="Components">
														<html:button  styleClass="buton" property="submitButton" onclick="addComponents()">
															<digi:trn key="btn:addComponents">Add Components</digi:trn>
														</html:button>
														</field:display>
														 &nbsp;&nbsp;&nbsp;
														 <field:display name="Remove Components Button" feature="Components">
														<html:button  styleClass="buton" property="submitButton" onclick="removeSelComponents()">
															<digi:trn key="btn:removeComponents">Remove Components</digi:trn>
														</html:button>
														</field:display>
													</td></tr>
												</table>
											</td></tr>
										</table>
									</logic:notEmpty>
									<logic:empty name="aimEditActivityForm" property="selectedComponents">
									<field:display name="Add Components Button" feature="Components">
										<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
											<tr><td>
												<html:button  styleClass="buton" property="submitButton" onclick="addComponents()">
														<digi:trn key="btn:addComponents">Add Components</digi:trn>
												</html:button>
											</td></tr>
										</table>
									</field:display>
									</logic:empty>
