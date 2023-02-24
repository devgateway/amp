<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.digijava.module.aim.helper.ChartGenerator"%>
<%@ page import="org.digijava.module.aim.util.IndicatorUtil"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="org.digijava.module.aim.util.TeamMemberUtil"%>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil"%>
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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="activityHistoryUtil.jsp" flush="true" />
<jsp:include page="activityViewWorkspaces.jsp" flush="true" />
<%@ page import="org.digijava.module.aim.util.TeamUtil" %>
<style type="text/css">
	.legend_label a.trnClass { color:yellow;}
</style>

<script language="JavaScript1.2" type="text/javascript"src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"> </script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="js_2/yui/yahoo/yahoo-min.js"/>" ></script>

<script type="text/javascript" src="<digi:file src="module/aim/scripts/jquery-ui-1.11.0/jquery-ui.min.js"/>"> </script>

<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/yahoo-dom-event/yahoo-dom-event.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/container/container-min.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/dragdrop/dragdrop-min.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/event/event-min.js'/>" ></script>
<script type="text/javascript" src="<digi:file src="js_2/yui/connection/connection-min.js"/>"></script>


	<DIV id="TipLayer"
		style="visibility: hidden; position: absolute; z-index: 1000; top: -100;">
	</DIV>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>


<%
	//Quick fix AMP-6573 please check it
	if (request.getParameter("currentlyEditing") != null) {
%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<%
	}
%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<digi:file src= 'module/aim/scripts/jquery-ui-1.11.0/jquery-ui.min.css'/>">

<script language="JavaScript">

function exportToPdf (actId) {
	var href="/aim/exportActToPDF.do?activityid="+actId;
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var popupName = "popup"+new Date().getTime();	
		var popupWindow =  window.open("", popupName, "height=500,width=780,menubar=no,scrollbars=yes,resizable");
		var referLink = document.createElement('a');
		referLink.href = href;
		referLink.target = popupName;
		document.body.appendChild(referLink);
		referLink.click();
		
	}
	else{
		openURLinResizableWindow(href, 780, 500);
	}
	
}

