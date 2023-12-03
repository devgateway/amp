<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>

<jsp:include page="previewLogframeUtil.jsp" flush="true" />
<jsp:include page="activityHistoryUtil.jsp" flush="true" />
<jsp:include page="overviewOptionsPopupUtil.jsp" flush="true" />

<div id="myContent" style="display: none;">
	<div id="myContentContent" class="content" style="overflow: scroll; height: 500px;">
	</div>
</div>

<style type="text/css">
	.td_top1 {
		border-top-style:solid; 
		border-top-color:#455786;
		border-top-width: 1px;
		border-right-style:solid; 
		border-right-color:#455786;
		border-right-width: 1px; 
		border-left-style:solid; 
		border-left-color:#455786;
		border-left-width: 1px;
	}
	
	.td_bottom1 {
		border-bottom-style:solid; 
		border-bottom-color:#455786;
		border-bottom-width: 1px;
		border-right-style:solid; 
		border-right-color:#455786;
		border-right-width: 1px; 
		border-left-style:solid; 
		border-left-color:#455786;
		border-left-width: 1px;
	}
	
	.td_right_left1 {
		border-right-style:solid; 
		border-right-color:#455786;
		border-right-width: 1px; 
		border-left-style:solid; 
		border-left-color:#455786;
		border-left-width: 1px;
	}
</style>

<script type="text/javascript">

YAHOOAmp.namespace("YAHOOAmp.amp");

var myPanel = new YAHOOAmp.widget.Panel("myPreview", {
	width:"940px",
	fixedcenter: true,
    constraintoviewport: false,
    underlay:"none",
    close:true,
    visible:false,
    modal:true,
    draggable:true,
    context: ["showbtn", "tl", "bl"]
    });
var panelStart=0;

var responseSuccess = function(o){
	/* Please see the Success Case section for more
	* details on the response object's properties.
	* o.tId
	* o.status
	* o.statusText
	* o.getResponseHeader[ ]
	* o.getAllResponseHeaders
	* o.responseText
	* o.responseXML
	* o.argument
	*/
	var response = o.responseText; 
	var content = document.getElementById("myContentContent");
	//response = response.split("<!")[0];
	content.innerHTML = response;
	//content.style.visibility = "visible";
	
	showContent();
}

function showPanelLoading(msg){
	   var content = document.getElementById("myContentContent");
	   content.innerHTML = "<div style='text-align: center'>" + "Loading..." +
	   "... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";   
	   showContent();
	 }
	 
var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
	//alert("Connection Failure!"); 
}  
var callback = 
{ 
	success:responseSuccess, 
	failure:responseFailure 
};

function showContent(){
	var element = document.getElementById("myContent");
	element.style.display = "inline";
	if (panelStart < 1){
		myPanel.setBody(element);
	}
	if (panelStart < 2){
		document.getElementById("myContent").scrollTop=0;
		myPanel.show();
		panelStart = 2;
	}
}

function preview(id)
{
	showPanelLoading();
	var postString="&activityId=" + id+"&isPreview=2&previewPopin=true";
	//alert(postString);
	<digi:context name="addUrl" property="context/ampModule/moduleinstance/viewActivityPreviewPopin.do" />
	var url = "<%=addUrl %>?"+postString;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	
}

function initPopin() {
	var msg="\n<digi:trn>Activity Preview</digi:trn>";
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

window.onload=initPopin;

function viewChanges(id){
	var windowname = "popup"+new Date().getTime();
	
	openNewWindowWithName(650,200, windowname);
	<digi:context name="showLog" property="context/ampModule/moduleinstance/showActivityLog.do" />
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var referLink = document.createElement('a');
		referLink.href = "<%= showLog %>?activityId=" + id;;
		referLink.target = windowname;
		document.body.appendChild(referLink);
		referLink.click();
	}
	else
	{
		popupPointer.href = "<%= showLog %>?activityId=" + id;
	}
	
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

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/ampModule/moduleinstance/projectFicheExport.do" />
	var url ="<%=ficheUrl%>~ampActivityId=" + id;
	openURLinWindow(url,650,500);
}

function fnEditProject(id)
{
	
	<digi:context name="addUrl" property="context/ampModule/moduleinstance/editActivity.do" />
	document.aimChannelOverviewForm.action = "<%=addUrl%>~pageId=1~step=1~resetMessages=true~action=edit~surveyFlag=true~activityId=" + id + "~actId=" + id;
	document.aimChannelOverviewForm.target = "_self";
    document.aimChannelOverviewForm.submit();
    
}

