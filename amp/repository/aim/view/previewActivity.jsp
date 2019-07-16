<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.digijava.module.aim.helper.*"%>
<%@ page import="org.digijava.module.aim.helper.ChartGenerator"%>
<%@ page import="org.digijava.module.aim.util.IndicatorUtil"%>
<%@ page import="java.io.PrintWriter,java.util.*"%>
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

<jsp:include page="activityHistoryUtil.jsp" flush="true" />
<%@page import="java.math.BigDecimal"%>
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
<module:display name="/Activity Form/Identification" parentModule="/Activity Form">
	<fieldset>
		<legend>
			<span class=legend_label id="identificationlink" style="cursor: pointer;">
				<digi:trn>Identification</digi:trn>
			</span>		
		</legend>
		<div id="identificationdiv" class="toggleDiv">
			<module:display name="/Activity Form/Identification/Project Title" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:projectTitle">Project title</digi:trn>:&nbsp;<br />
				<span class="word_break bold"><c:out value="${aimEditActivityForm.identification.title}"/></span>
				<hr />
			</module:display>
			
			<digi:trn key="aim:ampId">AMP ID</digi:trn>:&nbsp;<br /><b><c:out value="${aimEditActivityForm.identification.ampId}"/></b> <br>
			<hr />	

			<module:display name="/Activity Form/Identification/Activity Status" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:status">Status</digi:trn>:&nbsp;
				<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.statusId}"/></b>
				<hr />
			</module:display>
			<module:display name="/Activity Form/Identification/Status Other Info"
							parentModule="/Activity Form/Identification">
				<c:if test="${not empty aimEditActivityForm.identification.statusOtherInfo}">
					<digi:trn>Status Other Info</digi:trn>:&nbsp;<br />
					<span class="word_break bold"><c:out value="${aimEditActivityForm.identification.statusOtherInfo}"/></span>
					<hr />
				</c:if>
			</module:display>

			<module:display name="/Activity Form/Identification/Status Reason" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:statusReason">Status Reason</digi:trn>:&nbsp;
				<c:if test="${not empty aimEditActivityForm.identification.statusReason}">
					<c:set var="projstatusReason" value="${aimEditActivityForm.identification.statusReason}"/>
					<span class="word_break bold"><digi:edit key="${projstatusReason}"></digi:edit></span>
				</c:if>
				<hr />
			</module:display>

			<module:display name="/Activity Form/Funding/Overview Section/Type of Cooperation"
			    parentModule="/Activity Form/Funding/Overview Section">
				<c:if test="${not empty aimEditActivityForm.identification.ssc_typeOfCooperation}">
				<digi:trn>Type of Cooperation</digi:trn>:&nbsp;<br />
				<b><c:out value="${aimEditActivityForm.identification.ssc_typeOfCooperation}"/></b>
				<hr />
				</c:if>
			</module:display>

			<module:display name="/Activity Form/Funding/Overview Section/Type of Implementation"
			    parentModule="/Activity Form/Funding/Overview Section">
				<digi:trn>Type of Implementation</digi:trn>:&nbsp;<br />
				<b><c:out value="${aimEditActivityForm.identification.ssc_typeOfImplementation}"/></b>
				<hr />
			</module:display>

			<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/Modalities"/>
			<c:if test="${aimEditActivityForm.identification.team !=null && aimEditActivityForm.identification.team.isSSCWorkspace()}">
				<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/SSC Modalities"/>
			</c:if>

			<module:display name="${modalitiesPath}"
				parentModule="/Activity Form/Funding/Overview Section">
				<digi:trn>Modalities</digi:trn>:&nbsp;<br />
				<c:if test="${not empty aimEditActivityForm.identification.ssc_modalities}">				
				<b>
				<c:forEach var="modality" items="${aimEditActivityForm.identification.ssc_modalities}">
					<span class="word_break">${modality}</span><br/>
				</c:forEach>
				</b>
				</c:if>
				<hr />
			</module:display>


			<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/Modalities Other Info"/>
			<c:if test="${aimEditActivityForm.identification.team !=null && aimEditActivityForm.identification.team.isSSCWorkspace()}">
				<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/SSC Modalities Other Info"/>
			</c:if>

			<module:display name="${modalitiesPath}"
							parentModule="/Activity Form/Funding/Overview Section">
				<c:if test="${not empty aimEditActivityForm.identification.modalitiesOtherInfo}">
					<digi:trn>Modalities Other Info</digi:trn>:&nbsp;<br />
					<span class="word_break bold"><c:out value="${aimEditActivityForm.identification.modalitiesOtherInfo}"/></span>
					<hr />
				</c:if>
			</module:display>

			
			<module:display name="/Activity Form/Identification/Objective" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:objectives">Objectives</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.objectives!=null}">
					<c:set var="objKey" value="${aimEditActivityForm.identification.objectives}"/>
					<span class="word_break bold"><digi:edit key="${objKey}"></digi:edit></span>
				</c:if>
					<hr />
			</module:display>
			
			<module:display name="/Activity Form/Identification/Objective Comments" parentModule="/Activity Form/Identification">
				<logic:present name="currentMember" scope="session">
					<digi:trn key="aim:objectiveComments">Objective Comments</digi:trn>:&nbsp;
						<ul>
						<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
							<module:display name="/Activity Form/Identification/Objective Comments/Objective Assumption" parentModule="/Activity Form/Identification/Objective Comments">
								<logic:equal name="comments" property="key" value="Objective Assumption">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<digi:trn>Objective Assumption</digi:trn>:&nbsp;<br />								
											<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
											<br />
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>	
							<module:display name="/Activity Form/Identification/Objective Comments/Objective Verification" parentModule="/Activity Form/Identification/Objective Comments">
								<logic:equal name="comments" property="key" value="Objective Verification">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<digi:trn>Objective Verification</digi:trn>:&nbsp;	<br />								
											<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>							
							<module:display name="/Activity Form/Identification/Objective Comments/Objective Objectively Verifiable Indicators" 
								parentModule="/Activity Form/Identification/Objective Comments">
								<logic:equal name="comments" property="key" value="Objective Objectively Verifiable Indicators">
									<logic:iterate name="comments" id="comment" property="value" 
										type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<digi:trn>Objective Objectively Verifiable Indicators</digi:trn>:&nbsp;	<br />									
											<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
											<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>
						</logic:iterate>
						</ul>
						<hr/>
					</logic:present>
			</module:display>
			
			<module:display name="/Activity Form/Identification/Description" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:description">Description</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.description!=null}">
					<c:set var="descKey" value="${aimEditActivityForm.identification.description}"/>
					<span class="word_break bold"><digi:edit key="${descKey}"></digi:edit></span>
				</c:if>
					<hr />
			</module:display>
			
			<module:display name="/Activity Form/Identification/Project Comments" parentModule="/Activity Form/Identification">
				<digi:trn>Project Comments</digi:trn>:&nbsp;<br />
				<c:if test="${aimEditActivityForm.identification.projectComments!=null}">
					<c:set var="projcomKey" value="${aimEditActivityForm.identification.projectComments}"/>
					<span class="word_break bold"><digi:edit key="${projcomKey}"></digi:edit></span>
				</c:if>
					<hr />
			</module:display>
					
			
			<module:display name="/Activity Form/Identification/Lessons Learned" parentModule="/Activity Form/Identification">
				<digi:trn>Lessons Learned</digi:trn>:&nbsp;<br />
				<c:if test="${not empty aimEditActivityForm.identification.lessonsLearned}">
					<bean:define id="lessonsLearnedKey">
						<c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
					</bean:define>
					<span class="word_break bold"><digi:edit key="${lessonsLearnedKey}"></digi:edit></span>
				</c:if>
					<hr />
			</module:display>

			<bean:define id="largeTextFeature" value="Identification" toScope="request"/>
			<logic:present name="aimEditActivityForm" property="identification.projectImpact">
				<bean:define id="moduleName" value="/Activity Form/Identification/Project Impact" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.projectImpact}"/>
				</bean:define>
				<span class="word_break">
					<jsp:include page="largeTextPropertyView.jsp" />
				</span>
			</logic:present>
			
		 	<logic:present name="aimEditActivityForm" property="identification.activitySummary"> 
				<bean:define id="moduleName" value="/Activity Form/Identification/Activity Summary" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.activitySummary}"/>
				</bean:define>
				<span class="word_break">
					<jsp:include page="largeTextPropertyView.jsp" />
				</span>
		 	</logic:present> 
			<logic:present name="aimEditActivityForm" property="identification.conditionality">
				<bean:define id="moduleName" value="/Activity Form/Identification/Conditionalities" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.conditionality}"/>
				</bean:define>
				<span class="word_break">
					<jsp:include page="largeTextPropertyView.jsp" />
				</span>
			</logic:present>
			<logic:present name="aimEditActivityForm" property="identification.projectManagement">
				<bean:define id="moduleName" value="/Activity Form/Identification/Project Management" toScope="request"/>
				<bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
				<bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
				<bean:define id="largeTextKey" toScope="request">
					<c:out value="${aimEditActivityForm.identification.projectManagement}"/>
				</bean:define>
				<span class="word_break">
					<jsp:include page="largeTextPropertyView.jsp" />
				</span>
			</logic:present>
			<module:display name="/Activity Form/Identification/Purpose" parentModule="/Activity Form/Identification">
				<digi:trn >Purpose</digi:trn>:<br />
				<c:if test="${aimEditActivityForm.identification.purpose!=null}">
					<c:set var="objKey" value="${aimEditActivityForm.identification.purpose}"/>
					<span class="word_break bold"><digi:edit key="${objKey}"></digi:edit></span>
				</c:if>
				<hr/>
			</module:display>
			<module:display name="/Activity Form/Identification/Purpose Comments" parentModule="/Activity Form/Identification">
				<logic:present name="aimEditActivityForm" property="comments.allComments">
					<digi:trn>Purpose Comments</digi:trn>&nbsp;
					<ul>
					<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
						<module:display name="/Activity Form/Identification/Purpose Comments/Purpose Assumption" parentModule="/Activity Form/Identification/Purpose Comments">
						<logic:equal name="comments" property="key" value="Purpose Assumption">
							<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
								<li>
									<digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:<br />
									<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
									<br />
								</li>
							</logic:iterate>
						</logic:equal>
						</module:display>
						<module:display name="/Activity Form/Identification/Purpose Comments/Purpose Verification" parentModule="/Activity Form/Identification/Purpose Comments">
						<logic:equal name="comments" property="key" value="Purpose Verification">
							<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
								<li>
									<digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:<br />
									<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
									<br/>
								</li>
							</logic:iterate>
						</logic:equal>
						</module:display>
						<module:display name="/Activity Form/Identification/Purpose Comments/Purpose Objectively Verifiable Indicators" parentModule="/Activity Form/Identification/Purpose Comments">
						<logic:equal name="comments" property="key" value="Purpose Objectively Verifiable Indicators">
							<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
								<li>
									<digi:trn key="aim:purposeObjectivelyVerifiableIndicators">Purpose Objectively Verifiable Indicators</digi:trn>:<br />
									<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
									<br/>
								</li>
							</logic:iterate>
						</logic:equal>
						</module:display>
					</logic:iterate>
					</ul> 
					<hr/>
				</logic:present>
			</module:display>
					
			<module:display name="/Activity Form/Identification/Results" parentModule="/Activity Form/Identification">
				<digi:trn key="aim:results">Results</digi:trn>:&nbsp;<br />
					<c:if test="${aimEditActivityForm.identification.results!=null}">
						<c:set var="objKey" value="${aimEditActivityForm.identification.results}"/>
						<span class="word_break bold"><digi:edit key="${objKey}"></digi:edit></b></span>
						<hr>
					</c:if> 
			</module:display>					
				<logic:present name="aimEditActivityForm" property="comments.allComments">
					<module:display name="/Activity Form/Identification/Results Comments" parentModule="/Activity Form/Identification">
						<digi:trn>Results Comments</digi:trn>
						<ul>
						<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
							<module:display name="/Activity Form/Identification/Results Comments/Results Assumption" parentModule="/Activity Form/Identification/Results Comments">
								<logic:equal name="comments" property="key" value="Results Assumption">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
										<digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:<br />
										<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>
							<module:display name="/Activity Form/Identification/Results Comments/Results Verification" parentModule="/Activity Form/Identification/Results Comments">							
								<logic:equal name="comments" property="key" value="Results Verification">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
										<digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:<br />
										<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>
							<module:display name="/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators" parentModule="/Activity Form/Identification/Results Comments">							
								<logic:equal name="comments" property="key" value="Results Objectively Verifiable Indicators">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										 <li>
											<digi:trn key="aim:resultsObjectivelyVerifiableIndicators">Results Objectively Verifiable Indicators</digi:trn>:<br />
										<span class="word_break bold"><bean:write name="comment" property="comment" /></span>
										<br/>
										</li>
									</logic:iterate>
								</logic:equal>
							</module:display>								
						</logic:iterate>
						</ul>
					</module:display>
					<hr/>
				</logic:present>
			
			
			
			<module:display name="/Activity Form/Identification/Accession Instrument" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.accessionInstrument > 0}">
				<digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>:&nbsp;<br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.accessionInstrument}"/>
					</span>
					<hr />
					</c:if>
			</module:display>
			
			<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
			<module:display name="/Activity Form/Identification/Project Implementing Unit" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.projectImplUnitId > 0}">			
				<digi:trn>Project Implementing Unit</digi:trn><br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectImplUnitId}"/>
					</span>
				</c:if> 
				<hr/>
			</module:display>
					 
			<module:display name="/Activity Form/Identification/A.C. Chapter" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.acChapter > 0}">
				<digi:trn>A.C. Chapter</digi:trn><br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.acChapter}"/>
					</span>
					<hr />
				</c:if>					
			</module:display>
					 
			<module:display name="/Activity Form/Identification/Cris Number" parentModule="/Activity Form/Identification">
				<digi:trn>Cris Number</digi:trn>:&nbsp;<br />
					<span class="word_break bold">
						<c:out value="${aimEditActivityForm.identification.crisNumber}"/>
					</span>
					<hr />
			</module:display>
					
			<module:display name="/Activity Form/Identification/Procurement System" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.procurementSystem > 0}">
				<digi:trn>Procurement System</digi:trn><br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.procurementSystem}"/>
					</span>
				</c:if>
					<hr />
			</module:display>
								
			<module:display name="/Activity Form/Identification/Reporting System" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.reportingSystem > 0}">
				<digi:trn>Reporting System</digi:trn>:&nbsp;<br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.reportingSystem}"/>
					</span>
					<hr />
					</c:if>
			</module:display>
								
			<module:display name="/Activity Form/Identification/Audit System" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.auditSystem > 0}">
				<digi:trn>Audit System</digi:trn>:&nbsp;<br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.auditSystem}"/>
					</span>
				</c:if> 				
					<hr />	
			</module:display>
				
			<module:display name="/Activity Form/Identification/Institutions" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.institutions > 0}">
				<digi:trn>Institutions</digi:trn>:&nbsp;<br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.institutions}"/>
					</span>
					<hr />
				</c:if>
			</module:display>
			<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
			<module:display name="/Activity Form/Identification/Project Category" parentModule="/Activity Form/Identification">
				<c:if test="${aimEditActivityForm.identification.projectCategory > 0}">
				<digi:trn>Project Category</digi:trn><br />
					<span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectCategory}"/>
					</span>
					<hr />
					</c:if>	
			</module:display>
			<module:display name="/Activity Form/Identification/Project Category Other Info"
							parentModule="/Activity Form/Identification">
				<c:if test="${not empty aimEditActivityForm.identification.projectCategoryOtherInfo}">
					<digi:trn>Project Category Other Info</digi:trn>:&nbsp;<br />
					<span class="word_break bold"><c:out value="${aimEditActivityForm.identification.projectCategoryOtherInfo}"/></span>
					<hr />
				</c:if>
			</module:display>
						 
			<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
			<module:display name="/Activity Form/Identification/Government Agreement Number" parentModule="/Activity Form/Identification">
				<c:if test="${not empty aimEditActivityForm.identification.govAgreementNumber}">
				<digi:trn>Government Agreement Number</digi:trn>:&nbsp;<br />
				<span class="word_break bold"> <c:out value="${aimEditActivityForm.identification.govAgreementNumber}"/>
				</span><hr />
				</c:if>
				</module:display>
	</module:display>
	<!-- END IDENTIFICATION SECTION -->
	<!-- BUDGET SECTION -->
	<!-- MISSING FIELD IN THE NEW ACTIVITY FORM -->
	
	
	<module:display name="/Activity Form/Identification/Activity Budget" parentModule="/Activity Form/Identification">
		<b><digi:trn>Budget</digi:trn></b><br/>
		<c:choose>
			<c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">
				<digi:trn>Activity is On Budget</digi:trn><br />
			</c:when>
			<c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOff}">
				<digi:trn>Activity is Off Budget</digi:trn><br />
			</c:when>
			<c:when test="${aimEditActivityForm.identification.budgetCV==0}">
				<digi:trn>Budget Unallocated</digi:trn><br />
			</c:when>
			<c:otherwise>
				<digi:trn>Activity is On</digi:trn>
				<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.budgetCV}"/></b>
			</c:otherwise>
		</c:choose>
		<hr/>
		
		<c:if test="${!empty aimEditActivityForm.identification.chapterForPreview}" >
			<digi:trn>Code Chapitre</digi:trn>:<br />
			<span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.chapterForPreview.code" /> - 
			<bean:write name="aimEditActivityForm" property="identification.chapterForPreview.description"/></span>]
			<digi:trn>Imputations</digi:trn>:<br />
			<logic:iterate id="imputation" name="aimEditActivityForm" property="identification.chapterForPreview.imputations">
				<span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.chapterForPreview.year"/> - 
				<bean:write name="imputation" property="code" /> - 
				<bean:write name="imputation" property="description" /></span>
				<hr>
			</logic:iterate>
		</c:if>
		</module:display>
			<c:if test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">
				<module:display name="/Activity Form/Identification/Budget Extras/FY" parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>FY</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.FY"/></b>
					<br />
				</module:display>
				<module:display name="/Activity Form/Identification/Budget Extras/Ministry Code"  parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Ministry Code</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.ministryCode"/></b>
					<br />
				</module:display>
				
				<module:display name="/Activity Form/Identification/Budget Extras/Project Code" parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Project Code</digi:trn>:&nbsp;
					<b><bean:write name="aimEditActivityForm" property="identification.projectCode"/></b>
					<br />
				</module:display>
				
				<module:display name="/Activity Form/Identification/Budget Extras/Vote"  parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Vote</digi:trn>:&nbsp;
					<span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.vote"/></span>
					<br />
				</module:display>
					<module:display name="/Activity Form/Identification/Budget Extras/Sub-Vote"  parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Sub-Vote </digi:trn>:&nbsp;
					<span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.subVote"/></span>
					<br />
				</module:display>
				<module:display name="/Activity Form/Identification/Budget Extras/Sub-Program" parentModule="/Activity Form/Identification/Budget Extras">
					<digi:trn>Sub-Program</digi:trn>:&nbsp;
					<span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.subProgram"/></span>
					<br />
				</module:display>
				
			</c:if>

		<hr>	
		<module:display name="/Activity Form/Identification/Budget Classification" parentModule="/Activity Form/Identification">
			<c:if test="${!empty aimEditActivityForm.identification.selectedbudgedsector}">
			<digi:trn>Budget Classification</digi:trn>:<br />
				<c:forEach var="selectedsector" items="${aimEditActivityForm.identification.budgetsectors}">
					<c:if test="${aimEditActivityForm.identification.selectedbudgedsector==selectedsector.idsector}">
						<li style="margin-left: 10px">
							<span class="word_break bold">
								<c:out value="${selectedsector.code}" /> - <c:out value="${selectedsector.sectorname}" />
							</span>
						</li>
					</c:if>
				</c:forEach>
				<br>
			</c:if>
			
			<c:if test="${!empty aimEditActivityForm.identification.selectedorg}">
				<c:forEach var="selectedorgs" items="${aimEditActivityForm.identification.budgetorgs}">
					<c:if test="${aimEditActivityForm.identification.selectedorg==selectedorgs.ampOrgId}">
						<li style="margin-left: 10px"><c:out value="${selectedorgs.budgetOrgCode}"/> - 
							<span class="word_break bold"><c:out value="${selectedorgs.name}" /></span>
						</li>
					</c:if>
				</c:forEach>
				<br>
			</c:if>
			
			<c:if test="${!empty aimEditActivityForm.identification.selecteddepartment}">
				<c:forEach var="selecteddep" items="${aimEditActivityForm.identification.budgetdepartments}">
					<c:if test="${aimEditActivityForm.identification.selecteddepartment==selecteddep.id}">
						<li style="margin-left: 10px">
							<span class="word_break bold"><c:out value="${selecteddep.code}"/> - 
							<c:out value="${selecteddep.name}"/></span>
						</li>
					</c:if>
				</c:forEach>
				<br>
			</c:if>
			
			<c:if test="${!empty aimEditActivityForm.identification.selectedprogram}" >
				<c:forEach var="selectedprog" items="${aimEditActivityForm.identification.budgetprograms}">
					<c:if test="${aimEditActivityForm.identification.selectedprogram==selectedprog.ampThemeId}">
						<li style="margin-left: 10px">
							<span class="word_break bold">
								<c:out value="${selectedprog.themeCode}"/> - <c:out value="${selectedprog.name}" />
							</span>
						</li>
					</c:if>
				</c:forEach>
			</c:if>
			<hr/>
		</module:display>
	
	<!-- END BUDGET SECTION -->
	
	<!-- INDETIFICATION SECTION 2 -->
	<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
	<field:display feature="Identification" name="Organizations and Project ID">
		<digi:trn>Organizations and Project IDs</digi:trn>:&nbsp;<br />
		<c:if test="${!empty aimEditActivityForm.identification.selectedOrganizations}">
			<table cellSpacing=2 cellPadding=2 border="0">
				<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.identification.selectedOrganizations}">
					<c:if test="${not empty selectedOrganizations}">
						<tr>
							<td>
								<c:if test="${!empty selectedOrganizations.organisation.ampOrgId}">
									<bean:define id="selectedOrgForPopup" 
										name="selectedOrganizations" 
										type="org.digijava.module.aim.helper.OrgProjectId"
										toScope="request"/>
										<jsp:include page="previewOrganizationPopup.jsp" />
								</c:if>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table><hr />
		</c:if> 
	</field:display>
	
	<module:display name="/Activity Form/Identification/Government Approval Procedures" parentModule="/Activity Form/Identification">
		<c:if test="${aimEditActivityForm.identification.governmentApprovalProcedures!=null}">
			<digi:trn>Government Approval Procedures</digi:trn>:&nbsp;<br />
			<c:if test="${aimEditActivityForm.identification.governmentApprovalProcedures==false}">
				<b><digi:trn key="aim:no">No</digi:trn></b>
			</c:if> 
			<c:if test="${aimEditActivityForm.identification.governmentApprovalProcedures==true}">
				<b><digi:trn key="aim:yes">Yes</digi:trn></b>
			</c:if>
			<hr/>
		</c:if>
	</module:display>
	<module:display name="/Activity Form/Identification/Joint Criteria" parentModule="/Activity Form/Identification">
		<c:if test="${aimEditActivityForm.identification.jointCriteria!=null}">
			<digi:trn>Joint Criteria</digi:trn>:&nbsp;<br />
			<c:if test="${aimEditActivityForm.identification.jointCriteria==false}">
				<b><digi:trn key="aim:no">No</digi:trn></b>
			</c:if>
			<c:if test="${aimEditActivityForm.identification.jointCriteria==true}">
				<b><digi:trn key="aim:yes">Yes</digi:trn></b>
			</c:if>
			<hr/>
		</c:if>
	</module:display>
	<module:display name="/Activity Form/Identification/Humanitarian Aid" parentModule="/Activity Form/Identification">
		<c:if test="${aimEditActivityForm.identification.humanitarianAid!=null}">
			<digi:trn>Humanitarian Aid</digi:trn>:&nbsp;<br />
			<c:if test="${aimEditActivityForm.identification.humanitarianAid==false}">
				<b><digi:trn key="aim:no">No</digi:trn></b>
			</c:if> 
			<c:if test="${aimEditActivityForm.identification.humanitarianAid==true}">
				<b><digi:trn key="aim:yes">Yes</digi:trn></b>
			</c:if>
			<hr/>
		</c:if>
	</module:display>
	<!-- END INDETIFICATION SECTION 2 -->
	</div>