function exportToWord (actId) {
	var href="/aim/exportActToWord.do?activityid="+actId;
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var popupName = "popup"+new Date().getTime();	
		var popupWindow =  window.open("", popupName, "height=500,width=780,menubar=no,scrollbars=yes,resizable");
		var referLink = document.createElement('a');
		referLink.href = href;
		referLink.target = popupName;
		document.body.appendChild(referLink);
		referLink.click();
		
	}
	else{
		openURLinResizableWindow(href, 780, 500);
	}
	
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
		msg+= '<digi:trn key="aim:saveActivity:started" jsFriendly="true">Do you want to submit this activity for approval ?</digi:trn>';
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.getElementById('approvalStatus').value = "approved";
		}
		else if (confirm(msg))
				document.getElementById('approvalStatus').value = "created";
	}
	if (appstatus == "approved") {
		msg+='<digi:trn key="aim:saveActivity:approved" jsFriendly="true">Do you want to approve this activity ?</digi:trn>';
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

function viewChanges(){
	openNewWindow(650,200);
	<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />
	popupPointer.document.location.href = "<%=showLog%>?activityId=${aimEditActivityForm.activityId}";
}

function editActivity() {
    <field:display name="Add SSC Button" feature="Edit Activity">
        <digi:context name="editActivity" property="/wicket/onepager/ssc" />
        document.location.href="<%=editActivity%>/<%=request.getAttribute("actId")%>";
    </field:display>
    <field:display name="Add Activity Button" feature="Edit Activity">
        <digi:context name="editActivity" property="/wicket/onepager/activity" />
        document.location.href="<%=editActivity%>/<%=request.getAttribute("actId")%>";
    </field:display>
}

function expandAll(){
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
	
	
	
	YAHOO.namespace("YAHOO.amp");

		var myPanel = new YAHOO.widget.Panel("indicatorChartPopin", {
			width:"600px",
			fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true
		    });
	
	
</script>

<%
	Long actId = (Long) request.getAttribute("actId");
	String url = "/aim/viewIndicatorValues.do?ampActivityId=" + actId+ "&tabIndex=6";
	String actPerfChartFileName = null;
	String actPerfChartUrl = null;
	String actRiskChartUrl = null;
	String actRiskChartFileName = null;
	if(actId != null ){

	try {
		actPerfChartFileName = ChartGenerator
				.getActivityPerformanceChartFileName(actId, session,
						new PrintWriter(out), 370, 450, url, true, request);
	} catch (Exception e) {
		e.printStackTrace();
	}

	if (actPerfChartFileName != null) {
			actPerfChartUrl = request.getContextPath()+ "/aim/DisplayChart.img?filename="+ actPerfChartFileName;
	}


	try {

			actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(actId, session,new PrintWriter(out), 370, 350, url);
	} catch (Exception e) {
		e.printStackTrace();
	}


	if (actRiskChartFileName != null) {
		actRiskChartUrl = request.getContextPath()
				+ "/aim/DisplayChart.img?filename="
				+ actRiskChartFileName;
	}
	}
    int risk = IndicatorUtil.getOverallRisk(actId);
    String riskName = IndicatorUtil.getRiskRatingName(risk);
    String rskColor = IndicatorUtil.getRiskColor(risk);
    //request.setAttribute("overallRisk", riskName);
    //request.setAttribute("riskColor", rskColor);

%>

<digi:context name="digiContext" property="context" />
<digi:form action="/saveActivity.do" method="post">
<%-- 	<html:hidden property="step" /> --%>
	<html:hidden property="editAct" />
	<html:hidden property="identification.approvalStatus" styleId="approvalStatus" />
	<html:hidden property="workingTeamLeadFlag" styleId="workingTeamLeadFlag"/>
	

<!-- MAIN CONTENT PART START -->
<div class="content-dir">
<logic:present scope="request" parameter="editingUserId">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center style="margin-top:15px;">
	     <tr>
		     <td align="center">
		        <font color="red" size="3">
					<%
					if (request.getParameter("editingUserId").equals(TeamUtil.getCurrentMember().getMemberId().toString())) {
					%>
					<digi:trn key="aim:activityEditLocked">You may only edit one activity at a time.</digi:trn>
					<%} else {%>
					<digi:trn key="aim:activityIsBeeingEdited">Current activity is being edited by:</digi:trn> <%= TeamMemberUtil.getTeamMember(Long.valueOf(request.getParameter("editingUserId"))).getMemberName() %>
					<%}%>
		        </font>
		     </td>
	     </tr>
	     <tr>
	         <td>&nbsp;

	         </td>
	     </tr>
	</table>
</logic:present>

<c:if test="${aimEditActivityForm.identification.team.isolated}">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center style="margin-top:15px;">
	     <tr>
		     <td align="center">
		        <font color="red" size="3">
		                <digi:trn key="aim:workspaceIsPrivate">This activity has been created in a private workspace. It 
		                will not be visible in other workspaces.</digi:trn>
		        </font>
		     </td>
	     </tr>           
	     <tr>
	         <td>&nbsp;
	             
	         </td>
	     </tr>
	</table>
</c:if>

<logic:present scope="request" parameter="editPermissionError">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center style="margin-top:15px;">
	     <tr>
		     <td align="center">
		        <font color="red" size="3">
		                <digi:trn key="aim:noPermissionToEDit">You do not have permissions to edit this activity.</digi:trn>
		        </font>
		     </td>
	     </tr>
	     <tr>
	         <td>&nbsp;
	         </td>
	     </tr>
	</table>
</logic:present>

<c:if test="${aimEditActivityForm.activityExists=='no' && aimEditActivityForm.activityId > 0}">
	<div class="activity_preview_header" style="font-size: 12px;text-align: center;color:red">
		<ul style="padding-top: 5px;font-size: 12px">
			<li><digi:trn>Couldn't find activity! It may have been deleted from the system</digi:trn></li>
		</ul>	
	</div>
</c:if>
<c:if test="${aimEditActivityForm.activityExists!='no'}">

	<div class="activity_preview_header">
	  <table width="990" border="0" cellpadding="0" cellspacing="0">
	  <tr>
	    <td width="60%">
	    	<div class="activity_preview_name word_break"><c:out value="${aimEditActivityForm.identification.title}"/></div>
            <div style="clear:both;"></div>
			<div class="l_sm">
			 	<font color="red">
			 		<jsp:include page="utils/amountUnitsUnformatted.jsp">
						<jsp:param value="* " name="amount_prefix"/>
					</jsp:include>
				</font>
	    	</div>
            <div style="clear:both;"></div>

	    </td>
		
	    
	    <td class="preview-align" width=40%>
			<table width="100%" cellspacing="2" cellpadding="2" border="0" class="preview-buttons-align">
				<tr>
					<td>
						<c:set var="trn">
							<digi:trn>Export To PDF</digi:trn>
						</c:set>
						<a onclick="javascript:exportToPdf(${actId})" class="l_sm" style="cursor: pointer; color:#376091;" title="${trn}">
							<img hspace="2" src="img_2/ico_pdf.gif" />${trn}
						</a>
					</td>
					<td>
						<c:set var="showWordSetting" scope="page" value="false"/>
						<%if(FeaturesUtil.showEditableExportFormats()){ %>
							<c:set var="showWordSetting" scope="page" value="true"/>
						<%}%>
						<c:if test="${(sessionScope.currentMember != null) || (showWordSetting)}">
							<a onclick="javascript:exportToWord(${actId})" class="l_sm" style="cursor: pointer; color:#376091;">
								<img hspace="2" src="img_2/ico_word.gif" />
									<digi:trn>Export to Word</digi:trn>
							</a>
						</c:if>
					</td>
					<td>
						<logic:present name="currentMember" scope="session">
						<a onclick="window.open('/showPrinterFriendlyPage.do?edit=true', '_blank', '');" class="l_sm" style="cursor: pointer; color:#376091;" title="<digi:trn key="aim:print">Print</digi:trn>">
							<img hspace="2" src="img_2/ico_print.gif" width="15" height="18" />
							<digi:trn key="aim:print">Print</digi:trn>
						</a>
						</logic:present>
					</td>
				</tr>
			</table>
	    </td>
	  </tr>
	</table>
	</div>
	
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center class="fixed-layout" style="margin-top:15px;">
  <tr>
    <td width=215 bgcolor="#F4F4F4" valign=top>
	<div class="dash_left">
	<fieldset>
		<legend>
			<span class=legend_label>
				<digi:trn>Funding Information</digi:trn>&nbsp; 
				(${aimEditActivityForm.currCode})			
			</span>		
		</legend>
		<div class="field_text_big">
			<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Commitments" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
			<c:if test="${aimEditActivityForm.funding.showActual}">
			<digi:trn>Total Actual Commitments</digi:trn>:<br/> 
				<c:if test="${not empty aimEditActivityForm.funding.totalCommitments}">
					<b>
		                 <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalCommitments" /></span> 
		                 ${aimEditActivityForm.currCode}
					</b>
		         </c:if>
		         <c:if test="${empty aimEditActivityForm.funding.totalCommitments}">
			         <b>
			         0
			            ${aimEditActivityForm.currCode}
	                 </b>
		         </c:if>
			<hr/>
			</c:if>
			<c:if test="${aimEditActivityForm.funding.showPlanned}">
			<digi:trn>Total Planned Commitment</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalPlannedCommitments}">
					<b>
					 <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPlannedCommitments" /></span> 
                     ${aimEditActivityForm.currCode}
	     	      	</b>
                </c:if>
                <c:if test="${empty aimEditActivityForm.funding.totalPlannedCommitments}">
                	<b>
			         0
			         ${aimEditActivityForm.currCode}
	           
			         </b>
		         </c:if>
		         <hr/>
		         </c:if>
		       </module:display>
			<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
			<c:if test="${aimEditActivityForm.funding.showActual}">
			<digi:trn>Total Actual Disbursements</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalDisbursements}">
	                <b>
	                 <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalDisbursements" /></span> 
                      ${aimEditActivityForm.currCode}
	           
	                </b>		          
		         </c:if>
		         <c:if test="${empty aimEditActivityForm.funding.totalDisbursements}">
			         <b>
			         0
                     ${aimEditActivityForm.currCode}
	                 </b>
		         </c:if>
			<hr/>
			</c:if>
			<c:if test="${aimEditActivityForm.funding.showPlanned}">
			<digi:trn>Total Planned Disbursements</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalPlannedDisbursements}">
	                <b>
	                 <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPlannedDisbursements" /></span> 
	                 ${aimEditActivityForm.currCode}
	                </b>		          
		         </c:if>
		         <c:if test="${empty aimEditActivityForm.funding.totalPlannedDisbursements}">
			         <b>
			         0
			         ${aimEditActivityForm.currCode}
			         </b>
		         </c:if>
			<hr/>
			</c:if>
			</module:display>
			<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
			<c:if test="${aimEditActivityForm.funding.showActual}">
			<digi:trn>Total Expenditures</digi:trn>:<br/>
			<c:if test="${not empty aimEditActivityForm.funding.totalExpenditures}">
				<b>
				<span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalExpenditures" /></span> 
					${aimEditActivityForm.currCode}
				</b>
			</c:if>
			<c:if test="${empty aimEditActivityForm.funding.totalExpenditures}">
				<b>
					0
					${aimEditActivityForm.currCode}
				</b>
			</c:if>
			<hr/>
			</c:if>
			<c:if test="${aimEditActivityForm.funding.showActual}">
			<digi:trn>Unallocated Disbursements</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.unDisbursementsBalance}">
	                <b>
	                 <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.unDisbursementsBalance" /></span> 
	                 ${aimEditActivityForm.currCode}
	                </b>
		         </c:if>
		          <c:if test="${empty aimEditActivityForm.funding.unDisbursementsBalance}">
			         <b>
			         0
			         ${aimEditActivityForm.currCode}
			         </b>
		         </c:if>
			<hr/>
			</c:if>
			<c:if test="${aimEditActivityForm.funding.showPlanned}">
			<digi:trn>Total Planned Expenditures</digi:trn>:<br/>
				<c:if test="${not empty aimEditActivityForm.funding.totalPlannedExpenditures}">
	                <b>
	                 <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPlannedExpenditures" /></span> 
	                 ${aimEditActivityForm.currCode}
	                </b>		          
		         </c:if>
		         <c:if test="${empty aimEditActivityForm.funding.totalPlannedExpenditures}">
			         <b>
			         0
			         ${aimEditActivityForm.currCode}
			         </b>
		         </c:if>
			    <hr/>
			</c:if>
			</module:display>
			<c:if test="${not empty aimEditActivityForm.funding.totalMtefProjections}">
					<digi:trn>Total MTEF Projections</digi:trn>:<br/>
					<b>
		                 <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalMtefProjections" /></span> 
		                 ${aimEditActivityForm.currCode}
	            	</b>	
				<hr/>
			</c:if>
			<field:display name="Duration of Project" feature="Planning">
				<img src="../ampTemplate/images/help.gif" title="<digi:trn>Actual Completion Date - Actual Start Date</digi:trn>" width="10" height=10 border="0">
				<digi:trn>Duration of project</digi:trn>:
				<c:if test="${not empty aimEditActivityForm.planning.projectPeriod}">
                    <br/>
                    <b>${aimEditActivityForm.planning.projectPeriod }&nbsp; </b><digi:trn>Months</digi:trn>
				</c:if>
                <hr/>
			</field:display>
			<field:display name="Delivery rate" feature="Funding Information">
				<img src="../ampTemplate/images/help.gif" title="<digi:trn>Actual Disbursements / Actual Commitments * 100</digi:trn>" width="10" height=10 border="0">
				<digi:trn>Delivery rate</digi:trn>:<br/>
				<b> ${aimEditActivityForm.funding.deliveryRate}</b>
				<hr/>
			</field:display> 
	</fieldset>	
	<fieldset>
	<legend>
		<span class=legend_label><digi:trn>Additional info</digi:trn></span>	</legend>
	<div class="field_text_big word_break">
		<digi:trn>Activity created by</digi:trn>: <br/>
		<b>
			<c:out value="${aimEditActivityForm.identification.actAthFirstName}"/>
			<c:out value="${aimEditActivityForm.identification.actAthLastName}"/>
		</b>
		<hr/>
		<digi:trn>Activity created on</digi:trn>:<br/>
		<b><c:out value="${aimEditActivityForm.identification.createdDate}"/></b>
		<hr/>
		<module:display name="/Activity Form/Identification/Activity Last Updated by" parentModule="/Activity Form/Identification">
			<logic:notEmpty name="aimEditActivityForm" property="identification.modifiedBy">
				<digi:trn>Activity last updated by</digi:trn>: <br/>
				<b>
					<c:out value="${aimEditActivityForm.identification.modifiedBy.user.firstNames}"/>
					<c:out value="${aimEditActivityForm.identification.modifiedBy.user.lastName}"/>
				</b>
			</logic:notEmpty>
		</module:display>
		<hr/>
		<module:display name="/Activity Form/Identification/Activity Updated On" parentModule="/Activity Form/Identification">
			<logic:notEmpty name="aimEditActivityForm" property="identification.updatedDate">
				<digi:trn>Activity updated on</digi:trn>: <br/>
				<b><c:out value="${aimEditActivityForm.identification.updatedDate}"/></b>
			</logic:notEmpty>
		</module:display>
		<hr/>

		<digi:trn>Created in workspace</digi:trn>: <br/>
		<c:if test="${aimEditActivityForm.identification.team !=null}">
			<b>
				<c:out value="${aimEditActivityForm.identification.team.name}"/> -
				<digi:trn>
					<c:out value="${aimEditActivityForm.identification.team.accessType}"/>
				</digi:trn>
			</b>
		</c:if>
		<hr/>
		<field:display name="Data Team Leader" feature="Identification">
			<digi:trn>Workspace manager</digi:trn>: <br/>
			<b>
				<c:out value="${aimEditActivityForm.identification.team.teamLead.user.firstNames}"/>
				<c:out value="${aimEditActivityForm.identification.team.teamLead.user.lastName}"/> -
				<c:out value="${aimEditActivityForm.identification.team.teamLead.user.email}"/>
			</b>
		</field:display>

		<hr/>
		<digi:trn>Computation</digi:trn>: <br/>
		<b>
			<c:if test="${aimEditActivityForm.identification.team.computation == 'true'}">
				<digi:trn key="aim:yes">Yes</digi:trn>
			</c:if>
			<c:if test="${aimEditActivityForm.identification.team.computation == 'false'}">
				<digi:trn key="aim:no">No</digi:trn>
			</c:if>
		</b>
	
	</div>
