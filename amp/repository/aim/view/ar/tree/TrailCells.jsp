<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

<!-- generate total row -->
<tr>
	<td class=clsTableL1SubTotalEndSectionLabel colspan='<bean:write name="reportData" property="sourceColsCount"/>'>
	TOTALS FOR <bean:write name="reportData" property="name"/>
	</td>
	<logic:iterate name="reportData" property="trailCells" id="cell" scope="page">
		<td class=clsTableL1SubTotalEndSection>
		<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />			
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
		</td>
	</logic:iterate>
</tr>