<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

<logic:notPresent name="currentMember">
<% String publicView=FeaturesUtil.getGlobalSettingValue("Public View");
if("On".equals(publicView)) { %>
<DIV id="menu">
<UL>

		<LI class="noLink">
				<c:set var="message">
			<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
		</c:set>
			<digi:link styleClass="head-menu-link" href="/reportsPublicView.do" module="aim" onclick="return quitRnot1('${message}')">
					::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
			</digi:link>
		</LI>

		<LI class="noLink">
		<c:set var="message">
			<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
		</c:set>
		<digi:link styleClass="head-menu-link" href="/viewTeamReports.do" module="aim" onclick="return quitRnot1('${message}')">
				::: <digi:trn key="aim:reports">REPORTS</digi:trn></digi:link>
		</LI>
		<feature:display name="Content Repository" module="Document Management">
		<li>
			<digi:link styleClass="head-menu-link" href="/documentManager.do" module="contentrepository" onclick="return quitRnot()">			
				::: <digi:trn key="contentrepository:publicDocuments">Public Documents</digi:trn></digi:link>
		</li>
		</feature:display>

</UL>
</DIV>
<% } %>
</logic:notPresent>

<logic:present name="ampAdmin" scope="session">
<DIV id="menu">
	<UL>
		<LI class="noLink">&nbsp;</LI>

		<logic:equal name="ampAdmin" value="yes">
		<LI>
			<div id="gen" title='<digi:trn key="aim:clickToAccessAdminTools">Click here to access admin tools</digi:trn>'>
					<c:set var="message">
				<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
		</c:set>
				<digi:link styleClass="head-menu-link" href="/admin.do" module="aim" onclick="return quitRnot1('${message}')">
					::: <digi:trn key="aim:aminTools">ADMIN TOOLS</digi:trn>
				</digi:link>
			</div></LI>
		</logic:equal>
		<logic:equal name="ampAdmin" value="no">
			<logic:present name="<%= org.digijava.module.aim.helper.Constants.ONLY_PREVIEW %>" scope="request">
				<c:set var="message">
					no
				</c:set>
			</logic:present>
			<logic:notPresent name="<%= org.digijava.module.aim.helper.Constants.ONLY_PREVIEW %>" scope="request">
				<c:set var="message">
					<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
				</c:set>
			</logic:notPresent>
		<LI>
			<div id="gen" title='<digi:trn key="aim:enterIntoAIM">Enter in to Aid Information Module</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/showDesktop.do" module="aim" onclick="return quitRnot1('${message}')">
					::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
				</digi:link>
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot1('${message}')">
					::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
				</digi:link>
			</logic:empty>
			</div></LI>

		<LI>
			<div id="gen"  title='<digi:trn key="aim:viewPublicReports">View public team reports</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/viewTeamReports.do" module="aim" onclick="return quitRnot1('${message}')">
				::: <digi:trn key="aim:reports">REPORTS</digi:trn></digi:link>
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot1('${message}')">
				::: <digi:trn key="aim:reports">REPORTS</digi:trn></digi:link>
			</logic:empty>
			</div>
		</LI>
			<%--<feature:display name="Documents Tab" module="Document">
		    	<LI><a class="head-menu-link" href="/viewAllDocuments.do" module="aim" onclick="return quitRnot1('${message}')">::: <digi:trn key="aim:documentsHeader">DOCUMENTS</digi:trn></a></LI>
		    </feature:display> --%>
		    <module:display name="Scenarios" parentModule="PROJECT MANAGEMENT">
		    	<LI><a class="head-menu-link">::: <digi:trn key="aim:scenarios">SCENARIOS</digi:trn></a></LI>
	    	</module:display>
			<feature:display name="Content Repository" module="Document Management">
			<LI><a class="head-menu-link" href="/contentrepository/documentManager.do" module="aim" onclick="return quitRnot1('${message}')">::: <digi:trn key="contentrepository:contentRepositoryTitle">CONTENT REPOSITORY</digi:trn></a></LI>
			</feature:display>
	    <feature:display name="Calendar" module="Calendar">
		<LI>
			<div id="gen" title='<digi:trn key="aim:viewPlanningCalendar">View Planning Calendar</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/showCalendarView.do" module="calendar" onclick="return quitRnot1('${message}')">
				::: <digi:trn key="aim:calendar">CALENDAR</digi:trn></digi:link>
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot1('${message}')">
				::: <digi:trn key="aim:calendar">CALENDAR</digi:trn></digi:link>
			</logic:empty>
			</div>
		</LI>
		</feature:display>
		
    	<LI>
			<div id="gen"  title='<digi:trn key="aim:viewNPD">View National Plan Dashboard</digi:trn>'>
			<logic:notEmpty name="TID" scope="session">
                             <c:set var="linkProf">
                                 <feature:display name="Dashboard" module="M & E">
                                     /viewPortfolioDashboard.do~actId=-1~indId=-1
                                 </feature:display>
                                 
                             </c:set>
                             <c:set var="linkNPD">
                                <module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
                                    /nationalPlaningDashboard.do
                                </module:display>
                  
                             </c:set>
                            
                                 <c:set var="link">
                                     <c:if test="${empty linkProf}">
                                         ${linkNPD}
                                     </c:if>
                                      <c:if test="${not empty linkProf}">
                                         ${linkProf}
                                     </c:if>
                                  </c:set>
                                 
                           
                            
                             
                          
             <c:if test="${not empty link}">
                   <digi:link styleClass="head-menu-link" href="${link}" module="aim" onclick="return quitRnot1('${message}')">
                                     
                                     ::: <digi:trn key="aim:NPD">NPD</digi:trn>
                      </digi:link>
             </c:if>
             
				
				
			</logic:notEmpty>
			<logic:empty name="TID" scope="session">
				<digi:link styleClass="head-menu-link" href="/" module="aim" onclick="return quitRnot1('${message}')">
				::: <digi:trn key="aim:NPD">NPD</digi:trn></digi:link>
			</logic:empty>
			</div>
		</LI>
		
		<module:display name="HELP">
		
		<LI>
			<div id="gen"  title='<digi:trn key="help:viewHelpPage">View Help</digi:trn>'>
				<digi:link styleClass="head-menu-link" href="/help.do?blankPage=true" module="help">
					::: <digi:trn key="help:help">Help</digi:trn>
				</digi:link>
			</div>
		</LI>
		</module:display>
		</logic:equal>


	</UL>
</DIV>
</logic:present>
