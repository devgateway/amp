<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript">
	function addActivity() {
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~action=create";	
	}
	
	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
</script>

	
<digi:context name="digiContext" property="context" />


<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<ul id="MyTabs" class="shadeTabs">

<logic:iterate name="myReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
			<logic:equal name="report" property="drilldownTab" value="true">
			<li><a id='Tab-<bean:write name="report" property="name"/>' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><bean:write name="report" property="name"/></a></li>
			</logic:equal>
</logic:iterate>
</ul>
<div id="ajaxcontentarea" class="contentstyle">
Text
</div>

<script type="text/javascript">
//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
startajaxtabs("MyTabs");
reloadTab("MyTabs","Tab-By Project");
</script>

<div id="debug"></div>
