<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<c:set var="act">${param.extraAction}</c:set>
<%--<c:set var="rll_ajax">render_locations_list</c:set>  --%>
<%
	       if ("pledge_program_refresh_data".equals(request.getParameter("extraAction"))) // ajax?
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgePrograms.jsp"></jsp:include><%  
	} else if ("pledge_program_refresh_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgeProgramsAddProgram.jsp"></jsp:include><%
	} else if ("pledge_sector_refresh_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgeSectorsAddSector.jsp"></jsp:include><%
	} else if ("pledge_sector_refresh_data".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgeSectors.jsp"></jsp:include><%
	} else if ("pledge_funding_refresh_data".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgeFunding.jsp"></jsp:include><%
	} else if ("pledge_document_refresh_data".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgeDocuments.jsp"></jsp:include><%
	} else if ("pledge_document_refresh_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgeDocumentsAddDocument.jsp"></jsp:include><% 				
    } else 
    	
    { // not ajax: render the full bootstrap iframe 
%>
unrecognized action!
<%
    } // the big "otherwise"
%>