</fieldset>

<!-- PROJECT INTERNAL IDS SECTION -->
<module:display name="/Activity Form/Activity Internal IDs" parentModule="/Activity Form">
	<fieldset>
		<legend>
			<span class=legend_label id="internallink" style="cursor: pointer;">
				<digi:trn>Agency Internal IDs</digi:trn>
			</span>
		</legend>
		<div id="internaldiv" class="toggleDiv">
			<c:if test="${!empty aimEditActivityForm.internalIds}">
				<c:forEach var="internalObj" items="${aimEditActivityForm.internalIds}">
					<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
						<tr>
							<td width="85%">
								<b>[${internalObj.organisation.name}]</b>
							</td>
							<td width="15%" align="right" valign=top>
								<module:display name="/Activity Form/Activity Internal IDs/Internal IDs/internalId" parentModule="/Activity Form">
									<b><c:out value="${internalObj.internalId}"/></b>
								</module:display>
							</td>
						</tr>
					</table>
					<hr/>
				</c:forEach>
			</c:if>
		</div>
	</fieldset>
</module:display>
<!-- END PROJECT INTERNAL IDS SECTION -->

<!-- PLANNING SECTION -->
<module:display name="/Activity Form/Planning" parentModule="/Activity Form">	
<fieldset>
	<legend>
		<span class=legend_label id="planninglink" style="cursor: pointer;">
			<digi:trn>Planning</digi:trn>
		</span>
		</legend>
	<div id="planningdiv" class="toggleDiv">
		<module:display name="/Activity Form/Planning/Line Ministry Rank" parentModule="/Activity Form/Planning">
			<br>
			<div class="planning-line"><digi:trn>Line Ministry Rank</digi:trn>:&nbsp;</div>
				<c:if test="${aimEditActivityForm.planning.lineMinRank == -1}"></c:if> 
				<c:if test="${aimEditActivityForm.planning.lineMinRank != -1}">
					<div class="planning-line"><b>${aimEditActivityForm.planning.lineMinRank}</b></div>				</c:if>
		</module:display>
		
		
		<module:display name="/Activity Form/Planning/Proposed Approval Date" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Proposed Approval Date</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.originalAppDate}</b></div>
		</module:display>
		
		<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
		<module:display name="/Activity Form/Planning/Actual Approval Date" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Actual Approval Date</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.revisedAppDate}</b></div>
		</module:display>

		<module:display name="/Activity Form/Planning/Proposed Start Date" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Proposed Start Date</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.originalStartDate}</b></div>
		</module:display>
							
		<module:display name="/Activity Form/Planning/Actual Start Date" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Actual Start Date </digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.revisedStartDate}</b></div>
		</module:display>
		
		<module:display name="/Activity Form/Planning/Original Completion Date" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Original Completion Date</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.originalCompDate}</b></div>
		</module:display>
		
		<module:display name="/Activity Form/Planning/Proposed Completion Date" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Proposed Completion Date</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.proposedCompDate}</b></div>
		</module:display>
		<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
			<feature:display name="Planning" module="Project ID and Planning">
				<field:display name="Final Date for Disbursements Comments" feature="Planning">
					<hr>
					<div class="planning-line"><digi:trn>Final Date for Disbursements comments</digi:trn>:&nbsp;</div>
					<div class="planning-line">
					<ul>
						<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
								<logic:equal name="comments" property="key" value="Final Date for Disbursements">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<span class="word_break ">
												<bean:write name="comment" property="comment" />
											</span>
											<br />
										</li>
									</logic:iterate>
								</logic:equal>
						</logic:iterate>
					</ul>
					</div>
				</field:display>
			</feature:display>
		</module:display>
		
		<module:display name="/Activity Form/Planning/Actual Completion Date" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Actual Completion Date</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b><c:out value="${aimEditActivityForm.planning.currentCompDate}"/></b></div>
		</module:display> 
							
		<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
			<feature:display name="Planning" module="Project ID and Planning">
				<field:display name="Current Completion Date Comments" feature="Planning">
					<hr>
					<div class="planning-line"><digi:trn>Current Completion Date comments</digi:trn>:&nbsp;</div>
					<div class="planning-line">
					<ul>
						<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
								<logic:equal name="comments" property="key" value="current completion date">
									<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
										<li>
											<span class="word_break ">
												<bean:write name="comment" property="comment" />
											</span>
											<br />
										</li>
									</logic:iterate>
								</logic:equal>
						</logic:iterate>
					</ul>
					</div>
				</field:display>
			</feature:display>
		</module:display>
		
		<module:display name="/Activity Form/Planning/Project Implementation Delay" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Project Implementation Delay</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.projectImplementationDelay}</b></div>
		</module:display>
							
		<module:display name="/Activity Form/Planning/Final Date for Contracting" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Final Date for Contracting</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b><c:out value="${aimEditActivityForm.planning.contractingDate}"/></b></div>
		</module:display>
					
		<module:display name="/Activity Form/Planning/Final Date for Disbursements" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Final Date for Disbursements</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b><c:out value="${aimEditActivityForm.planning.disbursementsDate}"/></b></div>
		</module:display>

		<module:display name="/Activity Form/Planning/Proposed Project Life" parentModule="/Activity Form/Planning">
			<hr>
			<div class="planning-line"><digi:trn>Proposed Project Life</digi:trn>:&nbsp;</div>
			<div class="planning-line"><b>${aimEditActivityForm.planning.proposedProjectLife}</b></div>
		</module:display>

		<field:display name="Duration of Project" feature="Planning">
			<hr>
			<div class="planning-line"><digi:trn>Duration of project</digi:trn>:&nbsp;</div>
			<c:if test="${not empty aimEditActivityForm.planning.projectPeriod}">
				<div class="planning-line">
					<b>${aimEditActivityForm.planning.projectPeriod}</b>&nbsp;<digi:trn>Months</digi:trn>
				</div>
			</c:if>
		</field:display>
		</div>
