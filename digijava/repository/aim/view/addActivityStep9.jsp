<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>

<script language="JavaScript">
<!--
	function resetAll()
	{
		<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
		document.aimEditActivityForm.action = "<%= resetAll %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
		return true;
	}

	function setValues(val) 
	{
		if(document.aimEditActivityForm.workingTeamLeadFlag.value == 'no')
		{
			var emptybox = chkforEmptyBox();
			if(emptybox == true)
			{
				document.aimEditActivityForm.indicatorId.value = val;
				document.aimEditActivityForm.submit();
			}
		}
		else
		{
			var emptyboxes = chkforEmptyBoxes();
			if(emptyboxes == true)
			{
				document.aimEditActivityForm.indicatorId.value = val;
				document.aimEditActivityForm.submit();
			}
		}
	}
	function chkforEmptyBox()
	{
		if(trim(document.aimEditActivityForm.currentVal.value) == 0)
		{
			alert("Please give a Current Value");
			document.aimEditActivityForm.currentVal.focus();
			return false;
		}
		return true;
	}
	function chkforEmptyBoxes()
	{
		if(trim(document.aimEditActivityForm.baseVal.value) == 0)
		{
			alert("Please give a Base Value");
			document.aimEditActivityForm.baseVal.focus();
			return false;
		}
		if(trim(document.aimEditActivityForm.targetVal.value) == 0)
		{
			alert("Please give a Target Value");
			document.aimEditActivityForm.targetVal.focus();
			return false;
		}
		return true;
	}
