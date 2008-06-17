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

        <!-- Core + Skin CSS -->
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />

<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

<logic:notPresent name="currentMember">
<% 
String publicView=FeaturesUtil.getGlobalSettingValue("Public View");
if("On".equals(publicView)) { 
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
          <ul class="first-of-typeamp">
            <li class="yuiampmenuitem">
                <c:set var="message">
                <digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                </c:set>
                <digi:link styleClass="yuiampmenuitemlabel" href="/reportsPublicView.do" module="aim" onclick="return quitRnot1('${message}')">
                <digi:trn key='aim:PublicView'>PUBLIC VIEW</digi:trn>
                </digi:link>
            </li>
            <feature:display name="yuiampmenuitemlabel" module="Document Management">
            <li class="yuiampmenuitem">
                <digi:link styleClass="yuiampmenuitemlabel" href="/documentManager.do" module="contentrepository" onclick="return quitRnot()">			
                <digi:trn key="contentrepository:publicDocuments">Public Documents</digi:trn></digi:link>
            </li>
            </feature:display>
            <li class="yuiampmenuitem">
                <digi:link styleClass="yuiampmenuitemlabel" href="/showRegisterUser.do" module="aim" title="${trn3}">
                <digi:trn key="aim:newUserRegistration">
                New user registration
                </digi:trn>
	            </digi:link>
            </li>
            <li>
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
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                 <digi:trn key="aim:tools">TOOLS</digi:trn>
                              </a>
                              <div id="tools" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li style="_width:200px">
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
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                  <digi:trn key="help:help">HELP</digi:trn>
                              </a>
                              <div id="help" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="/help/help.do~blankPage=true">
                                        <digi:trn key="aim:AMPHelp">AMP Help</digi:trn>
                                        </a>
                                        </li>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:tutorial">Tutorial</digi:trn>
                                        </a>
                                        </li>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:supportrequestform">Support Request Form</digi:trn>
                                        </a>
                                        </li>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:aboutamp">About AMP</digi:trn>
                                        </a>
                                        </li>
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

</script>

             <div class="yuiamp-skin-amp" style="clear:both;">
              <div id="mainmenuHeader" class="yuiampmenu">
                  <div class="bd">
                      <ul class="first-of-type">
                    	 <li class="yuiampmenuitem">
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                  My Desktop
                              </a>
                              <div id="mydesktop" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">
                                      <ul>
                                      
                                      
										<li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/showDesktop.do">
                                            <digi:trn key="aim:ampdesktop">AMP Desktop</digi:trn>
                                            </a>
                                        </li>
<c:if test="${not empty sessionScope.currentMember}">
<c:if test="${sessionScope.currentMember.addActivity == 'true'}">
		                                        <li class="yuiampmenuitem">
                                                    <c:set var="trnClickToAddNewActivit">
                                                    <digi:trn key="aim:clickToAddNewActivit">Click here to Add New Activity</digi:trn>
                                                    </c:set>
                                                    <a class="yuiampmenuitemlabel" href="javascrtip:addActivity()" title="${trnClickToAddNewActivit}"><digi:trn key="btn:addActivity">Add Activity</digi:trn></a>
												</li>
</c:if></c:if>
										<li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/advancedReportManager.do?clear=true&reportEdit=false">
                                            <digi:trn key="aim:addTab">Add Tab</digi:trn>
                                            </a>
                                        </li>
                                        <li class="yuiampmenuitem" style="_width:100px">
                                        	<a class="yuiampmenuitemlabel" href="#">
                                            	<digi:trn key="aim:changeworkspace">Change workspace</digi:trn>
                                            </a>
                                            <div id="workspaceSubmenu" class="yuiampmenu">
                                          	<div class="bd">                    
                                                      <ul class="first-of-type">
                                                      
                                                        <logic:iterate id="item"  name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
                                            		    <bean:define id="team" name="item" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
                                            		    <logic:equal name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
                                            		       <li class="yuiampmenuitem yuiampmenuitem-checked ">
                                            		   	  	 <digi:context name="URL" property="context/module/moduleinstance/selectTeam.do" />
                                                            	 <a class="yuiampmenuitemlabel" href="#"><bean:write name="team" property="name"/></a>
                                                          </li>
                                                        </logic:equal>
                                            		    
                                            		     <logic:notEqual name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
                                            		   	   <li class="yuiampmenuitem ">
                                            		   	  	 <digi:context name="URL" property="context/module/moduleinstance/selectTeam.do" />
                                                            	 <a class="yuiampmenuitemlabel" href='<%=URL%>~id=<bean:write name="item" property="ampTeamMemId"/>'><bean:write name="team" property="name"/></a>
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
                          <li class="yuiampmenuitem">
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                  <digi:trn key="aim:reports">Reports</digi:trn>
                              </a>
                              <div id="reports" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li title='<digi:trn key="aim:createReport">Create Report</digi:trn>'>
                                            <a class="yuiampmenuitemlabel" href="/advancedReportManager.do?clear=true&reportEdit=false">
                                                <b><digi:trn key="aim:reportGenerator">Report Generator</digi:trn></b>
                                            </a>
                                        </li>
										<LI title='<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>
                                            <a class="yuiampmenuitemlabel" href="/viewTeamReports.do">
                                                <digi:trn key="aim:ReportManager">Report Manager</digi:trn>
                                            </a>
										</li>
                                        <feature:display name="Paris Indicators Reports" module="Portfolio">
                                                <li title='<digi:trn key="aim:clickToViewParisIndcReports">Click here to view Paris Indicator Reports</digi:trn>'>
                                                    <a class="yuiampmenuitemlabel" href="/parisIndicatorReport.do">
                                                        <b><digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn></b>
                                                    </a>
                                                </li>
                                        </feature:display>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
						  <feature:display name="Content Repository" module="Document Management">
                          <li class="yuiampmenuitem">
                              <a class="yuiampmenuitemlabel"  href="/contentrepository/documentManager.do" style="border-right:1px solid white;">
                                 <digi:trn key="aim:resources">Resources</digi:trn>
                              </a>
                          </li>
                          </feature:display>
						  <feature:display name="Calendar" module="Calendar">
                          <li class="yuiampmenuitem">
                              <a class="yuiampmenuitemlabel"  href="#" style="border-right:1px solid white;">
                                 <digi:trn key="aim:calendar">Calendar</digi:trn>
                              </a>
                              <div id="calendaroptions" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/calendar/showCalendarEvent.do~selectedCalendarTypeId=0~method=new">
                                            <digi:trn key="aim:calendarAddEvent">Add Event</digi:trn>
                                            </a>
                                        </li>
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/calendar/showCalendarView.do">
                                            <digi:trn key="aim:calendar">Calendar</digi:trn>
                                            </a>
                                        </li>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </feature:display>
                          <li class="yuiampmenuitem">
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                  <digi:trn key="aim:medashboard">DASHBOARDS</digi:trn>
                              </a>
                              <div id="dashboards" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="/nationalPlaningDashboard.do">
                                            <digi:trn key="aim:npd">NPD</digi:trn>
                                            </a>
                                        </li>
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="#">
                                            <digi:trn key="aim:programs">Programs</digi:trn>
                                            </a>
                                        </li>
                                        <li class="yuiampmenuitem">
                                            <a class="yuiampmenuitemlabel" href="#">
                                            <digi:trn key="aim:gis">GIS</digi:trn>
                                            </a>
                                        </li>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
						  <feature:display name="Messages" module="Messaging System">
                          <li class="yuiampmenuitem">
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                 <digi:trn key="fm:messages">Messages</digi:trn>
                              </a>
                              <div id="messages" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li class="yuiampmenuitem">
                                        <a class="yuiampmenuitemlabel" onclick="return addMessage('fillTypesAndLevels')" href="#">
                                        	<digi:trn key="aim:createmessage">Create Message</digi:trn>
                                        </a>
                                        </li>
                                        <li class="yuiampmenuitem">
                                        <a class="yuiampmenuitemlabel"  href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">
                                        	<digi:trn key="aim:messagemanager">Message Manager</digi:trn>
                                        </a>
                                        </li>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </feature:display>
                          <li class="yuiampmenuitem">
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                 <digi:trn key="aim:tools">TOOLS</digi:trn>
                              </a>
                              <div id="tools" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li>
                                        <a class="yuiampmenuitemlabel" onclick="teamWorkspaceSetup('-1')" href="#">
                                        <digi:trn key="aim:workspaceinfo">Workspace Info</digi:trn>
                                        </a>
                                        </li>
                                        <li style="_width:200px">
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
                              <a class="yuiampmenuitemlabel" href="#" style="border-right:1px solid white;">
                                  <digi:trn key="help:help">HELP</digi:trn>
                              </a>
                              <div id="help" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd">                    
                                      <ul>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="/help/help.do~blankPage=true">
                                        <digi:trn key="aim:AMPHelp">AMP Help</digi:trn>
                                        </a>
                                        </li>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:tutorial">Tutorial</digi:trn>
                                        </a>
                                        </li>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:supportrequestform">Support Request Form</digi:trn>
                                        </a>
                                        </li>
                                        <li>
                                        <a class="yuiampmenuitemlabel" href="#">
                                        <digi:trn key="aim:aboutamp">About AMP</digi:trn>
                                        </a>
                                        </li>
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
//Run initialization for menu
var oMenuBar = new YAHOOAmp.widget.MenuBar("mainmenuHeader", { 
autosubmenudisplay: true
 });
oMenuBar.render();

</script>      
