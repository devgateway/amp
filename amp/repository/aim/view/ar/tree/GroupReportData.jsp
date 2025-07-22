<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>
<logic:notPresent name="groupReport" property="parent">
        <tr><td>
        <input value="Expand All" type="button" onclick="treeObj.expandAll();"/>
        <input value="Collapse All" type="button" onclick="treeObj.collapseAll();"/>
        </td></tr>
        <tr><td>
		 <ul id="tree-<bean:write name="groupReport" property="name"/>" class="DHTMLSuite_tree">
</logic:notPresent>

<logic:notPresent name="groupReport" property="parent">
<li noDrag="true">
<a><bean:write name="groupReport" property="name"/></a>
<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
<jsp:include page="subtotalsNodeLine.jsp"/>
<ul>
</logic:notPresent>
<logic:present name="groupReport" property="parent">
<c:if test="${!(groupReport.name == groupReport.parent.name)}">
<div style='position:relative;display:none;' id='<bean:write name="groupReport" property="absoluteReportNameMD5"/>'> 
<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
<jsp:include page="/repository/aim/view/ar/print/TrailCells.jsp"/>
</div>
<li noDrag="true">
<a onMouseOver="stm(['Totals for <bean:write name="groupReport" property="name"/>',document.getElementById('<bean:write name="groupReport" property="absoluteReportNameMD5"/>').innerHTML],Style[0])" onMouseOut="htm()"><bean:write name="groupReport" property="name"/></a>
<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.GroupReportData" scope="page" toScope="request"/>
<jsp:include page="subtotalsNodeLine.jsp"/>
<ul>
</c:if>
</logic:present>
<logic:iterate name="groupReport"  property="items" id="item" scope="page">
        <bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
        <jsp:include page="<%=viewable.getViewerPath()%>"/>
</logic:iterate>


<logic:notPresent name="groupReport" property="parent">
</li>
</ul>
</logic:notPresent>
<logic:present name="groupReport" property="parent">
<c:if test="${!(groupReport.name == groupReport.parent.name)}">
</ul>
</li>
</c:if>
</logic:present>


<logic:notPresent name="groupReport" property="parent">
</ul>
</td></tr>
<script type="text/javascript">
        treeObj = new DHTMLSuite.JSDragDropTree();
        treeObj.setTreeId('tree-<bean:write name="groupReport" property="name"/>');
        treeObj.setMaximumDepth(7);
        treeObj.setMessageMaximumDepthReached('Maximum depth reached'); // If you want to show a message when maximum depth is reached, i.e. on drop.		
        treeObj.init();
        treeObj.showHideNode(false,'tree-<bean:write name="groupReport" property="name"/>');
</script>
</logic:notPresent>