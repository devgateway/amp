<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
<%@page import="org.dgfoundation.amp.ar.AmountCellColumn"%>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>

<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>
<bean:define id="columnNo" name="columnNo" type="java.lang.Integer" scope="request" toScope="page"/>
<bean:define id="bckColor" name="bckColor" type="java.lang.String" scope="request" toScope="page"/>		
<% Cell c=cellColumn.getByOwner(ownerId);%>
<logic:equal name="columnNo" value="0">
	<bean:define id="reportData" name="cellColumn" property="parent" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="page"/>
	<td class="report_inside" style="padding-left:<%=10+10*(reportData.getLevelDepth()-1)%>;" valign="middle"  bgcolor="<%= bckColor.equals("true")?"#F2F2F2":"ffffff" %>" >
</logic:equal>
<logic:notEqual name="columnNo" value="0">
	<td class="report_inside"  bgcolor="<%= bckColor.equals("true")?"#F2F2F2":"#ffffff" %>">
</logic:notEqual> 
<% if(c!=null) {
	request.setAttribute("cell",c);
%> 
	<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
	<bean:define id="caller" name="cellColumn" type="org.dgfoundation.amp.ar.CellColumn" scope="page" toScope="request" />	
	<logic:equal name="columnNo" value="0">
				<a href='/aim/viewActivityPreview.do~activityId=<bean:write name="ownerId"/>' style="text-decoration: none">
	</logic:equal>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>
	<logic:equal name="columnNo" value="0">
	</a>
	</logic:equal>
<%}else{%>
	<logic:equal name="columnNo" value="0">
		<a href='/aim/viewActivityPreview.do~activityId=<bean:write name="ownerId"/>'>
		<digi:trn key="amp:reports:unspecified">Unspecified</digi:trn>
	</a>
	</logic:equal>
	<%
		String parent = cellColumn.getParent().toString();
		String name = ArConstants.COLUMN_TOTAL;
		//if (parent.contains("(") && parent.contains("Total Costs"))
		if (cellColumn instanceof AmountCellColumn)
		{%>
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
