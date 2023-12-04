<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/category" prefix="category" %>


<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<digi:instance property="aimEditActivityForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--

-->

</script>


<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="workingTeamLeadFlag" />

<div style="width:100%;height:370px;overflow-y:scroll;">

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td valign="top" width="100%" vAlign="top" align="left">
<bean:define id="defaultCurrency" name="currentMember" property="appSettings.currencyId" type="java.lang.Long" scope="session" toScope="page"/> 
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="99%" vAlign="top" align="left" border="0">
	<tr>
		<td valign="top" class=r-dotted-lg width="10" align="left" vAlign="top">&nbsp;</td>
		<td valign="top" class=r-dotted-lg align=left valign="top">
			<table width="100%" cellSpacing="3" cellPadding="1" vAlign="top" align="left" border="0">
				<tr><td valign="top">
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td valign="top" width="100%" vAlign="top">
						<table width="100%" cellspacing="0" cellpadding="0" vAlign="top" align="left">
							<tr>
								<td valign="top" width="100%">
									<table cellpadding="0" cellspacing="0" width="100%" border="0">
										<tr>
											<td valign="top" width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td valign="top" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:logframePlanningMatrix">
													Logframe Planning Matrix
												</digi:trn>
												
											</td>
											<td valign="top" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<% java.util.Date now = new java.util.Date();
											session.setAttribute("logframepr","false");
												java.text.DateFormat df =  java.text.DateFormat.getDateInstance();%>
											      <%= df.format(now)%></td>
											<td valign="top" width="13" height="20" background="module/aim/images/right-side.gif">
												
											</td>
										</tr>
									 </table>
								</td>
							</tr>
							
							<tr>
								<td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
									<tr>
										<td valign="top"  width="50%" vAlign="center" align ="left" height="20">
											<digi:trn key="aim:plogframe:logframePlanningMatrix">Logframe Planning Matrix for</digi:trn>
											<logic:notEmpty name="aimEditActivityForm" property="identification.title">
												<bean:write name="aimEditActivityForm" property="identification.title"/>
											</logic:notEmpty>
											,
											<c:forEach items="${aimEditActivityForm.location.selectedLocs}" var="loc">
												<c:out value="${loc.country}" />
											</c:forEach>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="left" height="20">
											<digi:trn key="aim:plogframe:programNameAndNumber">Program name and number</digi:trn>:
											<logic:notEmpty name="aimEditActivityForm" property="identification.acChapter">
												<logic:notEqual name="aimEditActivityForm" property="identification.acChapter" value="0">
													<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.acChapter}"/>,
												</logic:notEqual>
											</logic:notEmpty>
											<logic:notEmpty name="aimEditActivityForm" property="identification.ampId">
												<bean:write name="aimEditActivityForm" property="identification.ampId"/>
											</logic:notEmpty>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="left" height="20">
											
										</td>
										
									</tr>
									<tr>
										<td valign="top"  width="50%" vAlign="center" align ="left" height="20" style="border-top: 1px solid #CCC; border-top-style: dashed;">
											&nbsp;
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="left" height="20" style="border-top: 1px solid #CCC; border-top-style: dashed;">
											<digi:trn key="aim:plogframe:contractingPeriodExpires">Contracting period expires</digi:trn>:
											<logic:notEmpty name="aimEditActivityForm" property="planning.contractingDate">
												<bean:write name="aimEditActivityForm" property="planning.contractingDate"/>
											</logic:notEmpty>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="left" height="20" style="border-top: 1px solid #CCC; border-top-style: dashed;">
											<digi:trn key="aim:plogframe:disbursementPeriodExpires">Disbursement period expires</digi:trn>:
											<logic:notEmpty name="aimEditActivityForm" property="planning.disbursementsDate">
												<bean:write name="aimEditActivityForm" property="planning.disbursementsDate"/>
											</logic:notEmpty>
										</td>
									</tr>	
									<tr>
										<td valign="top"  width="50%" vAlign="center" align ="left" height="20">
											&nbsp;
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="left" height="20" style="border-top: 1px solid #CCC; border-top-style: dashed;">
											<digi:trn key="aim:plogframe:ipaBudget">IPA budget</digi:trn>:
											<bean:write name="aimEditActivityForm" property="contracts.ipaBudget" format="###,###,###.##" />
											
										</td>
									</tr>	
								</table>
								</td>
							</tr>
							
							
							<tr>
								<td valign="top" width="70%" bgcolor="#f4f4f2">
									<table width="75%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" bgcolor="#006699" >
									<tr>
										<td valign="top"  width="33%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:overallObjective">Overall Objective</digi:trn>
										</td>
										<td valign="top"  width="33%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699" >
											<digi:trn key="aim:objectivelyIndicatorsPreview">Objectively Verifiable Indicators</digi:trn>
										</td>
										<td valign="top"  width="33%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:verification">Verification</digi:trn>
										</td>
										
										
									</tr>
									<tr>
										<td valign="top"  width="33%"  bgcolor="#ffffff" style="border:1px solid #CCC;">
										<digi:edit key="${aimEditActivityForm.identification.objectives}"/>
										</td>
										<td valign="top"  width="33%" bgcolor="#ffffff" style="border:1px solid #CCC;">
											<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
	                                        	<logic:equal name="comments" property="key" value="Objective Objectively Verifiable Indicators">
													<logic:iterate name="comments" id="comment" property="value" 
														type="org.digijava.module.aim.dbentity.AmpComments">
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
											</logic:iterate>
										</td>
											
										<td valign="top"  width="33%" bgcolor="#ffffff" style="border:1px solid #CCC;">
											<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
	                                        	<logic:equal name="comments" property="key" value="Objective Verification">
													<logic:iterate name="comments" id="comment" property="value" 
														type="org.digijava.module.aim.dbentity.AmpComments">
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
											</logic:iterate>
										</td>
										
									</tr>
								</table>
								</td>
							</tr>
							
							<tr>
								<td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699" >
									<tr>
										<td valign="top" width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:projectPurpose">Project Purpose</digi:trn>
										</td>
										<td valign="top" width="25%"  vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699" >
											<digi:trn key="aim:objectivelyIndicatorsPreview">Objectively Verifiable Indicators</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:verification">Verification</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:assumptions">Assumptions</digi:trn>
										</td>
										
									</tr>
									<tr>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
												<digi:edit key="${aimEditActivityForm.identification.purpose}"/>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">&nbsp;
											<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
	                                        	<logic:equal name="comments" property="key" value="Purpose Objectively Verifiable Indicators">
													<logic:iterate name="comments" id="comment" property="value" 
														type="org.digijava.module.aim.dbentity.AmpComments">
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
											</logic:iterate>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
											<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
	                                        	<logic:equal name="comments" property="key" value="Purpose Verification">
													<logic:iterate name="comments" id="comment" property="value" 
														type="org.digijava.module.aim.dbentity.AmpComments">
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
											</logic:iterate>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
										 	<logic:equal name="comments" property="key" value="Purpose Assumption">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments">
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										</td>
									</tr>
								    </table>
							   </td>
							</tr>
							
							<tr>
								<td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699" >
									<tr>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:results">Results</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699" >
											<digi:trn key="aim:objectivelyIndicatorsPreview">Objectively Verifiable Indicators</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:verification">Verification</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:assumptions">Assumptions</digi:trn>
										</td>
										
									<tr>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
												<digi:edit key="${aimEditActivityForm.identification.results}"/>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
											<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
	                                        	<logic:equal name="comments" property="key" value="Results Objectively Verifiable Indicators">
													<logic:iterate name="comments" id="comment" property="value" 
														type="org.digijava.module.aim.dbentity.AmpComments">
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
											</logic:iterate>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
											 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
												<logic:equal name="comments" property="key" value="Results Verification">
													<logic:iterate name="comments" id="comment" property="value" 
														type="org.digijava.module.aim.dbentity.AmpComments">
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
											</logic:iterate>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
										 	<logic:equal name="comments" property="key" value="Results Assumption">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments">
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										
										</td>
									</tr>
								    </table>
							   </td>
							</tr>

							<tr>
								<td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="0" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
									<tr>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:activitiesMonte">Activities</digi:trn>
										</td>
										<td valign="top" vAlign="center"  width="25%" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:means">Means</digi:trn>
										</td>
										<td valign="top" vAlign="center"  width="25%" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:assumptions">Assumptions</digi:trn>
										</td>
										<td></td>
										
									</tr>
								    </table>
							   </td>
							</tr>
							
						</table>
						</td></tr>
					</table>
				</td></tr>
			</table>
		</td></tr>
	</table>
</td></tr>
<tr>
	<td valign="top" bgcolor="#ffffff" align="center" width="100%" >
		<table width="98%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#ffffff" >
			<tr>
				<td width="100%" align="center">
					<c:set var="translation">
							<digi:trn key='aim:boutonclose'>Close</digi:trn>
						</c:set>

					<html:button styleClass="dr-menu" value="${translation}" onclick="return hidePLogframe()" property="closeButton"/>
						&nbsp;&nbsp;&nbsp;

                          <c:set var="translation">
							<digi:trn key='aim:boutonPrint'>Print</digi:trn>
						</c:set>


					<html:button styleClass="dr-menu" value="${translation}" onclick="return window.print()" property="printButton"/>&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>

</div>