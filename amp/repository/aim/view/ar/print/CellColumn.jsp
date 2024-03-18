<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.dgfoundation.amp.ar.cell.Cell" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.dgfoundation.amp.ar.CellColumn"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="cellColumn" name="viewable" type="org.dgfoundation.amp.ar.CellColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>

<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>
<bean:define id="isGroup" name="isGroup" type="java.lang.String" scope="request" toScope="page"/>

<% if (isGroup.equalsIgnoreCase("false")){%>
<td width="5%">&nbsp</td>
<%} %>
<td nowrap="nowrap" style="font-size:8pt;text-transform: uppercase">
<% Cell c=cellColumn.getByOwner(ownerId);%>
		<c:set var="key">
			aim:reportBuilder:<%=cellColumn.getName()%>
		</c:set>
	 	<digi:trn key="${key}">
	 		<%=cellColumn.getName()%>
		 </digi:trn>:
</td>
<td style="font-size:8pt;padding-left: 3px;" width="100%">
<% if(c!=null) {
	request.setAttribute("cell",c);
%> 
	<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.cell.Cell" scope="request" toScope="request"/>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>	 
<% } 
	else { %> 
	&nbsp; 
<% } %>
</td>