-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/saveIndicatorValues.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="indicatorId" />
<html:hidden property="indicatorValId" />
<html:hidden property="activityId" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home ">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">								
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot()" title="Click here to view MyDesktop ">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>				
								<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" 
								title="Click here to goto Add Activity Step 1">
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivityStep1">
										Edit Activity - Step 1
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addActivityStep1">
										Add Activity - Step 1
									</digi:trn>
								</c:if>																
								</digi:link>&nbsp;&gt;&nbsp;						
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 2" >						
									<digi:trn key="aim:addActivityStep2">
									Step 2
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 3">						
									<digi:trn key="aim:addActivityStep3">
									Step 3
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;							
									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 4">						
									<digi:trn key="aim:addActivityStep4">
									Step 4
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<digi:link href="/addActivity.do?step=5&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 5">						
									<digi:trn key="aim:addActivityStep5">
									Step 5
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=6&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 6">						
									<digi:trn key="aim:addActivityStep6">
									Step 6
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=7&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 7">						
									<digi:trn key="aim:addActivityStep7">
									Step 7
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;									
									<digi:link href="/addActivity.do?step=8&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 8">						
									<digi:trn key="aim:addActivityStep8">
									Step 8
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;									
									<digi:trn key="aim:addActivityStep9" >
									Step 9
									</digi:trn>									
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addNewActivity">
										Add New Activity
									</digi:trn>
								</c:if>			
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">
										Edit Activity
									</digi:trn>
								</c:if>				
							</td>
						</tr>	
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="75%" vAlign="top">	
						<table cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:step9of9MonitoringnEvaluation">
													Step 9 of 9: Monitoring and Evaluation
												</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>							
							</tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:MonitoringnEvaluation">Monitoring and Evaluation - Indicators</digi:trn>">
										<b><digi:trn key="aim:MonitorEvaluate">Monitoring and Evaluation</digi:trn></b>
										</a>
									</td></tr>
									<tr><td>
										<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
											<tr>
												<td width="32%" align="center" colspan="2"><b>
													Indicators</b>
												</td>
											</tr>
											<logic:empty name="aimEditActivityForm" property="indicatorsME">
											<tr>
												<td width="32%" bgcolor=#f4f4f2 align="center" colspan="2"><font color="red"><b>
													No Activity specific Indicators & No Global Indicators present</b></font>
												</td>
											</tr>
											</logic:empty>
											<logic:notEmpty name="aimEditActivityForm" property="indicatorsME">
											<logic:iterate name="aimEditActivityForm" property="indicatorsME" id="indicator" 
											type="org.digijava.module.aim.helper.ActivityIndicator">
											<tr>
												<td bgcolor=#f4f4f2 align="left" colspan="2">&nbsp;&nbsp;
													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
													<c:if test="${aimEditActivityForm.expIndicatorId==indicator.indicatorId}">
														<digi:link href="/nondetailedIndicator.do">
															<img src= "../ampTemplate/images/arrow_down.gif" border=0>
														</digi:link>
													</c:if>
													<c:if test="${aimEditActivityForm.expIndicatorId!=indicator.indicatorId}">
														<c:set target="${urlParams}" property="indValId">
															<bean:write name="indicator" property="indicatorValId" />
														</c:set>
														<c:set target="${urlParams}" property="activityId">
															<bean:write name="aimEditActivityForm" property="activityId"/>
														</c:set>
														<digi:link href="/detailedIndicator.do" name="urlParams">
															<img src= "../ampTemplate/images/arrow_right.gif" border=0>
														</digi:link>
													</c:if>&nbsp;&nbsp;&nbsp;<b>
													<bean:write name="indicator" property="indicatorName" /></b> - 
													<bean:write name="indicator" property="indicatorCode" />
												</td>
											</tr>
											<c:if test="${aimEditActivityForm.expIndicatorId==indicator.indicatorId}">
											<tr>
												<td colspan="2">
													<table cellspacing="0" cellpadding="3" valign="top" align="center" width="90%">
														<c:if test="${aimEditActivityForm.workingTeamLeadFlag=='no'}">
															<tr>
																<td><b>Base Value</b></td>
																<td>
																	<bean:write name="aimEditActivityForm" property="baseVal" />
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	Date  :
																</td>
																<td align="left">&nbsp;&nbsp;
																	<bean:write name="aimEditActivityForm" property="baseValDate" />
																</td>
															</tr>
															<tr>
																<td><b>Target Value</b></td>
																<td>
																	<bean:write name="aimEditActivityForm" property="targetVal" />
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	Date  :
																</td>
																<td align="left">&nbsp;&nbsp;
																	<bean:write name="aimEditActivityForm" property="targetValDate" />
																</td>
															</tr>
															<tr>
																<td><b>Revised target Value</b></td>
																<td>
																	<bean:write name="aimEditActivityForm" property="revTargetVal" />
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	Date  :
																</td>
																<td align="left">&nbsp;&nbsp;
																	<bean:write name="aimEditActivityForm" property="revTargetValDate" />
																</td>
															</tr>
														</c:if>
														<c:if test="${aimEditActivityForm.workingTeamLeadFlag=='yes'}">
															<tr>
																<td><b>Base Value</b></td>
																<td>
																	<html:text name="aimEditActivityForm" property="baseVal" styleClass="inp-text"/>
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	Date  :
																</td>
																<td align="left">
																	<html:text name="aimEditActivityForm" property="baseValDate" size="10"
																	styleId="baseValDate" styleClass="inp-text" readonly="true"/>&nbsp;&nbsp;
																	<a href="javascript:calendar('baseValDate')">
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																	</a>
																</td>
															</tr>
															<tr>
																<td><b>Target Value</b></td>
																<td>
																	<html:text name="aimEditActivityForm" property="targetVal" styleClass="inp-text"/>
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	Date  :
																</td>
																<td align="left">
																	<html:text name="aimEditActivityForm" property="targetValDate" size="10"
																	styleId="targetValDate" styleClass="inp-text" readonly="true"/>&nbsp;&nbsp;
																	<a href="javascript:calendar('targetValDate')">
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																	</a>
																</td>
															</tr>
															<tr>
																<td><b>Revised target Value</b></td>
																<td>
																	<html:text name="aimEditActivityForm" property="revTargetVal" styleClass="inp-text"/>
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	Date  :
																</td>
																<td align="left">
																	<html:text name="aimEditActivityForm" property="revTargetValDate" size="10"
																	styleId="revTargetValDate" styleClass="inp-text" readonly="true"/>&nbsp;&nbsp;
																	<a href="javascript:calendar('revTargetValDate')">
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																	</a>
																</td>
															</tr>
														</c:if>
														<logic:notEmpty name="aimEditActivityForm" property="indicatorPriorValues" >
															<tr bgColor=#dddddb><td bgColor=#dddddb align="left" colspan="5"><b>
																Prior Values :</b>
															</td></tr>
															<logic:iterate name="aimEditActivityForm" property="indicatorPriorValues"
															id="priorValues" type="org.digijava.module.aim.helper.PriorCurrentValues">
																<tr bgColor=#f4f4f2>
																	<td align="center">
																		<img src= "../ampTemplate/images/arrow_dark.gif" border=0>
																	</td>
																	<td>
																		<bean:write name="priorValues" property="currValue" />
																	</td>
																	<td>&nbsp;&nbsp;&nbsp;</td>
																	<td align="right">
																		Date  :
																	</td>
																	<td align="left">&nbsp;&nbsp;
																		<bean:write name="priorValues" property="currValDate" format="yyyy-mm-dd"/>
																	</td>
																</tr>
															</logic:iterate>
														</logic:notEmpty>
														<tr>
															<td><b>Current Value</b></td>
															<td>
																<html:text name="aimEditActivityForm" property="currentVal" styleClass="inp-text"/>
															</td>
															<td>&nbsp;&nbsp;&nbsp;</td>
															<td align="right">
																Date  :
															</td>
															<td>
																<html:text name="aimEditActivityForm" property="currentValDate" size="10"
																styleId="currentValDate" styleClass="inp-text" readonly="true"/>&nbsp;&nbsp;
																<a href="javascript:calendar('currentValDate')">
																	<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</td>
														</tr>			
														<tr>
															<td><b>Comments</b></td>
															<td>
																<html:textarea name="aimEditActivityForm" property="comments" styleClass="inp-text"/>
															</td>
															<td>&nbsp;&nbsp;&nbsp;</td>
															<td><b>Risk</b></td>
															<td>
																<html:select property="indicatorRisk" styleClass="inp-text">
																	<html:optionsCollection name="aimEditActivityForm" property="riskCollection" 
																	value="ampIndRiskRatingsId" label="ratingName" />
																</html:select>
															</td>
														</tr>
														<tr><td>&nbsp;</td></tr>										
														<tr>
															<td>&nbsp;</td>
															<td colspan="3" align="center">
																<input type="button" class="dr-menu" value="Set Values" 
																onclick="setValues('<c:out value="${indicator.indicatorId}" />')">
															</td>
															<td>&nbsp;</td>
														</tr>
													</table>
												</td>
											</tr>
											</c:if>
											</logic:iterate>
											</logic:notEmpty>
										</table>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<input type="button" value=" << Back " class="dr-menu" onclick="gotoStep(8)">
												</td>
												<td>
													<input type="button" value="Preview" class="dr-menu" onclick="previewClicked()">
												</td>
												<td>
													<input type="reset" value="Reset" class="dr-menu" onclick="return resetAll()">
												</td>
											</tr>
										</table>
									</td></tr>									
								</table>
							</td></tr>
							</table>
							</td></tr>							
						</table>
						</td>
						<td width="25%" vAlign="top" align="right">
						<!-- edit activity form menu -->
							<jsp:include page="editActivityMenu.jsp" flush="true" />
						<!-- end of activity form menu -->							
						</td></tr>
					</table>
				</td></tr>
				<tr><td>
					&nbsp;
				</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>
