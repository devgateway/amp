<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.digijava.module.aim.helper.*"%>
<%@ page import="org.digijava.module.aim.helper.ChartGenerator"%>
<%@ page import="java.io.PrintWriter,java.util.*"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@page import="java.math.BigDecimal"%><script language="JavaScript1.2"
	type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"
></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"
></script>
<DIV id="TipLayer"
	style="visibility: hidden; position: absolute; z-index: 1000; top: -100;"
></DIV>
<digi:instance property="aimEditActivityForm" />
<%
	//Quick fix AMP-6573 please check it
	if (request.getParameter("currentlyEditing") != null) {
%>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/addActivity.js"/>"
></script>
<%
	}
%>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"
></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"
></script>
<script language="JavaScript">

<!--

function exportToPdf (actId) {
	openURLinResizableWindow("/aim/exportActToPDF.do?activityid="+actId, 780, 500);
}

function gotoStep(value) {
	document.aimEditActivityForm.step.value = value;
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%=step%>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function backClicked() {
	document.aimEditActivityForm.step.value = "8";
	<digi:context name="backStep" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%=backStep%>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function disable() {
	document.aimEditActivityForm.submitButton.disabled = true;
	document.aimEditActivityForm.backButton.disabled = true;
	var appstatus = document.getElementById('approvalStatus').value;
	var wTLFlag   = document.getElementById('workingTeamLeadFlag').value;
	var msg='';
	if (appstatus == "started") {
		msg+='<digi:trn key="aim:saveActivity:started">Do you want to submit this activity for approval ?</digi:trn>';
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.getElementById('approvalStatus').value = "approved";
		}
		else if (confirm(msg))
				document.getElementById('approvalStatus').value = "created";
	}
	if (appstatus == "approved") {
		msg+='<digi:trn key="aim:saveActivity:approved">Do you want to approve this activity ?</digi:trn>';
		if (wTLFlag != "yes")
			document.getElementById('approvalStatus').value = "edited";
	}
	else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			if (confirm(msg))
				document.getElementById('approvalStatus').value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
	return true;
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

function viewChanges()

{
	openNewWindow(650,200);

	<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />

	popupPointer.document.location.href = "<%=showLog%>?activityId=${aimEditActivityForm.activityId}";

}

function expandAll() {
   
	$("img[id$='_minus']").show();
	$("img[id$='_plus']").hide();	
	$("div[id$='_dots']").hide();
	$("div[id^='act_']").show('fast');
}

function collapseAll() {

	$("img[id$='_minus']").hide();
	$("img[id$='_plus']").show();	
	$("div[id$='_dots']").show();
	$("div[id^='act_']").hide();
}

	showZoomedMap = function(show) {
		
		var containerObj = $("#zoomMapContainer");
		
		if (show) {
			
			document.body.appendChild(containerObj[0]);
			containerObj.show();
			var containerWidth = containerObj.outerWidth();
			var containerHeight = containerObj.outerHeight();
			
			var wndWidth = window.innerWidth;
			var wndHeight = window.innerHeight;
			var wndScrollX = window.pageXOffset;
			var wndScrollY = window.pageYOffset;
			
			var moveToX = (wndWidth - wndScrollX)/2 - containerWidth/2;
			var moveToY = wndHeight/2 + wndScrollY - containerHeight/2;
			
			containerObj.css("left", moveToX + "px");
			containerObj.css("top", moveToY + "px");
		} else {
			containerObj.hide();
		}	
	}
	var coordinates = new Array();
-->

</script>
<%
	Long actId = (Long) request.getAttribute("actId");
	String url = "/aim/viewIndicatorValues.do?ampActivityId=" + actId
			+ "&tabIndex=6";
	String actPerfChartFileName = null;
	try {
		actPerfChartFileName = ChartGenerator
				.getActivityPerformanceChartFileName(actId, session,
						new PrintWriter(out), 370, 450, url, true,
						request);
	} catch (Exception e) {
		e.printStackTrace();
	}
	String actPerfChartUrl = null;
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath()
				+ "/aim/DisplayChart.img?filename="
				+ actPerfChartFileName;
	}

	String actRiskChartFileName = null;
	try {

		actRiskChartFileName = ChartGenerator
				.getActivityRiskChartFileName(actId, session,
						new PrintWriter(out), 370, 350, url, request);
	} catch (Exception e) {
		e.printStackTrace();
	}

	String actRiskChartUrl = null;

	if (actRiskChartFileName != null) {
		actRiskChartUrl = request.getContextPath()
				+ "/aim/DisplayChart.img?filename="
				+ actRiskChartFileName;
	}
%>
<digi:context name="digiContext" property="context" />
<digi:form action="/saveActivity.do" method="post">
	<html:hidden property="step" />
	<html:hidden property="editAct" />
	<html:hidden property="identification.approvalStatus"
		styleId="approvalStatus"
	/>
	<html:hidden property="workingTeamLeadFlag"
		styleId="workingTeamLeadFlag"
	/>
	<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top"
		align="CENTER"
	>
		<logic:present name="currentMember" scope="session">
			<tr>
				<td width="100%" vAlign="top" align="left"><!--  AMP Admin Logo -->
				<jsp:include page="teamPagesHeader.jsp" flush="true" /> <!-- End of Logo -->
				</td>
			</tr>
		</logic:present>
		<tr>
			<td width="100%" vAlign="top" align="left"></td>
		</tr>
	</table>
	<logic:present name="currentMember" scope="session">
		<!-- BREADCRUMP START -->
		<div class="breadcrump_1" align="center">
		<div class="centering">
		<div class="breadcrump_cont"><c:if
			test="${aimEditActivityForm.pageId == 1}"
		>
			<c:forEach var="step" items="${aimEditActivityForm.steps}">
				<c:set property="translation" var="trans">
					<digi:trn
						key="aim:clickToViewAddActivityStep${step.stepActualNumber}"
					>
                                                                            Click here to goto Add Activity Step ${step.stepActualNumber}
                                                                        </digi:trn>
				</c:set>
				<c:set var="link">
					<c:if test="${step.stepNumber==9}">
                                                                             /editSurveyList.do?edit=true
                                                                             
                                                                        </c:if>
					<c:if test="${step.stepNumber!=9}">
                                                                         
                                                                             /addActivity.do?step=${step.stepNumber}&edit=true
                                                                             
                                                                         </c:if>
				</c:set>
				<c:if test="${index.first}">
					<digi:link href=" ${link}" styleClass="l_sm" title="${trans}">
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
						<span class="breadcrump_sep"><b>»</b></span>
					</digi:link>
				</c:if>
				<c:if test="${!index.first}">
					<digi:link href="${link}" styleClass="l_sm" title="${trans}">
						<digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
					</digi:link>
					<span class="breadcrump_sep"><b>»</b></span>
				</c:if>
			</c:forEach>
			<span class="bread_sel"> <digi:trn key="aim:previewActivity">
																	Preview Activity
																</digi:trn> </span>
		</c:if></div>
		</div>
		</div>
		<br />
		<!-- BREADCRUMP END -->
	</logic:present>
	<!-- MAIN CONTENT PART START -->
	<table width="1000" border="0" cellspacing="0" cellpadding="0"
		align=center
	>
		<tr>
			<td width=1000>
			<div class="step_head_lng">
			<div class="step_head_cont">Preview</div>
			</div>
			</td>
		</tr>
		<tr valign=top>
			<td class="main_side">
			<div class="main_side_cont">
			<div style="float: right;"><logic:present name="currentMember"
				scope="session"
			>
				<table cellSpacing="1" cellPadding="1" vAlign="bottom" border="0">
					<tr>
						<td><c:set var="tran">
							<digi:trn key="aim:previe:expandAll">Expand all</digi:trn>
						</c:set> <input type="button" class="buttonx"
							onclick="javascript:expandAll()" value="${tran}"
						/></td>
						<td><c:set var="tran">
							<digi:trn key="aim:previe:collapseAll">Collapse all</digi:trn>
						</c:set> <input type="button" class="buttonx"
							onclick="javascript:collapseAll()" value="${tran}"
						/></td>
						<td height=16 vAlign=bottom align="right"><input
							type="button" class="buttonx"
							onclick="window.open('/showPrinterFriendlyPage.do?edit=true', '_blank', '');"
							value="<digi:trn key="aim:print">Print</digi:trn>"
						></td>
						<td><c:set var="trn">
							<digi:trn>Export To PDF</digi:trn>
						</c:set> <input type="button" class="buttonx"
							onclick="javascript:exportToPdf(${actId})" value="${trn}"
						/></td>
						<td><c:if test="${aimEditActivityForm.pageId == 1}">
							<input type="button" class="buttonx" onclick="disable()"
								value='<digi:trn key="btn:saveActivity">Save Activity</digi:trn>'
								name="submitButton"
							/>
						</c:if></td>
					</tr>
				</table>
			</logic:present></div>
			<input type="button" class="buttonx"
				onclick="javascript:history.go(-1)"
				value='<< <digi:trn key="btn:back">Back</digi:trn>'
				name="backButton"
			/>
			<div class="wht_blck">
			<div class="wht_blck_cont">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<feature:display name="Identification"
					module="Project ID and Planning"
				>
					<field:display name="AMP ID" feature="Identification">
						<tr>
							<td width="45%" align=right class="prv_left"><digi:trn
								key="aim:ampId"
							>
															 AMP ID</digi:trn></td>
							<td width="55%" class="prv_right"><b><c:out
								value="${aimEditActivityForm.identification.ampId}"
							/></b></td>
						</tr>
					</field:display>
					<field:display name="Contract Number" feature="Planning">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:convenionumcont"
							>Contract Number</digi:trn></td>
							<td class="prv_right"><c:out
								value="${aimEditActivityForm.identification.convenioNumcont}"
							/>&nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Project Title" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:projectTitle"
							>Project title</digi:trn></td>
							<td class="prv_right"><c:out
								value="${aimEditActivityForm.identification.title}"
							/>&nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Status" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn key="aim:status">Status</digi:trn></td>
							<td class="prv_right"><category:getoptionvalue
								categoryValueId="${aimEditActivityForm.identification.statusId}"
							/></td>
						</tr>
						<tr>
							<td class="prv_left" align=right>&nbsp;</td>
							<td class="prv_right"><c:out
								value="${aimEditActivityForm.identification.statusReason}"
							/></td>
						</tr>
					</field:display>
					<field:display feature="Identification" name="Objectives">
						<field:display feature="Identification" name="Objective">
							<tr>
								<td class="prv_left" align=right><digi:trn
									key="aim:objectives"
								>Objectives</digi:trn></td>
								<td class="prv_right"><c:if
									test="${aimEditActivityForm.identification.objectives!=null}"
								>
									<c:set var="objKey"
										value="${aimEditActivityForm.identification.objectives}"
									/>
									<digi:edit key="${objKey}"></digi:edit>
								</c:if> &nbsp;</td>
							</tr>
						</field:display>
						<logic:present name="currentMember" scope="session">
							<tr>
								<td class="prv_left" align=right><field:display
									feature="Identification" name="Objective Comments"
								>
									<digi:trn key="aim:objectiveComments"> 
						Objective Comments
					</digi:trn>
								</field:display></td>
								<td class="prv_right"><logic:iterate
									name="aimEditActivityForm" id="comments"
									property="comments.allComments"
								>
									<field:display feature="Identification"
										name="Objective Assumption"
									>
										<logic:equal name="comments" property="key"
											value="Objective Assumption"
										>
											<logic:iterate name="comments" id="comment" property="value"
												type="org.digijava.module.aim.dbentity.AmpComments"
											>
												<b> <digi:trn key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</b>
												<bean:write name="comment" property="comment" />
												<br />
											</logic:iterate>
										</logic:equal>
									</field:display>
									<field:display feature="Identification"
										name="Objective Verification"
									>
										<logic:equal name="comments" property="key"
											value="Objective Verification"
										>
											<logic:iterate name="comments" id="comment" property="value"
												type="org.digijava.module.aim.dbentity.AmpComments"
											>
												<b> <digi:trn key="aim:objectiveVerification">Objective Verification</digi:trn>:</b>
												<bean:write name="comment" property="comment" />
												<br />
											</logic:iterate>
										</logic:equal>
									</field:display>
									<field:display feature="Identification"
										name="Objective Objectively Verifiable Indicators"
									>
										<logic:equal name="comments" property="key"
											value="Objective Objectively Verifiable Indicators"
										>
											<logic:iterate name="comments" id="comment" property="value"
												type="org.digijava.module.aim.dbentity.AmpComments"
											>
												<b> <digi:trn
													key="aim:objectivelyVerificationIndicators"
												>Objective Objectively Verifiable Indicators</digi:trn>:</b>
												<bean:write name="comment" property="comment" />
												<br />
											</logic:iterate>
										</logic:equal>
									</field:display>
								</logic:iterate> &nbsp;</td>
							</tr>
						</logic:present>
					</field:display>
					<field:display feature="Identification" name="Description">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:description"
							>Description</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.description!=null}"
							>
								<c:set var="descKey"
									value="${aimEditActivityForm.identification.description}"
								/>
								<digi:edit key="${descKey}"></digi:edit>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display feature="Identification" name="Project Comments">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:description"
							>Project Comments</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.projectComments!=null}"
							>
								<c:set var="projcomKey"
									value="${aimEditActivityForm.identification.projectComments}"
								/>
								<digi:edit key="${projcomKey}"></digi:edit>
							</c:if>&nbsp;</td>
						</tr>
					</field:display>
					<field:display name="NPD Clasification" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:npdClasification"
							>NPD Clasification</digi:trn></td>
							<td class="prv_right"><c:out
								value="${aimEditActivityForm.identification.clasiNPD}"
							/>&nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Lessons Learned" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:Lessons Learned"
							>Lessons Learned</digi:trn></td>
							<td class="prv_right"><c:if
								test="${not empty aimEditActivityForm.identification.lessonsLearned}"
							>
								<bean:define id="lessonsLearnedKey">
									<c:out
										value="${aimEditActivityForm.identification.lessonsLearned}"
									/>
								</bean:define>
								<digi:edit key="<%=lessonsLearnedKey%>" />
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<bean:define id="largeTextFeature" value="Identification"
						toScope="request"
					/>
					<field:display name="Project Impact" feature="Identification">
						<logic:present name="aimEditActivityForm"
							property="identification.projectImpact"
						>
							<bean:define id="largeTextLabel" value="Project Impact"
								toScope="request"
							/>
							<bean:define id="largeTextKey" toScope="request">
								<c:out
									value="${aimEditActivityForm.identification.projectImpact}"
								/>
							</bean:define>
							<jsp:include page="largeTextPropertyView.jsp" />
						</logic:present>
					</field:display>
					<logic:present name="aimEditActivityForm"
						property="identification.activitySummary"
					>
						<bean:define id="largeTextLabel" value="Activity Summary"
							toScope="request"
						/>
						<bean:define id="largeTextKey" toScope="request">
							<c:out
								value="${aimEditActivityForm.identification.activitySummary}"
							/>
						</bean:define>
						<jsp:include page="largeTextPropertyView.jsp" />
					</logic:present>
					<logic:present name="aimEditActivityForm"
						property="identification.contractingArrangements"
					>
						<bean:define id="largeTextLabel" value="Contracting Arrangements"
							toScope="request"
						/>
						<bean:define id="largeTextKey" toScope="request">
							<c:out
								value="${aimEditActivityForm.identification.contractingArrangements}"
							/>
						</bean:define>
						<jsp:include page="largeTextPropertyView.jsp" />
					</logic:present>
					<logic:present name="aimEditActivityForm"
						property="identification.condSeq"
					>
						<bean:define id="largeTextLabel"
							value="Conditionality and Sequencing" toScope="request"
						/>
						<bean:define id="largeTextKey" toScope="request">
							<c:out value="${aimEditActivityForm.identification.condSeq}" />
						</bean:define>
						<jsp:include page="largeTextPropertyView.jsp" />
					</logic:present>
					<logic:present name="aimEditActivityForm"
						property="identification.linkedActivities"
					>
						<bean:define id="largeTextLabel" value="Linked Activities"
							toScope="request"
						/>
						<bean:define id="largeTextKey" toScope="request">
							<c:out
								value="${aimEditActivityForm.identification.linkedActivities}"
							/>
						</bean:define>
						<jsp:include page="largeTextPropertyView.jsp" />
					</logic:present>
					<logic:present name="aimEditActivityForm"
						property="identification.conditionality"
					>
						<bean:define id="largeTextLabel" value="Conditionalities"
							toScope="request"
						/>
						<bean:define id="largeTextKey" toScope="request">
							<c:out
								value="${aimEditActivityForm.identification.conditionality}"
							/>
						</bean:define>
						<jsp:include page="largeTextPropertyView.jsp" />
					</logic:present>
					<logic:present name="aimEditActivityForm"
						property="identification.projectManagement"
					>
						<bean:define id="largeTextLabel" value="Project Management"
							toScope="request"
						/>
						<bean:define id="largeTextKey" toScope="request">
							<c:out
								value="${aimEditActivityForm.identification.projectManagement}"
							/>
						</bean:define>
						<jsp:include page="largeTextPropertyView.jsp" />
					</logic:present>
					<field:display feature="Identification" name="Purpose">
						<tr>
							<td class="prv_left" align=right><digi:trn key="aim:purpose">Purpose</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.purpose!=null}"
							>
								<c:set var="objKey"
									value="${aimEditActivityForm.identification.purpose}"
								/>
								<digi:edit key="${objKey}"></digi:edit>
							</c:if>&nbsp;</td>
						</tr>
						<logic:present name="aimEditActivityForm"
							property="coments.allComments"
						>
							<tr>
								<td class="prv_left" align=right><digi:trn
									key="aim:purposeComments"
								>Purpose Comments</digi:trn></td>
								<td class="prv_right"><logic:iterate
									name="aimEditActivityForm" id="comments"
									property="comments.allComments"
								>
									<logic:equal name="comments" property="key"
										value="Purpose Assumption"
									>
										<logic:iterate name="comments" id="comment" property="value"
											type="org.digijava.module.aim.dbentity.AmpComments"
										>
											<b> <digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</b>
											<bean:write name="comment" property="comment" />
											<br />
										</logic:iterate>
									</logic:equal>
									<logic:equal name="comments" property="key"
										value="Purpose Verification"
									>
										<logic:iterate name="comments" id="comment" property="value"
											type="org.digijava.module.aim.dbentity.AmpComments"
										>
											<b> <digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:</b>
											<bean:write name="comment" property="comment" />
											<br />
										</logic:iterate>
									</logic:equal>
									<logic:equal name="comments" property="key"
										value="Purpose Objectively Verifiable Indicators"
									>
										<logic:iterate name="comments" id="comment" property="value"
											type="org.digijava.module.aim.dbentity.AmpComments"
										>
											<b> <digi:trn
												key="aim:purposeObjectivelyVerifiableIndicators"
											>Purpose Objectively Verifiable Indicators</digi:trn>:</b>
											<bean:write name="comment" property="comment" />
											<br />
										</logic:iterate>
									</logic:equal>
								</logic:iterate> &nbsp;</td>
							</tr>
						</logic:present>
					</field:display>
					<field:display feature="Identification" name="Results">
						<tr>
							<td class="prv_left" align=right><digi:trn key="aim:results">Results</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.results!=null}"
							>
								<c:set var="objKey"
									value="${aimEditActivityForm.identification.results}"
								/>
								<digi:edit key="${objKey}"></digi:edit>
							</c:if> &nbsp;</td>
						</tr>
						<logic:present name="aimEditActivityForm"
							property="comments.allComments"
						>
							<tr>
								<td class="prv_left" align=right><digi:trn
									key="aim:resultsComments"
								>Results Comments</digi:trn></td>
								<td class="prv_right"><logic:iterate
									name="aimEditActivityForm" id="comments"
									property="comments.allComments"
								>
									<logic:equal name="comments" property="key"
										value="Results Assumption"
									>
										<logic:iterate name="comments" id="comment" property="value"
											type="org.digijava.module.aim.dbentity.AmpComments"
										>
											<b> <digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:</b>
											<bean:write name="comment" property="comment" />
											<br />
										</logic:iterate>
									</logic:equal>
									<logic:equal name="comments" property="key"
										value="Results Verification"
									>
										<logic:iterate name="comments" id="comment" property="value"
											type="org.digijava.module.aim.dbentity.AmpComments"
										>
											<b> <digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:</b>
											<bean:write name="comment" property="comment" />
											<br />
										</logic:iterate>
									</logic:equal>
									<logic:equal name="comments" property="key"
										value="Results Objectively Verifiable Indicators"
									>
										<logic:iterate name="comments" id="comment" property="value"
											type="org.digijava.module.aim.dbentity.AmpComments"
										>
											<b> <digi:trn
												key="aim:resultsObjectivelyVerifiableIndicators"
											>Results Objectively Verifiable Indicators</digi:trn>:</b>
											<bean:write name="comment" property="comment" />
											<br />
										</logic:iterate>
									</logic:equal>
								</logic:iterate> &nbsp;</td>
							</tr>
						</logic:present>
					</field:display>
					<field:display name="Accession Instrument" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:AccessionInstrument"
							>Accession Instrument</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.accessionInstrument > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.accessionInstrument}"
								/>
							</c:if> &nbsp; &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Project Implementing Unit"
						feature="Identification"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn>Project Implementing Unit</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.projectImplUnitId > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.projectImplUnitId}"
								/>
							</c:if> &nbsp;&nbsp;</td>
						</tr>
					</field:display>
					<field:display name="A.C. Chapter" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:acChapter"
							>A.C. Chapter</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.acChapter > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.acChapter}"
								/>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Cris Number" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:crisNumber"
							>Cris Number</digi:trn></td>
							<td class="prv_right"><c:out
								value="${aimEditActivityForm.identification.crisNumber}"
							/> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Procurement System" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:ProcurementSystem"
							>Procurement System</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.procurementSystem > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.procurementSystem}"
								/>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Reporting System" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:ReportingSystem"
							>Reporting System</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.reportingSystem > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.reportingSystem}"
								/>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Audit System" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:AuditSystem"
							>Audit System</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.auditSystem > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.auditSystem}"
								/>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Institutions" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:Institutions"
							>Institutions</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.institutions > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.institutions}"
								/>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Project Category" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:ProjectCategory"
							>Project Category</digi:trn></td>
							<td class="prv_right"><c:if
								test="${aimEditActivityForm.identification.projectCategory > 0}"
							>
								<category:getoptionvalue
									categoryValueId="${aimEditActivityForm.identification.projectCategory}"
								/>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Government Agreement Number"
						feature="Identification"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:step1:GovernmentAgreementNumTitle"
							>Government Agreement Number</digi:trn></td>
							<td class="prv_right"><c:out
								value="${aimEditActivityForm.identification.govAgreementNumber}"
							/>&nbsp;</td>
						</tr>
					</field:display>
					<feature:display name="Budget" module="Project ID and Planning">
						<tr>
							<td class="prv_left" align=right><img id="budget_plus"
								onclick="toggleGroup('budget')"
								src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
							/> <img id="budget_minus" onclick="toggleGroup('budget')"
								src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
								style="display: none"
							/> <digi:trn key="aim:actBudget">Budget</digi:trn></td>
							<td class="prv_right">
							<div id="budget_dots">...</div>
							<div id="act_budget" style="display: none;"><field:display
								name="On/Off/Treasure Budget" feature="Budget"
							>
								<c:choose>
									<c:when
										test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}"
									>
										<digi:trn>Activity is On Budget</digi:trn>
									</c:when>
									<c:when
										test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOff}"
									>
										<digi:trn>Activity is Off Budget</digi:trn>
									</c:when>
									<c:when
										test="${aimEditActivityForm.identification.budgetCV==0}"
									>
										<digi:trn>Budget Unallocated</digi:trn>
									</c:when>
									<c:otherwise>
										<digi:trn>Activity is On</digi:trn>
										<category:getoptionvalue
											categoryValueId="${aimEditActivityForm.identification.budgetCV}"
										/>
									</c:otherwise>
								</c:choose>
								<c:if
									test="${aimEditActivityForm.identification.budgetCV == aimEditActivityForm.identification.budgetCVOn}"
								>
									<p /><field:display name="Project Code" feature="Budget">
										<digi:trn key="aim:actProjectCode">Project Code</digi:trn>: <bean:write
											name="aimEditActivityForm"
											property="identification.projectCode"
										/>
									</field:display>
								</c:if>
								<c:if
									test="${!empty aimEditActivityForm.identification.chapterForPreview}"
								>
									<digi:trn>Code Chapitre</digi:trn>:
				<bean:write name="aimEditActivityForm"
										property="identification.chapterForPreview.code"
									/> - 
				<bean:write name="aimEditActivityForm"
										property="identification.chapterForPreview.description"
									/>
									<p /><digi:trn>Imputations</digi:trn>:<br />
									<logic:iterate id="imputation" name="aimEditActivityForm"
										property="identification.chapterForPreview.imputations"
									>
										<bean:write name="aimEditActivityForm"
											property="identification.chapterForPreview.year"
										/> -
				<bean:write name="imputation" property="code" /> -
				<bean:write name="imputation" property="description" />
										<br />
									</logic:iterate>
								</c:if>
							</field:display> <field:display name="Budget Classification" feature="Budget">
								<strong><digi:trn>Budget Classification</digi:trn>:</strong>
								<br>
								<c:if
									test="${!empty aimEditActivityForm.identification.selectedbudgedsector}"
								>
									<c:forEach var="selectedsector"
										items="${aimEditActivityForm.identification.budgetsectors}"
									>
										<c:if
											test="${aimEditActivityForm.identification.selectedbudgedsector==selectedsector.idsector}"
										>
											<li style="margin-left: 10px"><c:out
												value="${selectedsector.code}"
											/> - <c:out value="${selectedsector.sectorname}" /></li>
										</c:if>
									</c:forEach>
								</c:if>
								<br>
								<c:if
									test="${!empty aimEditActivityForm.identification.selectedorg}"
								>
									<c:forEach var="selectedorgs"
										items="${aimEditActivityForm.identification.budgetorgs}"
									>
										<c:if
											test="${aimEditActivityForm.identification.selectedorg==selectedorgs.ampOrgId}"
										>
											<li style="margin-left: 10px"><c:out
												value="${selectedorgs.budgetOrgCode}"
											/> - <c:out value="${selectedorgs.name}" /></li>
										</c:if>
									</c:forEach>
								</c:if>
								<br>
								<c:if
									test="${!empty aimEditActivityForm.identification.selecteddepartment}"
								>
									<c:forEach var="selecteddep"
										items="${aimEditActivityForm.identification.budgetdepartments}"
									>
										<c:if
											test="${aimEditActivityForm.identification.selecteddepartment==selecteddep.id}"
										>
											<li style="margin-left: 10px"><c:out
												value="${selecteddep.code}"
											/> - <c:out value="${selecteddep.name}" /></li>
										</c:if>
									</c:forEach>
								</c:if>
								<br>
								<c:if
									test="${!empty aimEditActivityForm.identification.selectedprogram}"
								>
									<c:forEach var="selectedprog"
										items="${aimEditActivityForm.identification.budgetprograms}"
									>
										<c:if
											test="${aimEditActivityForm.identification.selectedprogram==selectedprog.ampThemeId}"
										>
											<li style="margin-left: 10px"><c:out
												value="${selectedprog.themeCode}"
											/> - <c:out value="${selectedprog.name}" /></li>
										</c:if>
									</c:forEach>
								</c:if>
							</field:display></div>
							</td>
						</tr>
					</feature:display>
					<field:display feature="Identification"
						name="Organizations and Project ID"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:orgsAndProjectIds"
							>Organizations and Project IDs											</digi:trn></td>
							<td class="prv_right"><c:if
								test="${!empty aimEditActivityForm.identification.selectedOrganizations}"
							>
								<table cellSpacing=2 cellPadding=2 border="0">
									<c:forEach var="selectedOrganizations"
										items="${aimEditActivityForm.identification.selectedOrganizations}"
									>
										<c:if test="${not empty selectedOrganizations}">
											<tr>
												<td><c:if
													test="${!empty selectedOrganizations.organisation.ampOrgId}"
												>
													<bean:define id="selectedOrgForPopup"
														name="selectedOrganizations"
														type="org.digijava.module.aim.helper.OrgProjectId"
														toScope="request"
													/>
													<jsp:include page="previewOrganizationPopup.jsp" />
												</c:if></td>
											</tr>
										</c:if>
									</c:forEach>
								</table>
							</c:if> &nbsp;</td>
						</tr>
					</field:display>
					<field:display name="Humanitarian Aid" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:humanitarianaid"
							>
					 Humanitarian Aid</digi:trn></td>
							<td class="prv_right"><c:if
								test="${!aimEditActivityForm.identification.humanitarianAid==true}"
							>
								<digi:trn key="aim:no">No</digi:trn>
							</c:if> <c:if
								test="${aimEditActivityForm.identification.humanitarianAid==true}"
							>
								<digi:trn key="aim:yes">Yes</digi:trn>
							</c:if></td>
						</tr>
					</field:display>
					<!--15-->
					<tr>
						<td class="prv_left" align=right><img
							id="group_planning_plus" onclick="toggleGroup('group_planning')"
							src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
						/> <img id="group_planning_minus"
							onclick="toggleGroup('group_planning')"
							src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
							style="display: none"
						/> <digi:trn key="aim:planning">Planning</digi:trn></td>
						<td class="prv_right">
						<div id="group_planning_dots">...</div>
						<div id="act_group_planning" style="display: none;">
						<table width="100%" cellSpacing=2 cellpadding="1" style="font-size:11px;">
							<field:display feature="Planning" name="Line Ministry Rank">
								<tr>
									<td width="32%"><digi:trn key="aim:lineMinRank">
							Line Ministry Rank</digi:trn></td>
									<td width="1">:</td>
									<td align="left"><c:if
										test="${aimEditActivityForm.planning.lineMinRank == -1}"
									>
									</c:if> <c:if test="${aimEditActivityForm.planning.lineMinRank != -1}">
							${aimEditActivityForm.planning.lineMinRank}													</c:if></td>
								</tr>
							</field:display>
							<field:display name="Ministry of Planning Rank"
								feature="Planning"
							>
								<tr>
									<td width="32%"><digi:trn key="aim:planMinRank">
							Ministry of Planning Rank</digi:trn></td>
									<td width="1">:</td>
									<td align="left"><c:if
										test="${aimEditActivityForm.planning.planMinRank == -1}"
									>
									</c:if> <c:if test="${aimEditActivityForm.planning.planMinRank != -1}">
							${aimEditActivityForm.planning.planMinRank}													</c:if></td>
								</tr>
							</field:display>
							<field:display name="Proposed Start Date" feature="Planning">
								<tr>
									<td width="32%"><digi:trn>Proposed Start Date</digi:trn></td>
									<td width="1">:</td>
									<td align="left">
									${aimEditActivityForm.planning.originalStartDate}</td>
								</tr>
							</field:display>
							<field:display name="Actual Start Date" feature="Planning">
								<tr>
									<td width="32%"><digi:trn>Actual Start Date </digi:trn></td>
									<td width="1">:</td>
									<td align="left">
									${aimEditActivityForm.planning.revisedStartDate}</td>
								</tr>
							</field:display>
							<field:display name="Proposed Approval Date" feature="Planning">
								<tr>
									<td width="32%"><digi:trn>Proposed Approval Date</digi:trn>
									</td>
									<td width="1">:</td>
									<td align="left">
									${aimEditActivityForm.planning.originalAppDate}</td>
								</tr>
							</field:display>
							<field:display name="Actual Approval Date" feature="Planning">
								<tr>
									<td width="32%"><digi:trn>Actual Approval Date </digi:trn>
									</td>
									<td width="1">:</td>
									<td align="left">
									${aimEditActivityForm.planning.revisedAppDate}</td>
								</tr>
							</field:display>
							<field:display name="Final Date for Contracting"
								feature="Planning"
							>
								<tr>
									<td width="32%"><digi:trn
										key="aim:ContractingDateofProject1"
									>Final Date for Contracting</digi:trn></td>
									<td width="1">:</td>
									<td align="left"><c:out
										value="${aimEditActivityForm.planning.contractingDate}"
									/></td>
								</tr>
							</field:display>
							<field:display name="Final Date for Disbursements"
								feature="Planning"
							>
								<tr>
									<td width="32%"><digi:trn
										key="aim:DisbursementsDateofProject1"
									>Final Date for Disbursements</digi:trn></td>
									<td width="1">:</td>
									<td align="left"><c:out
										value="${aimEditActivityForm.planning.disbursementsDate}"
									/></td>
								</tr>
							</field:display>
							<field:display name="Proposed Completion Date" feature="Planning">
								<tr>
									<td width="32%"><digi:trn>Proposed Completion Date</digi:trn></td>
									<td width="1">:</td>
									<td align="left">
									${aimEditActivityForm.planning.proposedCompDate}</td>
								</tr>
							</field:display>
							<field:display name="Current Completion Date" feature="Planning">
								<tr>
									<td width="32%"><digi:trn>Current Completion Date</digi:trn>
									</td>
									<td width="1">:</td>
									<td align="left"><c:out
										value="${aimEditActivityForm.planning.currentCompDate}"
									/></td>
								</tr>
							</field:display>
						</table>
						</div>
						</td>
					</tr>
					<!--END 15-->
				</feature:display>
				<module:display name="References" parentModule="PROJECT MANAGEMENT">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:References"
						>References</digi:trn></td>
						<td class="prv_right"><c:forEach
							items="${aimEditActivityForm.documents.referenceDocs}"
							var="refDoc" varStatus="loopstatus"
						>
							<table border="0">
								<tr>
									<td><c:if test="${!empty refDoc.comment}">
													${refDoc.categoryValue}													</c:if></td>
								</tr>
							</table>
						</c:forEach> &nbsp;</td>
					</tr>
				</module:display>
				
				<feature:display name="Location" module="Project ID and Planning">
					<tr>
						<td class="prv_left" align=right style="vertical-align: top;">
							<img id="location_plus" onclick="toggleGroup('location')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/> 
							<img id="location_minus" onclick="toggleGroup('location')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display: none"/> 
							<digi:trn key="aim:location"> Location</digi:trn>
						</td>
						<td class="prv_right">
						<div id="location_dots">...</div>
						<div id="act_location" style="display: none;">
						<field:display name="Implementation Location" feature="Location">
							<c:if test="${!empty aimEditActivityForm.location.selectedLocs}">
								<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
									<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
										<tr>
											<td width="85%">
												<c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
                                                 	[${ancestorLoc}] 
                                                </c:forEach>
                                            </td>
											<td width="15%" align="right">
												<field:display name="Regional Percentage" feature="Location">
													<c:if test="${selectedLocs.showPercent}">
														<c:out value="${selectedLocs.percent}"/>%
													</c:if>
												</field:display>
											</td>
										</tr>
									</c:forEach>
									<module:display name="GIS dashboard">
									<tr>
										<td colspan="2"><br>
										<logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
											<bean:define id="selLocIds">
											<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
													<bean:write name="selectedLocs" property="locId" />|
											</c:forEach>
											</bean:define>
										</logic:notEmpty>
										<logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
											<a href="javascript:showZoomedMap(true)"> <img id="mapThumbnail" border="0" src="/gis/getActivityMap.do?action=paintMap&noCapt=true&width=200&height=200&mapLevel=2&mapCode=TZA&selRegIDs=<bean:write name="selLocIds"/>"></a>
											<div id="zoomMapContainer" style="display: none; border: 1px solid black; position: absolute; left: 0px; top: 0px;" z-index="9999"><a href="javascript:showZoomedMap(false)">
												<img border="0" src="/gis/getActivityMap.do?action=paintMap&width=500&height=500&mapLevel=2&mapCode=TZA&selRegIDs=<bean:write name="selLocIds"/>"></a>
											</div>
										</logic:notEmpty>
										</td>
									</tr>
									</module:display>
									<field:display name="Show Map In Activity Preview" feature="Map Options">
									<tr>
										<td colspan="2">
											<script type="text/javascript">
											<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
												coordinates.push('<c:out value="${selectedLocs.lat}"/>;<c:out value="${selectedLocs.lon}"/>');
											</c:forEach>
											</script>
											<jsp:include page="previewmap.jsp"/>
										</td>
									</tr>
									</field:display>
								</table>
							</c:if>
							</field:display>
							<field:display name="Implementation Level" feature="Location">
								<table>
								<tr>
									<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
										<digi:trn key="aim:level">Implementation Level</digi:trn>
									</td>
									<td bgcolor="#ffffff">
										<c:if test="${aimEditActivityForm.location.levelId>0}">
											<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.levelId}"/>
										</c:if>
									</td>
								</tr>
								</table>
							</field:display>
							<field:display name="Implementation Location" feature="Location">
							<table style="font-size:11px;">
								<tr>
									<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
										<digi:trn key="aim:implementationLocation">Implementation Location</digi:trn>
									</td>
									<td bgcolor="#ffffff">
										<c:if test="${aimEditActivityForm.location.implemLocationLevel>0}">
											<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.implemLocationLevel}"/>
										</c:if>
									</td>
								</tr>
							</table>
							</field:display>
						</div>
					</td>
				</feature:display>
				
				<feature:display name="Program" module="Program">
					<field:display name="National Planning Objectives"
						feature="NPD Programs"
					>
						<tr>
							<td width="30%" align="right" valign="top" nowrap="nowrap" class="prv_left">
							<digi:trn key="national Plan Objective">National Plan Objective</digi:trn>
							<img id="npo_plus" onclick="toggleGroup('npo')"
								src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
							/> <img id="npo_minus" onclick="toggleGroup('npo')"
								src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
								style="display: none"
							/></td>
							<td class="prv_right">
							<div id="npo_dots">...</div>
							<div id="act_npo" style="display: none;"><c:if
								test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}"
							>
								<c:forEach var="nationalPlanObjectivePrograms"
									items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}"
								>
									<c:set var="program"
										value="${nationalPlanObjectivePrograms.program}"
									/>
									<p />${nationalPlanObjectivePrograms.hierarchyNames}
									${nationalPlanObjectivePrograms.programPercentage}%
								</c:forEach>
							</c:if></div>
							</td>
						</tr>
					</field:display>
				</feature:display>
				<feature:display name="Sectors" module="Project ID and Planning">
					<tr>
						<td class="prv_left" align=right><img id="sector_plus"
							onclick="toggleGroup('sector')"
							src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
						/> <img id="sector_minus" onclick="toggleGroup('sector')"
							src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
							style="display: none"
						/> <digi:trn key="aim:sector">	Sector</digi:trn></td>
						<td class="prv_right">
						<div id="sector_dots">...</div>
						<div id="act_sector" style="display: none;"><c:forEach
							var="config"
							items="${aimEditActivityForm.sectors.classificationConfigs}"
							varStatus="ind"
						>
							<bean:define id="emptySector" value="Sector"></bean:define>
							<field:display name="${config.name} Sector" feature="Sectors">
								<c:set var="hasSectors">
																false
															</c:set>
								<c:forEach var="actSect"
									items="${aimEditActivityForm.sectors.activitySectors}"
								>
									<c:if test="${actSect.configId==config.id}">
										<c:set var="hasSectors">
																		true
																	</c:set>
									</c:if>
								</c:forEach>
								<c:if test="${hasSectors}">
									<strong> <digi:trn
										key="aim:addactivitysectors:${config.name} Sector"
									>
										<c:out value="${config.name} Sector" />
									</digi:trn> </strong>
									<br />
								</c:if>
								<c:if
									test="${!empty aimEditActivityForm.sectors.activitySectors}"
								>
									<c:forEach var="sectors"
										items="${aimEditActivityForm.sectors.activitySectors}"
									>
										<c:if test="${sectors.configId==config.id}">
											<field:display name="Sector Scheme Name" feature="Sectors">
												<c:out value="${sectors.sectorScheme}" />
											</field:display>
											<c:if test="${!empty sectors.sectorName}">
												<field:display name="Sector Scheme Name" feature="Sectors">
													<digi:img src="module/aim/images/arrow-th-BABAB9.gif"
														width="16"
													/>
												</field:display>
												<c:out value="${sectors.sectorName}" />
											</c:if>
											<c:if test="${!empty sectors.subsectorLevel1Name}">
												<field:display name="${config.name} Sector Sub-Sector"
													feature="Sectors"
												>
													<digi:img src="module/aim/images/arrow-th-BABAB9.gif"
														width="16"
													/>
													<c:out value="${sectors.subsectorLevel1Name}" />
												</field:display>
											</c:if>
											<c:if test="${!empty sectors.subsectorLevel2Name}">
												<field:display name="${config.name} Sector Sub-Sub-Sector"
													feature="Sectors"
												>
													<digi:img src="module/aim/images/arrow-th-BABAB9.gif"
														width="16"
													/>
													<c:out value="${sectors.subsectorLevel2Name}" />
												</field:display>
											</c:if>
                                                                		&nbsp;&nbsp;
                                                                		<field:display
												name="Percentage" feature="Sectors"
											>
												<c:if test="${sector.sectorPercentage!=''}">
													<c:if test="${sector.sectorPercentage!='0'}">
	                                                                                    (<c:out
															value="${sectors.sectorPercentage}"
														/>)%
	                                                                            </c:if>
												</c:if>
												<br />
											</field:display>
										</c:if>
									</c:forEach>
								</c:if>
							</field:display>
						</c:forEach></div>
						</td>
					</tr>
				</feature:display>
				<c:if
					test="${not empty aimEditActivityForm.components.activityComponentes}"
				>
					<tr>
						<td class="prv_left" align=right><img
							id="component_sector_plus"
							onclick="toggleGroup('component_sector')"
							src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
						/> <img id="component_sector_minus"
							onclick="toggleGroup('component_sector')"
							src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
							style="display: none"
						/> <digi:trn key="aim:preview:component_Sector">Components</digi:trn>
						</td>
						<td class="prv_right">
						<div id="component_sector_dots">...</div>
						<div id="act_component_sector" style="display: none;">
						<table>
							<c:forEach var="compo"
								items="${aimEditActivityForm.components.activityComponentes}"
							>
								<tr>
									<td width="100%">${compo.sectorName}</td>
									<td align="right">${compo.sectorPercentage}%</td>
								</tr>
							</c:forEach>
						</table>
						</div>
						</td>
					</tr>
				</c:if>
				<module:display name="National Planning Dashboard"
					parentModule="NATIONAL PLAN DASHBOARD"
				>
					<feature:display name="NPD Programs"
						module="National Planning Dashboard"
					>
						<field:display name="National Planning Objectives"
							feature="NPD Programs"
						>
							<TR>
								<td class="prv_left" align=right><img id="npd_npo_plus"
									onclick="toggleGroup('npd_npo')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/> <img id="npd_npo_minus" onclick="toggleGroup('npd_npo')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/> <digi:trn key="aim:national Plan Objective">National Plan Objective</digi:trn></TD>
								<td class="prv_right">
								<div id="npd_npo_dots">...</div>
								<div id="act_npd_npo" style="display: none;"><c:forEach
									var="program"
									items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}"
								>
									<c:out value="${program.hierarchyNames}" />&nbsp; <c:out
										value="${program.programPercentage}"
									/>%<br />
								</c:forEach></div>
								</TD>
							</TR>
						</field:display>
						<field:display name="Primary Program" feature="NPD Programs">
							<TR>
								<td class="prv_left" align=right><img
									id="npd_primaryprog_plus"
									onclick="toggleGroup('npd_primaryprog')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/> <img id="npd_primaryprog_minus"
									onclick="toggleGroup('npd_primaryprog')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/> <digi:trn key="aim:primary Programs">Primary Programs</digi:trn></TD>
								<td class="prv_right">
								<div id="npd_primaryprog_dots">...</div>
								<div id="act_npd_primaryprog" style="display: none;"><c:forEach
									var="program"
									items="${aimEditActivityForm.programs.primaryPrograms}"
								>
									<c:out value="${program.hierarchyNames}" />&nbsp; <c:out
										value="${program.programPercentage}"
									/>%<br />
								</c:forEach></div>
								</TD>
							</TR>
						</field:display>
						<field:display name="Secondary Program" feature="NPD Programs">
							<TR>
								<td class="prv_left" align=right><img
									id="npd_secondprog_plus"
									onclick="toggleGroup('npd_secondprog')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/> <img id="npd_secondprog_minus"
									onclick="toggleGroup('npd_secondprog')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/> <digi:trn key="aim:secondary Programs">Secondary Programs</digi:trn></TD>
								<td class="prv_right">
								<div id="npd_secondprog_dots">...</div>
								<div id="act_npd_secondprog" style="display: none;"><c:forEach
									var="program"
									items="${aimEditActivityForm.programs.secondaryPrograms}"
								>
									<c:out value="${program.hierarchyNames}" />&nbsp; <c:out
										value="${program.programPercentage}"
									/>%<br />
								</c:forEach></div>
								</TD>
							</TR>
						</field:display>
					</feature:display>
				</module:display>
				<logic:present name="currentMember" scope="session">
					<module:display name="Funding" parentModule="PROJECT MANAGEMENT">
						<bean:define id="aimEditActivityForm" name="aimEditActivityForm"
							scope="page" toScope="request"
						></bean:define>
						<jsp:include page="previewActivityFunding.jsp" />
					</module:display>
				</logic:present>
				<feature:display name="Regional Funding" module="Funding">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:regionalFunding"
						>
										    Regional Funding</digi:trn></td>
						<td class="prv_right"><c:if
							test="${!empty aimEditActivityForm.funding.regionalFundings}"
						>
							<table width="100%" cellSpacing="1" cellPadding="3"
								bgcolor="#aaaaaa"
							>
								<c:forEach var="regFunds"
									items="${aimEditActivityForm.funding.regionalFundings}"
								>
									<tr>
										<td class="prv_right">
										<table width="100%" cellSpacing="1" cellPadding="1">
											<tr>
												<td class="prv_right"><b> <c:out
													value="${regFunds.regionName}"
												/></b></td>
											</tr>
											<feature:display module="Funding" name="Commitments">
												<c:if test="${!empty regFunds.commitments}">
													<tr>
														<td class="prv_right">
														<table width="100%" cellSpacing="1" cellPadding="0"
															class="box-border-nopadding" border="1"
														>
															<tr>
																<td valign="top" width="100" bgcolor="#f0f0f0"><digi:trn
																	key="aim:commitments"
																>
																				Commitments</digi:trn></td>
																<td class="prv_right">
																<table width="100%" cellSpacing="1" cellPadding="1"
																	bgcolor="#eeeeee"
																>
																	<c:forEach var="fd" items="${regFunds.commitments}">
																		<tr>
																			<td width="50" bgcolor="#f0f0f0"><digi:trn
																				key="aim:${fd.adjustmentTypeNameTrimmed}"
																			>
																				<c:out value="${fd.adjustmentTypeName}" />
																			</digi:trn></td>
																			<td align="right" width="100" bgcolor="#f0f0f0">
																			<!-- <FONT color=blue>*</FONT> --> <c:out
																				value="${fd.transactionAmount}"
																			/></td>
																			<td class="prv_right"><c:out
																				value="${fd.currencyCode}"
																			/></td>
																			<td bgcolor="#f0f0f0" width="70"><c:out
																				value="${fd.transactionDate}"
																			/></td>
																			<td class="prv_right"></td>
																		</tr>
																	</c:forEach>
																</table>
																</td>
															</tr>
														</table>
														</td>
													</tr>
												</c:if>
											</feature:display>
											<feature:display module="Funding" name="Disbursement">
												<c:if test="${!empty regFunds.disbursements}">
													<tr>
														<td class="prv_right">
														<table width="100%" cellSpacing="1" cellPadding="1"
															class="box-border-nopadding"
														>
															<tr>
																<td valign="top" width="100" bgcolor="#f0f0f0"><digi:trn
																	key="aim:disbursements"
																>
																				Disbursements</digi:trn></td>
																<td class="prv_right">
																<table width="100%" cellSpacing="1" cellPadding="1"
																	bgcolor="#eeeeee"
																>
																	<c:forEach var="fd" items="${regFunds.disbursements}">
																		<tr>
																			<td width="50" bgcolor="#f0f0f0"><digi:trn
																				key="aim:${fd.adjustmentTypeNameTrimmed}"
																			>
																				<c:out value="${fd.adjustmentTypeName}" />
																			</digi:trn></td>
																			<td align="right" width="100" bgcolor="#f0f0f0">
																			<!--<FONT color=blue>*</FONT>--> <c:out
																				value="${fd.transactionAmount}"
																			/></td>
																			<td class="prv_right"><c:out
																				value="${fd.currencyCode}"
																			/></td>
																			<td bgcolor="#f0f0f0" width="70"><c:out
																				value="${fd.transactionDate}"
																			/></td>
																			<td class="prv_right"></td>
																		</tr>
																	</c:forEach>
																</table>
																</td>
															</tr>
														</table>
														</td>
													</tr>
												</c:if>
											</feature:display>
											<feature:display module="Funding" name="Expenditures">
												<c:if test="${!empty regFunds.expenditures}">
													<tr>
														<td class="prv_right">
														<table width="100%" cellSpacing="1" cellPadding="1"
															class="box-border-nopadding"
														>
															<tr>
																<td valign="top" width="100" bgcolor="#f0f0f0"><digi:trn
																	key="aim:expenditures"
																>
																				Expenditures</digi:trn></td>
																<td class="prv_right">
																<table width="100%" cellSpacing="1" cellPadding="1"
																	bgcolor="#eeeeee"
																>
																	<c:forEach var="fd" items="${regFunds.expenditures}">
																		<tr>
																			<td width="50" bgcolor="#f0f0f0"><digi:trn
																				key="aim:${fd.adjustmentTypeNameTrimmed}"
																			>
																				<c:out value="${fd.adjustmentTypeName}" />
																			</digi:trn></td>
																			<td align="right" width="100" bgcolor="#f0f0f0">
																			<!--<FONT color=blue>*</FONT>--> <c:out
																				value="${fd.transactionAmount}"
																			/></td>
																			<td class="prv_right"><c:out
																				value="${fd.currencyCode}"
																			/></td>
																			<td bgcolor="#f0f0f0" width="70"><c:out
																				value="${fd.transactionDate}"
																			/></td>
																			<td class="prv_right"></td>
																		</tr>
																	</c:forEach>
																</table>
																</td>
															</tr>
														</table>
														</td>
													</tr>
												</c:if>
											</feature:display>
										</table>
										</td>
									</tr>
								</c:forEach>
								<tr>
									<td class="prv_right"><gs:test
										name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>"
										compareWith="true" onTrueEvalBody="true"
									>
										<FONT color=blue>* <digi:trn
											key="aim:theAmountEnteredAreInThousands"
										>
													The amount entered are in thousands (000)</digi:trn></FONT>
									</gs:test></td>
								</tr>
							</table>
						</c:if> &nbsp;</td>
					</tr>
				</feature:display>
				<logic:equal name="globalSettings" scope="application"
					property="showComponentFundingByYear" value="false"
				>
					<module:display name="Components" parentModule="PROJECT MANAGEMENT">
						<tr>
							<td class="prv_left" align=right><img id="components_plus"
								onclick="toggleGroup('components')"
								src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
							/> <img id="components_minus" onclick="toggleGroup('components')"
								src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
								style="display: none"
							/> <digi:trn key="aim:components">
											Components</digi:trn></td>
							<td class="prv_right">
							<div id="components_dots">...</div>
							<div id="act_components" style="display: none;"><c:if
								test="${!empty aimEditActivityForm.components.selectedComponents}"
							>
								<c:forEach var="comp"
									items="${aimEditActivityForm.components.selectedComponents}"
								>
									<table width="100%" cellSpacing="1" cellPadding="1">
										<tr>
											<td>
											<table width="100%" cellSpacing="2" cellPadding="1"
												class="box-border-nopadding"
											>
												<tr>
													<td><b> <c:out value="${comp.title}" /> </b></td>
												</tr>
												<tr>
													<td><i> <digi:trn key="aim:description">Description</digi:trn>
													:</i> <c:out value="${comp.description}" /></td>
												</tr>
												<tr>
													<td class="prv_right"><b><digi:trn
														key="aim:fundingOfTheComponent"
													>Finance of the component</digi:trn></b></td>
												</tr>
												<c:if test="${!empty comp.commitments}">
													<tr>
														<td class="prv_right">
														<table width="100%" cellSpacing="1" cellPadding="0"
															class="box-border-nopadding"
														>
															<tr>
																<td valign="top" width="100" bgcolor="#f0f0f0"><digi:trn
																	key="aim:commitments"
																>
																				Commitments</digi:trn></td>
																<td class="prv_right">
																<table width="100%" cellSpacing="1" cellPadding="1"
																	bgcolor="#eeeeee"
																>
																	<c:forEach var="fd" items="${comp.commitments}">
																		<tr>
																			<field:display
																				name="Components Actual/Planned Commitments"
																				feature="Activity - Component Step"
																			>
																				<td width="50" bgcolor="#f0f0f0"><digi:trn
																					key="aim:${fd.adjustmentTypeNameTrimmed}"
																				>
																					<c:out value="${fd.adjustmentTypeName}" />
																				</digi:trn></td>
																			</field:display>
																			<field:display name="Components Amount Commitments"
																				feature="Activity - Component Step"
																			>
																				<td align="right" width="100" bgcolor="#f0f0f0">
																				<!--<FONT color="blue">*</FONT>--> <c:out
																					value="${fd.transactionAmount}"
																				/></td>
																			</field:display>
																			<field:display name="Components Currency Commitments"
																				feature="Activity - Component Step"
																			>
																				<td class="prv_right"><c:out
																					value="${fd.currencyCode}"
																				/></td>
																			</field:display>
																			<field:display name="Components Date Commitments"
																				feature="Activity - Component Step"
																			>
																				<td bgcolor="#f0f0f0" width="70"><c:out
																					value="${fd.transactionDate}"
																				/></td>
																			</field:display>
																		</tr>
																	</c:forEach>
																</table>
																</td>
															</tr>
														</table>
														</td>
													</tr>
												</c:if>
												<c:if test="${!empty comp.disbursements}">
													<tr>
														<td class="prv_right">
														<table width="100%" cellSpacing="1" cellPadding="1"
															class="box-border-nopadding"
														>
															<tr>
																<td valign="top" width="100" bgcolor="#f0f0f0"><digi:trn
																	key="aim:disbursements"
																>
																				Disbursements</digi:trn></td>
																<td class="prv_right">
																<table width="100%" cellSpacing="1" cellPadding="1"
																	bgcolor="#eeeeee"
																>
																	<c:forEach var="fd" items="${comp.disbursements}">
																		<tr>
																			<field:display
																				name="Components Actual/Planned Disbursements"
																				feature="Activity - Component Step"
																			>
																				<td width="50" bgcolor="#f0f0f0"><digi:trn
																					key="aim:${fd.adjustmentTypeNameTrimmed}"
																				>
																					<c:out value="${fd.adjustmentTypeName}" />
																				</digi:trn></td>
																			</field:display>
																			<field:display name="Components Amount Disbursements"
																				feature="Activity - Component Step"
																			>
																				<td align="right" width="100" bgcolor="#f0f0f0">
																				<!--<FONT color="blue">*</FONT>--> <c:out
																					value="${fd.transactionAmount}"
																				/></td>
																			</field:display>
																			<field:display
																				name="Components Currency Disbursements"
																				feature="Activity - Component Step"
																			>
																				<td class="prv_right"><c:out
																					value="${fd.currencyCode}"
																				/></td>
																			</field:display>
																			<field:display name="Components Date Disbursements"
																				feature="Activity - Component Step"
																			>
																				<td bgcolor="#f0f0f0" width="70"><c:out
																					value="${fd.transactionDate}"
																				/></td>
																			</field:display>
																		</tr>
																	</c:forEach>
																</table>
																</td>
															</tr>
														</table>
														</td>
													</tr>
												</c:if>
												<c:if test="${!empty comp.expenditures}">
													<tr>
														<td class="prv_right">
														<table width="100%" cellSpacing="1" cellPadding="1"
															class="box-border-nopadding"
														>
															<tr>
																<td valign="top" width="100" bgcolor="#f0f0f0"><digi:trn
																	key="aim:expenditures"
																>
																				Expenditures</digi:trn></td>
																<td class="prv_right">
																<table width="100%" cellSpacing="1" cellPadding="1"
																	bgcolor="#eeeeee"
																>
																	<c:forEach var="fd" items="${comp.expenditures}">
																		<tr bgcolor="#f0f0f0">
																			<field:display
																				name="Components Actual/Planned Expenditures"
																				feature="Activity - Component Step"
																			>
																				<td width="50" bgcolor="#f0f0f0"><digi:trn
																					key="aim:${fd.adjustmentTypeNameTrimmed}"
																				>
																					<c:out value="${fd.adjustmentTypeName}" />
																				</digi:trn></td>
																			</field:display>
																			<field:display name="Components Amount Expenditures"
																				feature="Activity - Component Step"
																			>
																				<td align="right" width="100" bgcolor="#f0f0f0">
																				<!--<FONT color=blue>*</FONT>--> <c:out
																					value="${fd.transactionAmount}"
																				/></td>
																			</field:display>
																			<field:display
																				name="Components Currency Expenditures"
																				feature="Activity - Component Step"
																			>
																				<td class="prv_right"><c:out
																					value="${fd.currencyCode}"
																				/></td>
																			</field:display>
																			<field:display name="Components Date Expenditures"
																				feature="Activity - Component Step"
																			>
																				<td bgcolor="#f0f0f0" width="70"><c:out
																					value="${fd.transactionDate}"
																				/></td>
																			</field:display>
																		</tr>
																	</c:forEach>
																</table>
																</td>
															</tr>
														</table>
														</td>
													</tr>
												</c:if>
												<tr>
													<td class="prv_right"><gs:test
														name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>"
														compareWith="true" onTrueEvalBody="true"
													>
														<FONT color="blue">* <digi:trn
															key="aim:theAmountEnteredAreInThousands"
														>
																		The amount entered are in thousands (000)		  															</digi:trn>
														</FONT>
													</gs:test></td>
												</tr>
												<field:display name="Components Physical Progress"
													feature="Activity - Component Step"
												>
													<tr>
														<td class="prv_right"><b><digi:trn
															key="aim:physicalProgressOfTheComponent"
														>
																Physical progress of the component</digi:trn></b></td>
													</tr>
													<c:if test="${!empty comp.phyProgress}">
														<c:forEach var="phyProg" items="${comp.phyProgress}">
															<tr>
																<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b> <c:out
																	value="${phyProg.title}"
																/></b> - <c:out value="${phyProg.reportingDate}" /></td>
															</tr>
															<tr>
																<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <i> <digi:trn
																	key="aim:description"
																>Description</digi:trn> :</i> <c:out value="${phyProg.description}" />
																</td>
															</tr>
														</c:forEach>
													</c:if>
												</field:display>
											</table>
											</td>
										</tr>
									</table>
								</c:forEach>
							</c:if></div>
							</td>
						</tr>
						<!--end 26-->
					</module:display>
				</logic:equal>
				<logic:equal name="globalSettings" scope="application"
					property="showComponentFundingByYear" value="true"
				>
					<module:display name="Components Resume"
						parentModule="PROJECT MANAGEMENT"
					>
						<tr>
							<td class="prv_left" align=right><img
								id="components_resume_plus"
								onclick="toggleGroup('components_resume')"
								src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
							/> <img id="components_resume_minus"
								onclick="toggleGroup('components_resume')"
								src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
								style="display: none"
							/> <digi:trn key="aim:components">
											Components</digi:trn></td>
							<td class="prv_right">
							<div id="components_resume_dots">...</div>
							<div id="act_components_resume" style="display: none;"><c:if
								test="${!empty aimEditActivityForm.components.selectedComponents}"
							>
								<c:forEach var="comp"
									items="${aimEditActivityForm.components.selectedComponents}"
								>
									<table width="100%" cellSpacing="1" cellPadding="1">
										<tr>
											<td>
											<table width="100%" cellSpacing="2" cellPadding="1"
												class="box-border-nopadding"
											>
												<tr>
													<td><b> <c:out value="${comp.title}" /></b></td>
												</tr>
												<tr>
													<td><i> <digi:trn key="aim:component_code">Component code</digi:trn>
													:</i> <c:out value="${comp.code}" /></td>
												</tr>
												<tr>
													<td><a href="<c:out value="${comp.url}"/>"
														target="_blank"
													><digi:trn key="aim:preview_link_to_component">Link to component</digi:trn>&nbsp;<c:out
														value="${comp.code}"
													/></a></td>
												</tr>
												<tr>
													<td class="prv_right"><b><digi:trn
														key="aim:fundingOfTheComponent"
													>Finance of the component</digi:trn></b></td>
												</tr>
												<tr>
													<td class="prv_right">
													<table width="100%" cellSpacing="1" cellPadding="0"
														class="box-border-nopadding"
													>
														<c:forEach var="financeByYearInfo"
															items="${comp.financeByYearInfo}"
														>
															<tr>
																<td valign="top" width="100" bgcolor="#f0f0f0"><c:out
																	value="${financeByYearInfo.key}"
																/></td>
																<c:set var="financeByYearInfoMap"
																	value="${financeByYearInfo.value}"
																/>
																<td class="prv_right">
																<table width="100%" cellSpacing="1" cellPadding="1"
																	bgcolor="#eeeeee"
																>
																	<fmt:timeZone value="US/Eastern">
																		<tr>
																			<td width="50" bgcolor="#f0f0f0"><digi:trn
																				key="aim:preview_plannedcommitments_sum"
																			>Planned Commitments Sum</digi:trn></td>
																			<td align="right" width="100" bgcolor="#f0f0f0">
																			<aim:formatNumber
																				value="${financeByYearInfoMap['MontoProgramado']}"
																			/> USD</td>
																		</tr>
																		<tr>
																			<td width="50" bgcolor="#f0f0f0"><digi:trn
																				key="aim:preview_actualcommitments_sum"
																			>Actual Commitments Sum</digi:trn></td>
																			<td align="right" width="100" bgcolor="#f0f0f0">
																			<aim:formatNumber
																				value="${financeByYearInfoMap['MontoReprogramado']}"
																			/> USD</td>
																		</tr>
																		<tr>
																			<td width="50" bgcolor="#f0f0f0"><digi:trn
																				key="aim:preview_plannedexpenditures_sum"
																			>Actual Expenditures Sum</digi:trn></td>
																			<td align="right" width="100" bgcolor="#f0f0f0">
																			<aim:formatNumber
																				value="${financeByYearInfoMap['MontoEjecutado']}"
																			/> USD</td>
																		</tr>
																	</fmt:timeZone>
																</table>
																</td>
															</tr>
															<tr>
																<td>&nbsp;</td>
																<td>&nbsp;</td>
															</tr>
														</c:forEach>
													</table>
													</td>
												</tr>
											</table>
											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
									</table>
								</c:forEach>
							</c:if></div>
							</td>
						</tr>
						<!--end 26-->
					</module:display>
				</logic:equal>
				<module:display name="Issues" parentModule="PROJECT MANAGEMENT">
					<feature:display name="Issues" module="Issues">
						<field:display name="Issues" feature="Issues">
							<tr>
								<td class="prv_left" align=right><img id="issues_plus"
									onclick="toggleGroup('issues')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/> <img id="issues_minus" onclick="toggleGroup('issues')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/> <digi:trn key="aim:issues">
											Issues</digi:trn></td>
								<td class="prv_right">
								<div id="issues_dots">...</div>
								<div id="act_issues" style="display: none;"><c:if
									test="${!empty aimEditActivityForm.issues.issues}"
								>
									<table width="100%" cellSpacing="2" cellPadding="2" border="0">
										<c:forEach var="issue"
											items="${aimEditActivityForm.issues.issues}"
										>
											<tr>
												<td valign="top">
												<li class="level1"><b> <digi:trn
													key="aim:issuename:${issue.id}"
												>
													<c:out value="${issue.name}" />
												</digi:trn> <field:display feature="Issues" name="Issue Date">
													<c:out value="${issue.issueDate}" />
												</field:display> </b></li>
												</td>
											</tr>
											<field:display name="Measures Taken" feature="Issues">
												<c:if test="${!empty issue.measures}">
													<c:forEach var="measure" items="${issue.measures}">
														<tr>
															<td>
															<li class="level2"><i> <digi:trn
																key="aim:${measure.nameTrimmed}"
															>
																<c:out value="${measure.name}" />
															</digi:trn> </i></li>
															</td>
														</tr>
														<field:display name="Actors" feature="Issues">
															<c:if test="${!empty measure.actors}">
																<c:forEach var="actor" items="${measure.actors}">
																	<tr>
																		<td>
																		<li class="level3"><digi:trn
																			key="aim:${actor.nameTrimmed}"
																		>
																			<c:out value="${actor.name}" />
																		</digi:trn></li>
																		</td>
																	</tr>
																</c:forEach>
															</c:if>
														</field:display>
													</c:forEach>
												</c:if>
											</field:display>
										</c:forEach>
									</table>
								</c:if></div>
								</td>
							</tr>
						</field:display>
					</feature:display>
				</module:display>
				<module:display name="Document" parentModule="PROJECT MANAGEMENT">
					<feature:display name="Related Documents" module="Document">
						<tr>
							<td class="prv_left" align=right><img
								id="related_documents_plus"
								onclick="toggleGroup('related_documents')"
								src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
							/> <img id="related_documents_minus"
								onclick="toggleGroup('related_documents')"
								src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
								style="display: none"
							/> <digi:trn key="aim:relatedDocuments">
											Related Documents</digi:trn></td>
							<td class="prv_right">
							<div id="related_documents_dots">...</div>
							<div id="act_related_documents" style="display: none;"><c:if
								test="${ (!empty aimEditActivityForm.documents.documentList) || (!empty aimEditActivityForm.documents.crDocuments)}"
							>
								<table width="100%" cellSpacing="0" cellPadding="0">
									<logic:iterate name="aimEditActivityForm"
										property="documents.documents" id="docs"
										type="org.digijava.module.aim.helper.Documents"
									>
										<c:if test="${docs.isFile == true}">
											<tr>
												<td>
												<table width="100%" class="box-border-nopadding">
													<tr bgcolor="#f0f0f0">
														<td vAlign="center" align="left">&nbsp;<b><c:out
															value="${docs.title}"
														/></b> - &nbsp;&nbsp;&nbsp;<i><c:out
															value="${docs.fileName}"
														/></i> <logic:notEqual name="docs" property="docDescription"
															value=" "
														>
															<br />&nbsp;
																	<b><digi:trn key="aim:description">Description</digi:trn>:</b>
																	&nbsp;<bean:write name="docs" property="docDescription" />
														</logic:notEqual> <logic:notEmpty name="docs" property="date">
															<br />&nbsp;
																	<b><digi:trn key="aim:date">Date</digi:trn>:</b>
																	&nbsp;<c:out value="${docs.date}" />
														</logic:notEmpty> <logic:notEmpty name="docs" property="docType">
															<br />&nbsp;
																	<b><digi:trn key="aim:documentType">Document Type</digi:trn>:</b>&nbsp;
																	<bean:write name="docs" property="docType" />
														</logic:notEmpty></td>
													</tr>
												</table>
												</td>
											</tr>
										</c:if>
									</logic:iterate>
									<logic:notEmpty name="aimEditActivityForm"
										property="documents.crDocuments"
									>
										<tr>
											<td><logic:iterate name="aimEditActivityForm"
												property="documents.crDocuments" id="crDoc"
											>
												<table width="100%" class="box-border-nopadding">
													<tr bgcolor="#f0f0f0">
														<td vAlign="center" align="left">&nbsp;<b><c:out
															value="${crDoc.title}"
														/></b> - &nbsp;&nbsp;&nbsp;<i><c:out value="${crDoc.name}" /></i>
														<c:set var="translation">
															<digi:trn
																key="contentrepository:documentManagerDownloadHint"
															>Click here to download document</digi:trn>
														</c:set> <a
															style="cursor: pointer; text-decoration: underline; color: blue;"
															id="<c:out value="${crDoc.uuid}"/>"
															onclick="window.location='/contentrepository/downloadFile.do?uuid=<c:out value="${crDoc.uuid}"/>'"
															title="${translation}"
														><img
															src="/repository/contentrepository/view/images/check_out.gif"
															border="0"
														></a> <logic:notEmpty name="crDoc" property="description">
															<br />&nbsp;
																			<b><digi:trn key="aim:description">Description</digi:trn>:</b>&nbsp;
																			<bean:write name="crDoc" property="description" />
														</logic:notEmpty> <logic:notEmpty name="crDoc" property="calendar">
															<br />&nbsp;
																			<b><digi:trn key="aim:date">Date</digi:trn>:</b>
																			&nbsp;<c:out value="${crDoc.calendar}" />
														</logic:notEmpty></td>
													</tr>
												</table>
											</logic:iterate></td>
										</tr>
									</logic:notEmpty>
								</table>
							</c:if> <c:if test="${!empty aimEditActivityForm.documents.linksList}">
								<table width="100%" cellSpacing="0" cellPadding="0">
									<c:forEach var="docList"
										items="${aimEditActivityForm.documents.linksList}"
									>
										<bean:define id="links" name="docList" property="relLink" />
										<tr>
											<td>
											<table width="100%" class="box-border-nopadding">
												<tr>
													<td width="2"><digi:img
														src="module/aim/images/web-page.gif"
													/></td>
													<td align="left" vAlign="center">&nbsp; <b><c:out
														value="${links.title}"
													/></b> - &nbsp;&nbsp;&nbsp;<i><a
														href="<c:out value="${links.url}"/>"
													> <c:out value="${links.url}" /></a></i> <br>
													&nbsp; <b><digi:trn key="aim:description">Description</digi:trn>:</b>
													&nbsp;<c:out value="${links.description}" /></td>
												</tr>
											</table>
											</td>
										</tr>
									</c:forEach>
								</table>
							</c:if></div>
							</td>
						</tr>
					</feature:display>
				</module:display>
				<module:display name="Organizations"
					parentModule="PROJECT MANAGEMENT"
				>
					<tr>
						<td class="prv_left" align=right><img id="orgz_plus"
							onclick="toggleGroup('orgz')"
							src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
						/> <img id="orgz_minus" onclick="toggleGroup('orgz')"
							src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
							style="display: none"
						/> <digi:trn key="aim:relatedOrganizations">Related Organizations</digi:trn>
						</td>
						<td class="prv_right"><feature:display module="Organizations"
							name="Responsible Organization"
						></feature:display> <feature:display module="Organizations"
							name="Responsible Organization"
						>
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.respOrganisations"
							>
								<img id="implementing_agency_plus"
									onclick="toggleGroup('responsible_organisation')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/>
								<img id="implementing_agency_minus"
									onclick="toggleGroup('responsible_organisation')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/>
							</logic:notEmpty>
							<b><digi:trn key="aim:responsibleorganisation">Responsible Organization</digi:trn></b>
							<br />
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.respOrganisations"
							>
								<div id="responsible_organisation_dots">...</div>
								<div id="act_responsible_organisation" style="display: none;">
								<table width="100%" cellSpacing="1" cellPadding="5"
									class="box-border-nopadding"
								>
									<tr>
										<td><logic:iterate name="aimEditActivityForm"
											property="agencies.respOrganisations" id="respOrganisations"
											type="org.digijava.module.aim.dbentity.AmpOrganisation"
										>
											<ul>
												<li><bean:write name="respOrganisations"
													property="name"
												/> <c:set var="tempOrgId" scope="page">${respOrganisations.ampOrgId}</c:set>
												<field:display
													name="Responsible Organization Additional Info"
													feature="Responsible Organization"
												>
													<logic:notEmpty name="aimEditActivityForm"
														property="agencies.respOrgToInfo(${tempOrgId})"
													>
																	(  <c:out
															value="${aimEditActivityForm.agencies.respOrgToInfo[tempOrgId]}"
														/> ) 
																	</logic:notEmpty>
												</field:display></li>
											</ul>
										</logic:iterate></td>
									</tr>
								</table>
								</div>
							</logic:notEmpty>
						</feature:display>
						<div id="orgz_dots">...</div>
						<div id="act_orgz" style="display: none;"><feature:display
							name="Executing Agency" module="Organizations"
						>
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.executingAgencies"
							>
								<img id="executing_agency_plus"
									onclick="toggleGroup('executing_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/>
								<img id="executing_agency_minus"
									onclick="toggleGroup('executing_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/>
							</logic:notEmpty>
							<b><digi:trn key="aim:executingAgency">Executing Agency</digi:trn></b>
							<br />
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.executingAgencies"
							>
								<div id="executing_agency_dots">...</div>
								<div id="act_executing_agency" style="display: none;">
								<table width="100%" cellSpacing="1" cellPadding="5"
									class="box-border-nopadding"
								>
									<tr>
										<td><logic:iterate name="aimEditActivityForm"
											property="agencies.executingAgencies" id="execAgencies"
											type="org.digijava.module.aim.dbentity.AmpOrganisation"
										>
											<ul>
												<li><bean:write name="execAgencies" property="name" />
												<c:set var="tempOrgId">${execAgencies.ampOrgId}</c:set> <field:display
													name="Executing Agency Additional Info"
													feature="Executing Agency"
												>
													<logic:notEmpty name="aimEditActivityForm"
														property="agencies.executingOrgToInfo(${tempOrgId})"
													>
																		(  <c:out
															value="${aimEditActivityForm.agencies.executingOrgToInfo[tempOrgId]}"
														/> )
																		</logic:notEmpty>
												</field:display></li>
											</ul>
										</logic:iterate></td>
									</tr>
								</table>
								</div>
							</logic:notEmpty>
							<br />
						</feature:display> <feature:display name="Implementing Agency"
							module="Organizations"
						>
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.impAgencies"
							>
								<img id="implementing_agency_plus"
									onclick="toggleGroup('implementing_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/>
								<img id="implementing_agency_minus"
									onclick="toggleGroup('implementing_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/>
							</logic:notEmpty>
							<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b>
							<br />
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.impAgencies"
							>
								<div id="implementing_agency_dots">...</div>
								<div id="act_implementing_agency" style="display: none;">
								<table width="100%" cellSpacing="1" cellPadding="5"
									class="box-border-nopadding"
								>
									<tr>
										<td><logic:iterate name="aimEditActivityForm"
											property="agencies.impAgencies" id="impAgencies"
											type="org.digijava.module.aim.dbentity.AmpOrganisation"
										>
											<ul>
												<li><bean:write name="impAgencies" property="name" />
												<c:set var="tempOrgId">${impAgencies.ampOrgId}</c:set> <field:display
													name="Implementing Agency Additional Info"
													feature="Implementing Agency"
												>
													<logic:notEmpty name="aimEditActivityForm"
														property="agencies.impOrgToInfo(${tempOrgId})"
													>
																		(  <c:out
															value="${aimEditActivityForm.agencies.impOrgToInfo[tempOrgId]}"
														/> )
																		</logic:notEmpty>
												</field:display></li>
											</ul>
										</logic:iterate></td>
									</tr>
								</table>
								</div>
							</logic:notEmpty>
						</feature:display> <feature:display name="Beneficiary Agency" module="Organizations">
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.benAgencies"
							>
								<img id="benAgencies_agency_plus"
									onclick="toggleGroup('benAgencies_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/>
								<img id="benAgencies_agency_minus"
									onclick="toggleGroup('benAgencies_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/>
							</logic:notEmpty>
							<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b>
							<br />
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.benAgencies"
							>
								<div id="benAgencies_dots">...</div>
								<div id="act_benAgencies_agency" style="display: none;">
								<table width="100%" cellSpacing="1" cellPadding="5"
									class="box-border-nopadding"
								>
									<tr>
										<td><logic:iterate name="aimEditActivityForm"
											property="agencies.benAgencies" id="benAgency"
											type="org.digijava.module.aim.dbentity.AmpOrganisation"
										>
											<ul>
												<li><bean:write name="benAgency" property="name" /> <c:set
													var="tempOrgId"
												>${benAgency.ampOrgId}</c:set> <field:display
													name="Beneficiary Agency  Additional Info"
													feature="Beneficiary Agency"
												>
													<logic:notEmpty name="aimEditActivityForm"
														property="agencies.benOrgToInfo(${tempOrgId})"
													>
																			(  <c:out
															value="${aimEditActivityForm.agencies.benOrgToInfo[tempOrgId]}"
														/> ) 
																			</logic:notEmpty>
												</field:display></li>
											</ul>
										</logic:iterate></td>
									</tr>
								</table>
								</div>
							</logic:notEmpty>
							<br />
						</feature:display> <feature:display name="Contracting Agency" module="Organizations">
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.conAgencies"
							>
								<img id="contracting_agency_plus"
									onclick="toggleGroup('contracting_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/>
								<img id="contracting_agency_minus"
									onclick="toggleGroup('contracting_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/>
							</logic:notEmpty>
							<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b>
							<br />
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.conAgencies"
							>
								<div id="contracting_agency_dots">...</div>
								<div id="act_contracting_agency" style="display: none;">
								<table width="100%" cellSpacing="1" cellPadding="5"
									class="box-border-nopadding"
								>
									<tr>
										<td><logic:iterate name="aimEditActivityForm"
											property="agencies.conAgencies" id="conAgencies"
											type="org.digijava.module.aim.dbentity.AmpOrganisation"
										>
											<ul>
												<li><bean:write name="conAgencies" property="name" />
												<c:set var="tempOrgId">${conAgencies.ampOrgId}</c:set> <field:display
													name="Contracting Agency Additional Info"
													feature="Contracting Agency"
												>
													<logic:notEmpty name="aimEditActivityForm"
														property="agencies.conOrgToInfo(${tempOrgId})"
													>
																		(  <c:out
															value="${aimEditActivityForm.agencies.conOrgToInfo[tempOrgId]}"
														/> )
																		</logic:notEmpty>
												</field:display></li>
											</ul>
										</logic:iterate></td>
									</tr>
								</table>
								</div>
							</logic:notEmpty>
							<br />
						</feature:display> <logic:notEmpty name="aimEditActivityForm"
							property="agencies.sectGroups"
						>
							<img id="sectGroups_agency_plus"
								onclick="toggleGroup('sectGroups_agency')"
								src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
							/>
							<img id="sectGroups_agency_minus"
								onclick="toggleGroup('sectGroups_agency')"
								src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
								style="display: none"
							/>
						</logic:notEmpty> <feature:display name="Sector Group" module="Organizations">
							<field:display name="Sector Group" feature="Sector Group">
								<b><digi:trn key="aim:sectorGroup">Sector Group</digi:trn></b>
								<br />
								<logic:notEmpty name="aimEditActivityForm"
									property="agencies.sectGroups"
								>
									<div id="sectGroups_dots">...</div>
									<div id="act_sectGroups_agency" style="display: none;">
									<table width="100%" cellSpacing="1" cellPadding="5"
										class="box-border-nopadding"
									>
										<tr>
											<td><logic:iterate name="aimEditActivityForm"
												property="agencies.sectGroups" id="sectGroup"
												type="org.digijava.module.aim.dbentity.AmpOrganisation"
											>
												<ul>
													<li><bean:write name="sectGroup" property="name" /> <c:set
														var="tempOrgId"
													>${sectGroup.ampOrgId}</c:set> <field:display
														name="Sector Group Additional Info" feature="Sector Group"
													>
														<logic:notEmpty name="aimEditActivityForm"
															property="agencies.sectOrgToInfo(${tempOrgId})"
														>
																		(  <c:out
																value="${aimEditActivityForm.agencies.sectOrgToInfo[tempOrgId]}"
															/> ) 
																		</logic:notEmpty>
													</field:display></li>
												</ul>
											</logic:iterate></td>
										</tr>
									</table>
									</div>
								</logic:notEmpty>
								<br />
							</field:display>
						</feature:display> <feature:display name="Regional Group" module="Organizations">
							<logic:notEmpty name="aimEditActivityForm"
								property="agencies.sectGroups"
							>
								<img id="regGroups_agency_plus"
									onclick="toggleGroup('regGroups_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
								/>
								<img id="regGroups_agency_minus"
									onclick="toggleGroup('regGroups_agency')"
									src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
									style="display: none"
								/>
							</logic:notEmpty>
							<field:display name="Regional Group" feature="Regional Group">
								<b><digi:trn key="aim:regionalGroup">Regional Group</digi:trn></b>
								<br />
								<logic:notEmpty name="aimEditActivityForm"
									property="agencies.regGroups"
								>
									<div id="regGroups_dots">...</div>
									<div id="act_regGroups_agency" style="display: none;">
									<table width="100%" cellSpacing="1" cellPadding="5"
										class="box-border-nopadding"
									>
										<tr>
											<td><logic:iterate name="aimEditActivityForm"
												property="agencies.regGroups" id="regGroup"
												type="org.digijava.module.aim.dbentity.AmpOrganisation"
											>
												<ul>
													<li><bean:write name="regGroup" property="name" /> <c:set
														var="tempOrgId"
													>${regGroup.ampOrgId}</c:set> <field:display
														name="Regional Group Additional Info"
														feature="Regional Group"
													>
														<logic:notEmpty
															property="agencies.regOrgToInfo(${tempOrgId})"
															name="aimEditActivityForm"
														>
																		(  <c:out
																value="${aimEditActivityForm.agencies.regOrgToInfo[tempOrgId]}"
															/> )
																	</logic:notEmpty>
													</field:display></li>
												</ul>
											</logic:iterate></td>
										</tr>
									</table>
									</div>
								</logic:notEmpty>
								<br />
							</field:display>
						</feature:display></div>
						</td>
					</tr>
				</module:display>
				<module:display name="Contact Information"
					parentModule="PROJECT MANAGEMENT"
				>
					<feature:display name="Donor Contact Information"
						module="Contact Information"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn>Donor funding contact information</digi:trn>
							</td>
							<td class="prv_right"><c:if
								test="${not empty aimEditActivityForm.contactInformation.donorContacts}"
							>
								<c:forEach var="donorContact"
									items="${aimEditActivityForm.contactInformation.donorContacts}"
								>
									<div><c:out value="${donorContact.contact.name}" /> <c:out
										value="${donorContact.contact.lastname}"
									/> - <c:forEach var="property"
										items="${donorContact.contact.properties}"
									>
										<c:if test="${property.name=='contact email'}">
											<c:out value="${property.value}" /> ;
																	</c:if>
									</c:forEach></div>
								</c:forEach>
							</c:if> &nbsp;</td>
						</tr>
					</feature:display>
					<feature:display name="Government Contact Information"
						module="Contact Information"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn>MOFED contact information</digi:trn>
							</td>
							<td class="prv_right"><c:if
								test="${not empty aimEditActivityForm.contactInformation.mofedContacts}"
							>
								<c:forEach var="mofedContact"
									items="${aimEditActivityForm.contactInformation.mofedContacts}"
								>
									<div><c:out value="${mofedContact.contact.name}" /> <c:out
										value="${mofedContact.contact.lastname}"
									/> - <c:forEach var="property"
										items="${mofedContact.contact.properties}"
									>
										<c:if test="${property.name=='contact email'}">
											<c:out value="${property.value}" /> ;
																	</c:if>
									</c:forEach></div>
								</c:forEach>
							</c:if> &nbsp;</td>
						</tr>
					</feature:display>
					<feature:display name="Project Coordinator Contact Information"
						module="Contact Information"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn>Project Coordinator Contact Information</digi:trn>
							</td>
							<td class="prv_right"><c:if
								test="${not empty aimEditActivityForm.contactInformation.projCoordinatorContacts}"
							>
								<c:forEach var="projCoordinatorContact"
									items="${aimEditActivityForm.contactInformation.projCoordinatorContacts}"
								>
									<div><c:out
										value="${projCoordinatorContact.contact.name}"
									/> <c:out value="${projCoordinatorContact.contact.lastname}" />
									- <c:forEach var="property"
										items="${projCoordinatorContact.contact.properties}"
									>
										<c:if test="${property.name=='contact email'}">
											<c:out value="${property.value}" /> ;
																	</c:if>
									</c:forEach></div>
								</c:forEach>
							</c:if> &nbsp;</td>
						</tr>
					</feature:display>
					<feature:display name="Sector Ministry Contact Information"
						module="Contact Information"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn>Sector Ministry Contact Information</digi:trn>
							</td>
							<td class="prv_right"><c:if
								test="${not empty aimEditActivityForm.contactInformation.sectorMinistryContacts}"
							>
								<c:forEach var="sectorMinistryContact"
									items="${aimEditActivityForm.contactInformation.sectorMinistryContacts}"
								>
									<div><c:out value="${sectorMinistryContact.contact.name}" />
									<c:out value="${sectorMinistryContact.contact.lastname}" /> -
									<c:forEach var="property"
										items="${sectorMinistryContact.contact.properties}"
									>
										<c:if test="${property.name=='contact email'}">
											<c:out value="${property.value}" />;
																	</c:if>
									</c:forEach></div>
								</c:forEach>
							</c:if> &nbsp;</td>
						</tr>
					</feature:display>
					<feature:display
						name="Implementing/Executing Agency Contact Information"
						module="Contact Information"
					>
						<tr>
							<td width="30%" align="right" valign="top" class="prv_left">
							<digi:trn>Implementing/Executing Agency Contact Information</digi:trn>
							</td>
							<td class="prv_right"><c:if
								test="${not empty aimEditActivityForm.contactInformation.implExecutingAgencyContacts}"
							>
								<c:forEach var="implExecAgencyContact"
									items="${aimEditActivityForm.contactInformation.implExecutingAgencyContacts}"
								>
									<div><c:out value="${implExecAgencyContact.contact.name}" />
									<c:out value="${implExecAgencyContact.contact.lastname}" /> -
									<c:forEach var="property"
										items="${implExecAgencyContact.contact.properties}"
									>
										<c:if test="${property.name=='contact email'}">
											<c:out value="${property.value}" /> ;
																	</c:if>
									</c:forEach></div>
								</c:forEach>
							</c:if> &nbsp;</td>
						</tr>
					</feature:display>
				</module:display>
				<field:display name="Activity Performance"
					feature="Activity Dashboard"
				>
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:meActivityPerformance"
						>
										    Activity - Performance</digi:trn></td>
						<td class="prv_right">
						<%
							if (actPerfChartUrl != null) {
						%> <img src="<%=actPerfChartUrl%>" width="370" height="450"
							border="0" usemap="#<%= actPerfChartFileName %>"
						><br>
						<br>
						<%
							} else {
						%> <br>
						<span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
						<digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>
						</span><br>
						<br>
						<%
							}
						%> &nbsp;</td>
					</tr>
				</field:display>
				<field:display name="Project Risk" feature="Activity Dashboard">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:meActivityRisk"
						>
										    Activity - Risk</digi:trn> <br />
						<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn></td>
						<td class="prv_right">
						<%
							if (actRiskChartUrl != null) {
						%> <img src="<%=actRiskChartUrl%>" align="bottom" width="370"
							height="350" border="0" usemap="#<%= actRiskChartFileName %>"
						> <br>
						<br>
						<%
							} else {
						%> <br>
						<span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
						<digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>
						</span><br>
						<br>
						<%
							}
						%>
						</td>
					</tr>
				</field:display>
				<feature:display name="Proposed Project Cost" module="Funding">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:proposedPrjectCost"
						> Proposed Project Cost</digi:trn></td>
						<td class="prv_right"><c:if
							test="${aimEditActivityForm.funding.proProjCost!=null}"
						>
							<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa"
								width="100%"
							>
								<tr bgcolor="#f0f0f0">
									<td><digi:trn key="aim:cost">Cost</digi:trn></td>
									<td bgcolor="#f0f0f0" align="left"><c:if
										test="${aimEditActivityForm.funding.proProjCost.funAmount!=null}"
									>
										<!--<FONT color=blue>*</FONT>-->
																 	 ${aimEditActivityForm.funding.proProjCost.funAmount}                                                          </c:if>&nbsp;
									<c:if
										test="${aimEditActivityForm.funding.proProjCost.currencyCode!=null}"
									> ${aimEditActivityForm.funding.proProjCost.currencyCode} </c:if></td>
								</tr>
								<tr bgcolor="#f0f0f0">
									<td><digi:trn key="aim:proposedCompletionDate">Proposed Completion Date</digi:trn></td>
									<td bgcolor="#f0f0f0" align="left" width="150"><c:if
										test="${aimEditActivityForm.funding.proProjCost.funDate!=null}"
									>
                                                             ${aimEditActivityForm.funding.proProjCost.funDate}                                                          </c:if>
									</td>
								</tr>
							</table>
						</c:if> &nbsp;</td>
					</tr>
				</feature:display>
				<!-- Costing -->
				<feature:display name="Costing" module="Activity Costing">
					<tr>
						<td class="prv_left" align=right><digi:trn key="aim:costing">
										    Costing</digi:trn></td>
						<td class="prv_right">&nbsp;&nbsp;&nbsp;
						<table width="100%" style="font-size:11px;">
							<tr>
								<td><bean:define id="mode" value="preview"
									type="java.lang.String" toScope="request"
								/> <jsp:include page="viewCostsSummary.jsp" flush="" /></td>
							</tr>
						</table>
						</td>
					</tr>
				</feature:display>
				<!-- End Costing -->
				<!-- IPA Contracting -->
				<feature:display name="Contracting" module="Contracting">
					<tr>
						<td class="prv_left" align=right><img id="contract_plus"
							onclick="toggleGroup('contract')"
							src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
						/> <img id="contract_minus" onclick="toggleGroup('contract')"
							src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
							style="display: none"
						/> <digi:trn key="aim:IPAContracting">IPA Contracting</digi:trn></td>
						<td class="prv_right">&nbsp;&nbsp;&nbsp;
						<div id="contract_dots">...</div>
						<div id="act_contract" style="display: none;">
						<table width="100%">
							<tr>
								<td><!-- contents --> <logic:notEmpty
									name="aimEditActivityForm" property="contracts"
								>
									<table width="100%" cellSpacing="1" cellPadding="3"
										vAlign="top" align="left" bgcolor="#006699"
									>
										<c:forEach items="${aimEditActivityForm.contracts.contracts}"
											var="contract" varStatus="idx"
										>
											<tr>
												<td bgColor=#f4f4f2 align="center" vAlign="top">
												<table width="100%" border="0" cellspacing="2"
													cellpadding="2" align="left" class="box-border-nopadding"
												>
													<field:display name="Contract Name" feature="Contracting">
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:name"
															>Contract name:</digi:trn></b></td>
															<td>${contract.contractName}</td>
														</tr>
													</field:display>
													<field:display name="Contract Description"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:description"
															>Description:</digi:trn></b></td>
															<td>${contract.description}</td>
														</tr>
													</field:display>
													<field:display name="Contracting Activity Category"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:actCat"
															>Activity Category:</digi:trn></b></td>
															<td><c:if
																test="${not empty contract.activityCategory}"
															>${contract.activityCategory.value}</c:if></td>
														</tr>
													</field:display>
													<field:display name="Contract type" feature="Contracting">
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:type"
															>Type</digi:trn>:</b></td>
															<td><c:if test="${not empty contract.type}">${contract.type.value}</c:if>
															</td>
														</tr>
													</field:display>
													<field:display name="Contracting Start of Tendering"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:startOfTendering"
															>Start of Tendering:</digi:trn></b></td>
															<td>${contract.formattedStartOfTendering}</td>
														</tr>
													</field:display>
													<field:display name="Signature of Contract"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:signatureOfContract"
															>Signature of Contract:</digi:trn></b></td>
															<td>${contract.formattedSignatureOfContract}</td>
														</tr>
													</field:display>
													<field:display name="Contract Organization"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:contractOrg"
															>Contract Organization:</digi:trn></b></td>
															<td><c:if test="${not empty contract.organization}">
                                                                                                     ${contract.organization.name}
                                                                                                </c:if>
															</td>
														</tr>
													</field:display>
													<field:display name="Contracting Contractor Name"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:contractOrg"
															>Contract Organization</digi:trn>:</b></td>
															<td>${contract.contractingOrganizationText}</td>
														</tr>
													</field:display>
													<field:display name="Contract Completion"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:contractCompletion"
															>Contract Completion:</digi:trn></b></td>
															<td>${contract.formattedContractCompletion}</td>
														</tr>
													</field:display>
													<field:display name="Contracting Status"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:status"
															>Status:</digi:trn></b></td>
															<td><c:if test="${not empty contract.status}">
                                                                                 
                                                                                                    ${contract.status.value}
                                                                                                </c:if>
															</td>
														</tr>
													</field:display>
													<field:display name="Total Amount" feature="Contracting">
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:totalAmount"
															>Total Amount</digi:trn>:</b></td>
															<td>${contract.totalAmount}
															${contract.totalAmountCurrency}</td>
														</tr>
													</field:display>
													<field:display name="Total EC Contribution"
														feature="Contracting"
													>
														<tr>
															<td align="left" colspan="2"><b><digi:trn
																key="aim:IPA:popup:totalECContribution"
															>Total EC Contribution:</digi:trn></b></td>
														</tr>
													</field:display>
													<field:display name="Contracting IB" feature="Contracting">
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:IB"
															>IB</digi:trn>:</b></td>
															<td>${contract.totalECContribIBAmount}
															${contract.totalAmountCurrency}</td>
														</tr>
													</field:display>
													<field:display name="Contracting INV" feature="Contracting">
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:INV"
															>INV:</digi:trn></b></td>
															<td>${contract.totalECContribINVAmount}
															${contract.totalAmountCurrency}</td>
														</tr>
													</field:display>
													<field:display
														name="Contracting Total National Contribution"
														feature="Contracting"
													>
														<tr>
															<td align="left" colspan="2"><b><digi:trn
																key="aim:IPA:popup:totalNationalContribution"
															>Total National Contribution:</digi:trn></b></td>
														</tr>
													</field:display>
													<field:display name="Contracting Central Amount"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:Central"
															>Central</digi:trn>:</b></td>
															<td>${contract.totalNationalContribCentralAmount}
															${contract.totalAmountCurrency}</td>
														</tr>
													</field:display>
													<field:display name="Contracting Regional Amount"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:Regional"
															>Regional</digi:trn>:</b></td>
															<td>${contract.totalNationalContribRegionalAmount}
															${contract.totalAmountCurrency}</td>
														</tr>
													</field:display>
													<field:display name="Contracting IFIs"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:IFIs"
															>IFIs</digi:trn>:</b></td>
															<td>${contract.totalNationalContribIFIAmount}
															${contract.totalAmountCurrency}</td>
														</tr>
													</field:display>
													<field:display name="Total Private Contribution"
														feature="Contracting"
													>
														<tr>
															<td align="left" colspan="2"><b><digi:trn
																key="aim:IPA:popup:totalPrivateContribution"
															>Total Private Contribution:</digi:trn></b></td>
														</tr>
													</field:display>
													<field:display name="Contracting IB" feature="Contracting">
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:IB"
															>IB:</digi:trn></b></td>
															<td>${contract.totalPrivateContribAmount}
															${contract.totalAmountCurrency}</td>
														</tr>
													</field:display>
													<field:display name="Total Disbursements of Contract"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:totalDisbursements"
															>Total Disbursements</digi:trn>:</b></td>
															<td>${contract.totalDisbursements} &nbsp; <logic:empty
																name="contract" property="dibusrsementsGlobalCurrency"
															>
                                                            										&nbsp; ${aimEditActivityForm.currCode}
                                                            									</logic:empty> <logic:notEmpty
																name="contract" property="dibusrsementsGlobalCurrency"
															>
                                                            										&nbsp; ${contract.dibusrsementsGlobalCurrency}
                                                            									</logic:notEmpty></td>
														</tr>
													</field:display>
													<field:display
														name="Total Funding Disbursements of Contract"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:totalFundingDisbursements"
															>Total Funding Disbursements</digi:trn>:</b></td>
															<td>${contract.fundingTotalDisbursements} &nbsp; <logic:empty
																name="contract" property="dibusrsementsGlobalCurrency"
															>
											              										&nbsp; ${contract.totalAmountCurrency}
											              									</logic:empty> <logic:notEmpty name="contract"
																property="dibusrsementsGlobalCurrency"
															>
											              										&nbsp; ${contract.dibusrsementsGlobalCurrency}
											              									</logic:notEmpty></td>
														</tr>
													</field:display>
													<field:display name="Contract Execution Rate"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:contractExecutionRate"
															>Contract Execution Rate</digi:trn>:</b></td>
															<td>&nbsp; ${contract.executionRate}</td>
														</tr>
													</field:display>
													<field:display name="Contract Funding Execution Rate"
														feature="Contracting"
													>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:popup:contractExecutionRate"
															>Contract Execution Rate</digi:trn>:</b></td>
															<td>&nbsp; ${contract.fundingExecutionRate}</td>
														</tr>
													</field:display>
													<field:display name="Disbursements" feature="Contracting">
														<tr>
															<td colspan="2"><b><digi:trn
																key="aim:IPA:popup:disbursements"
															>Disbursements:</digi:trn></b></td>
														</tr>
													</field:display>
													<tr>
														<td>&nbsp;</td>
														<td><logic:notEmpty name="contract"
															property="disbursements"
														>
															<table>
																<c:forEach items="${contract.disbursements}"
																	var="disbursement"
																>
																	<tr>
																		<td align="left" valign="top"><c:if
																			test="${disbursement.adjustmentType==0}"
																		>
																			<digi:trn key="aim:actual">Actual</digi:trn>
																		</c:if> <c:if test="${disbursement.adjustmentType==1}">
																			<digi:trn key="aim:planned">Planned</digi:trn>
																		</c:if></td>
																		<td align="left" valign="top">
																		${disbursement.amount}</td>
																		<td align="left" valign="top">
																		${disbursement.currency.currencyName}</td>
																		<td align="left" valign="top">
																		${disbursement.disbDate}</td>
																	</tr>
																</c:forEach>
															</table>
														</logic:notEmpty></td>
													</tr>
													<field:display name="Contracting Funding Disbursements"
														feature="Contracting"
													>
														<tr>
															<td colspan="2"><b><digi:trn
																key="aim:IPA:popup:fundingDisbursements"
															>Funding Disbursements:</digi:trn></b></td>
														</tr>
													</field:display>
													<tr>
														<td>&nbsp;</td>
														<td><logic:notEmpty name="aimEditActivityForm"
															property="funding.fundingDetails"
														>
															<table width="100%">
																<tr>
																	<td><field:display
																		name="Adjustment Type Disbursement"
																		feature="Disbursement"
																	>
																		<digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn>
																	</field:display></td>
																	<td><field:display name="Amount Disbursement"
																		feature="Disbursement"
																	>
																		<digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn>
																	</field:display></td>
																	<td><field:display name="Currency Disbursement"
																		feature="Disbursement"
																	>
																		<digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn>
																	</field:display></td>
																	<td><field:display name="Date Disbursement"
																		feature="Disbursement"
																	>
																		<digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn>
																	</field:display></td>
																</tr>
																<c:forEach
																	items="${aimEditActivityForm.funding.fundingDetails}"
																	var="fundingDetail"
																>
																	<logic:equal name="contract" property="contractName"
																		value="${fundingDetail.contract.contractName}"
																	>
																		<c:if test="${fundingDetail.transactionType == 1}">
																			<tr>
																				<td align="center" valign="top"><c:if
																					test="${fundingDetail.adjustmentType==0}"
																				>
																					<digi:trn key="aim:actual">Actual</digi:trn>
																				</c:if> <c:if test="${fundingDetail.adjustmentType==1}">
																					<digi:trn key="aim:planned">Planned</digi:trn>
																				</c:if></td>
																				<td align="center" valign="top">
																				${fundingDetail.transactionAmount}</td>
																				<td align="center" valign="top">
																				${fundingDetail.currencyCode}</td>
																				<td align="center" valign="top">
																				${fundingDetail.transactionDate}</td>
																			</tr>
																		</c:if>
																	</logic:equal>
																</c:forEach>
															</table>
														</logic:notEmpty></td>
													</tr>
													<field:display name="Contracting Amendments"
														feature="Contracting"
													>
														<bean:define id="ct" name="contract"
															type="org.digijava.module.aim.dbentity.IPAContract"
														/>
														<tr>
															<td align="left"><b><digi:trn
																key="aim:IPA:newPopup:donorContractFundinAmount"
															>Part du contrat financé par le bailleur</digi:trn>:</b></td>
															<td>&nbsp; <%=BigDecimal
														.valueOf(
																ct
																		.getDonorContractFundinAmount())
														.toPlainString()%>
															&nbsp;&nbsp;&nbsp;&nbsp;${contract.donorContractFundingCurrency.currencyName}
															</td>
														</tr>
														<tr>
															<td align="left"><b><digi:trn>Montant total du contrat part du bailleur</digi:trn>:</b>
															</td>
															<td>&nbsp; <%=BigDecimal
														.valueOf(
																ct
																		.getTotAmountDonorContractFunding())
														.toPlainString()%>
															&nbsp;&nbsp;&nbsp;&nbsp;${contract.totalAmountCurrencyDonor.currencyName}
															</td>
														</tr>
														<tr>
															<td align="left"><b><digi:trn>Montant total du contrat comprise la part de l'Etat</digi:trn>:</b>
															</td>
															<td>&nbsp; <%=BigDecimal
														.valueOf(
																ct
																		.getTotAmountCountryContractFunding())
														.toPlainString()%>
															&nbsp;&nbsp;&nbsp;&nbsp;${contract.totalAmountCurrencyCountry.currencyName}
															</td>
														</tr>
														<tr>
															<td colspan="2"><b><digi:trn>Amendments :</digi:trn></b>
															</td>
														</tr>
														<tr>
															<td>&nbsp;</td>
															<td><logic:notEmpty name="contract"
																property="amendments"
															>
																<table width="100%">
																	<tr>
																		<th><digi:trn>Amount</digi:trn></th>
																		<th><digi:trn>Currency</digi:trn></th>
																		<th><digi:trn>Date</digi:trn></th>
																		<th><digi:trn>Reference</digi:trn></th>
																	</tr>
																	<c:forEach items="${contract.amendments}"
																		var="amendment"
																	>
																		<bean:define id="am" name="amendment"
																			type="org.digijava.module.aim.dbentity.IPAContractAmendment"
																		/>
																		<tr>
																			<td align="center" valign="top">
																			${amendment.amoutStr}</td>
																			<td align="center" valign="top">
																			${amendment.currency.currencyName}</td>
																			<td align="center" valign="top">
																			${amendment.amendDate}</td>
																			<td align="center" valign="top">
																			${amendment.reference}</td>
																		</tr>
																	</c:forEach>
																</table>
															</logic:notEmpty></td>
														</tr>
													</field:display>
												</table>
												</td>
											</tr>
										</c:forEach>
									</table>
								</logic:notEmpty></td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
				</feature:display>
				<!-- end IPA Contracting -->
				<field:display name="Activity Created By" feature="Identification">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:activityCreatedBy"
						>
										    Activity created by</digi:trn></td>
						<td class="prv_right">
						<c:out
							value="${aimEditActivityForm.identification.actAthFirstName}"
						/> <c:out
							value="${aimEditActivityForm.identification.actAthLastName}"
						/> - <c:out
							value="${aimEditActivityForm.identification.actAthEmail}"
						/></td>
					</tr>
				</field:display>
				<field:display name="Workspace of Creator" feature="Identification">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:workspaceOfCreator"
						>Worskpace of creator</digi:trn></td>
						<td class="prv_right"><c:out
							value="${aimEditActivityForm.identification.createdBy.ampTeam.name}"
						/> - <c:out
							value="${aimEditActivityForm.identification.createdBy.ampTeam.accessType}"
						/></td>
					</tr>
				</field:display>
				<field:display name="Computation" feature="Identification">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:computation"
						>Computation</digi:trn></td>
						<td class="prv_right"><c:if
							test="${aimEditActivityForm.identification.createdBy.ampTeam.computation == 'true'}"
						>
							<digi:trn key="aim:yes">Yes</digi:trn>
						</c:if> <c:if
							test="${aimEditActivityForm.identification.createdBy.ampTeam.computation == 'false'}"
						>
							<digi:trn key="aim:no">No</digi:trn>
						</c:if> &nbsp;</td>
					</tr>
				</field:display>
				<field:display feature="Identification" name="Data Source">
					<tr>
						<td class="prv_left" align=right><digi:trn
							key="aim:dataSource"
						>
										    Data Source</digi:trn></td>
						<td class="prv_right"><c:out
							value="${aimEditActivityForm.identification.actAthAgencySource}"
						/> &nbsp;</td>
					</tr>
				</field:display>
				<field:display name="Activity Updated On" feature="Identification">
					<logic:notEmpty name="aimEditActivityForm"
						property="identification.updatedDate"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:activityUpdatedOn"
							>
											 Activity updated on</digi:trn></td>
							<td class="prv_right"><b><c:out
								value="${aimEditActivityForm.identification.updatedDate}"
							/></b><logic:present name="isUserLogged" scope="session">
								<html:button styleClass="buttonx_sm" property="submitButton"
									onclick="viewChanges()"
								>
									<digi:trn key="btn:last5changestoactivity">Last 5 changes to Activity</digi:trn>
								</html:button>
							</logic:present></td>
						</tr>
					</logic:notEmpty>
				</field:display>
				<field:display name="Activity Updated By" feature="Identification">
					<logic:notEmpty name="aimEditActivityForm"
						property="identification.updatedBy"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:activityUpdatedBy"
							>
											 Activity updated by</digi:trn></td>
							<td class="prv_right"><b><c:out
								value="${aimEditActivityForm.identification.updatedBy.user.firstNames}"
							/> <c:out
								value="${aimEditActivityForm.identification.updatedBy.user.lastName}"
							/> - <c:out
								value="${aimEditActivityForm.identification.updatedBy.user.email}"
							/></b></td>
						</tr>
					</logic:notEmpty>
				</field:display>
				<field:display name="Activity Created On" feature="Identification">
					<logic:notEmpty name="aimEditActivityForm"
						property="identification.createdDate"
					>
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:activityCreatedOn"
							>
											 Activity created on</digi:trn></td>
							<td class="prv_right"><b><c:out
								value="${aimEditActivityForm.identification.createdDate}"
							/></b></td>
						</tr>
					</logic:notEmpty>
				</field:display>
				<logic:notEmpty name="aimEditActivityForm"
					property="identification.team"
				>
					<field:display name="Data Team Leader" feature="Identification">
						<tr>
							<td class="prv_left" align=right><digi:trn
								key="aim:activityTeamLeader"
							>
											 Data Team Leader</digi:trn></td>
							<td class="prv_right"><b><c:out
								value="${aimEditActivityForm.identification.team.teamLead.user.firstNames}"
							/> <c:out
								value="${aimEditActivityForm.identification.team.teamLead.user.lastName}"
							/> - <c:out
								value="${aimEditActivityForm.identification.team.teamLead.user.email}"
							/></b></td>
						</tr>
					</field:display>
				</logic:notEmpty>
				<logic:notEmpty name="aimEditActivityForm" property="customFields">
					<logic:iterate name="aimEditActivityForm" property="customFields"
						id="customField" indexId="index"
					>
						<field:display name="${customField.FM_field}"
							feature="Step${customField.step.step}"
						>
							<tr>
								<td class="prv_left" align=right><digi:trn
									key="aim:customfield:${customField.name}"
								>${customField.name}</digi:trn></td>
								<td class="prv_right"><c:choose>
									<c:when test="<%=customField instanceof ComboBoxCustomField%>">
										<c:set var="idx" value="${customField.value}" />
										<c:out value="${customField.options[idx]}" />
									</c:when>
									<c:when test="<%=customField instanceof CategoryCustomField%>">
										<c:if test="${customField.value > 0}">
											<category:getoptionvalue
												categoryValueId="${customField.value}"
											/>
										</c:if>
									</c:when>
									<c:when test="<%=customField instanceof DateCustomField%>">
										<c:out value="${customField.strDate}" />
									</c:when>
									<c:when
										test="<%=customField instanceof RadioOptionCustomField%>"
									>
										<logic:iterate name="customField" property="options"
											id="option"
										>
											<logic:equal name="option" property="key"
												value="${customField.value}"
											>
												<c:out value="${option.value}" />
											</logic:equal>
										</logic:iterate>
									</c:when>
									<c:when test="<%=customField instanceof CheckCustomField%>">
										<c:if test="${customField.value == true}">
											<c:out value="${customField.labelTrue}" />
										</c:if>
										<c:if test="${customField.value == false}">
											<c:out value="${customField.labelFalse}" />
										</c:if>
									</c:when>
									<c:otherwise>
										<c:out value="${customField.value}" />
									</c:otherwise>
								</c:choose> &nbsp;</td>
							</tr>
						</field:display>
					</logic:iterate>
				</logic:notEmpty>
			</table>
			</div>
			</div>
			<div style="float: right;"><logic:present name="currentMember"
				scope="session"
			>
				<table cellSpacing="1" cellPadding="1" vAlign="bottom" border="0">
					<tr>
						<td><c:set var="tran">
							<digi:trn key="aim:previe:expandAll">Expand all</digi:trn>
						</c:set> <input type="button" class="buttonx"
							onclick="javascript:expandAll()" value="${tran}"
						/></td>
						<td><c:set var="tran">
							<digi:trn key="aim:previe:collapseAll">Collapse all</digi:trn>
						</c:set> <input type="button" class="buttonx"
							onclick="javascript:collapseAll()" value="${tran}"
						/></td>
						<td height=16 vAlign=bottom align="right"><input
							type="button" class="buttonx"
							onclick="window.open('/showPrinterFriendlyPage.do?edit=true', '_blank', '');"
							value="<digi:trn key="aim:print">Print</digi:trn>"
						></td>
						<td><c:set var="trn">
							<digi:trn>Export To PDF</digi:trn>
						</c:set> <input type="button" class="buttonx"
							onclick="javascript:exportToPdf(${actId})" value="${trn}"
						/></td>
						<td><c:if test="${aimEditActivityForm.pageId == 1}">
							<input type="button" class="buttonx" onclick="disable()"
								value='<digi:trn key="btn:saveActivity">Save Activity</digi:trn>'
								name="submitButton"
							/>
						</c:if></td>
					</tr>
				</table>
			</logic:present></div>
			<div align="center"><logic:notEmpty name="previousActivity"
				scope="session"
			>
				<digi:link href="/viewActivityPreview.do~pageId=2"
					paramId="activityId" paramName="previousActivity"
					paramScope="session"
				>
					<font size="2"><digi:trn key="aim:previous">Previous</digi:trn></font>
				</digi:link>
				<logic:notEmpty name="nextActivity" scope="session">
					<font size="2"> &nbsp;-&nbsp; </font>
				</logic:notEmpty>
			</logic:notEmpty> <logic:notEmpty name="nextActivity" scope="session">
				<digi:link href="/viewActivityPreview.do~pageId=2"
					paramId="activityId" paramName="nextActivity" paramScope="session"
				>
					<font size="2"><digi:trn key="aim:next">Next</digi:trn></font>
				</digi:link>
			</logic:notEmpty></div>
			<input type="button" class="buttonx"
				onclick="javascript:history.go(-1)"
				value='<< <digi:trn key="btn:back">Back</digi:trn>'
				name="backButton"
			/></div>
			</td>
		</tr>
	</table>
	<!-- MAIN CONTENT PART END -->
</digi:form>
<script language="javascript">
$(document).ready(function(){
	expandAll();
	 });</script>