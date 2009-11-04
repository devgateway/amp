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

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="../reportHeadings.jsp"/>


<% String display=columnReport.getLevelDepth()>1?"display:none":"";%>

<!-- generate total row -->
<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>

<!-- generate report data -->

<%int rowIdx = 2;%>
<logic:notEqual name="reportMeta" property="hideActivities" value="true">
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">

<logic:equal name="columnReport" property="canDisplayRow" value="true">


<%
	Integer counter = (Integer)session.getAttribute("progressValue");
	counter++;
	session.setAttribute("progressValue", counter);
%>

<% 
		if(bckColor.equals("true")) {
%>

<bean:define id="bckColor" value="false" toScope="page"/>
<tr onmouseout="setPointer(this, <%=rowIdx%>, 'out', '#ffffff', '#a5bcf2', '#FFFF00');" onmouseover="setPointer(this, <%=rowIdx%>, 'over', '#ffffff', '#a5bcf2', '#FFFF00');" style="<%=display%>">
	<c:if test="${addFakeColumn}">
			<td bgcolor="<%= bckColor.equals("true")?"dbe5f1":"ffffff" %>" class="reportsBorderTD" ></td>
		</c:if>
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
<tr onmouseout="setPointer(this, <%=rowIdx%>, 'out', '#dbe5f1', '#a5bcf2', '#FFFF00');" onmouseover="setPointer(this, <%=rowIdx%>, 'over', '#dbe5f1', '#a5bcf2', '#FFFF00');" style="<%=display%>">
		<c:if test="${addFakeColumn}">
				<td bgcolor="<%= bckColor.equals("true")?"dbe5f1":"ffffff" %>" class="reportsBorderTD" ></td>
		</c:if>
		
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
</logic:equal>
	<%rowIdx++;	%>
</logic:iterate>
</logic:notEqual>