</fieldset>	
	</div>	
	</td>
	<td width=15>&nbsp;</td>
    <td width=689 bgcolor="#F4F4F4" valign=top style="border:1px solid #DBDBDB">
	<div style="padding:10px; font-size:12px;">
    <%  if (! "true".equals(request.getParameter("popupView"))) {%>
    	<jsp:include page="activityPreviewButtons.jsp">
    		<jsp:param name="messages_on" value="true"  />
    	</jsp:include>  
        
    <%}%>

<!-- IDENTIFICATION SECTION -->
<jsp:include page="activitypreview/identificationSection.jsp" />

<!-- PROJECT INTERNAL IDS SECTION -->
<jsp:include page="activitypreview/internalIdsSection.jsp" />

<!-- PLANNING SECTION -->
<jsp:include page="activitypreview/planningSection.jsp" />

<!-- REFERENCES -->
<jsp:include page="activitypreview/referencesSection.jsp" />

<!--LOCATIONS SECTION-->
<jsp:include page="activitypreview/locationsSection.jsp" />

<!-- PROGRAM SECTION -->
<jsp:include page="activitypreview/programSection.jsp" />

<!-- SECTORS SECTION -->
<jsp:include page="activitypreview/sectorsSection.jsp" />


<!-- M & E  SECTION -->
<module:display name="M & E" parentModule="MONITORING AND EVALUATING">

	<fieldset>
		<legend>
			<span class=legend_label id="melink" style="cursor: pointer;">
				<digi:trn>M&E</digi:trn>
			</span>
		</legend>

		<!-- M & E  indicators list -->
		<div id="melistdiv" class="toggleDiv">
			<bean:define id="aimEditActivityForm" name="aimEditActivityForm" scope="page" toScope="request"/>
			<jsp:include page="previewIndicatosList.jsp"/>
		</div>
		<!-- END M & E  indicators list -->

		<div id="mediv" class="toggleDiv">
			<table>
				<field:display name="Activity Performance"  feature="Activity Dashboard">
					<tr>
						<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
							<digi:trn key="aim:level">Activity Performance</digi:trn>:
						</td>
						<td bgcolor="#ffffff">
							<% if (actPerfChartUrl != null) { %>
									<img src="<%= actPerfChartUrl %>" width="370" height="450" border="0" usemap="#<%= actPerfChartFileName %>"><br><br>
							<% } else { %>
									<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
								    <digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>
								    </span><br><br>
							<% } %>
						</td>
					</tr>
				</field:display>

				<field:display name="Activity Risk"  feature="Activity Dashboard">
					<tr>
						<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
							<digi:trn key="aim:level">Activity Risk</digi:trn>:
						</td>
						<td bgcolor="#ffffff">
							<% if (actRiskChartUrl != null) { %>
								<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn>:
								<font color="${riskColor}">

								<b><digi:trn key="<%=riskName%>"><%=riskName%></digi:trn></b>
								</font>
								<img src="<%= actRiskChartUrl %>" width="370" height="350" border="0" usemap="#<%= actRiskChartFileName %>">
								<br><br>
							<% } else { %>
								<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
						  	    <digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>
							    </span><br><br>
							<% } %>
						</td>
					</tr>
				</field:display>
			</table>
		</div>
	</fieldset>
