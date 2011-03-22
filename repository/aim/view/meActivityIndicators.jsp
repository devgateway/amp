<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>



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

		<digi:context name="selCreateInd" property="context/module/moduleinstance/selectCreateIndicators.do" />

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



<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">

<tr><td width="100%" valign="top" align="left">

<jsp:include page="teamPagesHeader.jsp" flush="true" />

</td></tr>

<tr><td width="100%" valign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>

	<tr>

		<td width=14>&nbsp;</td>

		<td align=left vAlign=top width=750>



			<table cellPadding=5 cellSpacing=0 width="100%">

				<tr>

					<td height=33><span class=crumb>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>

						</c:set>

						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:portfolio">Portfolio</digi:trn>

						</digi:link>

						&nbsp;&gt;&nbsp;

						<c:set var="translation">

							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>

						</c:set>

						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>

						</digi:link>

						&nbsp;&gt;&nbsp;

						<digi:trn key="aim:activityList">Activity List</digi:trn>

					</td>

				</tr>

				<tr>

					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>

					</td>

				</tr>

				<tr>

					<td noWrap width=571 vAlign="top">

						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">

							<tr>

								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="6" scope="request"/>

									<jsp:include page="teamSetupMenu.jsp" flush="true" />

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td valign="top">
                                    <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
	                                    <div align="center">
                                	

									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">

										<tr><td>

											<digi:errors />

										</td></tr>

										<tr>

											<td bgColor=#ffffff valign="top">

												<table border=0 cellPadding=0 cellSpacing=0  width="100%">

													<tr>

														<td align="left" width="100%" valign="center">

															<digi:trn key="aim:mandatoryFields">The fields marked <font color="red">*</font> are mandatory

															</digi:trn>

														</td>

													</tr>
                                                    <tr><td>
                                                
                                                        <table width="100%" cellspacing=1 cellpadding=3 bgcolor="ffffff" border=0>
                                                
                                                            <tr bgcolor="#ffffff">
                                                
                                                                <td width="9">
                                                
                                                                    <img src= "../ampTemplate/images/bullet_red.gif" border=0>
                                                
                                                                </td>
                                                
                                                                <td width="100">
                                                
                                                                    <digi:trn key="aim:globalIndicator">Global Indicator</digi:trn>
                                                
                                                                </td>
                                                
                                                                <td width="9">
                                                
                                                                    <img src= "../ampTemplate/images/bullet_grey.gif" border=0>
                                                
                                                                </td>
                                                
                                                                <td>
                                                
                                                                    <digi:trn key="aim:activitySpecificIndicator">Activity Specific Indicator</digi:trn>
                                                
                                                                </td>
                                                
                                                            </tr>
                                                
                                                        </table>
                                                	<br />
                                                    </td>
                                                    </tr>

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"

															bgcolor="#ffffff">

																<tr><td valign="center" align="center" bgcolor="#999999" height="20" style="color:black;">

																	<b><digi:trn key="aim:meIndicators">Indicators</digi:trn></b>

																</td></tr>

															</table>

														</td>

													</tr>

													<logic:empty name="aimUpdateIndicatorValuesForm" property="indicators">

													<tr>

														<td align="center">

															<table width="100%" cellSpacing=2 cellPadding=3 vAlign="top" align="center"

															bgcolor="#f4f4f2">

																<tr><td bgcolor="#f4f4f2" align="center">

																	<digi:trn key="aim:noIndicatorsPresent">

																		No indicators present

																	</digi:trn>

																</td></tr>

															</table>

														</td>

													</tr>

													</logic:empty>



													<logic:notEmpty name="aimUpdateIndicatorValuesForm" property="indicators">

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellSpacing=1 cellPadding=4 vAlign="top" align="left" bgcolor="#f4f4f2"

															border=0>

															<logic:iterate name="aimUpdateIndicatorValuesForm" property="indicators" id="indicator"

															type="org.digijava.module.aim.helper.ActivityIndicator">

																<tr bgcolor="#f4f4f2">

																<td width="12" valign="center">

																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

																	<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId==indicator.indicatorId}">

																		<digi:link href="/collapseIndicator.do">

																		<img src= "../ampTemplate/images/arrow_down.gif" border=0>

																		</digi:link>

																	</c:if>

																	<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId!=indicator.indicatorId}">

																		<c:set target="${urlParams}" property="indValId">

																			<bean:write name="indicator" property="indicatorId" />

																		</c:set>

																		<c:set target="${urlParams}" property="activityId">

																			<bean:write name="aimUpdateIndicatorValuesForm" property="activityId"/>

																		</c:set>

																		<digi:link href="/expandIndicator.do" name="urlParams">

																			<img src= "../ampTemplate/images/arrow_right.gif" border=0>

																		</digi:link>

																	</c:if>

																</td>

																<td width="9">

																	<c:if test="${indicator.defaultInd == true}">

																		<img src= "../ampTemplate/images/bullet_red.gif" border=0>

																	</c:if>

																	<c:if test="${indicator.defaultInd == false}">

																		<img src= "../ampTemplate/images/bullet_grey.gif" border=0>

																	</c:if>

																</td>

																<td valign="top" width="100%">

																	<b>

																		<bean:define id="indName">

																			<bean:write name="indicator" property="indicatorName"/>

																		</bean:define>

																		<digi:trn key='<%="aim:" + indName%>'><%=indName%></digi:trn>

																	</b>

																</td>

																<td valign="top" width="100">

																	<b><bean:write name="indicator" property="indicatorCode" /></b>

																</td>

																<td valign="top" width="12">

																	<c:if test="${indicator.indicatorValId > 0}">

																	<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>

																	<c:set target="${urlParams1}" property="event" value="delete" />

																	<c:set target="${urlParams1}" property="indicatorValId">

																		<bean:write name="indicator" property="indicatorValId" />

																	</c:set>



																	<digi:link href="/deleteIndicatorValue.do" name="urlParams1"

																	onclick="return confirmDelete()">

																		<img src= "../ampTemplate/images/trash_12.gif" border=0>

																	</digi:link>

																	</c:if>

																</td></tr>

																<c:if test="${aimUpdateIndicatorValuesForm.expIndicatorId==indicator.indicatorId}">

																<tr>

																	<td width="12">&nbsp;</td>

																	<td colspan="3">



																		<table cellspacing="0" cellpadding="3" valign="top" align="left"

																		border=0>

																			<tr>

																				<td><digi:trn key="aim:meBaseValue">Base Value</digi:trn>

																				<font color="red">*</font>

																				</td>

																				<td><input type="text" name="baseVal"

																				value="<bean:write name="indicator" property="baseVal" />"

																				class="inp-text" size="10"></td>

																				<td><digi:trn key="aim:meDate">Date</digi:trn>

																				<font color="red">*</font></td>

																				<td><input type="text" name="baseValDate"

																				value="<bean:write name="indicator" property="baseValDate" />"

																				class="inp-text" size="10" readonly="true"

																				id="baseValDate"></td>

																				<td align="left" vAlign="center">
																					<a id="date1" href='javascript:pickDate("date1",document.aimUpdateIndicatorValuesForm.baseValDate)'>
																						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																					</a>
																				</td>

																			</tr>

																			<tr>

																				<td><digi:trn key="aim:meBaseValueComments">Comments</digi:trn>

																				</td>

																				<td colspan="4">

																					<textarea name="baseValComments" class="inp-text" rows="2" cols="38"><bean:write name="indicator" property="baseValComments" /></textarea>

																				</td>

																			</tr>



																			<tr>

																				<td><digi:trn key="aim:meTargetValue">Target Value</digi:trn>

																				<font color="red">*</font>

																				</td>

																				<c:if test="${indicator.targetValDate != null}">

																				<td>

																					<input type="text" name="targetVal"

																					value="<bean:write name="indicator" property="targetVal" />"

																					class="inp-text" size="10" disabled="true">

																				</td>

																				<td><digi:trn key="aim:meDate">Date</digi:trn>

																				<font color="red">*</font></td>

																				<td>

																					<input type="text" name="targetValDate"

																					value="<bean:write name="indicator" property="targetValDate" />"

																					class="inp-text" size="10" disabled="true"

																					id="targetValDate">

																				</td>

																				<td align="left" vAlign="center">&nbsp;

																					

																				</td>

																				</c:if>

																				<c:if test="${indicator.targetValDate == null}">

																				<td>

																					<input type="text" name="targetVal"

																					value="<bean:write name="indicator" property="targetVal" />"

																					class="inp-text" size="10">

																				</td>

																				<td><digi:trn key="aim:meDate">Date</digi:trn>

																				<font color="red">*</font></td>

																				<td>

																					<input type="text" name="targetValDate"

																					value="<bean:write name="indicator" property="targetValDate" />"

																					class="inp-text" size="10" readonly="true"

																					id="targetValDate">

																				</td>

																				<td align="left" vAlign="center">
																					<a id="date2" href='javascript:pickDate("date2",document.aimUpdateIndicatorValuesForm.targetValDate)'>
																						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																					</a>
																				</td>

																				</c:if>

																			</tr>

																			<tr>

																				<td><digi:trn key="aim:meTargetValComments">Comments</digi:trn>

																				</td>

																				<td colspan="4">

																					<textarea name="targetValComments" class="inp-text" rows="2" cols="38"><bean:write name="indicator" property="targetValComments" /></textarea>

																				</td>

																			</tr>

																			<c:if test="${indicator.targetValDate != null}">

																			<tr>

																				<td><digi:trn key="aim:meRevisedTargetValue">Revised Target Value</digi:trn>

																				<font color="red">*</font>

																				</td>

																				<td><input type="text" name="revisedTargetVal"

																				value="<bean:write name="indicator" property="revisedTargetVal" />"

																				class="inp-text" size="10"></td>

																				<td><digi:trn key="aim:meDate">Date</digi:trn>

																				<font color="red">*</font></td>

																				<td><input type="text" name="revisedTargetValDate"

																				value="<bean:write name="indicator" property="revisedTargetValDate" />"

																				class="inp-text" size="10" readonly="true"

																				id="revisedTargetValDate"></td>

																				<td align="left" vAlign="center">
																					<a id="date3" href='javascript:pickDate("date3",document.aimUpdateIndicatorValuesForm.revisedTargetValDate)'>
																						<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																					</a>
																				</td>

																			</tr>

																			<tr>

																				<td><digi:trn key="aim:meRevisedTargetValComments">Comments</digi:trn>

																				</td>

																				<td colspan="4">

																					<textarea name="revisedTargetValComments" class="inp-text" rows="2" cols="38"><bean:write name="indicator" property="revisedTargetValComments" /></textarea>

																				</td>

																			</tr>

																			</c:if>

																			<tr>

																				<td colspan="4">

																					<input type="submit" value="Save Values" class="dr-menu"

																					onclick="return validate()">

																				</td>

																			</tr>

																		</table>



																	</td>

																	<td width="12">&nbsp;</td>

																</tr>

																</c:if>

															</logic:iterate>

														</table>

														</td>

													</tr>

													</logic:notEmpty>

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"

															bgcolor="#ffffff">

																<tr><td valign="center" align="center" bgcolor="#dddddd" height="20">

<!-- the following will be uncommented further -->																	
<!--																	<input class="dr-menu" type="button" name="addIndicator" value="<digi:trn key="aim:addIndicator">Add Indicator</digi:trn>"-->
<!---->
<!--																	property="activityId"-->
<!---->
<!--																	onclick="return addIndicators()">-->

																</td></tr>

															</table>

														</td>

													</tr>

												</table>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a onClick="history.go(-1);return true;" style="cursor: pointer;color: blue;text-decoration: underline;">
													<digi:trn>Go Back</digi:trn>
												</a>
												&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
										</tr>
									</table>

	                                    </div>
                                    </div>
								</td>

							</tr>

							<tr><td bgColor=#f4f4f2>&nbsp;

								

							</td></tr>

						</table>

					</td>

				</tr>


			</table>

		</td>

	</tr>



</table>

</td></tr>

</table>

</digi:form>

