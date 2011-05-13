<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlParams}" property="tId" value="-1"/>
<c:set target="${urlParams}" property="dest" value="teamLead"/>			
<c:set target="${urlParams}" property="subtab" value="0"/>			

<jsp:useBean id="urlParamsChild" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlParamsChild}" property="tId" value="-1"/>
<c:set target="${urlParamsChild}" property="dest" value="teamLead"/>			
<c:set target="${urlParamsChild}" property="subtab" value="2"/>			

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
<c:set var="loadstatustext">
	<digi:trn key="aim:loadstatustext">Requesting Content</digi:trn>
</c:set>

<script language="javascript">
function putLoading(){

	var mainDiv = document.getElementsByClassName("contentbox_border");
	var subtabs = document.getElementById("subtabs");
	
	if(mainDiv[0]){
		loadstatustext='&nbsp;&nbsp;&nbsp;<img src="/repository/aim/view/scripts/ajaxtabs/loading.gif" /> <%=((String) pageContext.getAttribute("loadstatustext")).replaceAll("\r\n"," ")%> <span id="statusValue">...</span>';
		mainDiv[0].innerHTML = loadstatustext;
	}
	if(subtabs){
		subtabs.style.display = "none";
	}
	
	return true;
}
</script>
<div style="width:100%;">
				<DIV id="tabs">
					<UL>
						<c:if test="${selectedTab  == '0'}">
						   	<LI>
						   		<a name="node">
								<div>
									<digi:trn key="aim:workspaceConfiguration">Configuration</digi:trn>								
								</div>
								</a>
							</LI>
						</c:if>	
						<c:if test="${selectedTab != '0'}">
							<LI>
						   		<span>
									<digi:link href="/workspaceOverview.do" name="urlParams">
									<div title='<digi:trn key="aim:clickToViewWorkspaceconfiguration">Click here to view workspace configuration</digi:trn>'>
										<digi:trn key="aim:workspaceConfiguration">Configuration</digi:trn>
									</div>	
									</digi:link>							
						   		</span>
							</LI>
						</c:if>
						<c:if test="${selectedTab == '1'}">
							<LI>
						   		<a name="node">
						   		<div>
								<digi:trn key="aim:members">Members</digi:trn>	
								</div>
								</a>
							</LI>
						</c:if>
						
						<c:if test="${selectedTab != '1'}">
							<LI>
						   		<span>
								<digi:link href="/teamMemberList.do">	
								<div title='<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>'>	
										<digi:trn key="aim:members">Members</digi:trn>	
								</div>
								</digi:link>
						   		</span>
							</LI>							
						</c:if>						
						<c:if test="${teamAccessTypeLocal != 'Management' }">
							<c:if test="${selectedTab == '2'}">
								<LI>
							   		<a name="node">
							   		<div>
									<digi:trn key="aim:activities">Activities</digi:trn>
									</div>
									</a>
								</LI>
							</c:if>
							<c:if test="${selectedTab != '2'}">
								<LI>
									<span>
									<digi:link href="/teamActivityList.do" name="urlParams">
									<div title='<digi:trn key="aim:clickToViewActivities">Click here to view Activities</digi:trn>'>
										<digi:trn key="aim:activities">Activities</digi:trn>
									</div>
									</digi:link>
									</span>
								</LI>
							</c:if>
						</c:if>
						<c:if test="${selectedTab == '3'}">
							<LI>
						   		<a name="node">
						   		<div>
									<digi:trn key="aim:workspaceReports">Reports</digi:trn>	
								</div>
								</a>
							</LI>
						</c:if>
						
						<c:if test="${selectedTab != '3'}">
							<LI>
								<span>
								<digi:link href="/teamReportList.do"> 	
								<div title='<digi:trn key="aim:clickToViewReports">Click here to view reports</digi:trn>'>	
									<digi:trn key="aim:workspaceReports">Reports</digi:trn>	
								</div>
								</digi:link>
								</span>	
							</LI>
						</c:if>
						

						<c:if test="${selectedTab == '4'}">
							<LI>
						   		<a name="node">
						   		<div>
								<digi:trn key="fm:documentmanagement">Document Management</digi:trn>	
								</div>
								</a>
							</LI>
						</c:if>
						
						<c:if test="${selectedTab != '4'}">
							<LI>
							<span>
								<bean:define id="subtabLink" value="0" />
								<digi:link href="/relatedLinksList.do" onclick="return putLoading();" paramId="subtab" paramProperty="subtabLink" >	
								<div title='<digi:trn key="aim:clickToViewResources">Click here to view resources</digi:trn>'>	
									<digi:trn key="fm:documentmanagement">Document Management</digi:trn>	
								</div>
								</digi:link>	
							</span>
							</LI>		
						</c:if>
							
						<module:display name="Trend Analysis and Forecasting" parentModule="TREND ANALYSIS">
						
							<c:if test="${selectedTab == '6'}">
								<LI>
						   		<a name="node">
						   		<div>
									<digi:trn key="aim:m&e">M&E</digi:trn>
								</div>
								</a>
								</LI>
							</c:if>
						
							<c:if test="${selectedTab != '6'}">
								<LI>
							<span>
										<digi:link href="/getTeamActivities.do">
									<div title='<digi:trn key="aim:clickToViewM&E">Click here to view M & E</digi:trn>'>
											<digi:trn key="aim:m&e">M&E</digi:trn>
									</div>
										</digi:link>
							</span>
								</LI>
							</c:if>
							
						</module:display>
						
						<c:if test="${selectedTab  == '7'}">
						   	<LI>
						   		<a name="node">
								<div>
									<digi:trn key="aim:workspaceaudittrail">Audit Trail</digi:trn>								
								</div>
								</a>
							</LI>
						</c:if>	
						
						<c:if test="${selectedTab != '7'}">
							<LI>
						   		<span>
									<digi:link href="/teamAuditList.do">
									<div title='<digi:trn key="aim:clickToViewWorkspaceaudittrail">Click here to view Audit Trail</digi:trn>'>
										<digi:trn key="aim:aim:workspaceaudittrail">Audit Trail</digi:trn>
									</div>	
									</digi:link>							
						   		</span>
							</LI>
						</c:if>

						<c:if test="${selectedTab  == '8'}">
						   	<LI>
						   		<a name="node">
								<div>
									<digi:trn key="aim:workspacedesktoptabs">Desktop Tabs</digi:trn>								
								</div>
								</a>
							</LI>
						</c:if>	
						
						<c:if test="${selectedTab != '8'}">
							<LI>
						   		<span>
									<digi:link href="/teamDesktopTabList.do"> 
									<div title='<digi:trn key="aim:clickToViewWorkspaceDesktopTabs">Click here to view Desktop Tabs</digi:trn>'>
										<digi:trn key="aim:workspacedesktoptabs">Desktop Tabs</digi:trn>
									</div>	
									</digi:link>							
						   		</span>
							</LI>
						</c:if>

					</UL>

				</DIV>



				<logic:notPresent  name="selectedSubTab">
                    <div id="mainEmpty" />
                </logic:notPresent>
				<logic:present  name="selectedSubTab">
				<div id="main">
				<DIV id="subtabs">
					<UL>
					<c:if test="${selectedTab  == '4'}">
						<c:if test="${selectedSubTab  == '0'}">
						   	<LI>
                                <span>
									<digi:trn key="aim:workspaceDocuments">Documents</digi:trn>								
                                </span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '0'}">
							<LI>
								<div title='<digi:trn key="aim:clickToViewWorkspaceDocuments">Click here to view Documents</digi:trn>'>
									<span>
										<bean:define id="subtabDocuments" value="0" />
										<digi:link href="/relatedLinksList.do" paramId="subtab" paramName="subtabDocuments" >	
										<digi:trn key="aim:workspaceDocuments">Documents</digi:trn>
									</digi:link>							
									</span>
								</div>	
							</LI>
						</c:if>
						<c:if test="${selectedSubTab  == '1'}">
						   	<LI>
									<span>
									<digi:trn key="aim:workspaceLinks">Links</digi:trn>								
									</span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '1'}">
							<LI>
								<div title='<digi:trn key="aim:clickToViewWorkspaceLinks">Click here to view Links</digi:trn>'>
									<span>
									<bean:define id="subtabLinks" value="1" />
									<digi:link href="/relatedLinksList.do" paramId="subtab" paramName="subtabLinks">	
										<digi:trn key="aim:workspaceLinks">Links</digi:trn>
									</digi:link>							
									</span>
								</div>	
							</LI>
						</c:if>					
					</c:if>
					
					<c:if test="${selectedTab  == '2'}">
						<c:if test="${selectedSubTab  == '0'}">
						   	<LI>
                                <span>
                                <digi:trn key="aim:workspaceActivitiesMember">Member</digi:trn>								
                                </span>
							</LI>
						</c:if>
                        
						<c:if test="${selectedSubTab != '0'}">
							<LI>
								<div title='<digi:trn key="aim:clickToViewWorkspaceActivitiesMember">Click here to view member activities</digi:trn>'>
									<span>
									<digi:link href="/teamActivityList.do" name="urlParams">
										<digi:trn key="aim:workspaceActivitiesMember">Member</digi:trn>
									</digi:link>							
									</span>
								</div>	
							</LI>
						</c:if>
						<c:if test="${selectedSubTab  == '1'}">
						   	<LI>
									<span>
									<digi:trn key="aim:workspaceActivitiesDonor">Donor</digi:trn>								
									</span>
							</LI>
						</c:if>
								
                        <c:if test="${aimTeamActivitiesForm.donorFlag == false}">
                            <c:if test="${selectedSubTab  == '2'}">
                                <LI>
                                        <span>
                                            <digi:trn key="aim:unassignedactivities">
                                                Unassigned Activities
                                            </digi:trn>
                                        </span>
                                </LI>
                            </c:if>
                            <c:if test="${selectedSubTab != '2'}">
                                <LI>
                                    <div title='<digi:trn key="aim:clickToViewWorkspaceActivitiesDonor">Click here to view Donor activities</digi:trn>'>
                                        <span>
                                        <digi:link href="/updateTeamActivity.do" name="urlParams">
                                            <digi:trn key="aim:unassignedactivities">
                                                Unassigned Activities
                                            </digi:trn>
                                        </digi:link>							
                                        </span>
                                    </div>	
                                </LI>
                            </c:if>					
                            </c:if>					
					</c:if>
					<c:if test="${selectedTab  == '3'}">
						<c:if test="${selectedSubTab  == '0'}">
						   	<LI>
									<span>
									<digi:trn key="aim:assignedreports">Assigned</digi:trn>								
									</span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '0'}">
							<LI>
								<div title='<digi:trn key="aim:clickToViewWorkspaceActivitiesDonor">Click here to view Donor activities</digi:trn>'>
									<span>
									<digi:link href="/teamReportList.do" name="urlParams">
										<digi:trn key="aim:assignedreports">Assigned</digi:trn>
									</digi:link>							
									</span>
								</div>	
							</LI>
						</c:if>					
						<c:if test="${selectedSubTab  == '1'}">
						   	<LI>
                                <span>
                                <digi:trn key="aim:unassignedreports">Unassigned</digi:trn>								
                                </span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '1'}">
							<LI>
								<div title='<digi:trn key="aim:listofunassignedreports">List of unassigned reports</digi:trn>'>
									<span>
									<a href="javascript:listReports()">
										<digi:trn key="aim:unassignedreports">Unassigned</digi:trn>
									</a>							
									</span>
								</div>	
							</LI>
						</c:if>
					</c:if>
					<c:if test="${selectedTab  == '8'}">
						<c:if test="${selectedSubTab  == '0'}">
						   	<LI>
									<span>
									<digi:trn key="aim:assignedreports">Assigned</digi:trn>								
									</span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '0'}">
							<LI>
								<div title='<digi:trn key="aim:clickToViewWorkspaceActivitiesDonor">Click here to view Donor activities</digi:trn>'>
									<span>
									<digi:link href="/teamDesktopTabList.do" name="urlParams">
										<digi:trn key="aim:assignedreports">Assigned</digi:trn>
									</digi:link>							
									</span>
								</div>	
							</LI>
						</c:if>					
						<c:if test="${selectedSubTab  == '1'}">
						   	<LI>
                                <span>
                                <digi:trn key="aim:unassignedreports">Unassigned</digi:trn>								
                                </span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '1'}">
							<LI>
								<div title='<digi:trn key="aim:listofunassignedreports">List of unassigned reports</digi:trn>'>
									<span>
									<a href="javascript:listReports()">
										<digi:trn key="aim:unassignedreports">Unassigned</digi:trn>
									</a>							
									</span>
								</div>	
							</LI>
						</c:if>
					</c:if>
					<c:if test="${selectedTab  == '0'}">
						<c:if test="${selectedSubTab  == '0'}">
						   	<LI>
						   		<span>
									<digi:trn key="aim:workspaceGeneralSettings">General Settings</digi:trn>
								</span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '0'}">
							<LI>
								<div title='<digi:trn key="aim:clickToViewWorkspaceGeneralSettings">Click here to view General Settings</digi:trn>'>
						   			<span>
										<digi:link href="/workspaceOverview.do" name="urlParams">
											<digi:trn key="aim:workspaceGeneralSettings">General Settings</digi:trn>
										</digi:link>							
						   			</span>
								</div>	
							</LI>
						</c:if>
						<c:if test="${selectedSubTab  == '1'}">
						   	<LI>
							   	<span>
									<digi:trn key="aim:workspaceApplicationSettings">Application Settings</digi:trn>								
							   	</span>
							</LI>
						</c:if>
						<c:if test="${selectedSubTab != '1'}">
							<LI>
								<div title='<digi:trn key="aim:clickToViewWorkspaceApplicationsSettings">Click here to view Applications Settings</digi:trn>'>
									<span>
										<digi:link href="/defaultSettings.do">
											<digi:trn key="aim:workspaceApplicationSettings">Application Settings</digi:trn>
										</digi:link>							
									</span>	
								</div>
							</LI>
						</c:if>
						<c:if test="${childWorkspaces == 'enabled'}" >
							<c:if test="${selectedSubTab  == '2'}">
							   	<LI>
									<span>
										<digi:trn key="aim:workspaceChildWorkspaces">Child Workspaces</digi:trn>								
									</span>
								</LI>
							</c:if>
							<c:if test="${selectedSubTab != '2'}">
								<LI>
									<div title='<digi:trn key="aim:clickToViewWorkspaceChildWorkspaces">Click here to view Child Workspaces</digi:trn>'>
										<span>
											<digi:link href="/workspaceOverview.do" name="urlParamsChild" >
												<digi:trn key="aim:workspaceChildWorkspaces">Child Workspaces</digi:trn>
											</digi:link>							
										</span>
									</div>	
								</LI>
							</c:if>
						</c:if>
					</c:if>
					</UL>
				&nbsp;
				</DIV>
</div>

				</logic:present>

</div>

