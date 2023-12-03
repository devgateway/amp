<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>



<script type="text/javascript">

function confirmDelete() {

var flag = confirm('Delete this indicator value?');

return flag;

}



function validate() {

if (containsValidNumericValue(document.aimUpdateIndicatorValuesForm.baseVal) == false) {

	alert("Invalid Base value or Base value not entered");

	document.aimUpdateIndicatorValuesForm.baseVal.focus();

	return false;

}

if (isEmpty(document.aimUpdateIndicatorValuesForm.baseValDate.value) == true) {

	alert("Base value date not entered");

	document.aimUpdateIndicatorValuesForm.baseValDate.focus();

	return false;

}

if (containsValidNumericValue(document.aimUpdateIndicatorValuesForm.targetVal) == false &&

					 document.aimUpdateIndicatorValuesForm.targetVal.disabled == false) {

	alert("Invalid Target value or Target value not entered");

	document.aimUpdateIndicatorValuesForm.targetVal.focus();

	return false;

}

if (isEmpty(document.aimUpdateIndicatorValuesForm.targetValDate.value) == true) {

	alert("Target value date not entered");

	document.aimUpdateIndicatorValuesForm.targetValDate.focus();

	return false;

}

if (document.aimUpdateIndicatorValuesForm.revisedTargetVal != null) {

	if (containsValidNumericValue(document.aimUpdateIndicatorValuesForm.revisedTargetVal) == false) {

		alert("Invalid Revised target value or Revised target value not entered");

		document.aimUpdateIndicatorValuesForm.revisedTargetVal.focus();

		return false;

	}

	if (isEmpty(document.aimUpdateIndicatorValuesForm.revisedTargetValDate.value) == true) {

		alert("Revised target value date not entered");

		document.aimUpdateIndicatorValuesForm.revisedTargetValDate.focus();

		return false;

	}

}

return true;

}



function addIndicators()

{

var actId = document.aimUpdateIndicatorValuesForm.activityId.value;

<digi:context name="selCreateInd" property="context/ampModule/moduleinstance/selectCreateIndicators.do" />

document.aimUpdateIndicatorValuesForm.action = "<%=selCreateInd%>?activityId="+actId;

document.aimUpdateIndicatorValuesForm.target = "_self";

document.aimUpdateIndicatorValuesForm.submit();

}

</script>



<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${bcparams}" property="tId" value="-1"/>

<c:set target="${bcparams}" property="dest" value="teamLead"/>



<digi:instance property="aimUpdateIndicatorValuesForm" />



<digi:form action="/saveMEIndicatorValues.do">



<html:hidden property="indicatorId" />

<html:hidden property="indicatorValId" />

<html:hidden property="activityId" />

<input type="hidden" name="event" value="save">