</fieldset>
</module:display>

<!-- REFERENCES -->
<module:display name="References" parentModule="PROJECT MANAGEMENT">	
<fieldset>
	<legend>
		<span class=legend_label id="referenceslink" style="cursor: pointer;">
			<digi:trn>References</digi:trn>
		</span>
	</legend>
	<div id="referencesdiv" class="toggleDiv">
	<ul>
		<logic:notEmpty name="aimEditActivityForm" property="documents.referenceDocs">
			<logic:iterate name="aimEditActivityForm" property="documents.referenceDocs" id="doc">
				<li>
				<span class="word_break">
					<bean:write name="doc" property="categoryValue"/>
				</span>
				</li>
			</logic:iterate>
		</logic:notEmpty>
	</ul>
	</div>
</fieldset>
</module:display>



<!--LOCATIONS SECTION-->
<module:display name="/Activity Form/Location" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="locationlink" style="cursor: pointer;">
			<digi:trn>Location</digi:trn></span>	
		</legend>
		<div id="locationdiv" class="toggleDiv">
		<module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
			<c:if test="${!empty aimEditActivityForm.location.selectedLocs}">
				<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
					<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
					<tr>
						<td width="85%">
							<c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
								<b>[${ancestorLoc}]</b>
							</c:forEach>
						</td>
						<td width="15%" align="right" valign=top>
							<field:display name="Regional Percentage" feature="Location">
								<c:if test="${selectedLocs.showPercent}">
									<b><c:out value="${selectedLocs.percent}"/> %</b>
								</c:if>
							</field:display>
						</td>
					</tr>
					</table>
					<hr/>
				</c:forEach>
				<module:display name="/Activity Form/Map Options/Show Map In Activity Preview" parentModule="/Activity Form/Map Options">
					<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
					<tr> <td colspan="2">
						<script type="text/javascript">
						<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
							coordinates.push('<c:out value="${selectedLocs.lat}"/>;<c:out value="${selectedLocs.lon}"/>;<c:out value="${selectedLocs.locationName}"/>');
						</c:forEach>
						</script>
						<jsp:include page="previewmap.jsp"/>
					</td> </tr>
					</table>
					<hr/>
				</module:display>
			</c:if>
		</module:display>
		<module:display name="/Activity Form/Location/Implementation Level" parentModule="/Activity Form/Location">
			<table>
				<tr>
					<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
						<digi:trn key="aim:level">Implementation Level</digi:trn>:
					</td>
					<td bgcolor="#ffffff">
						<c:if test="${aimEditActivityForm.location.levelId>0}">
							<b><category:getoptionvalue categoryValueId="${aimEditActivityForm.location.levelId}"/></b>
						</c:if>
					</td>
				</tr>
			</table>
			<hr/>
		</module:display>
		<module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
			<table style="font-size:11px;">
				<tr>
					<td width="30%" align="right" valign="top" nowrap="nowrap" bgcolor="#f4f4f2" class="t-name">
						<digi:trn key="aim:implementationLocation">Implementation Location</digi:trn>:
					</td>
					<td bgcolor="#ffffff">
						<c:if test="${aimEditActivityForm.location.implemLocationLevel>0}">
							<span class="word_break bold">
								<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.implemLocationLevel}"/>
							</span>
						</c:if>
					</td>
				</tr>
			</table>
			<hr/>
		</module:display>
	</div>
</fieldset>
</module:display>

<!-- LOCATIONS SECTION -->

