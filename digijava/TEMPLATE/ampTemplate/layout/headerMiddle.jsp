<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
			
<DIV id="menu">
	<UL>
		<LI class="noLink">&nbsp;</LI>
		<LI>
			<div id="gen" title='<digi:trn key="aim:enterIntoAIM">Enter in to Aid Information Module</digi:trn>'>
			<digi:link styleClass="head-menu-link" href="/showDesktop.do" module="aim" onclick="return quitRnot()">
			::: <digi:trn key="aim:aidInformationModule">AID INFORMATION MODULE</digi:trn>
			</digi:link></div></LI> 
		<LI>
			<div id="gen"  title='<digi:trn key="aim:viewPublicReports">View public team reports</digi:trn>'>
			<digi:link styleClass="head-menu-link" href="/viewTeamReports.do" module="aim" onclick="return quitRnot()">
			::: <digi:trn key="aim:reports">REPORTS</digi:trn></digi:link></div></LI> 
		<LI><a class="head-menu-link">::: <digi:trn key="aim:documentsHeader">DOCUMENTS</digi:trn></a></LI>
		<LI>
			<div id="gen" title='<digi:trn key="aim:viewPlanningCalendar">View Planning Calendar</digi:trn>'>
			<digi:link styleClass="head-menu-link" module="calendar" href="/showCalendarView.do" onclick="return quitRnot()">
			::: <digi:trn key="aim:calendar">CALENDAR</digi:trn></digi:link></div></LI> 
		<logic:notEmpty name="ME" scope="application">
    	<LI>
			<div id="gen"  title='<digi:trn key="aim:viewMEDashboard">View M&E Dashboard</digi:trn>'>
			<digi:link styleClass="head-menu-link" href="/viewPortfolioDashboard.do~actId=-1~indId=-1" module="aim" onclick="return quitRnot()">
			::: <digi:trn key="aim:medashboard">M & E DASHBOARD</digi:trn></digi:link></div></LI>
		</logic:notEmpty>
	</UL>
</DIV>
