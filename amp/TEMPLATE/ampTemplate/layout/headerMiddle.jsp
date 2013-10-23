<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
 <!-- Dependencies --> 
<%@page import="org.digijava.module.aim.helper.Constants"%>


<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>

<!-- Core + Skin CSS -->
<digi:ref href="/TEMPLATE/ampTemplate/css_2/menu.css" type="text/css" rel="stylesheet" />

<jsp:include page="/repository/aim/view/ar/aboutScripts.jsp"/>
<!--<div id="myAboutFilterWrapper" style="display: none;" >-->
<div id="customAbout" class="invisible-item" class="content">
	<jsp:include page="/repository/aim/view/helpAbout.jsp" />
</div>
<!--</div>-->

<div id="myUserFilterWrapper" style="display: none;" >
	<div id="customUser" class="invisible-item" class="content" >
		<jsp:include page="/repository/aim/view/workspacePopup.jsp" />
	</div>
</div>
    

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

<style>
.yuiampmenuitemlabel
{
	cursor:pointer;
}
</style>
<div class="menucontainer">
<div class="yuiamp-skin-amp" style="clear:both;" >
    <div id="mainmenuHeader" class="yuiampmenu">
      <div class="bd bd_drop">   
      	 <ul class="first-of-type">
      	 	<module:display name="Home Page Menu Entry" parentModule="PUBLIC VIEW">
	            <li class="yuiampmenuitem">
	            <logic:present name="currentMember" scope="session">
	                <a class="yuiampmenuitemlabel" href="/aim" title="${trn3}">
	                	<digi:trn>Home Page</digi:trn>
		            </a>
		        </logic:present>
		        <logic:notPresent name="currentMember" scope="session">
		                <a class="yuiampmenuitemlabel" href="/" title="${trn3}">
		                	<digi:trn>Home Page</digi:trn>
			            </a>
		            </logic:notPresent>    
	            </li>
            </module:display>
            
            <module:display name="Public Site" parentModule="PUBLIC VIEW">
               <li class="yuiampmenuitem" style="float:left;">
                <c:set var="message">
                <digi:trn>WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
                </c:set>
                <a class="yuiampmenuitemlabel" href="/reportsPublicView.do" module="aim" >
                	<digi:trn>PUBLIC SITE</digi:trn>
                </a>
            </li> 
            </module:display>
             
            <module:display name="Public Documents" parentModule="PUBLIC VIEW">
            <li class="yuiampmenuitem" style="float:left;">
                <a class="yuiampmenuitemlabel" href="/contentrepository/publicDocTabManager.do?action=publicShow" module="contentrepository" onclick="return quitRnot()">			
                	<digi:trn>Public Documents</digi:trn>
                </a>
            </li>
            </module:display>
            
            <module:display name="Public Reports" parentModule="PUBLIC VIEW">
			<li class="yuiampmenuitem" style="float:left;">
				<span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px;border-right:0px none;">
                    <digi:trn>Reports</digi:trn>
                </span>
            
                	<a style="text-decoration:none; padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                		<img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
					</a>
	                <div id="reports" class="yuiampmenu" style="opacity:0.9;min-width:170px;_width:250px;">
	                	<div class="bd bd_drop">
	                		<ul class="first-of-type"> 
	                			<li class="yuiampmenuitem_drop">
	           						<a class="yuiampmenuitemlabel" href="/viewTeamReports.do?tabs=false&reset=true"  onclick="return canExit()">
                   						 <digi:trn>Public Reports</digi:trn>
                					</a>
	               				</li>  
	               				<module:display name="Multi-dimensional Reports Public View" parentModule="REPORTING">                 
	                			<li class="yuiampmenuitem_drop">
	           						<digi:link styleClass="yuiampmenuitemlabel" href="/mainreports.do" module="mondrian" >
	                   					<digi:trn>Multi-dimensional Reports</digi:trn>
	                   				</digi:link>
	               				</li>
	               				</module:display>
	            			</ul>
	            		</div>
	            	</div>
           	</li>
           	</module:display>
           	
           	
           	<module:display name="Calendar" parentModule="PROJECT MANAGEMENT">
	           	<module:display name="Public Calendar" parentModule="PUBLIC VIEW">
                <li class="yuiampmenuitem" style="float:left;">
                	<a class="yuiampmenuitemlabel"  href="/calendar/showCalendarView.do?view=none&filterInUse=false" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()">
                       <digi:trn>Calendar</digi:trn>
                    </a>
                </li>
            </module:display>
            </module:display>
            
	           	<module:display name="Public Dashboards" parentModule="PUBLIC VIEW">
	                <li class="yuiampmenuitem" style="float:left;">
	                	<span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px;border-right:0px none;">
	                		<digi:trn key="aim:medashboard">DASHBOARDS</digi:trn>
	                    </span>
	                    <a  style="text-decoration:none; padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
	                    	<img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br/>
	                    </a>
	                  	<div id="dashboards" class="yuiampmenu" style="opacity:0.9;min-width:170px;_width:250px;">
	                    	<div class="bd bd_drop">                    
	                    		<ul class="first-of-type"> 
									
									<module:display name="Map Module" >
										<feature:display name="Show map in public view" module="Map Module">
			                            	<li class="yuiampmenuitem_drop">
			                              	<a target="_blank" class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/esrigis/mainmap.do?public=true"  onclick="return canExit()">
			                                	<digi:trn key="fm:gis">Advanced GIS</digi:trn>
			                                </a> 
		                                	</li>
	                              		</feature:display>
									</module:display>
									
																								
									<module:display name="GIS DASHBOARD" >
										<feature:display name="Show in public view" module="GIS DASHBOARD">
			                            	<li class="yuiampmenuitem_drop">
			                              	<a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/gis/showPublicGis.do?public=true">
			                                	<digi:trn key="fm:gis">GIS Dashboard</digi:trn>
			                                </a> 
		                                	</li>
	                              		</feature:display>
									</module:display>
										
										<module:display name="Dashboard Generator in public view" parentModule="Visualization Dashboards">
	                                        <li class="yuiampmenuitem_drop">
				                              	<a class="yuiampmenuitemlabel" href="/visualization/dashboardLauncher.do"  onclick="return canExit()">
				                                	<digi:trn>Dashboard Generator</digi:trn>
				                                </a> 
			                                </li>
		                                </module:display>
		                                <logic:present name="MENU_DASHBOARDS" scope="session">
			                                <logic:iterate id="item" name="MENU_DASHBOARDS" scope="session" type="org.digijava.module.visualization.dbentity.AmpDashboard">
												<li class="yuiampmenuitem_drop">
													<a class="yuiampmenuitemlabel" href='/visualization/launchDashboard.do?reset=true&publicView=true&id=<bean:write name="item" property="id"/>' onclick="return canExit()">
														<digi:trn><bean:write name="item" property="name"/></digi:trn>
													</a>
												</li>
											</logic:iterate>
										</logic:present>
										
								</ul>
						</div>
						</div>                    
					</li>
				</module:display>
			
           	<module:display name="Public Language Switch" parentModule="PUBLIC VIEW">
           	<feature:display name="Language Option" module="Tools">
		              <li class="yuiampmenuitem" style="float:left;margin:0px 0px 0px 0px;_margin:0px;">
		                <span class="yuiampmenuitemlabel" href="#" style="float:left;position:relative;top:0px;_top:1px;border-right:0px none;">
		                	<digi:trn>Language</digi:trn>
		                </span>
		                 <a  style="text-decoration:none;padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
		                   <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
		                </a>               
		                <div id="language" class="yuiampmenu">
		                    <div class="bd bd_drop">                    
	                    		<ul class="first-of-type"> 
		                    		<digi:insert flush="false" attribute="dropdownLangSwitch" />
		                        </ul>
		                    </div>
		                </div>                              
		            </li>
		  </feature:display>
		  </module:display>
          </ul>            
      </div>
  </div>