</module:display>

<!-- END OF M & E  SECTION -->


<!-- FUNDING SECTION -->

<!-- PROPOSED PROJECT COST -->

<c:set var="costName" scope="request">Proposed Project Cost</c:set>
<c:set var="yearBudget" scope="request">${aimEditActivityForm.funding.proposedAnnualBudgets}</c:set>
<c:set var="projCost" scope="request">${aimEditActivityForm.funding.proProjCost}</c:set>
<c:set var="funAmount" scope="request">${aimEditActivityForm.funding.proProjCost == null ? null : aimEditActivityForm.funding.proProjCost.funAmount}</c:set>
<c:set var="currencyCode" scope="request">${aimEditActivityForm.funding.proProjCost == null ? null : aimEditActivityForm.funding.proProjCost.currencyCode}</c:set>
<c:set var="funDate" scope="request">${aimEditActivityForm.funding.proProjCost == null ? null : aimEditActivityForm.funding.proProjCost.funDate}</c:set>
<jsp:include page="projectCost.jsp"/>

<c:set var="costName" scope="request">Revised Project Cost</c:set>
<c:set var="yearBudget" scope="request"/>
<c:set var="projCost" scope="request">${aimEditActivityForm.funding.revProjCost}</c:set>
<c:set var="funAmount" scope="request">${aimEditActivityForm.funding.revProjCost == null ? null : aimEditActivityForm.funding.revProjCost.funAmount}</c:set>
<c:set var="currencyCode" scope="request">${aimEditActivityForm.funding.revProjCost == null ? null : aimEditActivityForm.funding.revProjCost.currencyCode}</c:set>
<c:set var="funDate" scope="request">${aimEditActivityForm.funding.revProjCost == null ? null : aimEditActivityForm.funding.revProjCost.funDate}</c:set>
<jsp:include page="projectCost.jsp"/>