<!-- PROGRAM SECTION -->
<module:display name="/Activity Form/Program" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class="legend_label" style="cursor: pointer;"><digi:trn>Program</digi:trn></span>
	</legend>
	<div class="toggleDiv">
	    <module:display name="/Activity Form/Program/National Plan Objective" parentModule="/Activity Form/Program">
            <c:set var="programs_list" value="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}" />
            <c:set var="programs_name"><digi:trn>National Plan Objective</digi:trn></c:set>
            <%@include file="activitypreview/programs.jspf" %>
        </module:display>
		<module:display name="/Activity Form/Program/Primary Programs" parentModule="/Activity Form">
			<c:set var="programs_list" value="${aimEditActivityForm.programs.primaryPrograms}" />
			<c:set var="programs_name"><digi:trn>Primary Programs</digi:trn></c:set>
			<%@include file="activitypreview/programs.jspf" %>
		</module:display>
		<module:display name="/Activity Form/Program/Secondary Programs" parentModule="/Activity Form/Program">
			<c:set var="programs_list" value="${aimEditActivityForm.programs.secondaryPrograms}" />
			<c:set var="programs_name"><digi:trn>Secondary Programs</digi:trn></c:set>
			<%@include file="activitypreview/programs.jspf" %>
		</module:display>
		<module:display name="/Activity Form/Program/Tertiary Programs" parentModule="/Activity Form/Program">
			<c:set var="programs_list" value="${aimEditActivityForm.programs.tertiaryPrograms}" />
			<c:set var="programs_name"><digi:trn>Tertiary Programs</digi:trn></c:set>
			<%@include file="activitypreview/programs.jspf" %>
		</module:display>
		<!-- program description -->
		<module:display name="/Activity Form/Program/Program Description" parentModule="/Activity Form/Program">
			<c:set var="programDescription" value="${aimEditActivityForm.programs.programDescription}" />
			<c:if test="${not empty programDescription}">
				<span class="word_break"><digi:trn>Program Description</digi:trn></span>
				<table width="100%" cellSpacing="2" cellPadding="1" style="font-size: 11px;">
					<tr>
						<td width="85%">
							<span class="word_break bold"><digi:edit key="${programDescription}" /></span>
						</td>
					</tr>
				</table>
				<hr>
			</c:if>
		</module:display>
		<!-- end program description -->
	</div>
</fieldset>
</module:display>
<!-- END PROGRAM SECTION -->

<!-- SECTORS SECTION -->
<module:display name="/Activity Form/Sectors" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="sectorslink" style="cursor: pointer;">
			<digi:trn>Sectors</digi:trn>
		</span>	
	</legend>
	<div id="sectorsdiv" class="toggleDiv">
		<c:forEach var="config" items="${aimEditActivityForm.sectors.classificationConfigs}" varStatus="ind">
			<bean:define id="emptySector" value="Sector"/>
			<module:display name="/Activity Form/Sectors/${config.name} Sectors" parentModule="/Activity Form/Sectors">
				<c:set var="hasSectors">false</c:set>
				<c:forEach var="actSect" items="${aimEditActivityForm.sectors.activitySectors}">
					<c:if test="${actSect.configId==config.id}">
						<c:set var="hasSectors">true</c:set>
					</c:if>
				</c:forEach>
				<c:if test="${hasSectors}">
					<digi:trn key="aim:addactivitysectors:${config.name} Sector">
						<span class="word_break"><c:out value="${config.name} Sector" /></span>
					</digi:trn> 
					<br/>
				</c:if>
				<c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
				<c:forEach var="sectors" items="${aimEditActivityForm.sectors.activitySectors}">
					<c:if test="${sectors.configId==config.id}">
						<module:display name="/Activity Form/Sectors" parentModule="/Activity Form">
							<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:10px;" >
							  <tr>
								 <td width=85%>
								  	<b><c:out value="${sectors.sectorScheme}" /></b>
									<c:if test="${!empty sectors.sectorName}">
										-
										<span class="word_break bold"><c:out value="${sectors.sectorName}"/></span>
									</c:if>
									<c:if test="${!empty sectors.subsectorLevel1Name}">
										<!-- Sub sector field not found -->
										-
										<span class="word_break bold"><c:out value="${sectors.subsectorLevel1Name}"/></span>
									</c:if>
									<c:if test="${!empty sectors.subsectorLevel2Name}">
										-
										<span class="word_break bold"><c:out value="${sectors.subsectorLevel2Name}"/></span>
									</c:if>
								</td>
							<td width=15% align=right valign=top>
                                <c:if test="${sectors.sectorPercentage!='' && sectors.sectorPercentage!='0'}">
									<span class="word_break bold">(<c:out value="${sectors.sectorPercentage}"/>)
                                        %</span>
                                </c:if>
							</td>
							</tr>
							</table>
							<hr>
					</module:display>
					</c:if>
			</c:forEach>
			</c:if>
		</module:display>
	</c:forEach>		
	<c:if test="${not empty aimEditActivityForm.components.activityComponentes}">
		<digi:trn>Components</digi:trn>:&nbsp;
		<table>
			<c:forEach var="compo" items="${aimEditActivityForm.components.activityComponentes}">
			<tr>
				<td width="100%"><span class="word_break">${compo.sectorName}</span></td>
				<td align="right"><span class="word_break">${compo.sectorPercentage} %</span></td>
			</tr>
			</c:forEach>
		</table>
		<hr />
	</c:if>
</div>
</fieldset>
</module:display>
<!-- END SECTORS SECTION -->

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

<c:set var="costName">Proposed Project Cost</c:set>
<c:set var="yearBudget">${aimEditActivityForm.funding.proposedAnnualBudgets}</c:set>
<c:set var="projCost">${aimEditActivityForm.funding.proProjCost}</c:set>
<c:set var="funAmount">${aimEditActivityForm.funding.proProjCost == null ? null : aimEditActivityForm.funding.proProjCost.funAmount}</c:set>
<c:set var="currencyCode">${aimEditActivityForm.funding.proProjCost == null ? null : aimEditActivityForm.funding.proProjCost.currencyCode}</c:set>
<c:set var="funDate">${aimEditActivityForm.funding.proProjCost == null ? null : aimEditActivityForm.funding.proProjCost.funDate}</c:set>
<%@ include file="projectCost.jspf" %>

<c:set var="costName">Revised Project Cost</c:set>
<c:set var="yearBudget"/>
<c:set var="projCost">${aimEditActivityForm.funding.revProjCost}</c:set>
<c:set var="funAmount">${aimEditActivityForm.funding.revProjCost == null ? null : aimEditActivityForm.funding.revProjCost.funAmount}</c:set>
<c:set var="currencyCode">${aimEditActivityForm.funding.revProjCost == null ? null : aimEditActivityForm.funding.revProjCost.currencyCode}</c:set>
<c:set var="funDate">${aimEditActivityForm.funding.revProjCost == null ? null : aimEditActivityForm.funding.revProjCost.funDate}</c:set>
<%@ include file="projectCost.jspf" %>


<!-- END PROPOSED PROJECT COST -->

<!-- BUDGET STRUCTURE -->
<module:display name="/Activity Form/Budget Structure" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="proposedcostlink" style="cursor: pointer;">
			<digi:trn>Budget Structure</digi:trn>
		</span>	</legend>
	<div id="budgetstructurediv" class="toggleDiv">
		<c:if test="${aimEditActivityForm.budgetStructure!=null}">
			<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%" >
				<tr bgcolor="#f0f0f0">
					<td>
						<digi:trn key="aim:name">Name</digi:trn>
					</td>
					<td>
						<digi:trn key="aim:percentage">Percentage</digi:trn>
					</td>
				</tr>
				<c:forEach var="budgetStructure" items="${aimEditActivityForm.budgetStructure}" >
					<tr bgcolor="#f0f0f0">
						<td bgcolor="#f0f0f0" align="left" width="150">
							<span class="word_break bold">${budgetStructure.budgetStructureName}</span>
						</td>
						<td bgcolor="#f0f0f0" align="left" width="150">
							<c:if test="${budgetStructure.budgetStructurePercentage != null && budgetStructure.budgetStructurePercentage.length() > 0}">
								<span class="word_break bold">${budgetStructure.budgetStructurePercentage}%</span>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</div>
</fieldset>
</module:display>
<!-- END BUDGET STRUCTURE -->
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
<module:display name="/Activity Form/Regional Funding" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="regionalfundinglink" style="cursor: pointer;">
			<digi:trn>Regional Funding</digi:trn>
		</span>	
	</legend>
	<div id="regionalfundingdiv" class="toggleDiv">
	<c:if test="${!empty aimEditActivityForm.funding.regionalFundings}">
	<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
		<c:forEach var="regFunds" items="${aimEditActivityForm.funding.regionalFundings}">
			<tr>
				<td class="prv_right">
					<table width="100%" cellSpacing="1" cellPadding="1">
						<tr>
							<td class="prv_right">
								<span class="word_break bold"><c:out value="${regFunds.regionName}"/></span>
							</td>
						</tr>
						<module:display name="/Activity Form/Regional Funding/Region Item/Commitments" parentModule="/Activity Form/Regional Funding/Region Item">
						<c:if test="${!empty regFunds.commitments}">
							<tr>
								<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding" border="1"> <tr>
										<td valign="top" width="100" bgcolor="#f0f0f0"> 
											<digi:trn>Commitments</digi:trn>											
										</td>
										<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<c:forEach var="fd" items="${regFunds.commitments}">
													<tr> <td width="50" bgcolor="#f0f0f0">
														<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
															<c:out value="${fd.adjustmentTypeName}" />
														</digi:trn></td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<c:out value="${fd.transactionAmount}"/>
														</td>
														<td class="prv_right">
															<c:out value="${fd.currencyCode}"/>
														</td>
														<td bgcolor="#f0f0f0" width="70">
															<c:out value="${fd.transactionDate}"/>
														</td>
														<td class="prv_right"></td> </tr>
												</c:forEach>
											</table>
										</td>
									</tr> </table>
								</td>
							</tr>
						</c:if>
							</module:display>
							<module:display name="/Activity Form/Regional Funding/Region Item/Disbursements" 
								parentModule="/Activity Form/Regional Funding/Region Item">
							<c:if test="${!empty regFunds.disbursements}">
								<tr>
									<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
										<tr>
											<td valign="top" width="100" bgcolor="#f0f0f0">
												<digi:trn key="aim:disbursements">Disbursements</digi:trn>
											</td>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<c:forEach var="fd" items="${regFunds.disbursements}">
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																<c:out value="${fd.adjustmentTypeName}" />
															</digi:trn>
														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<c:out value="${fd.transactionAmount}"/>
														</td>
														<td class="prv_right">
															<c:out value="${fd.currencyCode}"/>
														</td>
														<td bgcolor="#f0f0f0" width="70">
															<c:out value="${fd.transactionDate}"/>
														</td>
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
							</module:display>
							
							<module:display name="/Activity Form/Regional Funding/Region Item/Expenditures" 
										parentModule="/Activity Form/Regional Funding/Region Item">
							<c:if test="${!empty regFunds.expenditures}">
								<tr>
									<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
										<tr>
											<td valign="top" width="100" bgcolor="#f0f0f0">
												<digi:trn key="aim:expenditures">Expenditures</digi:trn>
											</td>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<c:forEach var="fd" items="${regFunds.expenditures}">
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																<c:out value="${fd.adjustmentTypeName}" />
															</digi:trn>
														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<c:out value="${fd.transactionAmount}"/>
														</td>
														<td class="prv_right">
															<c:out value="${fd.currencyCode}"/></td>
														<td bgcolor="#f0f0f0" width="70">
															<c:out value="${fd.transactionDate}"/>
														</td>
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
							</module:display>
						</table>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td class="prv_right">
						<FONT color='blue'>
							<jsp:include page="utils/amountUnitsUnformatted.jsp">
								<jsp:param value="* " name="amount_prefix"/>
							</jsp:include>	
						</FONT>
					</td>
				</tr>
			</table>
		</c:if> 
	</div>
