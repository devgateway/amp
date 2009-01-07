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
		<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>

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
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />

<c:set var="message">
<c:if test="${sessionScope.currentMember.addActivity == 'true'}">
<digi:trn key="aim:activityNotSaved">You did not save your activity. Do you want proceed without saving it ?</digi:trn>
</c:if>
<c:if test="${sessionScope.currentMember.addActivity == 'false'}">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:if>
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
<style>
.yuiampmenuitemlabel
{
border-right:1px solid white;
cursor:pointer;
}
</style>
<div class="yuiamp-skin-amp" style="clear:both;">
    <div id="mainmenuHeader" class="yuiampmenu">
      <div class="bd">
          <ul class="first-of-typeamp">
            <li class="yuiampmenuitem" style="float:left;">
                <c:set var="message">
                <digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                </c:set>
                <digi:link styleClass="yuiampmenuitemlabel" href="/reportsPublicView.do" module="aim" onclick="return quitRnot1('${message}')">
                <digi:trn key='aim:PublicView'>PUBLIC VIEW</digi:trn>
                </digi:link>
            </li>
            <feature:display name="yuiampmenuitemlabel" module="Document Management">
            <li class="yuiampmenuitem" style="float:left;">
                <digi:link styleClass="yuiampmenuitemlabel" href="/documentManager.do" module="contentrepository" onclick="return quitRnot()">			
                <digi:trn key="contentrepository:publicDocuments">Public Documents</digi:trn></digi:link>
            </li>
            </feature:display>
			<li style="float:left;"  title='<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>
               <a class="yuiampmenuitemlabel" href="/viewTeamReports.do?tabs=false">
               <digi:trn key="aim:publicReports">Public Reports</digi:trn>
               </a>
			</li>            
            <li style="float:left;">
                <span class="yuiampmenuitemlabel" href="#" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                <digi:trn key="aim:deflanguage">Language</digi:trn>
                </span>
                 <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                   <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                </a>               
                <div id="reports2" class="yuiampmenu">
                    <div class="bd">                    
                        <ul>
                        <digi:insert attribute="dropdownLangSwitch" />
                        </ul>
                    </div>
                </div>                              
            </li>
          </ul>            
      </div>
  </div>
</div> 

<% } 
else  //In case the public view aren't activated
{
%>
<style>
.yuiampmenuitemlabel
{
border-right:1px solid white;
}
</style>
<div class="yuiamp-skin-amp" style="clear:both;">
    <div id="mainmenuHeader" class="yuiampmenu">
      <div class="bd">
          <ul class="first-of-type">
            <li class="yuiampmenuitem">
                <digi:link styleClass="yuiampmenuitemlabel" href="/showRegisterUser.do" module="aim" title="${trn3}">
                <digi:trn key="aim:newUserRegistration">
                New user registration
                </digi:trn>
	            </digi:link>
            </li>
            <li>
                <span class="yuiampmenuitemlabel" href="#" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                <digi:trn key="aim:deflanguage">Language</digi:trn>
                </span>
                <a  onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                   <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                </a>
                <div id="reports2" class="yuiampmenu">
                    <div class="bd">                    
                        <ul>
                        <digi:insert attribute="dropdownLangSwitch" />
                        </ul>
                    </div>
                </div>                              
            </li>
          </ul>            
      </div>
  </div>
</div> 
<%
}
%>
</logic:notPresent>
<logic:present name="ampAdmin" scope="session">
	<logic:equal name="ampAdmin" value="yes">
<script language="JavaScript">
function help(){
	 <digi:context name="rev" property="/help/help.do~blankPage=true" />
		openURLinWindow("<%=rev%>",1024,768);
	}
