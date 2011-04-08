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
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<%@page import="org.digijava.module.aim.util.DbUtil"%>
<%@page import="org.digijava.module.aim.form.ActivityForm"%><digi:instance property="aimEditActivityForm"/>


<c:set var="trnAddComponents">
    <digi:trn>Add Components</digi:trn>
</c:set>
<c:set var="trnRemoveComponents">
    <digi:trn>Remove Components</digi:trn>
</c:set>
								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">
										<b><digi:trn key="aim:components">Components</digi:trn></b></a>
									</td></tr>
									<tr><td>&nbsp;
										
									</td></tr>

									<tr><td align="left">
										<table width="100%" cellSpacing=5 cellpadding="0" border="0" class="box-border-nopadding">

											<logic:notEmpty name="aimEditActivityForm" property="components.selectedComponents">
																	<tr><td>
                                                                           &nbsp;&nbsp;<b> <digi:trn>Select currency </digi:trn></b>

                                                                            <html:select property="fundingCurrCode" styleClass="inp-text" onchange="totalsPage()">
                                                                                <c:forEach var="currency" items="${aimEditActivityForm.funding.validcurrencies}">
                                                                                    <c:if test="${currency.currencyCode!=aimEditActivityForm.fundingCurrCode}">
                                                                                        <option value="<c:out value="${currency.currencyCode}"/>">
                                                                                    </c:if>
                                                                                    <c:if test="${currency.currencyCode==aimEditActivityForm.fundingCurrCode}">
                                                                                        <option value="<c:out value="${currency.currencyCode}"/>" selected="selected">
                                                                                        </c:if>
                                                                                        <c:out value="${currency.currencyName}" />
                                                                                    </option>
                                                                                </c:forEach>

                                                                            </html:select>
                                                                                    <br/>
																		<b><field:display name="Components Grand Total Commitments" feature="Activity - Component Step">
																		&nbsp;&nbsp;
                                                                        <span id="comp_comms">
																			<digi:trn key="aim:commitments">Commitments</digi:trn> - (
																			<digi:trn key="aim:grantTotalActualAllocation">Grand Total actual
																			allocation</digi:trn> = 
																				<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getFunding().getTotalCommitments()%>
																			
                                                                        <c:out value="${aimEditActivityForm.currCode}"/>)
                                                                        </span>
																		<br/></field:display>
																		<field:display name="Components Grand Total Disbursements" feature="Activity - Component Step">
																				&nbsp;&nbsp;<span id="comp_disb">
																			<digi:trn key="aim:disbursements">Disbursements</digi:trn> - (
																			<digi:trn key="aim:totalActualToDate">Total actual to date
																			</digi:trn> =
																			<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getFunding().getTotalDisbursements()%>
																			
                                                                                <c:out value="${aimEditActivityForm.currCode}"/>)</span>
																		<br/>
																		</field:display>
																		<field:display name="Components Grand Total Expenditures" feature="Activity - Component Step">
																		&nbsp;&nbsp;<span id="comp_expn">
																			<digi:trn key="aim:expenditures">Expenditures</digi:trn> - (
																			<digi:trn key="aim:totalActualToDate">Total actual to date
																			</digi:trn> =
																			<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getFunding().getTotalExpenditures()%>
																			
                                                                        <c:out value="${aimEditActivityForm.currCode}"/>)</span>
																		<br/>
																		</field:display>
																		&nbsp;&nbsp;

																		<font
																		<c:if test="${aimEditActivityForm.funding.totalCommitmentsDouble < aimEditActivityForm.components.compTotalDisb }">
																		 color="RED"
																		</c:if>
																		>
                                                                            <span id="comp_totalDisb">
																		<digi:trn key="aim:totalComponentActualDisbursement">Component Grand Total Actual Disbursements</digi:trn>=
																			<aim:formatNumber value="<%=((org.digijava.module.aim.form.EditActivityForm) pageContext.getAttribute("aimEditActivityForm")).getComponents().getCompTotalDisb()%>"/>
																	
																		<c:out value="${aimEditActivityForm.currCode}"/></span>
																		</font>
																		</b></td></tr>

											<logic:iterate name="aimEditActivityForm" property="components.selectedComponents"
											id="selComponents" type="org.digijava.module.aim.helper.Components">

												<tr><td align="center">
													<table width="98%" cellspacing="1" cellPadding=4 vAlign="top" align="center" border="0"
													class="box-border-nopadding">
														<tr>
															<td>
																<logic:iterate id="type" name="aimEditActivityForm" property="components.allCompsType" type="org.digijava.module.aim.dbentity.AmpComponentType">
																	<b>
																		<%if (selComponents.getType_Id().longValue()==type.getType_id().longValue()){%>
																	 		<digi:trn key="aim:type">Type:</digi:trn> <%=type.getName()%>	
																		<%} %>
																	</b>
																</logic:iterate>
															</td>
														</tr>
														<tr bgcolor="#fffffc">
															<td vAlign="center" align="left" width="95%">
																<html:multibox property="components.selComp"  styleId="selComp">
																	<c:out value="${selComponents.componentId}"/>
																</html:multibox>
																
																<a title="<digi:trn key="aim:TitleofComponent">Title of the project component specified</digi:trn>">											<b>
																<digi:trn key="aim:TitleofComponent">Component Title</digi:trn></a> :
																<b><c:out value="${selComponents.title}"/></b>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																
																
																<field:display name="Edit Components Link" feature="Activity - Component Step">
																	<a href="javascript:editFunding('<bean:write name="selComponents" property="componentId"/>')"><digi:trn key="aim:edit">Edit</digi:trn></a>
																</field:display>
																<br>
																<digi:trn key="aim:description">Description</digi:trn>:&nbsp;<c:out value="${selComponents.description}"/>
																<br>
															</td>
														</tr>

																<tr><td>
																<!-- Component funding details -->
																	<table width="100%" cellspacing="1" cellPadding=3 border="0"
																	bgcolor="#d7eafd">
																		<logic:notEmpty name="selComponents" property="commitments">
																		<tr><td>
																		<b><field:display name="Grand Total Commitments" feature="Activity - Component Step">
																		&nbsp;&nbsp;
																			<digi:trn key="aim:commitments">Commitments</digi:trn>
																		</field:display>
																		</b></td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellspacing="1" cellPadding=3 border="0"
																			bgcolor="#eeeeee">
																				<tr>
																					<field:display name="Components Actual/Planned Commitments" feature="Activity - Component Step"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																					<field:display name="Components Amount Commitments" feature="Activity - Component Step"><td><digi:trn key="aim:totalCommitments">Total Commitments</digi:trn></td></field:display>
																					<field:display name="Components Currency Commitments" feature="Activity - Component Step"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																					<field:display name="Components Date Commitments" feature="Activity - Component Step"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>
																				</tr>
																				<logic:iterate name="selComponents" property="commitments"
																				id="commitment"
																				type="org.digijava.module.aim.helper.FundingDetail"> 																						           <!-- L2 START-->
																					<tr bgcolor="#ffffff">
																						<field:display name="Components Actual/Planned Commitments" feature="Activity - Component Step"><td>
																							<digi:trn key="aim:${commitment.adjustmentTypeName}"><c:out value="${commitment.adjustmentTypeName}"/></digi:trn></td></field:display>
																						<field:display name="Components Amount Commitments" feature="Activity - Component Step"><td align="left">
																							<FONT color=blue>*</FONT>
																							<c:out value="${commitment.transactionAmount}"/>
																						</td></field:display>
																						<field:display name="Components Currency Commitments" feature="Activity - Component Step"><td>
																							<c:out value="${commitment.currencyCode}"/>
																						</td></field:display>
																						<field:display name="Components Date Commitments" feature="Activity - Component Step">
																						<td>
																							<c:out value="${commitment.transactionDate}"/>
																						</td></field:display>
																					</tr>
																				</logic:iterate>	<!-- L2 END-->
																			</table>
																		</td></tr>
																	</logic:notEmpty>
																	<logic:notEmpty name="selComponents" property="disbursements">
																	<tr>
																	<td>
																	<field:display name="Grand Total Disbursements" feature="Activity - Component Step">
																				&nbsp;&nbsp;
																		<b>	<digi:trn key="aim:disbursements">Disbursements</digi:trn> </b>

																	</field:display>
																	</td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellspacing="1" cellPadding=3 border="0"
																			bgcolor="#eeeeee">
																				<tr>

																					<field:display name="Components Actual/Planned Disbursements" feature="Activity - Component Step"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																					<field:display name="Components Amount Disbursements" feature="Activity - Component Step"><td><digi:trn key="aim:totalDisbursements">Total Disbursements</digi:trn></td></field:display>
																					<field:display name="Components Currency Disbursements" feature="Activity - Component Step"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																					<field:display name="Components Date Disbursements" feature="Activity - Component Step"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>
																				</tr>
																				<logic:iterate name="selComponents" property="disbursements"
																				id="disbursement"
																				type="org.digijava.module.aim.helper.FundingDetail">
																				<!-- L3 START-->
																					<tr bgcolor="#ffffff">
																						<field:display name="Components Actual/Planned Disbursements" feature="Activity - Component Step"><td>
																							<digi:trn key="aim:${disbursement.adjustmentTypeName}"><c:out value="${disbursement.adjustmentTypeName}"/></digi:trn>
																						</td></field:display>
																						<field:display name="Components Amount Disbursements" feature="Activity - Component Step"><td align="left">
																							<FONT color=blue>*</FONT>
																							<c:out value="${disbursement.transactionAmount}"/>
																						</td></field:display>
																						<field:display name="Components Currency Disbursements" feature="Activity - Component Step"><td>
																							<c:out value="${disbursement.currencyCode}"/>
																						</td></field:display>
																						<field:display name="Components Date Disbursements" feature="Activity - Component Step">
																						<td>
																							<c:out value="${disbursement.transactionDate}"/>
																						</td></field:display>
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
																			<table width="100%" cellspacing="1" cellPadding=3 border="0"
																			bgcolor="#eeeeee">
																				<tr>
																					<field:display name="Components Actual/Planned Expenditures" feature="Activity - Component Step"><td><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></td></field:display>
																					<field:display name="Components Amount Expenditures" feature="Activity - Component Step"><td><digi:trn key="aim:totalExpenditures">Total Expenditures</digi:trn></td></field:display>
																					<field:display name="Components Currency Expenditures" feature="Activity - Component Step"><td><digi:trn key="aim:currency">Currency</digi:trn></td></field:display>
																					<field:display name="Components Date Expenditures" feature="Activity - Component Step"><td><digi:trn key="aim:date">Date</digi:trn></td></field:display>
																				</tr>
																				<logic:iterate name="selComponents" property="expenditures"
																				id="expenditure"
																				type="org.digijava.module.aim.helper.FundingDetail">
																				<!-- L4 START-->
																				<tr bgcolor="#ffffff">
																					<field:display name="Components Actual/Planned Expenditures" feature="Activity - Component Step"><td>
																						<digi:trn key="aim:${expenditure.adjustmentTypeName}"><c:out value="${expenditure.adjustmentTypeName}"/></digi:trn>
																					</td></field:display>
																					<field:display name="Components Amount Expenditures" feature="Activity - Component Step">
																					<td align="left">
																						<FONT color=blue>*</FONT>
																						<c:out value="${expenditure.transactionAmount}"/>
																					</td></field:display>
																					<field:display name="Components Currency Expenditures" feature="Activity - Component Step">
																					<td>
																						<c:out value="${expenditure.currencyCode}"/>
																					</td></field:display>
																					<field:display name="Components Date Expenditures" feature="Activity - Component Step">
																					<td>
																						<c:out value="${expenditure.transactionDate}"/>
																					</td></field:display>
																				</tr>
																			</logic:iterate>
																			<!-- L4 END-->
																		</table>
																	</td></tr>

																</logic:notEmpty>

															</table>
															<logic:notEmpty name="selComponents" property="commitments">
															<TR><TD>
																<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
																<FONT color=blue>*
																	<digi:trn key="aim:allTheAmountsInThousands">
																		All the amounts are in thousands (000)
				  													</digi:trn>
																</FONT>
																</gs:test>
															</TD></TR>
															</logic:notEmpty>
														</td></tr>
													</tr>
													<feature:display name="Physical Progress" module="Components">
													<tr>
														<td colspan="2">
															<table width="100%" cellpadding="1" cellspacing="1" vAlign="top" border="0"
															class="box-border-nopadding">
															<tr>
																<td width="100%" bgcolor="#dddddd" height="15">
																	<a title="<digi:trn key="aim:physicalProgressTitleDesc">Measurable task done on a
																	project to achieve component objectives</digi:trn>">
																		<digi:trn key="aim:physicalProgres">Physical Progress</digi:trn>
																	</a>
																</td>
															</tr>
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
																	<table width="100%" cellspacing="1" cellpadding="1" vAlign="top" align="left">
																		<tr>
																		<field:display name="Add Physical Progress Link" feature="Physical Progress">
																		<td vAlign="top" align="left" width="35%">
																			<a href="javascript:addPhyProgess(<bean:write name="phyProg"
																			property="pid" />,<c:out value="${selComponents.componentId}"/>)">
																				<bean:write name="phyProg" property="title" /> -
																				<bean:write name="phyProg" property="reportingDate" />
																			</a>
																		</td>
																		</field:display>
																		<field:display name="Physical Progress Description" feature="Physical Progress">
																		<td valign="middle" align="left" width="60%">
																				<bean:write name="phyProg" property="description" />
																		</td>
																		</field:display>
																		<field:display name="Remove Physical Progress Link" feature="Physical Progress">
																		<td align="right">
																			<bean:define id="id" property="pid" name="phyProg"/>
																			<bean:define id="compId" property="componentId" name="selComponents"/>
																			<% String url1 =
																			"/removeSelPhyProg.do~edit=true~pid="+id+"~cid="+compId;%>
																			<digi:link href="<%=url1%>">
																				<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0"
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
														<field:display name="Add Components Button" feature="Activity - Component Step">
														<html:button  styleClass="dr-menu" property="submitButton" onclick="addComponents()" title="${trnAddComponents}">
															<digi:trn key="btn:addComponents">Add Components</digi:trn>
														</html:button>
														</field:display>
														 &nbsp;&nbsp;&nbsp;
														 <field:display name="Remove Components Button" feature="Activity - Component Step">
														<html:button  styleClass="dr-menu" property="submitButton" onclick="removeSelComponents()" title="${trnRemoveComponents}">
															<digi:trn key="btn:removeComponents">Remove Components</digi:trn>
														</html:button>
														</field:display>
													</td></tr>
												</table>
											</td></tr>
										</table>
										
									</logic:notEmpty>
									
									<logic:empty name="aimEditActivityForm" property="components.selectedComponents">
									<field:display name="Add Components Button" feature="Activity - Component Step">
										<table width="100%" cellspacing="1" cellPadding=5 class="box-border-nopadding">
											<tr><td>
												<html:button  styleClass="dr-menu" property="submitButton" onclick="addComponents()" title="${trnAddComponents}">
														<digi:trn key="btn:addComponents">Add Components</digi:trn>
												</html:button>
											</td></tr>
										</table>
									</field:display>
									</logic:empty>
									</td></tr>
</table>								