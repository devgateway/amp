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
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>
<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicTooltip.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<jsp:include page="scripts/newCalendar.jsp"  />

<c:set var="trnAddObservations">
	<digi:trn>Add Observations</digi:trn>
</c:set>
<c:set var="trnEditActor">
	<digi:trn>Edit Actor</digi:trn>
</c:set>
<c:set var="trnDeleteActor">
	<digi:trn>Delete Actor</digi:trn>
</c:set>

<digi:instance property="aimEditActivityForm" />
	<module:display name="Regional Observations" parentModule="PROJECT MANAGEMENT">
		<feature:display name="Regional Observations" module="Regional Observations">
			<field:display name="Regional Observations" feature="Regional Observations">
				<table width="95%" bgcolor="#f4f4f2">
					<tr>
						<td>
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
							<a title="<digi:trn key="aim:observationsForTheActivity">The observations for the activity</digi:trn>">
							<b><digi:trn>Observations</digi:trn></b></a>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<logic:notEmpty name="aimEditActivityForm" property="observations.issues">
								<table width="100%" cellspacing="1" cellPadding=4 class="box-border-nopadding">
									<tr>
										<td align="center">
											<table width="98%" cellspacing="1" cellPadding=2 vAlign="top" align="center" bgcolor="#dddddd">
											<%	int i = 1;
												String rowClass = "";
											%>
											<logic:iterate name="aimEditActivityForm" property="observations.issues" id="observations" type="org.digijava.module.aim.helper.Issues">
												<% if ((i % 2) != 0) {
													rowClass = "rowAlternate";
												} else {
													rowClass = "rowNormal";
												}
												i++;
												%>
												<tr class="<%=rowClass%>">
													<td vAlign="center" align="left" bgcolor="#dbe5f1">
																  <div style="display:block;padding:2px;font-size:10pt;">
																	<c:out value="${observations.name}"/>
																	<field:display name="Regional Observations Date" feature="Regional Observations">
	  																	<br/>
																		<strong><c:out value="${observations.issueDate}"/></strong>
																	</field:display>
																	<br/>
																	</div>
																	<button onclick="updateObservations('<c:out value="${observations.id}"/>');return false;" title='<digi:trn>Edit this observation</digi:trn>' class="buton"><digi:trn>Edit observation</digi:trn></button>
																	<button onclick="removeObservation('${observations.id}');return false;" title='<digi:trn>Delete this observation</digi:trn>' class="buton"><digi:trn>Delete observation</digi:trn></button>
	<!--
																	<a href="javascript:updateObservations('<c:out value="${observations.id}"/>')"  title="Edit this issue"><digi:img src="../ampTemplate/images/application_edit.png" border="0"/></a>
																	<a href="javascript:removeObservation('${observations.id}')" title="Delete this issue"><digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" /></a>
	-->
																	<field:display feature="Regional Observations" name="Measures Taken">
																		<field:display name="Regional Observations Add Measures Link" feature="Regional Observations">
																			<button onclick="javascript:addMeasures('<c:out value="${observations.id}"/>'); return false;" class="buton" title='<digi:trn>Add Measures</digi:trn>'>
																				<digi:trn key="aim:addMeasures">Add Measures</digi:trn>
																			</button>
																		</field:display>													
																	</field:display>
																</td>
															</tr>
															<field:display feature="Regional Observations" name="Measures Taken">
															<tr class="<%=rowClass%>">
																<td vAlign="center" align="left">
																	<table width="100%" cellPadding=2 cellspacing="1" vAlign="top" border="0"
																	bgcolor="#dddddd">
																		<logic:notEmpty name="observations" property="measures">
																		<logic:iterate name="observations" property="measures" id="measure"
																		 type="org.digijava.module.aim.helper.Measures">
																		<tr class="<%=rowClass%>">
																			<td vAlign="top" align="left" width="3">
																				<digi:img src="../ampTemplate/images/link_out_bot.gif" border="0" />
																			</td>
																			<td vAlign="center" align="left" bgcolor="#dedede">
																				<div style="display:block;font-size:10pt;">
																				<c:out value="${measure.name}"/>
																				<br/>
																				<br/>
	<!--
	       																<a href="javascript:updateMeasures('<c:out value="${observations.id}"/>','<c:out value="${measure.id}"/>')" title="Edit this measure"><digi:img src="../ampTemplate/images/application_edit.png" border="0"/></a>
	      																<a href="javascript:removeMeasure('<c:out value="${observations.id}"/>','<c:out value="${measure.id}"/>')" title="Delete this measure"><digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" /></a>
	-->
	      																</div>
	      																<button class="buton" onclick="updateMeasures('<c:out value="${observations.id}"/>','<c:out value="${measure.id}"/>');return false;" title='<digi:trn>Edit this measure</digi:trn>'><digi:trn>Edit measure</digi:trn></button>
	      																<button class="buton" onclick="removeMeasure('<c:out value="${observations.id}"/>','<c:out value="${measure.id}"/>');return false;" title='<digi:trn>Delete this measure</digi:trn>'><digi:trn>Delete measure</digi:trn></button>
																				<field:display name="Regional Observations Add Actors Link" feature="Regional Observations">
																					<button class="buton" onclick="addActors('<c:out value="${observations.id}"/>','<c:out value="${measure.id}"/>');return false;" title='<digi:trn>Add Actors</digi:trn>'><digi:trn key="aim:addActors">Add Actors</digi:trn></button>
																				</field:display>
																			</td>
																		</tr>
																		<tr class="<%=rowClass%>">
																			<td vAlign="center" align="left" width="3">
																			</td>
																			<td vAlign="center" align="left" >
																			  <field:display name="Regional Observations Actors" feature="Regional Observations">
																				<table width="100%" cellPadding=4 cellspacing="1" vAlign="top" border="0"
																				bgcolor="#dddddd">
																					<logic:notEmpty name="measure" property="actors">
																						<logic:iterate name="measure" property="actors" id="actor" type="org.digijava.module.aim.dbentity.AmpActor">
																							<tr class="<%=rowClass%>">
																								<td vAlign="center" align="left" width="3">
			       																					<digi:img src="../ampTemplate/images/link_out_bot.gif" border="0" />
																								</td>
																								<td vAlign="center" align="left" style="border:1px solid #cecece;" >
			      																					<div style="display:block;">
																										<c:out value="${actor.name}"/>
																									</div>
																									<br/>
																									<button class="buton" onclick="updateActor('<c:out value="${observations.id}"/>','<c:out value="${measure.id}"/>','<c:out value="${actor.ampActorId}"/>');return false;" title="${trnEditActor}"><digi:trn>Edit actor</digi:trn></button>
																									<button class="buton" onclick="removeActor('<c:out value="${observations.id}"/>','<c:out value="${measure.id}"/>','<c:out value="${actor.ampActorId}"/>');return false;" title="${trnDeleteActor}"><digi:trn>Delete actor</digi:trn></button>
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
															
															<field:display name="Regional Observations Add Button" feature="Regional Observations">
																<html:button  styleClass="dr-menu" property="submitButton" onclick="addObservations()" title="${trnAddObservations}">
																		<digi:trn key="btn:addObservations">Add Observations</digi:trn>
																</html:button>
															</field:display>

													</td>
												</tr>
											</table>
										</logic:notEmpty>
										<logic:empty name="aimEditActivityForm" property="observations.issues">
											<field:display name="Regional Observations Add Button" feature="Regional Observations">
											<table width="100%" cellspacing="1" cellPadding=5 class="box-border-nopadding">
												<tr><td>
													<html:button  styleClass="dr-menu" property="submitButton" onclick="addObservations()" title="${trnAddObservations}">
															<digi:trn key="btn:addObservations">Add Observations</digi:trn>
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