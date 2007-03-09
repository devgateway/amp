<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>

<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>
<bean:define id="columnNo" name="columnNo" type="java.lang.Integer" scope="request" toScope="page"/>
<bean:define id="bckColor" name="bckColor" type="java.lang.String" scope="request" toScope="page"/>		
<% Cell c=cellColumn.getByOwner(ownerId);%>
<logic:equal name="columnNo" value="0">
<bean:define id="reportData" name="cellColumn" property="parent" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="page"/>
<td style=" padding-left:<%=10+10*(reportData.getLevelDepth()-1)%>" valign="top"  bgcolor="<%= bckColor.equals("true")?"DDDDDD":"ffffff" %>" class="reportsBorderTD" >
</logic:equal>
<logic:notEqual name="columnNo" value="0">

<td valign="top"  class="reportsBorderTD" bgcolor="<%= bckColor.equals("true")?"DDDDDD":"ffffff" %>" >
</logic:notEqual> 
<% if(c!=null) {
	request.setAttribute("cell",c);
%> 
<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
<bean:define id="caller" name="cellColumn" type="org.dgfoundation.amp.ar.CellColumn" scope="page" toScope="request" />	

<logic:equal name="columnNo" value="0">
<b>
<logic:present name="currentMember" scope="session">
<a href='/aim/viewChannelOverview.do~tabIndex=0~ampActivityId=<bean:write name="ownerId"/>'>
</logic:present>
<logic:notPresent name="currentMember" scope="session">
<a href='/aim/viewActivityPreview.do~pageId=2~activityId=<bean:write name="ownerId"/>' target=_blank>
</logic:notPresent>
</logic:equal>
<jsp:include page="<%=viewable.getViewerPath()%>"/>	
<logic:equal name="columnNo" value="0">
</a>
</b>
</logic:equal>

<% } else { %> &nbsp; <% } %>
</td>