</fieldset>
</module:display>
<!-- END REGIONAL FUNDING -->

<!-- COMPONENTS -->
<module:display name="/Activity Form/Components" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="componentlink" style="cursor: pointer;">
			<digi:trn>Components</digi:trn>
		</span>	
	</legend>
	<div id="componentdiv" class="toggleDiv">
<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="false">
		<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
			<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
				<table width="100%" cellSpacing="1" cellPadding="1">
					<tr> <td> <table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
						<tr>
							<td>
								<b> <c:out value="${comp.title}" /> </b>
							</td>
						</tr>
						<tr>
							<td>
								<i> <digi:trn key="aim:description">Description</digi:trn>
								:</i> <c:out value="${comp.description}" />
							</td>
						</tr>
						<tr>
							<td>
								<i> <digi:trn key="aim:description">Component Type</digi:trn>
								:</i> <c:out value="${comp.typeName}" />
							</td>
						</tr>
						<tr>
							<td class="prv_right">
								<span class="word_break bold"><digi:trn>Component Fundings</digi:trn></span>									
							</td>
						</tr>
						<module:display name="/Activity Form/Components/Component/Components Commitments" 
								parentModule="/Activity Form/Components/Component">
							<c:if test="${!empty comp.commitments}">
								<tr>
									<td class="prv_right">
										<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
											<tr>
												<td width="100" style="padding-left:5px;" vAlign="top" bgcolor="#f0f0f0">
													<digi:trn key="aim:commitments">Commitments</digi:trn>
												</td>
												<td class="prv_right">
													<c:forEach var="fd" items="${comp.commitments}">
													<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee" class="component-funding-table">												
														<tr>
															<module:display name="/Activity Form/Components/Component/Components Commitments" 
																parentModule="/Activity Form/Components/Component">
															<td width="100">
																<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																	<b><c:out value="${fd.adjustmentTypeName}" /></b>
																</digi:trn>
															</td>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount" 
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">	
																<td align="right" width="100">
																	<b><c:out value="${fd.transactionAmount}"/></b>
																</td>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency"
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																<td class="prv_right">
																	<b><c:out value="${fd.currencyCode}"/></b>
																</td>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date"
																			parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																<td width="80">
																	<b><c:out value="${fd.transactionDate}"/></b>
																</td>
															</module:display>
														</tr>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization"
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																<tr>
																	<td width="100">
																		<b><digi:trn>Organisation</digi:trn>:</b>
																	</td>
																	<td colspan="3" style="padding-left: 15px">
																		<logic:notEmpty property="componentOrganisation"
																						name="fd">
																			<c:out value="${fd.componentOrganisation.name}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Second Responsible Organization"
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																<tr>
																	<td width="100">
																		<b><digi:trn>Component Second Responsible Organization</digi:trn>:</b>
																	</td>
																	<td colspan="3" style="padding-left: 15px">
																		<logic:notEmpty property="componentSecondResponsibleOrganization"
																						name="fd">
																			<c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															</module:display>
														<module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Description"
																parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
															<tr>
																<td width="100">
																	<b><digi:trn>Description</digi:trn></b>
																</td>
																<td colspan="3" style="padding-left: 15px">
																	<b><c:out value="${fd.componentTransactionDescription}" /></b>
																</td>
															</tr>
														</module:display>
														</table> 
														<hr />
													</c:forEach>
												</td>
											</tr>
										</table> 
									</td>
								</tr>
							</c:if>
							</module:display>
  							<module:display name="/Activity Form/Components/Component/Components Disbursements"
													parentModule="/Activity Form/Components/Component">
							<c:if test="${!empty comp.disbursements}">
								<tr>
									<td class="prv_right">
									<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
										<tr>
											<td width="100" style="padding-left:5px;" vAlign="top" bgcolor="#f0f0f0">
												<digi:trn key="aim:disbursements">Disbursements</digi:trn>
											</td>
											<td class="prv_right">
												<c:forEach var="fd" items="${comp.disbursements}">
													<table width="100%" cellSpacing="1" cellPadding="1"
														   bgcolor="#eeeeee"
														   class="component-funding-table">
													<tr>
														<module:display name="/Activity Form/Components/Component/Components Disbursements"
																parentModule="/Activity Form/Components/Component">
															<td width="50">
																<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
																	<b><c:out value="${fd.adjustmentTypeName}" /></b>
																</digi:trn>
															</td>
														</module:display>
														<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount"
																	parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
															<td align="right" width="100">
																<b><c:out value="${fd.transactionAmount}"/></b>
															</td>
														</module:display>
														
														<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency"
																parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
															<td class="prv_right">
																<b><c:out value="${fd.currencyCode}"/></b>
															</td>
														</module:display>
														<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date"
															parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
															<td width="70">
																<b><c:out value="${fd.transactionDate}"/></b>
															</td>
														</module:display>
													</tr>
															<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component Organization"
																parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																<tr>
																	<td width="100">
																		<b><digi:trn>Organisation</digi:trn>:</b>
																	</td>
																	<td colspan="3" style="padding-left: 15px">
																		<logic:notEmpty property="componentOrganisation"
																						name="fd">
																			<c:out value="${fd.componentOrganisation.name}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component Second Responsible Organization"
																parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																<tr>
																	<td width="100">
																		<b><digi:trn>Component Second Responsible Organization</digi:trn>:</b>
																	</td>
																	<td colspan="3" style="padding-left: 15px">
																		<logic:notEmpty property="componentSecondResponsibleOrganization"
																						name="fd">
																			<c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															</module:display>

														<module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Description"
																parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
															<tr>
																<td width="100">
																	<b><digi:trn>Description</digi:trn></b>
																</td>
																<td colspan="3" style="padding-left: 15px">
																	<b><c:out value="${fd.componentTransactionDescription}" /></b>
																</td>
															</tr>
														</module:display>
														</table>
														<hr />
												</c:forEach>

											</td>
										</tr>
								</table>
							</td>
						</tr>
					</c:if>
					</module:display>
					
					<module:display name="/Activity Form/Components/Component/Components Expenditures" 
							parentModule="/Activity Form/Components/Component">
					<c:if test="${!empty comp.expenditures}">
						<tr>
							<td class="prv_right">
							<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" class="box-border-nopadding">
								<tr>
									<td width="100" bgcolor="#f0f0f0" vAlign="top" style="padding-left:5px;">
										<digi:trn key="aim:expenditures">Expenditures</digi:trn>
									</td>
									<td class="prv_right">
										<c:forEach var="fd" items="${comp.expenditures}">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee" class="component-funding-table">
											<tr>
												<module:display name="/Activity Form/Components/Component/Components Expeditures" 
													parentModule="/Activity Form/Components/Component">
													<td width="50">
														<digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
															<b><c:out value="${fd.adjustmentTypeName}" /></b>
														</digi:trn>
													</td>
												</module:display>
												<module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Amount"
													parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
													<td align="right" width="100">
														<b><c:out value="${fd.transactionAmount}"/></b>
													</td>
												</module:display>
												<module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Currency"
													parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
													<td class="prv_right">
														<b><c:out value="${fd.currencyCode}"/></b>
													</td>
												</module:display>
												<module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Transaction Date"
													parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
													<td width="70">
														<b><c:out value="${fd.transactionDate}"/></b>
													</td>
												</module:display>
											</tr>
															<module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Organization"
																parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																<tr>
																	<td width="100">
																		<b><digi:trn>Organisation</digi:trn>:</b>
																	</td>
																	<td colspan="3" style="padding-left: 15px">
																		<logic:notEmpty property="componentOrganisation"
																						name="fd">
																			<c:out value="${fd.componentOrganisation.name}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															</module:display>
															<module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Second Responsible Organization"
																parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																<tr>
																	<td width="100">
																		<b><digi:trn>Component Second Responsible Organization</digi:trn>:</b>
																	</td>
																	<td colspan="3" style="padding-left: 15px">
																		<logic:notEmpty property="componentSecondResponsibleOrganization"
																						name="fd">
																			<c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															</module:display>

														<module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Description"
																parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
															<tr>
																<td width="100">
																	<b><digi:trn>Description</digi:trn></b>
																</td>
																<td colspan="3" style="padding-left: 15px">
																	<b><c:out value="${fd.componentTransactionDescription}" /></b>
																</td>
															</tr>
														</module:display>
														</table>
														<hr />
										</c:forEach>

									</td>
								</tr>
							</table>
							</td>
						</tr>
					</c:if>
					</module:display>
					<tr>
						<td class="prv_right">
							<FONT color='blue'>
								<jsp:include page="utils/amountUnitsUnformatted.jsp">
									<jsp:param value="* " name="amount_prefix"/>
								</jsp:include>	
							</FONT>
						</td>
					</tr>
				</table> </td> </tr>
		</table>
	</c:forEach>
	</c:if>
	</logic:equal>
	<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="true">
		<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
			<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
				<table width="100%" cellSpacing="1" cellPadding="1">
					<tr>
						<td>
						<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
							<tr>
								<td>
									<span class="word_break bold"><c:out value="${comp.title}" /></span>
								</td>
							</tr>
							<tr>
								<td>
									<span class="italic"><digi:trn key="aim:component_code">Component code</digi:trn></span>
								</td>
									
								:<span class="word_break"><c:out value="${comp.code}" /></span>
								</td>
							</tr>
							<c:if test="${!empty comp.url} }">
							<tr>
								<td>
									<a href="<c:out value="${comp.url}"/>" target="_blank">
										<digi:trn key="aim:preview_link_to_component">Link to component</digi:trn>&nbsp;
										<c:out value="${comp.code}"/>
									</a>
								</td>
							</tr>
							</c:if>
							<tr>
								<td class="prv_right"><b>
									<digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
								</td>
							</tr>
							<tr>
								<td class="prv_right">
								<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
									<c:forEach var="financeByYearInfo" items="${comp.financeByYearInfo}">
										<tr>
											<td valign="top" width="100" bgcolor="#f0f0f0">
												<c:out value="${financeByYearInfo.key}"/></td>
												<c:set var="financeByYearInfoMap" value="${financeByYearInfo.value}"/>
											<td class="prv_right">
											<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
												<fmt:timeZone value="US/Eastern">
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:preview_plannedcommitments_sum">Planned Commitments Sum</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<aim:formatNumber value="${financeByYearInfoMap['MontoProgramado']}"/>USD														</td>
													</tr>
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:preview_actualcommitments_sum">Actual Commitments Sum</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<aim:formatNumber value="${financeByYearInfoMap['MontoReprogramado']}"/> USD														</td>
													</tr>
													<tr>
														<td width="50" bgcolor="#f0f0f0">
															<digi:trn key="aim:preview_plannedexpenditures_sum">Actual Expenditures Sum</digi:trn>														</td>
														<td align="right" width="100" bgcolor="#f0f0f0">
															<aim:formatNumber value="${financeByYearInfoMap['MontoEjecutado']}"/>USD														</td>
													</tr>
												</fmt:timeZone>
											</table>											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
									</c:forEach>
								</table>								</td>
							</tr>
						</table>						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
			</c:forEach>
			</c:if>	
	</logic:equal>
	</div>
