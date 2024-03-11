<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>


<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>


<!-- generate report headings -->
<bean:define id="viewable" name="groupReport" property="firstColumnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="reportHeading.jsp"/>

<!-- generate total row -->
<logic:present name="groupReport" property="parent">
	<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
	<bean:define id="grandTotal" toScope="request" value="yes" property="java.lang.String" />
	<jsp:include page="TrailCells.jsp"/>
	<tr>
		<td height="5px" colspan='<bean:write name="groupReport" property="totalDepth"/>'></td>
	</tr>
</logic:present>


	<logic:iterate name="groupReport"  property="items" id="item" scope="page">
		<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<logic:equal name="viewable" property="renderBody" value="true">
			<jsp:include page="<%=viewable.getViewerPath()%>"/>	
		</logic:equal>
	</logic:iterate>
	</td>
</tr>

