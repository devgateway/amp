<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
/*	function addActivity() {
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~action=create";	
	}
	*/
	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
	
	var tabName	= "Tab-By Project";
	<logic:empty name="filterCurrentReport" scope="session">
	<logic:notEmpty name="defaultTeamReport" scope="session">
			tabName	= 'Tab-<bean:write name="defaultTeamReport" scope="session" property="name"/>';
	</logic:notEmpty>
	</logic:empty>
	<logic:notEmpty name="filterCurrentReport" scope="session">
		tabName	= 'Tab-<bean:write name="filterCurrentReport" scope="session" property="name"/>';
	</logic:notEmpty>	
	
</script>

	
<digi:context name="digiContext" property="context" />


<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<ul id="MyTabs" class="shadeTabs">
<logic:present name="myReports" scope="session">
	<logic:iterate name="myReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
				<logic:equal name="report" property="drilldownTab" value="true">
				<li><a id='Tab-${report.name}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><digi:trn key="aim:clickreport:tabs:${report.nameTrn}">${report.name}</digi:trn></a></li>
				</logic:equal>
	</logic:iterate>
</logic:present>

</ul>
<div id="ajaxcontentarea" class="contentstyle">
<digi:trn key="aim:clickOnATab">
<p/>
Click on one of the tabs to display activities. You can create more tabs by using the Advanced Reports Manager.
</digi:trn>
</div>
<c:set var="loadstatustext">
	<digi:trn key="aim:loadstatustext">Requesting Content</digi:trn>
</c:set>
<script type="text/javascript">
	loadstatustext='<img src="/repository/aim/view/scripts/ajaxtabs/loading.gif" /> <%=((String) pageContext.getAttribute("loadstatustext")).replaceAll("\r\n"," ")%>';
	//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
	startajaxtabs("MyTabs");
	reloadTab("MyTabs",tabName);
</script>

<div id="debug"></div>