function adminHelp(){
		 <digi:context name="admin" property="/help/admin/help.do~blankPage=true" />
		openURLinWindow("<%=admin%>",1024,768);
}
</script>
			 <div class="yuiamp-skin-amp" style="clear:both;">
              <div id="mainmenuHeader" class="yuiampmenu">
                  <div class="bd">
                      <ul class="first-of-type">
                    		  <li class="yuiampmenuitem">
                                          <c:set var="message">
                       							<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                 						  </c:set>
					                    <digi:link style="border-right:1px solid white;" styleClass="yuiampmenuitemlabel"  href="/admin.do" module="aim" onclick="return quitRnot1('${message}')">
					                        <digi:trn key="aim:aminTools">ADMIN TOOLS</digi:trn>
					                    </digi:link>
                                        </li>
                          <li class="yuiampmenuitem">
                              <span class="yuiampmenuitemlabel" href="#" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                 <digi:trn key="aim:tools">TOOLS</digi:trn>
                              </span>
                              <a  onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="tools" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li style="_width:240px">
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:deflanguage">Language</digi:trn>
                                        </a>
                                          <div id="reports2" class="yuiampmenu">
                                              <div class="bd">                    
                                                  <ul>
													<digi:insert attribute="dropdownLangSwitch" />
                                                  </ul>
                                              </div>
                                          </div>                              
                                        </li>
                                        <logic:notEmpty name="currentMember" scope="session">
                                            <digi:secure actions="TRANSLATE">
                                                    <digi:insert attribute="transmode"/>
                                            </digi:secure>
                                        </logic:notEmpty>                                        
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          <module:display name="HELP">
                          <li class="yuiampmenuitem">
                              <span class="yuiampmenuitemlabel" href="#" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                  <digi:trn key="help:help">HELP</digi:trn>
                              </span>
                              <a  onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="help" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
										<feature:display name="Admin Help" module="HELP">
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#" onClick="adminHelp();" >
                                        <digi:trn>AMP Admin Help</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
										<feature:display name="User Help" module="HELP">
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#" onClick="help();">
                                        <digi:trn key="aim:AMPHelp">AMP Help</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
										<feature:display name="Support Request Form" module="HELP">
                                        <li>	
                                        <a class="yuiampmenuitemlabel" href="http://support.ampdev.net/login.action?code=<%=FeaturesUtil.getDefaultCountryIso()%>" target="_blank">
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
                                        <a class="yuiampmenuitemlabel" href="" target="name"
											onClick="showAbout(); return false;">
                                        	<digi:trn key="aim:aboutamp">About AMP</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>            
                        </ul>            
                  </div>
              </div>
            </div>
		</logic:equal>
</logic:present>


<logic:present name="ampAdmin" scope="session">
		<logic:equal name="ampAdmin" value="no">
	
<script language="JavaScript">
	function addActivity() {
		selectedLevelId=0; 
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~action=create~activityLevelId="+selectedLevelId;	
	}

	function openGIS() {
		openNewWindowWithMenubar(1000, 600);
		popupPointer.location.href = "/gis/showGisDashboard.do";
		
	}
	
	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
	function addMessage(fillTypesAndLevels) {
		window.location.href="/message/messageActions.do?editingMessage=false&actionType="+fillTypesAndLevels;
	}

	function addLink(id){
		<digi:context name="rev" property="context/module/moduleinstance/addQuickLink.do" />
		openURLinWindow("<%=rev%>?memId="+id,400,100);
	}
	
	function help(){
	 <digi:context name="rev" property="/help/help.do~blankPage=true" />
		openURLinWindow("<%=rev%>",1024,768);
	}
	
	function canExit(){
		return quitRnot1('${msg}');
	}	

</script>
<!-- 
<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
	${fn:replace(message,quote,escapedQuote)}
</c:set>
   --> 
             <div class="yuiamp-skin-amp" style="clear: both; width: 940px;">
              <div id="mainmenuHeader" class="yuiampmenu">
                  <div class="bd">
                      <ul class="first-of-type">
                    	 <li class="yuiampmenuitem" style="float:left;">
                              <a class="yuiampmenuitemlabel" href="/showDesktop.do" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()" >
                                 <digi:trn key="aim:mydesktop">My Desktop</digi:trn>
                              </a>
                              <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="mydesktop" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">
                                      <ul>
<!--
 										<li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/showDesktop.do">
                                            <digi:trn key="aim:ampdesktop">AMP Desktop</digi:trn>
                                            </a>
                                        </li>
-->
<c:if test="${not empty sessionScope.currentMember}">
<c:if test="${sessionScope.currentMember.addActivity == 'true'}">
		                                        <li class="yuiampmenuitem">
                                                    <c:set var="trnClickToAddNewActivit">
                                                    <digi:trn key="aim:clickToAddNewActivit">Click here to Add New Activity</digi:trn>
                                                    </c:set>
                                                    <a class="yuiampmenuitemlabel" href="javascript:addActivity()" onclick="return canExit()" title="${trnClickToAddNewActivit}">
                                                    <digi:trn key="btn:addActivity">Add Activity</digi:trn>
                                                    </a>
												</li>
