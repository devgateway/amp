<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>

<logic:present name="groupReport" property="parent">
	<c:if test="${(groupReport.name == groupReport.parent.name)}">
		<logic:present name="groupReport" property="firstColumnReport" scope="page">
			<bean:define id="viewable" name="groupReport" property="firstColumnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
			<jsp:include page="reportHeadings.jsp"/>
		</logic:present>
	</c:if>
		<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<jsp:include page="TrailCells.jsp"/>
</logic:present>

<logic:iterate name="groupReport"  property="items" id="item" scope="page">
		<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<logic:equal name="viewable" property="renderBody" value="true">
			<jsp:include page="<%=viewable.getViewerPath()%>"/>	
		</logic:equal>
</logic:iterate>






