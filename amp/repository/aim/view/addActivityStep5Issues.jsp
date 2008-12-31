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
														<tr bgcolor="#ffd5d5">
															<td vAlign="center" align="left" width="3">
																<input type="checkbox" name="checkAllIssues" onclick="checkallIssues()">
															</td>
															<td vAlign="center" align="left">
																<b><digi:trn key="aim:issues">Issues</digi:trn></b>
															</td>
														</tr>
														<% int i = 0;
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
															<td vAlign="center" align="left" width="3">
																<html:multibox property="issues.selIssues" styleId="selIssues">
																	<c:out value="${issues.id}"/>
																</html:multibox>
															</td>
															<td vAlign="center" align="left">
																<a href="javascript:updateIssues('<c:out value="${issues.id}"/>')">
																<c:out value="${issues.name}"/></a>
																 &nbsp;
																<field:display feature="Issues" name="Issue Date">
																	<c:out value="${issues.issueDate}"/>
																</field:display>
															</td>
														</tr>
														<field:display feature="Issues" name="Measures Taken">
														<tr class="<%=rowClass%>">
															<td vAlign="center" align="left" width="3">
															</td>
															<td vAlign="center" align="left">
																<table width="100%" cellPadding=4 cellSpacing=1 vAlign="top" border=0
																bgcolor="#dddddd">
																	<tr class="<%=rowClass%>">
																		<td align="left" colspan="2">
																			<b><digi:trn key="aim:measures">Measures</digi:trn></b>&nbsp;&nbsp;
																			<field:display name="Add Measures Link" feature="Issues">
																			<a href="javascript:addMeasures('<c:out value="${issues.id}"/>')">
																			<digi:trn key="aim:addMeasures">Add Measures</digi:trn></a>
																			</field:display>
																		</td>
																	</tr>
																	<logic:notEmpty name="issues" property="measures">
																	<logic:iterate name="issues" property="measures" id="measure"
																	 type="org.digijava.module.aim.helper.Measures">
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																			<html:multibox property="issues.selMeasures">
																				<c:out value="${measure.id}"/>
																			</html:multibox>
																		</td>
																		<td vAlign="center" align="left">
																			<a href="javascript:updateMeasures('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')">
																			<c:out value="${measure.name}"/>
																		</td>
																	</tr>
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																		</td>
																		<td vAlign="center" align="left">
																		  <field:display name="Actors" feature="Issues">
																			<table width="100%" cellPadding=4 cellSpacing=1 vAlign="top" border=0
																			bgcolor="#dddddd">
																				<tr class="<%=rowClass%>">
																					<td align="left" colspan="2">
																						<b><digi:trn key="aim:actors">Actors</digi:trn></b>&nbsp;&nbsp;
																						<field:display name="Add Actors Link" feature="Issues">
																						<a href="javascript:addActors('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')"><digi:trn key="aim:addActors">Add Actors</digi:trn></a>
																						</field:display>
																					</td>
																				</tr>
																				<logic:notEmpty name="measure" property="actors">
																				<logic:iterate name="measure" property="actors" id="actor"
																				 type="org.digijava.module.aim.dbentity.AmpActor">
																				<tr class="<%=rowClass%>">
																					<td vAlign="center" align="left" width="3">
																						<html:multibox property="issues.selActors">
																							<c:out value="${actor.ampActorId}"/>
																						</html:multibox>
																					</td>
																					<td vAlign="center" align="left">
																						<a href="javascript:updateActor('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>','<c:out value="${actor.ampActorId}"/>')">
																							<c:out value="${actor.name}"/>
																					</td>
																				</tr>
																				</logic:iterate>
																				<field:display name="Remove Actors Button" feature="Issues">
																				<tr class="<%=rowClass%>">
																					<td vAlign="center" align="left" width="3">
																					</td>
																					<td vAlign="center" align="left">
																						<input type="button" class="dr-menu" onclick="removeActors('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')" value='<digi:trn key="btn:removeActors">Remove Actors</digi:trn>' />
																					</td>
																				</tr>
																				</field:display>
																				</logic:notEmpty>
																			</table>
																		  </field:display>
																		</td>
																	</tr>
																	</logic:iterate>
																	<field:display name="Remove Measures Button" feature="Issues">
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																		</td>
																		<td vAlign="center" align="left">
																			<input type="button" class="dr-menu" onclick="removeMeasure('<c:out value="${issues.id}"/>')" value='<digi:trn key="btn:removeMeasures">Remove Measures</digi:trn>' />

																		</td>
																	</tr>
																	</field:display>
																	</logic:notEmpty>
																</table>
															</td>
														</tr>
														</field:display>
														</logic:iterate>
													</table>
												</td></tr>
												<tr><td align="center">
													<table cellSpacing=2 cellPadding=2>
														<tr>
															
															<field:display name="Add Issues Button" feature="Issues">
															<td>
																<html:button  styleClass="dr-menu" property="submitButton" onclick="addIssues()">
																		<digi:trn key="btn:addIssues">Add Issues</digi:trn>
																</html:button>
															</td>
															</field:display>
															<field:display name="Remove Issues Button" feature="Issues">
															<td>
																<html:button  styleClass="dr-menu" property="submitButton" onclick="removeIssues()">
																		<digi:trn key="btn:removeIssues">Remove Issues</digi:trn>
																</html:button>
															</td>
															</field:display>
														</tr>
													</table>
												</td></tr>
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