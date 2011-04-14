<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>

<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>
<bean:define id="columnNo" name="columnNo" type="java.lang.Integer" scope="request" toScope="page"/>
<bean:define id="bckColor" name="bckColor" type="java.lang.String" scope="request" toScope="page"/>		
<% Cell c=cellColumn.getByOwner(ownerId);%>
<logic:equal name="columnNo" value="0">
	<bean:define id="reportData" name="cellColumn" property="parent" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="page"/>
	<td class="inside" style="padding-left:<%=10+10*(reportData.getLevelDepth()-1)%>;" valign="middle"  bgcolor="<%= bckColor.equals("true")?"dbe5f1":"ffffff" %>" >
</logic:equal>
<logic:notEqual name="columnNo" value="0">
	<td class="inside"  bgcolor="<%= bckColor.equals("true")?"#dbe5f1":"#ffffff" %>">
</logic:notEqual> 
<% if(c!=null) {
	request.setAttribute("cell",c);
%> 
	<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
	<bean:define id="caller" name="cellColumn" type="org.dgfoundation.amp.ar.CellColumn" scope="page" toScope="request" />	
	<logic:equal name="columnNo" value="0">
		<logic:present name="currentMember" scope="session">
			<a href='/aim/selectActivityTabs.do~ampActivityId=<bean:write name="ownerId"/>' style="text-decoration: none">
		</logic:present>
		<logic:notPresent name="currentMember" scope="session">
			<a href='/aim/viewActivityPreview.do~public=true~pageId=2~activityId=<bean:write name="ownerId"/>' target=_blank style="text-decoration: none">
		</logic:notPresent>
	</logic:equal>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>
	<logic:equal name="columnNo" value="0">
	</a>
	</logic:equal>
<%}else{%>
	<logic:equal name="columnNo" value="0">
		<logic:present name="currentMember" scope="session">
		<a href='/aim/selectActivityTabs.do~ampActivityId=<bean:write name="ownerId"/>'>
	</logic:present>
		<logic:notPresent name="currentMember" scope="session">
		<a href='/aim/viewActivityPreview.do~pageId=2~activityId=<bean:write name="ownerId"/>' target=_blank>
	</logic:notPresent>
		<digi:trn key="amp:reports:unspecified">Unspecified</digi:trn>
	</a>
	</logic:equal>
	<%
		String parent = cellColumn.getParent().toString();
		String name = ArConstants.COLUMN_TOTAL;
		if (parent.contains("(") && parent.contains("Total Costs")) {%>
		<logic:notEqual name="columnNo" value="0">
			<div class="desktop_project_count_sel">0</div>
		</logic:notEqual>
	<%}else{%>
		<logic:notEqual name="columnNo" value="0">
			<div class="desktop_project_count_sel" style="text-align: left;font-style: italic;">
				<%=cellColumn.getName().trim()%>&nbsp;<digi:trn key="amp:reports:unspecified">Unspecified</digi:trn>
			</div>
		</logic:notEqual>
	<%}%>
<%}%>
</td>