</div> 

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
                  <div class="bd bd_drop">   
                      <ul class="first-of-type">
                    	<li class="yuiampmenuitem">
                        	<c:set var="message">
                 				<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
           					</c:set>
                   			<a class="yuiampmenuitemlabel"  href="/admin.do" module="aim" >
                       			<digi:trn key="aim:aminTools">ADMIN TOOLS</digi:trn>
                   			</a>
                        </li>
                    <module:display name="Tools" parentModule="PROJECT MANAGEMENT">
                          <li class="yuiampmenuitem">
                              <span class="yuiampmenuitemlabel" href="#" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                 <digi:trn key="aim:tools">TOOLS</digi:trn>
                              </span>
                              <a   style="text-decoration:none;padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="tools" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd bd_drop">                    
                                      <ul>
                                       <feature:display name="Language Option" module="Tools">
		                                        <li>
		                                        	<a class="yuiampmenuitemlabel" href="#">
		                                        		<digi:trn key="aim:deflanguage">Language</digi:trn>
		                                       		</a>
		                                          <div id="reports2" class="yuiampmenu">
		                                              <div class="bd bd_drop">                    
		                                                  <ul>
															<digi:insert flush="false" attribute="dropdownLangSwitch" />
		                                                  </ul>
		                                              </div>
		                                          </div>                              
		                                        </li>
		                                   </feature:display>
                                        <logic:notEmpty name="currentMember" scope="session">
                                            <digi:secure actions="TRANSLATE">
                                                 <digi:insert flush="false" attribute="transmode"/>
                                            </digi:secure>
                                        </logic:notEmpty>                                        
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>
                          <module:display name="HELP">
                          <li class="yuiampmenuitem">
                              <span class="yuiampmenuitemlabel" href="#" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                  <digi:trn key="help:help">HELP</digi:trn>
                              </span>
                              <a   style="text-decoration:none;padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="help" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd bd_drop" style="border: 1px solid transparent; width:100px;">                    
                                      <ul>
																			<feature:display name="Admin Help" module="HELP">
                                        <li class="yuiampmenuitem_drop">
                                        	<a class="yuiampmenuitemlabel" href="#" onClick="adminHelp();" >
                                        		<digi:trn>AMP Admin Help</digi:trn>
                                       		 </a>
                                        </li>
										</feature:display>
										<feature:display name="User Help" module="HELP">
                                        	<li class="yuiampmenuitem_drop">
                                        		<a class="yuiampmenuitemlabel" href="#" onClick="help();">
                                        			<digi:trn key="aim:AMPHelp">AMP Help</digi:trn>
                                        	</a>
                                        </li>
										</feature:display>

										<feature:display name="Glossary" module="HELP">
                                        <li class="yuiampmenuitem_drop">	
                                        	<a class="yuiampmenuitemlabel" href="/help/glossary.do">
                                        		<digi:trn>Glossary</digi:trn>
                                        	</a>
                                        </li>
										</feature:display>

										<feature:display name="Support Request Form" module="HELP">
                                        <li class="yuiampmenuitem_drop">	
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
	//'${msg}'
	function canExit(){
	    if(typeof quitRnot1 == 'function') {
	        return quitRnot1('${msg}');
	    }
	    else{
	        return true;
	    }
	
	}	

	function newAddActivity() {
		selectedLevelId=0; 
		window.location.href="/wicket/onepager/activity/new";	
	}
	
	function addActivity() {
		selectedLevelId=0; 
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~resetMessages=true~action=create~activityLevelId="+selectedLevelId;	
	}