<!-- END PROPOSED PROJECT COST -->

<!-- BUDGET STRUCTURE -->
<jsp:include page="activitypreview/budgetStructureSection.jsp" />

<!--Total Number of Funding Sources STRUCTURE		-->
<module:display name="/Activity Form/Funding/Overview Section/Total Number of Funding Sources"
				parentModule="/Activity Form/Funding/Overview Section">
	<fieldset>
		<legend>
		<span class=legend_label id="fundingSources" style="cursor: pointer;">
			<digi:trn>Total Number of Funding Sources</digi:trn>
		</span>
		</legend>
		<div class="toggleDiv">
			<table cellspacing="1" cellpadding="3" bgcolor="#aaaaaa" width="100%">
				<tr bgcolor="#f0f0f0">
					<td style="width: 40%">
						<digi:trn>Funding Sources</digi:trn>
					</td>
					<td bgcolor="#f0f0f0" align="left">
						<c:if test="${aimEditActivityForm.identification.fundingSourcesNumber!=null}">
							${aimEditActivityForm.identification.fundingSourcesNumber}
						</c:if>
					</td>
				</tr>
			</table>
		</div>
	</fieldset>
</module:display>
<!-- END Total Number of Funding Sources SECTION -->

	<module:display name="/Activity Form/Funding" parentModule="/Activity Form">
		<fieldset>
			<legend>
				<span class=legend_label id="fundinglink" style="cursor: pointer;">
					<digi:trn>Funding</digi:trn>
				</span>			
			</legend>
			<div id="fundingdiv" class="toggleDiv">
				<bean:define id="aimEditActivityForm" name="aimEditActivityForm" scope="page" toScope="request"/>
				<jsp:include page="activitypreview/previewActivityFunding.jsp"/>
			</div>
		</fieldset>
	</module:display>
	<!-- END FUNDING SECTION -->


	 <module:display name="/Activity Form/Aid Effectivenes" parentModule="/Activity Form">
        <fieldset>
            <legend>
                <span class=legend_label id="aidEffectivenesLink"
                    style="cursor: pointer;"> <digi:trn>Aid Effectivenes</digi:trn>
                </span>
            </legend>

            <div id="aidEffectivenesdiv" class="toggleDiv">

            <logic:notEmpty name="aimEditActivityForm" property="selectedEffectivenessIndicatorOptions">
                <logic:iterate id="option" name="aimEditActivityForm" property="selectedEffectivenessIndicatorOptions">
                    <module:display name="/Activity Form/Aid Effectivenes/${option.indicator.ampIndicatorName}"
                        parentModule="/Activity Form/Aid Effectivenes">
                        <div>
                            <span class="word_break bold">${fn:escapeXml(option.indicator.ampIndicatorName)}</span> -
                            <span class="word_break">${fn:escapeXml(option.ampIndicatorOptionName)}</span>
                        </div>
                    </module:display>
                </logic:iterate>
            </logic:notEmpty>
        </fieldset>
     </module:display>