<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">
	<tr>
		<td width="100%" valign="top" align="left">
			<jsp:include page="teamPagesHeader.jsp" flush="true" />
		</td>
	</tr>
	<tr>
		<td width="100%" valign="top" align="left">
			<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td height=33><span class=crumb>
						
						<div class="breadcrump_cont">
							<span class="sec_name">
								<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
							</span>
							
							<span class="breadcrump_sep">|</span>
							<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
								<digi:trn key="aim:portfolio">Portfolio</digi:trn>
							</digi:link>
							<span class="breadcrump_sep"><b>�</b></span>
							<c:set var="translation">
								<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
							</c:set>
							<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
							<span class="breadcrump_sep"><b>�</b></span>
							<span class="bread_sel"><digi:trn key="aim:activityList">Activity List</digi:trn></span>
						</div>
						
						
					</td>
				</tr>
			<tr>
			<tr>
						<td vAlign="top" width="100%">
							<c:set var="selectedTab" value="6" scope="request"/>
							<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
								<jsp:include page="teamSetupMenu.jsp" flush="true" />
									<table class="inside" width="100%">
										<tr>
											<td class="inside">
												<digi:errors />
											</td>
										</tr>
										<tr>
											<td align="left" width="100%" valign="center" class="inside">
												<digi:trn>The fields marked </digi:trn><digi:trn><font color="red"> * </font></digi:trn><digi:trn> are mandatory</digi:trn>
											</td>
										</tr>
										<tr>
											<td class="inside">
                        <div>
                        	&nbsp;
                          <img src= "../ampTemplate/images/bullet_red.gif" border="0">
                          <digi:trn key="aim:globalIndicator">Global Indicator</digi:trn>
                          &nbsp;
                          <img src= "../ampTemplate/images/bullet_grey.gif" border="0">
                          <digi:trn key="aim:activitySpecificIndicator">Activity Specific Indicator</digi:trn>
                        </div>
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:trn key="aim:meIndicators">Indicators</digi:trn>
											</td>
										</tr>
										<logic:empty name="aimUpdateIndicatorValuesForm" property="indicators">
											<tr>
												<td align="center" class="inside">
													<digi:trn key="aim:noIndicatorsPresent">No indicators present</digi:trn>
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="aimUpdateIndicatorValuesForm" property="indicators">
											<tr>
												<td class="inside">
													<table border="1" class="inside" width="100%">
														<logic:iterate name="aimUpdateIndicatorValuesForm" property="indicators" id="indicator" type="org.digijava.ampModule.aim.helper.ActivityIndicator">
															<tr>
																<td class="inside" width="9">
																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId==indicator.indicatorId}">
																		<digi:link href="/collapseIndicator.do"><img src= "../ampTemplate/images/arrow_down.gif" border="0"></digi:link>
																	</c:if>
																	<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId!=indicator.indicatorId}">
																		<c:set target="${urlParams}" property="indValId">
																			<bean:write name="indicator" property="indicatorId" />
																		</c:set>
																		<c:set target="${urlParams}" property="activityId">
																			<bean:write name="aimUpdateIndicatorValuesForm" property="activityId"/>
																		</c:set>
																		<digi:link href="/expandIndicator.do" name="urlParams"><img src= "../ampTemplate/images/arrow_right.gif" border="0"></digi:link>
																	</c:if>
																</td>
																<td width="9" class="inside">
																	<c:if test="${indicator.defaultInd == true}">
																		<img src= "../ampTemplate/images/bullet_red.gif" border="0">
																	</c:if>
																	<c:if test="${indicator.defaultInd == false}">
																		<img src= "../ampTemplate/images/bullet_grey.gif" border="0">
																	</c:if>
																</td>
																<td class="inside">
																	<bean:define id="indName">
																		<bean:write name="indicator" property="indicatorName"/>
																	</bean:define>
																	<digi:trn key='<%="aim:" + indName%>'><%=indName%></digi:trn>
																</td>
																<td class="inside">
																	<bean:write name="indicator" property="indicatorCode" />
																</td>
																<td class="inside">
																	<c:if test="${indicator.indicatorValId > 0}">
																		<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams1}" property="event" value="delete" />
																		<c:set target="${urlParams1}" property="indicatorValId">
																			<bean:write name="indicator" property="indicatorValId" />
																		</c:set>
			
																		<digi:link href="/deleteIndicatorValue.do" name="urlParams1" onclick="return confirmDelete()">
																			<img src= "../ampTemplate/images/trash_12.gif" border="0">
																		</digi:link>
																	</c:if>
																</td>
															</tr>
															<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId==indicator.indicatorId}">
																<tr>
																	<td width="12" class="inside">&nbsp;</td>
																	<td colspan="4" class="inside">
																		<table class="inside" valign="top" align="left">
																			<tr>
																				<td class="inside">
																					<digi:trn key="aim:meBaseValue">Base Value</digi:trn>
																					<font color="red">*</font>
																				</td>
																				<td class="inside">
																					<input type="text" name="baseVal" value="<bean:write name="indicator" property="baseVal" />" class="inputx" size="10">
																				</td>
																				<td class="inside">
																					<digi:trn key="aim:meDate">Date</digi:trn>
																					<font color="red">*</font>
																				</td>
																				<td class="inside">
																					<input type="text" name="baseValDate" value="<bean:write name="indicator" property="baseValDate" />" class="inputx" size="10" readonly="true" id="baseValDate">
																				</td>
																				<td align="left" vAlign="center" class="inside">
																					<a id="date1" href='javascript:pickDateById("date1","baseValDate")'>
																						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																					</a>
																				</td>
																			</tr>
																			<tr>
																				<td class="inside">
																					<digi:trn key="aim:meBaseValueComments">Comments</digi:trn>
																				</td>
																				<td colspan="4" class="inside">
																					<textarea name="baseValComments" class="inputx" rows="2" cols="38"><bean:write name="indicator" property="baseValComments" /></textarea>
																				</td>
																			</tr>
																			<tr>
																				<td class="inside">
																					<digi:trn key="aim:meTargetValue">Target Value</digi:trn>
																					<font color="red">*</font>
																				</td>
																				
																				<c:if test="${indicator.targetValDate != null}">
																					<td class="inside">
																						<input type="text" name="targetVal" value="<bean:write name="indicator" property="targetVal" />" class="inputx" size="10" disabled="true">
																					</td>
																					<td class="inside">
																						<digi:trn key="aim:meDate">Date</digi:trn>
																						<font color="red">*</font></td>
																					<td class="inside">
																						<input type="text" name="targetValDate" value="<bean:write name="indicator" property="targetValDate" />" class="inputx" size="10" disabled="true" id="targetValDate">
																					</td>
																					<td align="left" vAlign="center" class="inside">
																						&nbsp;
																					</td>
																				</c:if>
																				
																				<c:if test="${indicator.targetValDate == null}">
																					<td class="inside">
																						<input type="text" name="targetVal" value="<bean:write name="indicator" property="targetVal" />" class="inputx" size="10">
																					</td>
																					<td class="inside">
																						<digi:trn key="aim:meDate">Date</digi:trn>
																						<font color="red">*</font>
																					</td>
																					<td class="inside">
																						<input type="text" name="targetValDate" value="<bean:write name="indicator" property="targetValDate" />" class="inputx" size="10" readonly="true" id="targetValDate">
																					</td>
																					<td align="left" vAlign="center" class="inside">
																						<a id="date2" href='javascript:pickDateById("date2","targetValDate")'>
																							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																						</a>
																					</td>
																				</c:if>
																			</tr>
																			<tr>
																				<td class="inside">
																					<digi:trn key="aim:meTargetValComments">Comments</digi:trn>
																				</td>
																				<td colspan="4" class="inside">
																					<textarea name="targetValComments" class="inputx" rows="2" cols="38"><bean:write name="indicator" property="targetValComments" /></textarea>
																				</td>
																			</tr>
																			<c:if test="${indicator.targetValDate != null}">
																				<tr>
																					<td class="inside">
																						<digi:trn key="aim:meRevisedTargetValue">Revised Target Value</digi:trn>
																						<font color="red">*</font>
																					</td>
																					<td class="inside">
																						<input type="text" name="revisedTargetVal" value="<bean:write name="indicator" property="revisedTargetVal" />" class="inputx" size="10">
																					</td>
																					<td class="inside">
																						<digi:trn key="aim:meDate">Date</digi:trn>
																						<font color="red">*</font>
																					</td>
																					<td>
																						<input type="text" name="revisedTargetValDate" value="<bean:write name="indicator" property="revisedTargetValDate" />" class="inputx" size="10" readonly="true" id="revisedTargetValDate">
																					</td class="inside">
																					<td align="left" vAlign="center" class="inside">
																						<a id="date3" href='javascript:pickDateById("date3","revisedTargetValDate")'>
																							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																						</a>
																					</td>
																				</tr>
																				<tr>
																					<td class="inside">
																						<digi:trn key="aim:meRevisedTargetValComments">Comments</digi:trn>
																					</td>
																					<td colspan="4" class="inside">
																						<textarea name="revisedTargetValComments" class="inputx" rows="2" cols="38"><bean:write name="indicator" property="revisedTargetValComments" /></textarea>
																					</td>
																				</tr>
																			</c:if>
																			<tr>
																				<td colspan="5" class="inside">
																					<input type="submit" value="Save Values" class="buttonx_sm" onclick="return validate()">
																				</td>
																			</tr>
																		</table>
																	</c:if>
		
																</logic:iterate>
															</table>
														</td>
													</tr>
											</logic:notEmpty>

                                 
                                                                        </table>						
</div>
                                </div>
										<tr>
											<td align="right">
												<a onClick="history.go(-1);return true;" style="cursor: pointer;color: blue;text-decoration: underline;">
													<digi:trn>Go Back</digi:trn>
												</a>
												&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
										</tr>

</table>

</td></tr>

</table>

</digi:form>