</script>

<script language="JavaScript">
<!--

	function viewProjectDetails(type,key) {
		openNewWindow(600, 400);
		<digi:context name="viewProjDetails" property="context/ampModule/moduleinstance/viewProjectDetails.do"/>
		document.aimMainProjectDetailsForm.action = "<%= viewProjDetails %>";
		document.aimMainProjectDetailsForm.type.value = type;
		document.aimMainProjectDetailsForm.projectComments.value = key;
		document.aimMainProjectDetailsForm.description.value = key;
		document.aimMainProjectDetailsForm.objectives.value = key;
		document.aimMainProjectDetailsForm.target = popupPointer.name;
		document.aimMainProjectDetailsForm.submit();					  
	}

-->
</script>
<style type="text/css">


#tabs {
	font-family: Arial,Helvetica,sans-serif;
	font-size: 8pt;
	clear: both;
	text-align: center;
}

#tabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#tabs li { 
	 float: left;
}



#tabs a, #tabs span { 
	font-size: 8pt;
}

#tabs ul li a { 
	background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs ul li span a { 
	background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li span a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs a:hover {
    background: #455786 url(/TEMPLATE/ampTemplate/images/tableftcornerhover.gif) left top no-repeat;  
}

#tabs a:hover span {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}
#tabs a:hover div {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}

#tabs a.active {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 4px 10px 4px 10px;
	text-decoration: none;
	color: #333;
}

#tabs a.active:hover {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 6px 10px 6px 10px;
	text-decoration: none;
	color: #333;
}


#subtabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#subtabs li {
	float: left;
	padding: 0px 4px 0px 4px;
}

#subtabs a, #subtabs span { 
	font-size: 8pt; 
}

#subtabs a {
}

#subtabs ul li span {
	text-decoration: none;
}

#subtabs ul li div span {
	text-decoration: none;
}

#subtabs {
	text-align: center;
	font-family:Arial,Helvetica,sans-serif;
	font-size: 8pt;
	padding: 2px 4px 2px 4px;
	background-color:#CCDBFF;
}

#main {
	clear:both;
	text-align: left;
	border-top: 2px solid #222E5D;
	border-left: 1px solid #666;
	border-right: 1px solid #666;
	padding: 2px 4px 2px 4px;
}
html>body #main {
	width:742px;
}

#mainEmpty {
	border-top: 2px solid #222E5D;
	width: 750px;
	clear:both;
}
html>body #mainEmpty {
	clear:both;
	width:752px;
}

</style>
<%--
<digi:errors/>
--%>

<digi:instance property="aimMainProjectDetailsForm" />

<logic:equal name="aimMainProjectDetailsForm" property="sessionExpired" value="true">
</logic:equal>

<logic:equal name="aimMainProjectDetailsForm" property="sessionExpired" value="false">
<jsp:useBean id="urlTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlTabs}" property="ampActivityId">
	<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlTabs}" property="tabIndex" value="0"/>
<jsp:useBean id="urlDescription" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDescription}" property="ampActivityId">
	<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
</c:set>

<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" vAlign="top" align="left"><tr><td class="td_top1">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" vAlign="top" align="left"><tr><td>

<input name="tempActivity" type="hidden" value='<%=request.getParameter("ampActivityId")%>' id="tempActivity">
<input name="showBottomBorder" type="hidden" value="1" id="showBottomBorder">
<c:set var="activityExists">
${aimMainProjectDetailsForm.activityExists}
</c:set>
<c:choose>
<c:when test="${activityExists}">

