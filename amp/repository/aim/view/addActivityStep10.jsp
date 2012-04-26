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
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>

<script language="JavaScript">
<!--

	var invalidBaseValue="<digi:trn>Invalid Base value or Base value not entered</digi:trn>";
	var baseValueDateNotEntered="<digi:trn>Base value date not entered</digi:trn>";
	var targetValueNotEntered="<digi:trn>Target value not entered</digi:trn>";
	var targetValueDateNotEntered="<digi:trn>Target value date not entered</digi:trn>";
	var currValueNotEntered="<digi:trn>Current value not entered</digi:trn>";
	var currValueDateNotEntered="<digi:trn>Current value date not entered</digi:trn>";
	var invalidRevisedTargetValue="<digi:trn>Invalid Revised target value or Revised target value not entered</digi:trn>";
	var revisedTargetValueDateNotEntered="<digi:trn>Revised target value date not entered</digi:trn>";
	var deleteThisIndicator="<digi:trn>Are you sure you want to delete this Indicator?</digi:trn>";

	var numericValueNeeded="<digi:trn>Please enter a numeric value</digi:trn>";
	
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
		//
		var baseVal = document.getElementsByName('indicator.baseVal')[0];
		var targetVal = document.getElementsByName('indicator.targetVal')[0];
		var revTargetVal = null;
		if (document.getElementsByName('indicator.revTargetValDate')[0] != null) {
			revTargetVal = document.getElementsByName('indicator.revTargetVal')[0];
		}
		var currentVal = document.getElementsByName('indicator.currentVal')[0];

		//
		if (isEmpty(baseVal.value) == true) {
			alert(invalidBaseValue);
			baseVal.focus();
			return false;
		} else if (!containsValidNumericValueZeroIncluded(baseVal)) {
			alert(numericValueNeeded);
			baseVal.focus();
			return false;
		}
		if (isEmpty(document.getElementsByName('indicator.baseValDate')[0].value) == true) {
			alert(baseValueDateNotEntered);
			document.getElementsByName('indicator.baseValDate')[0].focus();
			return false;
		}
		if (isEmpty(targetVal.value) == true) {
			alert(targetValueNotEntered);
			targetVal.focus();
			return false;
		} else if (!containsValidNumericValueZeroIncluded(targetVal)) {
			alert(numericValueNeeded);
			targetVal.focus();
			return false;
		}
		if (isEmpty(document.getElementsByName('indicator.targetValDate')[0].value) == true) {
			alert(targetValueDateNotEntered);
			document.getElementsByName('indicator.targetValDate')[0].focus();
			return false;
		}
		if (document.getElementsByName('indicator.revTargetValDate')[0] != null) {
			if (isEmpty(revTargetVal.value) == true) {
				alert(invalidRevisedTargetValue);
				revTargetVal.focus();
				return false;
			} else if (!containsValidNumericValueZeroIncluded(revTargetVal)) {
				alert(numericValueNeeded);
				revTargetVal.focus();
				return false;
			}
			if (isEmpty(document.getElementsByName('indicator.revTargetValDate')[0].value) == true) {
				alert(revisedTargetValueDateNotEntered);
				document.getElementsByName('indicator.revTargetValDate')[0].focus();
				return false;
			}
		}
        if(typeof currentVal != 'undefined'){
            if (isEmpty(currentVal.value) == true) {
                alert(currValueNotEntered);
                document.getElementsByName('indicator.currentVal')[0].focus();
                return false;
            } else if (!containsValidNumericValueZeroIncluded(currentVal)) {
                alert(numericValueNeeded);
                currentVal.focus();
                return false;
            }
            if (isEmpty(document.getElementsByName('indicator.currentValDate')[0].value) == true) {
                alert(currValueDateNotEntered);
                document.getElementsByName('indicator.currentValDate')[0].focus();
                return false;
            }
        }
		return true;
	}

	function validateEntryByMember() {
        var txt=null;

        txt=document.getElementById("txtBaseValue");
		if (txt!=null && !containsValidNumericValueZeroIncluded(txt)) {
			alert(invalidBaseValue);
			txt.focus();
			return false;
		}

        txt=document.getElementById("txtBaseValDate");
		if (txt!=null && isEmpty(txt.value)) {
			alert(baseValueDateNotEntered);
			txt.focus();
			return false;
		}

        txt=document.getElementById("txtRevTargetVal");
		if (txt!=null && !containsValidNumericValueZeroIncluded(txt)) {
			alert(targetValueNotEntered);
			txt.focus();
			return false;
		} else if (txt==null) {//check for target value only
			txt=document.getElementById("txtTargetVal");
			if (txt!=null && !containsValidNumericValueZeroIncluded(txt)) {
				alert(targetValueNotEntered);
				txt.focus();
				return false;
			}
		}

        txt=document.getElementById("txtRevisedTargetValDate");
		if (txt!=null && isEmpty(txt.value)) {
			alert(targetValueDateNotEntered);
			txt.focus();
			return false;
		} else if (txt==null) {//check for target date value only
			txt=document.getElementById("txtTargetValDate");
			if (txt!=null && isEmpty(txt.value)) {
				alert(targetValueDateNotEntered);
				txt.focus();
				return false;
			}
		}

        txt=document.getElementById("txtCurrVal");
		if (txt!=null && isEmpty(txt.value)) {
			alert(currValueNotEntered);
			txt.focus();
			return false;
		}

        txt=document.getElementById("txtCurrValDate");
		if (txt!=null && isEmpty(txt.value)) {
			alert(currValueDateNotEntered);
			txt.focus();
			return false;
		}
		
		return true;

	}

	function setValues(val) {
		var valid;
		if(document.aimEditActivityForm.teamLead.value) {
			valid = validateEntryByLeader();			
		} else {
			valid = validateEntryByMember();
		}
		if (valid == true) {
			document.getElementsByName("indicator.indicatorId")[0].value = val;
			document.aimEditActivityForm.submit();
		}
	}

	function addIndicator()
	{
		openNewRsWindow(800, 400);
		<digi:context name="selCreateInd" property="context/module/moduleinstance/selectCreateIndicators.do" />
		document.aimEditActivityForm.action = "<%=selCreateInd%>?addIndicatorForStep9=true&clear=true";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}

	function deleteIndicator()
		{
			return confirm(deleteThisIndicator);
		}


