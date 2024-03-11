<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="act">${param.extraAction}</c:set>
<%
	if ("add_locations_refresh_data".equals(request.getParameter("extraAction"))) // ajax?
	{
		%><jsp:include page="/src/main/webapp/WEB-INF/repository/aim/view/pledgeform/pledgelocationslist.jsp"></jsp:include><%
	} else if ("add_locations_refresh_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/src/main/webapp/WEB-INF/repository/aim/view/pledgeform/pledgelocationsAddPledge.jsp"></jsp:include><%
    } else 
    { // not ajax: render the full bootstrap iframe 
    	// System.out.println("rendering the full bootstrap iframe");
%>
<jsp:include page="/src/main/webapp/WEB-INF/repository/aim/view/pledgeform/pledgeFormMain.jsp"></jsp:include>
<%
    } // the big "otherwise"
%>
