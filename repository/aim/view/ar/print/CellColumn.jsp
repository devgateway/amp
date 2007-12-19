<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.dgfoundation.amp.ar.CellColumn"%>
<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>

<% Cell c=cellColumn.getByOwner(ownerId);%>

<b>			
	<c:set var="key">
		aim:reportBuilder:<%=cellColumn.getName()%>
	</c:set>
	 	<digi:trn key="${key}">
	 		<%=cellColumn.getName()%>
		 </digi:trn>
	
	
</b>

&nbsp;
<% if(c!=null) {
	request.setAttribute("cell",c);
%> 
	<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>	 
<% } else { %> &nbsp; <% } %>