</fieldset>
</module:display>
<!-- END COMPONENTS -->
<!-- ISSUES SECTION -->
<module:display name="/Activity Form/Issues Section" parentModule="/Activity Form">
<module:display name="/Activity Form/Issues Section/Issue" parentModule="/Activity Form/Issues Section">
<fieldset>
	<legend>
		<span class=legend_label id="issueslink" style="cursor: pointer;">
			<digi:trn>Issues</digi:trn> 
		</span>	
	</legend>
	<div id="issuesdiv" class="toggleDiv">
		<c:if test="${!empty aimEditActivityForm.issues.issues}">
		<table width="100%" cellSpacing="2" cellPadding="2" border="0">
			<c:forEach var="issue" items="${aimEditActivityForm.issues.issues}">
				<tr>
					<td valign="top" colspan="3">
						<li class="level1"><b> 
							<digi:trn key="aim:issuename:${issue.id}">
							<span class="word_break"><c:out value="${issue.name}" /></span>
							</digi:trn> 
						<module:display name="/Activity Form/Issues Section/Issue/Date" parentModule="/Activity Form/Issues Section/Issue">
								 <c:out value="${issue.issueDate}" />
						</module:display>
							</b>						</li>					</td>
				</tr>
					<module:display name="/Activity Form/Issues Section/Issue/Measure" parentModule="/Activity Form/Issues Section/Issue">
					<c:if test="${!empty issue.measures}">
						<c:forEach var="measure" items="${issue.measures}">
							<tr>
								<td></td>
								<td colspan="2">
									<li class="level2"><i> <digi:trn key="aim:${measure.nameTrimmed}">
										<span class="word_break"><c:out value="${measure.name}" /></digi:trn></span>
									<module:display name="/Activity Form/Issues Section/Issue/Measure/Date" parentModule="/Activity Form/Issues Section/Issue/Measure">
										 <c:out value="${measure.measureDate}" />
									</module:display>
										</i></li></td>
							</tr>
								<module:display name="/Activity Form/Issues Section/Issue/Measure/Actor" parentModule="/Activity Form/Issues Section/Issue/Measure">
								<c:if test="${!empty measure.actors}">
									<c:forEach var="actor" items="${measure.actors}">
										<tr>
											<td></td><td></td>
											<td>
											<li class="level3">
												<digi:trn key="aim:${actor.nameTrimmed}">
													<span class="word_break"><c:out value="${actor.name}" /></span>
												</digi:trn>
											</li></td>
										</tr>
									</c:forEach>
								</c:if>
								</module:display>
						</c:forEach>
					</c:if>
					</module:display>
			</c:forEach>
		</table>
	</c:if>
	</div>
</fieldset>
</module:display>
</module:display>
<!-- END ISSUES SECTION -->
	
<!-- DOCUMENT SECTION -->
        <module:display name="/Activity Form/Related Documents" parentModule="/Activity Form">
            <fieldset>
                <legend>
		<span class=legend_label id="documentslink" style="cursor: pointer;">
			<digi:trn>Related Documents</digi:trn>
		</span>	
	</legend>
	<div id="documnetsdiv" class="toggleDiv">
	<c:if test="${ (!empty aimEditActivityForm.documents.documents) || (!empty aimEditActivityForm.documents.crDocuments)}">
		<table width="100%" cellSpacing="1" cellPadding="5" cellSpacing="0" cellPadding="0">
			<logic:notEmpty name="aimEditActivityForm" property="documents.documents" >
				<logic:iterate name="aimEditActivityForm" property="documents.documents" id="docs" type="org.digijava.module.aim.helper.Documents">
					<c:if test="${docs.isFile == true}">
						<tr>
							<td>
								<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
									<tr>
										<td vAlign="center">&nbsp;
											<span class="word_break bold"><c:out value="${docs.title}"/></span>&nbsp;&nbsp;-&nbsp;&nbsp;<i>
											<c:out value="${docs.fileName}"/></i> 
											<logic:notEqual name="docs" property="docDescription" value=" ">
												<br/>&nbsp;
												<digi:trn>Description</digi:trn>:
												&nbsp;<span class="word_break bold"><bean:write name="docs" property="docDescription" /></span>
											</logic:notEqual> 
											<logic:notEmpty name="docs" property="date">
												<br />&nbsp;
												<digi:trn>Date</digi:trn>:
												<b>&nbsp;<c:out value="${docs.date}" /></b>
											</logic:notEmpty> 
											<logic:notEmpty name="docs" property="docType">
												<br />&nbsp;
												<digi:trn>Document Type</digi:trn>:&nbsp;
												<span class="word_break bold"><bean:write name="docs" property="docType" /></span>
											</logic:notEmpty>
										</td>
									</tr>
								</table>
								<hr />
							</td>
						</tr>
					</c:if>
				</logic:iterate>
			</logic:notEmpty>
			<logic:notEmpty name="aimEditActivityForm" property="documents.crDocuments" >
				<tr>
					<td>
						<logic:iterate name="aimEditActivityForm" property="documents.crDocuments" id="crDoc">
						<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
								<tr>
									<td vAlign="center">
										&nbsp;<b><c:out value="${crDoc.title}"/></b>&nbsp;&nbsp;-&nbsp;&nbsp;
										<i><c:out value="${crDoc.name}"/></i>
										<c:set var="translation">
											<digi:trn>Click here to download document</digi:trn>
										</c:set> 
                                                        <a id="<c:out value="${crDoc.uuid}"/>" target="_blank" href="${crDoc.generalLink}" title="${translation}">
                                                            <img src="/repository/contentrepository/view/images/check_out.gif" border="0">
                                                        </a>
                                                        <logic:notEmpty name="crDoc" property="description">
                                                            <br/>&nbsp;
											<digi:trn>Description</digi:trn>:&nbsp;
											<b><bean:write name="crDoc" property="description" /></b>
                                                        </logic:notEmpty>
                                                        <logic:notEmpty name="crDoc" property="calendar">
                                                            <br/>&nbsp;
											<digi:trn>Date</digi:trn>:
											<b>&nbsp;<c:out value="${crDoc.calendar}" /></b>
										</logic:notEmpty>
									</td>
                                                </tr>
                                            </table>
							<hr />
						</logic:iterate>
					</td>
                                </tr>
                            </logic:notEmpty>
                        </table>
                    </c:if>
                <%--<c:if test="${!empty aimEditActivityForm.documents.linksList}">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<c:forEach var="docList" items="${aimEditActivityForm.documents.linksList}" >
				<bean:define id="links" name="docList" property="relLink" />
				<tr>
					<td>
					<table width="100%" class="box-border-nopadding">
						<tr>
							<td width="2">
								<digi:img src="module/aim/images/web-page.gif"/>							</td>
							<td align="left" vAlign="center">&nbsp; <b>
								<c:out value="${links.title}"/></b> - &nbsp;&nbsp;&nbsp;
								<i> <a href="<c:out value="${links.url}"/>"> 
									<c:out value="${links.url}" />
								</a></i> 
								<br>
								&nbsp; <b><digi:trn>Description</digi:trn>:</b>
								&nbsp;<c:out value="${links.description}" />							</td>
						</tr>
					</table>					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if> --%>
	</div>
</fieldset>
</module:display>

<jsp:include page="previewActivityRegionalObservations.jsp"></jsp:include>

<jsp:include page="previewActivityLineMinistryObservations.jsp"></jsp:include>

