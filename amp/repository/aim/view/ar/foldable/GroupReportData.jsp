<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

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






