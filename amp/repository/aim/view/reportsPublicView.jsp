<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.ampModule.aim.util.FeaturesUtil,org.digijava.ampModule.aim.helper.Constants" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:context name="digiContext" property="context" />
<style type="text/css">

table.inside, .desktop_project_name_sel{
 background-color:transparent;
}
.desktop_project_name_sel{
border: none;
}
</style>

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<bean:define id="firstReportFound"  value="false" toScope="page"/>

<logic:iterate name="publicReports" id="report" scope="session" type="org.digijava.ampModule.aim.dbentity.AmpReports" indexId="position">
	<logic:equal name="report" property="publicReport" value="true"> 
		<logic:equal name="report" property="drilldownTab" value="true">
			<logic:equal name="firstReportFound" value="false">
				<bean:define id="firstReportName" name="report" property="id" toScope="Page"/>
				<bean:define id="firstReportFound" value="true" toScope="page"/>
			</logic:equal>
		</logic:equal>
	</logic:equal>
</logic:iterate>

<logic:present name="firstReportName">
	<logic:notPresent name="currentMember">
	<%
	String onOff=FeaturesUtil.getGlobalSettingValue(Constants.GLOBAL_BUDGET_FILTER);
	if("On Budget".equals(onOff)) {
	%>
	<digi:trn key="amp:showOnBudget">
	Showing all On Budget activities...
	</digi:trn>
	<% 
	} 
	else if("Off Budget".equals(onOff)) {
	%>
	<digi:trn key="amp:showOffBudget">
	Showing all Off Budget activities...
	</digi:trn>
	<% 
	} 
	%>
	</logic:notPresent>
</logic:present>
<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> 
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn> 
</c:set>

<script language="JavaScript">
function toggleSettings(){
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	var framediv  = document.getElementById('ajaxcontentarea');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings}";
		framediv.style.height=637;
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings}";
		framediv.style.height=737;
	}
}
function preventTabClickEvent(e){
	if ($(e.target).parents('#PublicTabs').length > 0 ) {
		e.stopPropagation();
		e.preventDefault();
	}
}

	
</script>

<div id="content"  style="padding-left:10px;width:98%;min-width:680px;"> 
<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;font-size:10px;">

<logic:present name="firstReportName">
<ul id="PublicTabs" class="yui-nav">
<logic:iterate name="publicReports" id="report" scope="session" type="org.digijava.ampModule.aim.dbentity.AmpReports" indexId="position">
			<logic:equal name="report" property="publicReport" value="true">
				<logic:equal name="report" property="drilldownTab" value="true">
                        <li><a id='Tab-<bean:write name="report" property="id"/>' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><div><bean:write name="report" property="name"/></div></a></li> 
				</logic:equal>
			</logic:equal>
</logic:iterate>
</ul>
</div>
</logic:present>
<logic:notPresent name="firstReportName">
<br /><br />
<div id="ajaxcontentarea" class="contentstyle" style="overflow:auto; border:1px solid #D0D0D0; min-height:637px;font-size:12px;">
<digi:trn key="aim:noPublicTabs">
	No Public Tabs
</digi:trn>
</div>
</logic:notPresent>

<logic:present name="firstReportName">
<div id="ajaxcontentarea" class="contentstyle" style="overflow:auto; border:1px solid #D0D0D0; min-height:637px;font-size:12px;">
</div>
</div>	
	<script type="text/javascript">
	//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
	
	startajaxtabs("PublicTabs");
	tabName = 'Tab-<bean:write name="firstReportName"/>';
	<logic:notEmpty name="filterCurrentReport" scope="session">
		tabName	= 'Tab-<bean:write name="filterCurrentReport" scope="session" property="ampReportId"/>';
	</logic:notEmpty>
	reloadTab('PublicTabs',tabName);
	</script>
</logic:present>