<!-- REGIONAL FUNDING -->
<jsp:include page="activitypreview/regionalFundingSection.jsp" />

<!-- COMPONENTS -->
<jsp:include page="activitypreview/componentsSection.jsp" />

<!-- ISSUES SECTION -->
<jsp:include page="activitypreview/issuesSection.jsp" />

<!-- DOCUMENT SECTION -->
<jsp:include page="activitypreview/documentSection.jsp" />

<jsp:include page="previewActivityRegionalObservations.jsp"></jsp:include>
<jsp:include page="previewActivityLineMinistryObservations.jsp"></jsp:include>

<!-- RELATED ORGANIZATIONS SECTION -->
<jsp:include page="activitypreview/relatedOrganizationsSection.jsp" />

<!-- CONTACT INFORMATION -->
<c:set var="hideContactsForPublicUsers" scope="page" value="false"/>
		
<%if(!FeaturesUtil.isVisibleFeature("Contacts")){ %> 
	<c:set var="hideContactsForPublicUsers" scope="page" value="true"/>
<%}%>
		
<c:if test="${(sessionScope.currentMember != null) || (not hideContactsForPublicUsers)}">
	<module:display name="/Activity Form/Contacts" parentModule="/Activity Form">
		<fieldset>
			<legend>
				<span class=legend_label id="contactlink" style="cursor: pointer;">
					<digi:trn>Contact Information</digi:trn>
				</span>	
			</legend>
			<div id="contactdiv" class="toggleDiv">
				<module:display name="/Activity Form/Contacts/Donor Contact Information" parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.donorContacts}">
						<digi:trn>Donor funding contact information</digi:trn>:&nbsp;
						<c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.donorContacts}" scope="request" />
						<jsp:include page="activitypreview/contactInformation.jsp"/>
						<hr>
					</c:if> 
				</module:display>	
				<module:display name="/Activity Form/Contacts/Mofed Contact Information" parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.mofedContacts}">
						<digi:trn>MOFED contact information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.mofedContacts}" scope="request" />
						<jsp:include page="activitypreview/contactInformation.jsp"/>
						<hr>
					</c:if> 
				</module:display>
				
				<module:display name="/Activity Form/Contacts/Project Coordinator Contact Information" parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.projCoordinatorContacts}">
						<digi:trn>Project Coordinator Contact Information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.projCoordinatorContacts}" scope="request" />
						<jsp:include page="activitypreview/contactInformation.jsp"/>
						<hr>
					</c:if>
				</module:display>
						
				<module:display name="/Activity Form/Contacts/Sector Ministry Contact Information" parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.sectorMinistryContacts}">
						<digi:trn>Sector Ministry Contact Information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.sectorMinistryContacts}" scope="request" />
						<jsp:include page="activitypreview/contactInformation.jsp"/>
						<hr>
					</c:if> 
				</module:display>
							
				<module:display name="/Activity Form/Contacts/Implementing Executing Agency Contact Information"  parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.implExecutingAgencyContacts}">
						<digi:trn>Implementing/Executing Agency Contact Information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.implExecutingAgencyContacts}" scope="request" />
						<jsp:include page="activitypreview/contactInformation.jsp"/>
					</c:if>
				</module:display>
			</div>
		</fieldset>
	</module:display>