<!-- RELATED ORGANIZATIONS SECTION -->
<module:display name="/Activity Form/Organizations" parentModule="/Activity Form">
<fieldset>
	<legend>
		<span class=legend_label id="relatedorglink" style="cursor: pointer;">
			<digi:trn>Related Organizations</digi:trn>
		</span>
	</legend>

	<div id="relateorgdiv" class="toggleDiv">
		<module:display name="/Activity Form/Organizations/Donor Organization" parentModule="/Activity Form/Organizations">
			<logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
                <digi:trn key="aim:donororganisation">Donor Organization</digi:trn>
                <br/>
				<div id="act_donor_organisation" style="display: block;">
					<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding" >
						<tr>
							<td>
								<logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">
								<ul>
									<li>
										<span class="word_break bold"><bean:write name="fundingOrganization" property="orgName"/></span>
									</li>
								</ul>
								</logic:iterate>
							</td>
						</tr>
					</table>
				</div>
				<hr />
			</logic:notEmpty>
		</module:display>

     <module:display name="/Activity Form/Organizations/Responsible Organization" parentModule="/Activity Form/Organizations">
		<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrganisations" >
			<div id="act_responsible_organisation" style="display: block;">
				<digi:trn key="aim:responsibleorganisation">Responsible Organization</digi:trn>
				<br />

				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding" >
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.respOrganisations" id="respOrganisations"
								type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<ul>
									<li>
										<span class="word_break bold"><bean:write name="respOrganisations" property="name"/></span>
										<c:set var="tempOrgId" scope="page">${respOrganisations.ampOrgId}</c:set>
										<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgToInfo(${tempOrgId})">
											<span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.respOrgToInfo[tempOrgId]}"/>)</span>
										</logic:notEmpty>
										<module:display name="/Activity Form/Organizations/Responsible Organization/percentage" parentModule="/Activity Form/Organizations/Responsible Organization">
 	 	 	 		                         <logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgPercentage(${tempOrgId})" >
												<c:out value="${aimEditActivityForm.agencies.respOrgPercentage[tempOrgId]}" /> %
 	 	 	 								</logic:notEmpty>
 	 	 	 							</module:display>
									</li>
								</ul>
							</logic:iterate>
						</td>
					</tr>
				</table>
				</div>
				<hr />
				<br/>
			</logic:notEmpty>
		</module:display>

        <module:display name="/Activity Form/Organizations/Executing Agency" parentModule="/Activity Form/Organizations">
 		<logic:notEmpty name="aimEditActivityForm" property="agencies.executingAgencies">
			<digi:trn key="aim:executingAgency">Executing Agency</digi:trn>
			<div id="act_executing_agency" style="display: block;">
			<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
				<tr>
					<td>
						<logic:iterate name="aimEditActivityForm" property="agencies.executingAgencies" id="execAgencies"
							type="org.digijava.module.aim.dbentity.AmpOrganisation">
							<ul>
								<li>
									<span class="word_break bold"><bean:write name="execAgencies" property="name" /></span>
									<c:set var="tempOrgId">${execAgencies.ampOrgId}</c:set>
									<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgToInfo(${tempOrgId})">
										<span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.executingOrgToInfo[tempOrgId]}"/>)</span>
									</logic:notEmpty>
									<module:display name="/Activity Form/Organizations/Executing Agency/percentage" parentModule="/Activity Form/Organizations/Executing Agency">
                                        <logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgPercentage(${tempOrgId})" >
                                          <c:out value="${aimEditActivityForm.agencies.executingOrgPercentage[tempOrgId]}" /> %
                                        </logic:notEmpty>
                                    </module:display>
								</li>
							</ul>
						</logic:iterate>
					</td>
				</tr>
			</table>
			</div>
			<hr/>
		</logic:notEmpty>
		</module:display>

		<module:display name="/Activity Form/Organizations/Implementing Agency" parentModule="/Activity Form/Organizations">
		<logic:notEmpty name="aimEditActivityForm" property="agencies.impAgencies" >
			<digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn>
			<br/>
			<div id="act_implementing_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.impAgencies" id="impAgencies"
								type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<ul>
									<li>
										<span class="word_break bold"><bean:write name="impAgencies" property="name" /></span>
										<c:set var="tempOrgId">${impAgencies.ampOrgId}</c:set>
										<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgToInfo(${tempOrgId})">
											<span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.impOrgToInfo[tempOrgId]}"/>)</span>
										</logic:notEmpty>
										<module:display name="/Activity Form/Organizations/Implementing Agency/percentage" parentModule="/Activity Form/Organizations/Implementing Agency">
                                            <logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgPercentage(${tempOrgId})" >
                                              <c:out value="${aimEditActivityForm.agencies.impOrgPercentage[tempOrgId]}" /> %
                                            </logic:notEmpty>
                                        </module:display>
									</li>
								</ul>
							 </logic:iterate>
						</td>
					</tr>
				</table>
			</div>
			<hr />
		</logic:notEmpty>
		</module:display>

		<module:display name="/Activity Form/Organizations/Beneficiary Agency" parentModule="/Activity Form/Organizations">
 		<logic:notEmpty name="aimEditActivityForm" property="agencies.benAgencies">
			<digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn>
			<br />
				<div id="act_benAgencies_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.benAgencies" id="benAgency"
								type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<ul>
									<li>
										<span class="word_break bold"><bean:write name="benAgency" property="name" /></span>
										<c:set var="tempOrgId">${benAgency.ampOrgId}</c:set>
										<!-- Additional Info field not found in the new activity form-->
										<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgToInfo(${tempOrgId})">
											<span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.benOrgToInfo[tempOrgId]}"/>)</span>
										</logic:notEmpty>
										<module:display name="/Activity Form/Organizations/Beneficiary Agency/percentage" parentModule="/Activity Form/Organizations/Beneficiary Agency">
                                            <logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgPercentage(${tempOrgId})" >
                                              <c:out value="${aimEditActivityForm.agencies.benOrgPercentage[tempOrgId]}" /> %
                                            </logic:notEmpty>
                                        </module:display>
									</li>
								</ul>
							</logic:iterate>
						</td>
					</tr>
				</table>
			</div>
			<hr />
		</logic:notEmpty>
		</module:display>

		<module:display name="/Activity Form/Organizations/Contracting Agency" parentModule="/Activity Form/Organizations">
		<logic:notEmpty name="aimEditActivityForm" property="agencies.conAgencies">
			<digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn>
			<br />
				<div id="act_contracting_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
							<logic:iterate name="aimEditActivityForm" property="agencies.conAgencies" id="conAgencies"
								type="org.digijava.module.aim.dbentity.AmpOrganisation">
								<ul>
									<li>
										<span class="word_break bold"><bean:write name="conAgencies" property="name" /></span>
										<c:set var="tempOrgId">${conAgencies.ampOrgId}</c:set>
										<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgToInfo(${tempOrgId})">
											<span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.conOrgToInfo[tempOrgId]}"/> )</span>
										</logic:notEmpty>
										<module:display name="/Activity Form/Organizations/Contracting Agency/percentage" parentModule="/Activity Form/Organizations/Contracting Agency">
                                            <logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgPercentage(${tempOrgId})" >
                                              <c:out value="${aimEditActivityForm.agencies.conOrgPercentage[tempOrgId]}" /> %
                                            </logic:notEmpty>
                                        </module:display>
									</li>
								</ul>
							</logic:iterate>
						</td>
					</tr>
				</table>
				</div>
				<hr />
			</logic:notEmpty>
		</module:display>


		<!--SECTOR GROUP SECTION -->
		<module:display name="/Activity Form/Organizations/Sector Group" parentModule="/Activity Form/Organizations">
			<logic:notEmpty name="aimEditActivityForm" property="agencies.sectGroups">
			<digi:trn key="aim:sectorGroup">Sector Group</digi:trn>
			<br/>
			
			<div id="act_sectGroups_agency" style="display: block;">
				<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
					<tr>
						<td>
						<logic:iterate name="aimEditActivityForm" property="agencies.sectGroups" id="sectGroup"
							type="org.digijava.module.aim.dbentity.AmpOrganisation">
							<ul>
								<li>
									<span class="word_break bold"><bean:write name="sectGroup" property="name" /></span> 
									<c:set var="tempOrgId">${sectGroup.ampOrgId}</c:set> 
									
									<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgToInfo(${tempOrgId})">
										<span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.sectOrgToInfo[tempOrgId]}"/> )</span>
									</logic:notEmpty>
									<module:display name="/Activity Form/Organizations/Sector Group/percentage" parentModule="/Activity Form/Organizations/Sector Group">
										<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgPercentage(${tempOrgId})" >
										  <c:out value="${aimEditActivityForm.agencies.sectOrgPercentage[tempOrgId]}" /> %
										</logic:notEmpty> 
									</module:display>
								</li>
							</ul>
						</logic:iterate></td>
					</tr>
				</table>
				</div>
			<hr />			
			</logic:notEmpty>
		</module:display>
		
		<module:display name="/Activity Form/Organizations/Regional Group" parentModule="/Activity Form/Organizations">
			<logic:notEmpty name="aimEditActivityForm" property="agencies.regGroups">
			<digi:trn key="aim:regionalGroup">Regional Group</digi:trn>
			<br/>
			
					<div id="act_regGroups_agency" style="display: block;">
						<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
							<tr>
								<td>
									<logic:iterate name="aimEditActivityForm" property="agencies.regGroups" id="regGroup"
										type="org.digijava.module.aim.dbentity.AmpOrganisation">
										<ul>
											<li>
												<span class="word_break bold"><bean:write name="regGroup" property="name" /></span> 
												<c:set var="tempOrgId" >${regGroup.ampOrgId}</c:set> 
												<logic:notEmpty property="agencies.regOrgToInfo(${tempOrgId})" name="aimEditActivityForm">
													<span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.regOrgToInfo[tempOrgId]}"/>)</span>
												</logic:notEmpty>
												<module:display name="/Activity Form/Organizations/Regional Group/percentage" parentModule="/Activity Form/Organizations/Regional Group">
													<logic:notEmpty name="aimEditActivityForm" property="agencies.regOrgPercentage(${tempOrgId})" >
													  <c:out value="${aimEditActivityForm.agencies.regOrgPercentage[tempOrgId]}" /> %
													</logic:notEmpty> 
												</module:display>
											</li>
										</ul>
									</logic:iterate>								</td>
							</tr>
						</table>
					</div>
			</logic:notEmpty>
		</module:display>
	</div>
</fieldset>
</module:display>

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
						<c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.donorContacts}" />
						<%@include file="activitypreview/contactInformation.jspf" %>
						<hr>
					</c:if> 
				</module:display>	
				<module:display name="/Activity Form/Contacts/Mofed Contact Information" parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.mofedContacts}">
						<digi:trn>MOFED contact information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.mofedContacts}" />
                        <%@include file="activitypreview/contactInformation.jspf" %>
						<hr>
					</c:if> 
				</module:display>
				
				<module:display name="/Activity Form/Contacts/Project Coordinator Contact Information" parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.projCoordinatorContacts}">
						<digi:trn>Project Coordinator Contact Information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.projCoordinatorContacts}" />
                        <%@include file="activitypreview/contactInformation.jspf" %>
						<hr>
					</c:if>
				</module:display>
						
				<module:display name="/Activity Form/Contacts/Sector Ministry Contact Information" parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.sectorMinistryContacts}">
						<digi:trn>Sector Ministry Contact Information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.sectorMinistryContacts}" />
                        <%@include file="activitypreview/contactInformation.jspf" %>
						<hr>
					</c:if> 
				</module:display>
							
				<module:display name="/Activity Form/Contacts/Implementing Executing Agency Contact Information"  parentModule="/Activity Form/Contacts">
					<c:if test="${not empty aimEditActivityForm.contactInformation.implExecutingAgencyContacts}">
						<digi:trn>Implementing/Executing Agency Contact Information</digi:trn>:&nbsp;
                        <c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.implExecutingAgencyContacts}" />
                        <%@include file="activitypreview/contactInformation.jspf" %>
					</c:if>
				</module:display>
			</div>
		</fieldset>
	</module:display>
</c:if>
<!-- END CONTACT INFORMATION -->

<!-- COSTING -->
<module:display name="Activity Costing" parentModule="PROJECT MANAGEMENT">
<fieldset>
	<legend>
		<span class=legend_label id="costinglink" style="cursor: pointer;">
			<digi:trn>Costing</digi:trn>
		</span>	
	</legend>
	<div id=costingdiv class="toggleDiv">
		<table width="100%" style="font-size:11px;">
			<tr>
				<td>
					<bean:define id="mode" value="preview" type="java.lang.String" toScope="request"/> 
					<jsp:include page="viewCostsSummary.jsp" flush="" />				</td>
			</tr>
		</table>
	</div>
