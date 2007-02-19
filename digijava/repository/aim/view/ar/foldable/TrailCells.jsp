<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>

<!-- generate total row -->
<tr id='<bean:write name="reportData" property="absoluteReportName"/>' title='<bean:write name="reportData" property="levelDepth"/>'>
	<td style="padding-left:<%=10*(reportData.getLevelDepth()-1)%>" class=clsTableL1SubTotalEndSectionLabel colspan='<bean:write name="reportData" property="sourceColsCount"/>'>
<c:if test="${!(reportData.name == reportMeta.name)}">
		<img style="cursor:pointer" src="/repository/aim/view/images/images_dhtmlsuite/dhtmlgoodies_minus.gif" alt='shown' onclick='toggleRows(this,"<bean:write name="reportData" property="absoluteReportName"/>")' title="<bean:write name="reportData" property="levelDepth"/>" border="0" width="20"/>
</c:if>		
<b>
<bean:write name="reportData" property="name"/>
</b>
	</td>
	<logic:iterate name="reportData" property="trailCells" id="cell" scope="page">
		<td class=clsTableL1SubTotalEndSection>
		<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />			
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
		</td>
	</logic:iterate>
</tr>