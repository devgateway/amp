<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<c:set var="act">${param.extraAction}</c:set>
<%--<c:set var="rll_ajax">render_locations_list</c:set>  --%>
<%
	if ("render_programs_list".equals(request.getParameter("extraAction"))) // ajax?
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgePrograms.jsp"></jsp:include><%  
	} else if ("render_locations_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgelocationsAddPledge.jsp"></jsp:include><%
    } else 
    { // not ajax: render the full bootstrap iframe 
%>
unrecognized action!
<%
    } // the big "otherwise"
%>
