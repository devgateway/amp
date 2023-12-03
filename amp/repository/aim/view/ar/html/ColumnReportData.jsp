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

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.ampModule.aim.dbentity.AmpReports" toScope="page"/>

<!-- generate total row -->

<bean:define id="viewable" name="columnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>

<%int rowIdx = 2;%>

<!-- generate report data -->

<logic:notEqual name="reportMeta" property="hideActivities" value="true">	
<logic:iterate name="columnReport" property="ownerIds" id="ownerId" scope="page">
<logic:equal name="columnReport" property="canDisplayRow" value="true">
<%
	rowIdx++;	
	String width="0";
%>

<% 
	if(columnReport.getLevelDepth()<=2){
		request.setAttribute("pading","20px");
	}
	if (columnReport.getLevelDepth()==3){
		request.setAttribute("pading","30px");
	}
	if (columnReport.getLevelDepth()==4){
		request.setAttribute("pading","35px");
	}
%>
<%if (rowIdx%2==0){ %>
	<tr bgcolor="#FFFFFF" height="16px" onclick="setPointerhtml(this,'click','#A5BCF2','#FFFFFF');"
	 	onMouseover="setPointerhtml(this,'','#A5BCF2','#FFFFFF');"
	 	onMouseout="setPointerhtml(this,'','#FFFFFF','#FFFFFF');">
<%}else{%>
	<tr bgcolor="#DBE5F1" height="16px" onclick="setPointerhtml(this,'click','#A5BCF2','#DBE5F1');" 
		onMouseover="setPointerhtml(this,'','#A5BCF2','#DBE5F1');" 
		onMouseout="setPointerhtml(this,'','#DBE5F1','#DBE5F1');">
<%}%>
	<c:if test="${addFakeColumn}">
			<td></td>
		</c:if>
	<logic:iterate name="columnReport" property="items" id="column" scope="page">
		<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>
	</logic:iterate>	
</tr>
</logic:equal>
</logic:iterate>
</logic:notEqual>