-->
</script>

<jsp:include page="scripts/newCalendar.jsp"  />

<digi:instance property="aimEditActivityForm" />
<digi:form action="/saveIndicatorValues.do~edit=true" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="indicator.indicatorId" />
<html:hidden property="indicator.indicatorValId" />
<html:hidden property="activityId" />
<html:hidden property="teamLead" />
<input type="hidden" name="edit" value="true">
  <c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
  </c:set>



	<feature:display name="Activity" module="M & E"></feature:display>
	<feature:display name="Activity Dashboard" module="M & E"></feature:display>
	<feature:display name="Admin" module="M & E"></feature:display>


<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" vAlign="top" align="center" border="0">
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left valign="top" class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
									<c:set var="message">
									<digi:trn>WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>
									<c:set var="quote">'</c:set>
									<c:set var="escapedQuote">\'</c:set>
									<c:set var="msg">
									${fn:replace(message,quote,escapedQuote)}
									</c:set>

									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')"
									title="Click here to view MyDesktop ">
										<digi:trn>
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								
                                                                 <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">

                                                                     <c:set property="translation" var="trans">
                                                                         <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                                                             Click here to goto Add Activity Step ${step.stepActualNumber}
                                                                         </digi:trn>
                                                                     </c:set>

                                                                     <c:if test="${!index.last}">

                                                                         <c:if test="${index.first}">

                                                                             <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">


                                                                                 <c:if test="${aimEditActivityForm.editAct == true}">
                                                                                     <digi:trn>
                                                                                         Edit Activity - Step 1
                                                                                     </digi:trn>
                                                                                 </c:if>
                                                                                 <c:if test="${aimEditActivityForm.editAct == false}">
                                                                                     <digi:trn>
                                                                                         Add Activity - Step 1
                                                                                     </digi:trn>
                                                                                 </c:if>

                                                                             </digi:link>
                                                                             &nbsp;&gt;&nbsp;
                                                                         </c:if>
                                                                         <c:if test="${!index.first}">
                                                                             <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                                                                 <digi:trn key="aim:addActivityStep${step.stepActualNumber}">
                                                                                 Step ${step.stepActualNumber}
                                                                             </digi:trn>
                                                                             </digi:link>
                                                                             &nbsp;&gt;&nbsp;
                                                                         </c:if>
                                                                     </c:if>



                                                                     <c:if test="${index.last}">

                                                                         <c:if test="${index.first}">



                                                                             <c:if test="${aimEditActivityForm.editAct == true}">
                                                                                 <digi:trn>
                                                                                     Edit Activity - Step 1
                                                                                 </digi:trn>
                                                                             </c:if>
                                                                             <c:if test="${aimEditActivityForm.editAct == false}">
                                                                                 <digi:trn>
                                                                                     Add Activity - Step 1
                                                                                 </digi:trn>
                                                                             </c:if>
                                                                         </c:if>


                                                                         <c:if test="${!index.first}">
                                                                             <digi:trn key="aim:addActivityStep${step.stepActualNumber}"> Step ${step.stepActualNumber}</digi:trn>
                                                                         </c:if>



                                                                     </c:if>







                                                                 </c:forEach>

								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 valign="center" width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn>
										Add New Activity
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn>
										Edit Activity
									</digi:trn>:
										<bean:write name="aimEditActivityForm" property="identification.title"/>
								</c:if>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="75%" vAlign="top">
						<table cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td width="100%">
									<table cellpadding="0" cellspacing="0" width="100%" border="0">
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn>
													Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>
                                                                                                 ${fn:length(aimEditActivityForm.steps)}:
                                                                                                 <digi:trn>
                                                                                                     Monitoring and Evaluation
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
								<table width="95%" bgcolor="#f4f4f2" border="0">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn>Monitoring and Evaluation - Indicators</digi:trn>">
										<b><digi:trn>Monitoring and Evaluation</digi:trn></b>
										</a>
									</td></tr>
									<tr><td>
										<table width="100%" cellSpacing=2 cellPadding=2 valign="top" align=left class="box-border-nopadding" border="0">
											<tr>
												<td width="32%" align="center" colspan="6"><b>
													<digi:trn>Indicators</digi:trn>
													</b>
												</td>
											</tr>
											<logic:empty name="aimEditActivityForm" property="indicator.indicatorsME">
											<tr>
												<td width="32%" bgcolor="#f4f4f2" align="center" colspan="6"><font color="red"><b>
													<digi:trn>No Activity specific Indicators & No Global Indicators present</digi:trn>
													</b></font>
												</td>
											</tr>
											</logic:empty>
											<logic:notEmpty name="aimEditActivityForm" property="indicator.indicatorsME">
											<logic:iterate name="aimEditActivityForm" property="indicator.indicatorsME" id="indicator" type="org.digijava.module.aim.helper.ActivityIndicator">
											<tr>
												<td bgcolor="#f4f4f2" align="left" colspan="5">&nbsp;&nbsp;
													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
													<c:if test="${aimEditActivityForm.indicator.expIndicatorId==indicator.indicatorId}">
														<digi:link href="/nondetailedIndicator.do~edit=true">
															<img src= "../ampTemplate/images/arrow_down.gif" border="0">
														</digi:link>
													</c:if>
													<c:if test="${aimEditActivityForm.indicator.expIndicatorId!=indicator.indicatorId}">
														<c:set target="${urlParams}" property="indValId">
															<bean:write name="indicator" property="indicatorId" />
														</c:set>
														<c:set target="${urlParams}" property="activityId">
															<bean:write name="aimEditActivityForm" property="activityId"/>
														</c:set>
														<c:set target="${urlParams}" property="edit" value="true" />
														<digi:link href="/detailedIndicator.do" name="urlParams">
															<img src= "../ampTemplate/images/arrow_right.gif" border="0">
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
												<td bgcolor="#f4f4f2" align="right">
													<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams1}" property="indId">
														<bean:write name="indicator" property="indicatorId" />
													</c:set>
													<digi:link href="/removeIndFromActivity.do" name="urlParams1">
														<img src="../ampTemplate/images/trash_12.gif" border="0" onclick="return deleteIndicator()"/>
													</digi:link>
												</td>
											</tr>

											<c:if test="${aimEditActivityForm.indicator.expIndicatorId==indicator.indicatorId}">
											<tr>
												<td colspan="6">
													<table cellspacing="0" cellpadding="6" valign="top" align="center" width="90%">
														<field:display name="Logframe Category" feature="Activity">
															<tr>
																<td >
																	<digi:trn>Logframe Category</digi:trn>
																</td>
																<td>
																	<category:showoptions name="aimEditActivityForm" property="indicator.logframeCategory" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.LOGFRAME_KEY %>" styleClass="inp-text" />
																</td>
															</tr>
														</field:display>


															<tr>
																<field:display name="Base Value" feature="Activity"></field:display>
																<td><b>
																	<digi:trn>Base Value</digi:trn></b>
																	<font color="red">*</font></td>
																<td>
																<html:text property="indicator.baseVal" size="10" maxlength="10" styleId="txtBaseValue"/>
																	
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<field:display name="Date Base Value" feature="Activity"></field:display>
																<td align="right">
																	<digi:trn>Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																
																
																	<input type="text" name="indicator.baseValDate"
																	value="<bean:write name="indicator" property="baseValDate" />"
																	class="inp-text" size="10" readonly="readonly" id="txtBaseValDate">&nbsp;&nbsp;
																
																	<a id="clear1" href="javascript:clearDate(document.getElementById("txtBaseValDate"), 'clear1')">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																
																	<a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("txtBaseValDate"),"clear1")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																	</a>
																
																</td>
															</tr>
															<field:display name="Comments Base Value" feature="Activity"></field:display>
															<tr>
																<td><digi:trn>Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="indicator.baseValComments" cols="38" rows="2" styleClass="inp-text"/>

																</td>
															</tr>
															<tr>
															<field:display name="Target Value" feature="Activity"></field:display>
																<td><b>
																	<digi:trn>Target Value</digi:trn>
																	</b><font color="red">*</font>
																</td>
																<c:if test="${indicator.targetValDate == null}">

																<td>
																<html:text property="indicator.targetVal" size="10" maxlength="10" styleId="txtTargetVal"/>
																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	<digi:trn>Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																	
																	<input type="text" name="indicator.targetValDate"
																	value="<bean:write name="indicator" property="targetValDate" />"
																	class="inp-text" size="10" readonly="readonly" id="txtTargetValDate">&nbsp;&nbsp;
																	
																	<a id="clear2" href="javascript:clearDate(document.getElementById('txtTargetValDate'), 'clear2')">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																	
																	<a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("txtTargetValDate"),"clear2")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																	</a>
															
																</td>
																</c:if>
																<c:if test="${indicator.targetValDate != null}">
																<td>
																	<input type="text" name="indicator.targetVal"
																	value="<bean:write name="indicator" property="targetVal" />"
																	class="inp-text" size="10" disabled="true" id="txtTargetVal">
																</td>
																
																
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	<digi:trn>Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																	<input type="text" name="indicator.targetValDate"
																	value="<bean:write name="indicator" property="targetValDate" />"
																	class="inp-text" size="10"  id="txtTargetValDate">&nbsp;&nbsp;
																</td>
																</c:if>
															</tr>
															<tr>
																<td><digi:trn>Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="indicator.targetValComments" cols="38" rows="2" styleClass="inp-text"/>

																</td>
															</tr>
															<c:if test="${indicator.targetValDate != null}">
															<tr>
																<td><b>
																	<digi:trn>Revised Target Value</digi:trn>
																	</b><font color="red">*</font>
																</td>
																<td>
																<html:text property="indicator.revTargetVal" size="10" maxlength="10" styleId="txtRevTargetVal"/>
																	

																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																<td align="right">
																	<digi:trn key="aim:meDate">Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																	<input type="text" name="indicator.revTargetValDate"
																	value="<bean:write name="indicator" property="revisedTargetValDate" />"
																	class="inp-text" size="10" readonly="readonly" id="txtRevisedTargetValDate">&nbsp;&nbsp;

																	<a id="clear3" href="javascript:clearDate(document.getElementById('txtRevisedTargetValDate'), 'clear3')">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																	<a id="date3" href='javascript:pickDateWithClear("date3",document.getElementById("txtRevisedTargetValDate"),"clear3")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																	</a>
																</td>
															</tr>
															<tr>
																<td><digi:trn>Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="indicator.revTargetValComments" cols="38" rows="2" styleClass="inp-text"/>
																</td>
															</tr>
															</c:if>



														<logic:notEmpty name="indicator" property="priorValues" >
															<tr bgColor="#dddddb"><td bgColor=#dddddb align="left" colspan="5"><b>
																<digi:trn>Prior Values</digi:trn> :</b>
															</td></tr>
															<logic:iterate name="indicator" property="priorValues"
															id="priorValues" type="org.digijava.module.aim.helper.PriorCurrentValues">
																<tr bgColor=#f4f4f2>
																	<td align="center">
																		<img src= "../ampTemplate/images/arrow_dark.gif" border="0">
																	</td>
																	<td>
																		<bean:write name="priorValues" property="currValue" />
																	</td>
																	<td>&nbsp;&nbsp;&nbsp;</td>
																	<td align="right">
																		<digi:trn>Date</digi:trn>:
																	</td>
																	<td align="left">&nbsp;&nbsp;
																		<bean:write name="priorValues" property="currValDate" />
																	</td>
																</tr>
																<tr>
																	<td><digi:trn>Comments</digi:trn>
																	</td>
																	<td colspan="4">
																		<bean:write name="priorValues" property="comments" />
																	</td>
																</tr>
															</logic:iterate>
														</logic:notEmpty>

															<field:display name="Current Value" feature="Activity">
															<tr>																
																<td><b>
																	<digi:trn>Current Value</digi:trn>
																	<font color="red">*</font>
																</b></td>
																<td>
																	<html:text property="indicator.currentVal" size="10" maxlength="10" styleId="txtCurrVal"/>																	

																</td>
																<td>&nbsp;&nbsp;&nbsp;</td>
																</field:display>
																<field:display name="Date Current Value" feature="Activity">
																<td align="right">
																	<digi:trn>Date</digi:trn>
																	<font color="red">*</font>
																</td>
																<td align="left">
																	<input type="text" name="indicator.currentValDate"
																	value="<bean:write name="indicator" property="currentValDate" />"
																	class="inp-text" size="10" readonly="readonly" id="txtCurrValDate">&nbsp;&nbsp;

																	<a id="clear4" href="javascript:clearDate(document.getElementById('txtCurrValDate'), 'clear4')">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																	</a>
																	<a id="date4" href='javascript:pickDateWithClear("date4",document.getElementById("txtCurrValDate"),"clear4")'>
																		<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
																	</a>
																</td>																
															</tr>
															</field:display>
															<field:display name="Comments Current Value" feature="Activity">
															<tr>
																<td><digi:trn>Comments</digi:trn>
																</td>
																<td colspan="4">
																<html:textarea property="indicator.currentValComments" cols="38" rows="2" styleClass="inp-text"/>

																</td>
															</tr>
															</field:display>
														<field:display name="Risk" feature="Activity">
														<tr>
															<td>&nbsp;&nbsp;&nbsp;</td>
															<td align="right"><b>
															<digi:trn>Risk</digi:trn>
															</b></td>
															<td>
																<html:select property="indicator.indicatorRisk" styleClass="inp-text">
																<option value="0"><digi:trn key="help:selectRisk">Select Risk</digi:trn></option>
																	<logic:iterate id="currRisk" name="aimEditActivityForm" property="indicator.riskCollection">
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
																<input type="button" class="dr-menu" value="<digi:trn key='btn:setValues'>Set Values</digi:trn>"
																onclick="setValues('${indicator.indicatorId}')">
															</td>
															<td>&nbsp;</td>
														</tr>
														<%--
														</logic:notEmpty>
														--%>

													</table>
												</td>
											</tr>
											</c:if>
											</logic:iterate>
											</logic:notEmpty>
											<tr>
												<td width="32%" align="center" colspan="6">
													&nbsp;
												</td>
											</tr>
											<field:display name="Add Indicator Button" feature="Activity">
											<tr>
												<td width="32%" align="center" colspan="6">
													<input type="button" value="<digi:trn key='btn:addIndicator'>Add Indicator</digi:trn>"  class="dr-menu" onclick="addIndicator()">
												</td>
											</tr>
											</field:display>
											<tr>
												<td width="32%" align="center" colspan="6">
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
							<jsp:include page="editActivityMenu.jsp"  />
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

	clearDisplay(document.getElementById('txtBaseValDate'), "clear1");
	clearDisplay(document.getElementById('txtTargetValDate'), "clear2");
	clearDisplay(document.getElementById('txtRevisedTargetValDate'), "clear3");
	clearDisplay(document.getElementById('txtCurrValDate'), "clear4");
</script>
