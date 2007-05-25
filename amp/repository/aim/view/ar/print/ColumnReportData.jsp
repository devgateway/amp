<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<tr><td>
<i><bean:write name="columnReport" property="name"/></i>
</td></tr>


<!-- generate report data -->
<logic:notEqual name="reportMeta" property="hideActivities" value="true">
<table width="700" cellpadding="1" cellspacing="0" bgcolor="#FFFFFF">
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">
<tr><td colspan="2">
<br/>
</td></tr>
<tr><td align="left" valign="top"> 
	<table cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
	<logic:iterate name="columnReport" property="items" id="column" scope="page">
	<logic:equal name="column" property="name" value="Total">
	<tr><td>
	<br/>
	</td></tr>	
	</logic:equal>
	<logic:notEqual name="column" property="name" value="Funding">
	<tr><td>
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>			
	</td></tr>
	</logic:notEqual>
	</logic:iterate>
	</table>
</td><td align="left" valign="top">
	<table cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
	<logic:iterate name="columnReport" property="items" id="column" scope="page">
	<logic:equal name="column" property="name" value="Funding">
	<tr><td>
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>			
	</td></tr>
	</logic:equal>
	</logic:iterate>
	</table>
</td></tr>
<tr><td colspan="2">
<hr/>
</td></tr>
</logic:iterate>
</table>
</logic:notEqual>

<!-- generate total row -->

<tr><td>
<br/>
</td></tr>

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.ColumnReportData" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>	

<tr><td>
<hr/>
</td></tr>
