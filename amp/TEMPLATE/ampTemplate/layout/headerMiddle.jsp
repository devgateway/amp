<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
       
        <!-- Dependencies --> 
	<%@page import="org.digijava.module.aim.helper.Constants"%>
	<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
        
     	<!-- Menu's Stylesheet-->
		<link rel="stylesheet" href="css_2/jquery.jdMenu.css" type="text/css" />


<script language="JavaScript">
	function addActivity() {
		selectedLevelId=0; 
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~resetMessages=true~action=create~activityLevelId="+selectedLevelId;	
	}
	
	<jsp:include page="gisMenu.jsp" />
	
	function teamWorkspaceSetup(a) {
		if (canExit()) window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";
		else return false;	
	}
	function addMessage(fillTypesAndLevels) {
		window.location.href="/message/messageActions.do?editingMessage=false&actionType="+fillTypesAndLevels;
	}

	function help(){
	 <digi:context name="rev" property="/help/help.do~blankPage=true" />
		openURLinWindow("<%=rev%>",1024,768);
	}
	
	


</script>

		
<jsp:include page="/repository/aim/view/ar/aboutScripts.jsp"/>
<!--<div id="myAboutFilterWrapper" style="display: none;" >-->
	<div id="customAbout" style="display: none" class="content">
		<jsp:include page="/repository/aim/view/helpAbout.jsp" />
	</div>
<!--</div>-->

<div id="myUserFilterWrapper" style="display: none;" >
	<div id="customUser" style="display: none" class="content" >
		<jsp:include page="/repository/aim/view/workspacePopup.jsp" />
	</div>
</div>

        <!-- Core + Skin CSS -->
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />


<c:set var="message">
    <c:choose>
      <c:when test="${sessionScope.currentMember.addActivity == 'true'}">
      	<digi:trn>You did not save your activity. Do you want proceed without saving it ?</digi:trn>
      </c:when>
      <c:otherwise>
      	<digi:trn>WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
      </c:otherwise>
    </c:choose>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>