</fieldset>
</module:display>
<!-- END COSTING -->
<!-- IPA Contracting -->
<feature:display name="Contracting" module="Contracting">
<fieldset>
	<legend>
		<span class=legend_label id="ipalink" style="cursor: pointer;">
			<digi:trn>IPA Contracting</digi:trn>
		</span>	
	</legend>
	<div id="ipadiv" class="toggleDiv">
		<table width="100%">
			<tr>
				<td><!-- contents --> 
					<logic:notEmpty name="aimEditActivityForm" property="contracts">
					<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#DBDBDB">
						<c:forEach items="${aimEditActivityForm.contracts.contracts}" var="contract" varStatus="idx">
							<tr>
								<td bgColor=#f4f4f2 align="center" vAlign="top">
								<table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Name" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:name" >Contract name:</digi:trn>
											</td>
											<td><span class="word_break bold">${contract.contractName}</span></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Description" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:description">Description:</digi:trn>
											</td>
											<td><span class="word_break bold">${contract.description}</span></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Activity Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										<tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn>
											</td>
											<td>
												<c:if test="${not empty contract.activityCategory}">
													<span class="word_break bold">${contract.activityCategory.value}</span>
												</c:if>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:type">Type</digi:trn>:
											</td>
											<td>
												<c:if test="${not empty contract.type}">
													<span class="word_break bold">${contract.type.value}</span>
												</c:if>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Start of Tendering" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:startOfTendering">Start of Tendering:</digi:trn>											
											</td>
											<td><span class="word_break bold">${contract.formattedStartOfTendering}</span></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Signature" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn>
											</td>
											<td><span class="word_break bold">${contract.formattedSignatureOfContract}</span></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Organizations" parentModule="/Activity Form/Contracts/Contract Item">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn>
											</td>
											<td>
												<c:if test="${not empty contract.organization}">
	                                            	<span class="word_break bold">${contract.organization.name}</span>
	                                            </c:if>
	                                          </td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Contractor Name" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractOrg">Contract Organization</digi:trn>:
											</td>
											<td><span class="word_break bold">${contract.contractingOrganizationText}</span></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Completion" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn>
											</td>
											<td><span class="word_break bold">${contract.formattedContractCompletion}</span></td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Details/Status" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:status">Status:</digi:trn>
											</td>
											<td>
												<c:if test="${not empty contract.status}">
	                                            	<span class="word_break bold">${contract.status.value}</span>
	                                           </c:if>
	                                         </td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Value" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:totalAmount">Total Amount</digi:trn>:
											</td>
											<td>
												<b>
												${contract.totalAmount}
												${contract.totalAmountCurrency.currencyCode}
												</b>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Contract Total Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left" colspan="2">
												<digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:IB">IB</digi:trn>:
											</td>
											<td>
												<b>
												${contract.totalECContribIBAmount}
												${contract.totalAmountCurrency.currencyCode}
												</b>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/INV Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:INV">INV:</digi:trn>
											</td>
											<td>
												<b>
												${contract.totalECContribINVAmount}
												${contract.totalAmountCurrency.currencyCode}
												</b>
											</td>
										</tr>
									</module:display>
									<tr>
										<td align="left" colspan="2">
											<digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn>
										</td>
									</tr>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Central Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:Central">Central</digi:trn>:
											</td>
											<td>
												<b>
												${contract.totalNationalContribCentralAmount}
												${contract.totalAmountCurrency.currencyCode}
												</b>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Regional Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:Regional">Regional</digi:trn>:
											</td>
											<td>
												<b>
												${contract.totalNationalContribRegionalAmount}
												${contract.totalAmountCurrency.currencyCode}
												</b>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IFI Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:IFIs">IFIs</digi:trn>:
											</td>
											<td>
												<b>
												${contract.totalNationalContribIFIAmount}
												${contract.totalAmountCurrency.currencyCode}
												</b>
											</td>
										</tr>
									</module:display>
									<tr>
										<td align="left" colspan="2">
											<digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn>
										</td>
									</tr>
									<module:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:IB">IB:</digi:trn></b>
											</td>
											<td>
												<b>
												${contract.totalPrivateContribAmount}
												${contract.totalAmountCurrency.currencyCode}
												</b>
											</td>
										</tr>
									</module:display>
									<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements" parentModule="/Activity Form/Contracts/Contract Item">
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:totalDisbursements">Total Disbursements</digi:trn>:
											</td>
											<td>
												<b>${contract.totalDisbursements}</b> &nbsp; 
											<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
													&nbsp; <b>${aimEditActivityForm.currCode}</b>
											</logic:empty> 
		                                    <logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
											&nbsp; <b>${contract.dibusrsementsGlobalCurrency.currencyCode}</b>
											</logic:notEmpty>
											</td>
										</tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:totalFundingDisbursements">Total Funding Disbursements</digi:trn>:
											</td>
											<td>
												<b>${contract.fundingTotalDisbursements}</b> &nbsp; 
												<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
													&nbsp;<b>${contract.totalAmountCurrency}</b>							              		
												</logic:empty> 
							              		<logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
						              				&nbsp;<b>${contract.dibusrsementsGlobalCurrency.currencyCode}</b>
						              			</logic:notEmpty>
						              		</td>
										</tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:
											</td>
											<td>
												&nbsp; <b>${contract.executionRate}</b>
											</td>
										</tr>
										<tr>
											<td align="left">
												<digi:trn key="aim:IPA:popup:contractFundingExecutionRate">Contract Funding Execution Rate</digi:trn>:
											</td>
											<td>&nbsp; <b>${contract.fundingExecutionRate}</b></td>
										</tr>
										<tr>
											<td colspan="2">
												<digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn>
											</td>
										</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<logic:notEmpty name="contract" property="disbursements">
											<table>
												<c:forEach items="${contract.disbursements}" var="disbursement">
													<tr>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
														<b><digi:trn>${disbursement.adjustmentType.value}</digi:trn></b>
<%-- 															<c:if test="${disbursement.adjustmentType==0}"> --%>
<%-- 																<b><digi:trn key="aim:actual">Actual</digi:trn></b> --%>
<%-- 															</c:if>  --%>
<%-- 															<c:if test="${disbursement.adjustmentType==1}"> --%>
<%-- 																<b><digi:trn key="aim:planned">Planned</digi:trn></b> --%>
<%-- 															</c:if>														 --%>
														</td>
														</module:display>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
															<b>${disbursement.amount}</b>
														</td>
														</module:display>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
															<b>${disbursement.currency.currencyCode}</b>
														</td>
														</module:display>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
														<td align="left" valign="top">
															<b>${disbursement.disbDate}</b>
														</td>
														</module:display>
													</tr>
												</c:forEach>
											</table>
										</logic:notEmpty></td>
									</tr>
									<tr>
										<td colspan="2">
											<digi:trn key="aim:IPA:popup:fundingDisbursements">Funding Disbursements:</digi:trn>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<logic:notEmpty name="aimEditActivityForm"
											property="funding.fundingDetails">
											<table width="100%">
												<tr>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn>
														</module:display>
													</td>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn>
														</module:display>
													</td>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn>
														</module:display>													
													</td>
													<td>
														<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
															<digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn>
														</module:display>
												</tr>
												<c:forEach items="${aimEditActivityForm.funding.fundingDetails}" var="fundingDetail">
													<logic:equal name="contract" property="contractName" value="${fundingDetail.contract.contractName}">
														<c:if test="${fundingDetail.transactionType == 1}">
															<tr>
																<td align="center" valign="top">
<%-- 			                                                   <digi:trn>${fundingDetail.adjustmentTypeName.value}</digi:trn> --%>
																
<%-- 																	<c:if test="${fundingDetail.adjustmentType==0}"> --%>
<%-- 																		<digi:trn key="aim:actual">Actual</digi:trn> --%>
<%-- 																	</c:if>  --%>
<%-- 																	<c:if test="${fundingDetail.adjustmentType==1}"> --%>
<%-- 																		<digi:trn key="aim:planned">Planned</digi:trn> --%>
<%-- 																	</c:if>																 --%>
																</td>
																<td align="center" valign="top">
																<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
																	<b>${fundingDetail.transactionAmount}</b>
																</module:display>	
																</td>
																
																<td align="center" valign="top">
																<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
																	<b>${fundingDetail.currencyCode.currencyCode}</b>
																</module:display>
																</td>
																<td align="center" valign="top">
																<module:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
																	<b>${fundingDetail.transactionDate}</b>
																</module:display>
																</td>
															</tr>
														</c:if>
													</logic:equal>
												</c:forEach>
											</table>
										</logic:notEmpty></td>
									</tr>
								</module:display>									
									<field:display name="Contracting Amendments" feature="Contracting">
										<bean:define id="ct" name="contract" type="org.digijava.module.aim.dbentity.IPAContract"/>
										<tr>
											<td align="left">
												<digi:trn>Contracts financed by the lessor</digi:trn>:
											</td>
											<td>&nbsp;
												<% if(ct.getDonorContractFundinAmount()!=null){ %> 
													<b><%=BigDecimal.valueOf(ct.getDonorContractFundinAmount()).toPlainString()%></b>
												<%}%>
												&nbsp;&nbsp;&nbsp;&nbsp;
											<b>${contract.donorContractFundingCurrency.currencyCode}</b>
										</td>
										</tr>
										<tr>
											<td align="left"><digi:trn>Total amount of the contract by the lessor</digi:trn>:											
										</td>
											<td>&nbsp; 
												<%if(ct.getDonorContractFundinAmount()!=null){ %> 
													<b><%=BigDecimal.valueOf(ct.getTotAmountDonorContractFunding()).toPlainString()%></b>
												<%}%>
												&nbsp;&nbsp;&nbsp;&nbsp;
												<b>${contract.totalAmountCurrencyDonor.currencyCode}</b>											
										</td>
										</tr>
										<tr>
											<td align="left"><digi:trn>Total contract amount from state</digi:trn>:											
											</td>
											<td>&nbsp; 
											<%if(ct.getDonorContractFundinAmount()!=null){ %> 
												<b><%=BigDecimal.valueOf(ct.getTotAmountCountryContractFunding()).toPlainString()%></b>
											<%}%>
												&nbsp;&nbsp;&nbsp;&nbsp;
												<b>${contract.totalAmountCurrencyCountry.currencyCode}</b>											
											</td>
										</tr>
										<tr>
											<td colspan="2"><b><digi:trn>Amendments :</digi:trn></b>											</td>
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
																<b>${amendment.amoutStr}</b>
															</td>
															<td align="center" valign="top">
																<b>${amendment.currency.currencyCode}</b>
															</td>
															<td align="center" valign="top">
																<b>${amendment.amendDate}</b>
															</td>
															<td align="center" valign="top">
																<b>${amendment.reference}</b>
															</td>
														</tr>
													</c:forEach>
												</table>
											</logic:notEmpty></td>
										</tr>
									</field:display>
								</table>								</td>
							</tr>
						</c:forEach>
					</table>
				</logic:notEmpty></td>
			</tr>
		</table>
	</div>
</fieldset>
<!-- end IPA Contracting -->
</feature:display>

<!-- GPI -->
		<module:display name="/Activity Form/GPI" parentModule="/Activity Form">
			<fieldset>
				<legend>
			<span class=legend_label id="gpilink" style="cursor: pointer;">
				<digi:trn>GPI</digi:trn>
			</span>
				</legend>
				<div class="field_text_big">
					<div id="gpi" class="toggleDiv" style="display: block;">
						<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
							<bean:define id="gpiSurvey" name="gpiSurveys" scope="request" toScope="page"
										 type="java.util.Collection"/>

							<c:set var="currentIndicatorName" value=""/>
							<logic:iterate name="gpiSurveys" id="gpiSurvey"
										   type="java.util.Collection" indexId="gpiId">
								<logic:iterate name="gpiSurvey" id="gpiresponse"
											   type="org.digijava.module.aim.dbentity.AmpGPISurveyResponse">

									<c:if test="${!currentIndicatorName.equals(gpiresponse.ampQuestionId.ampIndicatorId.name)}">
										<c:set var="currentIndicatorName"
											   value="${gpiresponse.ampQuestionId.ampIndicatorId.name}"/>
										<tr>
											<td bgcolor="#eeeeee" style="text-transform: uppercase;">
												<c:set var="indicatorName"
													   value="${gpiresponse.ampQuestionId.ampIndicatorId.name}"/>
												<span class="word_break bold"><digi:trn>${indicatorName}</digi:trn></span>
											</td>
										</tr>
									</c:if>
									<tr>
										<td>
											<c:set var="questionText"
												   value="${gpiresponse.ampQuestionId.questionText}"/>
											<c:set var="ampTypeName"
												   value="${gpiresponse.ampQuestionId.ampTypeId.name}"/>
											<span class="word_break bold"><digi:trn>${questionText}</digi:trn></span>
											<c:set var="responseText" value="${gpiresponse.response}"/>
											<lu>
												<li>
													<c:if test='${"yes-no".equals(ampTypeName) &&
													!"".equals(responseText) && responseText != null}'>
														<span class="word_break bold"><digi:trn>${responseText}</digi:trn></span>
													</c:if>
													<c:if test='${!"yes-no".equals(ampTypeName)}'>
														<span class="word_break bold">${responseText}</span>
													</c:if>
												</li>
											</lu>
										</td>
									</tr>
									<tr>
										<td>
											<hr/>
										</td>
									</tr>

								</logic:iterate>

							</logic:iterate>

						</table>
					</div>

				</div>
			</fieldset>
		</module:display>
		<!-- end GPI -->

<%@include file="previewActivityStructures.jsp" %>
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