<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter, java.util.*" %>

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

function gotoStep(value) {
	document.aimEditActivityForm.step.value = value;
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function backClicked() {
	document.aimEditActivityForm.step.value = "8";
	<digi:context name="backStep" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= backStep %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function disable() {
	document.aimEditActivityForm.submitButton.disabled = true;
	document.aimEditActivityForm.backButton.disabled = true;
	var appstatus = document.aimEditActivityForm.approvalStatus.value;
	var wTLFlag   = document.aimEditActivityForm.workingTeamLeadFlag.value;
	var msg='';
	if (appstatus == "started") {
		msg+='<digi:trn key="aim:saveActivity:started">Do you want to submit this activity for approval ?</digi:trn>';
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
		else if (confirm(msg))
				document.aimEditActivityForm.approvalStatus.value = "created";
	}
	if (appstatus == "approved") {
		msg+='<digi:trn key="aim:saveActivity:approved">Do you want to approve this activity ?</digi:trn>';
		if (wTLFlag != "yes")
			document.aimEditActivityForm.approvalStatus.value = "edited";
	}
	else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			if (confirm(msg))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
	return true;
}
-->

</script>

<%
	Long actId = (Long) request.getAttribute("actId");

	String url = "/aim/viewIndicatorValues.do?ampActivityId="+actId+"&tabIndex=6";

	String actPerfChartFileName = ChartGenerator.getActivityPerformanceChartFileName(
						 actId,session,new PrintWriter(out),370,450,url,true,request);

	String actPerfChartUrl = null;
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}


	String actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(
						 actId,session,new PrintWriter(out),370,350,url,request);

	String actRiskChartUrl = null;

	if (actRiskChartFileName != null)  {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}
%>

<digi:context name="digiContext" property="context" />
<digi:form action="/saveActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="approvalStatus" />
<html:hidden property="workingTeamLeadFlag" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<logic:present name="currentMember" scope="session">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
</logic:present>
<tr><td width="100%" vAlign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="780" vAlign="top" align="left" border=0>
	<tr>
		<td class=r-dotted-lg width="10" align="left" vAlign="top">&nbsp;</td>
		<td class=r-dotted-lg align=left vAlign=top>
			<table width="100%" cellSpacing="3" cellPadding="1" vAlign="top" align="left" border=0>
					<tr><td>
					<logic:present name="currentMember" scope="session">
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 2}">
									<c:set var="trnViewMyDesktop">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${trnViewMyDesktop}" >
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;

									<jsp:useBean id="urlParam" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParam}" property="ampActivityId">
										<c:out value="${aimEditActivityForm.activityId}"/>
									</c:set>
									<c:set target="${urlParam}" property="tabIndex" value="0"/>

									<c:set var="trnViewChannelOverview">
										<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>
									</c:set>

									<digi:link href="/viewChannelOverview.do" styleClass="comment" name="urlParam" title="${trnViewChannelOverview}" >
										<digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:previewActivity">
									Preview Activity
									</digi:trn>
								</c:if>

								<c:if test="${aimEditActivityForm.pageId == 1}">

								<c:set var="trnAddActivity1">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>
								</c:set>

								<c:if test="${aimEditActivityForm.donorFlag == true}">
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
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" title="${trnAddActivity1}" >
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
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep2">
										Step 2
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity2">
										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" title="${trnAddActivity2}" >
										<digi:trn key="aim:addActivityStep2">
											Step 2
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:set var="trnAddActivity3">
									<digi:trn key="aim:clickToViewAddActivityStep3">Click here to goto Add Activity Step 3</digi:trn>
								</c:set>
								<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" title="${trnAddActivity3}" >
									<digi:trn key="aim:addActivityStep3">
										Step 3
									</digi:trn>
								</digi:link>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep4">
										Step 4
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity4">
										<digi:trn key="aim:clickToViewAddActivityStep4">Click here to goto Add Activity Step 4</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment" title="${trnAddActivity4}" >
										<digi:trn key="aim:addActivityStep4">
											Step 4
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep5">
										Step 5
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity">
										<digi:trn key="aim:clickToViewAddActivityStep5">Click here to goto Add Activity Step 5</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=5&edit=true" styleClass="comment" title="${trnAddActivity}" >
										<digi:trn key="aim:addActivityStep5">
											Step 5
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep6">
										Step 6
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity6">
										<digi:trn key="aim:clickToViewAddActivityStep6">Click here to goto Add Activity Step 6</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=6&edit=true" styleClass="comment" title="${trnAddActivity6}" >
										<digi:trn key="aim:addActivityStep6">
											Step 6
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep7">
										Step 7
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity7">
										<digi:trn key="aim:clickToViewAddActivityStep7">Click here to goto Add Activity Step 7</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=7&edit=true" styleClass="comment" title="${trnAddActivity7}" >
										<digi:trn key="aim:addActivityStep7">
											Step 7
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep8">
										Step 8
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity8">
										<digi:trn key="aim:clickToViewAddActivityStep8">Click here to goto Add Activity Step 8</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=8&edit=true" styleClass="comment" title="${trnAddActivity8}" >
										<digi:trn key="aim:addActivityStep8">
											Step 8
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<digi:trn key="aim:addActivityStep9">
										Step 9
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<c:set var="trnAddActivity9">
										<digi:trn key="aim:clickToViewAddActivityStep9">Click here to goto Add Activity Step 9</digi:trn>
									</c:set>
									<digi:link href="/addActivity.do?step=10&edit=true" styleClass="comment" title="${trnAddActivity9}" >
										<digi:trn key="aim:addActivityStep9">
											Step 9
										</digi:trn>
									</digi:link>
								</c:if>

								&nbsp;&gt;&nbsp;

								<digi:trn key="aim:previewActivity">
									Preview Activity
								</digi:trn>
								</c:if>
								</span>
							</td>
						</tr>
					</table>
					</logic:present>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="0" cellPadding="0" vAlign="bottom">
						<tr>
							<td width="50%" align="left">
								<c:if test="${aimEditActivityForm.pageId == 1}">
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="bottom">
									<tr>
										<td height=16 vAlign="bottom" width="100%"><span class=subtitle-blue>
											<c:if test="${aimEditActivityForm.editAct == false}">
												<digi:trn key="aim:addNewActivity">
													Add New Activity
												</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.editAct == true}">
												<digi:trn key="aim:editActivity">
													Edit Activity
												</digi:trn>
											</c:if></span>
										</td>
									</tr>
								</table>
								</c:if>
							</td>
							<logic:present name="currentMember" scope="session">
							<td width="50%" align="right">
								<table cellSpacing="1" cellPadding="1" vAlign="bottom" border=0>
									<tr>
										<td height=16 vAlign=bottom align="right">
											<digi:img src="module/aim/images/print_icon.gif"/>
										</td>
										<td height=16 vAlign=center align="left" width="50">
											<digi:link href="/showPrinterFriendlyPage.do?edit=true" target="_blank">
												<digi:trn key="aim:print">
													Print
												</digi:trn>
											</digi:link>&nbsp;
										</td>
									</tr>
								</table>
							</td>
							</logic:present>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="100%" vAlign="top">
						<table width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left">
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:previewActivity">
													Preview Activity
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
							<tr><td align="center" vAlign="top" bgcolor="#ffffff">
								<table width="100%" cellSpacing=1 cellpadding=3 bgcolor="#dddddd">
								<module:display name="Project ID and Planning">
									<feature:display name="Identification" module="Project ID and Planning">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:ampId">
											AMP ID</digi:trn>
										</td>
										<td class="v-name" bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.ampId}"/>
										</td>
									</tr>
									<field:display feature="Identification" name="Title">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:projectTitle">
											Project title</digi:trn>
										</td>
										<td class="v-name"  bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.title}"/>
										</td>
									</tr>
								</field:display>
								<field:display feature="Identification" name="Objectives">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:objectives">
											Objectives</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.objectives!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.objectives}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>
										</td>
									</tr>

									<logic:present name="currentMember" scope="session">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:objectiveComments">
											Objective Comments</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Objective Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Objective Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:objectiveVerification">Objective Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>

										</td>
									</tr>
									</logic:present>
									</field:display>
									<field:display feature="Identification" name="Description">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:description">
											Description</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.description!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.description}" />
											<digi:edit key="${descKey}"></digi:edit>
                                            </c:if>
										</td>
									</tr>
									</field:display>

									<field:display feature="Identification" name="Purpose">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:purpose">
											Purpose</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.purpose!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.purpose}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>
										</td>
									</tr>
									<logic:present name="aimEditActivityForm" property="allComments">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:purposeComments">
											Purpose Comments</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Purpose Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>

										</td>
									</tr>
									</logic:present>
									</field:display>

									<field:display feature="Identification" name="Results">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:results">
											Results</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.results!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.results}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>
										</td>
									</tr>
									<logic:present name="aimEditActivityForm" property="allComments">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:resultsComments">
											Results Comments</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Results Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>

										</td>
									</tr>
									</logic:present>
									</field:display>
									<field:display name="Activity Budget" feature="Identification">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#fffff0">
											<digi:trn key="aim:actBudget">Budget</digi:trn>
										</td>
										<td bgcolor="#ffffff">

										<logic:equal name="aimEditActivityForm" property="budget" value="true">
										<digi:trn key="aim:actBudgeton">
												Activity is On Budget
										</digi:trn>
										</logic:equal>

										<logic:equal name="aimEditActivityForm" property="budget" value="false">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget
										</digi:trn>
										</logic:equal>

										<logic:equal name="aimEditActivityForm" property="budget" value="">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget
										</digi:trn>
										</logic:equal>

										</td>
									</tr>
									</field:display>

									<field:display feature="Identification" name="Organizations and Project ID">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:orgsAndProjectIds">
											Organizations and Project IDs
											</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedOrganizations}">
												<table cellSpacing=2 cellPadding=2 border=0>
													<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.selectedOrganizations}" >
														<c:if test="${not empty selectedOrganizations}"> 
															<tr><td>
																<c:out value="${selectedOrganizations.name}"/>:
																<c:out value="${selectedOrganizations.projectId}"/>
																	<c:if test ="${!empty selectedOrganizations.organisation.ampOrgId}">
																			<bean:define id="selectedOrgForPopup" name="selectedOrganizations" 	type="org.digijava.module.aim.helper.OrgProjectId"  	toScope="request" />
																			<jsp:include page="previewOrganizationPopup.jsp"/>
																	</c:if>
															</td></tr>
														</c:if>														
													</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</field:display>
									</feature:display>

									<feature:display module="Project ID and Planning" name="Planning">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:planning">
											Planning</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<table width="100%" cellSpacing=2 cellPadding=1>
												<field:display feature="Planning" name="Line Ministry Rank">
												<tr>
													<td width="32%"><digi:trn key="aim:lineMinRank">
													Line Ministry Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.lineMinRank == -1}">

													</c:if>
													<c:if test="${aimEditActivityForm.lineMinRank != -1}">
													${aimEditActivityForm.lineMinRank}
													</c:if>
													</td>
												</tr>
												</field:display>
												<field:display name="Ministry of Planning Rank" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:planMinRank">
													Ministry of Planning Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.planMinRank == -1}">

													</c:if>
													<c:if test="${aimEditActivityForm.planMinRank != -1}">
													${aimEditActivityForm.planMinRank}
													</c:if>
													</td>
												</tr>
												</field:display>

												<field:display name="Proposed Approval Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:originalApprovalDate">
													Original Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalAppDate}
													</td>
												</tr>
												</field:display>
												<field:display name="Actual Approval Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:revisedApprovalDate">Revised Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedAppDate}
													</td>
												</tr>
												</field:display>
												<field:display name="Proposed Start Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:originalStartDate">Original Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalStartDate}
													</td>
												</tr>
												</field:display>
												<field:display name="Final Date for Contracting" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:ContractingDateofProject1">Final Date for Contracting</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.contractingDate}"/>
													</td>
												</tr>
												</field:display>
												<field:display name="Final Date for Disbursements" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:DisbursementsDateofProject1">Final Date for Disbursements</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.disbursementsDate}"/>
													</td>
												</tr>
												</field:display>
												<field:display name="Actual Start Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:revisedStartDate">Revised Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedStartDate}
													</td>
												</tr>
												</field:display>
												<field:display name="Proposed Completion Date" feature="Planning">
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn key="aim:proposedCompletionDate">
													Proposed Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.proposedCompDate}
													</td>
												</tr>
												</c:if>
												</field:display>
												<field:display name="Current Completion Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:currentCompletionDate">
													Current Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.currentCompDate}"/>
													</td>
												</tr>
												</field:display>
												<c:if test="${aimEditActivityForm.editAct}">
												<c:if test="${!empty aimEditActivityForm.activityCloseDates}">
												<tr>
													<td width="32%" valign=top><digi:trn key="aim:proposedCompletionDates">
													Proposed Completion Dates</digi:trn></td>
													<td width="1" valign=top>:</td>
													<td align="left" valign=top>
														<table cellPadding=0 cellSpacing=0>
															<c:forEach var="closeDate" items="${aimEditActivityForm.activityCloseDates}">
															<tr>
																<td>
																	<c:out value="${closeDate}"/>
																</td>
															</tr>
															</c:forEach>
														</table>
													</td>
												</tr>
												</c:if>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												</c:if>


												<field:display name="Status" feature="Planning">
												<tr>
													<td colspan="3"><digi:trn key="aim:status">Status</digi:trn> :
														<category:getoptionvalue categoryValueId="${aimEditActivityForm.statusId}"/>
													</td>
												</tr>
												<tr>
													<td colspan="3"><c:out value="${aimEditActivityForm.statusReason}"/></td>
												</tr>
												</field:display>
											</table>
										</td>
									</tr>
									</feature:display>

									<feature:display name="Location" module="Project ID and Planning">
									<field:display name="Implementation Level" feature="Location">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:level">
											Level</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.levelId>0}" >
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.levelId}"/>
											</c:if>
										</td>
									</tr>
									</field:display>
									
									<tr>
									<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
									References
									</td>
									<td bgcolor="#ffffff">
									<c:forEach items="${aimEditActivityForm.referenceDocs}" var="refDoc" varStatus="loopstatus">
										<table border="0">
											<tr>
												<td>
													<c:if test="${!empty refDoc.comment}">
													${refDoc.categoryValue}
													</c:if> 
												</td>
											</tr>
										</table>
									</c:forEach>
									</td>
									</tr>
									
									<field:display name="Implementation Location" feature="Location">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:location">
											Location</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedLocs}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="locations" items="${aimEditActivityForm.selectedLocs}">
													<tr><td>
													<c:if test="${!empty locations.country}">
														[<c:out value="${locations.country}"/>]
													</c:if>
													<c:if test="${!empty locations.region}">
														[<c:out value="${locations.region}"/>]
													</c:if>
													<c:if test="${!empty locations.zone}">
														[<c:out value="${locations.zone}"/>]
													</c:if>
													<c:if test="${!empty locations.woreda}">
														[<c:out value="${locations.woreda}"/>]
													</c:if>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</field:display>
									</feature:display>

									<feature:display name="Sectors" module="Project ID and Planning">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:sector">
											Sector</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.activitySectors}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="sectors" items="${aimEditActivityForm.activitySectors}">
													<tr><td>
													<c:if test="${!empty sectors.sectorName}">
																				<c:out value="${sectors.sectorName}" />
																			</c:if>&nbsp;&nbsp; <c:if
																				test="${sector.sectorPercentage!=''}">
																				<c:if test="${sector.sectorPercentage!='0'}">
																			(<c:out value="${sectors.sectorPercentage}" />)%
																			</c:if>
																			</c:if> <c:if test="${!empty sectors.subsectorLevel1Name}">
														[<c:out value="${sectors.subsectorLevel1Name}"/>]
													</c:if>
													<c:if test="${!empty sectors.subsectorLevel2Name}">
														[<c:out value="${sectors.subsectorLevel2Name}"/>]
													</c:if>
													</td>

													</tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</feature:display>
									</module:display>
									<module:display name="National Planning Dashboard">
									<feature:display name="NPD Programs " module="National Planning Dashboard">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:program">Program</digi:trn>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.actPrograms!=null}">
                                            <c:if test="${!empty aimEditActivityForm.actPrograms}">
                                              <c:forEach var="tempPgm" items="${aimEditActivityForm.actPrograms}">
                                                <c:if test="${tempPgm!=null}">
                                                  *&nbsp;&nbsp;<c:out value="${tempPgm.programviewname}"/> <br>
                                                </c:if>
                                              </c:forEach>
                                            </c:if>
                                          </c:if>
										</td>
                                    </tr>
									</feature:display>
									<feature:display name="Proposed Project Cost" module="National Planning Dashboard">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:proposedPrjectCost">Proposed Project Cost</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.proProjCost!=null}">
                                                  <table cellSpacing=1 cellPadding="3" bgcolor="#aaaaaa" width="100%">
                                                      <tr bgcolor="#ffffff">
																		  <td>
																		  			Cost
																		  </td>
                                                        <td bgcolor="#FFFFFF" align="left" >
                                                          <c:if test="${aimEditActivityForm.proProjCost.funAmount!=null}">
																			 	<FONT color=blue>*</FONT>
                                                            ${aimEditActivityForm.proProjCost.funAmount}
                                                          </c:if>&nbsp;
																			 <c:if test="${aimEditActivityForm.proProjCost.currencyCode!=null}">
                                                            ${aimEditActivityForm.proProjCost.currencyCode}
                                                          </c:if>
                                                        </td>
																		  </tr>
																		  <tr bgcolor="#ffffff">
																		  <td>
																		  	Proposed	Completion Date
																		  </td>
                                                        <td bgcolor="#FFFFFF" align="left" width="150">
                                                          <c:if test="${aimEditActivityForm.proProjCost.funDate!=null}">
                                                             ${aimEditActivityForm.proProjCost.funDate}
                                                          </c:if>
                                                        </td>
                                                      </tr>
                                                    </table>
                                             </c:if>
										</td>

									</tr>
									</feature:display>
									<feature:display name="Project Performance" module="National Planning Dashboard">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:meActivityPerformance">
											Activity - Performance</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<% if (actPerfChartUrl != null) { %>
												<img src="<%= actPerfChartUrl %>" width=370 height=450 border=0 usemap="#<%= actPerfChartFileName %>"><br><br>
											<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
											    <digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>
											    </span><br><br>
											<% } %>
										</td>
									</tr>
									</feature:display>
									<feature:display name="Project Risk" module="National Planning Dashboard">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:meActivityRisk">
											Activity - Risk</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<% if (actRiskChartUrl != null) { %>
												<bean:define id="riskColor" name="riskColor" scope="request" toScope="page" type="java.lang.String"/>
												<bean:define id="riskName" name="overallRisk" scope="request" toScope="page" type="java.lang.String"/>
												<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn>:
												<font color="<bean:write name="riskColor" />">

												<b><digi:trn key="<%=riskName%>"><%=riskName%></digi:trn></b>

												<img src="<%= actRiskChartUrl %>" width=370 height=350 border=0 usemap="#<%= actRiskChartFileName %>">
												<br><br>
											<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
										  	    <digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>
											    </span><br><br>
											<% } %>
										</td>
									</tr>
									</feature:display>
									</module:display>
								<logic:present name="currentMember" scope="session">
									<module:display name="Funding">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:funding">
											Funding</digi:trn>
										</td>
										<td bgcolor="#ffffff">
										                                            <table width="95%" cellSpacing=1 cellPadding=0 border=0 align="center">
                                              <tr>
                                                <td>
                                                  <table cellSpacing=8 cellPadding=0 border=0 width="100%" class="box-border-nopadding">
                                                    <logic:notEmpty name="aimEditActivityForm" property="fundingOrganizations">
                                                      <logic:iterate name="aimEditActivityForm" property="fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">

                                                        <logic:notEmpty name="fundingOrganization" property="fundings">
                                                          <logic:iterate name="fundingOrganization" indexId="index" property="fundings" id="funding" type="org.digijava.module.aim.helper.Funding">
                                                            <tr>
                                                              <td>
                                                                <table cellSpacing=1 cellPadding=0 border="0" width="100%" class="box-border-nopadding">
                                                                  <tr>
                                                                    <td>
                                                                      <table cellSpacing=1 cellPadding=0 border="0" width="100%">
                                                                        <tr>
                                                                          <td>
                                                                            <table width="100%" border="0" cellpadding="1" bgcolor="#ffffff" cellspacing="1">
                                                                            <field:display name="Funding Organization Id" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">																																<digi:trn key="aim:fundingOrgId">
                                                                                    Funding Organization Id</digi:trn></a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <bean:write name="funding"	property="orgFundingId"/>
                                                                                </td>
                                                                              </tr>
                                                                             </field:display>
                                                                             <field:display name="Funding Organization Name" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">

                                                                                  <a title="<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>">
                                                                                  	<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>
                                                                                  </a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  ${fundingOrganization.orgName}
                                                                                </td>
                                                                              </tr>
                                                                             </field:display>

                                                                              <!-- type of assistance -->
                                                                              <field:display name="Type Of Assistance" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  <digi:trn key="aim:typeOfAssist">
                                                                                    Type of Assistance </digi:trn>
																					</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="typeOfAssistance">
                                                                                    <bean:write name="funding"	property="typeOfAssistance.value"/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                              </tr>
																			</field:display>
																			<field:display name="Type Of Assistance" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>">
                                                                                 	 <digi:trn key="aim:financialInst">Financial Instrument</digi:trn>
																				  </a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="financingInstrument">
                                                                                    <bean:write name="funding"	property="financingInstrument.value"/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                              </tr>
																			</field:display>
				                                                            </table>
                                          </td>
                                                                        </tr>
                                                                      </table>
                                    </td>
                                                                  </tr>
                                                                  <tr>
                                                                    <td>
                                                                      <table width="98%" border="0" cellpadding="1"   bgcolor="#ffffff" cellspacing="1">
                                                                        <tr>
                                                                          <td bgcolor="#FFFFFF" align="right" colspan="2">
                                                                            <table width="100%" border="0" cellSpacing="1" cellPadding="1" bgcolor="#dddddd">
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="4"><b>
                                                                                  <a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>">
                                                                                  <digi:trn key="aim:commitments">Commitments </digi:trn></b>
                                                                                  </a>
                                                                                </td>
                                                                                <td width="25%"><b>
                                                                                <digi:trn key="aim:exchange">Exchange Rate</digi:trn></b>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.fundingDetails}">
                                                                              <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                                <logic:equal name="fundingDetail" property="transactionType" value="0">

                                                                                  <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                                                    <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                      <tr bgcolor="#FFFF00">
                                                                                    </c:if>
                                                                                    <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                    </c:if>

                                                                                    <td width="50">
	                                                                                    <field:display name="Adjustment Type Commitment" feature="Funding Organizations">
    	                                                                                	<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</digi:trn>
																						</field:display>
                                                                                    </td>


                                                                                    <td width="120" align="right">
                                                                                      <field:display name="Amount Commitment" feature="Funding Organizations">
                                                                                      	<FONT color=blue>*</FONT>
                                                                                      	<bean:write name="fundingDetail" property="transactionAmount" format="###,###,###,###,###"/>&nbsp;
                                                                                      </field:display>
                                                                                    </td>

                                                                                    <td width="150">
	                                                                                    <field:display name="Currency Commitment" feature="Funding Organizations">
    	                                                                                  <bean:write name="fundingDetail" property="currencyCode"/>
        	                                                                             </field:display>
                                                                                    </td>
                                                                                    <td width="70">
                                                                                    	<field:display name="Date Commitment" feature="Funding Organizations">
		                                                                                      <bean:write name="fundingDetail" property="transactionDate"/>
	                                                                                    </field:display>
                                                                                    </td>
                                                                                    <td>
                                                                                    	<field:display name="Exchange Rate" feature="Funding Organizations">
   																									<bean:write name="fundingDetail" property="fixedExchangeRate"/>
																							</field:display>

                                                                                    </td>
                                                                                      </tr>

                                                                                  </c:if>

                                                                                  <c:if test="${aimEditActivityForm.donorFlag == false}">
                                                                                    <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                        <td width="50">
                                                                                        <field:display name="Adjustment Type Commitment" feature="Funding Organizations">
                                                                                          <digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</digi:trn>
																							</field:display>
                                                                                        </td>
                                                                                        <td width="120" align="right">
                                                                                        <field:display name="Amount Commitment" feature="Funding Organizations">
                                                                                          <FONT color=blue>*</FONT>
                                                                                          <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                          </field:display>
                                                                                        </td>
                                                                                        <td width="150">
                                                                                        <field:display name="Currency Commitment" feature="Funding Organizations">
                                                                                          <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                          </field:display>
                                                                                        </td>
                                                                                        <td width="70">
                                                                                       	 	<field:display name="Date Commitment" feature="Funding Organizations">
                                                                                          		<bean:write name="fundingDetail" property="transactionDate"/>
                                                                                          	</field:display>
                                                                                        </td>
                                                                                        <td>
	                                                                                        <field:display name="Exchange Rate" feature="Funding Organizations">
   																									<bean:write name="fundingDetail" property="fixedExchangeRate"/>
																							</field:display>
                                                                                        </td>
                                                                                      </tr>
                                                                                    </c:if>
                                                                                     <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                        <td width="50">
                                                                                        <field:display name="Adjustment Type Commitment" feature="Funding Organizations">
                                                                                          <digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</digi:trn>
																						</field:display>
                                                                                        </td>
                                                                                        <td width="120" align="right">
	                                                                                        <field:display name="Amount Commitment" feature="Funding Organizations">
    	                                                                                      <FONT color=blue>*</FONT>
        	                                                                                  <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
        	                                                                                 </field:display>
                                                                                        </td>
                                                                                        <td width="150">
	                                                                                        <field:display name="Currency Commitment" feature="Funding Organizations">
    	                                                                                      <bean:write name="fundingDetail" property="currencyCode"/>
    	                                                                                    </field:display>
                                                                                        </td>
                                                                                        <td width="70">
	                                                                                        <field:display name="Date Commitment" feature="Funding Organizations">
    	                                                                                      <bean:write name="fundingDetail" property="transactionDate"/>
    	                                                                                    </field:display>
                                                                                        </td>
                                                                                        <td>
   																							<field:display name="Exchange Rate" feature="Funding Organizations">
   																									<bean:write name="fundingDetail" property="fixedExchangeRate"/>
																							</field:display>
																						</td>
                                                                                      </tr>
                                                                                    </c:if>
                                                                                  </c:if>

                                                                                </logic:equal>
                                                                              </logic:iterate>
                                                                                <tr>
                                                                                <td><digi:trn key='aim:totalcommittment'>
                                                                                TOTAL:
                                                                                </digi:trn></td>
                                                                                      <TD  colspan="4" align="right"><bean:write name="aimEditActivityForm" property="totalCommitted"/>&nbsp;USD</TD>
                                                                                </tr>
                                                                              </c:if>
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">&nbsp;</td>
                                                                              </tr>
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">
                                                                                  <a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>"><b> <digi:trn key="aim:disbursements">			Disbursements </digi:trn></b>
																				</a>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.fundingDetails}">
                                                                              <logic:iterate name="funding" property="fundingDetails"
                                                                              id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                              <logic:equal name="fundingDetail" property="transactionType" value="1">


                                                                                <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                                                  <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                    <tr bgcolor="#FFFF00">
                                                                                  </c:if>
                                                                                  <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																						</c:if>
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>
																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																								</field:display>
																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>
																							</td>
																							<td width="70">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>
																							</td>

																						</tr>
																						</c:if>

																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																						<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>
																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																								</field:display>
																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>
																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>
																							</td>
																						</tr>
																						</c:if>
																						<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>
																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																								</field:display>
																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>
																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>
																							</td>

																						</tr>
																						</c:if>
																						</c:if>

																						</logic:equal>
																						</logic:iterate>
                                                                                        </c:if>
																						<tr bgcolor="#ffffff">
																							<td colspan="5">&nbsp;</td>
																						</tr>
																						<tr bgcolor="#ffffff">
																							<td colspan="5">
																							<a title="<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>">	<b><digi:trn key="aim:expenditures"> Expenditures </digi:trn></b>
																							</a>
																							</td>
																						</tr>
                                                                                        <c:if test="${!empty funding.fundingDetails}">
																						<logic:iterate name="funding" property="fundingDetails"
																						id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
																						<logic:equal name="fundingDetail" property="transactionType" value="2">


																						<c:if test="${aimEditActivityForm.donorFlag == true}">
																						<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																						<tr bgcolor="#FFFF00">
																						</c:if>
																						<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																						</c:if>
																							<td width="50">
																								<field:display name="Adjustment Type Expenditure" feature="Funding Organizations">
																									<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>
																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Expenditure" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																								</field:display>
																							</td>
																							<td width="150">
																								<field:display name="Currency Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>
																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>
																							</td>

																						</tr>
																						<tr>
																							<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																								<field:display name="Classification Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="classification"/>
																								</field:display>
																							</td>
																						</tr>
																						</c:if>

																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																							<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																								<tr bgcolor="#ffffff">
																									<td width="50">
																								<field:display name="Adjustment Type Expenditure" feature="Funding Organizations">
																									<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>
																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Expenditure" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																								</field:display>
																							</td>
																							<td width="150">
																								<field:display name="Currency Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>
																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>
																							</td>

																						</tr>
																						<tr>
																							<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																								<field:display name="Classification Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="classification"/>
																								</field:display>
																							</td>
																						</tr>
																							</c:if>
																							<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																								<tr bgcolor="#ffffff">
																								<td width="50">
																								<field:display name="Adjustment Type Expenditure" feature="Funding Organizations">
																									<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>
																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Expenditure" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																								</field:display>
																							</td>
																							<td width="150">
																								<field:display name="Currency Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>
																							</td>
																							<td width="70">
																								<field:display name="Date Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>
																							</td>

																						</tr>
																						<tr>
																							<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																								<field:display name="Classification Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="classification"/>
																								</field:display>
																							</td>
																						</tr>
																							</c:if>
																						</c:if>
																						</logic:equal>
																						</logic:iterate>
                                                                                        </c:if>
																					</table>
																				</td>
																			</tr>
																		</table>

																	</td></tr>
																	<tr><td bgcolor="#ffffff">
																		<FONT color=blue>*
																			<digi:trn key="aim:theAmountEnteredAreInThousands">
																				The amount entered are in thousands (000)
		  																	</digi:trn>
																		</FONT>
																	</td></tr>
																	</table>
																	</td></tr>
																	</logic:iterate>
																	</logic:notEmpty>
																</logic:iterate>
																<tr><td>&nbsp;</td></tr>
																</logic:notEmpty>
															</table>
														</td>
													</tr>
												</table>
										<!--
											<table  border="0" cellpadding="4" cellspacing="1">
				                 					<TR bgcolor="#DDDDDB" >
							                        	<TD><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>
									                    <TD width="20"><digi:trn key="aim:organization">Organization</digi:trn></TD>
														<TD><digi:trn key="aim:totalCommitted">Total Committed</digi:trn></TD>
	                        						 	<TD><digi:trn key="aim:totalDisbursed">Total Disbursed</digi:trn></TD>
														<TD><digi:trn key="aim:unDisbursedFunds">Undisbursed Funds</digi:trn></TD>
							                         	<TD><digi:trn key="aim:totalExpended">Total Expended</digi:trn></TD>
														<TD><digi:trn key="aim:unExpendedFunds">Unexpended Funds</digi:trn></TD>
													</TR>

                                                    <c:if test="${!empty aimEditActivityForm.financingBreakdown}">
													<logic:iterate name="aimEditActivityForm" property="financingBreakdown" id="breakdown" type="org.digijava.module.aim.helper.FinancingBreakdown">
															<TR valign="top" bgcolor="#f4f4f2">
					    					           			<TD>
						               								<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlFinancialOverview}" property="ampActivityId">
																		<bean:write name="aimEditActivityForm" property="activityId"/>
																	</c:set>
																	<c:set target="${urlFinancialOverview}" property="ampFundingId">
																		<bean:write name="breakdown" property="ampFundingId"/>
																	</c:set>

																	<c:set var="translation">
																		<digi:trn key="aim:clickToViewFinancialOverview">
																		Click here to view Financial Overview</digi:trn>
																	</c:set>
											                  		<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" title="${translation}" >
																		<bean:write name="breakdown" property="financingId" />
																	</digi:link>
																</TD>
																<bean:define id="breakdown" name="breakdown" type="org.digijava.module.aim.helper.FinancingBreakdown" toScope="request" />

											                  		<TD><jsp:include page="previewFinancingOrganizationPopup.jsp"/></TD>
													                <TD align="right"><bean:write name="breakdown" property="totalCommitted"/></TD>
							                						<TD align="right"><bean:write name="breakdown" property="totalDisbursed"/></TD>
													                <TD align="right"><bean:write name="breakdown" property="unDisbursed"/></TD>
					    									        <TD align="right"><bean:write name="breakdown" property="totalExpended"/></TD>
					        				            		    <TD align="right"><bean:write name="breakdown" property="unExpended"/></TD>
															</TR>

													</logic:iterate>
                                                    </c:if>
													<TR valign="top" class="note">
																<TD><digi:trn key="aim:total">Total</digi:trn></TD>
											                    <TD>&nbsp;</TD>
																<TD align="right"><bean:write name="aimEditActivityForm" property="totalCommitted"/></TD>
																<TD align="right"><bean:write name="aimEditActivityForm" property="totalDisbursed"/></TD>
																<TD align="right"><bean:write name="aimEditActivityForm" property="totalUnDisbursed"/></TD>
																<TD align="right"><bean:write name="aimEditActivityForm" property="totalExpended"/></TD>
																<TD align="right"><bean:write name="aimEditActivityForm" property="totalUnExpended"/></TD>
													</TR>

											</table>
											-->
										</td>
									</tr>
									</module:display>
									</logic:present>
									<logic:present name="currentMember" scope="session">
									<!-- Costing -->
									<feature:display name="Costing" module="Activity Costing">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:costing">
											Costing</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											&nbsp;&nbsp;&nbsp;
											<table width="100%">
												<tr>
													<td>
														<bean:define id="mode" value="preview" type="java.lang.String" toScope="request" />
														<jsp:include page="viewCostsSummary.jsp" flush="" />
													</td>
												</tr>
											</table>

										</td>
									</tr>
									</feature:display>
									<!-- End Costing -->
									</logic:present>
									<feature:display name="Regional Funding" module="Funding">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:regionalFunding">
											Regional Funding</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.regionalFundings}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
												<c:forEach var="regFunds" items="${aimEditActivityForm.regionalFundings}">
													<tr><td bgcolor="#ffffff">
														<table width="100%" cellSpacing="1" cellPadding="1">
															<tr><td bgcolor="#ffffff"><b>
																<c:out value="${regFunds.regionName}"/></b>
															</td></tr>
															<c:if test="${!empty regFunds.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding" border="1">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty regFunds.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty regFunds.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.expenditures}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
														</table>
													</td></tr>
												</c:forEach>
												<tr><td bgcolor="#ffffff">
													<FONT color=blue>*
													<digi:trn key="aim:theAmountEnteredAreInThousands">
													The amount entered are in thousands (000)</digi:trn></FONT>
												</td></tr>
												</table>
											</c:if>
										</td>
									</tr>
									</feature:display>
									<module:display name="Organizations">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:relatedOrganizations">
											Related Organizations</digi:trn>
										</td>

										<td bgcolor="#ffffff">
											<feature:display name="Implementing Agency" module="Organizations">
											<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="impAgencies">
												<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="impAgencies"
													id="impAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="impAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/></feature:display>

											<feature:display name="Beneficiary Agency" module="Organizations">
											<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="benAgencies">
												<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="benAgencies"
													id="benAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="benAgency" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											</feature:display>

											<feature:display name="Contracting Agency" module="Organizations">
											<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="conAgencies">
												<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="conAgencies"
													id="conAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<ul><li> <bean:write name="conAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											</feature:display>
										</td>
									</tr>
									</module:display>
									<module:display name="Components">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:components">
											Components</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">

															<tr><td><b>
																<c:out value="${comp.title}"/></b>
															</td></tr>
															
															<tr><td>
																<i>
																<digi:trn key="aim:description">Description</digi:trn> :</i>
																<c:out value="${comp.description}"/>
															</td></tr>
															
															<!-- START SISIN Fields -->
															<feature:display name="SISIN" module="Components"> 
																<field:display name="SISIN Code" feature="SISIN"> 
																	<tr>
																		<td width="50">
																			<i>
																			<digi:trn key="aim:sisincode">SISIN Code</digi:trn> :</i>
																			<c:out value="${comp.sisinProyect.sisincode}"/>
																		</td>
																	</tr>
																</field:display>
																<field:display name="Localization" feature="SISIN"> 
																	<tr>
																		<td width="50">
																			<i>
																			<digi:trn key="aim:localization">Localization</digi:trn> :</i>
																			<c:out value="${comp.sisinProyect.localization}"/>
																		</td>
																	</tr>
																</field:display>
																<field:display name="SISIN Sector" feature="SISIN"> 
																	<tr>
																		<td width="50">
																			<i>
																			<digi:trn key="aim:sisinsector">SISIN Sector</digi:trn> :</i>
																			<c:out value="${comp.sisinProyect.sisinsector}"/>
																		</td>
																	</tr>
																</field:display>
																<field:display name="Financing Source" feature="SISIN">
																	<tr>
																		<td width="50">
																			<i>
																			<digi:trn key="financingsource">Financing Source</digi:trn> :</i>
																			<c:out value="${comp.sisinProyect.financingsource}"/>
																		</td>
																	</tr>
																</field:display>
																<field:display name="Agency Source" feature="SISIN">
																	<tr>
																		<td width="50">
																			<i>
																			<digi:trn key="agencysource">Agency Source</digi:trn> :</i>
																			<c:out value="${comp.sisinProyect.agencysource}"/>
																		</td>
																	</tr>
																</field:display>
																<field:display name="Stage" feature="SISIN">
																	<tr>
																		<td width="50">
																			<i>
																			<digi:trn key="stage">Stage</digi:trn> :</i>											 
																			<c:out value="${comp.sisinProyect.stage}"/>
																		</td>
																	</tr>
																</field:display>
																<field:display name="Classification Program Code" feature="SISIN">
																	<tr>
																		<td width="50">
																			<i>
																			<digi:trn key="classifprogramcode">Classification Program Code</digi:trn> :</i>
																			<c:out value="${comp.sisinProyect.programcode}"/>
																		</td>
																	</tr>
																</field:display>
															</feature:display>																																																															
															<!-- END SISIN Fields -->															
															
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>																
															</td></tr>
															<c:if test="${!empty comp.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.expenditures}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<c:out value="${fd.adjustmentTypeName}"/>
																							</td>
																							<td align="right">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>
																							</td>
																							<td>
																								<c:out value="${fd.currencyCode}"/>
																							</td>
																							<td width="70">
																								<c:out value="${fd.transactionDate}"/>
																							</td>
																							<td>
																								<c:out value="${fd.perspectiveName}"/>
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<tr><td bgcolor="#ffffff">
																<FONT color=blue>*
																	<digi:trn key="aim:theAmountEnteredAreInThousands">
																		The amount entered are in thousands (000)
		  															</digi:trn>
																</FONT>
															</td></tr>
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:physicalProgressOfTheComponent">
																Physical progress of the component</digi:trn></b>
															</td></tr>
															<c:if test="${!empty comp.phyProgress}">
																<c:forEach var="phyProg" items="${comp.phyProgress}">
																	<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<b>
																			<c:out value="${phyProg.title}"/></b> -
																			<c:out value="${phyProg.reportingDate}"/>
																	</td></tr>
																	<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<i>
																		<digi:trn key="aim:description">Description</digi:trn> :</i>
																		<c:out value="${phyProg.description}"/>
																	</td></tr>
																</c:forEach>
															</c:if>
														</table>
													</td></tr>
													</table>
												</c:forEach>
											</c:if>
										</td>
									</tr>
									</module:display>
									<module:display name="Issues">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:issues">
											Issues</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border=0>
												<c:forEach var="issue" items="${aimEditActivityForm.issues}">
													<tr><td valign="top">
														<li class="level1"><b><c:out value="${issue.name}"/></b></li>
													</td></tr>
													<c:if test="${!empty issue.measures}">
														<c:forEach var="measure" items="${issue.measures}">
															<tr><td>
																<li class="level2"><i><c:out value="${measure.name}"/></i></li>
															</td></tr>
															<c:if test="${!empty measure.actors}">
																<c:forEach var="actor" items="${measure.actors}">
																	<tr><td>
																		<li class="level3"><c:out value="${actor.name}"/></li>
																	</td></tr>
																</c:forEach>
															</c:if>
														</c:forEach>
													</c:if>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</module:display>
									<module:display name="Document Management">
									<feature:display name="Related Documents" module="Document Management">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:relatedDocuments">
											Related Documents</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.documentList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												 <logic:iterate name="aimEditActivityForm"  property="documents"
													id="docs" type="org.digijava.module.aim.helper.Documents">
													<c:if test="${docs.isFile == true}">
													<tr><td>
													 <table width="100%" class="box-border-nopadding">
													 	<tr bgcolor="#ffffff">
															<td vAlign="center" align="left">
																&nbsp;<b><c:out value="${docs.title}"/></b> -
																&nbsp;&nbsp;&nbsp;<i><c:out value="${docs.fileName}"/></i>
																<logic:notEqual name="docs" property="docDescription" value=" ">
																	<br />&nbsp;
																	<b>Description:</b>&nbsp;<bean:write name="docs" property="docDescription" />
																</logic:notEqual>
																<logic:notEmpty name="docs" property="date">
																	<br />&nbsp;
																	<b>Date:</b>&nbsp;<c:out value="${docs.date}"/>
																</logic:notEmpty>
																<logic:notEmpty name="docs" property="docType">
																	<br />&nbsp;
																	<b>Document Type:</b>&nbsp;
																	<bean:write name="docs" property="docType"/>
																</logic:notEmpty>
															</td>
														</tr>
													 </table>
													</td></tr>
													</c:if>
													</logic:iterate>
												</table>
											</c:if>
											<c:if test="${!empty aimEditActivityForm.linksList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="docList" items="${aimEditActivityForm.linksList}">
					   							<bean:define id="links" name="docList" property="relLink" />
													<tr><td>
														<table width="100%" class="box-border-nopadding">
															<tr>
																<td width="2">
																	<digi:img src="module/aim/images/web-page.gif"/>
																</td>
																<td align="left" vAlign="center">&nbsp;
																	<b><c:out value="${links.title}"/></b> -
																	&nbsp;&nbsp;&nbsp;<i><a href="<c:out value="${links.url}"/>">
																	<c:out value="${links.url}"/></a></i>
																	<br>&nbsp;
																	<b>Desc:</b>&nbsp;<c:out value="${links.description}"/>
																</td>
															</tr>
														</table>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</feature:display>
									</module:display>
									<feature:display name="Executing Agency" module="Organizations">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:executingAgencies">
											Executing Agencies</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.executingAgencies}">
												<table width="100%" cellpadding="2" cellspacing="2" valign="top" align="left">
													<c:forEach var="exAgency" items="${aimEditActivityForm.executingAgencies}">
														<tr><td>
															<c:out value="${exAgency.name}"/>
														</td></tr>
													</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</feature:display>

									<feature:display name="Implementing Agency" module="Organizations">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:implementingAgencies">
											Implementing Agencies</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.impAgencies}">
												<table width="100%" cellpadding="2" cellspacing="2" valign="top" align="left">
													<c:forEach var="impAgency" items="${aimEditActivityForm.impAgencies}">
														<tr><td>
															<c:out value="${impAgency.name}"/>
														</td></tr>
													</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</feature:display>
									<feature:display name="Contracting Agency" module="Organizations">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:contractors">
											Contractors</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.contractors}"/>
										</td>
									</tr>
									</feature:display>

									<module:display name="Contact Information">
									<feature:display name="Donor Contact Information" module="Contact Information">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:donorFundingContactInformation">
											Donor funding contact information</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.dnrCntFirstName}"/>
											<c:out value="${aimEditActivityForm.dnrCntLastName}"/> -
											<c:out value="${aimEditActivityForm.dnrCntEmail}"/>
										</td>
									</tr>
									</feature:display>
									<feature:display name="Mofed Contact Information" module="Contact Information">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:mofedContactInformation">
											MOFED contact information</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.mfdCntFirstName}"/>
											<c:out value="${aimEditActivityForm.mfdCntLastName}"/> -
											<c:out value="${aimEditActivityForm.mfdCntEmail}"/>
										</td>
									</tr>
									</feature:display>
									</module:display>


									<field:display name="Accession Instrument" feature="Identification">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.accessionInstrument > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.accessionInstrument}"/>
											</c:if>
											&nbsp;
										</td>
									</tr>
									</field:display>
									<field:display name="A.C. Chapter" feature="Identification">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:acChapter"> A.C. Chapter</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.acChapter > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.acChapter}"/>
											</c:if>
											&nbsp;
										</td>
									</tr>
									</field:display>


									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityCreatedBy">
											Activity created by</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthFirstName}"/>
											<c:out value="${aimEditActivityForm.actAthLastName}"/> -
											<c:out value="${aimEditActivityForm.actAthEmail}"/>
										</td>
									</tr>
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:dataSource">
											Data Source</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthAgencySource}"/>
										</td>
									</tr>
									<logic:notEmpty name="aimEditActivityForm" property="updatedDate">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityUpdatedOn">
											Activity updated on</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedDate}"/>
										</td>
									</tr>
									</logic:notEmpty>
									<logic:notEmpty name="aimEditActivityForm" property="updatedBy">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityUpdatedBy">
											Activity updated by</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedBy.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.updatedBy.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.updatedBy.user.email}"/>
										</td>
									</tr>
									</logic:notEmpty>

									<logic:notEmpty name="aimEditActivityForm" property="createdDate">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityCreatedOn">
											Activity created on</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.createdDate}"/>
										</td>
									</tr>

									</logic:notEmpty>
									<logic:notEmpty name="aimEditActivityForm" property="team">
									<tr>
										<td class="t-name" width="30%" align="right" bgcolor="#f4f4f2">
											<digi:trn key="aim:activityTeamLeader">
											Data Team Leader</digi:trn>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.team.teamLead.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.team.teamLead.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.team.teamLead.user.email}"/>

										</td>
									</tr>
									</logic:notEmpty>
									<c:if test="${aimEditActivityForm.pageId == 1}">
									<tr><td bgColor=#ffffff align="center" colspan=2>
										<table cellPadding=3>
											<tr>
												<c:if test="${aimEditActivityForm.donorFlag == true}">
												<td>
													<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<< <digi:trn key="btn:back">Back</digi:trn>'name="backButton"/>


												</td>
												</c:if>
												<c:if test="${aimEditActivityForm.donorFlag == false}">
												<td>
													<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<< <digi:trn key="btn:back">Back</digi:trn>'name="backButton"/>
												</td>
												</c:if>
												<td>
													<input type="button" class="dr-menu" onclick="disable()" value='<digi:trn key="btn:saveActivity">Save Activity</digi:trn>' name="submitButton"/>

												</td>
											</tr>
										</table>
									</td></tr>
									</c:if>
									<c:if test="${aimEditActivityForm.pageId > 2}">
									<tr><td bgColor=#ffffff align="center" colspan=2>
										<input type="button" class="dr-menu" onclick="javascript:history.go(-1)" value='<digi:trn key="btn:back">Back</digi:trn>' />
									</td></tr>
									</c:if>
								</table>
							</td></tr>
							</table>
							</td></tr>
							<tr>
								<td>
									<div align="center">
										<logic:notEmpty name="previousActivity" scope="session">
											<digi:link href="/viewActivityPreview.do~pageId=2" paramId="activityId" paramName="previousActivity" paramScope="session"><font size="2"><digi:trn key="aim:previous">Previous</digi:trn></font></digi:link>
												<logic:notEmpty name="nextActivity" scope="session">
													<font size="2">
														&nbsp;-&nbsp;
													</font>
												</logic:notEmpty>
										</logic:notEmpty>
										<logic:notEmpty name="nextActivity" scope="session">
											<digi:link href="/viewActivityPreview.do~pageId=2" paramId="activityId" paramName="nextActivity" paramScope="session"><font size="2"><digi:trn key="aim:next">Next</digi:trn></font></digi:link>
										</logic:notEmpty>

									</div>
								</td>
							</tr>
						</table>
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