</c:if></c:if>
                                            <%--<a class="yuiampmenuitemlabel" href="/advancedReportManager.do?clear=true&reportEdit=false">
                                            <digi:trn key="aim:addTab">Add Tab</digi:trn>
                                            </a> --%>
                                            <module:display name="Tab Generator" parentModule="REPORTING">
		                                      	<li class="yuiampmenuitem" title='<digi:trn key="aim:createTab">Create Tab</digi:trn>'>
		                                            <a class="yuiampmenuitemlabel"  onclick="return canExit()" href="/reportWizard.do?tab=true&reset=true">
		                                                <b><digi:trn key="aim:addTab">Add Tab</digi:trn></b>
		                                            </a>
		                                        </li>
		                                        <li class="yuiampmenuitem" title='<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>
		                                            <a class="yuiampmenuitemlabel" onclick="return canExit()" href="/viewTeamReports.do?tabs=true">
		                                                <digi:trn key="aim:tabManager">Tab Manager</digi:trn>
		                                            </a>
												</li>
	                                        </module:display>
                                        <li class="yuiampmenuitem" style="_width:230px;">
                                        	<a class="yuiampmenuitemlabel" href="#">
                                            	<digi:trn key="aim:changeworkspace">Change workspace</digi:trn>
                                            </a>
                                            <div id="workspaceSubmenu" class="yuiampmenu">
                                          	<div class="bd">                    
                                                      <ul>                                                      
                                                        <logic:iterate id="item"  name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
                                            		    <bean:define id="team" name="item" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
                                            		    <logic:equal name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
                                            		       <li class="yuiampmenuitem yuiampmenuitem-checked " style="_width:100%;">
                                                            	 <a class="yuiampmenuitemlabel" href="#"><bean:write name="team" property="name"/></a>
                                                          </li>
                                                        </logic:equal>                                            		    
                                            		     <logic:notEqual name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
                                            		   	   <li class="yuiampmenuitem" style="width: 100%;">
                                                            	<a class="yuiampmenuitemlabel" href='/selectTeam.do?id=<bean:write name="item" property="ampTeamMemId"/>' onclick="return canExit()"><bean:write name="team" property="name"/></a>
                                                          </li>
                                                        </logic:notEqual>
                                            		</logic:iterate>
                                                      </ul>
                                                  </div>
                                              </div>   
                                        </li>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
						<module:display name="REPORTING">
                          <li class="yuiampmenuitem" style="float:left;">
                              <a class="yuiampmenuitemlabel" href="/viewTeamReports.do?tabs=false" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()">
                                  <digi:trn key="aim:reports">Reports</digi:trn>
                              </a>
                              <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="reports" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <module:display  name="Report Generator" parentModule="REPORTING">
	                                        <li class="yuiampmenuitem" title='<digi:trn key="aim:createReport">Create Report</digi:trn>'>
	                                            <a class="yuiampmenuitemlabel" href="/reportWizard.do?tabs=false&reset=true"  onclick="return canExit()">
	                                                <b><digi:trn key="aim:reportGenerator">Report Generator</digi:trn></b>
	                                            </a>
	                                        </li>
	                                    </module:display>
										<module:display name="Off Line Reports" parentModule="REPORTING">
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/mondrian/mainreport.do" onclick="return canExit()">
                                            	<digi:trn key="aim:offlinereports">Off Line Reports</digi:trn>
                                            </a>
                                        </li>
                                        </module:display>
                                        <feature:display name="Paris Indicators Reports" module="Portfolio">
                                                <li class="yuiampmenuitem" title='<digi:trn key="aim:clickToViewParisIndcReports">Click here to view Paris Indicator Reports</digi:trn>'>
                                                    <a class="yuiampmenuitemlabel" href="/aim/parisIndicatorReport.do~indcId=1~reset=true"  onclick="return canExit()">
                                                        <b><digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn></b>
                                                    </a>
                                                </li>
                                        </feature:display>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>
						  <feature:display name="Content Repository" module="Document Management">
                          <li class="yuiampmenuitem" style="float:left;">
                              <a class="yuiampmenuitemlabel"  href="/contentrepository/documentManager.do" style="border-right:1px solid white;cursor:pointer;"  onclick="return canExit()">
                                 <digi:trn key="aim:resources">Resources</digi:trn>
                              </a>
                          </li>
                          </feature:display>
						  <module:display name="Calendar" parentModule="PROJECT MANAGEMENT">
						  	<!-- On which view calendar link should redirect. depends on FM -->
							  <c:set var="linkYearly">
							  	<feature:display name="Yearly View" module="Calendar">
							  		/calendar/showCalendarView.do
							  	</feature:display>
							  </c:set>
							  <c:set var="linkMonthly">
							  	<feature:display name="Monthly View" module="Calendar">
							  		/calendar/showCalendarView.do?view=monthly
							  	</feature:display>
							  </c:set>
							  <c:set var="linkWeekly">
							  	<feature:display name="Weekly View" module="Calendar">
							  		/calendar/showCalendarView.do?view=weekly
							  	</feature:display>
							  </c:set>
							  <c:set var="linkDaily">
							  	<feature:display name="Daily View" module="Calendar">
							  		/calendar/showCalendarView.do?view=daily
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
							  
                          <li class="yuiampmenuitem" style="float:left;">
                          	<c:if test="${not empty link}">
                          		<a class="yuiampmenuitemlabel"  href="${link}" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()">
                                 <digi:trn key="aim:calendar">Calendar</digi:trn>
                              </a>
                          	</c:if>
                          	<c:if test="${empty link}">
                          		<a class="yuiampmenuitemlabel"  href="/calendar/showCalendarView.do?view=none" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()">
                                 <digi:trn key="aim:calendar">Calendar</digi:trn>
                              </a>
                          	</c:if>
                          	<feature:display name="Create Event" module="Calendar">
                          		<a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;"  onclick="return canExit()">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="calendaroptions" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">
                                      <ul>
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/calendar/showCalendarEvent.do~selectedCalendarTypeId=0~method=new"  onclick="return canExit()">
                                            	<digi:trn key="aim:calendarAddEvent">Add Event</digi:trn>
                                            </a>
                                        </li>
                                      </ul>
                                  </div>
                              </div>
                          	</feature:display>
                          </li>
                          </module:display>
						<%	
							boolean showDashboard = false;
						  	if(FeaturesUtil.isVisibleFeature("NPD Dashboard", request.getSession().getServletContext())) {
						  		showDashboard = true;
						  	}
						  	if(FeaturesUtil.isVisibleModule("GIS DASHBOARD", request.getSession().getServletContext())) {
						  		showDashboard = true;
						  	}
						%>
						<%if (showDashboard) {%>
                          <li class="yuiampmenuitem" style="float:left;">
                              <span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px">
                                  <digi:trn key="aim:medashboard">DASHBOARDS</digi:trn>
                              </span>
                              <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="dashboards" class="yuiampmenu" style="opacity:0.9;min-width:170px;_width:250px;">
                                  <div class="bd">                    
                                      <ul>
                                       <feature:display name="NPD Dashboard" module="National Planning Dashboard">
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/nationalPlaningDashboard.do"  onclick="return canExit()">
                                            <digi:trn key="aim:npd">NPD</digi:trn>
                                            </a>
                                        </li>
                                       </feature:display>
                                        <module:display name="GIS DASHBOARD">
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/gis/showGisDashboard.do"  onclick="return canExit()">
                                            <digi:trn key="aim:gis">GIS</digi:trn>
                                            </a>
                                        </li>
                                        </module:display>
                                        <module:display name="Org Profile" >
                                            <li class="yuiampmenuitem">
                                                <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/orgProfile/showOrgProfile.do"  onclick="return canExit()">
                                                    <digi:trn key="fm:orgProfile">Org. Profile</digi:trn>
                                                </a> 
                                       
                                            </li>    
                                        </module:display>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
						<%}%>
                         
                          <c:set var="tabIndex">
                              <feature:display name="Event Tab" module="Messages">4</feature:display>
                              <feature:display name="Approval Tab" module="Messages">3</feature:display>
                              <feature:display name="Alert tab" module="Messages">2</feature:display>
                              <feature:display name="Message tab" module="Messages">1</feature:display>
                          </c:set>
						  <module:display name="Messages" parentModule="Messaging System">
                          <li class="yuiampmenuitem" style="float:left;">
                              <c:choose>
                                  <c:when test="${fn:contains(tabIndex, '1')}">
                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                  </c:when>
                                   <c:when test="${fn:contains(tabIndex, '2')}">
                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2&childTab=inbox"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                  </c:when>
                                  <c:when test="${fn:contains(tabIndex, '3')}">
                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3&childTab=inbox"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                  </c:when>
                                  <c:when test="${fn:contains(tabIndex, '4')}">
                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4&childTab=inbox"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                  </c:when>
                                  <c:otherwise>
                                       <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=5&childTab=inbox"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                  </c:otherwise>
                              </c:choose>



                                 <digi:trn key="fm:messages">Messages</digi:trn>
                              </a>
                              <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="messages" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li class="yuiampmenuitem">
                                        <a class="yuiampmenuitemlabel" href="/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels"  onclick="return canExit()">
                                        	<digi:trn key="aim:createmessage">Create Message</digi:trn>
                                        </a>
                                        </li>