<jsp:include page="gisMenu.jsp" />
	
	function teamWorkspaceSetup(a) {
		if (canExit()){
			if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){
 	 	 	 	var referLink = document.createElement('a');
 	 	 	 	referLink.href = "/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";
 	 	 	 	document.body.appendChild(referLink);
 	 	 	 	referLink.click();
 	 	 	 } else {
 	 	 		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";
 	 	 	 }
		} else {
			return false;
		}	
	}
	function addMessage(fillTypesAndLevels) {
		window.location.href="/message/messageActions.do?editingMessage=false&actionType="+fillTypesAndLevels;
	}

	function help(){
	 <digi:context name="rev" property="/help/help.do~blankPage=true" />
		openURLinWindow("<%=rev%>",1024,768);
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
             <div class="yuiamp-skin-amp" style="clear: both; ">
              <div id="mainmenuHeader" class="yuiampmenu">
                  <div class="bd bd_drop">   
                      <ul class="first-of-type">
                      	<module:display name="My Desktop" parentModule="PROJECT MANAGEMENT">
                    	 <li class="yuiampmenuitem" style="float:left;">
                              <a class="yuiampmenuitemlabel" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()" >
                                 <digi:trn>Desktop</digi:trn>
                              </a>
                              <a  style="text-decoration:none;padding: 4px 8px 5px 0px;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="mydesktop" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd bd_drop">
                                      <ul>
                                  		<li class="yuiampmenuitem_drop">
                                            <a class="yuiampmenuitemlabel" href="/aim">
                                            	<digi:trn>My Desktop</digi:trn>
                                            </a>
                                       	</li>

									<c:if test="${not empty sessionScope.currentMember}">
									        <c:if test="${sessionScope.currentMember.addActivity == 'true'}">
												<field:display name="Add Activity Button" feature="Edit Activity">
		                                        <li class="yuiampmenuitem_drop">
                                                    <c:set var="trnClickToAddNewActivit">
                                                    <digi:trn key="aim:clickToAddNewActivit">Click here to Add New Activity</digi:trn>
                                                    </c:set>
                                                    <a class="yuiampmenuitemlabel" href="javascript:newAddActivity()" onclick="return canExit()" title="${trnClickToAddNewActivit}">
                                                    	<digi:trn key="btn:addActivity">Add Activity</digi:trn>
                                                    </a>
												</li>
												<!-- OLD Add activity
		                                        <li class="yuiampmenuitem_drop">
                                                    <c:set var="trnClickToAddNewActivit">
                                                    <digi:trn key="aim:clickToAddNewActivit">Click here to Add New Activity</digi:trn>
                                                    </c:set>
                                                    <a class="yuiampmenuitemlabel" href="javascript:addActivity()" onclick="return canExit()" title="${trnClickToAddNewActivit}">
                                                    	<digi:trn key="btn:addActivity">Add Activity</digi:trn>
                                                    </a>
												</li>
												 -->
												</field:display>
                                            </c:if>
                                        <field:display name="Add SSC Button" feature="Edit Activity">
                                              <li class="yuiampmenuitem_drop">
                                                  <c:set var="trnClickToAddNewActivit">
                                                      <digi:trn>Click here to Add New SSC</digi:trn>
                                                  </c:set>
                                                  <a class="yuiampmenuitemlabel" href="/wicket/onepager/ssc/new" title="${trnClickToAddNewActivit}">
                                                      <digi:trn>Add SSC</digi:trn>
                                                  </a>
                                              </li>
                                        </field:display>
                                    </c:if>
                                            <%--<a class="yuiampmenuitemlabel" href="/advancedReportManager.do?clear=true&reportEdit=false">
                                            <digi:trn key="aim:addTab">Add Tab</digi:trn>
                                            </a> --%>
                                            <module:display name="Tab Generator" parentModule="REPORTING">
		                                      	<li class="yuiampmenuitem_drop" title='<digi:trn key="aim:createTab">Create Tab</digi:trn>'>
		                                            <a class="yuiampmenuitemlabel"  onclick="return canExit()" href="/reportWizard.do?tab=true&reset=true">
		                                                <digi:trn key="aim:addTab">Add Tab</digi:trn>
		                                            </a>
		                                        </li>
		                                        <li class="yuiampmenuitem_drop" title='<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>
		                                            <a class="yuiampmenuitemlabel" onclick="return canExit()" href="/viewTeamReports.do?tabs=true&reset=true">
		                                                <digi:trn key="aim:tabManager">Tab Manager</digi:trn>
		                                            </a>
												</li>
	                                        </module:display>
	                                        
                                        <feature:display name="Change Workspace" module="My Desktop">
	                                        <li class="yuiampmenuitem_drop">
	                                        	<a class="yuiampmenuitemlabel" href="#">
	                                            	<digi:trn key="aim:changeworkspace">Change workspace</digi:trn>
	                                            </a>
	                                            <div id="workspaceSubMenu" class="yuiampmenu">
	                                          		<div class="bd">      
	                                          		
	                                          		<c:if test="${fn:length(USER_WORKSPACES) ge 16}">
	                                          		      <ul style="overflow-x:hidden;overflow-y:scroll;height: 400px;width:auto;">
	                                          		</c:if>
	                                          		<c:if test="${fn:length(USER_WORKSPACES) le 15}">             
	                                          			  <ul>
	                                          		</c:if>
															<logic:iterate id="item" name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
																<bean:define id="team" name="item" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
																<logic:equal name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
																	<li class="yuiampmenuitemlabel yuimenuitemlabel"  style="height:18px;white-space: nowrap;font-size: 10px;">
																		<a href="#"><bean:write name="team" property="name"/></a>
																	</li>
																</logic:equal>
																<logic:notEqual name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
																	<li class="yuiampmenuitemlabel yuimenuitemlabel" style="height:18px;white-space: nowrap;font-size: 10px;">
																		<a href='/selectTeam.do?id=<bean:write name="item" property="ampTeamMemId"/>' onclick="return canExit()"><bean:write name="team" property="name"/></a>
																	</li>
																</logic:notEqual>
															</logic:iterate>
														  </ul>
	                                                  </div>
	                                              </div>
	                                        </li>
                                        </feature:display>
                                     
                                        
	                                    	<c:if test="${not empty sessionScope.currentMember}">
											<c:if test="${sessionScope.currentMember.pledger == 'true'}">
		 										<module:display name="Pledges" parentModule="PROJECT MANAGEMENT">
			                                        <li class="yuiampmenuitem_drop" title='<digi:trn key="aim:clickToManagePledges">Click here to manage pledges</digi:trn>'>
				                                       <a class="yuiampmenuitemlabel" onclick="return canExit()" href="/viewPledgesList.do">
				                                           <digi:trn key="aim:Pledges">Pledges</digi:trn>
				                                        </a>
													</li>
												</module:display>
											</c:if>
										</c:if>
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>
						<module:display name="REPORTING">
                          <li class="yuiampmenuitem" style="float:left;">
                              <a class="yuiampmenuitemlabel" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px"  onclick="return canExit()">
                                  <digi:trn key="aim:reports">Reports</digi:trn>
                              </a>
                              <a  style="text-decoration:none;padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="reports" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd bd_drop">                    
                                      <ul class="first-of-type">
                                      		<li class="yuiampmenuitem_drop">
	                                        	<a class="yuiampmenuitemlabel" href="#">
	                                            	<digi:trn>Reports</digi:trn>
	                                            </a>
	                                            <div id="reportsSubMenu" class="yuiampmenu">
	                                          		<div class="bd">      
	                                          			<ul>	                                          		
															<li class="yuiampmenuitem_drop" title='<digi:trn>Create Report</digi:trn>'>
					                                            <a class="yuiampmenuitemlabel" href="/viewTeamReports.do?tabs=false&reset=true&onlyFavourites=false"  onclick="return canExit()">
					                                                <digi:trn>All Reports</digi:trn>
					                                            </a>
					                                    	</li>
					                                    	<li class="yuiampmenuitem_drop" title='<digi:trn>Create Report</digi:trn>'>
					                                            <a class="yuiampmenuitemlabel" href="/viewTeamReports.do?tabs=false&reset=true&onlyFavourites=true"  onclick="return canExit()">
					                                                <digi:trn>Favourite Reports</digi:trn>
					                                            </a>
					                                    	</li>
														  </ul>
	                                                  </div>
	                                              </div>
	                                        </li>
                                        <module:display  name="Report Generator" parentModule="REPORTING">
	                                        <li class="yuiampmenuitem_drop" title='<digi:trn key="aim:createReport">Create Report</digi:trn>'>
	                                            <a class="yuiampmenuitemlabel" href="/reportWizard.do?tabs=false&reset=true"  onclick="return canExit()">
	                                                <digi:trn key="aim:reportGenerator">Report Generator</digi:trn>
	                                            </a>
	                                        </li>
	                                    </module:display>
	                                      <module:display name="Donor Profile Report" parentModule="REPORTING">
	                                        <li class="yuiampmenuitem_drop" title='<digi:trn key="aim:createReport">Create Report</digi:trn>'>
	                                            <a class="yuiampmenuitemlabel" href="/organizationReportWizard.do?reset=true"  onclick="return canExit()">
	                                                <digi:trn>Donor Report Generator</digi:trn>
	                                            </a>
	                                        </li>
	                                     </module:display>
	                                    <module:display name="Multi-dimensional Reports" parentModule="REPORTING">
                                        <li class="yuiampmenuitem_drop">
                                            <a class="yuiampmenuitemlabel" href="/mondrian/mainreports.do" onclick="return canExit()">
                                            	<digi:trn key="aim:multidimensionalreports">Multi-dimensional Reports</digi:trn>
                                            </a>
                                        </li>
                                        </module:display>
                                        <feature:display name="Paris Indicators Reports" module="Portfolio">
                                                <li class="yuiampmenuitem_drop" title='<digi:trn key="aim:clickToViewParisIndcReports">Click here to view Paris Indicator Reports</digi:trn>'>
                                                    <a class="yuiampmenuitemlabel" href="/parisindicator/parisindicator.do?reportId=3&reset=true&header=true"  onclick="return canExit()">
                                                        <digi:trn>Paris Indicator</digi:trn>
                                                    </a>
                                                </li>
                                        </feature:display>
                                        <feature:display name="Pledges Default Reports" module="Pledge Reports">
                                        	<li class="yuiampmenuitem_drop" title='<digi:trn>Click here to view Pledges Reports</digi:trn>'>
                                            	<a class="yuiampmenuitemlabel" href="/reportWizard.do?tabs=false&reset=true&type=5"  onclick="return canExit()">
                                                	<digi:trn key="aim:parisIndicator">Pledges Reports</digi:trn>
												</a>
                                           </li>
                                        </feature:display>
                                    </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>
						  <module:display name="Content Repository" parentModule="Resources">
                          	<li class="yuiampmenuitem" style="float:left;">
                                <a class="yuiampmenuitemlabel" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                                 <digi:trn key="aim:resources">Resources</digi:trn>
                              </a>
                              <a  style="text-decoration:none;padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
	                          	<img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
	                          </a>
                             <div id="Resources" class="yuiampmenu" style="opacity:0.9;min-width:170px;_width:250px;">
                           	 	<div class="bd bd_drop">
                                	<ul class="first-of-type">
                                		<li class="yuiampmenuitem_drop">
                              				<a class="yuiampmenuitemlabel"  href="/contentrepository/documentManager.do" style="cursor:pointer;"  onclick="return canExit()">
                                 				<digi:trn>View Resources</digi:trn>
                              				</a>
                              			</li>
                                	</ul>
                                </div>
                              </div>  
                         	</li>
                          </module:display>
                          
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
							  <li class="yuiampmenuitem" style="float:left;">
	                              <a class="yuiampmenuitemlabel" style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
	                                  <digi:trn>Calendar</digi:trn>
	                              </a>
	                              <a  style="text-decoration:none;padding: 4px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
	                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
	                              </a>
						  		 <div id="calendaroptions" class="yuiampmenu" style="opacity:0.9;min-width:170px;_width:250px;">
                            	   <div class="bd bd_drop">
                                		<ul class="first-of-type">  
                          					<li class="yuiampmenuitem_drop">
					                          	<c:if test="${not empty link}">
					                          		<a class="yuiampmenuitemlabel"  href="${link}" onclick="return canExit()">
					                                 	<digi:trn>View Calendar</digi:trn>
					                              	</a>
					                          	</c:if>
					                          	<c:if test="${empty link}">
					                          		<a class="yuiampmenuitemlabel"  href="/calendar/showCalendarView.do?view=none&filterInUse=false" onclick="return canExit()">
					                                 	<digi:trn>View Calendar</digi:trn>
					                              	</a>
					                          	</c:if>
                          					</li>
                        					<feature:display name="Create Event" module="Calendar">
	                        				<li class="yuiampmenuitem_drop">
	                                          	<a class="yuiampmenuitemlabel" href="/calendar/showCalendarEvent.do~selectedCalendarTypeId=0~method=new"  onclick="return canExit()">
	                                          		<digi:trn key="aim:calendarAddEvent">Add Event</digi:trn>
	                                          	</a>
	                                      	</li>
                                      	</feature:display>
                                      </ul>
                                  </div>
                              	</div>
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
						  	if(FeaturesUtil.isVisibleModule("Map Module", request.getSession().getServletContext())) {
						  		showDashboard = true;
						  	}
                            if(FeaturesUtil.isVisibleModule("Visualization Dashboards", request.getSession().getServletContext())) {
                                showDashboard = true;
                            }
						  							  	
						%>
						<%if (showDashboard) {%>
                          <li class="yuiampmenuitem" style="float:left;">
                              <span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px">
                                  <digi:trn key="aim:medashboard">DASHBOARDS</digi:trn>
                              </span>
                              <a  style="text-decoration:none;padding: 5px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="dashboards" class="yuiampmenu" style="opacity:0.9;min-width:170px;_width:250px;">
                                  <div class="bd bd_drop">                    
                                     <ul class="first-of-type">  
                                       <feature:display name="NPD Dashboard" module="National Planning Dashboard">
                                        <li class="yuiampmenuitem_drop">
                                            <a class="yuiampmenuitemlabel" href="/nationalPlaningDashboard.do"  onclick="return canExit()">
                                            <digi:trn key="aim:npd">NPD</digi:trn>
                                            </a>
                                        </li>
                                       </feature:display>
                                        <module:display name="GIS DASHBOARD">
                                        <li class="yuiampmenuitem_drop">
                                            <a class="yuiampmenuitemlabel" href="javascript:openGIS()"  onclick="return canExit()">
                                            <digi:trn key="aim:gis">GIS</digi:trn>
                                            </a>
                                        </li>
                                        </module:display>
                                        
                                        <module:display name="Map Module">
                                        <li class="yuiampmenuitem_drop">
                                        	<a class="yuiampmenuitemlabel" href="/esrigis/mainmap.do?reset=true" target="_blank">
			                                	<digi:trn>Advanced GIS</digi:trn>
			                                </a>
			                            </li> 
                                        </module:display>                                        
										
										<module:display name="Dashboard Generator" parentModule="Visualization Dashboards">
	                                        <li class="yuiampmenuitem_drop">
				                              	<a class="yuiampmenuitemlabel" href="/visualization/dashboardLauncher.do"  onclick="return canExit()">
				                                	<digi:trn>Dashboard Generator</digi:trn>
				                                </a> 
			                                </li>
		                                </module:display>
		                                
		                                <logic:iterate id="item" name="MENU_DASHBOARDS" scope="session" type="org.digijava.module.visualization.dbentity.AmpDashboard">
											<li class="yuiampmenuitem_drop">
												<a class="yuiampmenuitemlabel" href='/visualization/launchDashboard.do?reset=true&id=<bean:write name="item" property="id"/>' onclick="return canExit()">
													<bean:write name="item" property="name"/>
												</a>
											</li>
										</logic:iterate>
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
                          		<a class="yuiampmenuitemlabel"  style="float:left;cursor:pointer;position:relative;top:0px;_top:1px">
                          			<digi:trn key="fm:messages">Messages</digi:trn>
                              	</a> 
                          		<a  style="text-decoration:none;padding: 5px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 	<img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              	</a>
                             <div id="messages" class="yuiampmenu" style="opacity:0.9;">
	                             <div class="bd bd_drop">                    
	                           	<ul class="first-of-type">  
	                           		<li class="yuiampmenuitem_drop">
		                           	<c:choose>
		                                  <c:when test="${fn:contains(tabIndex, '1')}">
		                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox">
		                                  </c:when>
		                                   <c:when test="${fn:contains(tabIndex, '2')}">
		                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2&childTab=inbox">
		                                  </c:when>
		                                  <c:when test="${fn:contains(tabIndex, '3')}">
		                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3&childTab=inbox">
		                                  </c:when>
		                                  <c:when test="${fn:contains(tabIndex, '4')}">
		                                         <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4&childTab=inbox">
		                                  </c:when>
		                                  <c:otherwise>
		                                       <a class="yuiampmenuitemlabel" href="${pageContext.request.contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=5&childTab=inbox">
		                                  </c:otherwise>
		                              	</c:choose>
										<digi:trn>View Messages</digi:trn></a>
									</li>
	                               	<li class="yuiampmenuitem_drop">
	                                   	<a class="yuiampmenuitemlabel" href="/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels"  onclick="return canExit()">
	                                       	<digi:trn key="aim:createmessage">Create Message</digi:trn>
	                                    </a>
	                                </li>
	                               </ul>
	                           </div>
	                         </div>                    
	                      </li>
                          </module:display>
                          <module:display name="Tools" parentModule="PROJECT MANAGEMENT">
                          <li class="yuiampmenuitem" style="float:left;">
                              <span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px">
                                 <digi:trn key="aim:tools">TOOLS</digi:trn>
                              </span>
                              <a  style="text-decoration:none;padding: 5px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="tools" class="yuiampmenu" style="opacity:0.9;z-index:9999;">
                                  <div class="bd bd_drop" >                    
                                      <ul >
                                      	<feature:display name="Language Option" module="Tools">
                                      		<li >
		                                        <a class="yuiampmenuitemlabel" href="#">
		                                        <digi:trn key="aim:deflanguage">Language</digi:trn>
		                                        </a>
		                                          <div id="reports2" class="yuiampmenu">
		                                              <div class="bd">                    
		                                                  <ul>
		                                                  	<digi:insert flush="false" attribute="dropdownLangSwitch" />
		                                                  </ul>
		                                              </div>
		                                          </div>                              
		                                    </li>
                                      	</feature:display>                                     
   
                                      	<feature:display name="Workspace Info" module="Tools">
                                        <li class="yuiampmenuitem_drop">
	                                        <a class="yuiampmenuitemlabel" onclick="return teamWorkspaceSetup('-1');" href="/aim/workspaceOverview.do~tId=-1~dest=teamLead">
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
		                                        <li class="yuiampmenuitem_drop">
			                                        <a class="yuiampmenuitemlabel" href="/aim/switchDevelopmentMode.do">
														<digi:trn key="admin:userdevmode">Developer Mode</digi:trn>
			                                        </a>
		                                        </li>
                                            </digi:secure>
                                        </logic:notEmpty> 
                                        <module:display name="/Activity Form/FM Mode" parentModule="/Activity Form">                                       
                                        <li id="switchFMMode" class="yuiampmenuitem_drop" style="display: none;">
                                        	<a class="yuiampmenuitemlabel" href="javascript:wicketSwitchFMMode()"  onclick="return canExit()">
                                        		<digi:trn key="aim:search">FM Mode</digi:trn>
                                       		 </a>
                                        </li>
                                        </module:display>
                                        <li class="yuiampmenuitem_drop">
                                        	<a class="yuiampmenuitemlabel" href="/search/search.do?reset=true"  onclick="return canExit()">
                                        		<digi:trn key="aim:search">Search</digi:trn>
                                       		 </a>
                                        </li>
                                        
                                        <li class="yuiampmenuitem_drop">
	                                        <a class="yuiampmenuitemlabel" href="/aim/addressBook.do?actionType=viewAddressBook&reset=true" onclick="return canExit()">
	                                        	<digi:trn>Address Book</digi:trn>
	                                        </a>
                                        </li>
                                        
                                      </ul>
                                  </div>
                              </div>                    
                          </li>
                          </module:display>
                          <module:display name="HELP">
                          <li class="yuiampmenuitem" style="float:left;">
                              <span class="yuiampmenuitemlabel" href="#"  style="float:left;position:relative;top:0px;_top:1px;">
                                  <digi:trn key="help:help">HELP</digi:trn>
                              </span>
                              <a  style="text-decoration:none;padding: 5px 8px 5px 0;_padding-bottom:5px;cursor:pointer;display:block;float:left;">
                                 <img src="/TEMPLATE/ampTemplate/css/menubaritem_submenuindicator_disabled.png" style="border:0px;padding:0px 0px 0px 0px;"/><br />
                              </a>
                              <div id="help" class="yuiampmenu" style="opacity:0.9;">
                                  <div class="bd bd_drop">                    
                                      <ul>
										<feature:display name="User Help" module="HELP">
                                        <li class="yuiampmenuitem_drop">
                                         <a class="yuiampmenuitemlabel" href="#" onClick="help();">
                                        	<digi:trn>AMP Help</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
										
										<feature:display name="Glossary" module="HELP">
                                        <li class="yuiampmenuitem_drop">	
                                        <a class="yuiampmenuitemlabel" href="/help/glossary.do">
                                        <digi:trn>Glossary</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
										
										<feature:display name="Support Request Form" module="HELP">
                                        <li class="yuiampmenuitem_drop">
                                        <a class="yuiampmenuitemlabel" href="http://support.ampdev.net/" target="_blank">
                                        <digi:trn key="aim:supportrequestform">Support Request Form</digi:trn>
                                        </a>
                                        </li>
										</feature:display>
										<feature:display name="About AMP" module="HELP">
                                        <li class="yuiampmenuitem_drop">
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
 <div style="clear:both;"></div>
</div>

<script language="javascript">

YAHOO.util.Event.onDOMReady(function () {
	var oMenuBar = new YAHOO.widget.MenuBar("mainmenuHeader");
	oMenuBar.render();
});
 </script>
