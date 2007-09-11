<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>
<bean:define id="bckColor" value="true" toScope="page"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="../reportHeadings.jsp"/>
<% String display=columnReport.getLevelDepth()>1?"display:none":"";%>

<!-- generate total row -->
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>
<!-- generate report data -->

<bean:define id="recordsPerPage" name="recordsPerPage" scope="request" toScope="page" type="java.lang.Integer"/>
<bean:define id="pageNumber" name="pageNumber" scope="request"  type="java.lang.Integer"/>

<c:set var="rowIdx" value="<%=new Integer(0)%>" scope="page"/>
<bean:define id="rowIdx" name="rowIdx" scope="page" toScope="page" type="java.lang.Integer"/>
	
<c:set var="paginarLocal" value="<%=new Boolean(false)%>" scope="page"/>
<bean:define id="paginarLocal" name="paginarLocal" type="java.lang.Boolean" toScope="page"/>

<bean:define id="paginar" name="paginar" type="java.lang.Boolean" scope="request" toScope="page"/>
<c:if test="${paginar}">
	<c:set var="paginar" value="<%=new Boolean(false)%>" scope="request"/>
	<c:set var="paginarLocal" value="<%=new Boolean(true)%>" scope="page"/>
	<bean:define id="paginarLocal" name="paginarLocal" type="java.lang.Boolean" toScope="page"/>
</c:if>


<logic:notEqual name="reportMeta" property="hideActivities" value="true">
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">

	<c:if test="<%=!paginarLocal.booleanValue() || paginarLocal.booleanValue() && rowIdx.intValue() >= pageNumber.intValue() * recordsPerPage.intValue() && rowIdx.intValue() < (pageNumber.intValue() + 1) * recordsPerPage.intValue()%>">
<% 
		if(bckColor.equals("true")) {
%>
<bean:define id="bckColor" value="false" toScope="page"/>

<tr onmouseout="setPointer(this, <%=rowIdx%>, 'out', '#eeeeee', '#66CCCC', '#FFFF00');" onmouseover="setPointer(this, <%=rowIdx%>, 'over', '#eeeeee', '#66CCCC', '#FFFF00');" style="<%=display%>">

	<logic:iterate name="columnReport" property="items" id="column" scope="page" indexId="columnNo">
		
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<bean:define id="columnNo" name="columnNo" type="java.lang.Integer" scope="page" toScope="request"/>
		<bean:define id="bckColor" name="bckColor" type="java.lang.String" toScope="request"/>		
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
	</logic:iterate>
</tr>
<% } else { %>
<bean:define id="bckColor" value="true" toScope="page"/>
<tr onmouseout="setPointer(this, <%=rowIdx%>, 'out', '#dddddd', '#66CCCC', '#FFFF00');" onmouseover="setPointer(this, <%=rowIdx%>, 'over', '#dddddd', '#66CCCC', '#FFFF00');" style="<%=display%>">
	<logic:iterate name="columnReport" property="items" id="column" scope="page" indexId="columnNo">
		
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<bean:define id="columnNo" name="columnNo" type="java.lang.Integer" scope="page" toScope="request"/>
		<bean:define id="bckColor" name="bckColor" type="java.lang.String" toScope="request"/>		
		<jsp:include page="<%=viewable.getViewerPath()%>"/>			
	</logic:iterate>
</tr>

<% 
	}
%>
	</c:if>
	<c:set var="rowIdx" value="<%=new Integer(rowIdx.intValue() + 1)%>" scope="page"/>
	<bean:define id="rowIdx" name="rowIdx" scope="page" toScope="page" type="java.lang.Integer"/>
</logic:iterate>
</logic:notEqual>

<c:if test="${paginarLocal}">
	<c:set var="totalPages" value="<%=new Integer(rowIdx.intValue() /recordsPerPage.intValue() + (rowIdx.intValue() % recordsPerPage.intValue() == 0 ? 0 : 1))%>" scope="page"/>
	<bean:define id="totalPages" name="totalPages" type="java.lang.Integer" toScope="request"/>
</c:if>
