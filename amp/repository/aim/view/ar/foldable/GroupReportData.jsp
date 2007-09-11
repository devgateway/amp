<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>


<bean:define id="recordsPerPage" name="recordsPerPage" scope="request" toScope="page" type="java.lang.Integer"/>
<bean:define id="pageNumber" name="pageNumber" scope="request"  type="java.lang.Integer"/>

<c:set var="rowIdx" value="<%=new Integer(0)%>" scope="page"/>
<bean:define id="rowIdx" name="rowIdx" scope="page" toScope="page" type="java.lang.Integer"/>

<c:set var="paginarLocal" value="<%=new Boolean(false)%>" scope="page"/>
<bean:define id="paginarLocal" name="paginarLocal" type="java.lang.Boolean" toScope="page"/>

<logic:present name="groupReport" property="parent">
	<c:if test="${(groupReport.name == groupReport.parent.name)}">
	<bean:define id="viewable" name="groupReport" property="firstColumnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
	<jsp:include page="../reportHeadings.jsp"/>
	</c:if>
	
	<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
	<jsp:include page="TrailCells.jsp"/>

	<bean:define id="paginar" name="paginar" type="java.lang.Boolean" scope="request" toScope="page"/>
	<c:if test="${paginar}">
		<c:set var="paginar" value="<%=new Boolean(false)%>" scope="request"/>
		<c:set var="paginarLocal" value="<%=new Boolean(true)%>" scope="page"/>
		<bean:define id="paginarLocal" name="paginarLocal" type="java.lang.Boolean" toScope="page"/>
	</c:if>
</logic:present>


<tr><td>


<logic:iterate name="groupReport"  property="items" id="item" scope="page">
	<c:if test="<%=!paginarLocal.booleanValue() || paginarLocal.booleanValue() && rowIdx.intValue() >= pageNumber.intValue() * recordsPerPage.intValue() && rowIdx.intValue() < (pageNumber.intValue() + 1) * recordsPerPage.intValue()%>">
		<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
	</c:if>
	
	<c:set var="rowIdx" value="<%=new Integer(rowIdx.intValue() + 1)%>" scope="page"/>
	<bean:define id="rowIdx" name="rowIdx" scope="page" toScope="page" type="java.lang.Integer"/>
</logic:iterate>

</td></tr>

<c:if test="${paginarLocal}">
	<c:set var="totalPages" value="<%=new Integer(rowIdx.intValue() /recordsPerPage.intValue() + (rowIdx.intValue() % recordsPerPage.intValue() == 0 ? 0 : 1))%>" scope="page"/>
	<bean:define id="totalPages" name="totalPages" type="java.lang.Integer" toScope="request"/>
</c:if>