<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" vAlign="top" align="left">
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="td_top"><strong>&nbsp;
			<ampModule:display name="Previews"
				parentModule="PROJECT MANAGEMENT">
				<feature:display name="Preview Activity" ampModule="Previews">
					<field:display feature="Preview Activity" name="Preview Button">
						<a href="javascript:preview(document.getElementById('tempActivity').value);" title="<digi:trn key='btn:preview'>Preview</digi:trn>"> 
							<img src="/repository/aim/images/tangopack_preview.png" border="0"></a>
					</field:display>
				</feature:display>
			</ampModule:display>

			<c:set var="activityEditURL">
				<field:display name="Add SSC Button" feature="Edit Activity">/wicket/onepager/ssc/<%=request.getParameter("ampActivityId")%></field:display>
				<field:display name="Add Activity Button" feature="Edit Activity">/wicket/onepager/activity/<%=request.getParameter("ampActivityId")%></field:display>
			</c:set>

			<ampModule:display name="Previews" parentModule="PROJECT MANAGEMENT">
				<feature:display name="Edit Activity" ampModule="Previews">
					<field:display feature="Edit Activity" name="Edit Activity Button">  
						<logic:equal name="aimMainProjectDetailsForm" property="buttonText" value="edit">
	                		<a href="${activityEditURL}" style="cursor:pointer" target="_self" title="<digi:trn key='btn:edit'>Edit</digi:trn>">
								<img src="/repository/aim/images/tangopack_edit.png" border="0"></a>&nbsp;
	                		 <a style="cursor:pointer" target="_blank" onclick="javascript:fnEditProject(document.getElementById('tempActivity').value); return false;" title="<digi:trn key='btn:oldedit'>Edit using Old Form</digi:trn>"> 
								<img src="/repository/aim/images/tangopack_old_edit.png" border="0"></a>&nbsp;
						</logic:equal>
					</field:display>
				</feature:display>
			</ampModule:display>
			<ampModule:display name="Previews"
				parentModule="PROJECT MANAGEMENT">
				<feature:display name="Edit Activity" ampModule="Previews">
					<field:display feature="Edit Activity" name="Validate Activity Button">
						<logic:equal name="aimMainProjectDetailsForm" property="buttonText" value="validate">
							<c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}">
								<a href="${activityEditURL}" style="cursor:pointer" target="_self" title="<digi:trn key='btn:validate'>Validate</digi:trn>">
									<img src="/repository/aim/images/tangopack_validate2.png" border="0"></a>&nbsp;
							</c:if>
						</logic:equal>
					</field:display>
				</feature:display>
			</ampModule:display>
			<a style="cursor:pointer" target="_blank" onclick="javascript:previewHistory(document.getElementById('tempActivity').value); return false;" title="<digi:trn>View History</digi:trn>">
				<img src="/repository/aim/images/activity_history.png" border="0"></a>&nbsp;
			<ampModule:display name="Previews" parentModule="PROJECT MANAGEMENT">
				<feature:display name="Logframe" ampModule="Previews">
					<field:display name="Logframe Preview Button" feature="Logframe">
						<div id="gen" title='<digi:trn jsFriendly="true" key="logframeBtn:previewLogframe">Preview Logframe</digi:trn>'>
						<a href="#" onclick="javascript:previewLogframe(document.getElementById('tempActivity').value); return false;">
						<digi:trn key="logframeBtn:previewLogframe">Preview Logframe</digi:trn></a></div>&nbsp;
					</field:display>
				</feature:display>
			</ampModule:display>
			<ampModule:display name="Previews" parentModule="PROJECT MANAGEMENT">
				<feature:display name="Project Fiche" ampModule="Previews">
					<field:display name="Project Fiche Button" feature="Project Fiche">
						&nbsp;|
						<div id="gen" title='<digi:trn jsFriendly="true" key="aim:projectFiche">Project Fiche</digi:trn>'>
						<a href="#" onclick="javascript:projectFiche(document.getElementById('tempActivity').value); return false;">
						<digi:trn key="aim:projectFiche">Project Fiche</digi:trn></a></div>
					</field:display>
				</feature:display>
			</ampModule:display>
            <logic:present name="returnSearch" scope="session">
            &nbsp;|
            <a href="/search/search.do"><digi:trn key="search:backtosearch">Return to search results</digi:trn></a>
            </logic:present>
            </strong>
		</td>
	</tr>
	<tr><td>&nbsp;
		
	</td></tr>
   <TR>
		<TD>

			<TABLE width="100%" cellSpacing="0" cellPadding="0" vAlign="top">
				<TR>
					<TD>
						<TABLE cellpadding="0" cellspacing="0" valign="top" align=left width="100%"> 
							<TR>
							<feature:display ampModule="Project ID and Planning" name="Identification">
							<field:display  feature="Identification" name="Project Title">
								<TD valign="center"><span class="subtitle-blue-1">
									&nbsp;<bean:write name="aimMainProjectDetailsForm" property="name"/></span>
								</TD>
							</field:display>						
							</feature:display>
							</TR>
						</TABLE>
					</TD>
				</TR>					
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD width="100%" nowrap align="left" vAlign="bottom" height="20">
			<TABLE width="100%" cellspacing="1" cellpadding="0" border="0">
			  	<TR><TD>
                <br />
				<div style="padding-left:5px;width:900px;">
				<DIV id="tabs">
					<UL>
						<field:display name="Channel Overview Tab" feature="Channel Overview">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="0">
							   <LI>
                               		<a name="node">
                               		<div>
									<digi:trn key="aim:overview">Overview</digi:trn>
                                    </div>
                                    </a>
								</LI>
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="0">
								<LI>
                                <span>
								<digi:link href="/viewChannelOverview.do" name="urlTabs">
								<div title='<digi:trn jsFriendly="true" key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>'>
									<digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
                                </div>
								</digi:link>
                                </span>
								</LI>
							</logic:notEqual>
						</field:display>
						<ampModule:display name="References" parentModule="PROJECT MANAGEMENT">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="1">
							   <LI>
                               		<a name="node">
                               		<div>
									<digi:trn key="aim:references">References</digi:trn>
                                    </div>
                                    </a>
								</LI>
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="1">
								<c:set target="${urlTabs}" property="tabIndex" value="1"/>
								<LI>
                                <span>
								<digi:link href="/viewReferences.do" name="urlTabs">
								<div title='<digi:trn jsFriendly="true" key="aim:clickToViewReferences">Click here to view References</digi:trn>'>
									<digi:trn key="aim:references">references</digi:trn>
                                </div>
								</digi:link>
                                </span>
								</LI>
							</logic:notEqual>
						</ampModule:display>
						
						<feature:display name="Financial Progress Tab" ampModule="Funding">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="2">
							   <c:set target="${urlTabs}" property="tabIndex" value="2"/>
							   <LI>
                               		<a name="node">
                               		<digi:link href="/viewFinancingBreakdown.do" name="urlTabs">
										<div title='<digi:trn jsFriendly="true" key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>'>
											<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
		                                </div>
									</digi:link>
                                    </a>
								</LI>
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="2">
		     					<c:set target="${urlTabs}" property="tabIndex" value="2"/>
								<LI>
                                <span>
		              			<digi:link href="/viewFinancingBreakdown.do" name="urlTabs">
								<div title='<digi:trn jsFriendly="true" key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>'>
									<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
                                </div>
								</digi:link>
                                </span>
								</LI>
							</logic:notEqual>
						</feature:display >
						
							<feature:display name="Funding Information" ampModule="Funding"></feature:display>
								<field:display name="Funding Organizations Tab" feature="Funding Information">
									<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="3">
									   <LI>
                                        <a name="node">
                                        <div>
											<digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>  
                                        </div>
                                        </a>
										</LI>
										</logic:equal>
										<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="3">
				                  <c:set target="${urlTabs}" property="tabIndex" value="3"/>
										<LI>
                                        <span>
                                            <digi:link href="/viewPhysicalProgress.do" name="urlTabs">
                                            <div title='<digi:trn jsFriendly="true" key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>'>
                                            <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
                                            </div>
	                                        </digi:link>
    		                            </span>
										</LI>
									</logic:notEqual>
								</field:display>
							
						<ampModule:display name="Document" parentModule="PROJECT MANAGEMENT">
						<feature:display name="Documents Tab" ampModule="Document">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="4">
							   <LI>
                               		<a name="node">
                               		<div>
									<digi:trn key="aim:documents">Documents</digi:trn>
                                    </div>
                                    </a>
							    </LI>
						  	</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="4">
                                        <c:set target="${urlTabs}" property="tabIndex" value="4"/>
										<LI>
                                        <span>
                                            <digi:link href="/viewKnowledge.do" name="urlTabs">
                                            <div title='<digi:trn jsFriendly="true" key="aim:clickToViewDocuments">Click here to view Documents</digi:trn>'>
                                            <digi:trn key="aim:documents">Documents</digi:trn>
                                            </div>
	                                        </digi:link>
    		                            </span>
										</LI>
							</logic:notEqual>
							</feature:display>
							</ampModule:display>
							<feature:display name="Regional Funding" ampModule="Funding"></feature:display>
							 <field:display name="Regional Funding Tab" feature="Regional Funding">
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="5">
								   <LI>
                               		<a name="node">
	                                   <div>
										<digi:trn key="aim:regionalFunding">Regional Funding</digi:trn>
                                       </div>
                                    </a>
									</LI>
									</logic:equal>
									<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="5">
									<c:set target="${urlTabs}" property="tabIndex" value="5"/>
									<c:set target="${urlTabs}" property="regionId" value="-1"/>
									<LI>
                                    <span>
									<digi:link href="/viewRegionalFundingBreakdown.do" name="urlTabs">
										<div title='<digi:trn jsFriendly="true" key="aim:clickToViewRegionalFundings">Click here to view regional funding</digi:trn>'>
										<digi:trn key="aim:regionalFunding">Regional Funding</digi:trn> 	
                                        </div>
									</digi:link>
                                    </span>
									</LI>
								</logic:notEqual>
							 </field:display>
						<feature:display name="Paris Indicators" ampModule="Paris Indicators"></feature:display>
						<field:display name="Paris Survey" feature="Paris Indicators">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="6">
								   <LI>						
                               		<a name="node">
                                    <div>
 									<digi:trn key="aim:parisIndicators">Paris Indicators</digi:trn>
                                    </div>
                                    </a>
									</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="6">
		           			<jsp:useBean id="survey" type="java.util.Map" class="java.util.HashMap" />
								<c:set target="${survey}" property="ampActivityId">
									<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
								</c:set>
								<c:set target="${survey}" property="tabIndex" value="6"/>
								<LI>	
                                <span>
								<digi:link href="/viewSurveyList.do" name="survey">
								<div title='<digi:trn jsFriendly="true" key="aim:clickToViewAidEffectIndicators">Click here to view Aid Effectiveness Indicators</digi:trn>'>
									<digi:trn key="aim:parisIndicators">Paris Indicators</digi:trn> 	
                                </div>
								</digi:link>
                                </span>
								</LI>
							</logic:notEqual>
						</field:display>
						<feature:display name="Activity Dashboard" ampModule="M & E">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="7">
									<LI>
                               		<a name="node">
                                    <div>
									<digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
                                    </div>
                                    </a>
									</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="7">
								<c:set target="${urlTabs}" property="tabIndex" value="7"/>
								<LI>
                                <span>
								<digi:link href="/viewActivityDashboard.do" name="urlTabs">
								<div title='<digi:trn jsFriendly="true" key="aim:clickToViewActivityDashboard">Click here to view activity dashboard</digi:trn>'>
									<digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
                                </div>
								</digi:link>
                                </span>
								</LI>
							</logic:notEqual>
						</feature:display>						
						
						<feature:display name="Contracting" ampModule="Contracting">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="9">
							   <LI>
                               		<a name="node">
                                    <div>
									<digi:trn key="aim:projectContracting"> Contracting </digi:trn>								
                                    </div>
                                    </a>
								</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="9">
                                <c:set target="${urlTabs}" property="tabIndex" value="9"/>
                                <LI>
                                <span>
                                <digi:link href="/viewIPAContracting.do" name="urlTabs">
                                <div title='<digi:trn jsFriendly="true" key="aim:clickToViewContracting">Click here to view Contracting</digi:trn>'>
                                    <digi:trn key="aim:projectContracting">Contracting</digi:trn>
                                </div>
                                </digi:link>
                                </span>
								</LI>
							</logic:notEqual>
						</feature:display>
						
						<ampModule:display name="Regional Observations" parentModule="PROJECT MANAGEMENT">
							<feature:display name="Regional Observations" ampModule="Regional Observations">
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="10">
									<LI>
		                               	<a name="node">
		                                    <div>
												<digi:trn>Regional Observations</digi:trn>								
		                                    </div>
		                                </a>
									</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="10">
		                        	<c:set target="${urlTabs}" property="tabIndex" value="10"/>
		                            	<LI>
		                                	<span>
		                                		<digi:link href="/viewRegionalObservations.do" name="urlTabs">
			                                		<div title='<digi:trn jsFriendly="true">Click here to view Regional Observations</digi:trn>'>
			                                    		<digi:trn>Regional Observations</digi:trn>
			                                		</div>
		                                		</digi:link>
		                                	</span>
										</LI>
								</logic:notEqual>
							</feature:display>
						</ampModule:display>
					</UL>		
				</DIV>	
            	</DIV>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</c:when>
<c:otherwise>
    <div style="font-size: 12px;text-align: center;color:red">
<digi:trn>Couldn't find activity! It may have been deleted from the system</digi:trn>
    </div>
</c:otherwise>
</c:choose>
</logic:equal>