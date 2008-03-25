<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<script language="JavaScript">
<!--

	function validateForm() {
		return true;
	}

	function resetAll()
	{
		<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
		document.aimEditActivityForm.action = "<%= resetAll %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
		return true;
	}

	function validateEntryByLeader() {

		if (isEmpty(document.aimEditActivityForm.baseVal.value) == true) {
			alert("Invalid Base value or Base value not entered");
			document.aimEditActivityForm.baseVal.focus();
			return false;
		}
		if (isEmpty(document.aimEditActivityForm.baseValDate.value) == true) {
			alert("Base value date not entered");
			document.aimEditActivityForm.baseValDate.focus();
			return false;
		}
		if (isEmpty(document.aimEditActivityForm.targetVal.value) == true) {
			alert("Target value not entered");
			document.aimEditActivityForm.targetVal.focus();
			return false;
		}
		if (isEmpty(document.aimEditActivityForm.targetValDate.value) == true) {
			alert("Target value date not entered");
			document.aimEditActivityForm.targetValDate.focus();
			return false;
		}

		if (document.aimEditActivityForm.revTargetValDate != null) {

			if (isEmpty(document.aimEditActivityForm.revTargetVal.value) == true) {
				alert("Invalid Revised target value or Revised target value not entered");
				document.aimEditActivityForm.currentVal.focus();
				return false;
			}

			if (isEmpty(document.aimEditActivityForm.revTargetValDate.value) == true) {
				alert("Revised target value date not entered");
				document.aimEditActivityForm.revTargetValDate.focus();
				return false;
			}
		}

/*
		if (containsValidNumericValue(document.aimEditActivityForm.currentVal) == false) {
			alert("Invalid Current value or Current value not entered");
			document.aimEditActivityForm.currentVal.focus();
			return false;
		}

		if (isEmpty(document.aimEditActivityForm.currValDate.value) == true ^ isEmpty(document.aimEditActivityForm.currentVal.value) == true) {
			alert("Please fill Current Value and Current Value Date");
			document.aimEditActivityForm.currValDate.focus();
			return false;
		}
		*/
		return true;
	}

	function validateEntryByMember() {
		/*
		if (containsValidNumericValue(document.aimEditActivityForm.currentVal) == false) {
			alert("Invalid Current value or Current value not entered");
			document.aimEditActivityForm.currentVal.focus();
			return false;
		}
		
		if (isEmpty(document.aimEditActivityForm.currValDate.value) == true) {
			alert("Current value date not entered");
			document.aimEditActivityForm.currValDate.focus();
			return false;
		}
		*/
		return true;
	
	}

	function setValues(val) {
		var valid;
		if(document.aimEditActivityForm.teamLead.value) {
			valid = validateEntryByMember();
		} else {
			valid = validateEntryByLeader();
		}
		if (valid == true) {
			document.aimEditActivityForm.indicatorId.value = val;
			document.aimEditActivityForm.submit();
		}
	}

	function addIndicator()
	{
		openNewRsWindow(750, 400);
		<digi:context name="selCreateInd" property="context/module/moduleinstance/selectCreateIndicators.do" />
		document.aimEditActivityForm.action = "<%=selCreateInd%>?addIndicatorForStep9=true";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}


-->
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<digi:instance property="aimEditActivityForm" />
<digi:form action="/saveIndicatorValues.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="indicatorId" />
<html:hidden property="indicatorValId" />
<html:hidden property="activityId" />
<html:hidden property="teamLead" />
<input type="hidden" name="edit" value="true">


	<feature:display name="Activity" module="M & E"></feature:display>
	<feature:display name="Dashboard" module="M & E"></feature:display>
	<feature:display name="Admin" module="M & E"></feature:display>
	
	
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



