<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>

<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>

<% Cell c=cellColumn.getByOwner(ownerId);%>
<td valign="top" class="clsTableCellData" bgcolor="#FFFFFF">
<% if(c!=null) {
	request.setAttribute("cell",c);
%> 
<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
<bean:define id="caller" name="cellColumn" type="org.dgfoundation.amp.ar.CellColumn" scope="page" toScope="request" />	

<jsp:include page="<%=viewable.getViewerPath()%>"/>	
<% } else { %> &nbsp; <% } %>
</td>