<!-- 
                                        <li class="yuiampmenuitem">
                                        <a class="yuiampmenuitemlabel"  href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">
                                        	<digi:trn key="aim:messagemanager">Message Manager</digi:trn>
                                        </a>
                                        </li>
 -->
                                       </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>
                          
                          <li class="yuiampmenuitem" style="float:left;">
                              <span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px">
                                 <digi:trn key="aim:tools">TOOLS</digi:trn>
                              </span>
                              <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="tools" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li>
                                        <a class="yuiampmenuitemlabel" onclick="return canExit() && teamWorkspaceSetup('-1');" href="#">
                                        <digi:trn key="aim:workspaceinfo">Workspace Info</digi:trn>
                                        </a>
                                        </li>
                                        <li style="_width:300px">
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:deflanguage">Language</digi:trn>
                                        </a>
                                          <div id="reports2" class="yuiampmenu">
                                              <div class="bd">                    
                                                  <ul>
													<digi:insert attribute="dropdownLangSwitch" />
                                                  </ul>
                                              </div>
                                          </div>                              
                                        </li>
                                        <logic:notEmpty name="currentMember" scope="session">
                                            <digi:secure actions="TRANSLATE">
                                                    <digi:insert attribute="transmode"/>
                                            </digi:secure>
                                        </logic:notEmpty>                                        
                                        <logic:notEmpty name="currentMember" scope="session">
                                            <digi:secure group="UserSideAdmins">
		                                        <li>
			                                        <a class="yuiampmenuitemlabel" href="/aim/switchDevelopmentMode.do">
														<digi:trn key="admin:userdevmode">Developer Mode</digi:trn>
			                                        </a>
		                                        </li>
                                            </digi:secure>
                                        </logic:notEmpty>                                        
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="/search/search.do?reset=true"  onclick="return canExit()">
                                        <digi:trn key="aim:search">Search</digi:trn>
                                        </a>
                                        </li>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          <module:display name="HELP">
                          <li class="yuiampmenuitem" style="float:left;">
                              <span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px;">
                                  <digi:trn key="help:help">HELP</digi:trn>
                              </span>
                              <a onclick="arrowClicked = true" style="text-decoration:none;border-right:1px solid white;padding: 5px 3px 6px 3px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="help" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
										<feature:display name="User Help" module="HELP">
                                        <li>
                                         <a class="yuiampmenuitemlabel" href="#" onClick="help();">
                                        	<digi:trn>AMP Help</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
										<feature:display name="Support Request Form" module="HELP">
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="http://support.ampdev.net/" target="_blank">
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
                                        <a class="yuiampmenuitemlabel" href="" target="name"
											onClick="showAbout(); return false;">
                                        	<digi:trn key="aim:aboutamp">About AMP</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>
            
                        </ul>            
                  </div>
              </div>
            </div> 
            
		</logic:equal>
</logic:present>


<script language="javascript">

var arrowClicked = false;
//Run initialization for menu
var oMenuBar = new YAHOOAmp.widget.MenuBar("mainmenuHeader", { 
constraintoviewport:false
 });

for(var i = 0; i < oMenuBar.getItems().length; i++){
	oMenuBar.getItem(i).cfg.setProperty("onclick", { fn: onMenuItemClick });
}

oMenuBar.render();

function onMenuItemClick(p_sType, p_aArgs, p_oValue) { 
	if(arrowClicked){
//		alert(oMenuBar.activeItem);
		oSubmenu = this.cfg.getProperty("submenu");
		if(oSubmenu.cfg.getProperty("visible"));
		if(oSubmenu)
		{
			if(oSubmenu.cfg.getProperty("visible"))
			{
				oSubmenu.hide();
			}
			else
			{
				oSubmenu.show();
			}
				
		}
		arrowClicked = false;
	}
	else
	{
		oSubmenu.hide();		
	}
}



</script>