<logic:notPresent name="currentMember">
<% 
	String publicView=FeaturesUtil.getGlobalSettingValue("Public View");
	if("On".equals(publicView)) { 
	%>
<!-- AMP MENU BEGIN-->
	<div class="main_menu">
		<div class="centering">
	        <ul class="jd_menu" id="jd_menu">
	        	<li>
	        		<a href="/" module="aim" title="${trn3}">
                		<digi:trn>Home Page</digi:trn>
	            	</a>
	            </li>
	        	 <module:display name="Public Site" parentModule="PUBLIC VIEW">
	          	 	<c:set var="message">
                		<digi:trn>WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
               	 	</c:set>
	            	<li>
	            		 <a href="/showDesktop.do" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()" >
                     		 <digi:trn>PUBLIC SITE</digi:trn>
                     	</a>
                 	</li>
				<module:display name="Public Site" parentModule="PUBLIC VIEW">
                 	<li>
						<a href="/showDesktop.do" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()" >
							<digi:trn>Public Documents</digi:trn></a>
                     	</a>
                 	</li>
                </module:display>
            	<module:display name="Public Reports" parentModule="PUBLIC VIEW">
					<li>
						<a href="/viewTeamReports.do?tabs=false" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px;"  onclick="return canExit()">
                    		<digi:trn>Public Reports</digi:trn><img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 />
                		</a>
                		<ul>
                			<module:display name="Multi-dimensional Reports Public View" parentModule="REPORTING">
                			<li>
	 							<digi:link href="/mainreports.do" module="mondrian" >
	                   				<digi:trn>Multi-dimensional Reports</digi:trn>
	                   			</digi:link>
	               			</li>
	            				
	            			</module:display>
	            		</ul>
           			</li>
           		</module:display>
           	   	<module:display name="Calendar" parentModule="PROJECT MANAGEMENT">
	           		<module:display name="Public Calendar" parentModule="PUBLIC VIEW">
                	<li>
                		<a href="/calendar/showCalendarView.do?view=none&filterInUse=false" onclick="return canExit()">
                       	<digi:trn>Calendar</digi:trn>
                    	</a>
                	</li>
            		</module:display>
            	</module:display>    
				<module:display name="Public Dashboards" parentModule="PUBLIC VIEW">
	            	<li>
	                	<a style="cursor: pointer;">
	                		<digi:trn key="aim:medashboard">DASHBOARDS</digi:trn><img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 />
	                	</a>
	                    <ul>
	                    	<module:display name="Org Profile" >
	                        	<feature:display name="Enable Org. Profile in Public View" module="Org Profile">
		                            	<li>
			                              	<a href="${pageContext.request.contextPath}/orgProfile/showOrgProfile.do?reset=true"  onclick="return canExit()">
			                                	<digi:trn key="fm:orgProfile">Org. Profile</digi:trn>
			                                </a> 
	                                	</li>    
                                </feature:display>
							</module:display>
							<module:display name="GIS DASHBOARD" >
								<feature:display name="Show in public view" module="GIS DASHBOARD">
		                            <li>
		                              	<a href="${pageContext.request.contextPath}/gis/showPublicGis.do?public=true"  onclick="return canExit()">
		                                	<digi:trn key="fm:gis">GIS Dashboard</digi:trn>
		                                </a> 
	                                </li>
                              	</feature:display>
							</module:display>
						</ul>
					</li>
				</module:display>					
</module:display>
</ul>
</div>
</div>
<% } 
else  //In case the public view aren't activated
{
%>
<div class="main_menu">
	<div class="centering">
		<ul class="jd_menu">
	    	<li>
	        	<a  href="/" module="aim" title="${trn3}">
                	<digi:trn>Home Page</digi:trn>
	            </a>
			</li>
			<module:display name="Login - User Management" parentModule="PUBLIC VIEW">
        		<feature:display name="Enable New User Registration" module="Login - User Management">
	            <li>
	                <digi:link href="/showRegisterUser.do" module="aim" title="${trn3}">
		                <digi:trn key="aim:newUserRegistration">
	                		New user registration
	    	            </digi:trn>
		            </digi:link>
	            </li>
            	</feature:display>
            </module:display>
			<feature:display name="Language Option" module="Tools">
				<li>
		        	<a style="cursor: pointer;">
		        		<digi:trn key="aim:deflanguage">Language</digi:trn>
		            </a>    
	                <div>
	                    <div>                    
	                        <ul>
	                        	<digi:insert flush="false" attribute="dropdownLangSwitch" />
	                        </ul>
	                    </div>
	                </div>                              
		        </li>
		  </feature:display>
		</ul>
	</div>
</div>
<%
}
%>
</logic:notPresent>
<logic:present name="ampAdmin" scope="session">
<logic:equal name="ampAdmin" value="yes">
<div class="main_menu">
		<div class="centering">
	        <ul class="jd_menu">
	        	<module:display name="My Desktop" parentModule="PROJECT MANAGEMENT">
	            <li>
                 	<c:set var="message">
                    	<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                 	</c:set>
					<a href="/admin.do" module="aim" >
						<digi:trn key="aim:aminTools">ADMIN TOOLS</digi:trn>
					</a>
				</li>
				<module:display name="Tools" parentModule="PROJECT MANAGEMENT">
					<li>
                    	<a style="cursor: pointer;">
							<digi:trn key="aim:tools">TOOLS</digi:trn>
                        </a>
						<ul>
	                        <feature:display name="Language Option" module="Tools">
			                	<li>
			                    	<a style="cursor: pointer;"><digi:trn key="aim:deflanguage">Language</digi:trn></a>
			                   			<ul>
											<digi:insert flush="false" attribute="dropdownLangSwitch" />
			                            </ul>
			                    </li>
			               	</feature:display>
	                        <logic:notEmpty name="currentMember" scope="session">
	                        	<digi:secure actions="TRANSLATE">
	                            	<digi:insert flush="false" attribute="transmode"/>
	                            </digi:secure>
							</logic:notEmpty>                                        
                        </ul>
					</li>
				</module:display>
			 	<module:display name="HELP">
				<li>
                	<a style="cursor: pointer;">
                    	<digi:trn key="help:help">HELP</digi:trn>
                    </a>
					<ul>
						<feature:display name="Admin Help" module="HELP">
                     	<li>
                        	<a  onClick="adminHelp();" style="cursor: pointer;">
								<digi:trn>AMP Admin Help</digi:trn>
							</a>
						</li>
						</feature:display>
						<feature:display name="User Help" module="HELP">
                        <li>
                        	<a onClick="help();" style="cursor: pointer;">
                            	<digi:trn key="aim:AMPHelp">AMP Help</digi:trn>
							</a>
						</li>
						</feature:display>
						<feature:display name="Glossary" module="HELP">
                        <li>	
                        	<a href="/help/glossary.do">
								<digi:trn>Glossary</digi:trn>
                            </a>
						</li>
						</feature:display>
						<feature:display name="Support Request Form" module="HELP">
                       	<li>	
                            <a  href="http://support.ampdev.net/login.action?code=<%=FeaturesUtil.getDefaultCountryIso()%>" target="_blank">
								<digi:trn key="aim:supportrequestform">Support Request Form</digi:trn>
							</a>
                          </li>
						</feature:display>
						<feature:display name="About AMP" module="HELP">
                        <li>
							<%
							siteDomain = (org.digijava.kernel.request.SiteDomain) request.getAttribute(org.digijava.kernel.Constants.CURRENT_SITE);
							session.setAttribute("site", siteDomain);
							%>
                            <a target="name" onClick="showAbout(); return false;" style="cursor: pointer;">
                               <digi:trn key="aim:aboutamp">About AMP</digi:trn>
							</a>
                        </li>
						</feature:display>
					</ul>
				</li>
			</module:display>
		</module:display>
	</ul>
