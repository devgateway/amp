<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil,org.digijava.module.aim.helper.Constants" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:context name="digiContext" property="context" />

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<logic:iterate name="publicReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports" indexId="position"> 
	<logic:equal name="report" property="publicReport" value="true">
		<logic:equal name="report" property="drilldownTab" value="true">
			<logic:equal name="position" value="0">
				<bean:define id="firstReportName" name="report" property="name" toScope="Page"/>
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
	} else { 
	%>	
	<digi:trn key="amp:showallBudget">
	Showing all activities...
	</digi:trn>
	<% 
	} 
	%>
	</logic:notPresent>
</logic:present>

<ul id="PublicTabs" class="shadeTabs">
<logic:iterate name="publicReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports" indexId="position"> 
			<logic:equal name="report" property="publicReport" value="true">
				<logic:equal name="report" property="drilldownTab" value="true">
					<li><a id='Tab-<bean:write name="report" property="name"/>' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><bean:write name="report" property="name"/></a></li>
				</logic:equal>
			</logic:equal>
</logic:iterate>
</ul>


<logic:notPresent name="firstReportName">
<digi:trn key="amp:noPublicTabs">
No Public Tabs
</digi:trn>
</logic:notPresent>

<div id="ajaxcontentarea" class="contentstyle">
</div>	
<logic:present name="firstReportName">
	<script type="text/javascript">
	//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
	
	startajaxtabs("PublicTabs");
	reloadTab('PublicTabs','Tab-<bean:write name="firstReportName"/>');
	</script>
</logic:present>


