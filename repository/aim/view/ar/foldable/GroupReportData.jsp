<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="groupReport" name="viewable" type="org.dgfoundation.amp.ar.GroupReportData" scope="request" toScope="page"/>

<%
	int rowIdx = 0;
	int recordsPerPage = ((Integer)request.getAttribute("recordsPerPage")).intValue();
	int pageNumber = ((Integer)request.getAttribute("pageNumber")).intValue();
	boolean	paginar = false;
%>

<logic:present name="groupReport" property="parent">
<c:if test="${(groupReport.name == groupReport.parent.name)}">
<bean:define id="viewable" name="groupReport" property="firstColumnReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="../reportHeadings.jsp"/>
</c:if>

<bean:define id="viewable" name="groupReport" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
<jsp:include page="TrailCells.jsp"/>

<%
	if(((Boolean)request.getAttribute("paginar")).booleanValue()){
		request.setAttribute("paginar", new Boolean(false));	
		paginar = true;
	}
%>

</logic:present>


<tr><td>


<logic:iterate name="groupReport"  property="items" id="item" scope="page">
<% 
	if(!paginar || paginar && rowIdx >= pageNumber * recordsPerPage && rowIdx < (pageNumber + 1) * recordsPerPage){
%>
	<bean:define id="viewable" name="item" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
	<jsp:include page="<%=viewable.getViewerPath()%>"/>	
<% 
	}
	rowIdx++;
%>	
</logic:iterate>
</td></tr>
<%
	if(paginar){
		int	totalPages = rowIdx /recordsPerPage;
		totalPages += rowIdx % recordsPerPage == 0 ? 0 : 1;
		request.setAttribute("totalPages", new Integer(totalPages));
	}
%>