</c:if>
<!-- END CONTACT INFORMATION -->

<!-- IPA Contracting -->
<jsp:include page="activitypreview/ipaContractingSection.jsp" />

<!-- GPI -->
<jsp:include page="activitypreview/gpiSection.jsp" />

<jsp:include page="previewActivityStructures.jsp"/>
<br/>

<%  if (! "true".equals(request.getParameter("popupView"))) {%>
    <jsp:include page="activityPreviewButtons.jsp"/>
<%}%>

</div>
</td>
  </tr>
</table>
</c:if>
</div>
<module:display name="/Activity Form/Map Options/Show Map In Activity Preview" parentModule="/Activity Form/Map Options">
 <div id="locationPopupMap" style="visibility:hidden;width:4px; height:3px;position:relative;"></div>
</module:display>

<!-- MAIN CONTENT PART END -->
</digi:form>
<script language="JavaScript">
	$(document).ready(function(){
		expandAll();
		$('fieldset legend span.legend_label').click(function(){
			$(this).parent().siblings('.toggleDiv').toggle('slow');
		});
	});

	jQuery.fn.exists = function(){return this.length>0;}
	$("#collapseall").click(function() {
		var showOrHide;  
			  if($("#collapseall").attr('value')== '<digi:trn jsFriendly="true">Collapse All</digi:trn>'){ 
			  		$("#collapseall").attr('value','<digi:trn jsFriendly="true">Expand All</digi:trn>');
			  		$("#collapseall_1").attr('value','<digi:trn jsFriendly="true">Expand All</digi:trn>');
			  		showOrHide=false;
		  	  }else{
			  		$("#collapseall").attr('value','<digi:trn jsFriendly="true">Collapse All</digi:trn>');
			  		$("#collapseall_1").attr('value','<digi:trn jsFriendly="true">Collapse All</digi:trn>');
			  		showOrHide=true;
		  	  }
		  	$(".toggleDiv").toggle(showOrHide);
		  
	});
	
	//associating the click directly to the id was not working. So I associated it with the button
	//and the check for the correct id.
	$( "button, input[type='button']" ).click (function (event) {
		if (event.target.id=='collapseall_1') {
			var showOrHide;  
			  if($("#collapseall_1").attr('value')== '<digi:trn jsFriendly="true">Collapse All</digi:trn>'){ 
			  		$("#collapseall_1").attr('value','<digi:trn jsFriendly="true">Expand All</digi:trn>');
			  		$("#collapseall").attr('value','<digi:trn jsFriendly="true">Expand All</digi:trn>');
			  		$(event.target).attr('value','<digi:trn jsFriendly="true">Expand All</digi:trn>');
			  		showOrHide=false;
		  	  }else{
			  		$("#collapseall_1").attr('value','<digi:trn jsFriendly="true">Collapse All</digi:trn>');
			  		$("#collapseall").attr('value','<digi:trn jsFriendly="true">Collapse All</digi:trn>');
			  		$(event.target).attr('value','<digi:trn jsFriendly="true">Collapse All</digi:trn>');
			  		showOrHide=true;
		  	  }
			  $(".toggleDiv").toggle(showOrHide);
			  document.body.scrollTop = document.documentElement.scrollTop = 0;	
		}
	});
	
	//Change map URLs to popin
	var chartAreas = $("map[name='<%= actPerfChartFileName %>']").find("area");
	if (chartAreas != null) {
		$.each(chartAreas, function (idx, val) {
				//console.log (val.href);
				val.href = "javascript:showIndicatorValsPopin(\"" + encodeURIComponent(val.href) + "\")";
				//val.href = "javascript:showIndicatorValsPopin(\"" + val.href + "\")";
			});
	}
	
	var chartAreasRisk = $("map[name='<%= actRiskChartFileName %>']").find("area");
	if (chartAreasRisk != null) {
		$.each(chartAreasRisk, function (idx, val) {
				//console.log (val.href);
				val.href = "javascript:showIndicatorValsPopin(\"" + encodeURIComponent(val.href) + "\")";
			});
	}
	
	
	//Init indicator popin
	var msg="\n<digi:trn>Indicator values</digi:trn>";
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.render(document.body);	
	
	
	function showIndicatorValsPopin (url) {
		YAHOOAmp.util.Connect.asyncRequest("GET", url, callback);
	}
	
	var responseSuccess = function(o){
		var response = o.responseText; 
		myPanel.setBody(response);
		myPanel.show();
	}

	var responseFailure = function(o){ 
		alert("Connection Failure!"); 
	}
	
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure
	}; 
	
	
	$("#mapPreviewThumbnail").click(function (obj) {
		$("#zoomMapContainer").css("left", obj.pageX - 250 + "px").css("top", obj.pageY - 250 + "px").css("display", "block");
	});
	
	
	
</script>