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

<!-- dynamic drive ajax tabs -->
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>


<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>


<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>
<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicTooltip.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />




<digi:instance property="aimEditActivityForm" />

					<module:display name="Issues" parentModule="PROJECT MANAGEMENT">
						<feature:display name="Issues" module="Issues">
							<field:display name="Issues" feature="Issues">
								<table width="95%" bgcolor="#f4f4f2">

									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:issuesForTheActivity">The issues for the activity</digi:trn>">
										<b><digi:trn key="aim:issues">Issues</digi:trn></b></a>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="issues.issues">
											<table width="100%" cellSpacing=1 cellPadding=4 class="box-border-nopadding">
												<tr><td align="center">
													<table width="98%" cellSpacing=1 cellPadding=2 vAlign="top" align="center" bgcolor="#dddddd">
														<%--<tr bgcolor="#d7eafd">--%>
														<% int i = 1;
															String rowClass = "";
														%>

														<logic:iterate name="aimEditActivityForm" property="issues.issues"
														id="issues" type="org.digijava.module.aim.helper.Issues">
														<% if ((i % 2) != 0) {
															rowClass = "rowAlternate";
															} else {
															rowClass = "rowNormal";
															}
															i++;
														%>

														<tr class="<%=rowClass%>">
															<td vAlign="center" align="left">
																<a href="javascript:updateIssues('<c:out value="${issues.id}"/>')">
																<c:out value="${issues.name}"/></a>
																 &nbsp;
																<field:display feature="Issues" name="Issue Date">
																	<c:out value="${issues.issueDate}"/>
																</field:display>
																<a href="javascript:removeIssue('${issues.id}')">
																	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0"/>
																</a>&nbsp;&nbsp;&nbsp;&nbsp;
																<field:display feature="Issues" name="Measures Taken">
																	<field:display name="Add Measures Link" feature="Issues">
																		<a href="javascript:addMeasures('<c:out value="${issues.id}"/>')">
																			<digi:trn key="aim:addMeasures">Add Measures</digi:trn>
																		</a>
																	</field:display>													
																</field:display>
															</td>
														</tr>
														<field:display feature="Issues" name="Measures Taken">
														<tr class="<%=rowClass%>">
															<td vAlign="center" align="left">
																<table width="100%" cellPadding=4 cellSpacing=1 vAlign="top" border=0
																bgcolor="#dddddd">
																	<logic:notEmpty name="issues" property="measures">
																	<logic:iterate name="issues" property="measures" id="measure"
																	 type="org.digijava.module.aim.helper.Measures">
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																			&nbsp;&nbsp;
																		</td>
																		<td vAlign="center" align="left">
																			<a href="javascript:updateMeasures('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')">
																			<c:out value="${measure.name}"/> </a>
																			<a href="javascript:removeMeasure('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')">
																				<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0"/>
																			</a>&nbsp;&nbsp;&nbsp;&nbsp;
																			<field:display name="Add Actors Link" feature="Issues">
																				<a href="javascript:addActors('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')"><digi:trn key="aim:addActors">Add Actors</digi:trn></a>
																			</field:display>
																		</td>
																	</tr>
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																		</td>
																		<td vAlign="center" align="left">
																		  <field:display name="Actors" feature="Issues">
																			<table width="100%" cellPadding=4 cellSpacing=1 vAlign="top" border=0
																			bgcolor="#dddddd">
																				<logic:notEmpty name="measure" property="actors">
																				<logic:iterate name="measure" property="actors" id="actor"
																				 type="org.digijava.module.aim.dbentity.AmpActor">
																				<tr class="<%=rowClass%>">
																					<td vAlign="center" align="left" width="3">
																						&nbsp;&nbsp;
																					</td>
																					<td vAlign="center" align="left">
																						<a href="javascript:updateActor('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>','<c:out value="${actor.ampActorId}"/>')">
																							<c:out value="${actor.name}"/>
																						</a>
																						<a href="javascript:removeActor('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>','<c:out value="${actor.ampActorId}"/>')">
																							<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0"/>
																						</a>&nbsp;
																					</td>
																				</tr>
																				</logic:iterate>
																				</logic:notEmpty>
																			</table>
																		  </field:display>
																		</td>
																	</tr>
																	</logic:iterate>
																	</logic:notEmpty>
																</table>
															</td>
														</tr>
														</field:display>
														</logic:iterate>
													</table>
												</td></tr>
												<tr>
													<td align="center">
															
															<field:display name="Add Issues Button" feature="Issues">

																<html:button  styleClass="dr-menu" property="submitButton" onclick="addIssues()">
																		<digi:trn key="btn:addIssues">Add Issues</digi:trn>
																</html:button>

															</field:display>

													</td>
												</tr>
											</table>
										</logic:notEmpty>
										<logic:empty name="aimEditActivityForm" property="issues.issues">
											<field:display name="Add Issues Button" feature="Issues">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<tr><td>
													<html:button  styleClass="dr-menu" property="submitButton" onclick="addIssues()">
															<digi:trn key="btn:addIssues">Add Issues</digi:trn>
													</html:button>

												</td></tr>
											</table>
											</field:display>
										</logic:empty>
									</td></tr>
									</table>
								</field:display>
							</feature:display>
						</module:display>