<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')"
									title="Click here to view MyDesktop ">
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
									<digi:link href="/addActivity.do?step=1_5&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 2" >
									<digi:trn key="aim:addActivityStep2">
									Step 2
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 3" >
									<digi:trn key="aim:addActivityStep3">
									Step 3
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 4">
									<digi:trn key="aim:addActivityStep4">
									Step 4
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 5">
									<digi:trn key="aim:addActivityStep5">
									Step 5
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=5&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 6">
									<digi:trn key="aim:addActivityStep6">
									Step 6
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=6&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 7">
									<digi:trn key="aim:addActivityStep7">
									Step 7
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=7&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 8">
									<digi:trn key="aim:addActivityStep8">
									Step 8
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=8&edit=true" styleClass="comment"
									title="Click here to goto Add Activity Step 9">
									<digi:trn key="aim:addActivityStep9">
									Step 9
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:addActivityStep10" >
									Step 10
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
									</digi:trn>:
										<bean:write name="aimEditActivityForm" property="title"/>
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
												<digi:trn key="aim:step10of10_MonitoringnEvaluation">
													Step 10 of 10: Monitoring and Evaluation
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
													<digi:trn key="aim:meIndicators">Indicators</digi:trn>
													</b>
												</td>
											</tr>
											<logic:empty name="aimEditActivityForm" property="indicatorsME">
											<tr>
												<td width="32%" bgcolor=#f4f4f2 align="center" colspan="2"><font color="red"><b>
													<digi:trn key="aim:meNoActivityGlobalIndicators">
													No Activity specific Indicators & No Global Indicators present
													</digi:trn>
													</b></font>
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
														<digi:link href="/nondetailedIndicator.do~edit=true">
															<img src= "../ampTemplate/images/arrow_down.gif" border=0>
														</digi:link>
													</c:if>
													<c:if test="${aimEditActivityForm.expIndicatorId!=indicator.indicatorId}">
														<c:set target="${urlParams}" property="indValId">
															<bean:write name="indicator" property="indicatorId" />
														</c:set>
														<c:set target="${urlParams}" property="activityId">
															<bean:write name="aimEditActivityForm" property="activityId"/>
														</c:set>
														<c:set target="${urlParams}" property="edit" value="true" />
														<digi:link href="/detailedIndicator.do" name="urlParams">
															<img src= "../ampTemplate/images/arrow_right.gif" border=0>
														</digi:link>
													</c:if>&nbsp;&nbsp;&nbsp;
													<field:display name="Indicator Name" feature="Activity">
														<b><bean:define id="indName">
															<bean:write name="indicator" property="indicatorName"/>
														</bean:define>
														<digi:trn key="<%=indName%>"><%=indName%></digi:trn></b> -
													</field:display>

													<field:display name="Indicator ID" feature="Activity">
														<bean:write name="indicator" property="indicatorCode" />
													</field:display>

												</td>
											</tr>

											<c:if test="${aimEditActivityForm.expIndicatorId==indicator.indicatorId}">
											<tr>
												<td colspan="2">
													<table cellspacing="0" cellpadding="3" valign="top" align="center" width="90%">
														<field:display name="Logframe Category" feature="Activity">
															<tr>
																<td >
																	Logframe Category
																</td>
																<td>
																	<category:showoptions name="aimEditActivityForm" property="logframeCategory" keyName="logframe" styleClass="inp-text" />
																</td>
															</tr>
														</field:display>
														<c:if test="${!aimEditActivityForm.teamLead}">
															<tr>
																<field:display name="Base Value" feature="Activity"></field:display>
																<td><b>
																	<digi:trn key="aim:meBaseValue">Base Value</digi:trn></b>
																</td>
																<td>
																	<bean:write name="indicator" property="baseVal"/>
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<field:display name="Date Base Value" feature="Activity"></field:display>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																</td>
																<td align="left">&nbsp;&nbsp;
																	<bean:write name="indicator" property="baseValDate" />
																</td>
															</tr>
															<field:display name="Comments Base Value" feature="Activity"></field:display>
															<tr>
																<td><digi:trn key="aim:meBaseValueComments">Comments</digi:trn>
																</td>
																<td colspan="4">
																	<bean:write name="indicator" property="baseValComments" />
																</td>
															</tr>
															<tr>
																<field:display name="Target Value" feature="Activity"></field:display>
																<td><b>
																	<digi:trn key="aim:meTargetValue">Target Value</digi:trn></b>
																</td>
																<td>
																	<bean:write name="indicator" property="targetVal" />
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<field:display name="Date Target Value" feature="Activity"></field:display>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																</td>
																<td align="left">&nbsp;&nbsp;
																	<bean:write name="indicator" property="targetValDate" />
																</td>
															</tr>
															<field:display name="Comments Target Value" feature="Activity"></field:display>
															<tr>
																<td><digi:trn key="aim:meTargetValComments">Comments</digi:trn>
																</td>
																<td colspan="4">
																	<bean:write name="indicator" property="targetValComments" />
																</td>
															</tr>
															
															<tr>
																<field:display name="Revised Target Value" feature="Activity"></field:display>
																<td><b>
																	<digi:trn key="aim:meRevisedTargetValue">Revised Target Value</digi:trn></b>
																</td>
																<td>
																	<bean:write name="indicator" property="revisedTargetVal" />
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<field:display name="Date Revised Target Value" feature="Activity"></field:display>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																</td>
																<td align="left">&nbsp;&nbsp;
																	<bean:write name="indicator" property="revisedTargetValDate" />
																</td>
															</tr>
															<field:display name="Comments Revised Target Value" feature="Activity"></field:display>
															<tr>
															
																<td><digi:trn key="aim:meRevisedTargetValComments">Comments</digi:trn>
																</td>
																<td colspan="4">
																	<bean:write name="indicator" property="revisedTargetValComments" />
																</td>
															</tr>
														</c:if>

														<c:if test="${aimEditActivityForm.teamLead}">
															<tr>
																<field:display name="Base Value" feature="Activity"></field:display>
																<td><b>
																	<digi:trn key="aim:meBaseValue">Base Value</digi:trn></b>
																	<font color="red">*</font></td>
																<td>
																<html:text property="baseVal" size="10" maxlength="10"/>
																	<!-- <input type="text" name="baseVal"
																	value="<bean:write name="indicator" property="baseVal"/>"
																	class="inp-text" size="10">
																	-->
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<field:display name="Date Base Value" feature="Activity"></field:display>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																	<input type="text" name="baseValDate"
																	value="<bean:write name="indicator" property="baseValDate" />"
																	class="inp-text" size="10" readonly="true" id="baseValDate">&nbsp;&nbsp;
																	<a id="clear1" href="javascript:clearDate(document.aimEditActivityForm.baseValDate, 'clear1')">
																	 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																	<a id="date1" href='javascript:pickDateWithClear("date1",document.aimEditActivityForm.baseValDate,"clear1")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																	</a>
																</td>
															</tr>
															<field:display name="Comments Base Value" feature="Activity"></field:display>
															<tr>
																<td><digi:trn key="aim:meBaseValueComments">Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="baseValComments" cols="38" rows="2" styleClass="inp-text"/>

																</td>
															</tr>
															<tr>
															<field:display name="Target Value" feature="Activity"></field:display>
																<td><b>
																	<digi:trn key="aim:meTargetValue">Target Value</digi:trn>
																	</b><font color="red">*</font>
																</td>
																<c:if test="${indicator.targetValDate == null}">
																
																<td>
																<html:text property="targetVal" size="10" maxlength="10"/>
																	<!-- <input type="text" name="targetVal"
																	value="<bean:write name="indicator" property="targetVal" />"
																	class="inp-text" size="10">
																	-->
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																	<input type="text" name="targetValDate"
																	value="<bean:write name="indicator" property="targetValDate" />"
																	class="inp-text" size="10" readonly="true" id="targetValDate">&nbsp;&nbsp;
																	<a id="clear2" href="javascript:clearDate(document.aimEditActivityForm.targetValDate, 'clear2')">
																	 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																	<a id="date2" href='javascript:pickDateWithClear("date2",document.aimEditActivityForm.targetValDate,"clear2")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																	</a>
																</td>
																</c:if>
																<c:if test="${indicator.targetValDate != null}">
																<td>
																	<input type="text" name="targetVal"
																	value="<bean:write name="indicator" property="targetVal" />"
																	class="inp-text" size="10" disabled="true">
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																</td>
																<td align="left">
																	<input type="text" name="targetValDate"
																	value="<bean:write name="indicator" property="targetValDate" />"
																	class="inp-text" size="10" readonly="true" id="targetValDate">&nbsp;&nbsp;
																</td>
																</c:if>
															</tr>
															<tr>
																<td><digi:trn key="aim:meTargetValComments">Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="targetValComments" cols="38" rows="2" styleClass="inp-text"/>

																<!--
																<textarea name="targetValComments" class="inp-text" rows="2" cols="38"><bean:write name="indicator" property="targetValComments" /></textarea>
																-->
																</td>
															</tr>
															<c:if test="${indicator.targetValDate != null}">
															<tr>
																<td><b>
																	<digi:trn key="aim:meRevisedTargetValue">Revised Target Value</digi:trn>
																	</b><font color="red">*</font>
																</td>
																<td>
																<html:text property="revTargetVal" size="10" maxlength="10"/>
																	<!-- <input type="text" name="revTargetVal"
																	value="<bean:write name="indicator" property="revisedTargetVal" />"
																	class="inp-text" size="10">
																	-->

																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																	<input type="text" name="revTargetValDate"
																	value="<bean:write name="indicator" property="revisedTargetValDate" />"
																	class="inp-text" size="10" readonly="true" id="revisedTargetValDate">&nbsp;&nbsp;

																	<a id="clear3" href="javascript:clearDate(document.aimEditActivityForm.revTargetValDate, 'clear3')">
																	 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																	<a id="date3" href='javascript:pickDateWithClear("date3",document.aimEditActivityForm.revTargetValDate,"clear3")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																	</a>
																</td>
															</tr>
															<tr>
																<td><digi:trn key="aim:meRevisedTargetValComments">Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="revTargetValComments" cols="38" rows="2" styleClass="inp-text"/>
																<!--
																	<textarea name="revTargetValComments" class="inp-text" rows="2" cols="38"><bean:write name="indicator" property="revisedTargetValComments" /></textarea>
																-->
																</td>
															</tr>
															</c:if>

														</c:if>

														<logic:notEmpty name="indicator" property="priorValues" >
															<tr bgColor=#dddddb><td bgColor=#dddddb align="left" colspan="5"><b>
																<digi:trn key="aim:mePriorValues">Prior Values</digi:trn> :</b>
															</td></tr>
															<logic:iterate name="indicator" property="priorValues"
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
																		<digi:trn key="aim:meDate">Date</digi:trn>:
																	</td>
																	<td align="left">&nbsp;&nbsp;
																		<bean:write name="priorValues" property="currValDate" />
																	</td>
																</tr>
																<tr>
																	<td><digi:trn key="aim:meCurrentValComments">Comments</digi:trn>
																	</td>
																	<td colspan="4">
																		<bean:write name="priorValues" property="comments" />
																	</td>
																</tr>
															</logic:iterate>
														</logic:notEmpty>

														<c:if test="${aimEditActivityForm.teamLead ||
																indicator.baseValDate!=null}">

														<%--
														<logic:notEmpty name="indicator" property="baseValDate">
														--%>
															<tr>
																<field:display name="Current Value" feature="Activity">
																<td><b>
																	<digi:trn key="aim:meCurrentValue">Current Value</digi:trn>
																	
																</b></td>
																<td>
																	<!--<html:text property="currentVal" size="10" maxlength="10"/>-->
																	<input type="text" name="currentVal"
																	value="<bean:write name="indicator" property="actualVal" />"
																	class="inp-text" size="10">
																
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																</field:display>
																<field:display name="Date Current Value" feature="Activity">
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																	
																</td>
																<td align="left">
																	<input type="text" name="currValDate"
																	value="<bean:write name="indicator" property="currentValDate" />"
																	class="inp-text" size="10" readonly="readonly" id="currValDate">&nbsp;&nbsp;

																	<a id="clear4" href="javascript:clearDate(document.aimEditActivityForm.currValDate, 'clear4')">
																	 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																	<a id="date4" href='javascript:pickDateWithClear("date4",document.aimEditActivityForm.currValDate,"clear4")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																	</a>
																</td>
																</field:display>
															</tr>
															<field:display name="Comments Current Value" feature="Activity">
															<tr>
																<td><digi:trn key="aim:meCurrentValComments">Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="currentValComments" cols="38" rows="2" styleClass="inp-text"/>

																	<!--
																	<textarea name="currentValComments" class="inp-text" rows="2" cols="38"><bean:write name="indicator" property="currentValComments" /></textarea>
																 -->
																</td>
															</tr>
															</field:display>
														<field:display name="Risk" feature="Activity">															
														<tr>
															<%--
															<td><b>
																<digi:trn key="aim:meComments">Comments</digi:trn>
															</b></td>
															<td>
																<html:textarea name="aimEditActivityForm" property="comments" styleClass="inp-text"/>
															</td>
															--%>
															<td>&nbsp;&nbsp;&nbsp;</td>
															<td><b>
															<digi:trn key="aim:meRisk">Risk</digi:trn>
															</b></td>
															<td>
																<html:select property="indicatorRisk" styleClass="inp-text">
																<option value="0"><digi:trn key="help:selectRisk">Select Risk</digi:trn></option>
																	<logic:iterate id="currRisk" name="aimEditActivityForm" property="riskCollection">																		
																		<c:set var="trn">
																			<digi:trn key="aim:risk:${currRisk.translatedRatingName}">${currRisk.ratingName}</digi:trn>
																		</c:set>
																		<html:option value="${currRisk.ampIndRiskRatingsId}">${trn}</html:option>																		
																	</logic:iterate>
																</html:select>															
															</td>
														</tr>
														</field:display>
														<tr><td>&nbsp;</td></tr>
														<tr>
															<td>&nbsp;</td>
															<td colspan="3" align="center">
																<input type="button" class="buton" value="<digi:trn key='btn:setValues'>Set Values</digi:trn>"
																onclick="setValues('<c:out value="${indicator.indicatorId}" />')">
															</td>
															<td>&nbsp;</td>
														</tr>
														<%--
														</logic:notEmpty>
														--%>
														</c:if>
													</table>
												</td>
											</tr>
											</c:if>
											</logic:iterate>
											</logic:notEmpty>
											<tr>
												<td width="32%" align="center" colspan="2">
													&nbsp;
												</td>
											</tr>
											<field:display name="Add Indicator Button" feature="Activity">
											<tr>
												<td width="32%" align="center" colspan="2">
													<input type="button" value="<digi:trn key='btn:addIndicator'>Add Indicator</digi:trn>"  class="dr-menu" onclick="addIndicator()">
												</td>
											</tr>
											</field:display>
											<tr>
												<td width="32%" align="center" colspan="2">
													&nbsp;
												</td>
											</tr>
										</table>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
<%--
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
 --%>
								</table>
							</td></tr><%--
							<tr><td>
							<input type="button" value="Add Indicator" class="dr-menu" onclick="addIndicator()">
							</td></tr>--%>
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
		<td width="10">&nbsp;
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>

<script language="JavaScript">
	clearDisplay(document.aimEditActivityForm.baseValDate, "clear1");
	clearDisplay(document.aimEditActivityForm.targetValDate, "clear2");
	clearDisplay(document.aimEditActivityForm.revTargetValDate, "clear3");
	clearDisplay(document.aimEditActivityForm.currValDate, "clear4");
</script>
