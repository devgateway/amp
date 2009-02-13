<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>
<% String display=reportData.getLevelDepth()>2?"display:none":"";%>
<!-- generate total row -->
<c:set var="depthRelatVar">
	<bean:write name="reportData" property="levelDepth"/>@<bean:write name="reportData" property="relativeRowNo"/>
</c:set>
<c:set var="reportDataNameVar">
<bean:write name="reportData" property="name"/>
</c:set>
<tr id='<bean:write name="reportData" property="absoluteReportName"/>' title="${depthRelatVar}" style="<%=display%>;" onmouseover='this.title="${reportDataNameVar}"' onmouseout='this.title="${depthRelatVar}"'>	
	<% if (reportData.getLevelDepth() < 3) { %>		
		<td style=" padding-left:<%=10*(reportData.getLevelDepth()-1)%>; background-color:#BFD2DF;border-bottom:1px solid white;" colspan='<bean:write name="reportData" property="sourceColsCount"/>' class="reportsBorderTD" nowrap>
	<% } else { %>	
		<td style=" padding-left:<%=10*(reportData.getLevelDepth()-1)%>; background-color:#dddddd;border-bottom:1px solid white" colspan='<bean:write name="reportData" property="sourceColsCount"/>' class="reportsBorderTD" nowrap>
	<% } %>	
<c:if test="${!(reportData.name == reportMeta.name)}">		
		<img id="toggleImage" name="<bean:write name="reportData" property="absoluteReportName"/>" style="cursor:pointer" src="/repository/aim/view/images/images_dhtmlsuite/dhtmlgoodies_plus.gif" alt='hidden' onclick="document.getElementById('<bean:write name="reportData" property="absoluteReportName"/>').title='${depthRelatVar}';toggleRows(this,'<bean:write name="reportData" property="absoluteReportName"/>')" title='<digi:trn key="aim:report:expandcollapse">Expand/Collapse</digi:trn>' border="0"/>
</c:if>		
  
<b>
<% if(reportData.getName().indexOf(':')!=-1) { %>
<%=reportData.getName().substring(reportData.getName().indexOf(':')+1,reportData.getName().length())%>

<% } else { %>
<bean:write name="reportData" property="name"/>
<% } %>
&nbsp;
(
<bean:write name="reportData" property="totalUniqueRows"/>
)
</b>
	</td>
	<logic:iterate name="reportData" property="trailCells" id="cell" scope="page">
	<% if (reportData.getLevelDepth() < 3) { %>
		<td style="background-color:#BFD2DF;border-bottom:1px solid white" class="reportsBorderTD">
	<% } else { %>	
		<td style="background-color:#dddddd;border-bottom:1px solid white" class="reportsBorderTD">
	<% } %>
		
		<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />	
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
		</td>
	</logic:iterate>
</tr>