<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>
<logic:present name="groupReport" property="parent">
	<tr>
		<td colspan="3" style="font-size: 12pt; color:#000000" >
			<bean:write name="groupReport" property="name"/>
		</td>
	</tr>
</logic:present>


<logic:iterate name="groupReport"  property="items" id="item" scope="page">
	
		<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>
		
</logic:iterate>

<!-- generate total row -->
<logic:present name="groupReport" property="parent">
	<tr>
	<td></td>
		<td colspan="2">
			<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
			<jsp:include page="TrailCells.jsp"/>	
		</td>
	</tr>
		
		
</logic:present>