</div>
</div>
</logic:equal>
</logic:present>
<logic:present name="ampAdmin" scope="session">
	<logic:equal name="ampAdmin" value="no">
	<!-- AMP MENU BEGIN-->
	<div class="main_menu">
		<div class="centering">
	        <ul class="jd_menu">
	        	<!-- DESKTOP -->
	            <module:display name="My Desktop" parentModule="PROJECT MANAGEMENT">
	            <li>
	            	 <a href="/showDesktop.do" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()" >
                     	<digi:trn key="aim:mydesktop">My Desktop</digi:trn>
                     </a>
                     <img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 />
	                <ul>
	                	<c:if test="${not empty sessionScope.currentMember}">
							<c:if test="${sessionScope.currentMember.addActivity == 'true'}">
			                	<field:display name="Add Activity Button" feature="Edit Activity">
									<li>
										<c:set var="trnClickToAddNewActivit">
											<digi:trn key="aim:clickToAddNewActivit">Click here to Add New Activity</digi:trn>
		                               	</c:set>
										<a href="javascript:addActivity()" onclick="return canExit()" title="${trnClickToAddNewActivit}">
											<digi:trn key="btn:addActivity">Add Activity</digi:trn>
		                                </a>
									</li>
								</field:display>
							</c:if>
						</c:if>
	                	 <module:display name="Tab Generator" parentModule="REPORTING">
	                    <li>
	                    	<a onclick="return canExit()" href="/reportWizard.do?tab=true&reset=true">
								<digi:trn key="aim:addTab">Add Tab</digi:trn>
		                   </a>
	                    </li>
	                     <li title='<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>
							<a onclick="return canExit()" href="/viewTeamReports.do?tabs=true">
		                    	<digi:trn key="aim:tabManager">Tab Manager</digi:trn>
		                    </a>
						</li>
	                    </module:display>
		                	<c:if test="${not empty sessionScope.currentMember}">
								<c:if test="${sessionScope.currentMember.pledger == 'true'}">
			 						<module:display name="Pledges" parentModule="PROJECT MANAGEMENT">
				                		<li title='<digi:trn key="aim:clickToManagePledges">Click here to manage pledges</digi:trn>'>
					                		<a onclick="return canExit()" href="/viewPledgesList.do">
					                    		<digi:trn key="aim:Pledges">Pledges</digi:trn>
					                    	</a>
										</li>
									</module:display>
								</c:if>
							</c:if>
	                </ul>
	            </li>
	            </module:display>
	            <!-- END DESKTOP -->
	            <!-- REPORTING -->
	            <module:display name="REPORTING">
                 <li>
                   	<a href="/viewTeamReports.do?tabs=false" >
                     	<digi:trn key="aim:reports">Reports</digi:trn>
                    </a>
                   	<img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 />
       				<ul>
                     	<module:display  name="Report Generator" parentModule="REPORTING">
                    	<li>
                    		<a href="/reportWizard.do?tabs=false&reset=true" onclick="return canExit()">
	                        	<digi:trn key="aim:reportGenerator">Report Generator</digi:trn>
	                        </a>
                    	</li>
                    	</module:display>
                    	<module:display name="Multi-dimensional Reports" parentModule="REPORTING">
                    	<li>
                    		<digi:link href="/mainreports.do" module="mondrian">
                    			<digi:trn key="aim:multidimensionalreports">Multi-dimensional Reports</digi:trn>
                    		</digi:link>
                    	</li>
                   		</module:display>
                    	 <feature:display name="Paris Indicators Reports" module="Portfolio">
                    	 	<li title='<digi:trn key="aim:clickToViewParisIndcReports">Click here to view Paris Indicator Reports</digi:trn>'>
                    			<a href="/parisindicator/parisindicator.do?reportId=3&reset=true&header=true"  onclick="return canExit()">
                            		<digi:trn>Paris Indicator</digi:trn>
								</a>
							</li>
						</feature:display>
						<feature:display name="Pledges Default Reports" module="Multi-dimensional Reports">
                        	<li title='<digi:trn>Click here to view Pledges Reports</digi:trn>'>
                                <a href="/reportWizard.do?tabs=false&reset=true&type=5"  onclick="return canExit()">
                                	<digi:trn key="aim:parisIndicator">Pledges Reports</digi:trn>
								</a>
                             </li>
						</feature:display>
                	</ul>
            	</li>
            	</module:display>
            	<!-- END REPORTING -->
            	<!-- RESOURCES -->
	             <feature:display name="Content Repository" module="Document Management">
	            	<li>
	            		<a href="/contentrepository/documentManager.do" onclick="return canExit()">
							<digi:trn key="aim:resources">Resources</digi:trn>
						</a>
					</li>
	             </feature:display>
	             <!-- END RESOURCES -->
	             <!-- CALENDAR -->
				<module:display name="Calendar" parentModule="PROJECT MANAGEMENT">
				<!-- On which view calendar link should redirect. depends on FM -->
					<c:set var="linkYearly">
						<feature:display name="Yearly View" module="Calendar">
							/calendar/showCalendarView.do?filterInUse=false
						</feature:display>
					 </c:set>
					 <c:set var="linkMonthly">
						<feature:display name="Monthly View" module="Calendar">
					  		/calendar/showCalendarView.do?view=monthly&filterInUse=false
					  	</feature:display>
					 </c:set>
					 <c:set var="linkWeekly">
						<feature:display name="Weekly View" module="Calendar">
					  		/calendar/showCalendarView.do?view=weekly&filterInUse=false
					  	</feature:display>
					 </c:set>
					 <c:set var="linkDaily">
					 	<feature:display name="Daily View" module="Calendar">
					  		/calendar/showCalendarView.do?view=daily&filterInUse=false
					  	</feature:display>
					  </c:set>
					<c:set var="link">
					  	<c:choose>
					  		<c:when test="${not empty linkYearly}">${linkYearly}</c:when>
					  		<c:when test="${not empty linkMonthly}">${linkMonthly}</c:when>
					  		<c:when test="${not empty linkWeekly}">${linkWeekly}</c:when>
					  		<c:when test="${not empty linkDaily}">${linkDaily}</c:when>
					  	</c:choose>
					  </c:set>
					 <li>
						 <c:if test="${not empty link}">
						 	<a href="${link}" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()">
						    	<digi:trn key="aim:calendar">Calendar</digi:trn><img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 />
						    </a>
						 </c:if>
					     <c:if test="${empty link}">
					     	<a href="/calendar/showCalendarView.do?view=none&filterInUse=false" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()">
					        	<digi:trn key="aim:calendar">Calendar</digi:trn><img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 />
					       	</a>
					     </c:if>
					     <feature:display name="Create Event" module="Calendar">
					    	<ul>
					        	<li>
					            	<a href="/calendar/showCalendarEvent.do~selectedCalendarTypeId=0~method=new"  onclick="return canExit()">
					                	<digi:trn key="aim:calendarAddEvent">Add Event</digi:trn>
									</a>
					           </li>
					       </ul>
					    </feature:display>
					 </li>
				</module:display>
				<!-- END CALENDAR -->
	            <!-- DASHBOARDS -->
	            <%	
					boolean showDashboard = false;
					if(FeaturesUtil.isVisibleFeature("NPD Dashboard", request.getSession().getServletContext())) {
						showDashboard = true;
					}
				  	if(FeaturesUtil.isVisibleModule("GIS DASHBOARD", request.getSession().getServletContext())) {
				  		showDashboard = true;
				  	}
                          if(FeaturesUtil.isVisibleModule("Org Profile", request.getSession().getServletContext())) {
				  		showDashboard = true;
				  	}
				%>
				<%if (showDashboard) {%>
                <li style="cursor: pointer;">
                 	<a>
                 		<digi:trn key="aim:medashboard">DASHBOARDS</digi:trn><img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 />
                 	</a>
                    <ul>
                    	<feature:display name="NPD Dashboard" module="National Planning Dashboard">
	                        <li>
	                        	 <a href="/nationalPlaningDashboard.do"  onclick="return canExit()">
	                         		<digi:trn key="aim:npd">NPD</digi:trn>
	                        	</a>
	                       	</li>
                        </feature:display>
                        <module:display name="GIS DASHBOARD">
	                        <li>
	                        	<a href="javascript:openGIS()"  onclick="return canExit()">
	                            	<digi:trn key="aim:gis">GIS</digi:trn>
	                            </a>
	                        </li>
                        </module:display>
                        <module:display name="Org Profile" >
	                        <li>
	                         	<a href="${pageContext.request.contextPath}/orgProfile/showOrgProfile.do?reset=true"  onclick="return canExit()">
	                               <digi:trn key="fm:orgProfile">Org. Profile</digi:trn>
	                            </a> 
	                       	</li>    
                        </module:display>
					</ul>
				</li>
				<%}%>
				<!-- END DASHBOARDS -->
	            <!-- MESSAGES -->
	            <c:set var="tabIndex">
                	<feature:display name="Event Tab" module="Messages">4</feature:display>
                	<feature:display name="Approval Tab" module="Messages">3</feature:display>
                	<feature:display name="Alert tab" module="Messages">2</feature:display>
                	<feature:display name="Message tab" module="Messages">1</feature:display>
                </c:set>
				<module:display name="Messages" parentModule="Messaging System">
                	<li>
                    	<c:choose>
                        	<c:when test="${fn:contains(tabIndex, '1')}">
                            	<a href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox">
                            </c:when>
                            <c:when test="${fn:contains(tabIndex, '2')}">
                            	<a href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2&childTab=inbox">
                            </c:when>
                            <c:when test="${fn:contains(tabIndex, '3')}">
                            	<a href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3&childTab=inbox">
                            </c:when>
                            <c:when test="${fn:contains(tabIndex, '4')}">
								<a href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4&childTab=inbox">
							</c:when>
                            <c:otherwise>
                            	<a  href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=5&childTab=inbox">
							</c:otherwise>
						</c:choose>
						<digi:trn key="fm:messages">Messages</digi:trn>
                        </a>
						<ul>
                        	<li>
                            	<a href="/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels"  onclick="return canExit()">
									<digi:trn key="aim:createmessage">Create Message</digi:trn>
								</a>
							</li>
						</ul>
					</li>
				</module:display>
				<!-- END MESSAGES -->
				<!-- TOOLS -->
				<module:display name="Tools" parentModule="PROJECT MANAGEMENT">
					<li style="cursor: poi">
						<a><digi:trn key="aim:tools">TOOLS</digi:trn><img src="img_2/menu_arr_dwn.gif" class="menu_arr" border=0 /></a>   
                        <ul>
                        	<feature:display name="Workspace Info" module="Tools">
                            <li>
                            	<a onclick="return teamWorkspaceSetup('-1');" style="cursor: pointer;">
                                	<digi:trn key="aim:workspaceinfo">Workspace Info</digi:trn>
                                </a>
							</li>
                            </feature:display>
                           	
                           	<logic:notEmpty name="currentMember" scope="session">
                            	<digi:secure actions="TRANSLATE">
                                	<digi:insert flush="false" attribute="transmode"/>
                                </digi:secure>
                            </logic:notEmpty>                                        
                            <logic:notEmpty name="currentMember" scope="session">
                            	<digi:secure group="UserSideAdmins">
			                        <li>
				                    	<a href="/aim/switchDevelopmentMode.do">
											<digi:trn key="admin:userdevmode">Developer Mode</digi:trn>
				                         </a>
			                       	</li>
								</digi:secure>
							</logic:notEmpty>                                        
                            <li>
	                            <a href="/search/search.do?reset=true"  onclick="return canExit()">
	                            	<digi:trn key="aim:search">Search</digi:trn>
	                            </a>
	                        </li>
                            <li>
	                        	<a href="/aim/addressBook.do?actionType=viewAddressBook&reset=true">
									<digi:trn>Address Book</digi:trn>
	                            </a>
                            </li>
                        </ul>
					</li>
				</module:display>
				<!-- END TOOLS -->
	            </li>
	        </ul>
		</div>
	</div>
	<!-- MAIN MENU END -->
            
	</logic:equal>
</logic:present>