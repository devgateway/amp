<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="groupColumn" name="viewable" type="org.dgfoundation.amp.ar.GroupColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>

<% if(groupColumn.getVisibleCellCount(ownerId)!=0){ %>
<table width="100%" border="1" bordercolor="#B0B0B0" cellspacing=0
	cellpadding=0 valign=top align=left style="border-collapse: collapse">
<tr><td rowspan='<bean:write name="groupColumn" property="rowSpan"/>'> 
<b><bean:write name="groupColumn" property="name"/></b>
</td></tr>
<logic:iterate name="groupColumn" property="items" id="column" scope="page">
	<tr><td>
	<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>	
	</td></tr>
</logic:iterate>
</table>
<%}%>