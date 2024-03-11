<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
