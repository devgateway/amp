<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>




<logic:notPresent name="groupReport" property="parent">
        <tr><td>
        <input value="Expand All" type="button" onclick="treeObj.expandAll();"/>
        <input value="Collapse All" type="button" onclick="treeObj.collapseAll();"/>
        </td></tr>
        <tr><td>
		 <ul id="tree-<bean:write name="groupReport" property="name"/>" class="DHTMLSuite_tree">
</logic:notPresent>
<li noDrag="true"><a><bean:write name="groupReport" property="name"/></a>

<ul>
<logic:iterate name="groupReport"  property="items" id="item" scope="page">
        <bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
        <jsp:include page="<%=viewable.getViewerPath()%>"/>
</logic:iterate>
</ul>
</li>


<logic:notPresent name="groupReport" property="parent">
</ul>
</td></tr>
<script type="text/javascript">
        treeObj = new DHTMLSuite.JSDragDropTree();
        treeObj.setTreeId('tree-<bean:write name="groupReport" property="name"/>');
        treeObj.setMaximumDepth(7);
        treeObj.setMessageMaximumDepthReached('Maximum depth reached'); // If you want to show a message when maximum depth is reached, i.e. on drop.
        treeObj.init();
</script>

</logic:notPresent>
