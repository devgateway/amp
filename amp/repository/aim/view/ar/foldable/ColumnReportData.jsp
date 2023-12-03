<%@page import="org.digijava.ampModule.aim.util.TeamUtil"%>
<%@page import="org.digijava.ampModule.aim.dbentity.AmpTeam"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ page import="org.dgfoundation.amp.ar.cell.MetaTextCell"%>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ page import="org.digijava.ampModule.aim.util.ActivityUtil" %>
<%@ page import="org.digijava.ampModule.aim.helper.TeamMember" %>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>


<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.ampModule.aim.dbentity.AmpReports" toScope="page"/>
<bean:define id="bckColor" value="true" toScope="page"/>

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="reportHeadings.jsp"/>

<c:set var="activityEditURL">
	<field:display name="Add SSC Button" feature="Edit Activity">/wicket/onepager/ssc</field:display>
	<field:display name="Add Activity Button" feature="Edit Activity">/wicket/onepager/activity</field:display>
</c:set>

<% String display=columnReport.getLevelDepth()>1?"display:none":"";%>

<!-- generate total row -->
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>

<!-- generate report data -->

<%!
	java.util.Set<Long> validatedActivities;
%>
<%
	int rowIdx = 2;
	Boolean showColumn = false;
	TeamMember currentMember = (TeamMember) request.getSession().getAttribute("currentMember");
	//Boolean crossteamValidationEnabled = false;
	Boolean isPublic = request.getParameter("public") != null && request.getParameter("public").equalsIgnoreCase("true");
	boolean canApproveActivities = currentMember != null && (currentMember.getTeamHead() || currentMember.isApprover());
	
	if (currentMember != null && "Management".toLowerCase().compareTo(currentMember.getTeamAccessType().toLowerCase()) != 0) {
		//crossteamValidationEnabled = currentMember.getAppSettings().isCrossTeamValidationEnabled();
		showColumn = true;
		validatedActivities = showColumn ? ActivityUtil.getActivitiesWhichShouldBeValidated(currentMember, columnReport.getOwnerIds()) : null;	
}
%>

<c:set var="translatedEdit"> 
	<digi:trn>Edit</digi:trn>
</c:set>
<c:set var="translatedValidate">
    <digi:trn>Validate activity</digi:trn>
</c:set>
<c:set var="translatedActivate">
	<digi:trn>Activate</digi:trn>
</c:set>

<logic:notEqual name="reportMeta" property="hideActivities" value="true">
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">

<logic:equal name="columnReport" property="canDisplayRow" value="true">

<c:set var="action" value="edit"/>
<c:set var="actionString" value="${translatedEdit}"/>
<%
	ReportContextData.getFromRequest().increaseProgressValue(1);
	//This scriptlet searchs for rows marked green (for validation) and later, sets the "action" attribute to "validate" to change the icon.
	Boolean validateItem = showColumn && canApproveActivities && validatedActivities.contains(ownerId);
	if (validateItem){
%>
	<c:set var="action" value="validate"/>
	<c:set var="actionString" value="${translatedValidate}"/>
	<%
}
%>
	
<% 
		if(bckColor.equals("true")) {
%>

<bean:define id="bckColor" value="false" toScope="page"/>
<tr style="<%=display%>">
	<c:if test="${addFakeColumn}">
		<td class="desktop_project_name"></td>
	</c:if>
		<td align="center" class="report_inside" width="25">
			<logic:present name="currentMember" scope="session">
				<%if (showColumn) { %>
				<a href='${activityEditURL}/${ownerId}' style="text-decoration: none">
					<img src="/TEMPLATE/ampTemplate/img_2/ico_${action}.gif" border="0" height="16" width="16" title="${actionString}"><br/>
				</a>
				<%} %>
			</logic:present>
		</td>
	
	<logic:iterate name="columnReport" property="items" id="column" scope="page" indexId="columnNo">
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<bean:define id="columnNo" name="columnNo" type="java.lang.Integer" scope="page" toScope="request"/>
		<bean:define id="bckColor" name="bckColor" type="java.lang.String" toScope="request"/>		
		<jsp:include page="<%=viewable.getViewerPath()%>"/>
	</logic:iterate>
</tr>
<% } else { %>
<bean:define id="bckColor" value="true" toScope="page"/>
<tr style="<%=display%>">
		<c:if test="${addFakeColumn}">
				<td>
					<div class="desktop_project_name_sel"></div>
				</td>
		</c:if>
			<td align="center" class="report_inside" width="25">
				<logic:present name="currentMember" scope="session">
				<%if (showColumn) { %>
					<a href='${activityEditURL}/${ownerId}' style="text-decoration: none">
						<img src="/TEMPLATE/ampTemplate/img_2/ico_${action}.gif" border="0" height="16" width="16" title="<digi:trn>${actionString}</digi:trn>"><br/>
					</a>
				<%} %>
				</logic:present>
			</td>
		
		<logic:iterate name="columnReport" property="items" id="column" scope="page" indexId="columnNo">
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<bean:define id="columnNo" name="columnNo" type="java.lang.Integer" scope="page" toScope="request"/>
		<bean:define id="bckColor" name="bckColor" type="java.lang.String" toScope="request"/>		
		<jsp:include page="<%=viewable.getViewerPath()%>"/>			
		</logic:iterate>
</tr>
<% 
	}
%>
</logic:equal>
	<%rowIdx++;	%>
</logic:iterate>
</logic:notEqual>


