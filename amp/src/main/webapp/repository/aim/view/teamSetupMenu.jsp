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


<link type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/css_2/tabs.css"/>" rel="stylesheet" />


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


			
				<ul style="overflow: hidden" class="desktop_tab_base ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
						<c:if test="${selectedTab == '0'}">
							<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
								<a class="tab_link">
									<digi:trn key="aim:workspaceConfiguration">Configuration</digi:trn>
								</a>
							</li>
						</c:if>	
						<c:if test="${selectedTab != '0'}">
							<li class="desktop_tab ui-state-default ui-corner-top">
							<digi:link href="/workspaceOverview.do" name="urlParams" styleClass="tab_link">
								<digi:trn key="aim:workspaceConfiguration">Configuration</digi:trn>
							</digi:link>
							</li>
						</c:if>

						<c:if test="${selectedTab == '1'}">
							<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
								<a class="tab_link">
									<digi:trn key="aim:members">Members</digi:trn>
								</a>
							</li>
						</c:if>
						<c:if test="${selectedTab != '1'}">
						<li class="desktop_tab ui-state-default ui-corner-top">
							<digi:link href="/teamMemberList.do" styleClass="tab_link">
								<digi:trn key="aim:members">Members</digi:trn>
							</digi:link>
							</li>
						</c:if>
					
					<c:if test="${teamAccessTypeLocal != 'Management' }">
						<c:if test="${selectedTab != '2'}">
							<li class="desktop_tab ui-state-default ui-corner-top">
								<digi:link href="/teamActivityList.do" name="urlParams" styleClass="tab_link">
									<digi:trn key="aim:activities">Activities</digi:trn>
								</digi:link>
							</li>
						</c:if>
						<c:if test="${selectedTab == '2'}">
							<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
								<a class="tab_link">
									<digi:trn key="aim:activities">Activities</digi:trn>
								</a>
							</li>
						</c:if>
					</c:if>
					
					<c:if test="${selectedTab == '3'}">
						<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
							<a class="tab_link">
								<digi:trn key="aim:workspaceReports">Reports</digi:trn>
							</a>
						</li>
					</c:if>
					<c:if test="${selectedTab != '3'}">
						<li class="desktop_tab ui-state-default ui-corner-top">
							<digi:link href="/teamReportList.do~reset=true" styleClass="tab_link">
								<digi:trn key="aim:workspaceReports">Reports</digi:trn>
							</digi:link>
						</li>
					</c:if>
					
					<c:if test="${selectedTab == '4'}">
						<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
							<a class="tab_link">
								<digi:trn key="fm:documentmanagement">Document Management</digi:trn>
							</a>	
						</li>
					</c:if>
					<c:if test="${selectedTab != '4'}">
						<li class="desktop_tab ui-state-default ui-corner-top">
							<bean:define id="subtabLink" value="0" />
							<digi:link href="/relatedLinksList.do" onclick="return putLoading();" paramId="subtab" paramProperty="subtabLink" styleClass="tab_link">
								<digi:trn key="fm:documentmanagement">Document Management</digi:trn>
							</digi:link>
						</li>
					</c:if>
					
					
					<module:display name="Trend Analysis and Forecasting" parentModule="TREND ANALYSIS">
						<c:if test="${selectedTab == '6'}">
							<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
								<a class="tab_link" >
									<digi:trn key="aim:m&e">M&E</digi:trn>
								</a>
							</li>
						</c:if>
						<c:if test="${selectedTab != '6'}">
							<li class="desktop_tab ui-state-default ui-corner-top">
								<digi:link href="/getTeamActivities.do" styleClass="tab_link" >
									<digi:trn key="aim:m&e">M&E</digi:trn>
								</digi:link>
							</li>
						</c:if>
					</module:display>
					
					<c:if test="${selectedTab  == '7'}">
						<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
							<a class="tab_link">
								<digi:trn key="aim:workspaceaudittrail">Audit Trail</digi:trn>
							</a>
						</li>
					</c:if>
					<c:if test="${selectedTab  != '7'}">
						<li class="desktop_tab ui-state-default ui-corner-top">
							<digi:link href="/teamAuditList.do" styleClass="tab_link">
								<digi:trn key="aim:workspaceaudittrail">Audit Trail</digi:trn>
							</digi:link>
						</li>
					</c:if>

					<c:if test="${selectedTab  == '8'}">
						<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
							<a class="tab_link">
								<digi:trn key="aim:workspacedesktoptabs">Desktop Tabs</digi:trn>
							</a>
						</li>
					</c:if>					
					<c:if test="${selectedTab  != '8'}">
						<li class="desktop_tab ui-state-default ui-corner-top">
							<digi:link href="/teamDesktopTabList.do" styleClass="tab_link">
								<digi:trn key="aim:workspacedesktoptabs">Desktop Tabs</digi:trn>
							</digi:link>
						</li>
					</c:if>
				</ul>

				
				<div class="ui-tabs-panel ui-widget-content ui-corner-bottom">
				<!-- SubTabs -->
				<logic:present  name="selectedSubTab">
						<c:if test="${selectedTab  == '4'}">
							<c:if test="${selectedSubTab  == '0'}">
								<span class="bread_sel">
										<digi:trn key="aim:workspaceDocuments">Documents</digi:trn>								
	              </span>
							</c:if>
							<c:if test="${selectedSubTab != '0'}">
								<bean:define id="subtabDocuments" value="0" />
								<digi:link href="/relatedLinksList.do" paramId="subtab" paramName="subtabDocuments" styleClass="l_sm">	
									<digi:trn key="aim:workspaceDocuments">Documents</digi:trn>
								</digi:link>							
							</c:if>
							<span class="breadcrump_sep">|</span>
							<c:if test="${selectedSubTab  == '1'}">
								<span class="bread_sel">
									<digi:trn key="aim:workspaceLinks">Links</digi:trn>								
								</span>
							</c:if>
							<c:if test="${selectedSubTab != '1'}">
								<bean:define id="subtabLinks" value="1" />
								<digi:link href="/relatedLinksList.do" paramId="subtab" paramName="subtabLinks" styleClass="l_sm">
									<digi:trn key="aim:workspaceLinks">Links</digi:trn>
								</digi:link>
							</c:if>
						</c:if>
						
						<c:if test="${selectedTab  == '2'}">
							<c:if test="${selectedSubTab  == '0'}">
	              <span class="bread_sel">
		              <digi:trn key="aim:workspaceActivitiesMember">Member</digi:trn>								
	              </span>
							</c:if>
							<c:if test="${selectedSubTab != '0'}">
								<digi:link href="/teamActivityList.do" name="urlParams" styleClass="l_sm">
									<digi:trn key="aim:workspaceActivitiesMember">Member</digi:trn>
								</digi:link>							
							</c:if>
							<span class="breadcrump_sep">|</span>

							<c:if test="${selectedSubTab  == '1'}">
								<span class="bread_sel">
									<digi:trn key="aim:workspaceActivitiesDonor">Donor</digi:trn>								
								</span>
							</c:if>
									
	            	<c:if test="${aimTeamActivitiesForm.donorFlag == false}">
	            	<field:display name="Unassigned Activities" feature="Workspace Info">
	            	<c:if test="${selectedSubTab  == '2'}">
	            		<span class="bread_sel">
										<digi:trn key="aim:unassignedactivities">Unassigned Activities</digi:trn>
									</span>
								</c:if>
								<c:if test="${selectedSubTab != '2'}">
									<digi:link href="/updateTeamActivity.do" name="urlParams" styleClass="l_sm">
										<digi:trn key="aim:unassignedactivities">Unassigned Activities</digi:trn>
									</digi:link>							
								</c:if>	
							<span class="breadcrump_sep">|</span>
					</field:display>					
	          			</c:if>	
	          			
	          			<field:display name="Non-archived Activities" feature="Workspace Info">
	          			<c:if test="${selectedSubTab  == '3'}">
                             	<span class="bread_sel">
                                         <digi:trn>Non-Archived Activities</digi:trn>
                                </span>
                        </c:if>
                         <c:if test="${selectedSubTab != '3'}">
                                     <digi:link href="/teamActivityList.do~showArchivedActivities=false" name="urlParams" styleClass="l_sm">
                                         <digi:trn>Non-Archived Activities</digi:trn>
                                     </digi:link>							
                         </c:if>
                         <span class="breadcrump_sep">|</span>
                         </field:display>
                         <field:display name="Archived Activities" feature="Workspace Info">
                          <c:if test="${selectedSubTab  == '4'}">
                             <span class="bread_sel">
                                         <digi:trn>Archived Activities</digi:trn>
                                     </span>
                         </c:if>
                         <c:if test="${selectedSubTab != '4'}">
                                     <digi:link href="/teamActivityList.do~showArchivedActivities=true" name="urlParams" styleClass="l_sm">
                                         <digi:trn>Archived Activities</digi:trn>
                                     </digi:link>							
                         </c:if>	
	          			</field:display>			
						</c:if>
							
						
						
						<c:if test="${selectedTab  == '3'}">
							<c:if test="${selectedSubTab  == '0'}">
								<span class="bread_sel">
									<digi:trn key="aim:assignedreports">Assigned</digi:trn>
								</span>
							</c:if>
							<c:if test="${selectedSubTab != '0'}">
								<digi:link href="/teamReportList.do~reset=true" name="urlParams" styleClass="l_sm">
									<digi:trn key="aim:assignedreports">Assigned</digi:trn>
								</digi:link>
							</c:if>
							<span class="breadcrump_sep">|</span>
							<c:if test="${selectedSubTab  == '1'}">
								<span class="bread_sel">
									<digi:trn key="aim:unassignedreports">Unassigned</digi:trn>
								</span>
							</c:if>
							<c:if test="${selectedSubTab != '1'}">
								<a href="javascript:listReports()" class="l_sm">
									<digi:trn key="aim:unassignedreports">Unassigned</digi:trn>
								</a>
							</c:if>
						</c:if>
						
						<c:if test="${selectedTab  == '8'}">
							<c:if test="${selectedSubTab  == '0'}">
								<span class="bread_sel">
									<digi:trn key="aim:assignedreports">Assigned</digi:trn>
								</span>
							</c:if>
							<c:if test="${selectedSubTab != '0'}">
								<digi:link href="/teamDesktopTabList.do" name="urlParams" styleClass="l_sm">
									<digi:trn key="aim:assignedreports">Assigned</digi:trn>
								</digi:link>
							</c:if>
							<span class="breadcrump_sep">|</span>
							<c:if test="${selectedSubTab  == '1'}">
								<span class="bread_sel">
									<digi:trn key="aim:unassignedreports">Unassigned</digi:trn>
								</span>
							</c:if>
							<c:if test="${selectedSubTab != '1'}">
								<a href="javascript:listReports()" class="l_sm">
									<digi:trn key="aim:unassignedreports">Unassigned</digi:trn>
								</a>
							</c:if>
						</c:if>
						

						<c:if test="${selectedTab  == '0'}">
							<c:if test="${selectedSubTab  == '0'}">
								<span class="bread_sel">
									<digi:trn key="aim:workspaceGeneralSettings">General Settings</digi:trn>
								</span>
							</c:if>
							<c:if test="${selectedSubTab != '0'}">
								<digi:link href="/workspaceOverview.do" name="urlParams" styleClass="l_sm">
									<digi:trn key="aim:workspaceGeneralSettings">General Settings</digi:trn>
								</digi:link>							
							</c:if>
							<span class="breadcrump_sep">|</span>
							<c:if test="${selectedSubTab  == '1'}">
								<span class="bread_sel">
									<digi:trn key="aim:workspaceApplicationSettings">Application Settings</digi:trn>								
								</span>
							</c:if>
							<c:if test="${selectedSubTab != '1'}">
								<digi:link href="/defaultSettings.do" styleClass="l_sm">
									<digi:trn key="aim:workspaceApplicationSettings">Application Settings</digi:trn>
								</digi:link>							
							</c:if>
							<c:if test="${childWorkspaces == 'enabled'}" >
								<span class="breadcrump_sep">|</span>
								<c:if test="${selectedSubTab  == '2'}">
									<span class="bread_sel">
										<digi:trn key="aim:workspaceChildWorkspaces">Child Workspaces</digi:trn>								
									</span>
								</c:if>
								<c:if test="${selectedSubTab != '2'}">
									<digi:link href="/workspaceOverview.do" name="urlParamsChild" styleClass="l_sm">
										<digi:trn key="aim:workspaceChildWorkspaces">Child Workspaces</digi:trn>
									</digi:link>							
								</c:if>
							</c:if>
						</c:if>	
						<br>&nbsp;
				</logic:present>	
				
