<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>
<% Cell c=cellColumn.getByOwner(ownerId);%>
<td  nowrap="nowrap" class="clsTableCellDataHtml">
	<% if(c!=null) {
		request.setAttribute("cell",c);
	%> 
	<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
	<bean:define id="caller" name="cellColumn" type="org.dgfoundation.amp.ar.CellColumn" scope="page" toScope="request" />	

	<jsp:include page="<%=viewable.getViewerPath()%>"/>	
	<% } else { %> &nbsp; <% } %>
</td>
